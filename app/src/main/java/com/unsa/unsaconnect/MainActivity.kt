package com.unsa.unsaconnect

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.unsa.unsaconnect.data.repositories.NewsServerBackend
import com.unsa.unsaconnect.ui.screens.NewsFeed
import com.unsa.unsaconnect.ui.screens.NewsTopBar
import com.unsa.unsaconnect.ui.theme.UnsaConnectTheme

class MainActivity : ComponentActivity() {

    // NO uses @Transient aquí; no hace falta y puede confundir.
    private lateinit var server: NewsServerBackend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1) Inicializa ANTES del UI
        server = NewsServerBackend.initialize(applicationContext)
        Log.d("SERVER_INIT", "Servidor inicializado")
        val res = server.seedDefaultData()


        Log.d("INFORMATION", server.getNews(0).toString())
        // 3) UI
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
