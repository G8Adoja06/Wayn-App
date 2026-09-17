package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.AdventureMode
import com.example.ui.screens.AdventureSetupScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MissionGameplayScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PartyChatScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuestFeedScreen
import com.example.ui.screens.QuestPartiesScreen
import com.example.ui.screens.RecapScreen
import com.example.ui.screens.RouletteScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.BorderStroke
import com.example.ui.theme.ElectricOrange
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightElevated
import com.example.ui.theme.MidnightSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOffWhite
import com.example.ui.theme.WarmYellow
import com.example.viewmodel.Screen
import com.example.viewmodel.WaynViewModel

@Composable
fun WaynApp(
    viewModel: WaynViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val activeMission by viewModel.activeMission.collectAsState()
    val lastCompletedMission by viewModel.lastCompletedMission.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val achievements by viewModel.achievements.collectAsState()
    val groupDna by viewModel.groupDna.collectAsState()
    val logbookEntries by viewModel.logbookEntries.collectAsState()
    val feedPosts by viewModel.feedPosts.collectAsState()
    val questParties by viewModel.questParties.collectAsState()
    val leaderboard by viewModel.leaderboard.collectAsState()
    val dailyQuest by viewModel.dailyQuest.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val activeSetupMode by viewModel.activeSetupMode.collectAsState()
    val selectedPartyId by viewModel.selectedPartyId.collectAsState()
    val inAppAlert by viewModel.inAppAlert.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()

    // Handle system back navigation
    BackHandler(enabled = currentScreen != Screen.HOME && currentScreen != Screen.SPLASH && currentScreen != Screen.ONBOARDING) {
        when (currentScreen) {
            Screen.PARTY_CHAT -> viewModel.navigateTo(Screen.QUEST_PARTIES)
            Screen.QUEST_PARTIES -> viewModel.navigateTo(Screen.QUEST_FEED)
            Screen.MISSION_GAMEPLAY -> viewModel.navigateTo(Screen.HOME)
            Screen.ADVENTURE_SETUP, Screen.ROULETTE -> viewModel.navigateTo(Screen.HOME)
            Screen.RECAP -> viewModel.navigateTo(Screen.HOME)
            else -> viewModel.navigateTo(Screen.HOME)
        }
    }

    val showBottomBar = currentScreen in listOf(
        Screen.HOME,
        Screen.EXPLORE,
        Screen.QUEST_FEED,
        Screen.PROFILE
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                WaynBottomNavigation(
                    currentScreen = currentScreen,
                    hasActiveMission = activeMission != null && !activeMission!!.completed,
                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                    onPlayClicked = {
                        if (activeMission != null && !activeMission!!.completed) {
                            viewModel.navigateTo(Screen.MISSION_GAMEPLAY)
                        } else {
                            viewModel.startAdventureSetup(AdventureMode.BORED)
                        }
                    }
                )
            }
        },
        containerColor = MidnightBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Main Screen Routing
            when (currentScreen) {
                Screen.SPLASH -> SplashScreen()
                Screen.ONBOARDING -> OnboardingScreen(
                    onComplete = { name -> viewModel.completeOnboarding(name) }
                )
                Screen.HOME -> HomeScreen(
                    userProfile = userProfile,
                    selectedCity = selectedCity,
                    activeMission = activeMission,
                    dailyQuest = dailyQuest,
                    onCitySelected = { city -> viewModel.selectExploreCity(city) },
                    onStartMode = { mode -> viewModel.startAdventureSetup(mode) },
                    onOpenRoulette = { viewModel.openRoulette() },
                    onResumeMission = { viewModel.navigateTo(Screen.MISSION_GAMEPLAY) },
                    onCompleteDailyQuest = { viewModel.completeDailyQuest() },
                    onOpenProfile = { viewModel.navigateTo(Screen.PROFILE) },
                    onOpenNotifications = { viewModel.showAlert("No new notifications right now.") },
                    onOpenLogbook = { viewModel.navigateTo(Screen.LOGBOOK) }
                )
                Screen.ADVENTURE_SETUP -> AdventureSetupScreen(
                    mode = activeSetupMode,
                    currentCity = selectedCity,
                    isGenerating = isGenerating,
                    onBack = { viewModel.navigateTo(Screen.HOME) },
                    onGenerate = { req -> viewModel.generateAndStartAdventure(req) }
                )
                Screen.ROULETTE -> RouletteScreen(
                    currentCity = selectedCity,
                    onBack = { viewModel.navigateTo(Screen.HOME) },
                    onSpinFinished = { req -> viewModel.generateAndStartAdventure(req) }
                )
                Screen.MISSION_GAMEPLAY -> {
                    val mission = activeMission
                    if (mission != null && !mission.completed) {
                        MissionGameplayScreen(
                            mission = mission,
                            onCompleteQuest = { photoUri, actualSpent -> viewModel.completeQuestWithProof(photoUri, actualSpent) },
                            onSkipQuest = { viewModel.skipCurrentQuest() },
                            onTriggerPlotTwist = { viewModel.triggerPlotTwist() },
                            onCancelMission = { viewModel.cancelActiveMission() },
                            onBackToHome = { viewModel.navigateTo(Screen.HOME) }
                        )
                    } else if (mission != null && mission.completed) {
                        viewModel.navigateTo(Screen.RECAP)
                    } else {
                        viewModel.navigateTo(Screen.HOME)
                    }
                }
                Screen.RECAP -> RecapScreen(
                    mission = lastCompletedMission,
                    userName = userProfile.name,
                    onPostToFeed = { caption, photoUri ->
                        viewModel.postMissionToFeed(caption, photoUri)
                        viewModel.navigateTo(Screen.QUEST_FEED)
                    },
                    onNewAdventure = { viewModel.startAdventureSetup(AdventureMode.BORED) },
                    onHome = { viewModel.navigateTo(Screen.HOME) }
                )
                Screen.EXPLORE -> ExploreScreen(
                    exploreRepository = viewModel.exploreRepository,
                    selectedCity = selectedCity,
                    onCityChanged = { city -> viewModel.selectExploreCity(city) },
                    onLaunchAdventure = { req -> viewModel.generateAndStartAdventure(req) }
                )
                Screen.QUEST_FEED -> QuestFeedScreen(
                    feedPosts = feedPosts,
                    onToggleLike = { postId -> viewModel.toggleLikeFeed(postId) },
                    onAddComment = { postId, text -> viewModel.addCommentToFeed(postId, text) },
                    onTryQuest = { quest ->
                        viewModel.tryQuestFromFeed(quest)
                        viewModel.startCopiedQuestAsMission(quest)
                    },
                    onOpenParties = { viewModel.navigateTo(Screen.QUEST_PARTIES) }
                )
                Screen.QUEST_PARTIES -> QuestPartiesScreen(
                    parties = questParties,
                    onBack = { viewModel.navigateTo(Screen.QUEST_FEED) },
                    onJoinParty = { id -> viewModel.joinQuestParty(id) },
                    onLeaveParty = { id -> viewModel.leaveQuestParty(id) },
                    onOpenChat = { id -> viewModel.openPartyChat(id) },
                    onCreateParty = { name, city, time, maxMembers, desc, transport ->
                        viewModel.createParty(name, city, time, maxMembers, desc, transport)
                    }
                )
                Screen.PARTY_CHAT -> {
                    val party = questParties.firstOrNull { it.id == selectedPartyId }
                    if (party != null) {
                        PartyChatScreen(
                            party = party,
                            onBack = { viewModel.navigateTo(Screen.QUEST_PARTIES) },
                            onSendMessage = { text -> viewModel.sendPartyChatMessage(party.id, text) }
                        )
                    } else {
                        viewModel.navigateTo(Screen.QUEST_PARTIES)
                    }
                }
                Screen.PROFILE, Screen.LEADERBOARD, Screen.LOGBOOK -> ProfileScreen(
                    userProfile = userProfile,
                    achievements = achievements,
                    groupDna = groupDna,
                    leaderboard = leaderboard,
                    logbook = logbookEntries,
                    initialTab = if (currentScreen == Screen.LOGBOOK) 1 else if (currentScreen == Screen.LEADERBOARD) 2 else 0,
                    onOpenLeaderboard = { viewModel.navigateTo(Screen.LEADERBOARD) },
                    onQuickAddDemoXp = { viewModel.quickAddDemoXp() },
                    onResetDemoData = { viewModel.resetDemoData() },
                    onUpdateGroupDna = { adv, food, cult, chaos, chill, bud ->
                        viewModel.updateGroupDna(adv, food, cult, chaos, chill, bud)
                    }
                )
            }

            // In-App Toast / Alert Notification
            AnimatedVisibility(
                visible = inAppAlert != null,
                enter = slideInVertically { -it } + fadeIn(),
                exit = slideOutVertically { -it } + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            ) {
                inAppAlert?.let { alertText ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MidnightElevated)
                            .border(1.5.dp, ElectricOrange, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = alertText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextOffWhite,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.dismissAlert() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
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
private fun WaynBottomNavigation(
    currentScreen: Screen,
    hasActiveMission: Boolean,
    onNavigate: (Screen) -> Unit,
    onPlayClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .background(MidnightBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(MidnightSurface)
                .border(1.dp, BorderStroke, RoundedCornerShape(26.dp))
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. HOME
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = currentScreen == Screen.HOME,
                onClick = { onNavigate(Screen.HOME) },
                testTag = "nav_home"
            )

            // 2. EXPLORE
            BottomNavItem(
                icon = Icons.Default.Explore,
                label = "Explore",
                isSelected = currentScreen == Screen.EXPLORE,
                onClick = { onNavigate(Screen.EXPLORE) },
                testTag = "nav_explore"
            )

            // 3. CENTER PLAY BUTTON (Elevated Electric Orange CTA)
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(ElectricOrange)
                    .border(2.dp, WarmYellow, CircleShape)
                    .clickable { onPlayClicked() }
                    .testTag("nav_play_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // 4. QUEST FEED
            BottomNavItem(
                icon = Icons.Default.Public,
                label = "Feed",
                isSelected = currentScreen == Screen.QUEST_FEED,
                onClick = { onNavigate(Screen.QUEST_FEED) },
                testTag = "nav_feed"
            )

            // 5. PROFILE
            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                isSelected = currentScreen == Screen.PROFILE || currentScreen == Screen.LEADERBOARD,
                onClick = { onNavigate(Screen.PROFILE) },
                testTag = "nav_profile"
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) ElectricOrange else TextMuted,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) ElectricOrange else TextMuted
        )
    }
}
