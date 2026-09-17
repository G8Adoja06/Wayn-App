package com.example.data.repository

import com.example.data.local.WaynStorage
import com.example.data.model.AdventureRequest
import com.example.data.model.LogbookEntry
import com.example.data.model.Mission
import com.example.data.model.PhotoScore
import com.example.data.model.PlotTwist
import com.example.data.service.AiAdventureService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdventureRepository(
    private val aiService: AiAdventureService,
    private val storage: WaynStorage
) {
    private val _activeMission = MutableStateFlow<Mission?>(null)
    val activeMission: StateFlow<Mission?> = _activeMission.asStateFlow()

    private val _lastCompletedMission = MutableStateFlow<Mission?>(null)
    val lastCompletedMission: StateFlow<Mission?> = _lastCompletedMission.asStateFlow()

    init {
        // Load persisted mission on startup (never lose active mission!)
        val loaded = storage.loadActiveMission()
        if (loaded != null && !loaded.completed) {
            _activeMission.value = loaded
        }
    }

    suspend fun createAndStartAdventure(request: AdventureRequest): Mission {
        val mission = aiService.generateAdventure(request)
        _activeMission.value = mission
        storage.saveActiveMission(mission)
        return mission
    }

    fun resumeMission(): Mission? {
        return _activeMission.value
    }

    fun completeCurrentQuest(
        photoUri: String?,
        photoScore: PhotoScore?,
        actualSpent: Double = 0.0
    ): Pair<Int, String> {
        val current = _activeMission.value ?: return (0 to "")
        val currentIndex = current.currentQuestIndex
        val currentQuest = current.quests.getOrNull(currentIndex) ?: return (0 to "")

        var earnedXp = currentQuest.xp
        if (photoUri != null) {
            earnedXp += currentQuest.bonusXp
        }
        if (current.activePlotTwist != null) {
            earnedXp += current.activePlotTwist.bonusXp
        }

        val updatedQuests = current.quests.toMutableList()
        updatedQuests[currentIndex] = currentQuest.copy(
            completed = true,
            photoUri = photoUri,
            photoScore = photoScore,
            actualSpent = actualSpent
        )

        val nextIndex = currentIndex + 1
        val isAllCompleted = nextIndex >= updatedQuests.size

        val newTotalSpent = updatedQuests.filter { it.completed }.sumOf { it.actualSpent }
        val newRemainingBudget = maxOf(0.0, current.initialBudgetUsd - newTotalSpent)

        val updatedMission = current.copy(
            currentQuestIndex = nextIndex,
            completed = isAllCompleted,
            quests = updatedQuests,
            photosTaken = current.photosTaken + (if (photoUri != null) 1 else 0),
            totalSpentUsd = newTotalSpent,
            remainingBudgetUsd = newRemainingBudget
        )

        _activeMission.value = updatedMission
        storage.saveActiveMission(if (isAllCompleted) null else updatedMission)

        if (isAllCompleted) {
            _lastCompletedMission.value = updatedMission
            // Automatically record in Logbook
            try {
                val dateFormat = SimpleDateFormat("h:mm a", Locale.US)
                val entry = LogbookEntry(
                    id = "log_${System.currentTimeMillis()}",
                    missionName = updatedMission.title,
                    date = "Today, ${dateFormat.format(Date())}",
                    city = updatedMission.location,
                    mode = updatedMission.mode,
                    transportMode = updatedMission.transportMode,
                    durationMinutes = updatedMission.estimatedDurationMinutes,
                    xpEarned = updatedMission.quests.filter { it.completed }.sumOf { it.xp + if (it.photoUri != null) it.bonusXp else 0 },
                    initialBudget = updatedMission.initialBudgetUsd,
                    actualTotalSpent = updatedMission.totalSpentUsd,
                    moneyLeft = updatedMission.remainingBudgetUsd,
                    questsCompleted = updatedMission.quests.count { it.completed },
                    totalQuests = updatedMission.quests.size,
                    finalRank = if (updatedMission.totalSpentUsd <= 0.0) "BUDGET NINJA" else "CHAOTIC LEGEND",
                    photoUri = updatedMission.quests.firstOrNull { it.photoUri != null }?.photoUri
                )
                storage.saveLogbookEntry(entry)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val comment = photoScore?.aiComment
            ?: "Quest cleared. +$earnedXp XP added to your street credibility."

        return (earnedXp to comment)
    }

    fun skipCurrentQuest() {
        val current = _activeMission.value ?: return
        val currentIndex = current.currentQuestIndex
        val currentQuest = current.quests.getOrNull(currentIndex) ?: return

        val updatedQuests = current.quests.toMutableList()
        updatedQuests[currentIndex] = currentQuest.copy(skipped = true)

        val nextIndex = currentIndex + 1
        val isAllCompleted = nextIndex >= updatedQuests.size

        val updatedMission = current.copy(
            currentQuestIndex = nextIndex,
            completed = isAllCompleted,
            quests = updatedQuests
        )

        _activeMission.value = updatedMission
        storage.saveActiveMission(if (isAllCompleted) null else updatedMission)

        if (isAllCompleted) {
            _lastCompletedMission.value = updatedMission
        }
    }

    fun applyPlotTwist(twist: PlotTwist) {
        val current = _activeMission.value ?: return
        val updated = current.copy(activePlotTwist = twist)
        _activeMission.value = updated
        storage.saveActiveMission(updated)
    }

    suspend fun triggerRandomPlotTwist(): PlotTwist? {
        val current = _activeMission.value ?: return null
        if (current.activePlotTwist != null) return null // Do not overwhelm with multiple twists

        val twist = aiService.generatePlotTwist(current)
        if (twist != null) {
            applyPlotTwist(twist)
        }
        return twist
    }

    fun cancelActiveMission() {
        _activeMission.value = null
        storage.saveActiveMission(null)
    }

    fun setCompletedMissionForRecap(mission: Mission) {
        _lastCompletedMission.value = mission
    }
}
