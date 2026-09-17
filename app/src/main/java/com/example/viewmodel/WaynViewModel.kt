package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.WaynStorage
import com.example.data.model.AdventureMode
import com.example.data.model.AdventureRequest
import com.example.data.model.LogbookEntry
import com.example.data.model.Mission
import com.example.data.model.PhotoScore
import com.example.data.model.PlotTwist
import com.example.data.model.Quest
import com.example.data.model.SecondaryMood
import com.example.data.repository.AdventureRepository
import com.example.data.repository.DailyQuestRepository
import com.example.data.repository.ExploreRepository
import com.example.data.repository.SocialRepository
import com.example.data.repository.UserProfileRepository
import com.example.data.service.GeminiAiAdventureService
import com.example.data.service.LocalAiAdventureService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    SPLASH,
    ONBOARDING,
    HOME,
    ADVENTURE_SETUP,
    ROULETTE,
    MISSION_GAMEPLAY,
    RECAP,
    EXPLORE,
    QUEST_FEED,
    QUEST_PARTIES,
    PARTY_CHAT,
    PROFILE,
    LEADERBOARD,
    LOGBOOK
}

class WaynViewModel(application: Application) : AndroidViewModel(application) {

    private val storage = WaynStorage(application)
    private val localAi = LocalAiAdventureService()
    private val aiService = GeminiAiAdventureService(localAi)

    val adventureRepository = AdventureRepository(aiService, storage)
    val userProfileRepository = UserProfileRepository(storage)
    val socialRepository = SocialRepository(storage)
    val exploreRepository = ExploreRepository()
    val dailyQuestRepository = DailyQuestRepository(application)

