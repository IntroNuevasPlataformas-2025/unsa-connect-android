package com.unsa.unsaconnect.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.unsa.unsaconnect.data.models.New

// No se está controlando desbordamiento de texto aún, en caso de que el título sea muy largo.
@Composable
fun NewCard(new: New) {
    val timeAgo = new.publishedAt // Por implementar
    Row(
        modifier = Modifier
            .fillMaxSize()
            .height(100.dp)
    ) {
        // Acceso a imagen en res (temporal)
        Image(
            painter = painterResource(id = new.image),
            contentDescription = "Imagen de la noticia",
            modifier = Modifier
                .height(100.dp)
                .width(150.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = new.categories[0],
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondary
            ) //Ahora trabaja con categoría, aunque debería ser "tipo" para eventos o noticias
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = new.title, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = timeAgo, style = MaterialTheme.typography.bodyMedium)
        }
    }
}