package com.javier.auris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.javier.auris.navigation.AurisNavGraph
import com.javier.auris.ui.theme.AurisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AurisTheme {
                AurisNavGraph()
            }
        }
    }
}
