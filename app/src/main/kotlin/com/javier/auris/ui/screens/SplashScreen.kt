package com.javier.auris.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javier.auris.ui.theme.Accent
import com.javier.auris.ui.theme.Background
import com.javier.auris.ui.theme.TextPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToOnboarding: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "splash_fade",
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(2500)
        onNavigateToOnboarding()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "AURIS",
            modifier = Modifier.alpha(alpha),
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            letterSpacing = 8.sp,
        )
        Text(
            text = "Relájate. Duerme. Concéntrate.",
            modifier = Modifier
                .alpha(alpha)
                .padding(top = 14.dp),
            fontSize = 15.sp,
            color = Accent,
            letterSpacing = 0.5.sp,
        )
    }
}
