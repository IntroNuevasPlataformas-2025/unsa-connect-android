package com.unsa.unsaconnect.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable  
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController  
import com.unsa.unsaconnect.ui.viewmodels.DetailNewViewModel
import com.unsa.unsaconnect.ui.viewmodels.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
/**
 * @brief Pantalla de detalle de noticia.
 * Permite navegar a la pantalla de imagen completa al hacer click en la imagen principal.
 */
@Composable
fun DetailNewScreen(
    navController: NavHostController,  
    viewModel: DetailNewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoritesViewModel: FavoritesViewModel = hiltViewModel()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {  
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.error != null) {
            Text(text = "Error: ${uiState.error}")
        } else {
            uiState.news?.let { item ->

                val newsId = item.news.id
                val isFavorite = item.news.isFavorite

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Image(
                        painter = painterResource(id = item.news.image),
                        contentDescription = "Imagen de la noticia",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {  
                                navController.navigate("full_image/${item.news.image}")  
                            },
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = item.categories[0].name + " • Publicado • " + item.news.publishedAt,
                        color = Color(0xFF9C544A),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.news.title,
                        color = Color(0xFF1C0F0D),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Por " + item.news.author + " • " + item.news.source,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (isFavorite) {
                                favoritesViewModel.removeFavorite(newsId) // Quitar de favoritos
                            } else {
                                favoritesViewModel.addFavorite(newsId) // Agregar a favoritos
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.4f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF5E8E8)
                        )
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = "Save Icon",
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                            Text(text = "Save", color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = item.news.content,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
    // Ejemplo de navegación al hacer click en la imagen principal:
    // Image(
    //     modifier = Modifier.clickable {
    //         navController.navigate("full_image/{imageResId}")
    //     }
    // )
    // TODO: Permitir navegación con enlaces externos en futuras versiones.
}
