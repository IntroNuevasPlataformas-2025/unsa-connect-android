package com.unsa.unsaconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unsa.unsaconnect.data.repositories.NewsServerBackend
import com.unsa.unsaconnect.data.repositories.NewsWithTags
//import com.unsa.unsaconnect.data.repositories.model.NewsWithTags
import com.unsa.unsaconnect.ui.components.HighlightedNewCard
import com.unsa.unsaconnect.ui.components.NewCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "UNSA Connect",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                fontFamily = FontFamily.Default
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun NewsFeed(modifier: Modifier = Modifier) {
    //OJO: obtener instancia dentro del composable
    val server = remember { NewsServerBackend.getInstance() }

    // Estado para pintar la UI
    var featuredNews by remember { mutableStateOf<List<NewsWithTags>>(emptyList()) }
    var recentNews by remember { mutableStateOf<List<NewsWithTags>>(emptyList()) }

    // Cargar datos cuando entra a la pantalla
    LaunchedEffect(Unit) {
        featuredNews = server.getFeaturedNews()              // List<NewsWithTags>
        recentNews = server.getNews(page = 0, filterTags = null) // primera página (0)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Destacados - encabezado
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Destacados",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Ver todos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Carrusel de destacados
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(featuredNews) { item ->
                HighlightedNewCard(item) // acepta NewsWithTags
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de noticias recientes
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(recentNews) { item ->
                NewCard(item) // acepta NewsWithTags
            }
        }
    }
}
