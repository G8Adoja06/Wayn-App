package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricOrange
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOffWhite
import com.example.ui.theme.WarmYellow

@Composable
fun SplashScreen() {
    val scale = remember { Animatable(0.85f) }
    val glow = remember { Animatable(0.7f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.05f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
        glow.animateTo(
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(scale.value)
        ) {
            // Large Brand Name with glowing question mark
            Text(
                text = "WAYN?",
                fontSize = 54.sp,
                fontWeight = FontWeight.Black,
                color = ElectricOrange,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Lebanese Arabic
            Text(
                text = "وين؟",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = WarmYellow
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Turn anywhere into an adventure.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextMuted
            )
        }
    }
}
