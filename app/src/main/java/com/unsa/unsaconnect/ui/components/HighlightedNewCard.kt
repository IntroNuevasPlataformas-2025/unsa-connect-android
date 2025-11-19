package com.unsa.unsaconnect.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.data.models.NewsWithCategories
import com.unsa.unsaconnect.ui.utils.getImageForNews

@Composable
fun HighlightedNewCard(
    item: NewsWithCategories) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = getImageForNews(item.news.id)),
            contentDescription = "Imagen de la noticia",
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = item.news.title, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(6.dp))

        if (item.categories.isNotEmpty()) {
            Text(
                text = item.categories[0].name, // .categories[0]
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary
            )
        } else {
            Log.e("HighlightedNewCard", "No hay categorias para la noticia con id: ${item.news.id}")
        }
    }
}
