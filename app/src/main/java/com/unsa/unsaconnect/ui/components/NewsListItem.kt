package com.unsa.unsaconnect.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.data.models.NewsWithCategories
import com.unsa.unsaconnect.ui.utils.getImageForNews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListItem(
    item: NewsWithCategories,
    onClick: (NewsWithCategories) -> Unit,
    onRemoveFavorite: (() -> Unit)? = null
) {
    val timeAgo = item.news.publishedAt // Por implementar
    Card(
        onClick = { onClick(item) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = getImageForNews(item.news.id)),
                contentDescription = "Imagen de la noticia",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                if (item.categories.isNotEmpty()) {
                    Text(
                        text = item.categories[0].name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }else{
                    Log.e("NewsListItem", "No hay categorias para la noticia con id: ${item.news.id}")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.news.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = timeAgo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (onRemoveFavorite != null) {
                IconButton(
                    onClick = onRemoveFavorite,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar favorito",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Ver detalle",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
