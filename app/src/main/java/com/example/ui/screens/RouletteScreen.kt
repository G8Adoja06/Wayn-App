package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdventureMode
import com.example.data.model.AdventureRequest
import com.example.data.model.SecondaryMood
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricOrange
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightElevated
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MintGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOffWhite
import com.example.ui.theme.WarmYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class RouletteSegment(
    val name: String,
    val emoji: String,
    val color: Color,
    val mode: AdventureMode,
    val secondaryMood: SecondaryMood
)

@Composable
fun RouletteScreen(
    currentCity: String,
    onBack: () -> Unit,
    onSpinFinished: (AdventureRequest) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Segments: CHAOS, FOOD, CHILL, BROKE, ADVENTURE, MYSTERY, WILD CARD (DATE IS REMOVED!)
    val segments = remember {
        listOf(
            RouletteSegment("CHAOS", "🔥", ElectricOrange, AdventureMode.ROULETTE, SecondaryMood.CHAOS),
            RouletteSegment("FOOD", "🍔", WarmYellow, AdventureMode.FOOD, SecondaryMood.CHILL),
            RouletteSegment("CHILL", "🌊", MintGreen, AdventureMode.ROULETTE, SecondaryMood.CHILL),
            RouletteSegment("BROKE", "💸", CoralRed, AdventureMode.BROKE, SecondaryMood.CHAOS),
            RouletteSegment("ADVENTURE", "⚡", Color(0xFF00D2D3), AdventureMode.ROULETTE, SecondaryMood.ADVENTURE),
            RouletteSegment("MYSTERY", "🕵️", Color(0xFFA29BFE), AdventureMode.ROULETTE, SecondaryMood.MYSTERY),
            RouletteSegment("WILD CARD", "🎲", Color(0xFFFF7675), AdventureMode.ROULETTE, SecondaryMood.WILD_CARD)
        )
    }

    val rotation = remember { Animatable(0f) }
    var isSpinning by remember { mutableStateOf(false) }
    var selectedSegment by remember { mutableStateOf<RouletteSegment?>(null) }
    var showResultModal by remember { mutableStateOf(false) }

    fun spinWheel() {
        if (isSpinning) return
        isSpinning = true
        showResultModal = false
        selectedSegment = null

        coroutineScope.launch {
            // Pick a random target segment
            val winningIndex = Random.nextInt(segments.size)
            val segmentAngle = 360f / segments.size
            val winningCenter = winningIndex * segmentAngle + segmentAngle / 2f
            // Pointer is at TopCenter (270 degrees on Compose canvas).
            // Rotation required to bring winningCenter to 270 degrees:
            val extraAngle = (270f - winningCenter + 360f) % 360f
            val targetRotation = 360f * 6 + extraAngle

            rotation.snapTo(0f)
            rotation.animateTo(
                targetValue = targetRotation,
                animationSpec = tween(durationMillis = 3500, easing = FastOutSlowInEasing)
            )

            selectedSegment = segments[winningIndex]
            isSpinning = false
            showResultModal = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP BAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("roulette_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextOffWhite
                    )
                }

                Text(
                    text = "WAYN ROULETTE",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = WarmYellow,
                    letterSpacing = 1.sp
                )

                Box(modifier = Modifier.size(40.dp))
            }

            // HEADER INFO
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "عالحظ (Let Fate Decide)",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = TextOffWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Spin the wheel. Commit to the outcome without excuses.",
                    fontSize = 14.sp,
                    color = TextMuted
                )
            }

            // WHEEL GRAPHIC
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(280.dp)
                    .clip(CircleShape)
            ) {
                // Spinning Wheel Canvas
                Canvas(
                    modifier = Modifier
                        .size(270.dp)
                        .rotate(rotation.value)
                ) {
                    val sweep = 360f / segments.size
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.minDimension / 2
                    val textPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 36f
                        textAlign = android.graphics.Paint.Align.CENTER
                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                    }

                    segments.forEachIndexed { i, seg ->
                        val startAngle = i * sweep
                        drawArc(
                            color = seg.color,
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = true
                        )
                        // Slice divider
                        drawArc(
                            color = MidnightBackground,
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = true,
                            style = Stroke(width = 3.dp.toPx())
                        )
                        // Draw segment emoji
                        val midAngleRad = Math.toRadians((startAngle + sweep / 2.0))
                        val textRadius = radius * 0.68
                        val textX = (center.x + textRadius * Math.cos(midAngleRad)).toFloat()
                        val textY = (center.y + textRadius * Math.sin(midAngleRad) + 12f).toFloat()
                        drawContext.canvas.nativeCanvas.drawText(seg.emoji, textX, textY, textPaint)
                    }

                    // Outer ring
                    drawCircle(
                        color = MidnightElevated,
                        radius = radius,
                        style = Stroke(width = 8.dp.toPx())
                    )
                }

                // Center hub
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(MidnightSurface)
                        .border(3.dp, WarmYellow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = null,
                        tint = WarmYellow,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Fixed Pointer at Top (pointing down at 270 degrees)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 2.dp)
                        .size(26.dp, 30.dp)
                        .clip(RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp, topStart = 4.dp, topEnd = 4.dp))
                        .background(CoralRed)
                        .border(2.dp, TextOffWhite, RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp, topStart = 4.dp, topEnd = 4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("▼", color = TextOffWhite, fontSize = 13.sp, fontWeight = FontWeight.Black)
                }
            }

            // REVEAL RESULT BANNER OR SPIN BUTTON
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = showResultModal && selectedSegment != null,
                    enter = fadeIn() + scaleIn()
                ) {
                    selectedSegment?.let { result ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .border(2.dp, result.color, RoundedCornerShape(20.dp)),
                            colors = CardDefaults.cardColors(containerColor = MidnightCard)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${result.emoji} ${result.name} MODE ACTIVATED",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = result.color
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Fate has spoken. Preparing your adventure...",
                                    fontSize = 13.sp,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = {
                                        val req = AdventureRequest(
                                            location = currentCity,
                                            groupSize = "3",
                                            budget = if (result.mode == AdventureMode.BROKE) "$0" else "$10",
                                            duration = "1 hour",
                                            mode = result.mode,
                                            secondaryMood = result.secondaryMood
                                        )
                                        onSpinFinished(req)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = result.color),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("roulette_start_mission_button")
                                ) {
                                    Text(
                                        text = "LAUNCH MISSION 🔥",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MidnightBackground
                                    )
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = { spinWheel() },
                    enabled = !isSpinning,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .testTag("roulette_spin_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = if (isSpinning) "SPINNING..." else "SPIN THE WHEEL 🎰",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
