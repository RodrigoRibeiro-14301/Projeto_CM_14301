package com.example.campo.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.campo.ui.theme.CampoTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campo.ui.theme.*
import androidx.compose.ui.res.stringResource
import com.example.campo.R

@Composable
fun SplashScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ball_rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "⚽",
                fontSize = 72.sp,
                modifier = Modifier.rotate(rotation)
            )
            Text(
                text = stringResource(R.string.app_display_name),
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                letterSpacing = 4.sp
            )
            Text(
                text = stringResource(R.string.splash_subtitle),
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    CampoTheme {
        SplashScreen()
    }
}
