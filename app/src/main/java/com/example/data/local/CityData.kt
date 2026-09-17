package com.example.data.local

import com.example.data.model.AdventureMode
import com.example.data.model.Quest
import com.example.data.model.QuestType
import com.example.data.model.SecondaryMood
import com.example.data.model.TransportMode
import com.example.data.service.MissionConstraintValidator

object CityData {
    val CITIES = listOf(
        "Beirut",
        "Jounieh",
        "Byblos",
        "Batroun",
        "Zahle",
        "Tripoli",
        "Sidon",
        "Tyre"
    )

    val NEIGHBORHOODS = mapOf(
        "Beirut" to listOf("Hamra", "Mar Mikhael", "Gemmayzeh", "Badaro", "Raouche", "Ain El Mreisseh", "Achrafieh"),
        "Jounieh" to listOf("Old Souk", "Kaslik", "Maameltein", "Teleferique Bay"),
        "Byblos" to listOf("Old Port", "Citadel Quarter", "Roman Road Souk", "Mina Promenade"),
        "Batroun" to listOf("Phoenician Wall", "Mina Alleyways", "Bahsa Bay", "Old Souk"),
        "Zahle" to listOf("Berdawni River", "Midan", "Hoche El Oumara", "Historic Stairs"),
        "Tripoli" to listOf("Mina Harbor", "Old Citadel Souks", "Tall Square", "Khan al-Saboun"),
        "Sidon" to listOf("Sea Castle Port", "Old Khan Souk", "Corniche Promenade", "Soap Museum Quarter"),
        "Tyre" to listOf("Old Port Alleys", "Christian Quarter", "Al Mina Ruins", "Corniche Beach")
    )

