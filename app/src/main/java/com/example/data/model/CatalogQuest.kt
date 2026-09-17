package com.example.data.model

data class CatalogQuest(
    val id: String,
    val title: String,
    val description: String,
    val city: String,
    val neighborhood: String,
    val mode: AdventureMode,
    val category: String,
    val minimumBudget: Double = 0.0,
    val maximumExpectedSpend: Double = 0.0,
    val minimumDuration: Int = 10,
    val maximumDuration: Int = 35,
    val allowedTransportModes: List<TransportMode> = listOf(TransportMode.WALK, TransportMode.CAR),
    val groupCompatibility: List<String> = listOf("SOLO", "DUO", "QUAD", "SQUAD", "2", "3", "4", "5+"),
    val timeOfDay: String = "ANY", // ANY, DAY, SUNSET, EVENING, NIGHT
    val difficulty: String = "Easy",
    val estimatedDuration: Int = 15,
    val estimatedCost: Double = 0.0,
    val xp: Int = 180,
    val proofRequired: Boolean = true,
    val type: QuestType = QuestType.PHOTO_PROOF,
    val bonusChallenge: String? = null,
    val approxLatitude: Double? = null,
    val approxLongitude: Double? = null,
    val tags: List<String> = emptyList()
) {
    fun toQuest(bonusXp: Int = 80): Quest = Quest(
        id = id,
        title = title,
        description = description,
        type = type,
        xp = xp,
        bonusXp = bonusXp,
        estimatedDurationMinutes = estimatedDuration,
        estimatedCostUsd = estimatedCost,
        actualSpent = 0.0,
        location = "$neighborhood, $city",
        bonusChallenge = bonusChallenge,
        proofRequired = proofRequired
    )
}
