package com.example.data.model

enum class AdventureMode(val title: String, val arabicTitle: String, val subtitle: String, val emoji: String) {
    BORED("We're Bored", "وين نروح؟", "Turn anywhere into an adventure", "⚡"),
    BROKE("Broke", "على الصفر", "Financially unavailable, mentally unhinged", "💸"),
    DATE("Date", "مغروم", "Impress without looking like you tried", "❤️"),
    FOOD("Food", "جوعان", "Feed the soul, abandon all diet plans", "🍔"),
    ROULETTE("Roulette", "عالحظ", "Let fate make the questionable decisions", "🎰")
}

enum class SecondaryMood(val title: String, val emoji: String, val description: String) {
    CHAOS("Chaos", "🔥", "Expect the unexpected. Slightly unhinged."),
    CHILL("Chill", "🌊", "Low effort, immaculate vibes."),
    ADVENTURE("Adventure", "⚡", "Active wandering, secret alleys."),
    MYSTERY("Mystery", "🕵️", "Hidden spots and cryptic clues."),
    WILD_CARD("Wild Card", "🎲", "Total wildcard. No guarantees.")
}

enum class QuestType(val displayName: String, val icon: String) {
    LOCATION("Location", "📍"),
    PHOTO("Photo Challenge", "📸"),
    PHOTO_PROOF("Photo Proof", "📸"),
    FOOD("Food Quest", "🍔"),
    SOCIAL("Social", "👥"),
    SPEED("Speed Run", "⚡"),
    DISCOVERY("Discovery", "🔍"),
    RANDOM("Wildcard", "🎲"),
    FINAL("Final Destination", "🏁"),
    MYSTERY("Mystery", "🗝️"),
    WALK("On Foot", "🚶")
}

enum class TransportMode(val displayName: String, val arabicSubtitle: String, val icon: String) {
    WALK("ON FOOT", "ع اجريك يا بيك؟", "🚶"),
    CAR("CAR", "مرتاح عوضعك بالبنزين؟", "🚗")
}

data class AdventureRequest(
    val location: String = "Beirut",
    val groupSize: String = "3",
    val budget: String = "$10",
    val duration: String = "1 hour",
    val transportMode: TransportMode = TransportMode.WALK,
    val transportation: String = transportMode.displayName,
    val mode: AdventureMode = AdventureMode.BORED,
    val secondaryMood: SecondaryMood = SecondaryMood.CHAOS,
    val interests: List<String> = listOf("Food", "Adventure")
)
