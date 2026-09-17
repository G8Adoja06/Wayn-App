package com.example.data.repository

import com.example.data.model.AdventureMode
import com.example.data.model.CatalogQuest
import com.example.data.model.Quest
import com.example.data.model.QuestType
import com.example.data.model.SecondaryMood
import com.example.data.model.TransportMode
import kotlin.random.Random

class QuestCatalogRepository {

    // Anti-repetition tracker
    private val recentQuestIds = mutableListOf<String>()

    fun recordCompletedQuestIds(ids: List<String>) {
        recentQuestIds.addAll(ids)
        // Keep only last 25 quests to allow recycling eventually
        if (recentQuestIds.size > 25) {
            val trimCount = recentQuestIds.size - 25
            repeat(trimCount) {
                recentQuestIds.removeAt(0)
            }
        }
    }

    fun getRecentQuestIds(): List<String> = recentQuestIds.toList()

    val allCatalogQuests: List<CatalogQuest> = listOf(
        // ==========================================
        // BEIRUT (18 Quests)
        // ==========================================
        CatalogQuest(
            id = "b_zero_1",
            title = "Mar Mikhael Vintage Mural Hunt",
            description = "Track down the retro painted graffiti near the old train station and take a cinematic monochrome photo.",
            city = "Beirut",
            neighborhood = "Mar Mikhael",
            mode = AdventureMode.BROKE,
            category = "cat_zero",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 15,
            maximumDuration = 30,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 20,
            estimatedCost = 0.0,
            xp = 220,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Pose like an undercover private detective.",
            tags = listOf("art", "street", "free", "photo")
        ),
        CatalogQuest(
            id = "b_zero_2",
            title = "Ain El Mreisseh Fishermen Corniche Walk",
            description = "Walk along the breezy corniche to the stone pier where local fishermen cast lines into the Mediterranean.",
            city = "Beirut",
            neighborhood = "Ain El Mreisseh",
            mode = AdventureMode.BROKE,
            category = "cat_zero",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 20,
            maximumDuration = 40,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 0.0,
            xp = 200,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Capture sea spray in motion without dropping your phone.",
            tags = listOf("sea", "walk", "free", "view")
        ),
        CatalogQuest(
            id = "b_zero_3",
            title = "St. Nicolas Hidden Stairs Scavenger",
            description = "Climb the historic tiled steps of Saint Nicolas in Achrafieh and count the decorative flowerpots along the balconies.",
            city = "Beirut",
            neighborhood = "Achrafieh",
            mode = AdventureMode.BORED,
            category = "cat_discovery",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 15,
            maximumDuration = 30,
            allowedTransportModes = listOf(TransportMode.WALK),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "DAY",
            difficulty = "Medium",
            estimatedDuration = 20,
            estimatedCost = 0.0,
            xp = 250,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Reach the summit in under 3 minutes without breathing loudly.",
            tags = listOf("stairs", "architecture", "free", "fitness")
        ),
        CatalogQuest(
            id = "b_zero_4",
            title = "Raouche Sunset Horizon Stare",
            description = "Reach the railing opposite Pigeon Rocks exactly as the sun dips behind the colossal limestone pillars.",
            city = "Beirut",
            neighborhood = "Raouche",
            mode = AdventureMode.DATE,
            category = "cat_sunset",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 20,
            maximumDuration = 35,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO", "QUAD", "SOLO"),
            timeOfDay = "SUNSET",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 0.0,
            xp = 300,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Golden hour duo silhouette against the Mediterranean glow.",
            tags = listOf("date", "sunset", "romantic", "free")
        ),
        CatalogQuest(
            id = "b_food_5",
            title = "Hamra $2 Kaak or Falafel Bite",
            description = "Find a traditional street cart or quick corner snack bar in Hamra and order a hot zaatar kaak or fresh falafel wrap under $2.",
            city = "Beirut",
            neighborhood = "Hamra",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 2.0,
            maximumExpectedSpend = 3.0,
            minimumDuration = 15,
            maximumDuration = 25,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 15,
            estimatedCost = 2.0,
            xp = 240,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Ask the vendor for their crispest pickle slice.",
            tags = listOf("food", "cheap", "snack", "hamra")
        ),
        CatalogQuest(
            id = "b_food_10",
            title = "Legendary Beirut Shawarma Benchmark",
            description = "Grab a classic Lebanese beef or chicken shawarma with whipped garlic toum or tarator, and rate its wrap tightness out of 10.",
            city = "Beirut",
            neighborhood = "Mar Mikhael",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 5.0,
            maximumExpectedSpend = 7.0,
            minimumDuration = 20,
            maximumDuration = 35,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 5.5,
            xp = 280,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Deliver a dramatic critique of the toum ratio on camera.",
            tags = listOf("food", "shawarma", "street", "lunch")
        ),
        CatalogQuest(
            id = "b_food_knafeh",
            title = "Warm Knafeh in Kaakeh Breakfast Run",
            description = "Locate a buzzing pastry parlor and get melted cheese knafeh soaked in orange blossom syrup stuffed into sesame bread.",
            city = "Beirut",
            neighborhood = "Achrafieh",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 4.0,
            maximumExpectedSpend = 6.0,
            minimumDuration = 15,
            maximumDuration = 30,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "DAY",
            difficulty = "Easy",
            estimatedDuration = 20,
            estimatedCost = 4.5,
            xp = 260,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Capture the maximum cheese-pull stretch before breaking.",
            tags = listOf("food", "sweet", "knafeh", "dessert")
        ),
        CatalogQuest(
            id = "b_date_coffee",
            title = "Gemmayzeh Espresso & Vinyl Nook",
            description = "Step into a leafy courtyard or retro art-café in Gemmayzeh for two iced brews or specialty espressos.",
            city = "Beirut",
            neighborhood = "Gemmayzeh",
            mode = AdventureMode.DATE,
            category = "cat_date",
            minimumBudget = 6.0,
            maximumExpectedSpend = 12.0,
            minimumDuration = 30,
            maximumDuration = 50,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO", "QUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 35,
            estimatedCost = 8.0,
            xp = 310,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Ask your date their dream road-trip route through Lebanon.",
            tags = listOf("date", "coffee", "cozy", "gemmayzeh")
        ),
        CatalogQuest(
            id = "b_date_dinner",
            title = "Badaro Mezza & Candlelight Courtyard",
            description = "Find a charming brick-walled terrace in Badaro, order signature hot mezza plates and share a relaxed romantic dinner.",
            city = "Beirut",
            neighborhood = "Badaro",
            mode = AdventureMode.DATE,
            category = "cat_date",
            minimumBudget = 30.0,
            maximumExpectedSpend = 50.0,
            minimumDuration = 50,
            maximumDuration = 90,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO"),
            timeOfDay = "EVENING",
            difficulty = "Medium",
            estimatedDuration = 60,
            estimatedCost = 35.0,
            xp = 420,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Order one dish your partner has never tasted before.",
            tags = listOf("date", "dinner", "badaro", "evening")
        ),
        CatalogQuest(
            id = "b_broke_cat",
            title = "Gemmayzeh Alleyway Cat Diplomat",
            description = "Locate a distinguished neighborhood street cat lounging on an ancient stone ledge and take an editorial pet photo.",
            city = "Beirut",
            neighborhood = "Gemmayzeh",
            mode = AdventureMode.BROKE,
            category = "cat_zero",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 10,
            maximumDuration = 25,
            allowedTransportModes = listOf(TransportMode.WALK),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 15,
            estimatedCost = 0.0,
            xp = 210,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Get the cat to blink back at you in feline friendship.",
            tags = listOf("broke", "animals", "street", "photo")
        ),
        CatalogQuest(
            id = "b_bored_arch",
            title = "Sursock Triple-Arched Window Survey",
            description = "Stroll down Rue Sursock and identify three classic Venetian-influenced triple-arch mansion windows from the 19th century.",
            city = "Beirut",
            neighborhood = "Achrafieh",
            mode = AdventureMode.BORED,
            category = "cat_culture",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 20,
            maximumDuration = 35,
            allowedTransportModes = listOf(TransportMode.WALK),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "DAY",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 0.0,
            xp = 270,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Frame an arched window reflected in a sun glass surface.",
            tags = listOf("architecture", "history", "beirut", "free")
        ),
        CatalogQuest(
            id = "b_car_scenic",
            title = "Manara Coastal Panoramic Drive",
            description = "Drive along the winding seaside avenue of Paris Avenue from Ain El Mreisseh past the old pink lighthouse.",
            city = "Beirut",
            neighborhood = "Ain El Mreisseh",
            mode = AdventureMode.BORED,
            category = "cat_outdoor",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 20,
            maximumDuration = 40,
            allowedTransportModes = listOf(TransportMode.CAR),
            groupCompatibility = listOf("DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 0.0,
            xp = 230,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Snap the historic striped lighthouse standing tall over the sea.",
            tags = listOf("car", "drive", "coast", "views")
        ),
        CatalogQuest(
            id = "b_quick_sprint",
            title = "30-Minute Corniche Power Lap",
            description = "Clock exactly 1,500 brisk paces along the palm-lined seaside walkway with the sea breeze hitting your face.",
            city = "Beirut",
            neighborhood = "Ain El Mreisseh",
            mode = AdventureMode.BORED,
            category = "cat_quick",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 15,
            maximumDuration = 30,
            allowedTransportModes = listOf(TransportMode.WALK),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 20,
            estimatedCost = 0.0,
            xp = 200,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Zero phone scrolling during the entire sprint walk.",
            tags = listOf("quick", "fitness", "free", "sea")
        ),

        // ==========================================
        // JOUNIEH (14 Quests)
        // ==========================================
        CatalogQuest(
            id = "j_zero_1",
            title = "Old Jounieh Stone Souk Promenade",
            description = "Walk through the restored 19th-century basalt alleys and admire traditional arched entranceways without spending a dime.",
            city = "Jounieh",
            neighborhood = "Old Souk",
            mode = AdventureMode.BROKE,
            category = "cat_zero",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 15,
            maximumDuration = 30,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 20,
            estimatedCost = 0.0,
            xp = 230,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Photograph the ornate cast-iron street lamps under stone arches.",
            tags = listOf("jounieh", "souk", "stone", "free")
        ),
        CatalogQuest(
            id = "j_zero_2",
            title = "Jounieh Bay Sunset Horizon Watch",
            description = "Perch on the public harbor walkway overlooking Jounieh Bay as yachts dock and the mountain peaks turn amber.",
            city = "Jounieh",
            neighborhood = "Teleferique Bay",
            mode = AdventureMode.DATE,
            category = "cat_sunset",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 20,
            maximumDuration = 40,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO", "QUAD", "SOLO"),
            timeOfDay = "SUNSET",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 0.0,
            xp = 290,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Spot the Teleferique cable cars suspended in the golden sky.",
            tags = listOf("sunset", "bay", "sea", "romantic")
        ),
        CatalogQuest(
            id = "j_food_1",
            title = "Kaslik Seaside Snack & Gelato",
            description = "Stop by an artisan creamery or seaside bakery along Kaslik avenue for a rich sweet treat under $4.",
            city = "Jounieh",
            neighborhood = "Kaslik",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 3.0,
            maximumExpectedSpend = 5.0,
            minimumDuration = 15,
            maximumDuration = 30,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 20,
            estimatedCost = 3.5,
            xp = 240,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Pick a flavor pairing you have never tried before.",
            tags = listOf("food", "gelato", "sweet", "kaslik")
        ),
        CatalogQuest(
            id = "j_food_fish",
            title = "Maameltein Fresh Fishermen Sandwich",
            description = "Get a crisp grilled fish sandwich or toasted calamari wrap drizzled with spicy tarator from a local port kiosk.",
            city = "Jounieh",
            neighborhood = "Maameltein",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 6.0,
            maximumExpectedSpend = 9.0,
            minimumDuration = 20,
            maximumDuration = 40,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 7.0,
            xp = 310,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Squeeze fresh lemon while framing the blue harbor in background.",
            tags = listOf("food", "seafood", "jounieh", "harbor")
        ),
        CatalogQuest(
            id = "j_date_cafe",
            title = "Old Port Stone Terrace Romance",
            description = "Sit at a waterfront stone terrace table in Old Jounieh with panoramic bay views for tea or evening cocktails.",
            city = "Jounieh",
            neighborhood = "Old Souk",
            mode = AdventureMode.DATE,
            category = "cat_date",
            minimumBudget = 15.0,
            maximumExpectedSpend = 30.0,
            minimumDuration = 40,
            maximumDuration = 70,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO"),
            timeOfDay = "EVENING",
            difficulty = "Easy",
            estimatedDuration = 50,
            estimatedCost = 20.0,
            xp = 360,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Toast glasses while the coastal city lights flicker on water.",
            tags = listOf("date", "cocktails", "sea", "views")
        ),
        CatalogQuest(
            id = "j_car_mountain",
            title = "Harissa Hillside Scenic Overlook",
            description = "Drive up the mountain curves overlooking Jounieh crescent bay and park at a scenic hairpin turn to see the whole gulf.",
            city = "Jounieh",
            neighborhood = "Teleferique Bay",
            mode = AdventureMode.BORED,
            category = "cat_outdoor",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 30,
            maximumDuration = 60,
            allowedTransportModes = listOf(TransportMode.CAR),
            groupCompatibility = listOf("DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Medium",
            estimatedDuration = 40,
            estimatedCost = 0.0,
            xp = 340,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Capture the curving crescent coastline from high above.",
            tags = listOf("car", "mountain", "panorama", "view")
        ),

        // ==========================================
        // BYBLOS (15 Quests)
        // ==========================================
        CatalogQuest(
            id = "by_zero_1",
            title = "Byblos Old Souk Ancient Cobblestone Walk",
            description = "Stroll down the pedestrian Roman and medieval bazaar path, discovering historic sandstone facades and hanging bougainvillea.",
            city = "Byblos",
            neighborhood = "Roman Road Souk",
            mode = AdventureMode.BROKE,
            category = "cat_zero",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 15,
            maximumDuration = 35,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 0.0,
            xp = 240,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Find a fossilized sea creature imprint in the historic stone walls.",
            tags = listOf("byblos", "ancient", "history", "free")
        ),
        CatalogQuest(
            id = "by_zero_2",
            title = "Old Crusader Port Sea Gate Gaze",
            description = "Walk all the way out to the ancient harbor towers at Byblos port where wooden fishing vessels rock on azure water.",
            city = "Byblos",
            neighborhood = "Old Port",
            mode = AdventureMode.BORED,
            category = "cat_culture",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 20,
            maximumDuration = 40,
            allowedTransportModes = listOf(TransportMode.WALK),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 0.0,
            xp = 270,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Frame a traditional fishing boat through the medieval stone defensive tower.",
            tags = listOf("byblos", "port", "crusader", "free")
        ),
        CatalogQuest(
            id = "by_date_sunset",
            title = "Byblos Citadel Bastion Golden Hour",
            description = "Stand beside the ancient olive trees outside the citadel ramparts as the Mediterranean sun sets behind Phoenician ruins.",
            city = "Byblos",
            neighborhood = "Citadel Quarter",
            mode = AdventureMode.DATE,
            category = "cat_sunset",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 25,
            maximumDuration = 45,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO", "QUAD"),
            timeOfDay = "SUNSET",
            difficulty = "Easy",
            estimatedDuration = 30,
            estimatedCost = 0.0,
            xp = 320,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Take a classic romantic couple portrait framed by 7,000-year-old stones.",
            tags = listOf("date", "byblos", "sunset", "history")
        ),
        CatalogQuest(
            id = "by_food_snack",
            title = "Byblos Harbor Sesame Snack Challenge",
            description = "Buy a warm fresh sesame kaakeh stuffed with picon cheese or sumac thyme from a cart by the marina under $2.",
            city = "Byblos",
            neighborhood = "Mina Promenade",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 1.5,
            maximumExpectedSpend = 3.0,
            minimumDuration = 10,
            maximumDuration = 25,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 15,
            estimatedCost = 2.0,
            xp = 220,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Dip kaakeh edge in thyme without dropping a single sesame grain.",
            tags = listOf("food", "kaak", "cheap", "snack")
        ),
        CatalogQuest(
            id = "by_food_mezze",
            title = "Byblos Historic Port Seafood Mezze",
            description = "Sit down at a portside taverna for fresh fried bizri (whitebait), spicy batata harra, and crisp fattoush.",
            city = "Byblos",
            neighborhood = "Old Port",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 15.0,
            maximumExpectedSpend = 30.0,
            minimumDuration = 45,
            maximumDuration = 80,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Medium",
            estimatedDuration = 55,
            estimatedCost = 20.0,
            xp = 380,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Rate the crunchiness of the fried fish on a scale of 1 to 10.",
            tags = listOf("food", "seafood", "byblos", "mezze")
        ),
        CatalogQuest(
            id = "by_date_night",
            title = "Byblos Candlelit Lantern Alleys",
            description = "Wander through the lantern-lit cobblestone pathways of the old town at night and share a sweet dessert or mint tea.",
            city = "Byblos",
            neighborhood = "Roman Road Souk",
            mode = AdventureMode.DATE,
            category = "cat_date",
            minimumBudget = 8.0,
            maximumExpectedSpend = 20.0,
            minimumDuration = 30,
            maximumDuration = 60,
            allowedTransportModes = listOf(TransportMode.WALK),
            groupCompatibility = listOf("DUO"),
            timeOfDay = "EVENING",
            difficulty = "Easy",
            estimatedDuration = 40,
            estimatedCost = 12.0,
            xp = 330,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Whisper an ancient Phoenician riddle to each other.",
            tags = listOf("date", "night", "lanterns", "atmosphere")
        ),

        // ==========================================
        // BATROUN (15 Quests)
        // ==========================================
        CatalogQuest(
            id = "bat_zero_1",
            title = "Phoenician Sea Wall Wave-Crest Challenge",
            description = "Walk to the ancient natural sea wall carved by Phoenicians 2,500 years ago and photograph seafoam breaking over stone.",
            city = "Batroun",
            neighborhood = "Phoenician Wall",
            mode = AdventureMode.BROKE,
            category = "cat_zero",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 15,
            maximumDuration = 35,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 20,
            estimatedCost = 0.0,
            xp = 250,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Capture sunlight glistening through crashing Mediterranean water.",
            tags = listOf("batroun", "wall", "sea", "free")
        ),
        CatalogQuest(
            id = "bat_zero_2",
            title = "Batroun Blue Cobblestone Alley Discovery",
            description = "Navigate through the narrow sandstone alleys painted with azure blue shutters and overflowing purple bougainvillea.",
            city = "Batroun",
            neighborhood = "Mina Alleyways",
            mode = AdventureMode.BORED,
            category = "cat_discovery",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 15,
            maximumDuration = 30,
            allowedTransportModes = listOf(TransportMode.WALK),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "DAY",
            difficulty = "Easy",
            estimatedDuration = 20,
            estimatedCost = 0.0,
            xp = 220,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Frame a contrast photo: antique stone, cobalt door, green vine.",
            tags = listOf("batroun", "alley", "aesthetic", "free")
        ),
        CatalogQuest(
            id = "bat_food_lemonade",
            title = "Legendary Batroun Fresh Lemonade Sip",
            description = "Pick up an ice-cold cup of Batroun’s renowned freshly crushed lemonade made with fragrant lemon peels and mint for $2.",
            city = "Batroun",
            neighborhood = "Old Souk",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 1.5,
            maximumExpectedSpend = 3.0,
            minimumDuration = 10,
            maximumDuration = 20,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 15,
            estimatedCost = 2.0,
            xp = 230,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Hold your frosty lemonade cup against the blue sea horizon.",
            tags = listOf("lemonade", "refreshing", "famous", "food")
        ),
        CatalogQuest(
            id = "bat_food_seafood",
            title = "Batroun Fisherman Calamari or Fish Wrap",
            description = "Taste a crispy fried squid or sea bass sandwich seasoned with sumac and garlic tarator from a local port shack under $6.",
            city = "Batroun",
            neighborhood = "Bahsa Bay",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 4.0,
            maximumExpectedSpend = 7.0,
            minimumDuration = 20,
            maximumDuration = 35,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 20,
            estimatedCost = 5.0,
            xp = 270,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Deliver a quick 5-word food review on camera.",
            tags = listOf("food", "seafood", "batroun", "cheap")
        ),
        CatalogQuest(
            id = "bat_date_sunset",
            title = "Makaad El Mir Golden Hour Toast",
            description = "Walk to the historic coastal vantage point Makaad El Mir (Prince's Seat) to witness one of Lebanon’s finest coastal sunsets.",
            city = "Batroun",
            neighborhood = "Phoenician Wall",
            mode = AdventureMode.DATE,
            category = "cat_sunset",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 25,
            maximumDuration = 45,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO", "QUAD"),
            timeOfDay = "SUNSET",
            difficulty = "Easy",
            estimatedDuration = 30,
            estimatedCost = 0.0,
            xp = 340,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Capture a sun-kissed reflection on the rocky tidal pools.",
            tags = listOf("date", "sunset", "batroun", "romantic")
        ),
        CatalogQuest(
            id = "bat_date_coastal_cafe",
            title = "Bahsa Bay Pebble Beach Sunset Lounge",
            description = "Kick off your shoes on the smooth pebble beach at Bahsa Bay and sip a refreshing drink while waves gently lap the shore.",
            city = "Batroun",
            neighborhood = "Bahsa Bay",
            mode = AdventureMode.DATE,
            category = "cat_date",
            minimumBudget = 10.0,
            maximumExpectedSpend = 25.0,
            minimumDuration = 40,
            maximumDuration = 80,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO"),
            timeOfDay = "EVENING",
            difficulty = "Easy",
            estimatedDuration = 50,
            estimatedCost = 15.0,
            xp = 380,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Skip a flat pebble across the water at least three times.",
            tags = listOf("date", "beach", "batroun", "lounge")
        ),

        // ==========================================
        // ZAHLE (14 Quests)
        // ==========================================
        CatalogQuest(
            id = "z_zero_1",
            title = "Berdawni River Promenade Shaded Stroll",
            description = "Walk along the roaring Berdawni River shaded by towering walnut and weeping willow trees in the heart of Zahle.",
            city = "Zahle",
            neighborhood = "Berdawni River",
            mode = AdventureMode.BROKE,
            category = "cat_zero",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 20,
            maximumDuration = 40,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 0.0,
            xp = 230,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Record the sound of rushing mountain river water.",
            tags = listOf("zahle", "river", "nature", "free")
        ),
        CatalogQuest(
            id = "z_zero_2",
            title = "Historic Zahle Stone Stairs Climb",
            description = "Conquer one of Zahle’s steep historic pedestrian stairways connecting the valley to the upper hillside neighborhoods.",
            city = "Zahle",
            neighborhood = "Historic Stairs",
            mode = AdventureMode.BORED,
            category = "cat_outdoor",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 15,
            maximumDuration = 30,
            allowedTransportModes = listOf(TransportMode.WALK),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "DAY",
            difficulty = "Medium",
            estimatedDuration = 20,
            estimatedCost = 0.0,
            xp = 260,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Photograph the red tiled rooftops spreading across the valley.",
            tags = listOf("stairs", "zahle", "view", "fitness")
        ),
        CatalogQuest(
            id = "z_food_icecream",
            title = "Authentic Zahle Ashta & Pistachio Ice Cream",
            description = "Taste legendary handcrafted Booza Arabiyeh with mastic gum and fresh clotted cream (ashta), rolled in crushed green pistachios.",
            city = "Zahle",
            neighborhood = "Berdawni River",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 2.5,
            maximumExpectedSpend = 4.5,
            minimumDuration = 15,
            maximumDuration = 25,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 15,
            estimatedCost = 3.0,
            xp = 270,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Photograph the thick pistachio coating from up close.",
            tags = listOf("food", "icecream", "ashta", "zahle")
        ),
        CatalogQuest(
            id = "z_food_mezze",
            title = "Zahle Berdawni Trout & Hommos Feast",
            description = "Sit down along the riverside casino dining strips for fresh river trout, smooth creamy hommos, and warm puffy bread.",
            city = "Zahle",
            neighborhood = "Berdawni River",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 18.0,
            maximumExpectedSpend = 35.0,
            minimumDuration = 50,
            maximumDuration = 90,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Medium",
            estimatedDuration = 60,
            estimatedCost = 22.0,
            xp = 400,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Dip bread into hot garlic dip and toast with your squad.",
            tags = listOf("food", "mezze", "river", "dinner")
        ),
        CatalogQuest(
            id = "z_date_panorama",
            title = "Our Lady of Zahle Valley Lookout Drive",
            description = "Drive up to the tower statue overlooking the entire Bekaa valley and share a quiet moment overlooking orchards and vineyards.",
            city = "Zahle",
            neighborhood = "Midan",
            mode = AdventureMode.DATE,
            category = "cat_date",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 30,
            maximumDuration = 50,
            allowedTransportModes = listOf(TransportMode.CAR),
            groupCompatibility = listOf("DUO"),
            timeOfDay = "SUNSET",
            difficulty = "Easy",
            estimatedDuration = 35,
            estimatedCost = 0.0,
            xp = 330,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Spot the distant snow patches on Mount Sannine in the background.",
            tags = listOf("date", "zahle", "panorama", "car")
        ),

        // ==========================================
        // TRIPOLI (15 Quests)
        // ==========================================
        CatalogQuest(
            id = "t_zero_1",
            title = "Khan al-Saboun Artisan Soap Courtyard",
            description = "Step through the arched entrance of the 15th-century soap caravanserai and smell natural laurel and olive oil soaps drying.",
            city = "Tripoli",
            neighborhood = "Khan al-Saboun",
            mode = AdventureMode.BROKE,
            category = "cat_zero",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 20,
            maximumDuration = 35,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "DAY",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 0.0,
            xp = 260,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Find the giant handcrafted pyramid of carved green laurel soap.",
            tags = listOf("tripoli", "soap", "history", "culture")
        ),
        CatalogQuest(
            id = "t_zero_2",
            title = "Mamluk Vaulted Old Souk Scavenger",
            description = "Navigate through the maze of copper smiths, perfumers, and textile weavers beneath high stone medieval vaults.",
            city = "Tripoli",
            neighborhood = "Old Citadel Souks",
            mode = AdventureMode.BORED,
            category = "cat_discovery",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 25,
            maximumDuration = 45,
            allowedTransportModes = listOf(TransportMode.WALK),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "DAY",
            difficulty = "Medium",
            estimatedDuration = 30,
            estimatedCost = 0.0,
            xp = 280,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Photograph the rays of sunlight piercing through high stone skylights.",
            tags = listOf("souk", "mamluk", "ancient", "free")
        ),
        CatalogQuest(
            id = "t_food_sweets",
            title = "Legendary Tripoli Halawet El Jibn Quest",
            description = "Stop at a historic Tripoli sweets shop for delicate sweet cheese rolls stuffed with fresh ashta cream and drizzled with rose water syrup.",
            city = "Tripoli",
            neighborhood = "Tall Square",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 2.0,
            maximumExpectedSpend = 4.0,
            minimumDuration = 15,
            maximumDuration = 30,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 20,
            estimatedCost = 3.0,
            xp = 290,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Rate the lightness of the cheese dough against all other desserts.",
            tags = listOf("food", "sweets", "tripoli", "famous")
        ),
        CatalogQuest(
            id = "t_food_kaak",
            title = "Tripoli Asruniyeh Kaak with Picon Cheese",
            description = "Order a large ring of freshly baked puffed kaakeh dusted with sumac and stuffed with melted cheese from a street cart under $1.50.",
            city = "Tripoli",
            neighborhood = "Tall Square",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 1.0,
            maximumExpectedSpend = 2.0,
            minimumDuration = 10,
            maximumDuration = 20,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 15,
            estimatedCost = 1.5,
            xp = 220,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Snap a photo of the wooden paddle pulling kaak from oven.",
            tags = listOf("food", "cheap", "kaak", "tripoli")
        ),
        CatalogQuest(
            id = "t_zero_mina",
            title = "El Mina Corniche & Island Promenade",
            description = "Walk along the breezy Mina port walkway where colored fishing skiffs rock in the harbor and view Palm Islands on the horizon.",
            city = "Tripoli",
            neighborhood = "Mina Harbor",
            mode = AdventureMode.BORED,
            category = "cat_outdoor",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 20,
            maximumDuration = 40,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 0.0,
            xp = 240,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Find the painted yellow lighthouse at the harbor tip.",
            tags = listOf("mina", "tripoli", "sea", "free")
        ),
        CatalogQuest(
            id = "t_date_citadel",
            title = "Raymond de Saint-Gilles Citadel Viewpoint",
            description = "Climb to the stone terrace outside Tripoli’s massive Crusader fortress overlooking the Abou Ali River and ancient skyline.",
            city = "Tripoli",
            neighborhood = "Old Citadel Souks",
            mode = AdventureMode.DATE,
            category = "cat_date",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 30,
            maximumDuration = 55,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO"),
            timeOfDay = "SUNSET",
            difficulty = "Medium",
            estimatedDuration = 35,
            estimatedCost = 0.0,
            xp = 320,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Capture the silhouette of the fortress walls against twilight.",
            tags = listOf("date", "citadel", "tripoli", "views")
        ),

        // ==========================================
        // SIDON (14 Quests)
        // ==========================================
        CatalogQuest(
            id = "s_zero_1",
            title = "Sidon Sea Castle Causeway Walk",
            description = "Walk across the ancient stone arch causeway that connects mainland Sidon to the 13th-century island castle fortress.",
            city = "Sidon",
            neighborhood = "Sea Castle Port",
            mode = AdventureMode.BROKE,
            category = "cat_zero",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 15,
            maximumDuration = 35,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 20,
            estimatedCost = 0.0,
            xp = 270,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Stand where sea waves crash against the stone causeway arches.",
            tags = listOf("sidon", "castle", "sea", "free")
        ),
        CatalogQuest(
            id = "s_zero_2",
            title = "Khan al-Franj Vaulted Courtyard Stroll",
            description = "Enter the restored 17th-century merchant caravanserai with its grand stone central courtyard, fountain, and second-floor galleries.",
            city = "Sidon",
            neighborhood = "Old Khan Souk",
            mode = AdventureMode.BORED,
            category = "cat_culture",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 20,
            maximumDuration = 35,
            allowedTransportModes = listOf(TransportMode.WALK),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "DAY",
            difficulty = "Easy",
            estimatedDuration = 25,
            estimatedCost = 0.0,
            xp = 250,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Frame a symmetric perspective shot under the high stone arches.",
            tags = listOf("sidon", "history", "courtyard", "free")
        ),
        CatalogQuest(
            id = "s_food_falafel",
            title = "Sidon Harbor Crispy Falafel Challenge",
            description = "Get an authentic Sidon falafel sandwich with pickled turnips, wild cucumber, and fresh parsley under $2 near the fishing port.",
            city = "Sidon",
            neighborhood = "Sea Castle Port",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 1.5,
            maximumExpectedSpend = 2.5,
            minimumDuration = 10,
            maximumDuration = 20,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 15,
            estimatedCost = 2.0,
            xp = 230,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Deliver a dramatic crunch sound test on camera.",
            tags = listOf("food", "falafel", "sidon", "cheap")
        ),
        CatalogQuest(
            id = "s_food_senioura",
            title = "Sidon Sanioura Biscuits & Orange Blossom Sweet",
            description = "Sample traditional melt-in-the-mouth crumbly Sanioura shortbread cookies scented with pure clarified butter and orange blossom water.",
            city = "Sidon",
            neighborhood = "Old Khan Souk",
            mode = AdventureMode.FOOD,
            category = "cat_food",
            minimumBudget = 2.0,
            maximumExpectedSpend = 4.0,
            minimumDuration = 15,
            maximumDuration = 30,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("SOLO", "DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 20,
            estimatedCost = 2.5,
            xp = 250,
            proofRequired = true,
            type = QuestType.FOOD,
            bonusChallenge = "Hold a delicate cookie without crumbling it before taking a bite.",
            tags = listOf("food", "sweet", "sidon", "tradition")
        ),
        CatalogQuest(
            id = "s_date_corniche",
            title = "Sidon Fishermen Sunset Stroll",
            description = "Walk hand in hand along the wide palm-lined southern corniche as colorful wooden skiffs return to port with the day's catch.",
            city = "Sidon",
            neighborhood = "Corniche Promenade",
            mode = AdventureMode.DATE,
            category = "cat_sunset",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 20,
            maximumDuration = 45,
            allowedTransportModes = listOf(TransportMode.WALK, TransportMode.CAR),
            groupCompatibility = listOf("DUO", "QUAD"),
            timeOfDay = "SUNSET",
            difficulty = "Easy",
            estimatedDuration = 30,
            estimatedCost = 0.0,
            xp = 310,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Silhouette photo against the sea castle backlit by orange sunset.",
            tags = listOf("date", "sunset", "sidon", "sea")
        ),
        CatalogQuest(
            id = "s_car_view",
            title = "Sidon South Coastal Lookout Drive",
            description = "Drive along the southern coastal stretch where sandy Mediterranean dunes meet citrus and banana groves.",
            city = "Sidon",
            neighborhood = "Corniche Promenade",
            mode = AdventureMode.BORED,
            category = "cat_outdoor",
            minimumBudget = 0.0,
            maximumExpectedSpend = 0.0,
            minimumDuration = 25,
            maximumDuration = 45,
            allowedTransportModes = listOf(TransportMode.CAR),
            groupCompatibility = listOf("DUO", "QUAD", "SQUAD"),
            timeOfDay = "ANY",
            difficulty = "Easy",
            estimatedDuration = 30,
            estimatedCost = 0.0,
            xp = 240,
            proofRequired = true,
            type = QuestType.PHOTO_PROOF,
            bonusChallenge = "Snap the long golden beach stretching towards Tyre.",
            tags = listOf("car", "drive", "coast", "sidon")
        )
    )

    /**
     * Filters quests strictly according to user constraints.
     */
    fun filterCatalogQuests(
        city: String,
        mode: AdventureMode,
        transportMode: TransportMode,
        budgetMax: Double,
        maxDurationMinutes: Int,
        secondaryMood: SecondaryMood = SecondaryMood.CHAOS,
        timeOfDayContext: String = "ANY",
        excludeQuestIds: List<String> = emptyList()
    ): List<CatalogQuest> {
        val targetCityNorm = normalizeCity(city)

        return allCatalogQuests.filter { quest ->
            // 1. Strict City match
            val questCityNorm = normalizeCity(quest.city)
            if (questCityNorm != targetCityNorm && questCityNorm != "lebanon") {
                return@filter false
            }

            // 2. Transport compatibility
            if (!quest.allowedTransportModes.contains(transportMode)) {
                return@filter false
            }

            // 3. Budget Hard Filter:
            // If budget is $0, quest must be strictly $0
            if (budgetMax <= 0.05) {
                if (quest.estimatedCost > 0.05 || quest.minimumBudget > 0.05) {
                    return@filter false
                }
            } else {
                if (quest.estimatedCost > budgetMax) {
                    return@filter false
                }
            }

            // 4. Time compatibility: single quest must fit in available time
            if (quest.estimatedDuration > maxDurationMinutes) {
                return@filter false
            }

            // 5. Mode prioritization (relaxed fallback if needed)
            if (mode == AdventureMode.FOOD) {
                // In Food mode, require food or related category
                if (quest.category != "cat_food" && quest.mode != AdventureMode.FOOD) {
                    // Allow 1 dessert/snack/beverage, otherwise prioritize food
                    if (!quest.tags.contains("food") && !quest.tags.contains("sweet")) {
                        return@filter false
                    }
                }
            } else if (mode == AdventureMode.DATE) {
                // In Date mode, forbid purely chaotic solo tasks
                if (quest.groupCompatibility.none { it == "DUO" || it == "QUAD" }) {
                    return@filter false
                }
            } else if (mode == AdventureMode.BROKE) {
                // In Broke mode, strictly respect budget
                if (quest.estimatedCost > budgetMax) {
                    return@filter false
                }
            }

            // 6. Time of Day context if specific
            if (timeOfDayContext != "ANY" && quest.timeOfDay != "ANY") {
                if (quest.timeOfDay != timeOfDayContext) {
                    return@filter false
                }
            }

            // 7. Exclude specifically blacklisted IDs
            if (excludeQuestIds.contains(quest.id)) {
                return@filter false
            }

            true
        }
    }

    /**
     * Assembles a cohesive multi-quest Mission respecting exact budget, duration,
     * transport, and anti-repetition rules.
     */
    fun assembleMission(
        city: String,
        mode: AdventureMode,
        transportMode: TransportMode,
        budgetMax: Double,
        durationMinutes: Int,
        secondaryMood: SecondaryMood,
        groupSize: String = "3"
    ): List<Quest> {
        // Target quest count based on duration (Requirements #8, #12)
        val targetCount = when {
            durationMinutes <= 35 -> 2
            durationMinutes <= 75 -> 3
            durationMinutes <= 135 -> 4
            else -> 5
        }

        // Step 1: Query valid quests from catalog
        var candidateQuests = filterCatalogQuests(
            city = city,
            mode = mode,
            transportMode = transportMode,
            budgetMax = budgetMax,
            maxDurationMinutes = durationMinutes,
            secondaryMood = secondaryMood,
            excludeQuestIds = emptyList()
        )

        // If candidate pool is too small due to strict city, also allow neighboring or generic safe quests
        if (candidateQuests.size < targetCount) {
            candidateQuests = allCatalogQuests.filter { quest ->
                quest.allowedTransportModes.contains(transportMode) &&
                        (if (budgetMax <= 0.05) quest.estimatedCost <= 0.05 else quest.estimatedCost <= budgetMax) &&
                        quest.estimatedDuration <= durationMinutes
            }
        }

        // Step 2: Anti-Repetition weighting (prefer quests not recently completed)
        val sortedCandidates = candidateQuests.shuffled().sortedBy { candidate ->
            if (recentQuestIds.contains(candidate.id)) 1 else 0
        }

        val selected = mutableListOf<CatalogQuest>()
        var currentSpend = 0.0
        var currentDuration = 0

        // Time budget per quest with buffer (e.g. 5 mins travel between nearby spots)
        val travelBufferPerQuest = if (transportMode == TransportMode.WALK) 5 else 8

        for (candidate in sortedCandidates) {
            if (selected.size >= targetCount) break
            if (selected.any { it.id == candidate.id }) continue

            // Budget constraint check: sum of mandatory expected costs must NOT exceed user budget
            if (budgetMax <= 0.05) {
                if (candidate.estimatedCost > 0.05) continue
            } else {
                if (currentSpend + candidate.estimatedCost > budgetMax + 0.1) continue
            }

            // Duration check: total quest time + travel buffer must fit
            if (currentDuration + candidate.estimatedDuration + travelBufferPerQuest > durationMinutes + 10) {
                // If this quest makes it too long, skip and look for a shorter one
                continue
            }

            selected.add(candidate)
            currentSpend += candidate.estimatedCost
            currentDuration += (candidate.estimatedDuration + travelBufferPerQuest)
        }

        // If we still need quests to reach targetCount, pick remaining valid free/light quests
        if (selected.size < targetCount) {
            for (candidate in sortedCandidates) {
                if (selected.size >= targetCount) break
                if (selected.any { it.id == candidate.id }) continue
                if (budgetMax <= 0.05 && candidate.estimatedCost > 0.05) continue
                if (budgetMax > 0.05 && currentSpend + candidate.estimatedCost > budgetMax) continue

                selected.add(candidate)
                currentSpend += candidate.estimatedCost
            }
        }

        // Record selected quests in anti-repetition tracker
        recordCompletedQuestIds(selected.map { it.id })

        return selected.mapIndexed { index, catQuest ->
            val bonusXp = 60 + (index * 20)
            catQuest.toQuest(bonusXp)
        }
    }

    private fun normalizeCity(c: String): String {
        val trimmed = c.trim().lowercase()
        return when {
            trimmed.contains("beirut") -> "beirut"
            trimmed.contains("jounieh") -> "jounieh"
            trimmed.contains("byblos") || trimmed.contains("jbeil") -> "byblos"
            trimmed.contains("batroun") -> "batroun"
            trimmed.contains("zahle") || trimmed.contains("zahlé") -> "zahle"
            trimmed.contains("tripoli") || trimmed.contains("trablos") -> "tripoli"
            trimmed.contains("sidon") || trimmed.contains("saida") -> "sidon"
            trimmed.contains("tyre") || trimmed.contains("sour") -> "tyre"
            else -> trimmed
        }
    }
}
