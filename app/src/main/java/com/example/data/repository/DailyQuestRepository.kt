package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.DailyQuest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyQuestRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("wayn_daily_quest_prefs", Context.MODE_PRIVATE)

    private val _dailyQuest = MutableStateFlow(loadOrCreateDailyQuest())
    val dailyQuest: StateFlow<DailyQuest> = _dailyQuest.asStateFlow()

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    private fun loadOrCreateDailyQuest(): DailyQuest {
        val today = getTodayDateString()
        val savedDate = prefs.getString("daily_date", "")
        val isCompleted = prefs.getBoolean("daily_completed_$today", false)

        val templates = listOf(
            Triple("Street Food Recon", "Track down a snack under $3 within 1 km of your current spot.", "Local Area"),
            Triple("Hidden Corner Discovery", "Find a building or staircase older than 50 years and take a photo.", "Historic Alleyways"),
            Triple("Spontaneous Toast", "Buy an iced drink and toast to an absolute stranger or street cat.", "Neighbourhood Café"),
            Triple("The 15-Minute Stride", "Walk 1,000 steps without checking your notifications.", "City Promenade")
        )

        val seed = today.hashCode()
        val template = templates[kotlin.math.abs(seed) % templates.size]

        return DailyQuest(
            id = "daily_$today",
            dateString = today,
            title = template.first,
            description = template.second,
            locationTarget = template.third,
            rewardXp = 120,
            completed = isCompleted,
            emoji = "🎯"
        )
    }

    fun completeDailyQuest(): Int {
        val current = _dailyQuest.value
        if (current.completed) return 0

        val today = getTodayDateString()
        prefs.edit().putBoolean("daily_completed_$today", true).apply()
        _dailyQuest.value = current.copy(completed = true)
        return current.rewardXp
    }
}
