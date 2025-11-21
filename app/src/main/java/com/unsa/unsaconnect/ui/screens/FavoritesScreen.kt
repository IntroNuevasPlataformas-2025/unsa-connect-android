package com.unsa.unsaconnect.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.unsa.unsaconnect.ui.viewmodels.FavoritesViewModel

// El TopBar no necesita cambios, solo lo incluimos para el Scaffold
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Mis Favoritos",
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
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    // Inyección del ViewModel (similar a NewsFeed)
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    // Observación del estado (similar a NewsFeed)
    val uiState by viewModel.uiState.collectAsState()

    // --- Lógica de Debugging (Simulando la carga/display de datos) ---

    // Ejecutamos la lógica de logging cuando el estado cambia.
    // Usamos LaunchedEffect para ejecutar efectos secundarios basados en el estado.
    LaunchedEffect(uiState.isLoading, uiState.favoritesNews) {
        if (!uiState.isLoading) {
            val newsCount = uiState.favoritesNews.size
            val debugTag = "FavoritesDebug"

            Log.d(debugTag, "--- Estado de Favoritos Actualizado ---")
            Log.d(debugTag, "Total de noticias favoritas cargadas: $newsCount")

            if (newsCount >= 0) {
//                uiState.favoritesNews.forEachIndexed { index, news ->
//                    // Asumo que tu modelo 'New' tiene propiedades 'id' y 'title'
//                    Log.d(debugTag, "  Noticia ${index + 1}: ID=${news.id}, Título='${news.title}'")
//                }
                Log.d(null, uiState.favoritesNews.toString())
            } else {
                Log.d(debugTag, "La lista de favoritos está vacía o hubo un error.")
            }
        } else {
            Log.d("FavoritesDebug", "Cargando favoritos...")
        }
    }

    // --- Renderizado de la UI simple (para evitar errores) ---

    // La UI es exactamente la versión simple que funciona: TopBar y Box centrado.
    Scaffold(
        topBar = { FavoritesTopBar() }
    ) { paddingValues ->

        // Simulación del estado de Carga/Contenido, pero solo con UI simple.
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                // Muestra el indicador de carga si isLoading es true
                CircularProgressIndicator()
            }
        } else {
            // Muestra el contenido simple cuando no está cargando
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pantalla de Favoritos Lista. (Ver Logcat)",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}