package com.example.data.repository

import com.example.data.local.WaynStorage
import com.example.data.model.FeedComment
import com.example.data.model.FeedPost
import com.example.data.model.LeaderboardEntry
import com.example.data.model.PartyMessage
import com.example.data.model.Quest
import com.example.data.model.QuestParty
import com.example.data.model.QuestType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class SocialRepository(
    private val storage: WaynStorage
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _feedPosts = MutableStateFlow(storage.getUserFeedPosts() + createInitialFeed())
    val feedPosts: StateFlow<List<FeedPost>> = _feedPosts.asStateFlow()

    private val _questParties = MutableStateFlow(createInitialParties())
    val questParties: StateFlow<List<QuestParty>> = _questParties.asStateFlow()

    private val _leaderboardEntries = MutableStateFlow(createInitialLeaderboard())
    val leaderboardEntries: StateFlow<List<LeaderboardEntry>> = _leaderboardEntries.asStateFlow()

    private fun createInitialFeed(): List<FeedPost> {
        val q1 = Quest(
            id = "maya_quest_1",
            title = "$5 Beirut Food Run",
            description = "Track down the crispiest falafel sandwich in Hamra and convince the chef to add extra pickles.",
            type = QuestType.FOOD,
            xp = 420,
            bonusXp = 80,
            estimatedDurationMinutes = 35,
            estimatedCostUsd = 4.5,
            location = "Hamra, Beirut",
            bonusChallenge = "Ask for the secret hot sauce recipe in Lebanese Arabic.",
            funnyComment = "Worth every questionable decision."
        )

        val q2 = Quest(
            id = "karim_quest_2",
            title = "Zero Dollar Mar Mikhael Scavenger",
            description = "Find the vintage graffiti mural near the train station and pose like you're an undercover detective.",
            type = QuestType.PHOTO,
            xp = 350,
            bonusXp = 100,
            estimatedDurationMinutes = 40,
            estimatedCostUsd = 0.0,
            location = "Mar Mikhael, Beirut",
            bonusChallenge = "Black and white portrait with extreme dramatic shadows.",
            funnyComment = "The drama level broke the sensor."
        )

        val q3 = Quest(
            id = "lea_quest_3",
            title = "Phoenician Wall Sunset Sprint",
            description = "Sprint to the Batroun sea wall right before golden hour fades and record the waves breaking.",
            type = QuestType.FINAL,
            xp = 510,
            bonusXp = 120,
            estimatedDurationMinutes = 45,
            estimatedCostUsd = 3.0,
            location = "Old Batroun",
            bonusChallenge = "Toast lemonade cups directly into the sun.",
            funnyComment = "Mediterranean postcard tier."
        )

        return listOf(
            FeedPost(
                id = "post_1",
                authorName = "Maya S.",
                authorAvatar = "🌸",
                authorLevelTitle = "Chaos Specialist",
                questTitle = "$5 Beirut Food Run",
                location = "Hamra, Beirut",
                timeAgo = "16 min ago",
                dramaScore = 94,
                totalScore = 91,
                xpEarned = 420,
                caption = "Worth every questionable decision. The falafel chef even gave us free mint tea 😂🔥",
                likesCount = 38,
                isLiked = false,
                commentsCount = 3,
                comments = listOf(
                    FeedComment("c1", "Karim", "⚡", "Need to try this immediately 😂", "12 min ago"),
                    FeedComment("c2", "Rami", "🕶️", "No way you did this for $5!", "8 min ago"),
                    FeedComment("c3", "Lea", "🌊", "Adding this to my list right now 🔥", "2 min ago")
                ),
                originalQuest = q1,
                photoPlaceholderEmoji = "🥙"
            ),
            FeedPost(
                id = "post_2",
                authorName = "Karim Z.",
                authorAvatar = "⚡",
                authorLevelTitle = "Street Explorer",
                questTitle = "Zero Dollar Mar Mikhael Scavenger",
                location = "Mar Mikhael, Beirut",
                timeAgo = "42 min ago",
                dramaScore = 98,
                totalScore = 95,
                xpEarned = 350,
                caption = "Ma fi budget? Ma fi problem. Found this incredible hidden courtyard staircase.",
                likesCount = 52,
                isLiked = true,
                commentsCount = 2,
                comments = listOf(
                    FeedComment("c4", "Anthony", "🛵", "Which staircase is this?? Badaro or Armenia street?", "30 min ago"),
                    FeedComment("c5", "Maya S.", "🌸", "Aesthetic 10/10", "15 min ago")
                ),
                originalQuest = q2,
                photoPlaceholderEmoji = "🏛️"
            ),
            FeedPost(
                id = "post_3",
                authorName = "Lea K.",
                authorAvatar = "🌊",
                authorLevelTitle = "Beirut Veteran",
                questTitle = "Phoenician Wall Sunset Sprint",
                location = "Old Batroun",
                timeAgo = "2 hr ago",
                dramaScore = 89,
                totalScore = 92,
                xpEarned = 510,
                caption = "Beat the sunset timer with 4 minutes to spare. The fresh lemonade was mandatory.",
                likesCount = 74,
                isLiked = false,
                commentsCount = 1,
                comments = listOf(
                    FeedComment("c6", "Jad", "🎲", "Batroun sunset never misses.", "1 hr ago")
                ),
                originalQuest = q3,
                photoPlaceholderEmoji = "🌅"
            )
        )
    }

    private fun createInitialParties(): List<QuestParty> {
        return listOf(
            QuestParty(
                id = "party_1",
                title = "🔥 Chaos Run — Beirut",
                location = "Mar Mikhael & Gemmayzeh",
                memberCount = 3,
                maxMembers = 4,
                startsInText = "Starting in 12 min",
                members = listOf("Maya", "Karim", "Anthony"),
                mood = "Chaos",
                isJoined = false,
                messages = listOf(
                    PartyMessage("m1", "Maya", "🌸", "Yalla who's joining?", "10 min ago"),
                    PartyMessage("m2", "Karim", "⚡", "Anything except walking up Achrafieh stairs 😂", "8 min ago"),
                    PartyMessage("m3", "Anthony", "🛵", "I have my scooter ready if we need a fast getaway.", "3 min ago")
                )
            ),
            QuestParty(
                id = "party_2",
                title = "🍔 Food Hunt — Hamra",
                location = "Hamra Main Street",
                memberCount = 2,
                maxMembers = 4,
                startsInText = "Starting in 25 min",
                members = listOf("Lea", "Rami"),
                mood = "Food",
                isJoined = false,
                messages = listOf(
                    PartyMessage("m4", "Lea", "🌊", "Meeting near the red cinema. Budget is strictly under $10.", "15 min ago"),
                    PartyMessage("m5", "Rami", "🕶️", "Targeting the legendary knafeh spot after.", "11 min ago")
                )
            ),
            QuestParty(
                id = "party_3",
                title = "🌊 Sunset Chill — Byblos Port",
                location = "Byblos Old Souk",
                memberCount = 2,
                maxMembers = 3,
                startsInText = "Starting in 45 min",
                members = listOf("Sarah", "Jad"),
                mood = "Chill",
                isJoined = false,
                messages = listOf(
                    PartyMessage("m6", "Sarah", "✨", "Low energy, pure aesthetic sunset stroll.", "20 min ago")
                )
            )
        )
    }

    private fun createInitialLeaderboard(): List<LeaderboardEntry> {
        return listOf(
            LeaderboardEntry(1, "Team Shawarma", 14820, false, "Beirut", "👑"),
            LeaderboardEntry(2, "Ctrl Alt Elite", 12440, false, "Jounieh", "🥈"),
            LeaderboardEntry(3, "Lost Again", 11930, false, "Batroun", "🥉"),
            LeaderboardEntry(4, "No GPS Needed", 9840, false, "Byblos", "⚡"),
            LeaderboardEntry(5, "You", 1840, true, "Beirut", "🔥"),
            LeaderboardEntry(6, "Midnight Wanderers", 1710, false, "Tripoli", "🌙")
        )
    }

    fun toggleLike(postId: String) {
        val current = _feedPosts.value.toMutableList()
        val index = current.indexOfFirst { it.id == postId }
        if (index != -1) {
            val post = current[index]
            val newIsLiked = !post.isLiked
            val newCount = post.likesCount + (if (newIsLiked) 1 else -1)
            current[index] = post.copy(isLiked = newIsLiked, likesCount = maxOf(0, newCount))
            _feedPosts.value = current
        }
    }

    fun addComment(postId: String, text: String, authorName: String) {
        val current = _feedPosts.value.toMutableList()
        val index = current.indexOfFirst { it.id == postId }
        if (index != -1) {
            val post = current[index]
            val newComment = FeedComment(
                id = "cmt_${System.currentTimeMillis()}",
                author = authorName,
                authorAvatar = "⚡",
                text = text,
                timestampText = "Just now"
            )
            val updatedComments = post.comments + newComment
            current[index] = post.copy(
                comments = updatedComments,
                commentsCount = updatedComments.size
            )
            _feedPosts.value = current
        }
    }

    fun createParty(
        title: String,
        location: String,
        time: String,
        maxMembers: Int,
        hostName: String,
        description: String = "",
        transportMode: String = "Walk"
    ): QuestParty {
        val newParty = QuestParty(
            id = "party_${System.currentTimeMillis()}",
            title = title,
            location = "$location ($transportMode)",
            memberCount = 1,
            maxMembers = maxMembers.coerceIn(2, 8),
            startsInText = time,
            members = listOf(hostName),
            mood = if (description.isNotBlank()) description else "🔥 Spontaneous Adventure",
            isJoined = true,
            messages = listOf(
                PartyMessage(
                    id = "msg_init",
                    senderName = "System",
                    senderAvatar = "⚡",
                    text = "Party created by $hostName ($transportMode). Objective: ${if (description.isNotBlank()) description else title}",
                    timeText = "Just now",
                    isCurrentUser = true
                )
            )
        )
        _questParties.value = listOf(newParty) + _questParties.value

        // Simulate 1–2 mock users joining after a short realistic delay (for demo liveliness)
        scope.launch {
            delay(2500)
            val joiner1 = listOf("Maya", "Karim", "Lea", "Anthony").random()
            val updated = _questParties.value.toMutableList()
            val idx = updated.indexOfFirst { it.id == newParty.id }
            if (idx != -1) {
                val p = updated[idx]
                val newMembers = p.members + joiner1
                val joinMsg = PartyMessage(
                    id = "msg_join_1",
                    senderName = joiner1,
                    senderAvatar = if (joiner1 == "Maya") "🌸" else "⚡",
                    text = "Yalla I just joined! Let's do this 🔥",
                    timeText = "Just now",
                    isCurrentUser = false
                )
                updated[idx] = p.copy(
                    memberCount = minOf(p.maxMembers, p.memberCount + 1),
                    members = newMembers,
                    messages = p.messages + joinMsg
                )
                _questParties.value = updated
            }

            delay(3000)
            val joiner2 = listOf("Jad", "Nour", "Tariq").random()
            val updated2 = _questParties.value.toMutableList()
            val idx2 = updated2.indexOfFirst { it.id == newParty.id }
            if (idx2 != -1) {
                val p2 = updated2[idx2]
                if (p2.memberCount < p2.maxMembers) {
                    val newMembers2 = p2.members + joiner2
                    val joinMsg2 = PartyMessage(
                        id = "msg_join_2",
                        senderName = joiner2,
                        senderAvatar = "🛵",
                        text = "Count me in, meeting at $location!",
                        timeText = "Just now",
                        isCurrentUser = false
                    )
                    updated2[idx2] = p2.copy(
                        memberCount = minOf(p2.maxMembers, p2.memberCount + 1),
                        members = newMembers2,
                        messages = p2.messages + joinMsg2
                    )
                    _questParties.value = updated2
                }
            }
        }

        return newParty
    }

    fun createFeedPost(
        caption: String,
        questTitle: String,
        photoEmoji: String,
        authorName: String,
        photoUri: String? = null,
        location: String = "Beirut",
        xpEarned: Int = 450,
        totalSpent: Double? = null,
        questsCompletedCount: Int? = null
    ): FeedPost {
        val dummyQuest = Quest(
            id = "user_q_${System.currentTimeMillis()}",
            title = questTitle,
            description = caption,
            type = QuestType.PHOTO,
            xp = xpEarned,
            location = location
        )
        val newPost = FeedPost(
            id = "user_post_${System.currentTimeMillis()}",
            authorName = authorName,
            authorAvatar = "⚡",
            authorLevelTitle = "Active Adventurer",
            questTitle = questTitle,
            location = location,
            timeAgo = "Just now",
            dramaScore = 95,
            totalScore = 92,
            xpEarned = xpEarned,
            caption = caption,
            likesCount = 1,
            isLiked = true,
            commentsCount = 0,
            comments = emptyList(),
            originalQuest = dummyQuest,
            photoPlaceholderEmoji = photoEmoji,
            photoUri = photoUri,
            totalSpent = totalSpent,
            questsCompletedCount = questsCompletedCount
        )
        storage.saveUserFeedPost(newPost)
        _feedPosts.value = listOf(newPost) + _feedPosts.value
        return newPost
    }

    fun copyQuestToUserQuests(quest: Quest): Boolean {
        storage.saveCopiedQuest(quest)
        return true
    }

    fun joinParty(partyId: String, currentUserName: String) {
        val current = _questParties.value.toMutableList()
        val index = current.indexOfFirst { it.id == partyId }
        if (index != -1) {
            val party = current[index]
            if (!party.isJoined) {
                val updatedMembers = party.members + currentUserName
                val joinMsg = PartyMessage(
                    id = "join_${System.currentTimeMillis()}",
                    senderName = "System",
                    senderAvatar = "⚡",
                    text = "You joined the party.",
                    timeText = "Just now",
                    isCurrentUser = true
                )
                val updated = party.copy(
                    isJoined = true,
                    memberCount = party.memberCount + 1,
                    members = updatedMembers,
                    messages = party.messages + joinMsg
                )
                current[index] = updated
                _questParties.value = current

                // Simulate realistic response after 1.5 seconds!
                scope.launch {
                    delay(1500)
                    val mockResponder = party.members.firstOrNull() ?: "Maya"
                    val mockResponses = listOf(
                        "Let's goo 🔥",
                        "Yalla! Welcome to the squad.",
                        "Finally! We have enough for the quest.",
                        "Sho ya kbeer, ready for some chaos?"
                    )
                    val reply = PartyMessage(
                        id = "reply_${System.currentTimeMillis()}",
                        senderName = mockResponder,
                        senderAvatar = if (mockResponder == "Maya") "🌸" else "⚡",
                        text = mockResponses.random(),
                        timeText = "Just now",
                        isCurrentUser = false
                    )
                    appendPartyMessage(partyId, reply)
                }
            }
        }
    }

    fun leaveParty(partyId: String, currentUserName: String) {
        val current = _questParties.value.toMutableList()
        val index = current.indexOfFirst { it.id == partyId }
        if (index != -1) {
            val party = current[index]
            val updatedMembers = party.members.filter { it != currentUserName }
            val updated = party.copy(
                isJoined = false,
                memberCount = maxOf(1, party.memberCount - 1),
                members = updatedMembers
            )
            current[index] = updated
            _questParties.value = current
        }
    }

    fun sendPartyMessage(partyId: String, text: String, currentUserName: String) {
        val msg = PartyMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderName = currentUserName,
            senderAvatar = "⚡",
            text = text,
            timeText = "Just now",
            isCurrentUser = true
        )
        appendPartyMessage(partyId, msg)

        // Simulate local response after 1.8 seconds!
        scope.launch {
            delay(1800)
            val mockReplies = listOf(
                "100% agree 😂",
                "Heading there right now, don't start without me!",
                "Bro this mission is gonna be legendary.",
                "Meet at the corner in 5 mins."
            )
            val responder = listOf("Karim", "Anthony", "Maya", "Lea").random()
            val reply = PartyMessage(
                id = "reply_${System.currentTimeMillis()}",
                senderName = responder,
                senderAvatar = if (responder == "Maya") "🌸" else "⚡",
                text = mockReplies.random(),
                timeText = "Just now",
                isCurrentUser = false
            )
            appendPartyMessage(partyId, reply)
        }
    }

    private fun appendPartyMessage(partyId: String, message: PartyMessage) {
        val current = _questParties.value.toMutableList()
        val index = current.indexOfFirst { it.id == partyId }
        if (index != -1) {
            val party = current[index]
            val updatedMessages = party.messages + message
            current[index] = party.copy(messages = updatedMessages)
            _questParties.value = current
        }
    }

    fun updateLeaderboardUserScore(userXp: Int) {
        val current = _leaderboardEntries.value.toMutableList()
        val idx = current.indexOfFirst { it.isCurrentUser }
        if (idx != -1) {
            current[idx] = current[idx].copy(scoreXp = userXp)
            current.sortByDescending { it.scoreXp }
            val reRanked = current.mapIndexed { i, entry -> entry.copy(rank = i + 1) }
            _leaderboardEntries.value = reRanked
        }
    }
}