    private val _currentScreen = MutableStateFlow<Screen>(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _activeSetupMode = MutableStateFlow(AdventureMode.BORED)
    val activeSetupMode: StateFlow<AdventureMode> = _activeSetupMode.asStateFlow()

    private val _selectedPartyId = MutableStateFlow<String?>(null)
    val selectedPartyId: StateFlow<String?> = _selectedPartyId.asStateFlow()

    private val _inAppAlert = MutableStateFlow<String?>(null)
    val inAppAlert: StateFlow<String?> = _inAppAlert.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _logbookEntries = MutableStateFlow<List<LogbookEntry>>(storage.getLogbookEntries())
    val logbookEntries: StateFlow<List<LogbookEntry>> = _logbookEntries.asStateFlow()

    val activeMission = adventureRepository.activeMission
    val lastCompletedMission = adventureRepository.lastCompletedMission
    val userProfile = userProfileRepository.userProfile
    val achievements = userProfileRepository.achievements
    val groupDna = userProfileRepository.groupDna
    val feedPosts = socialRepository.feedPosts
    val questParties = socialRepository.questParties
    val leaderboard = socialRepository.leaderboardEntries
    val dailyQuest = dailyQuestRepository.dailyQuest
    val selectedCity = exploreRepository.selectedCity

    init {
        // Check if onboarding completed
        viewModelScope.launch {
            delay(1600) // Splash presentation
            if (storage.isOnboardingCompleted()) {
                _currentScreen.value = Screen.HOME
            } else {
                _currentScreen.value = Screen.ONBOARDING
            }
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun completeOnboarding(name: String) {
        if (name.isNotBlank()) {
            userProfileRepository.updateUserName(name.trim())
        }
        storage.setOnboardingCompleted(true)
        _currentScreen.value = Screen.HOME
    }

    fun startAdventureSetup(mode: AdventureMode) {
        _activeSetupMode.value = mode
        _currentScreen.value = Screen.ADVENTURE_SETUP
    }

    fun openRoulette() {
        _currentScreen.value = Screen.ROULETTE
    }

    fun openPartyChat(partyId: String) {
        _selectedPartyId.value = partyId
        _currentScreen.value = Screen.PARTY_CHAT
    }

    fun generateAndStartAdventure(request: AdventureRequest) {
        viewModelScope.launch {
            _isGenerating.value = true
            delay(900) // Realistic AI synthesis animation
            val mission = adventureRepository.createAndStartAdventure(request)
            _isGenerating.value = false
            _currentScreen.value = Screen.MISSION_GAMEPLAY
            showAlert("🔥 ${mission.title} activated! Yalla let's go.")
        }
    }

    fun completeQuestWithProof(photoUri: String?, actualSpent: Double = 0.0) {
        val currentMission = activeMission.value ?: return
        val currentQuest = currentMission.currentQuest() ?: return

        viewModelScope.launch {
            val score = localAi.ratePhotoChallenge(photoUri, currentQuest.title)
            val (earnedXp, comment) = adventureRepository.completeCurrentQuest(photoUri, score, actualSpent)

            userProfileRepository.recordQuestCompleted()
            val leveledUp = userProfileRepository.addXp(earnedXp)
            socialRepository.updateLeaderboardUserScore(userProfile.value.totalXp)

            if (leveledUp) {
                showAlert("🎉 LEVEL UP! You are now a ${userProfile.value.level.title}!")
            } else {
                showAlert("+$earnedXp XP! $comment")
            }

            // Check if mission is complete
            val updated = adventureRepository.activeMission.value
            if (updated == null || updated.completed) {
                userProfileRepository.recordAdventureCompleted()
                refreshLogbook()
                _currentScreen.value = Screen.RECAP
            }
        }
    }

    fun refreshLogbook() {
        _logbookEntries.value = storage.getLogbookEntries()
    }

    fun createQuestParty(title: String, location: String, time: String, maxMembers: Int) {
        socialRepository.createParty(title, location, time, maxMembers, userProfile.value.name)
        showAlert("🎉 Quest Party created! Other adventurers can now join.")
    }

    fun createFeedPost(caption: String, questTitle: String = "Spontaneous Adventure", photoPlaceholder: String = "📸") {
        socialRepository.createFeedPost(caption, questTitle, photoPlaceholder, userProfile.value.name)
        showAlert("🚀 Posted to Community Feed!")
    }

    fun skipCurrentQuest() {
        adventureRepository.skipCurrentQuest()
        showAlert("Quest skipped. No penalty, but we saw that.")

        val updated = adventureRepository.activeMission.value
        if (updated == null || updated.completed) {
            refreshLogbook()
            _currentScreen.value = Screen.RECAP
        }
    }

    fun triggerPlotTwist() {
        viewModelScope.launch {
            val twist = adventureRepository.triggerRandomPlotTwist()
            if (twist != null) {
                showAlert("${twist.emoji} ${twist.title}: ${twist.description}")
            } else {
                showAlert("No twist safe for current constraints right now.")
            }
        }
    }

    fun cancelActiveMission() {
        adventureRepository.cancelActiveMission()
        _currentScreen.value = Screen.HOME
        showAlert("Mission abandoned. We will pretend that never happened.")
    }

    fun completeDailyQuest() {
        val earned = dailyQuestRepository.completeDailyQuest()
        if (earned > 0) {
            val leveledUp = userProfileRepository.addXp(earned)
            val newStreak = userProfileRepository.checkStreak()
            if (leveledUp) {
                showAlert("🎉 Daily Quest complete! +$earned XP & LEVEL UP!")
            } else {
                showAlert("🎯 Daily Quest complete! +$earned XP. Streak: $newStreak days!")
            }
        }
    }

    fun toggleLikeFeed(postId: String) {
        socialRepository.toggleLike(postId)
    }

    fun addCommentToFeed(postId: String, text: String) {
        socialRepository.addComment(postId, text, userProfile.value.name)
        showAlert("Comment posted!")
    }

    fun tryQuestFromFeed(quest: Quest) {
        socialRepository.copyQuestToUserQuests(quest)
        showAlert("✅ Added \"${quest.title}\" to your saved quests!")
    }

    fun startCopiedQuestAsMission(quest: Quest) {
        val req = AdventureRequest(
            location = quest.location.split(",").lastOrNull()?.trim() ?: "Beirut",
            budget = "$${quest.estimatedCostUsd.toInt()}",
            duration = "${quest.estimatedDurationMinutes} min",
            mode = AdventureMode.BORED,
            secondaryMood = SecondaryMood.CHAOS
        )
        generateAndStartAdventure(req)
    }

    fun createParty(
        title: String,
        location: String,
        time: String,
        maxMembers: Int,
        description: String,
        transportMode: String
    ) {
        val user = userProfile.value
        val newParty = socialRepository.createParty(
            title = title,
            location = location,
            time = time,
            maxMembers = maxMembers,
            hostName = user.name,
            description = description,
            transportMode = transportMode
        )
        _selectedPartyId.value = newParty.id
        _currentScreen.value = Screen.PARTY_CHAT
        showAlert("🎉 Party \"$title\" created! You are the host.")
    }

    fun postMissionToFeed(caption: String, photoUri: String? = null) {
        val mission = lastCompletedMission.value ?: activeMission.value ?: return
        val user = userProfile.value
        socialRepository.createFeedPost(
            caption = caption.ifBlank { "Surviving Beirut one weird decision at a time" },
            questTitle = mission.title,
            photoEmoji = "📸",
            authorName = user.name,
            photoUri = photoUri ?: mission.quests.lastOrNull { it.photoUri != null }?.photoUri,
            location = mission.location,
            xpEarned = mission.earnedXp(),
            totalSpent = mission.totalSpentUsd,
            questsCompletedCount = mission.completedQuestsCount()
        )
        showAlert("🚀 Mission posted to Quest Feed!")
    }

    fun joinQuestParty(partyId: String) {
        socialRepository.joinParty(partyId, userProfile.value.name)
        showAlert("Joined party! Chat is now active.")
    }

    fun leaveQuestParty(partyId: String) {
        socialRepository.leaveParty(partyId, userProfile.value.name)
        showAlert("Left party.")
    }

    fun sendPartyChatMessage(partyId: String, text: String) {
        if (text.isNotBlank()) {
            socialRepository.sendPartyMessage(partyId, text.trim(), userProfile.value.name)
        }
    }

    fun selectExploreCity(city: String) {
        exploreRepository.setSelectedCity(city)
        showAlert("📍 Switched location to $city")
    }

    fun updateGroupDna(adv: Int, food: Int, cult: Int, chaos: Int, chill: Int, bud: Int) {
        userProfileRepository.updateGroupDna(adv, food, cult, chaos, chill, bud)
        showAlert("Group DNA calibrated!")
    }

    fun quickAddDemoXp() {
        val leveledUp = userProfileRepository.addXp(500)
        socialRepository.updateLeaderboardUserScore(userProfile.value.totalXp)
        if (leveledUp) {
            showAlert("⚡ Demo Mode: +500 XP added! LEVEL UP to ${userProfile.value.level.title}!")
        } else {
            showAlert("⚡ Demo Mode: +500 XP added! Current: ${userProfile.value.totalXp} XP")
        }
    }

    fun resetDemoData() {
        userProfileRepository.resetDemoData()
        adventureRepository.cancelActiveMission()
        showAlert("🔄 Demo data reset to fresh state.")
    }

    fun showAlert(message: String) {
        _inAppAlert.value = message
    }

    fun dismissAlert() {
        _inAppAlert.value = null
    }
}
