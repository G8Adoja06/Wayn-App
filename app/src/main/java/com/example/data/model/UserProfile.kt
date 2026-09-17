package com.example.data.model

data class Level(
    val levelNumber: Int,
    val title: String,
    val minXp: Int,
    val maxXp: Int,
    val perk: String
) {
    val maxForNext: Int get() = maxXp

    companion object {
        val LEVELS = listOf(
            Level(1, "Lost Tourist", 0, 499, "Can complain about Google Maps"),
            Level(2, "Weekend Rookie", 500, 1499, "Knows where to get 2 AM manousheh"),
            Level(3, "Local Scout", 1500, 2999, "Spots hidden alleys in Hamra & Mar Mikhael"),
            Level(4, "Street Explorer", 3000, 4999, "Master of finding parking in Beirut"),
            Level(5, "Chaos Specialist", 5000, 7999, "Thrives on zero-budget adventures"),
            Level(6, "Beirut Veteran", 8000, 11999, "Can negotiate in 3 languages simultaneously"),
            Level(7, "Wayn Legend", 12000, 999999, "Immortal. The city asks you what to do.")
        )

        fun fromXp(xp: Int): Level {
            return LEVELS.lastOrNull { xp >= it.minXp } ?: LEVELS.first()
        }
    }
}

enum class AchievementTier(val label: String, val badge: String) {
    BRONZE("Bronze", "🥉"),
    SILVER("Silver", "🥈"),
    GOLD("Gold", "🥇")
}

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val xpReward: Int,
    val isUnlocked: Boolean = false,
    val progress: Float = 0f, // 0f to 1f towards next or current tier
    val currentTier: AchievementTier? = null,
    val bronzeReq: String = "1 completion",
    val silverReq: String = "3 completions",
    val goldReq: String = "5 completions",
    val unlockedTiers: Set<AchievementTier> = emptySet(),
    val nextTierDesc: String = ""
)

data class GroupDna(
    val adventurePct: Int = 82,
    val foodPct: Int = 94,
    val culturePct: Int = 52,
    val chaosPct: Int = 97,
    val chillPct: Int = 41,
    val budgetPct: Int = 23,
    val compatibilityPct: Int = 63,
    val aiVerdict: String = "You agree on absolutely nothing except food. Proceed with caution."
)

data class DailyQuest(
    val id: String,
    val dateString: String,
    val title: String,
    val description: String,
    val locationTarget: String,
    val rewardXp: Int = 120,
    val completed: Boolean = false,
    val emoji: String = "🎯"
)

data class UserProfile(
    val name: String = "You",
    val avatarEmoji: String = "⚡",
    val totalXp: Int = 1840,
    val currentStreak: Int = 4,
    val lastStreakDate: String = "",
    val adventuresCompleted: Int = 6,
    val questsCompleted: Int = 21,
    val interests: List<String> = listOf("Food", "Adventure", "Chaos"),
    val demoModeEnabled: Boolean = true
) {
    val level: Level get() = Level.fromXp(totalXp)
}
