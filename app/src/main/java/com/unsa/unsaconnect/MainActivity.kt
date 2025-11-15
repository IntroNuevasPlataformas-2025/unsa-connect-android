package com.unsa.unsaconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.unsa.unsaconnect.ui.navigation.Navigation
import com.unsa.unsaconnect.ui.theme.UnsaConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            UnsaConnectTheme {
                Navigation()
            }
        }
    }
}
