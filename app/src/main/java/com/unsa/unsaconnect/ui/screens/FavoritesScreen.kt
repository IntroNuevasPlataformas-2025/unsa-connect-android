package com.unsa.unsaconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.unsa.unsaconnect.ui.components.NewsListItem
import com.unsa.unsaconnect.ui.navigation.Screen
import com.unsa.unsaconnect.ui.viewmodels.FavoritesViewModel
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.DismissDirection
import androidx.compose.material.rememberDismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
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
                    val newsWithCategories = viewModel.mapToNewsWithCategories(newsItem)
                    val dismissState = rememberDismissState()
                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        viewModel.removeFavorite(newsItem.id)
                    }
                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart),
                        background = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .background(Color.Red)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.White,
                                    modifier = Modifier.padding(end = 24.dp)
                                )
                            }
                        },
                        dismissContent = {
                            NewsListItem(
                                item = newsWithCategories,
                                onClick = {
                                    if (newsItem.id > 0) {
                                        navController.navigate(Screen.DetailNew.createRoute(newsItem.id))
                                    }
                                },
                                onRemoveFavorite = {
                                    viewModel.removeFavorite(newsItem.id)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}