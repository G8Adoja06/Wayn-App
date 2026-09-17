package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdventureMode
import com.example.data.model.AdventureRequest
import com.example.data.model.SecondaryMood
import com.example.data.model.TransportMode
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.CoralRed
import com.example.ui.theme.ElectricOrange
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOffWhite
import com.example.ui.theme.TextSubtle
import com.example.ui.theme.WarmYellow

@Composable
fun AdventureSetupScreen(
    mode: AdventureMode,
    currentCity: String,
    isGenerating: Boolean,
    onBack: () -> Unit,
    onGenerate: (AdventureRequest) -> Unit
) {
    var step by remember { mutableStateOf(1) }

    // State selections
    var groupSize by remember {
        mutableStateOf(
            if (mode == AdventureMode.DATE) "DUO" else "Solo"
        )
    }

    var transportMode by remember { mutableStateOf(TransportMode.WALK) }

    var budget by remember {
        mutableStateOf(
            when (mode) {
                AdventureMode.BROKE -> "$0"
                AdventureMode.DATE -> "$50"
                else -> "$10"
            }
        )
    }

    var duration by remember {
        mutableStateOf(
            if (mode == AdventureMode.FOOD) "0–1 hr" else "1 hour"
        )
    }

    var secondaryMood by remember { mutableStateOf(SecondaryMood.CHAOS) }

    val totalSteps = 5

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP BAR & PROGRESS
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (step > 1) step-- else onBack()
                        },
                        modifier = Modifier.testTag("adventure_setup_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextOffWhite
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = mode.title.uppercase(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = ElectricOrange,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Step $step of $totalSteps",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    Box(modifier = Modifier.size(40.dp)) // balance
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Progress Indicator Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (i in 1..totalSteps) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(if (i <= step) ElectricOrange else MidnightElevated)
                        )
                    }
                }
            }

            // STEP CONTENT (Animated)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
            ) {
                AnimatedContent(
                    targetState = step,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
                        } else {
                            (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
                        }
                    },
                    label = "stepTransition"
                ) { currentStep ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        when (currentStep) {
                            1 -> StepGroupSize(
                                mode = mode,
                                selected = groupSize,
                                onSelect = { groupSize = it }
                            )
                            2 -> StepTransportMode(
                                selected = transportMode,
                                onSelect = { transportMode = it }
                            )
                            3 -> StepBudget(
                                mode = mode,
                                selected = budget,
                                onSelect = { budget = it }
                            )
                            4 -> StepTime(
                                mode = mode,
                                selected = duration,
                                onSelect = { duration = it }
                            )
                            5 -> StepMood(
                                selected = secondaryMood,
                                onSelect = { secondaryMood = it }
                            )
                        }
                    }
                }
            }

            // BOTTOM NAVIGATION BUTTONS
            Column {
                if (step < totalSteps) {
                    Button(
                        onClick = { step++ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("adventure_setup_next_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            text = "NEXT",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            val request = AdventureRequest(
                                location = currentCity,
                                groupSize = groupSize,
                                budget = budget,
                                duration = duration,
                                mode = mode,
                                secondaryMood = secondaryMood,
                                transportMode = transportMode,
                                transportation = transportMode.displayName
                            )
                            onGenerate(request)
                        },
                        enabled = !isGenerating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .testTag("adventure_setup_generate_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricOrange),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "SYNTHESIZING ADVENTURE...",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "GENERATE MY ADVENTURE",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// STEP 1: GROUP SIZE
@Composable
private fun StepGroupSize(
    mode: AdventureMode,
    selected: String,
    onSelect: (String) -> Unit
) {
    Text(
        text = "Who’s coming?",
        fontSize = 28.sp,
        fontWeight = FontWeight.Black,
        color = TextOffWhite
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = if (mode == AdventureMode.DATE) "Select your romantic company configuration." else "Solo recon or whole crew?",
        fontSize = 15.sp,
        color = TextMuted
    )

    Spacer(modifier = Modifier.height(24.dp))

    if (mode == AdventureMode.DATE) {
        // ONLY DUO and QUAD
        val dateOptions = listOf(
            Triple("DUO", "انت وصاحبتك", "Just the two of you. High romance, zero distractions."),
            Triple("QUAD", "انت وصاحبتك، صاحبك وصاحبتو", "Double date madness. Shared appetizers and mutual roast sessions.")
        )

        dateOptions.forEach { (key, arabic, desc) ->
            val isSelected = selected == key
            SelectableOptionCard(
                title = key,
                subtitle = desc,
                arabicText = arabic,
                isSelected = isSelected,
                onClick = { onSelect(key) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    } else {
        // Standard Options: Solo, 2, 3, 4, 5+
        val standardOptions = listOf(
            Triple("Solo", "Lone Wolf", "Zero consensus needed. Move like a ghost."),
            Triple("2", "Dynamic Duo", "Fast decisions, easy navigation."),
            Triple("3", "The Triangle", "Perfect for sharing food and splitting cabs."),
            Triple("4", "The Squad", "Full car capacity. High energy."),
            Triple("5+", "The Mob", "Total chaos. Expect at least one person to get lost.")
        )

        standardOptions.forEach { (size, label, desc) ->
            val isSelected = selected == size
            SelectableOptionCard(
                title = size,
                badge = label,
                subtitle = desc,
                isSelected = isSelected,
                onClick = { onSelect(size) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

// STEP 2: TRANSPORT MODE (WALK VS CAR)
@Composable
private fun StepTransportMode(
    selected: TransportMode,
    onSelect: (TransportMode) -> Unit
) {
    Text(
        text = "How are we moving?",
        fontSize = 28.sp,
        fontWeight = FontWeight.Black,
        color = TextOffWhite
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = "Choose your exploration style. Quests will adapt to real navigation distances.",
        fontSize = 15.sp,
        color = TextMuted
    )

    Spacer(modifier = Modifier.height(24.dp))

    SelectableOptionCard(
        title = "🚶 On Foot",
        badge = "WALK",
        arabicText = "ع اجريك يا بيك؟",
        subtitle = "Walkable neighborhood clusters, alleys, staircases, scenic corners. Zero car or parking stress.",
        isSelected = selected == TransportMode.WALK,
        onClick = { onSelect(TransportMode.WALK) }
    )

    Spacer(modifier = Modifier.height(14.dp))

    SelectableOptionCard(
        title = "🚗 By Car",
        badge = "CAR",
        arabicText = "مرتاح عوضعك بالبنزين؟",
        subtitle = "Hop between scenic viewpoints, coastal highways, and distinct districts. Driving & parking friendly.",
        isSelected = selected == TransportMode.CAR,
        onClick = { onSelect(TransportMode.CAR) }
    )
}

// STEP 3: BUDGET SELECTOR
@Composable
private fun StepBudget(
    mode: AdventureMode,
    selected: String,
    onSelect: (String) -> Unit
) {
    if (mode == AdventureMode.DATE) {
        Text(
            text = "What’s the damage?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = TextOffWhite
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "قدي بدك تدفع يا كبير",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = WarmYellow
        )

        Spacer(modifier = Modifier.height(24.dp))

        val dateBudgets = listOf(
            Pair("$0", "بلا كحتنة"),
            Pair("$50", "بدك ياها تدفع نص الفاتورة؟"),
            Pair("$100+", "مين بيسترجي يسأل علمصاري؟")
        )

        dateBudgets.forEach { (amount, subtitle) ->
            val isSelected = selected == amount
            SelectableOptionCard(
                title = amount,
                arabicText = subtitle,
                isSelected = isSelected,
                onClick = { onSelect(amount) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    } else if (mode == AdventureMode.BROKE) {
        Text(
            text = "What’s the damage?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = TextOffWhite
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Strictly zero or pocket coins only.",
            fontSize = 15.sp,
            color = CoralRed
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ONLY $0 and $5
        val brokeBudgets = listOf(
            Pair("$0", "Financially unavailable. Street air is free."),
            Pair("$5", "We can work with this. One sandwich, shared memories.")
        )

        brokeBudgets.forEach { (amount, desc) ->
            val isSelected = selected == amount
            SelectableOptionCard(
                title = amount,
                subtitle = desc,
                isSelected = isSelected,
                onClick = { onSelect(amount) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    } else {
        // Standard We're Bored / Food Budget Selector
        Text(
            text = "What’s the damage?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = TextOffWhite
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Per person flexible budget target.",
            fontSize = 15.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(20.dp))

        val budgets = listOf(
            Pair("$0", "“Financially unavailable.”"),
            Pair("$5", "“We can work with this.”"),
            Pair("$10", "“Solid middle ground.”"),
            Pair("$20", "“Living large today.”"),
            Pair("$50", "“Okay, big spender.”"),
            Pair("Custom", "“We make our own rules.”")
        )

        budgets.forEach { (amount, quote) ->
            val isSelected = selected == amount
            SelectableOptionCard(
                title = amount,
                subtitle = quote,
                isSelected = isSelected,
                onClick = { onSelect(amount) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

// STEP 3: TIME SELECTOR
@Composable
private fun StepTime(
    mode: AdventureMode,
    selected: String,
    onSelect: (String) -> Unit
) {
    Text(
        text = "How much time?",
        fontSize = 28.sp,
        fontWeight = FontWeight.Black,
        color = TextOffWhite
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = "How long before your squad collapses or leaves?",
        fontSize = 15.sp,
        color = TextMuted
    )

    Spacer(modifier = Modifier.height(24.dp))

    if (mode == AdventureMode.FOOD) {
        // ONLY 0–1 hr (لقمة عالسريع) and 1+ hr (مطول استاذ؟)
        val foodTimes = listOf(
            Triple("0–1 hr", "لقمة عالسريع", "Quick in-and-out bite. Maximum speed."),
            Triple("1+ hr", "مطول استاذ؟", "Full sit-down feast, tea, debate, and food coma.")
        )

        foodTimes.forEach { (time, arabic, desc) ->
            val isSelected = selected == time
            SelectableOptionCard(
                title = time,
                arabicText = arabic,
                subtitle = desc,
                isSelected = isSelected,
                onClick = { onSelect(time) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    } else {
        // Standard times: 30 min, 1 hour, 2 hours, 3+ hours
        val standardTimes = listOf(
            Pair("30 min", "Quick adrenaline sprint. One or two fast objectives."),
            Pair("1 hour", "The sweet spot. Balanced adventure without exhaustion."),
            Pair("2 hours", "Epic neighborhood crawl with multiple milestones."),
            Pair("3+ hours", "Marathon mission. You will return as different people.")
        )

        standardTimes.forEach { (time, desc) ->
            val isSelected = selected == time
            SelectableOptionCard(
                title = time,
                subtitle = desc,
                isSelected = isSelected,
                onClick = { onSelect(time) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

// FINAL STEP: SECONDARY MOODS (NO DATE, BROKE, FOOD HERE!)
@Composable
private fun StepMood(
    selected: SecondaryMood,
    onSelect: (SecondaryMood) -> Unit
) {
    Text(
        text = "What are we feeling?",
        fontSize = 28.sp,
        fontWeight = FontWeight.Black,
        color = TextOffWhite
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = "Set the psychological temperature of the mission.",
        fontSize = 15.sp,
        color = TextMuted
    )

    Spacer(modifier = Modifier.height(24.dp))

    // CHAOS, CHILL, ADVENTURE, MYSTERY, WILD CARD (No Secret Saboteur, No Date/Broke/Food)
    val moods = listOf(
        SecondaryMood.CHAOS,
        SecondaryMood.CHILL,
        SecondaryMood.ADVENTURE,
        SecondaryMood.MYSTERY,
        SecondaryMood.WILD_CARD
    )

    moods.forEach { mood ->
        val isSelected = selected == mood
        SelectableOptionCard(
            title = "${mood.emoji} ${mood.title}",
            subtitle = mood.description,
            isSelected = isSelected,
            onClick = { onSelect(mood) }
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun SelectableOptionCard(
    title: String,
    subtitle: String? = null,
    arabicText: String? = null,
    badge: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) ElectricOrange else BorderStroke,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF26150F) else MidnightCard
        )
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
                    Text(
                        text = title,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isSelected) ElectricOrange else TextOffWhite
                    )
                    badge?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MidnightElevated)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = it, fontSize = 11.sp, color = WarmYellow, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                arabicText?.let {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmYellow
                    )
                }

                subtitle?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 13.sp,
                        color = TextMuted,
                        lineHeight = 17.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) ElectricOrange else MidnightElevated)
                    .border(1.dp, if (isSelected) ElectricOrange else BorderStroke, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
