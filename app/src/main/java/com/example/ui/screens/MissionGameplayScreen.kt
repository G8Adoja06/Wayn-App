package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Warning
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.data.model.Mission
import com.example.data.model.TransportMode
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
import java.io.File

@Composable
fun MissionGameplayScreen(
    mission: Mission,
    onCompleteQuest: (photoUri: String?, actualSpent: Double) -> Unit,
    onSkipQuest: () -> Unit,
    onTriggerPlotTwist: () -> Unit,
    onCancelMission: () -> Unit,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current
    var showCancelDialog by remember { mutableStateOf(false) }
    var showSpendingDialog by remember { mutableStateOf(false) }
    var inputSpentText by remember { mutableStateOf("0") }
    var currentPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }
    var isCompleting by remember { mutableStateOf(false) }

    // Live remaining millis ticker calculated from missionEndEpochMillis (FIXES TIMER BUG!)
    var remainingMillis by remember { mutableLongStateOf(mission.remainingMillis()) }

    LaunchedEffect(mission.missionEndEpochMillis) {
        while (true) {
            remainingMillis = mission.remainingMillis()
            delay(1000)
        }
    }

    // Camera Capture Launcher
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            currentPhotoUri = tempCameraUri
        }
    }

    // Fallback Image Picker
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentPhotoUri = uri
        }
    }

    fun launchCamera() {
        try {
            val photoFile = File(context.cacheDir, "proof_${System.currentTimeMillis()}.jpg")
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            tempCameraUri = uri
            takePictureLauncher.launch(uri)
        } catch (e: Exception) {
            // Camera intent fallback: use zero-permission Photo Picker
            pickMediaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    val currentQuest = mission.currentQuest()
    val questIndex = mission.currentQuestIndex
    val totalQuests = mission.quests.size

    val minutes = (remainingMillis / 1000) / 60
    val seconds = (remainingMillis / 1000) % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Abandon Mission?", color = TextOffWhite, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "“We will pretend that never happened.” Are you sure you want to cancel this adventure?",
                    color = TextMuted
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCancelDialog = false
                        onCancelMission()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
                ) {
                    Text("ABANDON", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("KEEP PLAYING", color = TextOffWhite)
                }
            },
            containerColor = MidnightCard
        )
    }

    if (showSpendingDialog && currentQuest != null) {
        val quickAmounts = if (mission.initialBudgetUsd >= 50.0) {
            listOf(0, 5, 10, 20, 50)
        } else {
            listOf(0, 1, 2, 5, 10)
        }
        val enteredDouble = inputSpentText.toDoubleOrNull() ?: 0.0
        val isExceedingBudget = mission.remainingBudgetUsd > 0.0 && enteredDouble > mission.remainingBudgetUsd

        AlertDialog(
            onDismissRequest = { showSpendingDialog = false },
            title = {
                Text(
                    text = "How much did that set you back?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextOffWhite
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Enter actual spending for \"${currentQuest.title}\". Remaining budget: $${String.format("%.2f", mission.remainingBudgetUsd)}",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quickAmounts.forEach { amt ->
                            val isSelected = inputSpentText == amt.toString() || (amt == 0 && (inputSpentText == "0" || inputSpentText == "0.0"))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) ElectricOrange else MidnightElevated)
                                    .border(1.dp, if (isSelected) ElectricOrange else BorderStroke, RoundedCornerShape(10.dp))
                                    .clickable { inputSpentText = amt.toString() }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$$amt",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else TextOffWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = inputSpentText,
                        onValueChange = { inputSpentText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                        label = { Text("Input: $ ______") },
                        prefix = { Text("$ ", color = WarmYellow, fontWeight = FontWeight.Bold) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricOrange,
                            unfocusedBorderColor = BorderStroke,
                            focusedTextColor = TextOffWhite,
                            unfocusedTextColor = TextOffWhite
                        )
                    )

                    if (isExceedingBudget) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⚠️ Exceeds remaining budget ($${String.format("%.2f", mission.remainingBudgetUsd)}). You can still proceed if needed.",
                            fontSize = 11.sp,
                            color = WarmYellow
                        )
                    }
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            showSpendingDialog = false
                            isCompleting = true
                            onCompleteQuest(currentPhotoUri?.toString(), 0.0)
                            currentPhotoUri = null
                            isCompleting = false
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderStroke)
                    ) {
                        Text(text = "$0 / SKIP", color = TextOffWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val spent = (inputSpentText.toDoubleOrNull() ?: 0.0).coerceAtLeast(0.0)
                            showSpendingDialog = false
                            isCompleting = true
                            onCompleteQuest(currentPhotoUri?.toString(), spent)
                            currentPhotoUri = null
                            isCompleting = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "CONFIRM", fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showSpendingDialog = false }) {
                    Text(text = "CANCEL", color = TextMuted)
                }
            },
            containerColor = MidnightCard
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TOP HEADER: BACK, TITLE & CANCEL (Always accessible at top)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackToHome,
                    modifier = Modifier.testTag("mission_back_to_home")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Home",
                        tint = TextOffWhite
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = mission.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = ElectricOrange,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "QUEST ${minOf(questIndex + 1, totalQuests)} OF $totalQuests",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmYellow
                    )
                }

                IconButton(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier.testTag("mission_cancel_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = CoralRed
                    )
                }
            }

            // SCROLLABLE MISSION CONTENT: STATS HUD, PLOT TWIST, QUEST CARD, ACTION BUTTONS
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // STATS HUD STRIP (Timer, Earned XP, Remaining Budget, Transport Mode)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MidnightCard)
                        .border(1.dp, BorderStroke, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Countdown Timer
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "TIMER", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        Text(
                            text = timeFormatted,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = if (remainingMillis < 300000L) CoralRed else WarmYellow
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(BorderStroke))

                    // XP Earned
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "EARNED XP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        Text(
                            text = "+${mission.earnedXp()}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = MintGreen
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(BorderStroke))

                    // Remaining Budget
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "BUDGET LEFT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        Text(
                            text = "$${String.format("%.1f", mission.remainingBudgetUsd)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = if (mission.remainingBudgetUsd <= 0.0) CoralRed else TextOffWhite
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(BorderStroke))

                    // Transport Mode Badge
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "MODE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                        Text(
                            text = if (mission.transportMode == TransportMode.CAR) "🚗 CAR" else "🚶 WALK",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = ElectricOrange
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ACTIVE PLOT TWIST BANNER (IF TRIGGERED)
            AnimatedVisibility(visible = mission.activePlotTwist != null) {
                mission.activePlotTwist?.let { twist ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(2.dp, CoralRed, RoundedCornerShape(18.dp))
                            .testTag("mission_plot_twist_banner"),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C1417))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = twist.emoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = twist.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CoralRed
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = twist.description,
                                    fontSize = 13.sp,
                                    color = TextOffWhite,
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "+${twist.bonusXp} XP Twist Reward",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarmYellow
                                )
                            }
                        }
                    }
                }
            }

            // MAIN CURRENT QUEST CARD
            if (currentQuest != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.5.dp, BorderStroke, RoundedCornerShape(24.dp))
                        .testTag("mission_current_quest_card"),
                    colors = CardDefaults.cardColors(containerColor = MidnightCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp)
                    ) {
                        // Category chip, Cost badge & XP reward
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MidnightElevated)
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = "${currentQuest.type.icon} ${currentQuest.type.displayName}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ElectricOrange
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MidnightElevated)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (currentQuest.estimatedCostUsd <= 0.0) "💸 FREE" else "💵 ~$${currentQuest.estimatedCostUsd.toInt()}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentQuest.estimatedCostUsd <= 0.0) MintGreen else TextOffWhite
                                    )
                                }
                            }

                            Text(
                                text = "+${currentQuest.xp} XP",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = WarmYellow
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Title
                        Text(
                            text = currentQuest.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = TextOffWhite
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Description
                        Text(
                            text = currentQuest.description,
                            fontSize = 15.sp,
                            color = TextMuted,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Bonus Challenge
                        currentQuest.bonusChallenge?.let { bonus ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MidnightElevated)
                                    .border(1.dp, BorderStroke, RoundedCornerShape(14.dp))
                                    .padding(14.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "⚡ BONUS OBJECTIVE (+${currentQuest.bonusXp} XP)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = WarmYellow
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = bonus,
                                        fontSize = 13.sp,
                                        color = TextOffWhite,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }

                        // Photo Proof Preview (if attached)
                        if (currentPhotoUri != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.5.dp, MintGreen, RoundedCornerShape(16.dp))
                            ) {
                                AsyncImage(
                                    model = currentPhotoUri,
                                    contentDescription = "Photo Proof",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MintGreen)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("PROOF ATTACHED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MidnightBackground)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ACTION BUTTONS: COMPLETE, TAKE PROOF PHOTO, SKIP, PLOT TWIST
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Main Complete Button (Prevents double taps)
                Button(
                    onClick = {
                        if (!isCompleting && currentQuest != null) {
                            inputSpentText = if (mission.initialBudgetUsd <= 0.0) "0" else (if (currentQuest.estimatedCostUsd > 0.0) currentQuest.estimatedCostUsd.toInt().toString() else "0")
                            showSpendingDialog = true
                        }
                    },
                    enabled = !isCompleting && currentQuest != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("mission_complete_quest_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "COMPLETE QUEST",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }

                // Row: Take Proof Photo & Image Picker Fallback
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { launchCamera() },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("mission_take_photo_button"),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmYellow),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = WarmYellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentPhotoUri != null) "RETAKE" else "PHOTO PROOF",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = WarmYellow
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            pickMediaLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .weight(0.7f)
                            .height(50.dp)
                            .testTag("mission_gallery_button"),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderStroke),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "GALLERY",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextMuted
                        )
                    }
                }

                // Row: Skip Quest & Plot Twist Trigger
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            if (currentQuest != null) onSkipQuest()
                        },
                        enabled = currentQuest != null,
                        modifier = Modifier.testTag("mission_skip_quest_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Skip Quest",
                            color = TextMuted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    TextButton(
                        onClick = onTriggerPlotTwist,
                        modifier = Modifier.testTag("mission_trigger_plot_twist_button")
                    ) {
                        Text(
                            text = "🚨 Plot Twist",
                            color = CoralRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
