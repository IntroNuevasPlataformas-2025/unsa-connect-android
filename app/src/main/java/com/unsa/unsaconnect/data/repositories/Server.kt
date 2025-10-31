package com.unsa.unsaconnect.data.repositories

import android.content.Context
import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// =====================
// Modelos
// =====================

@Serializable
data class Role(val id: Int, val name: String, val description: String? = null)

@Serializable
data class User(
    val id: Int,
    val username: String,
    val email: String,
    val passwordHash: String,
    val createdAt: String,
    var updatedAt: String
)

@Serializable
data class UserRole(val id: Int, val userId: Int, val roleId: Int)

@Serializable
data class Tag(val id: Int, val name: String, val description: String? = null)

@Serializable
data class News(
    val id: Int,
    var title: String,
    var content: String,
    var imageUrl: String? = null,
    var publishedAt: String? = null,
    val createdAt: String,
    var updatedAt: String,
    val authorId: Int,
    var status: String = "draft"
)

@Serializable
data class NewsTag(val id: Int, val newsId: Int, val tagId: Int)

@Serializable
data class FeaturedNews(
    val id: Int,
    val newsId: Int,
    val addedAt: String
)

@Serializable
data class ApiError(val error: String)

@Serializable
data class NewsWithTags(val news: News, val tags: List<Tag>)

/** Dump completo para exportaciones */
@Serializable
data class BackendDump(
    val roles: List<Role>,
    val users: List<User>,
    val userRoles: List<UserRole>,
    val tags: List<Tag>,
    val news: List<News>,
    val newsTags: List<NewsTag>,
    val featuredNews: List<FeaturedNews>,
    val generatedAt: String
)

// =====================
// Servidor
// =====================

class NewsServerBackend private constructor(private val context: Context) {

    companion object {
        @Volatile private var INSTANCE: NewsServerBackend? = null

        fun initialize(context: Context): NewsServerBackend {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NewsServerBackend(context).also { INSTANCE = it }
            }
        }

