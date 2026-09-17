package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Achievement
import com.example.data.model.GroupDna
import com.example.data.model.LeaderboardEntry
import com.example.data.model.LogbookEntry
import com.example.data.model.TransportMode
import com.example.data.model.UserProfile
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

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    achievements: List<Achievement>,
    groupDna: GroupDna,
    leaderboard: List<LeaderboardEntry>,
    logbook: List<LogbookEntry> = emptyList(),
    initialTab: Int = 0,
    onOpenLeaderboard: () -> Unit,
    onQuickAddDemoXp: () -> Unit,
    onResetDemoData: () -> Unit,
    onUpdateGroupDna: (adv: Int, food: Int, cult: Int, chaos: Int, chill: Int, bud: Int) -> Unit
) {
    var selectedTab by remember(initialTab) { mutableStateOf(initialTab) } // 0: Profile & DNA, 1: Logbook, 2: Leaderboard

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 96.dp)
    ) {
        // TOP HEADER: TABS (PROFILE vs LOGBOOK vs LEADERBOARD)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selectedTab == 0) ElectricOrange else MidnightCard)
                    .clickable { selectedTab = 0 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PROFILE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedTab == 0) Color.White else TextOffWhite
                )
            }

            Box(
                modifier = Modifier
                    .weight(1.1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selectedTab == 1) ElectricOrange else MidnightCard)
                    .clickable { selectedTab = 1 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = if (selectedTab == 1) Color.White else MintGreen,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "LOGBOOK (${logbook.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 1) Color.White else TextOffWhite
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1.1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selectedTab == 2) ElectricOrange else MidnightCard)
                    .clickable { selectedTab = 2 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Leaderboard,
                        contentDescription = null,
                        tint = if (selectedTab == 2) Color.White else WarmYellow,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "RANKS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 2) Color.White else TextOffWhite
                    )
                }
            }
        }

        if (selectedTab == 0) {
            // PROFILE CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.5.dp, BorderStroke, RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = MidnightCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MidnightElevated)
                                .border(2.dp, ElectricOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = userProfile.avatarEmoji, fontSize = 32.sp)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = userProfile.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = TextOffWhite
                            )
                            Text(
                                text = "Level ${userProfile.level.levelNumber}: ${userProfile.level.title}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarmYellow
                            )
                            Text(
                                text = "Perk: ${userProfile.level.perk}",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // XP Progress
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${userProfile.totalXp} XP",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextOffWhite
                        )
                        Text(
                            text = "Next: ${userProfile.level.maxForNext} XP",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    val progress = (userProfile.totalXp.toFloat() / userProfile.level.maxForNext).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = ElectricOrange,
                        trackColor = MidnightElevated,
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🔥 ${userProfile.currentStreak}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextOffWhite)
                            Text(text = "Day Streak", fontSize = 11.sp, color = TextMuted)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🗺️ ${userProfile.adventuresCompleted}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextOffWhite)
                            Text(text = "Adventures", fontSize = 11.sp, color = TextMuted)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "⚡ ${userProfile.questsCompleted}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextOffWhite)
                            Text(text = "Quests Cleared", fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ACHIEVEMENTS SECTION
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "ACHIEVEMENTS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextOffWhite
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    achievements.forEach { ach ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .border(
                                    1.dp,
                                    if (ach.unlockedTiers.isNotEmpty()) MintGreen.copy(alpha = 0.5f) else BorderStroke,
                                    RoundedCornerShape(18.dp)
                                ),
                            colors = CardDefaults.cardColors(containerColor = MidnightCard)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = ach.emoji, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = ach.title,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Black,
                                                color = TextOffWhite
                                            )
                                            ach.currentTier?.let { tier ->
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(WarmYellow.copy(alpha = 0.2f))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "${tier.badge} ${tier.label.uppercase()}",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = WarmYellow
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = ach.description,
                                            fontSize = 12.sp,
                                            color = TextMuted
                                        )
                                    }
                                    Text(
                                        text = "+${ach.xpReward} XP",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = WarmYellow
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // TIERS ROW: BRONZE, SILVER, GOLD
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val isBronze = ach.unlockedTiers.contains(com.example.data.model.AchievementTier.BRONZE)
                                    val isSilver = ach.unlockedTiers.contains(com.example.data.model.AchievementTier.SILVER)
                                    val isGold = ach.unlockedTiers.contains(com.example.data.model.AchievementTier.GOLD)

                                    TierChip("🥉 Bronze", ach.bronzeReq, isBronze, Modifier.weight(1f))
                                    TierChip("🥈 Silver", ach.silverReq, isSilver, Modifier.weight(1f))
                                    TierChip("🥇 Gold", ach.goldReq, isGold, Modifier.weight(1f))
                                }

                                if (ach.nextTierDesc.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Next: ${ach.nextTierDesc}",
                                            fontSize = 11.sp,
                                            color = WarmYellow,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "${(ach.progress * 100).toInt()}%",
                                            fontSize = 11.sp,
                                            color = TextMuted,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    androidx.compose.material3.LinearProgressIndicator(
                                        progress = { ach.progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(5.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = ElectricOrange,
                                        trackColor = MidnightElevated,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SQUAD DNA (STATISTICAL & NON-EDITABLE)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🧬 SQUAD BEHAVIORAL DNA",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextOffWhite
                        )
                        Text(
                            text = "Computed live from your mission history & budget discipline",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(ElectricOrange.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "AUTO STATS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = ElectricOrange
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, BorderStroke, RoundedCornerShape(20.dp)),
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
                            Text(
                                text = "Synergy & Compatibility",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Text(
                                text = "${groupDna.compatibilityPct}%",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = WarmYellow
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "“${groupDna.aiVerdict}”",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextOffWhite,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Behavioral stat bars (strictly statistical, non-editable)
                        DnaStatBar("🔥 Chaos & Plot Twist Tolerance", groupDna.chaosPct, CoralRed)
                        DnaStatBar("🍔 Street Food & Snack Intensity", groupDna.foodPct, ElectricOrange)
                        DnaStatBar("🚶 Walking & Exploration Stamina", groupDna.adventurePct, MintGreen)
                        DnaStatBar("💸 Frugality & Budget Discipline", groupDna.budgetPct, WarmYellow)
                        DnaStatBar("🌊 Mediterranean Chill Factor", groupDna.chillPct, Color(0xFF4FC3F7))
                        DnaStatBar("🏛️ Heritage & Cultural Curiosity", groupDna.culturePct, Color(0xFFBA68C8))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // DEMO CONTROLS (QUICK ACTIONS FOR EVALUATION)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, WarmYellow.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1910))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "⚡ COMPETITION DEMO TOOLBOX",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = WarmYellow,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Instant testing tools to evaluate leveling, photo proof scoring, and state persistence.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onQuickAddDemoXp,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("demo_add_xp_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = WarmYellow),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("+500 XP", fontSize = 12.sp, fontWeight = FontWeight.Black, color = MidnightBackground)
                        }

                        OutlinedButton(
                            onClick = onResetDemoData,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("demo_reset_data_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderStroke)
                        ) {
                            Text("RESET DATA", fontSize = 12.sp, color = TextOffWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else if (selectedTab == 1) {
            // LOGBOOK TAB
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "ADVENTURE LOGBOOK",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = TextOffWhite
                )
                Text(
                    text = "History of completed missions, photo proofs, and budget savings.",
                    fontSize = 13.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                var logFilter by remember { mutableStateOf("ALL") } // ALL, WALK, CAR, PHOTO, ZERO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        Pair("ALL", "✨ All"),
                        Pair("WALK", "🚶 ع اجريك"),
                        Pair("CAR", "🚗 بالسيارة"),
                        Pair("PHOTO", "📸 Photos"),
                        Pair("ZERO", "💸 $0")
                    ).forEach { (key, label) ->
                        val isSelected = logFilter == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) ElectricOrange else MidnightElevated)
                                .border(1.dp, if (isSelected) ElectricOrange else BorderStroke, RoundedCornerShape(12.dp))
                                .clickable { logFilter = key }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextOffWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val filteredLogbook = remember(logbook, logFilter) {
                    when (logFilter) {
                        "WALK" -> logbook.filter { it.transportMode == TransportMode.WALK }
                        "CAR" -> logbook.filter { it.transportMode == TransportMode.CAR }
                        "PHOTO" -> logbook.filter { it.photoUri != null }
                        "ZERO" -> logbook.filter { it.actualTotalSpent <= 0.05 }
                        else -> logbook
                    }
                }

                if (filteredLogbook.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .border(1.dp, BorderStroke, RoundedCornerShape(20.dp)),
                        colors = CardDefaults.cardColors(containerColor = MidnightCard)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🗺️", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (logbook.isEmpty()) "No Completed Adventures Yet" else "No Missions Match Filter",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextOffWhite
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (logbook.isEmpty()) "Start and complete a mission from Home to log your memories, verified photos, and budget tracking here." else "Tap 'All' to see all your past logged missions.",
                                fontSize = 13.sp,
                                color = TextMuted,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        filteredLogbook.forEach { entry ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(1.dp, BorderStroke, RoundedCornerShape(20.dp)),
                                colors = CardDefaults.cardColors(containerColor = MidnightCard)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(18.dp)
                                ) {
                                    // Title & XP
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = entry.missionName,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Black,
                                            color = TextOffWhite,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "+${entry.xpEarned} XP",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MintGreen
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // City, Date & Transport
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "📍 ${entry.city} • 📅 ${entry.date}",
                                            fontSize = 12.sp,
                                            color = TextMuted
                                        )
                                        Text(
                                            text = if (entry.transportMode == TransportMode.CAR) "🚗 Car" else "🚶 Walking",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ElectricOrange
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Stats Strip: Quests, Spent, Saved
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MidnightElevated)
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("QUESTS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                                            Text("${entry.questsCompleted}/${entry.totalQuests}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextOffWhite)
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("SPENT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                                            Text("$${String.format("%.2f", entry.actualTotalSpent)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WarmYellow)
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("BUDGET LEFT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                                            Text("$${String.format("%.2f", entry.moneyLeft)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MintGreen)
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("RATING", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                                            Text(entry.finalRank.take(10), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextOffWhite)
                                        }
                                    }

                                    // Photo Proof Preview if available
                                    if (entry.photoUri != null) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(140.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .border(1.dp, MintGreen, RoundedCornerShape(12.dp))
                                        ) {
                                            AsyncImage(
                                                model = entry.photoUri,
                                                contentDescription = "Proof Photo",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(6.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(MintGreen)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text("VERIFIED PROOF", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MidnightBackground)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // LEADERBOARD TAB
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "SQUAD LEADERBOARD",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = TextOffWhite
                )
                Text(
                    text = "Weekly rankings based on verified photo proofs and missions cleared.",
                    fontSize = 13.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    leaderboard.forEach { entry ->
                        val isUser = entry.isCurrentUser
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .border(
                                    width = if (isUser) 2.dp else 1.dp,
                                    color = if (isUser) ElectricOrange else BorderStroke,
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isUser) Color(0xFF2E1710) else MidnightCard
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "#${entry.rank}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (entry.rank <= 3) WarmYellow else TextMuted
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(text = entry.badgeEmoji, fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = if (isUser) "${entry.teamName} (You)" else entry.teamName,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isUser) ElectricOrange else TextOffWhite
                                        )
                                        Text(
                                            text = "📍 ${entry.city}",
                                            fontSize = 11.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                Text(
                                    text = "${entry.scoreXp} XP",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = WarmYellow
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TierChip(
    title: String,
    req: String,
    unlocked: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (unlocked) MidnightElevated else MidnightSurface)
            .border(
                1.dp,
                if (unlocked) WarmYellow.copy(alpha = 0.6f) else BorderStroke.copy(alpha = 0.4f),
                RoundedCornerShape(10.dp)
            )
            .padding(vertical = 6.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (unlocked) TextOffWhite else TextMuted.copy(alpha = 0.6f)
            )
            Text(
                text = req,
                fontSize = 9.sp,
                color = if (unlocked) WarmYellow else TextMuted.copy(alpha = 0.5f),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun DnaStatBar(label: String, value: Int, barColor: Color) {
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, fontSize = 12.sp, color = TextOffWhite, fontWeight = FontWeight.SemiBold)
            Text(text = "$value%", fontSize = 13.sp, color = barColor, fontWeight = FontWeight.Black)
        }
        Spacer(modifier = Modifier.height(4.dp))
        androidx.compose.material3.LinearProgressIndicator(
            progress = { (value.toFloat() / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = barColor,
            trackColor = MidnightElevated,
        )
    }
}
