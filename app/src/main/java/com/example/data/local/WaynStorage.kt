package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AdventureMode
import com.example.data.model.Mission
import com.example.data.model.PhotoScore
import com.example.data.model.PlotTwist
import com.example.data.model.PlotTwistType
import com.example.data.model.Quest
import com.example.data.model.QuestType
import com.example.data.model.SecondaryMood
import com.example.data.model.TransportMode
import com.example.data.model.LogbookEntry
import com.example.data.model.FeedPost
import com.example.data.model.QuestParty
import org.json.JSONArray
import org.json.JSONObject

class WaynStorage(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("wayn_adventure_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ACTIVE_MISSION = "key_active_mission"
        private const val KEY_USER_NAME = "key_user_name"
        private const val KEY_USER_XP = "key_user_xp"
        private const val KEY_USER_STREAK = "key_user_streak"
        private const val KEY_LAST_STREAK_DATE = "key_last_streak_date"
        private const val KEY_ADVENTURES_COUNT = "key_adventures_count"
        private const val KEY_QUESTS_COUNT = "key_quests_count"
        private const val KEY_ONBOARDING_DONE = "key_onboarding_done"
        private const val KEY_DEMO_MODE = "key_demo_mode"
        private const val KEY_SAVED_QUESTS = "key_saved_quests"
        private const val KEY_LOGBOOK_ENTRIES = "key_logbook_entries"
        private const val KEY_USER_FEED_POSTS = "key_user_feed_posts"
        private const val KEY_CREATED_PARTIES = "key_created_parties"
        private const val KEY_UNLOCKED_ACHIEVEMENTS = "key_unlocked_achievements"
    }

    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_DONE, false)
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, completed).apply()
    }

    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "You") ?: "You"
    }

    fun setUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }

    fun getUserXp(): Int {
        return prefs.getInt(KEY_USER_XP, 1840)
    }

    fun setUserXp(xp: Int) {
        prefs.edit().putInt(KEY_USER_XP, xp).apply()
    }

    fun getStreak(): Int {
        return prefs.getInt(KEY_USER_STREAK, 4)
    }

    fun setStreak(streak: Int) {
        prefs.edit().putInt(KEY_USER_STREAK, streak).apply()
    }

    fun getLastStreakDate(): String {
        return prefs.getString(KEY_LAST_STREAK_DATE, "") ?: ""
    }

    fun setLastStreakDate(date: String) {
        prefs.edit().putString(KEY_LAST_STREAK_DATE, date).apply()
    }

    fun getAdventuresCount(): Int {
        return prefs.getInt(KEY_ADVENTURES_COUNT, 6)
    }

    fun setAdventuresCount(count: Int) {
        prefs.edit().putInt(KEY_ADVENTURES_COUNT, count).apply()
    }

    fun getQuestsCount(): Int {
        return prefs.getInt(KEY_QUESTS_COUNT, 21)
    }

    fun setQuestsCount(count: Int) {
        prefs.edit().putInt(KEY_QUESTS_COUNT, count).apply()
    }

    fun isDemoMode(): Boolean {
        return prefs.getBoolean(KEY_DEMO_MODE, true)
    }

    fun setDemoMode(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DEMO_MODE, enabled).apply()
    }

    // --- Active Mission Persistence ---
    fun saveActiveMission(mission: Mission?) {
        if (mission == null) {
            prefs.edit().remove(KEY_ACTIVE_MISSION).apply()
            return
        }

        try {
            val json = JSONObject()
            json.put("id", mission.id)
            json.put("title", mission.title)
            json.put("subtitle", mission.subtitle)
            json.put("tagline", mission.tagline)
            json.put("difficulty", mission.difficulty)
            json.put("totalPossibleXP", mission.totalPossibleXP)
            json.put("estimatedDurationMinutes", mission.estimatedDurationMinutes)
            json.put("estimatedCostUsd", mission.estimatedCostUsd)
            json.put("initialBudgetUsd", mission.initialBudgetUsd)
            json.put("remainingBudgetUsd", mission.remainingBudgetUsd)
            json.put("totalSpentUsd", mission.totalSpentUsd)
            json.put("missionStartEpochMillis", mission.missionStartEpochMillis)
            json.put("missionEndEpochMillis", mission.missionEndEpochMillis)
            json.put("currentQuestIndex", mission.currentQuestIndex)
            json.put("completed", mission.completed)
            json.put("mode", mission.mode.name)
            json.put("secondaryMood", mission.secondaryMood.name)
            json.put("location", mission.location)
            json.put("groupSize", mission.groupSize)
            json.put("transportMode", mission.transportMode.name)
            json.put("transportation", mission.transportation)
            json.put("photosTaken", mission.photosTaken)
            json.put("questionableDecisions", mission.questionableDecisions)

            // Active Plot Twist
            if (mission.activePlotTwist != null) {
                val twistJson = JSONObject()
                twistJson.put("id", mission.activePlotTwist.id)
                twistJson.put("title", mission.activePlotTwist.title)
                twistJson.put("description", mission.activePlotTwist.description)
                twistJson.put("emoji", mission.activePlotTwist.emoji)
                twistJson.put("bonusXp", mission.activePlotTwist.bonusXp)
                twistJson.put("type", mission.activePlotTwist.type.name)
                json.put("activePlotTwist", twistJson)
            }

            // Quests array
            val questsArray = JSONArray()
            for (q in mission.quests) {
                val qObj = JSONObject()
                qObj.put("id", q.id)
                qObj.put("title", q.title)
                qObj.put("description", q.description)
                qObj.put("type", q.type.name)
                qObj.put("xp", q.xp)
                qObj.put("bonusXp", q.bonusXp)
                qObj.put("estimatedDurationMinutes", q.estimatedDurationMinutes)
                qObj.put("estimatedCostUsd", q.estimatedCostUsd)
                qObj.put("actualSpent", q.actualSpent)
                qObj.put("location", q.location)
                qObj.put("bonusChallenge", q.bonusChallenge ?: "")
                qObj.put("completed", q.completed)
                qObj.put("skipped", q.skipped)
                qObj.put("proofRequired", q.proofRequired)
                qObj.put("photoUri", q.photoUri ?: "")
                qObj.put("funnyComment", q.funnyComment ?: "")

                if (q.photoScore != null) {
                    val scoreObj = JSONObject()
                    scoreObj.put("drama", q.photoScore.drama)
                    scoreObj.put("composition", q.photoScore.composition)
                    scoreObj.put("unnecessaryIntensity", q.photoScore.unnecessaryIntensity)
                    scoreObj.put("totalScore", q.photoScore.totalScore)
                    scoreObj.put("aiComment", q.photoScore.aiComment)
                    qObj.put("photoScore", scoreObj)
                }

                questsArray.put(qObj)
            }
            json.put("quests", questsArray)

            prefs.edit().putString(KEY_ACTIVE_MISSION, json.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadActiveMission(): Mission? {
        val raw = prefs.getString(KEY_ACTIVE_MISSION, null) ?: return null
        return try {
            val json = JSONObject(raw)
            val modeStr = json.optString("mode", AdventureMode.BORED.name)
            val moodStr = json.optString("secondaryMood", SecondaryMood.CHAOS.name)
            val mode = runCatching { AdventureMode.valueOf(modeStr) }.getOrDefault(AdventureMode.BORED)
            val mood = runCatching { SecondaryMood.valueOf(moodStr) }.getOrDefault(SecondaryMood.CHAOS)

            val quests = mutableListOf<Quest>()
            val questsArray = json.optJSONArray("quests") ?: JSONArray()
            for (i in 0 until questsArray.length()) {
                val qObj = questsArray.getJSONObject(i)
                val typeStr = qObj.optString("type", QuestType.LOCATION.name)
                val type = runCatching { QuestType.valueOf(typeStr) }.getOrDefault(QuestType.LOCATION)

                var photoScore: PhotoScore? = null
                if (qObj.has("photoScore")) {
                    val sc = qObj.getJSONObject("photoScore")
                    photoScore = PhotoScore(
                        drama = sc.optInt("drama", 85),
                        composition = sc.optInt("composition", 75),
                        unnecessaryIntensity = sc.optInt("unnecessaryIntensity", 90),
                        totalScore = sc.optInt("totalScore", 83),
                        aiComment = sc.optString("aiComment", "Solid effort.")
                    )
                }

                quests.add(
                    Quest(
                        id = qObj.optString("id"),
                        title = qObj.optString("title"),
                        description = qObj.optString("description"),
                        type = type,
                        xp = qObj.optInt("xp", 100),
                        bonusXp = qObj.optInt("bonusXp", 50),
                        estimatedDurationMinutes = qObj.optInt("estimatedDurationMinutes", 15),
                        estimatedCostUsd = qObj.optDouble("estimatedCostUsd", 0.0),
                        actualSpent = qObj.optDouble("actualSpent", 0.0),
                        location = qObj.optString("location"),
                        bonusChallenge = qObj.optString("bonusChallenge").ifEmpty { null },
                        completed = qObj.optBoolean("completed", false),
                        skipped = qObj.optBoolean("skipped", false),
                        proofRequired = qObj.optBoolean("proofRequired", true),
                        photoUri = qObj.optString("photoUri").ifEmpty { null },
                        photoScore = photoScore,
                        funnyComment = qObj.optString("funnyComment").ifEmpty { null }
                    )
                )
            }

            var activePlotTwist: PlotTwist? = null
            if (json.has("activePlotTwist")) {
                val twistObj = json.getJSONObject("activePlotTwist")
                val tTypeStr = twistObj.optString("type", PlotTwistType.SIDE_QUEST.name)
                val tType = runCatching { PlotTwistType.valueOf(tTypeStr) }.getOrDefault(PlotTwistType.SIDE_QUEST)
                activePlotTwist = PlotTwist(
                    id = twistObj.optString("id"),
                    title = twistObj.optString("title"),
                    description = twistObj.optString("description"),
                    emoji = twistObj.optString("emoji", "⚡"),
                    bonusXp = twistObj.optInt("bonusXp", 100),
                    type = tType,
                    applied = true
                )
            }

            val initialBudget = json.optDouble("initialBudgetUsd", json.optDouble("estimatedCostUsd", 5.0))
            val totalSpent = json.optDouble("totalSpentUsd", 0.0)
            val transModeStr = json.optString("transportMode", TransportMode.WALK.name)
            val transMode = runCatching { TransportMode.valueOf(transModeStr) }.getOrDefault(TransportMode.WALK)

            Mission(
                id = json.optString("id"),
                title = json.optString("title"),
                subtitle = json.optString("subtitle"),
                tagline = json.optString("tagline", "Turn anywhere into an adventure."),
                difficulty = json.optString("difficulty", "Medium"),
                totalPossibleXP = json.optInt("totalPossibleXP", 500),
                estimatedDurationMinutes = json.optInt("estimatedDurationMinutes", 60),
                estimatedCostUsd = json.optDouble("estimatedCostUsd", 5.0),
                initialBudgetUsd = initialBudget,
                remainingBudgetUsd = json.optDouble("remainingBudgetUsd", initialBudget),
                totalSpentUsd = totalSpent,
                missionStartEpochMillis = json.optLong("missionStartEpochMillis", System.currentTimeMillis()),
                missionEndEpochMillis = json.optLong("missionEndEpochMillis", System.currentTimeMillis() + 3600000L),
                currentQuestIndex = json.optInt("currentQuestIndex", 0),
                completed = json.optBoolean("completed", false),
                quests = quests,
                activePlotTwist = activePlotTwist,
                mode = mode,
                secondaryMood = mood,
                location = json.optString("location", "Beirut"),
                groupSize = json.optString("groupSize", "3"),
                transportMode = transMode,
                transportation = json.optString("transportation", transMode.displayName),
                photosTaken = json.optInt("photosTaken", 0),
                questionableDecisions = json.optInt("questionableDecisions", 1)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // --- Logbook Persistence ---
    fun saveLogbookEntry(entry: LogbookEntry) {
        val list = getLogbookEntries().toMutableList()
        list.removeAll { it.id == entry.id }
        list.add(0, entry)

        val array = JSONArray()
        for (e in list) {
            val obj = JSONObject()
            obj.put("id", e.id)
            obj.put("missionName", e.missionName)
            obj.put("date", e.date)
            obj.put("city", e.city)
            obj.put("mode", e.mode.name)
            obj.put("transportMode", e.transportMode.name)
            obj.put("durationMinutes", e.durationMinutes)
            obj.put("xpEarned", e.xpEarned)
            obj.put("initialBudget", e.initialBudget)
            obj.put("actualTotalSpent", e.actualTotalSpent)
            obj.put("moneyLeft", e.moneyLeft)
            obj.put("questsCompleted", e.questsCompleted)
            obj.put("totalQuests", e.totalQuests)
            obj.put("finalRank", e.finalRank)
            obj.put("photoUri", e.photoUri ?: "")
            array.put(obj)
        }
        prefs.edit().putString(KEY_LOGBOOK_ENTRIES, array.toString()).apply()
    }

    fun getLogbookEntries(): List<LogbookEntry> {
        val raw = prefs.getString(KEY_LOGBOOK_ENTRIES, null)
        if (raw.isNullOrBlank()) {
            // Return seeded initial entries so Logbook is populated!
            return getInitialSeededLogbook()
        }
        val list = mutableListOf<LogbookEntry>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val mMode = runCatching { AdventureMode.valueOf(obj.optString("mode")) }.getOrDefault(AdventureMode.BORED)
                val tMode = runCatching { TransportMode.valueOf(obj.optString("transportMode")) }.getOrDefault(TransportMode.WALK)
                list.add(
                    LogbookEntry(
                        id = obj.optString("id"),
                        missionName = obj.optString("missionName"),
                        date = obj.optString("date"),
                        city = obj.optString("city"),
                        mode = mMode,
                        transportMode = tMode,
                        durationMinutes = obj.optInt("durationMinutes", 60),
                        xpEarned = obj.optInt("xpEarned", 450),
                        initialBudget = obj.optDouble("initialBudget", 10.0),
                        actualTotalSpent = obj.optDouble("actualTotalSpent", 4.5),
                        moneyLeft = obj.optDouble("moneyLeft", 5.5),
                        questsCompleted = obj.optInt("questsCompleted", 3),
                        totalQuests = obj.optInt("totalQuests", 3),
                        finalRank = obj.optString("finalRank", "CHAOTIC LEGEND"),
                        photoUri = obj.optString("photoUri").ifEmpty { null }
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return getInitialSeededLogbook()
        }
        return list
    }

    private fun getInitialSeededLogbook(): List<LogbookEntry> {
        return listOf(
            LogbookEntry(
                id = "log_seed_1",
                missionName = "Midnight Manousheh & Corniche Drift",
                date = "Yesterday, 11:42 PM",
                city = "Beirut",
                mode = AdventureMode.FOOD,
                transportMode = TransportMode.WALK,
                durationMinutes = 45,
                xpEarned = 520,
                initialBudget = 10.0,
                actualTotalSpent = 4.25,
                moneyLeft = 5.75,
                questsCompleted = 3,
                totalQuests = 3,
                finalRank = "CHAOTIC GOOD"
            ),
            LogbookEntry(
                id = "log_seed_2",
                missionName = "Zero Dollar Mar Mikhael Scavenger",
                date = "3 days ago",
                city = "Beirut",
                mode = AdventureMode.BROKE,
                transportMode = TransportMode.WALK,
                durationMinutes = 40,
                xpEarned = 480,
                initialBudget = 0.0,
                actualTotalSpent = 0.0,
                moneyLeft = 0.0,
                questsCompleted = 3,
                totalQuests = 3,
                finalRank = "STREET SMART"
            ),
            LogbookEntry(
                id = "log_seed_3",
                missionName = "Phoenician Wall Sunset Sprint",
                date = "Last Saturday",
                city = "Batroun",
                mode = AdventureMode.BORED,
                transportMode = TransportMode.CAR,
                durationMinutes = 60,
                xpEarned = 580,
                initialBudget = 20.0,
                actualTotalSpent = 12.50,
                moneyLeft = 7.50,
                questsCompleted = 4,
                totalQuests = 4,
                finalRank = "BEIRUT VETERAN"
            )
        )
    }

    // --- User Created Feed Posts ---
    fun saveUserFeedPost(post: FeedPost) {
        val list = getUserFeedPosts().toMutableList()
        list.removeAll { it.id == post.id }
        list.add(0, post)

        val array = JSONArray()
        for (p in list) {
            val obj = JSONObject()
            obj.put("id", p.id)
            obj.put("authorName", p.authorName)
            obj.put("authorAvatar", p.authorAvatar)
            obj.put("authorLevelTitle", p.authorLevelTitle)
            obj.put("questTitle", p.questTitle)
            obj.put("location", p.location)
            obj.put("timeAgo", p.timeAgo)
            obj.put("dramaScore", p.dramaScore)
            obj.put("totalScore", p.totalScore)
            obj.put("xpEarned", p.xpEarned)
            obj.put("caption", p.caption)
            obj.put("likesCount", p.likesCount)
            obj.put("isLiked", p.isLiked)
            obj.put("commentsCount", p.commentsCount)
            obj.put("photoPlaceholderEmoji", p.photoPlaceholderEmoji)
            obj.put("photoUri", p.originalQuest.photoUri ?: "")
            array.put(obj)
        }
        prefs.edit().putString(KEY_USER_FEED_POSTS, array.toString()).apply()
    }

    fun getUserFeedPosts(): List<FeedPost> {
        val raw = prefs.getString(KEY_USER_FEED_POSTS, null) ?: return emptyList()
        val list = mutableListOf<FeedPost>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val dummyQuest = Quest(
                    id = "quest_${obj.optString("id")}",
                    title = obj.optString("questTitle"),
                    description = obj.optString("caption"),
                    type = QuestType.FINAL,
                    xp = obj.optInt("xpEarned", 300),
                    location = obj.optString("location"),
                    photoUri = obj.optString("photoUri").ifEmpty { null }
                )
                list.add(
                    FeedPost(
                        id = obj.optString("id"),
                        authorName = obj.optString("authorName"),
                        authorAvatar = obj.optString("authorAvatar"),
                        authorLevelTitle = obj.optString("authorLevelTitle"),
                        questTitle = obj.optString("questTitle"),
                        location = obj.optString("location"),
                        timeAgo = obj.optString("timeAgo"),
                        dramaScore = obj.optInt("dramaScore", 90),
                        totalScore = obj.optInt("totalScore", 88),
                        xpEarned = obj.optInt("xpEarned", 400),
                        caption = obj.optString("caption"),
                        likesCount = obj.optInt("likesCount", 1),
                        isLiked = obj.optBoolean("isLiked", false),
                        commentsCount = obj.optInt("commentsCount", 0),
                        comments = emptyList(),
                        originalQuest = dummyQuest,
                        photoPlaceholderEmoji = obj.optString("photoPlaceholderEmoji", "🔥")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    // --- Unlocked Achievement Tiers ---
    fun getUnlockedAchievementTiers(): Set<String> {
        return prefs.getStringSet(KEY_UNLOCKED_ACHIEVEMENTS, emptySet()) ?: emptySet()
    }

    fun unlockAchievementTier(tierKey: String) {
        val current = getUnlockedAchievementTiers().toMutableSet()
        current.add(tierKey)
        prefs.edit().putStringSet(KEY_UNLOCKED_ACHIEVEMENTS, current).apply()
    }

    fun saveCopiedQuest(quest: Quest) {
        val current = getSavedQuests().toMutableList()
        current.removeAll { it.id == quest.id }
        current.add(0, quest)

        val array = JSONArray()
        for (q in current) {
            val obj = JSONObject()
            obj.put("id", q.id)
            obj.put("title", q.title)
            obj.put("description", q.description)
            obj.put("type", q.type.name)
            obj.put("xp", q.xp)
            obj.put("bonusXp", q.bonusXp)
            obj.put("estimatedDurationMinutes", q.estimatedDurationMinutes)
            obj.put("estimatedCostUsd", q.estimatedCostUsd)
            obj.put("location", q.location)
            obj.put("bonusChallenge", q.bonusChallenge ?: "")
            obj.put("funnyComment", q.funnyComment ?: "")
            array.put(obj)
        }
        prefs.edit().putString(KEY_SAVED_QUESTS, array.toString()).apply()
    }

    fun getSavedQuests(): List<Quest> {
        val raw = prefs.getString(KEY_SAVED_QUESTS, null) ?: return emptyList()
        val list = mutableListOf<Quest>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val typeStr = obj.optString("type", QuestType.LOCATION.name)
                val type = runCatching { QuestType.valueOf(typeStr) }.getOrDefault(QuestType.LOCATION)
                list.add(
                    Quest(
                        id = obj.optString("id"),
                        title = obj.optString("title"),
                        description = obj.optString("description"),
                        type = type,
                        xp = obj.optInt("xp", 120),
                        bonusXp = obj.optInt("bonusXp", 60),
                        estimatedDurationMinutes = obj.optInt("estimatedDurationMinutes", 20),
                        estimatedCostUsd = obj.optDouble("estimatedCostUsd", 0.0),
                        location = obj.optString("location"),
                        bonusChallenge = obj.optString("bonusChallenge").ifEmpty { null },
                        funnyComment = obj.optString("funnyComment").ifEmpty { null }
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