    fun getSampleQuests(
        city: String,
        mode: AdventureMode,
        mood: SecondaryMood,
        budgetMax: Double,
        durationMinutes: Int,
        transportMode: TransportMode = TransportMode.WALK
    ): List<Quest> {
        val neighborhoodList = NEIGHBORHOODS[city] ?: listOf(city)
        val primaryNeighborhood = neighborhoodList.first()
        val secondaryNeighborhood = if (neighborhoodList.size > 1) neighborhoodList[1] else primaryNeighborhood

        // Target quest count based on duration (Section 6)
        val targetCount = when {
            mode == AdventureMode.FOOD && durationMinutes <= 60 -> 3
            mode == AdventureMode.FOOD -> 4
            durationMinutes <= 35 -> 2
            durationMinutes <= 75 -> 3
            durationMinutes <= 135 -> 4
            else -> 6
        }

        val pool = mutableListOf<Quest>()
        val isFree = budgetMax <= 0.0

        // ==========================================
        // CITY-SPECIFIC QUEST GENERATION
        // ==========================================
        when (city.lowercase()) {
            "byblos" -> {
                pool.add(
                    Quest(
                        id = "byb_1_${System.currentTimeMillis()}",
                        title = "Phoenician Port Promenade",
                        description = "Walk along the ancient Byblos stone harbor in $primaryNeighborhood and find a fisherman boat with an authentic Lebanese name.",
                        type = QuestType.DISCOVERY,
                        xp = 130,
                        bonusXp = 65,
                        estimatedDurationMinutes = 15,
                        estimatedCostUsd = 0.0,
                        location = "$primaryNeighborhood, Byblos",
                        bonusChallenge = "Capture a reflection of the medieval walls in the seawater.",
                        funnyComment = "Oldest continuously inhabited city. Act like you belong."
                    )
                )
                pool.add(
                    Quest(
                        id = "byb_2_${System.currentTimeMillis()}",
                        title = if (isFree) "The Citadel Rampart Watch" else "Old Souk Lemonade Refreshment",
                        description = if (isFree) {
                            "Navigate the cobblestone pathways around the Crusader Citadel and find the ancient Roman column embedded in a stone wall."
                        } else {
                            "Stroll through the bougainvillea-covered Roman Road Souk and order a chilled fresh mint lemonade under $3."
                        },
                        type = if (isFree) QuestType.LOCATION else QuestType.FOOD,
                        xp = 150,
                        bonusXp = 75,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = if (isFree) 0.0 else minOf(3.0, budgetMax * 0.4),
                        location = "Roman Road Souk, Byblos",
                        bonusChallenge = "Take an aesthetic photo through an ancient stone arch.",
                        funnyComment = "Timeless vibes, zero regrets."
                    )
                )
                pool.add(
                    Quest(
                        id = "byb_3_${System.currentTimeMillis()}",
                        title = "Cobblestone Cat Diplomat",
                        description = "Identify a friendly street cat residing near the historic church courtyards. Bestow upon it a royal Phoenician title.",
                        type = QuestType.PHOTO,
                        xp = 140,
                        bonusXp = 70,
                        estimatedDurationMinutes = 15,
                        estimatedCostUsd = 0.0,
                        location = "Old Quarter, Byblos",
                        bonusChallenge = "Get a close-up portrait where the cat looks like royalty.",
                        funnyComment = "Respect the four-legged local rulers."
                    )
                )
                pool.add(
                    Quest(
                        id = "byb_4_${System.currentTimeMillis()}",
                        title = "Sunset Sea Wall Watch",
                        description = "Reach the edge of the old stone jetty before dusk settles over the Mediterranean horizon.",
                        type = QuestType.FINAL,
                        xp = 180,
                        bonusXp = 90,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = 0.0,
                        location = "Old Port Pier, Byblos",
                        bonusChallenge = "Dramatic silhouette pose facing the open horizon.",
                        funnyComment = "Cinematic finish achieved."
                    )
                )
            }

            "batroun" -> {
                pool.add(
                    Quest(
                        id = "bat_1_${System.currentTimeMillis()}",
                        title = "The Phoenician Wall Conquest",
                        description = "Wander through the historic sea alleyways to the ancient sea wall carved into natural sandstone.",
                        type = QuestType.DISCOVERY,
                        xp = 140,
                        bonusXp = 70,
                        estimatedDurationMinutes = 15,
                        estimatedCostUsd = 0.0,
                        location = "Phoenician Wall, Batroun",
                        bonusChallenge = "Photograph the crest of the waves breaking against the 2,000-year-old stone.",
                        funnyComment = "Nature plus history. Top-tier Mediterranean moment."
                    )
                )
                pool.add(
                    Quest(
                        id = "bat_2_${System.currentTimeMillis()}",
                        title = if (isFree) "Saydet Al Bahr Balcony Clue" else "Iconic Batrouni Lemonade Hunt",
                        description = if (isFree) {
                            "Climb the steps to the Saydet Al Bahr church courtyard and find the nautical sea marker."
                        } else {
                            "Grab an authentic hand-squeezed Batroun lemonade or fresh ice cream under $3."
                        },
                        type = if (isFree) QuestType.LOCATION else QuestType.FOOD,
                        xp = 150,
                        bonusXp = 75,
                        estimatedDurationMinutes = 15,
                        estimatedCostUsd = if (isFree) 0.0 else minOf(3.0, budgetMax * 0.4),
                        location = "Mina Alleyways, Batroun",
                        bonusChallenge = "Cheers your drinks (or imaginary cups) towards the setting sun.",
                        funnyComment = "You cannot leave Batroun without citrus in your bloodstream."
                    )
                )
                pool.add(
                    Quest(
                        id = "bat_3_${System.currentTimeMillis()}",
                        title = "Bahsa Bay Pebble Scavenger",
                        description = "Head down to Bahsa Bay and locate a flat white limestone sea pebble with natural markings.",
                        type = QuestType.PHOTO,
                        xp = 130,
                        bonusXp = 65,
                        estimatedDurationMinutes = 15,
                        estimatedCostUsd = 0.0,
                        location = "Bahsa Bay, Batroun",
                        bonusChallenge = "Skip the pebble or photograph it against the turquoise water.",
                        funnyComment = "100% free, 100% organic dopamine."
                    )
                )
                pool.add(
                    Quest(
                        id = "bat_4_${System.currentTimeMillis()}",
                        title = "The Blue Hour Stroll",
                        description = "Walk through the cobblestone pedestrian souk as warm vintage string lights turn on.",
                        type = QuestType.FINAL,
                        xp = 190,
                        bonusXp = 95,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = 0.0,
                        location = "Old Souk, Batroun",
                        bonusChallenge = "Aesthetic street style photo with glowing street lamps.",
                        funnyComment = "Batroun magic never fails."
                    )
                )
            }

            "jounieh" -> {
                pool.add(
                    Quest(
                        id = "joun_1_${System.currentTimeMillis()}",
                        title = "Old Souk Stone Arcade Walk",
                        description = "Explore the traditional sandstone arches along the historic coastal street of $primaryNeighborhood.",
                        type = QuestType.WALK,
                        xp = 130,
                        bonusXp = 65,
                        estimatedDurationMinutes = 15,
                        estimatedCostUsd = 0.0,
                        location = "Old Souk, Jounieh",
                        bonusChallenge = "Locate an ancient wooden carriage shutter still intact.",
                        funnyComment = "Classic coastal charm."
                    )
                )
                pool.add(
                    Quest(
                        id = "joun_2_${System.currentTimeMillis()}",
                        title = if (isFree) "Teleferique Wire Observation" else "Kaslik Waterfront Treat",
                        description = if (isFree) {
                            "Spot the vintage Teleferique cabins soaring above the hillside pine trees from the shoreline."
                        } else {
                            "Pick up a warm crêpe or iced coffee along Kaslik's main strip under $4."
                        },
                        type = if (isFree) QuestType.DISCOVERY else QuestType.FOOD,
                        xp = 140,
                        bonusXp = 70,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = if (isFree) 0.0 else minOf(4.0, budgetMax * 0.4),
                        location = "Kaslik, Jounieh",
                        bonusChallenge = "Take a high-angle photo looking out at the entire bay arc.",
                        funnyComment = "The bay view never gets old."
                    )
                )
                pool.add(
                    Quest(
                        id = "joun_3_${System.currentTimeMillis()}",
                        title = "Harissa Bay Panorama Challenge",
                        description = "Reach the water edge where the mountain meets the sea and capture the full panorama of Jounieh Bay.",
                        type = QuestType.FINAL,
                        xp = 180,
                        bonusXp = 90,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = 0.0,
                        location = "Teleferique Bay, Jounieh",
                        bonusChallenge = "Pan from sea level all the way to the mountain summit.",
                        funnyComment = "Scenic mastery verified."
                    )
                )
            }

            "zahle" -> {
                pool.add(
                    Quest(
                        id = "zhl_1_${System.currentTimeMillis()}",
                        title = "Berdawni River Walkway",
                        description = "Stroll along the riverside shaded promenade in Berdawni and feel the mountain air cooling down.",
                        type = QuestType.WALK,
                        xp = 130,
                        bonusXp = 65,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = 0.0,
                        location = "Berdawni, Zahle",
                        bonusChallenge = "Photograph the rushing river water under the arched footbridges.",
                        funnyComment = "Neighbor of the stars and valley breeze."
                    )
                )
                pool.add(
                    Quest(
                        id = "zhl_2_${System.currentTimeMillis()}",
                        title = if (isFree) "Historic Midan Balcony Clue" else "Zahlawi Sweet or Kaakeh",
                        description = if (isFree) {
                            "Identify three vintage red-tile roofed houses perched along the steep hillsides of Midan."
                        } else {
                            "Savor traditional Zahle ice cream (Ashta with rose water) or a warm stuffed kaakeh."
                        },
                        type = if (isFree) QuestType.DISCOVERY else QuestType.FOOD,
                        xp = 150,
                        bonusXp = 75,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = if (isFree) 0.0 else minOf(3.5, budgetMax * 0.4),
                        location = "Midan, Zahle",
                        bonusChallenge = "Pose like an old poet reciting verses about the valley.",
                        funnyComment = "City of poetry and generous portions."
                    )
                )
                pool.add(
                    Quest(
                        id = "zhl_3_${System.currentTimeMillis()}",
                        title = "The High Staircase Challenge",
                        description = "Climb one of Zahle's historic steep residential staircases connecting upper and lower quarters.",
                        type = QuestType.FINAL,
                        xp = 190,
                        bonusXp = 95,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = 0.0,
                        location = "Historic Stairs, Zahle",
                        bonusChallenge = "Count the steps and take a triumphant summit selfie looking over the Bekaa plain.",
                        funnyComment = "Leg day accomplished. Views earned."
                    )
                )
            }

            "tripoli" -> {
                pool.add(
                    Quest(
                        id = "trp_1_${System.currentTimeMillis()}",
                        title = "Mina Harbor Heritage Drift",
                        description = "Explore the historic port district of Al-Mina and find the vintage colorful boat workshops.",
                        type = QuestType.WALK,
                        xp = 130,
                        bonusXp = 65,
                        estimatedDurationMinutes = 15,
                        estimatedCostUsd = 0.0,
                        location = "Mina Harbor, Tripoli",
                        bonusChallenge = "Capture the weathered turquoise paint on an authentic fishing boat.",
                        funnyComment = "Authentic coastal soul."
                    )
                )
                pool.add(
                    Quest(
                        id = "trp_2_${System.currentTimeMillis()}",
                        title = if (isFree) "Mameluke Architectural Scavenger" else "Tripolitan Kaakeh or Halawet El Jibn",
                        description = if (isFree) {
                            "Locate the ornate black-and-white ablaq stonework arches in the historic souks."
                        } else {
                            "Order fresh Halawet El Jibn or a sesame-crusted kaakeh with picon cheese under $3."
                        },
                        type = if (isFree) QuestType.DISCOVERY else QuestType.FOOD,
                        xp = 160,
                        bonusXp = 80,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = if (isFree) 0.0 else minOf(3.0, budgetMax * 0.4),
                        location = "Old Citadel Souks, Tripoli",
                        bonusChallenge = "Photograph the bustling market colors without stopping pedestrian traffic.",
                        funnyComment = "Unmatched hospitality and flavor."
                    )
                )
                pool.add(
                    Quest(
                        id = "trp_3_${System.currentTimeMillis()}",
                        title = "The Citadel Shadow Clue",
                        description = "Walk up towards the foot of the Raymond de Saint-Gilles Citadel and locate the ancient stone gate.",
                        type = QuestType.FINAL,
                        xp = 190,
                        bonusXp = 95,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = 0.0,
                        location = "Tall Square, Tripoli",
                        bonusChallenge = "Silhouette photo against the towering stone battlements.",
                        funnyComment = "Epic fortress finish."
                    )
                )
            }

            "sidon" -> {
                pool.add(
                    Quest(
                        id = "sdn_1_${System.currentTimeMillis()}",
                        title = "The Sea Castle Port Approach",
                        description = "Walk along the stone causeway towards the Crusader Sea Castle surrounded by open Mediterranean breakers.",
                        type = QuestType.DISCOVERY,
                        xp = 140,
                        bonusXp = 70,
                        estimatedDurationMinutes = 15,
                        estimatedCostUsd = 0.0,
                        location = "Sea Castle Port, Sidon",
                        bonusChallenge = "Capture sea spray splashing against the fortress foundation.",
                        funnyComment = "Centuries of sea defenses in one frame."
                    )
                )
                pool.add(
                    Quest(
                        id = "sdn_2_${System.currentTimeMillis()}",
                        title = if (isFree) "Vaulted Souk Alleys Discovery" else "Traditional Seniyora Sweet Run",
                        description = if (isFree) {
                            "Navigate the arched covered alleyways of the Old City and discover an ancient stone doorway."
                        } else {
                            "Taste authentic melt-in-your-mouth Sanioura cookies or sweet tamriyeh under $3."
                        },
                        type = if (isFree) QuestType.LOCATION else QuestType.FOOD,
                        xp = 150,
                        bonusXp = 75,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = if (isFree) 0.0 else minOf(3.0, budgetMax * 0.4),
                        location = "Old Khan Souk, Sidon",
                        bonusChallenge = "Take a picture through a vaulted tunnel into a sunny courtyard.",
                        funnyComment = "The true scent of bay leaves and history."
                    )
                )
                pool.add(
                    Quest(
                        id = "sdn_3_${System.currentTimeMillis()}",
                        title = "Corniche Sunset Horizon",
                        description = "Stroll along the palm-lined maritime corniche as local vendors set up tea samovars.",
                        type = QuestType.FINAL,
                        xp = 180,
                        bonusXp = 90,
                        estimatedDurationMinutes = 20,
                        estimatedCostUsd = 0.0,
                        location = "Corniche Promenade, Sidon",
                        bonusChallenge = "Photo of the sun dropping straight into the Mediterranean.",
                        funnyComment = "Sidon evening unlocked."
                    )
                )
            }

            else -> {
                // BEIRUT & DEFAULT
                if (isFree || mode == AdventureMode.BROKE) {
                    pool.add(
                        Quest(
                            id = "bkt_1_${System.currentTimeMillis()}",
                            title = "The $0 Architectural Hunt",
                            description = "Walk through $primaryNeighborhood and locate a triple-arched heritage window with vintage wrought-iron railings.",
                            type = QuestType.DISCOVERY,
                            xp = 130,
                            bonusXp = 65,
                            estimatedDurationMinutes = 15,
                            estimatedCostUsd = 0.0,
                            location = "$primaryNeighborhood, Beirut",
                            bonusChallenge = "Take an editorial architecture photo emphasizing symmetry.",
                            funnyComment = "Culture is 100% free."
                        )
                    )
                    pool.add(
                        Quest(
                            id = "bkt_2_${System.currentTimeMillis()}",
                            title = "Street Art & Secret Alley",
                            description = "Navigate through narrow alleyways to find an underground mural or vintage painted staircase.",
                            type = QuestType.PHOTO,
                            xp = 150,
                            bonusXp = 75,
                            estimatedDurationMinutes = 20,
                            estimatedCostUsd = 0.0,
                            location = "Staircases of $secondaryNeighborhood",
                            bonusChallenge = "Strike an album cover pose without blocking pedestrians.",
                            funnyComment = "Ma fi budget? Fine, we improvise."
                        )
                    )
                    pool.add(
                        Quest(
                            id = "bkt_3_${System.currentTimeMillis()}",
                            title = "The Spy Observation Challenge",
                            description = "Find a public ledge, park bench, or coastal railing. Identify three completely bizarre juxtapositions in the city fabric.",
                            type = QuestType.LOCATION,
                            xp = 140,
                            bonusXp = 70,
                            estimatedDurationMinutes = 15,
                            estimatedCostUsd = 0.0,
                            location = "Public Overlook, $city",
                            bonusChallenge = "Whisper a fake secret code phrase with a deadpan expression.",
                            funnyComment = "Undercover operative status unlocked."
                        )
                    )
                    pool.add(
                        Quest(
                            id = "bkt_4_${System.currentTimeMillis()}",
                            title = "The Sunset Free Viewpoint",
                            description = "Sprint to the nearest public viewpoint or seaside promenade before dusk turns to night.",
                            type = QuestType.FINAL,
                            xp = 190,
                            bonusXp = 95,
                            estimatedDurationMinutes = 20,
                            estimatedCostUsd = 0.0,
                            location = "Corniche Promenade, $city",
                            bonusChallenge = "Victory jump silhouette against the golden hour sky.",
                            funnyComment = "Against all odds, you made $0 look cinematic."
                        )
                    )
                } else if (mode == AdventureMode.FOOD) {
                    pool.add(
                        Quest(
                            id = "bfd_1_${System.currentTimeMillis()}",
                            title = "Sizzling Street Snack Attack",
                            description = "Track down the crispiest falafel, manousheh, or shawarma in $primaryNeighborhood.",
                            type = QuestType.FOOD,
                            xp = 160,
                            bonusXp = 80,
                            estimatedDurationMinutes = 20,
                            estimatedCostUsd = minOf(4.5, budgetMax * 0.4),
                            location = "$primaryNeighborhood, $city",
                            bonusChallenge = "Ask the chef what secret seasoning makes it legendary.",
                            funnyComment = "Diet starts on Monday. It is not Monday."
                        )
                    )
                    pool.add(
                        Quest(
                            id = "bfd_2_${System.currentTimeMillis()}",
                            title = "Condiment Showdown",
                            description = "Sample a potent house sauce (tarator, extra toum, or spicy shatter) and record the group's reaction.",
                            type = QuestType.DISCOVERY,
                            xp = 140,
                            bonusXp = 70,
                            estimatedDurationMinutes = 15,
                            estimatedCostUsd = minOf(2.5, budgetMax * 0.2),
                            location = "Local Snack Counter",
                            bonusChallenge = "Face reaction photo with 100% genuine emotion.",
                            funnyComment = "Extra garlic is non-negotiable."
                        )
                    )
                    pool.add(
                        Quest(
                            id = "bfd_3_${System.currentTimeMillis()}",
                            title = "The Sweet Coma Finale",
                            description = "Cap off the culinary run with fresh knafeh, artisanal gelato, or freshly squeezed mint lemonade.",
                            type = QuestType.FINAL,
                            xp = 200,
                            bonusXp = 100,
                            estimatedDurationMinutes = 20,
                            estimatedCostUsd = minOf(4.0, budgetMax * 0.3),
                            location = "$secondaryNeighborhood Sweet Shop",
                            bonusChallenge = "Mandatory slow cheese-pull or dessert drizzle photo.",
                            funnyComment = "Certified food coma achieved."
                        )
                    )
                } else {
                    // Chaos / Chill / Adventure / Date / Bored
                    pool.add(
                        Quest(
                            id = "gen_1_${System.currentTimeMillis()}",
                            title = if (mood == SecondaryMood.CHAOS) "Find Something Questionable" else "The Neighborhood Vibe Check",
                            description = if (mood == SecondaryMood.CHAOS) {
                                "Explore $primaryNeighborhood and find the single most inexplicable or bizarre decorative object."
                            } else {
                                "Walk through $primaryNeighborhood and discover a hidden courtyard or shaded café terrace with zero loud traffic."
                            },
                            type = QuestType.DISCOVERY,
                            xp = 140,
                            bonusXp = 70,
                            estimatedDurationMinutes = 15,
                            estimatedCostUsd = if (isFree) 0.0 else minOf(2.0, budgetMax * 0.2),
                            location = "$primaryNeighborhood, $city",
                            bonusChallenge = "Stage a photo treating your discovery like a sacred historical relic.",
                            funnyComment = "The city provides if you know where to look."
                        )
                    )
                    pool.add(
                        Quest(
                            id = "gen_2_${System.currentTimeMillis()}",
                            title = "The Stranger's Recommendation",
                            description = "Ask a friendly local or shopkeeper in $primaryNeighborhood for their favorite hidden landmark or staircase.",
                            type = QuestType.SOCIAL,
                            xp = 160,
                            bonusXp = 80,
                            estimatedDurationMinutes = 20,
                            estimatedCostUsd = 0.0,
                            location = "$primaryNeighborhood Streets",
                            bonusChallenge = "Follow their direction immediately without second-guessing.",
                            funnyComment = "Local knowledge level: Suspiciously High."
                        )
                    )
                    pool.add(
                        Quest(
                            id = "gen_3_${System.currentTimeMillis()}",
                            title = "High Drama Photo Shoot",
                            description = "Stage an aggressively dramatic, high-fashion pose in front of ordinary urban architecture.",
                            type = QuestType.PHOTO,
                            xp = 170,
                            bonusXp = 85,
                            estimatedDurationMinutes = 15,
                            estimatedCostUsd = 0.0,
                            location = "Vintage Corner, $city",
                            bonusChallenge = "Intensity level must be at least 110%.",
                            funnyComment = "Vogue called. They're confused but intrigued."
                        )
                    )
                    pool.add(
                        Quest(
                            id = "gen_4_${System.currentTimeMillis()}",
                            title = "The Finish Line Sprint",
                            description = "Reach the designated landmark before your mission countdown hits zero.",
                            type = QuestType.FINAL,
                            xp = 210,
                            bonusXp = 105,
                            estimatedDurationMinutes = 20,
                            estimatedCostUsd = 0.0,
                            location = "Promenade Landmark, $city",
                            bonusChallenge = "Group celebratory victory pose.",
                            funnyComment = "Mission cleared. You survived."
                        )
                    )
                }
            }
        }

        // Apply MissionConstraintValidator to filter valid quests
        var timeRemaining = durationMinutes
        var budgetRemaining = budgetMax
        val validQuests = mutableListOf<Quest>()

        for (quest in pool) {
            if (MissionConstraintValidator.isQuestValid(
                    quest = quest,
                    budgetRemaining = budgetRemaining,
                    timeRemainingMinutes = timeRemaining,
                    transportMode = transportMode,
                    location = city
                )
            ) {
                validQuests.add(quest)
                timeRemaining = maxOf(5, timeRemaining - quest.estimatedDurationMinutes)
                budgetRemaining = maxOf(0.0, budgetRemaining - quest.estimatedCostUsd)
                if (validQuests.size >= targetCount) break
            }
        }

        // Fallback safety: ensure at least targetCount or non-empty
        return if (validQuests.isNotEmpty()) validQuests else pool.take(targetCount)
    }
}
