package com.unsa.unsaconnect.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.unsa.unsaconnect.R
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.data.repositories.NewsWithTags

@Composable
fun HighlightedNewCard(new: NewsWithTags) {
    val images: List<Int> = listOf(
        R.drawable.image_1,
        R.drawable.image_2,
        R.drawable.image_3
    )
    Column(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        // Acceso a imagen en res (temporal)
        Image(
            painter = painterResource(id = images[(0..2).random()]),
            contentDescription = "Imagen de la noticia",
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = new.news.title, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = new.tags[0].name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondary
        ) //Ahora trabaja con categoría, aunque debería ser "tipo" para eventos o noticias
    }
}