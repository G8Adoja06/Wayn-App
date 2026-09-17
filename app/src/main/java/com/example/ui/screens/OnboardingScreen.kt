package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.ElectricOrange
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOffWhite
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.WarmYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onComplete: (name: String) -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var nameInput by remember { mutableStateOf("") }
    val selectedInterests = remember { mutableStateListOf("Food", "Adventure") }
    val focusManager = LocalFocusManager.current

    val allInterests = listOf("Food", "Adventure", "Culture", "Chill", "Cheap", "Spontaneous")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar / Skip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WAYN?",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = ElectricOrange,
                    letterSpacing = 1.sp
                )

                TextButton(
                    onClick = { onComplete(nameInput.ifBlank { "Scout" }) },
                    modifier = Modifier.testTag("onboarding_skip_button")
                ) {
                    Text(
                        text = "Skip",
                        color = TextMuted,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Step Content
            AnimatedVisibility(
                visible = step == 1,
                enter = fadeIn() + slideInVertically()
            ) {
                Column {
                    Text(
                        text = "Your city is boring only if you let it be.",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 44.sp,
                        color = TextOffWhite,
                        letterSpacing = (-0.5).sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "No generic lists. No fake reviews. Real-world playable mini-adventures tailored to your budget, time, and squad.",
                        fontSize = 16.sp,
                        color = TextMuted,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(WarmYellow)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Beirut & Mediterranean energy",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WarmYellow
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = step == 2,
                enter = fadeIn() + slideInVertically()
            ) {
                Column {
                    Text(
                        text = "What should we call you?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = TextOffWhite
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Your alias for the leaderboards and squad missions.",
                        fontSize = 15.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        placeholder = { Text("e.g. Jad, Maya, Karim", color = TextSubtle) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("onboarding_name_input"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricOrange,
                            unfocusedBorderColor = BorderStroke,
                            focusedTextColor = TextOffWhite,
                            unfocusedTextColor = TextOffWhite,
                            focusedContainerColor = MidnightCard,
                            unfocusedContainerColor = MidnightCard
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "What's your vibe?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextOffWhite
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        allInterests.forEach { interest ->
                            val isSelected = selectedInterests.contains(interest)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) ElectricOrange else MidnightCard)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) ElectricOrange else BorderStroke,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable {
                                        if (isSelected) selectedInterests.remove(interest)
                                        else selectedInterests.add(interest)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = interest,
                                        color = if (isSelected) Color.White else TextOffWhite,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom CTA Button
            Column {
                if (step == 1) {
                    Button(
                        onClick = { step = 2 },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .testTag("onboarding_lets_go_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            text = "LET’S GO",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                } else {
                    Button(
                        onClick = { onComplete(nameInput.ifBlank { "Scout" }) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .testTag("onboarding_enter_app_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            text = "START ADVENTURING",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}
