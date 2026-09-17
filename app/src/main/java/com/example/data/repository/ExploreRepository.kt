package com.example.data.repository

import com.example.data.model.AdventureMode
import com.example.data.model.Quest
import com.example.data.model.SecondaryMood
import com.example.data.model.TransportMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ExploreCategory(
    val id: String,
    val title: String,
    val emoji: String,
    val description: String
)

data class CuratedAdventure(
    val id: String,
    val title: String,
    val subtitle: String,
    val city: String,
    val neighborhood: String,
    val categoryId: String,
    val budgetText: String,
    val durationText: String,
    val xpReward: Int,
    val mode: AdventureMode,
    val secondaryMood: SecondaryMood,
    val transportMode: TransportMode = TransportMode.WALK,
    val quests: List<Quest>
)

class ExploreRepository(
    val questCatalog: QuestCatalogRepository = QuestCatalogRepository()
) {

    val categories = listOf(
        ExploreCategory("cat_zero", "$0 Adventures", "💸", "Unapologetically free and surprisingly cinematic"),
        ExploreCategory("cat_food", "Food Runs", "🍔", "Crispy shawarma, melted knafeh, and legendary bites"),
        ExploreCategory("cat_date", "Date Ideas", "❤️", "Effortless charm, cozy corners, and high vibes"),
        ExploreCategory("cat_chaos", "Chaos Runs", "🔥", "Expect the unexpected. Slightly unhinged."),
        ExploreCategory("cat_quick", "30-Minute Sprints", "⚡", "Fast adrenaline hits before your next meeting"),
        ExploreCategory("cat_sunset", "Sunset Missions", "🌅", "Golden hour magic on Mediterranean rooftops"),
        ExploreCategory("cat_late", "Late Night", "🌙", "When the city truly wakes up (11 PM - 3 AM)"),
        ExploreCategory("cat_student", "Student Mode", "🎒", "Maximum fun on university campus budgets"),
        ExploreCategory("cat_trending", "Trending Nearby", "📈", "What squads in your area are doing right now")
    )

    private val allAdventures: List<CuratedAdventure> by lazy {
        listOf(
            // BEIRUT
            CuratedAdventure(
                id = "exp_b1",
                title = "$0 Beirut Alleyway Scavenger",
                subtitle = "Explore hidden Mar Mikhael staircases without spending a single LBP",
                city = "Beirut",
                neighborhood = "Mar Mikhael",
                categoryId = "cat_zero",
                budgetText = "$0",
                durationText = "45 min",
                xpReward = 360,
                mode = AdventureMode.BROKE,
                secondaryMood = SecondaryMood.CHAOS,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Beirut" && it.estimatedCost <= 0.05 }
                    .take(3)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_b2",
                title = "Hamra Street Snack & Corniche Drift",
                subtitle = "Crispy falafel, toasted sesame kaak, and Mediterranean sea breeze",
                city = "Beirut",
                neighborhood = "Hamra",
                categoryId = "cat_food",
                budgetText = "$5",
                durationText = "1 hour",
                xpReward = 420,
                mode = AdventureMode.FOOD,
                secondaryMood = SecondaryMood.CHILL,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Beirut" && it.category == "cat_food" }
                    .take(3)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_b3",
                title = "Hidden Balcony Romantic Walk",
                subtitle = "A cozy sunset stroll through Gemmayzeh's vintage french-mandate architecture",
                city = "Beirut",
                neighborhood = "Gemmayzeh",
                categoryId = "cat_date",
                budgetText = "$15",
                durationText = "1.5 hours",
                xpReward = 480,
                mode = AdventureMode.DATE,
                secondaryMood = SecondaryMood.CHILL,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Beirut" && (it.mode == AdventureMode.DATE || it.category == "cat_sunset") }
                    .take(3)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_b4",
                title = "Manara Coastal Panoramic Drive",
                subtitle = "Drive past the historic pink lighthouse as golden hour hits the Mediterranean",
                city = "Beirut",
                neighborhood = "Ain El Mreisseh",
                categoryId = "cat_quick",
                budgetText = "$0",
                durationText = "30 min",
                xpReward = 300,
                mode = AdventureMode.BORED,
                secondaryMood = SecondaryMood.ADVENTURE,
                transportMode = TransportMode.CAR,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Beirut" && it.allowedTransportModes.contains(TransportMode.CAR) }
                    .take(2)
                    .map { it.toQuest() }
            ),

            // JOUNIEH
            CuratedAdventure(
                id = "exp_j1",
                title = "Old Souk Seaside Secrets",
                subtitle = "Wander through stone-arched souks overlooking the azure bay",
                city = "Jounieh",
                neighborhood = "Old Souk",
                categoryId = "cat_zero",
                budgetText = "$0",
                durationText = "45 min",
                xpReward = 380,
                mode = AdventureMode.BROKE,
                secondaryMood = SecondaryMood.CHILL,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Jounieh" && it.estimatedCost <= 0.05 }
                    .take(2)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_j2",
                title = "Kaslik Bay Snack Safari",
                subtitle = "Artisanal gelato, fisherman wraps, and late evening bay views",
                city = "Jounieh",
                neighborhood = "Kaslik",
                categoryId = "cat_food",
                budgetText = "$7",
                durationText = "1 hour",
                xpReward = 410,
                mode = AdventureMode.FOOD,
                secondaryMood = SecondaryMood.ADVENTURE,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Jounieh" && it.category == "cat_food" }
                    .take(2)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_j3",
                title = "Harissa Hillside Overlook Drive",
                subtitle = "Scenic mountain hairpins overlooking the entire glowing crescent gulf",
                city = "Jounieh",
                neighborhood = "Teleferique Bay",
                categoryId = "cat_sunset",
                budgetText = "$0",
                durationText = "45 min",
                xpReward = 440,
                mode = AdventureMode.BORED,
                secondaryMood = SecondaryMood.ADVENTURE,
                transportMode = TransportMode.CAR,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Jounieh" && it.allowedTransportModes.contains(TransportMode.CAR) }
                    .take(2)
                    .map { it.toQuest() }
            ),

            // BYBLOS
            CuratedAdventure(
                id = "exp_by1",
                title = "Phoenician Port Sunset Conquest",
                subtitle = "Climb the ancient stone ramparts as the sky turns purple over the harbor",
                city = "Byblos",
                neighborhood = "Old Port",
                categoryId = "cat_sunset",
                budgetText = "$0",
                durationText = "1 hour",
                xpReward = 490,
                mode = AdventureMode.BORED,
                secondaryMood = SecondaryMood.ADVENTURE,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Byblos" && it.estimatedCost <= 0.05 }
                    .take(3)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_by2",
                title = "Roman Road Candlelit Date Walk",
                subtitle = "Cobblestone alleys, acoustic music, and sea breezes in the ancient souk",
                city = "Byblos",
                neighborhood = "Citadel Quarter",
                categoryId = "cat_date",
                budgetText = "$20",
                durationText = "1.5 hours",
                xpReward = 530,
                mode = AdventureMode.DATE,
                secondaryMood = SecondaryMood.CHILL,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Byblos" && (it.mode == AdventureMode.DATE || it.category == "cat_date") }
                    .take(3)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_by3",
                title = "Byblos Harbor Sesame Snack Challenge",
                subtitle = "Fresh sesame kaakeh, seaside towers, and ancient Roman ruins",
                city = "Byblos",
                neighborhood = "Roman Road Souk",
                categoryId = "cat_food",
                budgetText = "$5",
                durationText = "45 min",
                xpReward = 360,
                mode = AdventureMode.FOOD,
                secondaryMood = SecondaryMood.CHAOS,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Byblos" && it.category == "cat_food" }
                    .take(2)
                    .map { it.toQuest() }
            ),

            // BATROUN
            CuratedAdventure(
                id = "exp_ba1",
                title = "Lemonade & Phoenician Wall Sprint",
                subtitle = "Drink the legendary mint lemonade and touch the 2,500-year-old sea wall",
                city = "Batroun",
                neighborhood = "Phoenician Wall",
                categoryId = "cat_trending",
                budgetText = "$2",
                durationText = "45 min",
                xpReward = 450,
                mode = AdventureMode.FOOD,
                secondaryMood = SecondaryMood.CHAOS,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Batroun" }
                    .take(3)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_ba2",
                title = "Bahsa Beach Pebble Sunset Drift",
                subtitle = "Race to the smooth pebbles before the sun dips into the Mediterranean horizon",
                city = "Batroun",
                neighborhood = "Bahsa Bay",
                categoryId = "cat_sunset",
                budgetText = "$0",
                durationText = "30 min",
                xpReward = 390,
                mode = AdventureMode.BORED,
                secondaryMood = SecondaryMood.WILD_CARD,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Batroun" && it.estimatedCost <= 0.05 }
                    .take(2)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_ba3",
                title = "Old Souk Cobblestone Romantic Date",
                subtitle = "Makaad El Mir vantage point, sea breeze, and coastal candlelit courtyard",
                city = "Batroun",
                neighborhood = "Old Souk",
                categoryId = "cat_date",
                budgetText = "$15",
                durationText = "1.5 hours",
                xpReward = 510,
                mode = AdventureMode.DATE,
                secondaryMood = SecondaryMood.CHILL,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Batroun" && it.mode == AdventureMode.DATE }
                    .take(2)
                    .map { it.toQuest() }
            ),

            // ZAHLE
            CuratedAdventure(
                id = "exp_z1",
                title = "Berdawni Riverbank Booza Hunt",
                subtitle = "Walk along the roaring mountain river and taste authentic mastic ashta ice cream",
                city = "Zahle",
                neighborhood = "Berdawni River",
                categoryId = "cat_food",
                budgetText = "$5",
                durationText = "1 hour",
                xpReward = 400,
                mode = AdventureMode.FOOD,
                secondaryMood = SecondaryMood.CHILL,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Zahle" && it.category == "cat_food" }
                    .take(2)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_z2",
                title = "Historic Red-Tile Staircase Challenge",
                subtitle = "Climb the panoramic hills of Zahle overlooking the Bekaa valley floor",
                city = "Zahle",
                neighborhood = "Historic Stairs",
                categoryId = "cat_zero",
                budgetText = "$0",
                durationText = "45 min",
                xpReward = 440,
                mode = AdventureMode.BROKE,
                secondaryMood = SecondaryMood.CHAOS,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Zahle" && it.estimatedCost <= 0.05 }
                    .take(2)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_z3",
                title = "Our Lady of Zahle Valley Lookout Drive",
                subtitle = "Drive to the towering vantage point for sweeping Bekaa vineyard views",
                city = "Zahle",
                neighborhood = "Midan",
                categoryId = "cat_sunset",
                budgetText = "$0",
                durationText = "45 min",
                xpReward = 460,
                mode = AdventureMode.DATE,
                secondaryMood = SecondaryMood.CHILL,
                transportMode = TransportMode.CAR,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Zahle" && it.allowedTransportModes.contains(TransportMode.CAR) }
                    .take(2)
                    .map { it.toQuest() }
            ),

            // TRIPOLI
            CuratedAdventure(
                id = "exp_t1",
                title = "Khan al-Saboun & Mamluk Souk Scavenger",
                subtitle = "Lose yourself in 800-year-old covered stone alleys smelling of laurel soap",
                city = "Tripoli",
                neighborhood = "Old Citadel Souks",
                categoryId = "cat_zero",
                budgetText = "$0",
                durationText = "1 hour",
                xpReward = 520,
                mode = AdventureMode.BROKE,
                secondaryMood = SecondaryMood.MYSTERY,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Tripoli" && it.estimatedCost <= 0.05 }
                    .take(3)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_t2",
                title = "Legendary Tripoli Halawet El Jibn Quest",
                subtitle = "Sweet cheese rolls with fresh clotted cream and fragrant orange blossom syrup",
                city = "Tripoli",
                neighborhood = "Tall Square",
                categoryId = "cat_food",
                budgetText = "$3",
                durationText = "40 min",
                xpReward = 430,
                mode = AdventureMode.FOOD,
                secondaryMood = SecondaryMood.CHILL,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Tripoli" && it.category == "cat_food" }
                    .take(2)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_t3",
                title = "El Mina Harbor & Palm Island Breeze",
                subtitle = "Walk along the breezy port promenade where colorful fishing skiffs rock",
                city = "Tripoli",
                neighborhood = "Mina Harbor",
                categoryId = "cat_sunset",
                budgetText = "$0",
                durationText = "50 min",
                xpReward = 390,
                mode = AdventureMode.BORED,
                secondaryMood = SecondaryMood.CHILL,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Tripoli" && it.neighborhood == "Mina Harbor" }
                    .take(2)
                    .map { it.toQuest() }
            ),

            // SIDON (Saida)
            CuratedAdventure(
                id = "exp_s1",
                title = "Sea Castle Causeway Fortress Recon",
                subtitle = "Cross the ancient stone causeway into the wave-swept island Crusader fort",
                city = "Sidon",
                neighborhood = "Sea Castle Port",
                categoryId = "cat_zero",
                budgetText = "$0",
                durationText = "1 hour",
                xpReward = 430,
                mode = AdventureMode.BROKE,
                secondaryMood = SecondaryMood.ADVENTURE,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Sidon" && it.estimatedCost <= 0.05 }
                    .take(2)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_s2",
                title = "Old Souk Sanioura & Falafel Hunt",
                subtitle = "Taste legendary flaky sanioura cookies and crispy golden falafel balls",
                city = "Sidon",
                neighborhood = "Old Khan Souk",
                categoryId = "cat_food",
                budgetText = "$4",
                durationText = "50 min",
                xpReward = 410,
                mode = AdventureMode.FOOD,
                secondaryMood = SecondaryMood.CHAOS,
                transportMode = TransportMode.WALK,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Sidon" && it.category == "cat_food" }
                    .take(2)
                    .map { it.toQuest() }
            ),
            CuratedAdventure(
                id = "exp_s3",
                title = "Sidon Southern Coastal Drive",
                subtitle = "Drive along the sandy Mediterranean coast with distant views of ancient ruins",
                city = "Sidon",
                neighborhood = "Corniche Promenade",
                categoryId = "cat_sunset",
                budgetText = "$0",
                durationText = "40 min",
                xpReward = 380,
                mode = AdventureMode.BORED,
                secondaryMood = SecondaryMood.CHILL,
                transportMode = TransportMode.CAR,
                quests = questCatalog.allCatalogQuests
                    .filter { it.city == "Sidon" && it.allowedTransportModes.contains(TransportMode.CAR) }
                    .take(2)
                    .map { it.toQuest() }
            )
        )
    }

    private val _selectedCity = MutableStateFlow("Beirut")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    fun setSelectedCity(city: String) {
        _selectedCity.value = city
    }

    fun getAdventuresForCity(city: String): List<CuratedAdventure> {
        val filtered = allAdventures.filter { it.city.equals(city, ignoreCase = true) }
        return if (filtered.isNotEmpty()) filtered else allAdventures.filter { it.city == "Beirut" }
    }

    fun getAdventuresForCategory(city: String, categoryId: String): List<CuratedAdventure> {
        val cityMatches = allAdventures.filter { it.city.equals(city, ignoreCase = true) }
        val exactMatches = cityMatches.filter { it.categoryId == categoryId }
        if (exactMatches.isNotEmpty()) return exactMatches

        val allCategoryMatches = allAdventures.filter { it.categoryId == categoryId }
        if (allCategoryMatches.isNotEmpty()) return allCategoryMatches

        return emptyList()
    }

    /**
     * Multi-constraint filter for Explore section (Requirement #19)
     */
    fun getFilteredAdventures(
        city: String,
        categoryId: String?,
        budgetFilter: String? = null,
        durationFilter: String? = null,
        transportFilter: TransportMode? = null
    ): List<CuratedAdventure> {
        val baseCityList = getAdventuresForCity(city)

        return baseCityList.filter { adv ->
            // Category filter
            if (categoryId != null && adv.categoryId != categoryId) {
                return@filter false
            }

            // Budget filter ($0, Under $10, Under $25, Any)
            if (budgetFilter != null && budgetFilter != "All") {
                when (budgetFilter) {
                    "$0" -> if (adv.budgetText != "$0") return@filter false
                    "<$10" -> {
                        val num = adv.budgetText.replace("$", "").trim().toDoubleOrNull() ?: 0.0
                        if (num >= 10.0) return@filter false
                    }
                    "$10+" -> {
                        val num = adv.budgetText.replace("$", "").trim().toDoubleOrNull() ?: 0.0
                        if (num < 10.0) return@filter false
                    }
                }
            }

            // Transport filter
            if (transportFilter != null) {
                if (adv.transportMode != transportFilter) return@filter false
            }

            // Duration filter (30 min, 1 hr, 1.5 hr+)
            if (durationFilter != null && durationFilter != "All") {
                when (durationFilter) {
                    "30 min" -> if (!adv.durationText.contains("30") && !adv.durationText.contains("40") && !adv.durationText.contains("45")) return@filter false
                    "1 hr+" -> if (!adv.durationText.contains("hour") && !adv.durationText.contains("hr")) return@filter false
                }
            }

            true
        }
    }
}
