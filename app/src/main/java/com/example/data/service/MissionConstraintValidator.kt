package com.example.data.service

import com.example.data.model.PlotTwist
import com.example.data.model.PlotTwistType
import com.example.data.model.Quest
import com.example.data.model.QuestType
import com.example.data.model.TransportMode

object MissionConstraintValidator {

    /**
     * Validates if a quest satisfies all hard user constraints:
     * - Starting/remaining budget: If budget <= 0, no paid food or purchase requirements.
     * - Transportation: If WALK, must not require car/driving/long distance.
     * - Time: Quest duration must fit in available time buffer.
     * - City/Location: Quest must belong to or be close to the active area.
     */
    fun isQuestValid(
        quest: Quest,
        budgetRemaining: Double,
        timeRemainingMinutes: Int,
        transportMode: TransportMode,
        location: String,
        groupSize: String = "3"
    ): Boolean {
        // 1. Budget Hard Constraint ($0 Mission Rules)
        if (budgetRemaining <= 0.0) {
            if (quest.estimatedCostUsd > 0.0) return false
            // Check descriptions for mandatory spending keywords
            val lowerDesc = quest.description.lowercase()
            val lowerTitle = quest.title.lowercase()
            val forbiddenPaidKeywords = listOf(
                "buy ", "purchase", "order ", "pay for", "ticket",
                "restaurant meal", "coffee cup", "cocktail", "shisha", "degustation"
            )
            if (forbiddenPaidKeywords.any { lowerDesc.contains(it) || lowerTitle.contains(it) }) {
                // Must be explicitly optional if mentioning buying
                if (!lowerDesc.contains("optional") && !lowerDesc.contains("free") && !lowerDesc.contains("without spending")) {
                    return false
                }
            }
        } else {
            // Must not aggressively exceed remaining budget
            if (quest.estimatedCostUsd > budgetRemaining + 0.5) {
                return false
            }
        }

        // 2. Transportation Hard Constraint
        if (transportMode == TransportMode.WALK) {
            val lowerDesc = quest.description.lowercase()
            val lowerTitle = quest.title.lowercase()
            val carKeywords = listOf("drive", "highway", "car ride", "uber", "taxi", "park your car")
            if (carKeywords.any { lowerDesc.contains(it) || lowerTitle.contains(it) }) {
                return false
            }
            // Cannot travel outside the current city/neighborhood
            if (quest.location.isNotBlank() && !isLocationCompatible(quest.location, location, TransportMode.WALK)) {
                return false
            }
        }

        // 3. Time Hard Constraint
        if (timeRemainingMinutes > 0 && quest.estimatedDurationMinutes > timeRemainingMinutes) {
            return false
        }

        // 4. Location Hard Constraint
        if (!isLocationCompatible(quest.location, location, transportMode)) {
            return false
        }

        return true
    }

    /**
     * Validates if a plot twist can safely be applied without breaking user constraints.
     */
    fun isPlotTwistValid(
        twist: PlotTwist,
        budgetRemaining: Double,
        timeRemainingMinutes: Int,
        transportMode: TransportMode
    ): Boolean {
        if (budgetRemaining <= 0.0 && twist.type == PlotTwistType.BUDGET_CUT) {
            return false
        }
        if (transportMode == TransportMode.WALK && twist.description.lowercase().contains("car")) {
            return false
        }
        if (timeRemainingMinutes < 15 && twist.type == PlotTwistType.SIDE_QUEST) {
            return false
        }
        return true
    }

    private fun isLocationCompatible(questLocation: String, targetCity: String, mode: TransportMode): Boolean {
        if (questLocation.isBlank() || targetCity.isBlank()) return true
        val qLoc = questLocation.lowercase()
        val tCity = targetCity.lowercase()

        if (qLoc.contains(tCity)) return true

        // Allow regional walking proximity if mode is WALK
        if (mode == TransportMode.WALK) {
            return qLoc.contains(tCity)
        }

        return true
    }

    /**
     * Validates an entire list of assembled quests against mission constraints,
     * pruning any quests that violate total budget, time, or location.
     */
    fun validateFullMission(
        quests: List<Quest>,
        budgetTotal: Double,
        durationMinutes: Int,
        transportMode: TransportMode,
        city: String
    ): List<Quest> {
        var remainingBudget = budgetTotal
        var remainingTime = durationMinutes
        val validQuests = mutableListOf<Quest>()

        for (quest in quests) {
            if (isQuestValid(quest, remainingBudget, remainingTime, transportMode, city)) {
                // If budget is $0, must stay $0
                if (budgetTotal <= 0.05 && quest.estimatedCostUsd > 0.05) {
                    continue
                }
                validQuests.add(quest)
                if (budgetTotal > 0.05) {
                    remainingBudget = maxOf(0.0, remainingBudget - quest.estimatedCostUsd)
                }
                remainingTime = maxOf(0, remainingTime - quest.estimatedDurationMinutes)
            }
        }
        return validQuests
    }

    data class QuestCompatibilityResult(
        val isCompatible: Boolean,
        val reason: String? = null,
        val suggestedReplacementTitle: String? = null
    )

    fun checkQuestCompatibility(
        quest: Quest,
        currentBudget: Double,
        currentDuration: Int,
        currentTransport: TransportMode,
        currentCity: String
    ): QuestCompatibilityResult {
        if (currentBudget <= 0.05 && quest.estimatedCostUsd > 0.05) {
            return QuestCompatibilityResult(
                isCompatible = false,
                reason = "This quest normally costs about $${quest.estimatedCostUsd.toInt()}. Your current budget is $0."
            )
        }
        if (currentBudget > 0.05 && quest.estimatedCostUsd > currentBudget + 0.5) {
            return QuestCompatibilityResult(
                isCompatible = false,
                reason = "This quest normally costs about $${quest.estimatedCostUsd.toInt()}. Your current budget is $${currentBudget.toInt()}."
            )
        }
        if (!isLocationCompatible(quest.location, currentCity, currentTransport)) {
            return QuestCompatibilityResult(
                isCompatible = false,
                reason = "This quest is in ${quest.location}, while you are currently set to $currentCity."
            )
        }
        return QuestCompatibilityResult(isCompatible = true)
    }
}

