package com.example.data.repository

import com.example.data.local.WaynStorage
import com.example.data.model.Achievement
import com.example.data.model.GroupDna
import com.example.data.model.Level
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserProfileRepository(
    private val storage: WaynStorage
) {
    private val _userProfile = MutableStateFlow(
        UserProfile(
            name = storage.getUserName(),
            avatarEmoji = "⚡",
            totalXp = storage.getUserXp(),
            currentStreak = storage.getStreak(),
            lastStreakDate = storage.getLastStreakDate(),
            adventuresCompleted = storage.getAdventuresCount(),
            questsCompleted = storage.getQuestsCount(),
            interests = listOf("Food", "Adventure", "Chaos"),
            demoModeEnabled = storage.isDemoMode()
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _achievements = MutableStateFlow(createDefaultAchievements())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _groupDna = MutableStateFlow(GroupDna())
    val groupDna: StateFlow<GroupDna> = _groupDna.asStateFlow()

    init {
        recalculateBehavioralDna()
    }

    private fun createDefaultAchievements(): List<Achievement> {
        val completed = _userProfile.value.adventuresCompleted
        val quests = _userProfile.value.questsCompleted

        return listOf(
            Achievement(
                id = "broke_alive",
                title = "CHEAPSKATE LEGEND",
                description = "Master of zero-dollar street survival in Lebanese neighborhoods.",
                emoji = "💸",
                xpReward = 450,
                isUnlocked = true,
                progress = 0.7f,
                currentTier = com.example.data.model.AchievementTier.SILVER,
                bronzeReq = "1 Broke mission",
                silverReq = "3 Broke missions",
                goldReq = "6 Broke missions",
                unlockedTiers = setOf(com.example.data.model.AchievementTier.BRONZE, com.example.data.model.AchievementTier.SILVER),
                nextTierDesc = "Complete 2 more $0 missions for Gold 🥇"
            ),
            Achievement(
                id = "chaos_merchant",
                title = "CHAOS SURVIVOR",
                description = "Thrive under unexpected plot twists and unhinged Lebanese dares.",
                emoji = "🔥",
                xpReward = 500,
                isUnlocked = true,
                progress = 0.5f,
                currentTier = com.example.data.model.AchievementTier.BRONZE,
                bronzeReq = "1 Chaos run",
                silverReq = "4 Chaos runs",
                goldReq = "8 Chaos runs",
                unlockedTiers = setOf(com.example.data.model.AchievementTier.BRONZE),
                nextTierDesc = "Clear 2 more Chaos runs for Silver 🥈"
            ),
            Achievement(
                id = "street_explorer",
                title = "STREET EXPLORER",
                description = "Complete neighborhood quests and uncover secret alleys on foot.",
                emoji = "🚶",
                xpReward = 600,
                isUnlocked = true,
                progress = 0.85f,
                currentTier = com.example.data.model.AchievementTier.SILVER,
                bronzeReq = "5 quests",
                silverReq = "15 quests",
                goldReq = "30 quests",
                unlockedTiers = setOf(com.example.data.model.AchievementTier.BRONZE, com.example.data.model.AchievementTier.SILVER),
                nextTierDesc = "${30 - quests.coerceAtMost(30)} more quests for Gold 🥇"
            ),
            Achievement(
                id = "culinary_odyssey",
                title = "MEDITERRANEAN FOODIE",
                description = "Toum, cheese pull, and authentic street snack expeditions.",
                emoji = "🍔",
                xpReward = 550,
                isUnlocked = true,
                progress = 0.65f,
                currentTier = com.example.data.model.AchievementTier.BRONZE,
                bronzeReq = "2 Food missions",
                silverReq = "5 Food missions",
                goldReq = "10 Food missions",
                unlockedTiers = setOf(com.example.data.model.AchievementTier.BRONZE),
                nextTierDesc = "Complete 2 more Food runs for Silver 🥈"
            ),
            Achievement(
                id = "main_character",
                title = "HIGH DRAMA POSE",
                description = "Stage aggressive high-fashion poses in ordinary public spaces.",
                emoji = "🎬",
                xpReward = 400,
                isUnlocked = true,
                progress = 1.0f,
                currentTier = com.example.data.model.AchievementTier.GOLD,
                bronzeReq = "1 Photo proof",
                silverReq = "5 Photo proofs",
                goldReq = "10 Photo proofs",
                unlockedTiers = setOf(com.example.data.model.AchievementTier.BRONZE, com.example.data.model.AchievementTier.SILVER, com.example.data.model.AchievementTier.GOLD),
                nextTierDesc = "Max Tier Gold achieved! 🥇"
            ),
            Achievement(
                id = "grand_voyager",
                title = "OUTSIDE BEIRUT VOYAGER",
                description = "Expand your horizons to Byblos, Batroun, Jounieh, Zahle, Tripoli & Sidon.",
                emoji = "📍",
                xpReward = 750,
                isUnlocked = completed >= 2,
                progress = (completed.toFloat() / 6f).coerceIn(0f, 1f),
                currentTier = if (completed >= 5) com.example.data.model.AchievementTier.GOLD else if (completed >= 2) com.example.data.model.AchievementTier.BRONZE else null,
                bronzeReq = "1 Outer city",
                silverReq = "3 Outer cities",
                goldReq = "5 Outer cities",
                unlockedTiers = if (completed >= 5) setOf(com.example.data.model.AchievementTier.BRONZE, com.example.data.model.AchievementTier.SILVER, com.example.data.model.AchievementTier.GOLD) else if (completed >= 2) setOf(com.example.data.model.AchievementTier.BRONZE) else emptySet(),
                nextTierDesc = "Conquer 2 more Lebanese cities for Silver 🥈"
            )
        )
    }

    fun addXp(amount: Int): Boolean {
        val current = _userProfile.value
        val oldLevel = current.level
        val newXp = current.totalXp + amount
        storage.setUserXp(newXp)

        val updated = current.copy(totalXp = newXp)
        _userProfile.value = updated

        // Returns true if level up occurred!
        return updated.level.levelNumber > oldLevel.levelNumber
    }

    fun updateUserName(name: String) {
        storage.setUserName(name)
        _userProfile.value = _userProfile.value.copy(name = name)
    }

    fun recordQuestCompleted() {
        val current = _userProfile.value
        val newCount = current.questsCompleted + 1
        storage.setQuestsCount(newCount)
        _userProfile.value = current.copy(questsCompleted = newCount)
    }

    fun recordAdventureCompleted() {
        val current = _userProfile.value
        val newCount = current.adventuresCompleted + 1
        storage.setAdventuresCount(newCount)
        _userProfile.value = current.copy(adventuresCompleted = newCount)
        checkStreak()
    }

    fun checkStreak(): Int {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val current = _userProfile.value

        if (current.lastStreakDate == todayStr) {
            return current.currentStreak
        }

        val newStreak = current.currentStreak + 1
        storage.setStreak(newStreak)
        storage.setLastStreakDate(todayStr)
        _userProfile.value = current.copy(
            currentStreak = newStreak,
            lastStreakDate = todayStr
        )
        return newStreak
    }

    fun recalculateBehavioralDna() {
        val logbook = storage.getLogbookEntries()
        val user = _userProfile.value
        
        val totalAdventures = maxOf(1, user.adventuresCompleted)
        val onFootCount = logbook.count { it.transportMode == com.example.data.model.TransportMode.WALK }
        val adventurePct = (72 + (onFootCount * 4).coerceAtMost(24))
        
        val foodCount = logbook.count { it.missionName.contains("Flavor", ignoreCase = true) || it.missionName.contains("Food", ignoreCase = true) }
        val foodPct = (82 + (foodCount * 3).coerceAtMost(16))
        
        val avgSaved = if (logbook.isNotEmpty()) logbook.map { it.moneyLeft }.average() else 12.0
        val budgetPct = (45 + (avgSaved * 2.2).toInt().coerceIn(0, 48))
        
        val chaosPct = (86 + (user.questsCompleted % 9))
        val chillPct = (52 + (totalAdventures * 3).coerceAtMost(36))
        val culturePct = (58 + (logbook.size * 5).coerceAtMost(34))
        
        val compatibility = ((adventurePct * 0.2 + foodPct * 0.3 + culturePct * 0.1 + chaosPct * 0.2 + chillPct * 0.1 + budgetPct * 0.1)).toInt()
        
        val verdict = when {
            chaosPct > 90 && foodPct > 85 -> "Statistical match: You agree on zero life decisions except street food and unhinged plot twists."
            chillPct > 70 -> "High Mediterranean chill: Your squad moves slow, drinks iced coffee, and avoids stairs at all costs."
            budgetPct > 75 -> "Resourceful survival squad: High budget savings, maximum mileage, zero unnecessary spending."
            else -> "Spontaneous Urban Explorers: Solid balance of walking stamina, photo drama, and culinary runs."
        }
        
        _groupDna.value = GroupDna(
            adventurePct = adventurePct.coerceIn(10, 99),
            foodPct = foodPct.coerceIn(10, 99),
            culturePct = culturePct.coerceIn(10, 99),
            chaosPct = chaosPct.coerceIn(10, 99),
            chillPct = chillPct.coerceIn(10, 99),
            budgetPct = budgetPct.coerceIn(10, 99),
            compatibilityPct = compatibility.coerceIn(10, 99),
            aiVerdict = verdict
        )
    }

    fun updateGroupDna(
        adventure: Int,
        food: Int,
        culture: Int,
        chaos: Int,
        chill: Int,
        budget: Int
    ) {
        recalculateBehavioralDna()
    }

    fun toggleDemoMode() {
        val current = _userProfile.value
        val newValue = !current.demoModeEnabled
        storage.setDemoMode(newValue)
        _userProfile.value = current.copy(demoModeEnabled = newValue)
    }

    fun resetDemoData() {
        storage.setUserXp(1840)
        storage.setStreak(4)
        storage.setAdventuresCount(6)
        storage.setQuestsCount(21)
        storage.saveActiveMission(null)
        _userProfile.value = UserProfile(
            name = storage.getUserName(),
            avatarEmoji = "⚡",
            totalXp = 1840,
            currentStreak = 4,
            lastStreakDate = "",
            adventuresCompleted = 6,
            questsCompleted = 21,
            interests = listOf("Food", "Adventure", "Chaos"),
            demoModeEnabled = true
        )
        _achievements.value = createDefaultAchievements()
    }
}
