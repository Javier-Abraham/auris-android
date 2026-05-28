package com.javier.auris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.javier.auris.navigation.AurisNavGraph
import com.javier.auris.ui.theme.AurisTheme
import com.javier.auris.viewmodel.SettingsViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsVm: SettingsViewModel = viewModel()
            val accentIndex by settingsVm.accentColorIndex.collectAsState()
            AurisTheme(accentColorIndex = accentIndex) {
                AurisNavGraph()
            }
        }
    }
}
