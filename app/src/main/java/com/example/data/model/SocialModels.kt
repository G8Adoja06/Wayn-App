package com.example.data.model

data class FeedComment(
    val id: String,
    val author: String,
    val authorAvatar: String,
    val text: String,
    val timestampText: String
)

data class FeedPost(
    val id: String,
    val authorName: String,
    val authorAvatar: String,
    val authorLevelTitle: String,
    val questTitle: String,
    val location: String,
    val timeAgo: String,
    val dramaScore: Int,
    val totalScore: Int,
    val xpEarned: Int,
    val caption: String,
    val likesCount: Int,
    val isLiked: Boolean = false,
    val commentsCount: Int,
    val comments: List<FeedComment> = emptyList(),
    val originalQuest: Quest,
    val photoPlaceholderEmoji: String = "📸",
    val photoUri: String? = null,
    val totalSpent: Double? = null,
    val questsCompletedCount: Int? = null
)

data class PartyMessage(
    val id: String,
    val senderName: String,
    val senderAvatar: String,
    val text: String,
    val timeText: String,
    val isCurrentUser: Boolean = false
)

data class QuestParty(
    val id: String,
    val title: String,
    val location: String,
    val memberCount: Int,
    val maxMembers: Int,
    val startsInText: String,
    val members: List<String>,
    val mood: String,
    val isJoined: Boolean = false,
    val messages: List<PartyMessage> = emptyList()
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val scoreXp: Int,
    val isCurrentUser: Boolean = false,
    val city: String = "Beirut",
    val badge: String = "🔥"
) {
    val teamName: String get() = name
    val badgeEmoji: String get() = badge
}
