package com.example.data.service

import com.example.data.local.CityData
import com.example.data.model.AdventureMode
import com.example.data.model.AdventureRequest
import com.example.data.model.Mission
import com.example.data.model.PhotoScore
import com.example.data.model.PlotTwist
import com.example.data.model.PlotTwistType
import com.example.data.model.SecondaryMood
import com.example.data.repository.QuestCatalogRepository
import kotlin.random.Random

class LocalAiAdventureService(
    private val questCatalogRepository: QuestCatalogRepository = QuestCatalogRepository()
) : AiAdventureService {

    override suspend fun generateAdventure(request: AdventureRequest): Mission {
        // Parse budget as numeric maximum
        val budgetNum = parseBudget(request.budget)

        // Parse duration accurately (FIXING TIMER BUG!)
        val durationMinutes = parseDurationMinutes(request.duration, request.mode)

        // Step 1: Assemble quests strictly from the shared QuestCatalogRepository
        val catalogQuests = questCatalogRepository.assembleMission(
            city = request.location,
            mode = request.mode,
            transportMode = request.transportMode,
            budgetMax = budgetNum,
            durationMinutes = durationMinutes,
            secondaryMood = request.secondaryMood,
            groupSize = request.groupSize
        )

        // Step 2: Validate via MissionConstraintValidator
        var quests = MissionConstraintValidator.validateFullMission(
            quests = catalogQuests,
            budgetTotal = budgetNum,
            durationMinutes = durationMinutes,
            transportMode = request.transportMode,
            city = request.location
        )

        // Fallback to sample quests only if catalog returned empty
        if (quests.isEmpty()) {
            quests = CityData.getSampleQuests(
                city = request.location,
                mode = request.mode,
                mood = request.secondaryMood,
                budgetMax = budgetNum,
                durationMinutes = durationMinutes,
                transportMode = request.transportMode
            )
        }

        val totalPossibleXp = quests.sumOf { it.xp + it.bonusXp }
        val estimatedCost = quests.sumOf { it.estimatedCostUsd }

        val startMillis = System.currentTimeMillis()
        val endMillis = startMillis + (durationMinutes * 60 * 1000L)

        val (title, subtitle) = generateTitles(request)

        val difficulty = when {
            request.mode == AdventureMode.BROKE -> "Resourceful"
            request.secondaryMood == SecondaryMood.CHAOS -> "Unhinged"
            request.secondaryMood == SecondaryMood.CHILL -> "Immaculate"
            durationMinutes >= 120 -> "Endurance"
            else -> "Spontaneous"
        }

        return Mission(
            id = "mission_${System.currentTimeMillis()}_${Random.nextInt(1000, 9999)}",
            title = title,
            subtitle = subtitle,
            tagline = "Turn anywhere into an adventure.",
            difficulty = difficulty,
            totalPossibleXP = totalPossibleXp,
            estimatedDurationMinutes = durationMinutes,
            estimatedCostUsd = estimatedCost,
            initialBudgetUsd = budgetNum,
            remainingBudgetUsd = budgetNum,
            totalSpentUsd = 0.0,
            missionStartEpochMillis = startMillis,
            missionEndEpochMillis = endMillis,
            currentQuestIndex = 0,
            completed = false,
            quests = quests,
            activePlotTwist = null,
            mode = request.mode,
            secondaryMood = request.secondaryMood,
            location = request.location,
            groupSize = request.groupSize,
            transportMode = request.transportMode,
            transportation = request.transportMode.displayName
        )
    }

    override suspend fun generateFunnyReaction(context: String): String {
        return when {
            context.contains("budget_zero", ignoreCase = true) ->
                "Zero dollars? Beautiful. Let’s get creative."
            context.contains("chaos", ignoreCase = true) ->
                "You picked Chaos. That sounds like a personal problem."
            context.contains("complete", ignoreCase = true) ->
                "Against all odds, you did it."
            context.contains("fail", ignoreCase = true) ->
                "We will pretend that never happened."
            context.contains("late", ignoreCase = true) ->
                "You had one job."
            context.contains("broke", ignoreCase = true) ->
                "Ma fi budget? Fine. We improvise."
            else ->
                "Yalla. The city is waiting."
        }
    }

    override suspend fun ratePhotoChallenge(photoUri: String?, questTitle: String): PhotoScore {
        return LocalPhotoJudge.scorePhoto(photoUri, questTitle)
    }

    override suspend fun generatePlotTwist(currentMission: Mission): PlotTwist? {
        // Safety constraint validation: never violate budget, time, transportation safety
        val twists = mutableListOf<PlotTwist>()

        // 1. Budget cut only if remaining budget allows
        if (currentMission.remainingBudgetUsd >= 4.0) {
            twists.add(
                PlotTwist(
                    id = "twist_budget_${System.currentTimeMillis()}",
                    title = "🚨 BUDGET CUT",
                    description = "Your remaining flexible budget just dropped by 25%. Time to negotiate like a true local.",
                    emoji = "💸",
                    bonusXp = 100,
                    type = PlotTwistType.BUDGET_CUT
                )
            )
        }

        // 2. Side quest
        twists.add(
            PlotTwist(
                id = "twist_side_${System.currentTimeMillis()}",
                title = "⚡ SIDE QUEST",
                description = "Find something neon or purple in the next 3 minutes for bonus street cred.",
                emoji = "⚡",
                bonusXp = 120,
                type = PlotTwistType.SIDE_QUEST
            )
        )

        // 3. Walk it off (only if not already marked as heavy transport)
        twists.add(
            PlotTwist(
                id = "twist_walk_${System.currentTimeMillis()}",
                title = "🚶 WALK IT OFF",
                description = "Next objective must be reached strictly on foot through the nearest alley.",
                emoji = "🚶",
                bonusXp = 80,
                type = PlotTwistType.WALK_IT_OFF
            )
        )

        // 4. Double or nothing
        twists.add(
            PlotTwist(
                id = "twist_double_${System.currentTimeMillis()}",
                title = "🎲 DOUBLE OR NOTHING",
                description = "Complete the next quest with proof within 10 minutes for 2x XP.",
                emoji = "🎲",
                bonusXp = 200,
                type = PlotTwistType.DOUBLE_OR_NOTHING
            )
        )

        val remainingMinutes = (currentMission.remainingMillis() / 60000L).toInt()
        val validTwists = twists.filter { twist ->
            MissionConstraintValidator.isPlotTwistValid(
                twist = twist,
                budgetRemaining = currentMission.remainingBudgetUsd,
                timeRemainingMinutes = remainingMinutes,
                transportMode = currentMission.transportMode
            )
        }

        return validTwists.randomOrNull()
    }

    private fun parseBudget(budget: String): Double {
        val digits = budget.filter { it.isDigit() }
        val parsed = digits.toDoubleOrNull()
        if (parsed != null) {
            return parsed
        }
        return when {
            budget.contains("Broke", ignoreCase = true) || budget.contains("Free", ignoreCase = true) -> 0.0
            else -> 10.0
        }
    }

    private fun parseDurationMinutes(duration: String, mode: AdventureMode): Int {
        // STRICT ACCURACY TO FIX THE PREVIOUS 43-MINUTE BUG
        return when {
            mode == AdventureMode.FOOD && duration.contains("0–1") -> 45
            mode == AdventureMode.FOOD && duration.contains("1+") -> 85
            duration.contains("30 min", ignoreCase = true) -> 30
            duration.contains("1 hour", ignoreCase = true) || duration.contains("1 hr", ignoreCase = true) -> 60
            duration.contains("2 hours", ignoreCase = true) || duration.contains("2 hr", ignoreCase = true) -> 120
            duration.contains("3+", ignoreCase = true) -> 180
            else -> 60
        }
    }

    private fun generateTitles(request: AdventureRequest): Pair<String, String> {
        return when (request.mode) {
            AdventureMode.BROKE -> {
                "💸 ON THE ZERO: ${request.location.uppercase()}" to "Financially unavailable, undeniably legendary"
            }
            AdventureMode.DATE -> {
                "❤️ ROMANTIC DRIFT: ${request.location.uppercase()}" to "High charm, effortless Mediterranean memories"
            }
            AdventureMode.FOOD -> {
                "🍔 FLAVOR EXPEDITION: ${request.location.uppercase()}" to "Zero regrets, maximum toum and crispy carbs"
            }
            AdventureMode.ROULETTE -> {
                "🎰 ROULETTE WILDCARD: ${request.secondaryMood.title.uppercase()}" to "Fate made this questionable decision"
            }
            else -> {
                when (request.secondaryMood) {
                    SecondaryMood.CHAOS -> "🔥 CHAOS RUN: ${request.location.uppercase()}" to "Four missions. Zero apologies. High adrenaline."
                    SecondaryMood.CHILL -> "🌊 COASTAL CHILL: ${request.location.uppercase()}" to "Quiet corners, iced mint, zero stress"
                    SecondaryMood.MYSTERY -> "🕵️ THE SECRET SOUK: ${request.location.uppercase()}" to "Cryptic doorways and forgotten staircases"
                    SecondaryMood.ADVENTURE -> "⚡ URBAN EXPEDITION: ${request.location.uppercase()}" to "Take the side alleys. Explore the unexpected."
                    SecondaryMood.WILD_CARD -> "🎲 LEBANESE ROLLERCOASTER" to "Expect the wildly unexpected"
                }
            }
        }
    }
}
