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
import com.unsa.unsaconnect.data.models.New
import com.unsa.unsaconnect.data.repositories.FakeNewsRepository
import com.unsa.unsaconnect.domain.repositories.NewsRepository
import com.unsa.unsaconnect.ui.components.HighlightedNewCard
import com.unsa.unsaconnect.ui.components.NewsListItem

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
    val newsRepository: NewsRepository = remember { FakeNewsRepository() }

    var highlightedNews by remember { mutableStateOf<List<New>>(emptyList()) }
    var recentNews by remember { mutableStateOf<List<New>>(emptyList()) }

    LaunchedEffect(Unit) {
        highlightedNews = newsRepository.getHighlightedNews()
        recentNews = newsRepository.getRecentNews()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(highlightedNews) { item ->
                HighlightedNewCard(item)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(recentNews) { news ->
                NewsListItem(news, onClick = { /* TODO: Navigate to detail screen */ })
            }
        }
    }
}
