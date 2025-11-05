package com.unsa.unsaconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.unsa.unsaconnect.ui.screens.NewsFeed
import com.unsa.unsaconnect.ui.screens.NewsTopBar
import com.unsa.unsaconnect.ui.theme.UnsaConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            UnsaConnectTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { NewsTopBar() }
                ) { innerPadding ->
                    NewsFeed(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
