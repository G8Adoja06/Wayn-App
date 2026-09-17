package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Mission
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.ElectricOrange
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightElevated
import com.example.ui.theme.MintGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOffWhite
import com.example.ui.theme.WarmYellow

@Composable
fun RecapScreen(
    mission: Mission?,
    userName: String = "You",
    onPostToFeed: ((caption: String, photoUri: String?) -> Unit)? = null,
    onNewAdventure: () -> Unit,
    onHome: () -> Unit
) {
    val context = LocalContext.current
    var showPostFeedDialog by remember { mutableStateOf(false) }
    var postCaption by remember { mutableStateOf("Surviving Beirut one weird decision at a time") }

    val completedCount = mission?.completedQuestsCount() ?: 3
    val totalXp = mission?.earnedXp() ?: 540
    val actualSpent = mission?.totalSpentUsd ?: (mission?.estimatedCostUsd ?: 0.0)
    val remainingBudget = mission?.remainingBudgetUsd ?: 0.0
    val startingBudget = mission?.initialBudgetUsd ?: 0.0
    val transport = mission?.transportMode?.displayName ?: "Walking"
    val proofPhotoUri = mission?.quests?.lastOrNull { it.photoUri != null }?.photoUri

    val rating = when {
        actualSpent <= 0.0 -> "BUDGET NINJA ($0 SPENT)"
        mission?.difficulty == "Unhinged" -> "CHAOTIC LEGEND"
        mission?.difficulty == "Resourceful" -> "STREET SMART"
        mission?.difficulty == "Endurance" -> "BEIRUT VETERAN"
        else -> "CHAOTIC GOOD"
    }

    val shareText = """
        🔥 WAYN? ADVENTURE RECAP
        Location: ${mission?.location ?: "Beirut"}
        Mode: $transport Adventure
        Quests Cleared: $completedCount
        XP Earned: +$totalXp XP
        Starting Budget: $${String.format("%.2f", startingBudget)}
        Total Spent: $${String.format("%.2f", actualSpent)}
        Budget Left: $${String.format("%.2f", remainingBudget)}
        Adventure Rating: $rating
        Streak: +1 Day Active 🔥
        
        "Turn anywhere into an adventure." #WaynApp #LebanonAdventures
    """.trimIndent()

    fun shareRecap() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Wayn? Adventure Recap")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Share Adventure Recap"))
    }

    if (showPostFeedDialog) {
        AlertDialog(
            onDismissRequest = { showPostFeedDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⚡", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "POST TO QUEST FEED",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextOffWhite
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = userName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextOffWhite
                            )
                            Text(
                                text = "📍 ${mission?.location ?: "Beirut"} • Just now",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                        Text(
                            text = "+$totalXp XP",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = MintGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${mission?.title ?: "Lebanese Mission"} ($completedCount quests cleared • $${String.format("%.2f", actualSpent)} spent)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = WarmYellow
                    )

                    if (proofPhotoUri != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, MintGreen, RoundedCornerShape(12.dp))
                        ) {
                            coil.compose.AsyncImage(
                                model = proofPhotoUri,
                                contentDescription = "Proof Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    androidx.compose.material3.OutlinedTextField(
                        value = postCaption,
                        onValueChange = { postCaption = it },
                        label = { Text("Caption") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricOrange,
                            unfocusedBorderColor = BorderStroke,
                            focusedTextColor = TextOffWhite,
                            unfocusedTextColor = TextOffWhite
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPostFeedDialog = false
                        onPostToFeed?.invoke(postCaption, proofPhotoUri)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("POST QUEST", fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showPostFeedDialog = false }) {
                    Text("CANCEL", color = TextMuted)
                }
            },
            containerColor = MidnightCard
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // VERTICAL SOCIAL SHARE CARD
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .border(2.dp, ElectricOrange, RoundedCornerShape(26.dp))
                    .testTag("recap_card"),
                colors = CardDefaults.cardColors(containerColor = MidnightCard)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF2A140E),
                                    MidnightCard
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Brand mark
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "WAYN?",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = ElectricOrange,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "وين؟", fontSize = 14.sp, color = WarmYellow, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "YOU SURVIVED.",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = TextOffWhite,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "TONIGHT IN ${mission?.location?.uppercase() ?: "BEIRUT"} YOU:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarmYellow,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Stats list
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            RecapStatRow("🎯 Quests completed", "$completedCount cleared")
                            RecapStatRow("⚡ Total XP earned", "+$totalXp XP", MintGreen)
                            RecapStatRow("⏱️ Total duration", "${mission?.estimatedDurationMinutes ?: 45} mins")
                            RecapStatRow("💵 Starting budget", "$${String.format("%.2f", startingBudget)}")
                            RecapStatRow("💸 Total spent", "$${String.format("%.2f", actualSpent)}")
                            RecapStatRow("💰 Money left", "$${String.format("%.2f", remainingBudget)}")
                            RecapStatRow("🔥 Streak impact", "+1 Day Streak Active 🔥", WarmYellow)
                            RecapStatRow("🚶 Transport style", "$transport Adventure")
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Adventure Rating Badge
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "ADVENTURE RATING",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MidnightElevated)
                                    .border(1.5.dp, WarmYellow, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = rating,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = WarmYellow,
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ACTION BUTTONS: POST TO QUEST FEED, SHARE, NEW ADVENTURE, BACK HOME
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (onPostToFeed != null) {
                    Button(
                        onClick = { showPostFeedDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("recap_post_feed_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "POST TO QUEST FEED 🚀",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Button(
                    onClick = { shareRecap() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("recap_share_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = if (onPostToFeed != null) MidnightElevated else ElectricOrange),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SHARE RECAP",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onNewAdventure,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("recap_new_adventure_button"),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderStroke),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "NEW ADVENTURE",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextOffWhite
                        )
                    }

                    OutlinedButton(
                        onClick = onHome,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("recap_home_button"),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderStroke),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "BACK HOME",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextOffWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecapStatRow(label: String, value: String, valueColor: Color = TextOffWhite) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 14.sp, color = TextMuted)
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
