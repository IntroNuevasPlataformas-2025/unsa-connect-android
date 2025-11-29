package com.unsa.unsaconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.ui.components.NewsListItem
import com.unsa.unsaconnect.data.models.NewsWithCategories
import com.unsa.unsaconnect.ui.navigation.Screen
import com.unsa.unsaconnect.ui.viewmodels.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.favoritesNews.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tienes favoritos guardados.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.favoritesNews, key = { it.id }) { newsItem ->
                    // Convertir New a NewsWithCategories si es necesario
                    val newsWithCategories = viewModel.mapToNewsWithCategories(newsItem)
                    NewsListItem(
                        item = newsWithCategories,
                        onClick = {
                            // Navega pasando el id solo si es válido
                            if (newsItem.id > 0) {
                                navController.navigate(Screen.DetailNew.createRoute(newsItem.id))
                            } else {
                                // Mostrar un mensaje de error o ignorar
                            }
                        },
                        onRemoveFavorite = {
                            viewModel.removeFavorite(newsItem.id)
                        }
                    )
                }
            }
        }
    }
}