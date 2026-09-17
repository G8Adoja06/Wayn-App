package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.CityData
import com.example.data.model.AdventureMode
import com.example.data.model.DailyQuest
import com.example.data.model.Mission
import com.example.data.model.UserProfile
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricOrange
import com.example.ui.theme.ElectricOrangeGlow
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightElevated
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.MintGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOffWhite
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.WarmYellow

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    selectedCity: String,
    activeMission: Mission?,
    dailyQuest: DailyQuest,
    onCitySelected: (String) -> Unit,
    onStartMode: (AdventureMode) -> Unit,
    onOpenRoulette: () -> Unit,
    onResumeMission: () -> Unit,
    onCompleteDailyQuest: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenLogbook: () -> Unit = {}
) {
    var showCityDialog by remember { mutableStateOf(false) }

    val pulseAnim = rememberInfiniteTransition(label = "pulse")
    val glowScale by pulseAnim.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = { Text("Select City / Area", color = TextOffWhite, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Choose your adventure playground:", color = TextMuted, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    CityData.CITIES.forEach { city ->
                        val isSelected = city.equals(selectedCity, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) ElectricOrangeGlow else MidnightElevated)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) ElectricOrange else BorderStroke,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    onCitySelected(city)
                                    showCityDialog = false
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📍 $city",
                                    color = if (isSelected) ElectricOrange else TextOffWhite,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                                if (isSelected) {
                                    Text("Active", color = ElectricOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) {
                    Text("Close", color = ElectricOrange)
                }
            },
            containerColor = MidnightCard
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp)
    ) {
        // TOP APP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MidnightElevated)
                    .border(1.5.dp, ElectricOrange, CircleShape)
                    .clickable { onOpenProfile() }
                    .testTag("home_avatar_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(text = userProfile.avatarEmoji, fontSize = 22.sp)
            }

            // Location Chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MidnightElevated)
                    .border(1.dp, BorderStroke, RoundedCornerShape(20.dp))
                    .clickable { showCityDialog = true }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .testTag("home_location_chip"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "📍 $selectedCity",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextOffWhite
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Change Location",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Notification Bell / Alert Icon
            IconButton(
                onClick = onOpenNotifications,
                modifier = Modifier.testTag("home_notifications_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Alerts",
                    tint = WarmYellow
                )
            }
        }

        // HERO BANNER WITH ATMOSPHERIC CITY ARTWORK
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(190.dp)
                .clip(RoundedCornerShape(26.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.hero_beirut_night),
                contentDescription = "Beirut Nightlife Art",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dark gradient overlay for perfect contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x660C0C14),
                                Color(0xEE0C0C14)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "WAYN?",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = ElectricOrange,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "وين نروح؟",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmYellow
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "“Turn anywhere into an adventure.”",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextOffWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // RESUME MISSION CARD (IF MISSION IS ACTIVE!)
        AnimatedVisibility(visible = activeMission != null && !activeMission.completed) {
            activeMission?.let { mission ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .border(1.5.dp, ElectricOrange, RoundedCornerShape(22.dp))
                        .clickable { onResumeMission() }
                        .testTag("home_resume_mission_card"),
                    colors = CardDefaults.cardColors(containerColor = MidnightCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ElectricOrange)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "MISSION IN PROGRESS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = ElectricOrange,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = mission.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextOffWhite
                            )
                            Text(
                                text = "⏱️ Time left: ${mission.formattedRemainingTime()} • Quest ${mission.currentQuestIndex + 1}/${mission.quests.size}",
                                fontSize = 12.sp,
                                color = WarmYellow
                            )
                        }

                        Button(
                            onClick = onResumeMission,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("home_resume_mission_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("RESUME", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PRIMARY CTA: "WE'RE BORED."
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(glowScale)
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, ElectricOrange, RoundedCornerShape(24.dp))
                    .clickable { onStartMode(AdventureMode.BORED) }
                    .testTag("home_were_bored_cta"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF221510))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "WE’RE BORED.",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "“No plans? Good.”",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = WarmYellow
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(ElectricOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Turn your time, budget, and squad into a customized live adventure quest right now.",
                        fontSize = 13.sp,
                        color = TextMuted,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // SECONDARY CTA: "SURPRISE ME" (ROULETTE)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, BorderStroke, RoundedCornerShape(18.dp))
                .clickable { onOpenRoulette() }
                .testTag("home_surprise_me_cta"),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MidnightElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = null,
                            tint = WarmYellow,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "SURPRISE ME",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextOffWhite
                        )
                        Text(
                            text = "Spin the Wayn? roulette wheel",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                Text(
                    text = "SPIN 🎰",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = WarmYellow
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ADVENTURE LOGBOOK QUICK ACCESS
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, MintGreen.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .clickable { onOpenLogbook() }
                .testTag("home_logbook_cta"),
            colors = CardDefaults.cardColors(containerColor = MidnightSurface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MintGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🗺️", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "ADVENTURE LOGBOOK",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = TextOffWhite
                        )
                        Text(
                            text = "Past missions, verified photos & budget tracker",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                Text(
                    text = "VIEW 📖",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MintGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // SECTION: "How are we feeling?" (ONLY 3 CARDS: BROKE, DATE, FOOD)
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "How are we feeling?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextOffWhite
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Horizontally scrollable distinct mode cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. BROKE (على الصفر)
                ModeCard(
                    title = "BROKE",
                    arabicTitle = "على الصفر",
                    subtitle = "Zero budget. Pure street hustle.",
                    badge = "$0 - $5",
                    accentColor = CoralRed,
                    emoji = "💸",
                    testTag = "mode_card_broke",
                    onClick = { onStartMode(AdventureMode.BROKE) }
                )

                // 2. DATE (مغروم)
                ModeCard(
                    title = "DATE",
                    arabicTitle = "مغروم",
                    subtitle = "Romantic spots & zero awkward silences.",
                    badge = "Duo & Quad",
                    accentColor = CoralRed,
                    emoji = "❤️",
                    testTag = "mode_card_date",
                    onClick = { onStartMode(AdventureMode.DATE) }
                )

                // 3. FOOD (جوعان)
                ModeCard(
                    title = "FOOD",
                    arabicTitle = "جوعان",
                    subtitle = "Shawarma, knafeh & late night snacks.",
                    badge = "Quick & Feasts",
                    accentColor = WarmYellow,
                    emoji = "🍔",
                    testTag = "mode_card_food",
                    onClick = { onStartMode(AdventureMode.FOOD) }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // SECTION: DAILY QUEST
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "DAILY QUEST",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextOffWhite
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, if (dailyQuest.completed) MintGreen else BorderStroke, RoundedCornerShape(20.dp))
                    .testTag("home_daily_quest_card"),
                colors = CardDefaults.cardColors(containerColor = MidnightCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = dailyQuest.emoji, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = dailyQuest.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextOffWhite
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dailyQuest.description,
                            fontSize = 13.sp,
                            color = TextMuted,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "+${dailyQuest.rewardXp} XP Reward • ${dailyQuest.locationTarget}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = WarmYellow
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    if (dailyQuest.completed) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = MintGreen,
                                modifier = Modifier.size(32.dp)
                            )
                            Text("DONE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MintGreen)
                        }
                    } else {
                        Button(
                            onClick = onCompleteDailyQuest,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("home_complete_daily_quest_button")
                        ) {
                            Text("CLAIM", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SECTION: CURRENT STREAK
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, BorderStroke, RoundedCornerShape(20.dp))
                .testTag("home_streak_card"),
            colors = CardDefaults.cardColors(containerColor = MidnightCard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF381508)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = ElectricOrange,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "🔥 ${userProfile.currentStreak} DAY STREAK",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = TextOffWhite
                        )
                        Text(
                            text = "Next milestone: 7 Days (+250 XP & Special Badge)",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                Text(
                    text = "ON FIRE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricOrange
                )
            }
        }
    }
}

@Composable
private fun ModeCard(
    title: String,
    arabicTitle: String,
    subtitle: String,
    badge: String,
    accentColor: Color,
    emoji: String,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clip(RoundedCornerShape(22.dp))
            .border(1.dp, BorderStroke, RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = MidnightCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = emoji, fontSize = 28.sp)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = TextOffWhite
            )

            Text(
                text = arabicTitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = WarmYellow
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextMuted,
                lineHeight = 16.sp
            )
        }
    }
}