        fun getInstance(): NewsServerBackend {
            return INSTANCE ?: throw IllegalStateException("NewsServerBackend not initialized; call initialize() first")
        }
    }

    // =====================
    // Config y paths
    // =====================
    private val AUTO_SEED_ON_FIRST_RUN = false // true si quieres sembrar automáticamente tras el wipe.

    // Directorio de trabajo
    private val baseDir = File(context.filesDir, "backend_data")
    private val firstRunFlag get() = File(baseDir, "initialized.flag")

    private val rolesFile get() = File(baseDir, "roles.txt")
    private val usersFile get() = File(baseDir, "users.txt")
    private val userRolesFile get() = File(baseDir, "user_roles.txt")
    private val tagsFile get() = File(baseDir, "tags.txt")
    private val newsFile get() = File(baseDir, "news.txt")
    private val newsTagsFile get() = File(baseDir, "news_tags.txt")
    private val featuredNewsFile get() = File(baseDir, "featured_news.txt")

    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    private val roles = mutableListOf<Role>()
    private val users = mutableListOf<User>()
    private val userRoles = mutableListOf<UserRole>()
    private val tags = mutableListOf<Tag>()
    private val news = mutableListOf<News>()
    private val newsTags = mutableListOf<NewsTag>()
    private val featuredNews = mutableListOf<FeaturedNews>()

    init {
        baseDir.mkdirs()
        handleFirstRunWipe() // limpia todo la primera vez
        copyAssetsIfNeeded()
        loadAll()
        if (AUTO_SEED_ON_FIRST_RUN && !hasSeedData()) {
            seedDefaultData()
        }

    }

    private fun nowString(): String = dateFormat.format(Date())

    // =====================
    // FIRST RUN WIPE
    // =====================
    private fun handleFirstRunWipe() {
        if (!firstRunFlag.exists()) {
            Log.w("SERVER_INIT", "Primera ejecución detectada → limpiando backend_data completamente.")
            baseDir.listFiles()?.forEach { f ->
                try { f.deleteRecursively() } catch (_: Exception) {}
            }
            baseDir.mkdirs()
            firstRunFlag.writeText("initialized@${nowString()}")
        }
    }

    // =====================
    // Copia inicial de assets
    // =====================
    private fun copyAssetsIfNeeded() {
        val assetFiles = listOf(
            "roles.txt", "users.txt", "user_roles.txt",
            "tags.txt", "news.txt", "news_tags.txt", "featured_news.txt"
        )
        for (fileName in assetFiles) {
            val destFile = File(baseDir, fileName)
            if (!destFile.exists()) {
                try {
                    context.assets.open(fileName).use { input ->
                        destFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    Log.d("SERVER_INIT", "Archivo copiado desde assets: $fileName")
                } catch (e: Exception) {
                    destFile.writeText("")
                    Log.w("SERVER_INIT", "No encontrado en assets: $fileName → creado vacío")
                }
            }
        }
    }

    // Opción para forzar sincronización (reset desde assets)
    fun forceSyncFromAssets() {
        val assetFiles = listOf(
            "roles.txt", "users.txt", "user_roles.txt",
            "tags.txt", "news.txt", "news_tags.txt", "featured_news.txt"
        )
        for (fileName in assetFiles) {
            try {
                val destFile = File(baseDir, fileName)
                context.assets.open(fileName).use { input ->
                    destFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                Log.d("SERVER_FORCE", "Archivo sobrescrito desde assets: $fileName")
            } catch (e: Exception) {
                Log.e("SERVER_FORCE", "Error sobrescribiendo $fileName: ${e.message}")
            }
        }
        loadAll()
    }

    // =====================
    // Utilidades de IO (atómicas)
    // =====================
    private fun atomicWriteText(file: File, data: String): Boolean {
        return try {
            file.parentFile?.mkdirs()
            val tmp = File(file.parentFile, file.name + ".tmp")
            tmp.writeText(data)
            if (!tmp.renameTo(file)) {
                file.delete()
                val ok = tmp.renameTo(file)
                if (!ok) tmp.delete()
                ok
            } else true
        } catch (e: Exception) {
            Log.e("SERVER_SAVE", "Atomic write failed for ${file.name}: ${e.message}")
            false
        }
    }

    // Guardar SIEMPRE como JSON array
    private inline fun <reified T> saveListAtomic(file: File, list: List<T>): Boolean {
        val payload = json.encodeToString(list)
        val ok = atomicWriteText(file, payload)
        if (ok) Log.d("SERVER_SAVE", "Archivo actualizado (atomic, JSON array): ${file.name} (${list.size} elementos)")
        return ok
    }

    // =====================
    // Carga (robusta: NDJSON o JSON array; ignora líneas corruptas)
    // =====================
    private inline fun <reified T> loadList(file: File): MutableList<T> {
        if (!file.exists()) return mutableListOf()
        val text = try { file.readText() } catch (e: Exception) { "" }.trim()
        if (text.isEmpty()) return mutableListOf()

        // Si el archivo completo es un array JSON, úsalo directamente
        if (text.first() == '[') {
            return try {
                json.decodeFromString<List<T>>(text).toMutableList()
            } catch (e: Exception) {
                Log.e("SERVER_LOAD", "Fallo parseando JSON array en ${file.name}: ${e.message}")
                mutableListOf()
            }
        }

        // Modo NDJSON: un objeto por línea (solo funciona si cada objeto está en UNA línea)
        val out = mutableListOf<T>()
        val lines = text.lines()
        for ((i, raw) in lines.withIndex()) {
            val s = raw.trim()
            if (s.isEmpty()) continue
            try {
                out.add(json.decodeFromString<T>(s))
            } catch (e: Exception) {
                Log.e("SERVER_LOAD", "Línea corrupta en ${file.name} (línea ${i + 1}): '$s' → ${e.message}")
                // Se ignora la línea corrupta y continúa
            }
        }
        return out
    }

    private fun loadAll() {
        roles.clear(); users.clear(); userRoles.clear(); tags.clear(); news.clear(); newsTags.clear(); featuredNews.clear()
        roles.addAll(loadList(rolesFile))
        users.addAll(loadList(usersFile))
        userRoles.addAll(loadList(userRolesFile))
        tags.addAll(loadList(tagsFile))
        news.addAll(loadList(newsFile))
        newsTags.addAll(loadList(newsTagsFile))
        featuredNews.addAll(loadList(featuredNewsFile))
        Log.d("SERVER_INIT", "Datos cargados correctamente desde backend_data")
    }

    // Regraba los archivos actuales como ARREGLOS JSON (opcional, ejecútala 1 vez si migraste desde NDJSON).
    fun rewriteAllAsArraysOnce() {
        saveListAtomic(rolesFile, roles)
        saveListAtomic(usersFile, users)
        saveListAtomic(userRolesFile, userRoles)
        saveListAtomic(tagsFile, tags)
        saveListAtomic(newsFile, news)
        saveListAtomic(newsTagsFile, newsTags)
        saveListAtomic(featuredNewsFile, featuredNews)
        Log.d("SERVER_SAVE", "Todos los archivos reescritos como arreglos JSON.")
    }

    // =====================
    // Helpers de validación
    // =====================
    private fun isAdmin(userId: Int): Boolean =
        userRoles.any { ur -> ur.userId == userId && roles.find { r -> r.id == ur.roleId }?.name == "admin" }

    private fun newsExistsAndActive(id: Int): Boolean =
        news.any { it.id == id && it.status != "archived" }

    private fun nextIdOf(currentMax: Int?) = (currentMax ?: 0) + 1

    private fun hasSeedData(): Boolean =
        roles.isNotEmpty() || users.isNotEmpty() || tags.isNotEmpty() || news.isNotEmpty()

    // =====================
    // SEED + EXPORT JSON
    // =====================

    /**
     * Semilla por defecto (roles, usuarios, tags, news, relaciones y destacados)
     */
    fun seedDefaultData(): String {
        // 1) Limpiar en memoria y en disco (solo las tablas, no el flag)
        roles.clear(); users.clear(); userRoles.clear(); tags.clear(); news.clear(); newsTags.clear(); featuredNews.clear()
        saveListAtomic(rolesFile, roles)
        saveListAtomic(usersFile, users)
        saveListAtomic(userRolesFile, userRoles)
        saveListAtomic(tagsFile, tags)
        saveListAtomic(newsFile, news)
        saveListAtomic(newsTagsFile, newsTags)
        saveListAtomic(featuredNewsFile, featuredNews)

        // 2) Construir seed
        val now = nowString()

        // Roles
        val roleAdmin = Role(1, "admin", "Administrador")
        val roleUser = Role(2, "user", "Usuario estándar")
        roles.addAll(listOf(roleAdmin, roleUser))

        // Users
        val uAdmin = User(1, "admin", "admin@local", "hash(admin123)", now, now)
        val uUser  = User(2, "diego", "user@local", "hash(user123)",  now, now)
        users.addAll(listOf(uAdmin, uUser))

        // UserRoles
        userRoles.addAll(listOf(
            UserRole(1, userId = 1, roleId = 1),
            UserRole(2, userId = 2, roleId = 2)
        ))

        // Tags
        val t1 = Tag(1, "Becas")
        val t2 = Tag(2, "Noticias")
        val t3 = Tag(3, "Eventos")
        val t4 = Tag(4, "Convocatorias")
        val t5 = Tag(5, "Avisos")
        tags.addAll(listOf(t1, t2, t3, t4, t5))

        // News (10 items) — autor: admin
        fun n(id: Int, title: String, content: String, status: String = "published"): News {
            val created = nowString()
            return News(
                id = id,
                title = title,
                content = content,
                imageUrl = null,
                publishedAt = if (status == "published") created else null,
                createdAt = created,
                updatedAt = created,
                authorId = 1,
                status = status
            )
        }
        val allNews = listOf(
            n(1, "Convocatoria de Becas 2026", "Se abren inscripciones para becas de investigación.", "published"),
            n(2, "Feria de Empleo Universitaria", "Empresas locales y nacionales participarán en la feria.", "published"),
            n(3, "Aviso de Mantenimiento de Sistemas", "Habrá mantenimiento programado el fin de semana.", "published"),
            n(4, "Conferencia de Innovación", "Ponentes internacionales discutirán tendencias tecnológicas.", "published"),
            n(5, "Convocatoria: Taller de Kotlin", "Cupos limitados para el taller intensivo de Kotlin.", "published"),
            n(6, "Resultados Parciales de Becas", "Se publican resultados preliminares para revisión.", "published"),
            n(7, "Semana de la Investigación", "Eventos y charlas a lo largo de la semana.", "published"),
            n(8, "Nueva Política de Laboratorios", "Actualización de reglamento de uso de laboratorios.", "published"),
            n(9, "Lanzamiento de App Estudiantil", "Se presenta una app para gestión académica.", "published"),
            n(10, "Aviso: Corte de Energía", "Interrupción temporal en edificios A y B.", "published")
        )
        news.addAll(allNews)

        // NewsTags
        var ntId = 1
        fun link(newsId: Int, tagId: Int) { newsTags.add(NewsTag(ntId++, newsId, tagId)) }
        link(1, t1.id); link(1, t4.id)
        link(2, t3.id)
        link(3, t5.id)
        link(4, t2.id)
        link(5, t3.id); link(5, t4.id)
        link(6, t1.id)
        link(7, t3.id); link(7, t2.id)
        link(8, t2.id); link(8, t5.id)
        link(9, t2.id)
        link(10, t5.id)

        // Featured (4 noticias)
        val featuredSelection = listOf(1, 2, 7, 9)
        var featId = 1
        val nowFeat = nowString()
        featuredSelection.forEach { nid -> featuredNews.add(FeaturedNews(featId++, nid, nowFeat)) }

        // Persistir todo atómicamente
        val okR = saveListAtomic(rolesFile, roles)
        val okU = saveListAtomic(usersFile, users)
        val okUR = saveListAtomic(userRolesFile, userRoles)
        val okT = saveListAtomic(tagsFile, tags)
        val okN = saveListAtomic(newsFile, news)
        val okNT = saveListAtomic(newsTagsFile, newsTags)
        val okF = saveListAtomic(featuredNewsFile, featuredNews)

        val allOk = okR && okU && okUR && okT && okN && okNT && okF
        if (!allOk) return json.encodeToString(ApiError("Seed persistence failure"))

        val exportPath = exportAllToJson("seed_all.json")
        return json.encodeToString(mapOf("seed" to "ok", "export" to exportPath))
    }

    /**
     * Exporta el snapshot completo a un archivo JSON dentro de backend_data/.
     * @return ruta absoluta del archivo generado
     */
    fun exportAllToJson(filename: String = "dump.json"): String {
        val dump = BackendDump(
            roles = roles.toList(),
            users = users.toList(),
            userRoles = userRoles.toList(),
            tags = tags.toList(),
            news = news.toList(),
            newsTags = newsTags.toList(),
            featuredNews = featuredNews.toList(),
            generatedAt = nowString()
        )
        val file = File(baseDir, filename)
        val payload = json.encodeToString(dump)
        file.writeText(payload)
        Log.d("SERVER_EXPORT", "Dump generado: ${file.absolutePath}")
        return file.absolutePath
    }

    // =====================
    // NEWS (CRUD + LECTURAS)
    // =====================

    fun createNews(
        userId: Int,
        title: String,
        content: String,
        imageUrl: String? = null,
        tagNames: List<String> = emptyList()
    ): String {
        val user = users.find { it.id == userId } ?: return json.encodeToString(ApiError("User not found"))
        if (!isAdmin(userId)) return json.encodeToString(ApiError("User not authorized"))
        if (title.isBlank() || content.isBlank()) return json.encodeToString(ApiError("Title and content required"))

        val newsSnap = news.toMutableList()
        val tagsSnap = tags.toMutableList()
        val newsTagsSnap = newsTags.toMutableList()

        val newId = nextIdOf(newsSnap.maxOfOrNull { it.id })
        val now = nowString()
        val newItem = News(newId, title.trim(), content.trim(), imageUrl, null, now, now, userId, "published")
        newsSnap.add(newItem)

        tagNames.forEach { raw ->
            val name = raw.trim()
            if (name.isNotEmpty()) {
                var tag = tagsSnap.find { it.name.equals(name, true) }
                if (tag == null) {
                    tag = Tag(nextIdOf(tagsSnap.maxOfOrNull { it.id }), name)
                    tagsSnap.add(tag)
                }
                val link = NewsTag(nextIdOf(newsTagsSnap.maxOfOrNull { it.id }), newId, tag.id)
                newsTagsSnap.add(link)
            }
        }

        val okNews = saveListAtomic(newsFile, newsSnap)
        val okTags = saveListAtomic(tagsFile, tagsSnap)
        val okLinks = saveListAtomic(newsTagsFile, newsTagsSnap)

        return if (okNews && okTags && okLinks) {
            news.clear(); news.addAll(newsSnap)
            tags.clear(); tags.addAll(tagsSnap)
            newsTags.clear(); newsTags.addAll(newsTagsSnap)
            json.encodeToString(newItem)
        } else {
            json.encodeToString(ApiError("Persistence failure: transaction rolled back"))
        }
    }

    fun updateNews(userId: Int, newsId: Int, newTitle: String?, newContent: String?): String {
        val user = users.find { it.id == userId } ?: return json.encodeToString(ApiError("User not found"))
        if (!isAdmin(userId)) return json.encodeToString(ApiError("User not authorized"))

        val current = news.find { it.id == newsId } ?: return json.encodeToString(ApiError("News not found"))

        val newsSnap = news.toMutableList()
        val idx = newsSnap.indexOfFirst { it.id == newsId }
        if (idx < 0) return json.encodeToString(ApiError("News not found"))

        val updated = newsSnap[idx].copy(
            title = newTitle ?: newsSnap[idx].title,
            content = newContent ?: newsSnap[idx].content,
            updatedAt = nowString()
        )
        newsSnap[idx] = updated

        val ok = saveListAtomic(newsFile, newsSnap)
        return if (ok) {
            news.clear(); news.addAll(newsSnap)
            json.encodeToString(updated)
        } else {
            json.encodeToString(ApiError("Persistence failure updating news"))
        }
    }

    fun deleteNews(userId: Int, newsId: Int): String {
        val user = users.find { it.id == userId } ?: return json.encodeToString(ApiError("User not found"))
        if (!isAdmin(userId)) return json.encodeToString(ApiError("User not authorized"))

        val current = news.find { it.id == newsId } ?: return json.encodeToString(ApiError("News not found"))

        val newsSnap = news.toMutableList()
        val idx = newsSnap.indexOfFirst { it.id == newsId }
        if (idx < 0) return json.encodeToString(ApiError("News not found"))

        val archived = newsSnap[idx].copy(status = "archived", updatedAt = nowString())
        newsSnap[idx] = archived

        val ok = saveListAtomic(newsFile, newsSnap)
        return if (ok) {
            news.clear(); news.addAll(newsSnap)
            json.encodeToString(archived)
        } else {
            json.encodeToString(ApiError("Persistence failure deleting news"))
        }
    }

    /** Listado paginado + filtro OR por tags por nombre */
    fun getNews(page: Int, filterTags: List<String>? = null): List<NewsWithTags> {
        val pageSize = 5
        var visible = news.filter { it.status != "archived" }

        filterTags?.let { names ->
            val lower = names.map { it.lowercase() }
            val tagIds = tags.filter { it.name.lowercase() in lower }.map { it.id }
            visible = visible.filter { n ->
                val rel = newsTags.filter { it.newsId == n.id }.map { it.tagId }
                rel.any { it in tagIds }
            }
        }

        val sorted = visible.sortedByDescending { it.createdAt }
        val from = page * pageSize
        val to = (from + pageSize).coerceAtMost(sorted.size)
        val sub = if (from < sorted.size) sorted.subList(from, to) else emptyList()

        return sub.map { n ->
            val relatedTags = newsTags.filter { it.newsId == n.id }.mapNotNull { nt ->
                tags.find { it.id == nt.tagId }
            }
            NewsWithTags(n, relatedTags)
        }
    }

    /** Una noticia con sus tags */
    fun getNewsById(newsId: Int): NewsWithTags? {
        val item = news.find { it.id == newsId } ?: return null
        val tagList = newsTags.filter { it.newsId == newsId }.mapNotNull { nt ->
            tags.find { it.id == nt.tagId }
        }
        return NewsWithTags(item, tagList)
    }

    /** Noticias por autor (no archivadas) */
    fun getNewsByUser(userId: Int): List<NewsWithTags> {
        val userItems = news.filter { it.authorId == userId && it.status != "archived" }
        return userItems.map { n ->
            val relatedTags = newsTags.filter { it.newsId == n.id }.mapNotNull { nt ->
                tags.find { it.id == nt.tagId }
            }
            NewsWithTags(n, relatedTags)
        }
    }

    // =====================
    // FEATURED_NEWS
    // =====================

    fun addFeaturedNews(ids: List<Int>): String {
        if (ids.isEmpty()) return json.encodeToString(ApiError("No ids provided"))
        val invalidIds = ids.filter { !newsExistsAndActive(it) }
        if (invalidIds.isNotEmpty()) {
            return json.encodeToString(ApiError("Invalid or archived news ids: $invalidIds"))
        }

        val featuredSnap = featuredNews.toMutableList()
        val existingNewsIds = featuredSnap.map { it.newsId }.toSet()
        val toAdd = ids.distinct().filter { it !in existingNewsIds }

        if (toAdd.isEmpty()) {
            return json.encodeToString(mapOf("added" to emptyList<Int>(), "skipped" to ids))
        }

        val now = nowString()
        var nextId = nextIdOf(featuredSnap.maxOfOrNull { it.id })
        val newRows = toAdd.map { newsId ->
            val row = FeaturedNews(nextId, newsId, now)
            nextId += 1
            row
        }

        val finalList = featuredSnap + newRows
        val ok = saveListAtomic(featuredNewsFile, finalList)
        return if (ok) {
            featuredNews.clear(); featuredNews.addAll(finalList)
            json.encodeToString(mapOf("added" to newRows.map { it.newsId }, "skipped" to ids - toAdd.toSet()))
        } else {
            json.encodeToString(ApiError("Persistence failure adding featured news"))
        }
    }

    fun eraseFeaturedNewsById(id: Int): String {
        val idx = featuredNews.indexOfFirst { it.id == id }
        if (idx < 0) return json.encodeToString(ApiError("Featured id not found"))

        val snap = featuredNews.toMutableList()
        snap.removeAt(idx)

        val ok = saveListAtomic(featuredNewsFile, snap)
        return if (ok) {
            featuredNews.clear(); featuredNews.addAll(snap)
            json.encodeToString(mapOf("deletedId" to id))
        } else {
            json.encodeToString(ApiError("Persistence failure erasing featured"))
        }
    }

    fun clearFeaturedNews(): String {
        val snap = mutableListOf<FeaturedNews>()
        val ok = saveListAtomic(featuredNewsFile, snap)
        return if (ok) {
            featuredNews.clear()
            json.encodeToString(mapOf("cleared" to true))
        } else {
            json.encodeToString(ApiError("Persistence failure clearing featured"))
        }
    }

    /** Destacadas con tags (más recientes primero) */
    fun getFeaturedNews(): List<NewsWithTags> {
        val orderedFeatured = featuredNews.sortedByDescending { it.addedAt }
        val orderedIds = orderedFeatured.map { it.newsId }

        val activeNewsById = news
            .asSequence()
            .filter { it.status != "archived" && it.id in orderedIds }
            .associateBy { it.id }

        val orderedNews = orderedIds.mapNotNull { activeNewsById[it] }

        return orderedNews.map { n ->
            val relatedTags = newsTags
                .asSequence()
                .filter { it.newsId == n.id }
                .mapNotNull { nt -> tags.find { it.id == nt.tagId } }
                .toList()
            NewsWithTags(n, relatedTags)
        }
    }

    // =====================
    // DEBUG / UTIL
    // =====================

    // (Opcional) Para ver ruta en Logcat
    fun debugFilePath() {
        Log.d("SERVER_PATH", "Ruta de news.txt → ${newsFile.absolutePath}")
        Log.d("SERVER_PATH", "Ruta de featured_news.txt → ${featuredNewsFile.absolutePath}")
    }
}
