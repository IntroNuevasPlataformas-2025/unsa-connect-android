package com.unsa.unsaconnect

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unsa.unsaconnect.ui.theme.UnsaConnectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar() {
    CenterAlignedTopAppBar(
        title = {Text(
            text = "UNSA Connect",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            fontFamily = FontFamily.Default
        )},
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun NewsFeed(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        //Destacados
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Destacados",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            //Debería tener funcionalidad al presionar
            Text(
                text = "Ver todos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(highlightedNewsList) { new ->
                HighlightedNewCard(new)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Lista de noticias
        /*
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(newsList) { news ->
                NewsListItem(news)
            }
        }*/
    }
}

@Composable
fun HighlightedNewCard(new: New) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        // Acceso a imagen en res (temporal)
        Image(
            painter = painterResource(id = new.image),
            contentDescription = "Imagen de la noticia",
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = new.title, style = MaterialTheme.typography.bodyMedium)
    }
}

// Data Class para las noticias
// Aún no existe Data Class para las etiquetas/categorías
data class New(
    val id: Int,
    val title: String,
    val categories: List<String>, // Debería ser una lista de categorías (otra clase)
    val content: String,
    val image: Int, // Temporalmente solo se trabaja con imágenes en local
    val publishedAt: String, // Utilizar Date o LocalDateTime en las fechas (especialmente para calcular el "Time Ago")
    val createdAt: String,
    val updatedAt: String,
)

val highlightedNewsList = listOf(
    New(
        id = 1,
        title = "Convocatoria a Becas de Investigación 2025",
        categories = listOf(
            "Becas"
        ),
        content = "La universidad anuncia la apertura de las becas de investigación para el año académico 2025. Se invita a los estudiantes de pregrado y posgrado a postular con sus proyectos.",
        image = R.drawable.image_1,
        publishedAt = "2025-10-14T09:00:00",
        createdAt = "2025-10-14T09:00:00",
        updatedAt = "2025-10-14T09:00:00"
    ),
    New(
        id = 2,
        title = "Congreso Internacional de Ingeniería 2025",
        categories = listOf(
            "Congresos"
        ),
        content = "Se realizará el Congreso Internacional de Ingeniería en la ciudad de Arequipa del 15 al 17 de noviembre. Se contará con ponentes nacionales e internacionales.",
        image = R.drawable.image_2,
        publishedAt = "2025-09-25T10:00:00",
        createdAt = "2025-10-14T09:00:00",
        updatedAt = "2025-10-14T09:00:00"
    ),
    New(
        id = 3,
        title = "Suspensión de Clases por Mantenimiento",
        categories = listOf(
            "General"
        ),
        content = "Debido a trabajos de mantenimiento en el campus central, las clases del 20 de octubre se suspenden para todas las facultades.",
        image = R.drawable.image_3,
        publishedAt = "2025-10-14T09:00:00Z",
        createdAt = "2025-10-14T09:00:00Z",
        updatedAt = "2025-10-14T09:00:00Z"
    ),
)