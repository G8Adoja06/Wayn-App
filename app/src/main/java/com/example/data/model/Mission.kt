package com.example.data.model

data class PhotoScore(
    val drama: Int = 92,
    val composition: Int = 68,
    val unnecessaryIntensity: Int = 98,
    val totalScore: Int = 86,
    val aiComment: String = "We have concerns, but we respect the commitment."
)

enum class PlotTwistType {
    BUDGET_CUT,
    SIDE_QUEST,
    WALK_IT_OFF,
    DOUBLE_OR_NOTHING,
    SPEED_RUN
}

data class PlotTwist(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val bonusXp: Int,
    val type: PlotTwistType,
    val applied: Boolean = true
)

data class Quest(
    val id: String,
    val title: String,
    val description: String,
    val type: QuestType,
    val xp: Int,
    val bonusXp: Int = 75,
    val estimatedDurationMinutes: Int = 15,
    val estimatedCostUsd: Double = 2.0,
    val actualSpent: Double = 0.0,
    val location: String = "Local Area",
    val bonusChallenge: String? = null,
    val completed: Boolean = false,
    val skipped: Boolean = false,
    val proofRequired: Boolean = true,
    val photoUri: String? = null,
    val photoScore: PhotoScore? = null,
    val funnyComment: String? = null
) {
    val locationData: String get() = location
    val proof: String? get() = photoUri
}

data class Mission(
    val id: String,
    val title: String,
    val subtitle: String,
    val tagline: String = "Turn anywhere into an adventure",
    val difficulty: String = "Medium",
    val totalPossibleXP: Int,
    val estimatedDurationMinutes: Int,
    val estimatedCostUsd: Double,
    val initialBudgetUsd: Double = estimatedCostUsd,
    val remainingBudgetUsd: Double = initialBudgetUsd,
    val totalSpentUsd: Double = 0.0,
    val missionStartEpochMillis: Long,
    val missionEndEpochMillis: Long,
    val currentQuestIndex: Int = 0,
    val completed: Boolean = false,
    val quests: List<Quest>,
    val activePlotTwist: PlotTwist? = null,
    val mode: AdventureMode = AdventureMode.BORED,
    val secondaryMood: SecondaryMood = SecondaryMood.CHAOS,
    val location: String = "Beirut",
    val groupSize: String = "3",
    val transportMode: TransportMode = TransportMode.WALK,
    val transportation: String = transportMode.displayName,
    val photosTaken: Int = 0,
    val questionableDecisions: Int = 1
) {
    val initialBudget: Double get() = initialBudgetUsd
    val remainingBudget: Double get() = remainingBudgetUsd
    val totalSpent: Double get() = totalSpentUsd
    val moneyLeft: Double get() = maxOf(0.0, initialBudgetUsd - totalSpentUsd)
    val availableDuration: Int get() = estimatedDurationMinutes
    val startTimestamp: Long get() = missionStartEpochMillis
    val endTimestamp: Long get() = missionEndEpochMillis
    val status: String get() = if (completed) "COMPLETED" else "ACTIVE"

    fun remainingMillis(): Long {
        val now = System.currentTimeMillis()
        return maxOf(0L, missionEndEpochMillis - now)
    }

    fun isExpired(): Boolean = remainingMillis() <= 0L

    fun currentQuest(): Quest? = quests.getOrNull(currentQuestIndex)

    fun completedQuestsCount(): Int = quests.count { it.completed }

    fun earnedXp(): Int {
        var sum = 0
        for (q in quests) {
            if (q.completed) {
                sum += q.xp
                if (q.photoUri != null) {
                    sum += q.bonusXp
                }
            }
        }
        if (activePlotTwist != null) {
            sum += activePlotTwist.bonusXp
        }
        return sum
    }

    fun formattedRemainingTime(): String {
        val remaining = remainingMillis() / 1000
        val minutes = remaining / 60
        val seconds = remaining % 60
        return if (minutes >= 60) {
            val hours = minutes / 60
            val mins = minutes % 60
            String.format("%02d:%02d:%02d", hours, mins, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}

data class LogbookEntry(
    val id: String,
    val missionName: String,
    val date: String,
    val city: String,
    val mode: AdventureMode,
    val transportMode: TransportMode,
    val durationMinutes: Int,
    val xpEarned: Int,
    val initialBudget: Double,
    val actualTotalSpent: Double,
    val moneyLeft: Double,
    val questsCompleted: Int,
    val totalQuests: Int,
    val finalRank: String,
    val photoUri: String? = null
)
