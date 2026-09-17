package com.example

import android.app.Application
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.WaynStorage
import com.example.data.model.AdventureMode
import com.example.data.model.AdventureRequest
import com.example.data.model.SecondaryMood
import com.example.ui.WaynApp
import com.example.ui.theme.WaynTheme
import com.example.viewmodel.Screen
import com.example.viewmodel.WaynViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PrimaryFlowRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var application: Application
    private lateinit var storage: WaynStorage
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        application = ApplicationProvider.getApplicationContext()
        storage = WaynStorage(application)
        // Mark onboarding completed so app starts cleanly at Home
        storage.setOnboardingCompleted(true)
        storage.saveActiveMission(null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testCompletePrimaryFlowInViewModel() = runTest(testDispatcher) {
        val viewModel = WaynViewModel(application)
        advanceUntilIdle()

        // 1. Home
        assertEquals(Screen.HOME, viewModel.currentScreen.value)
        assertNull(viewModel.activeMission.value)

        // 2. We're Bored
        viewModel.startAdventureSetup(AdventureMode.BORED)
        assertEquals(Screen.ADVENTURE_SETUP, viewModel.currentScreen.value)
        assertEquals(AdventureMode.BORED, viewModel.activeSetupMode.value)

        // 3. Setup -> 4. Mission Generation
        val setupRequest = AdventureRequest(
            location = "Beirut",
            groupSize = "Solo",
            budget = "$20",
            duration = "1 hour",
            mode = AdventureMode.BORED,
            secondaryMood = SecondaryMood.CHAOS,
            transportMode = com.example.data.model.TransportMode.WALK
        )
        viewModel.generateAndStartAdventure(setupRequest)
        advanceUntilIdle()

        // 5. Mission Start
        assertEquals(Screen.MISSION_GAMEPLAY, viewModel.currentScreen.value)
        val activeMission = viewModel.activeMission.value
        assertNotNull("Active mission should exist", activeMission)
        assertTrue("Mission should have at least 1 quest", activeMission!!.quests.isNotEmpty())
        assertEquals(0, activeMission.currentQuestIndex)
        assertFalse(activeMission.completed)
        assertEquals(com.example.data.model.TransportMode.WALK, activeMission.transportMode)
        assertEquals(20.0, activeMission.initialBudgetUsd, 0.01)

        // 8. Timer verification
        assertTrue("Live timer remaining millis must be positive", activeMission.remainingMillis() > 0)
        assertFalse("Mission must not be expired upon start", activeMission.isExpired())

        // 6 & 7. Quest Completion & Camera Proof & XP & Budget Tracking
        val initialXp = viewModel.userProfile.value.totalXp
        val quest1 = activeMission.currentQuest()
        assertNotNull("Current quest 1 should exist", quest1)

        val dummyPhotoProofUri = "content://media/external/images/media/42"
        viewModel.completeQuestWithProof(dummyPhotoProofUri, actualSpent = 5.0)
        advanceUntilIdle()

        // Verify XP awarded and recorded
        val updatedXp = viewModel.userProfile.value.totalXp
        assertTrue("XP should increase after completing quest with photo proof", updatedXp > initialXp)
        val afterQuest1Mission = viewModel.activeMission.value
        assertNotNull(afterQuest1Mission)
        assertEquals(1, afterQuest1Mission!!.currentQuestIndex)
        assertEquals(1, afterQuest1Mission.photosTaken)
        assertEquals(5.0, afterQuest1Mission.totalSpentUsd, 0.01)
        assertEquals(15.0, afterQuest1Mission.remainingBudgetUsd, 0.01)

        // 9. Leave Mission
        viewModel.navigateTo(Screen.HOME)
        advanceUntilIdle()
        assertEquals(Screen.HOME, viewModel.currentScreen.value)
        // Ensure mission is NOT lost when leaving
        assertNotNull("Mission should remain active when leaving to Home", viewModel.activeMission.value)
        assertFalse("Mission should still be in progress", viewModel.activeMission.value!!.completed)

        // 10. Resume Mission
        viewModel.navigateTo(Screen.MISSION_GAMEPLAY)
        advanceUntilIdle()
        assertEquals(Screen.MISSION_GAMEPLAY, viewModel.currentScreen.value)
        assertEquals(1, viewModel.activeMission.value?.currentQuestIndex)

        // 11. Complete remaining quests -> 12. Completion Recap
        val remainingQuestsCount = viewModel.activeMission.value!!.quests.size - viewModel.activeMission.value!!.currentQuestIndex
        for (i in 0 until remainingQuestsCount) {
            viewModel.completeQuestWithProof(null, actualSpent = 2.0)
            advanceUntilIdle()
        }

        // Verify transition to RECAP screen
        assertEquals(Screen.RECAP, viewModel.currentScreen.value)
        val completedMission = viewModel.lastCompletedMission.value
        assertNotNull("Completed mission must be available for recap", completedMission)
        assertTrue("Mission must be marked completed", completedMission!!.completed)
        assertTrue("Earned XP must be positive", completedMission.earnedXp() > 0)
        assertEquals(completedMission.quests.size, completedMission.completedQuestsCount())

        // Verify Logbook entry was created and persisted
        val logbook = viewModel.logbookEntries.value
        assertTrue("Logbook should record the completed mission", logbook.isNotEmpty())
        assertEquals(completedMission.title, logbook.first().missionName)
        assertEquals(completedMission.totalSpentUsd, logbook.first().actualTotalSpent, 0.01)
    }

    @Test
    fun testPrimaryFlowUiInteraction() = runTest(testDispatcher) {
        val viewModel = WaynViewModel(application)
        advanceUntilIdle()

        composeTestRule.setContent {
            WaynTheme {
                WaynApp(viewModel = viewModel)
            }
        }
        composeTestRule.waitForIdle()

        // 1. Verify Home Screen is loaded with "WE'RE BORED." CTA
        composeTestRule.onNodeWithTag("home_were_bored_cta").assertIsDisplayed()

        // 2. Tap "WE'RE BORED." CTA to navigate to Setup
        composeTestRule.onNodeWithTag("home_were_bored_cta").performClick()
        advanceUntilIdle()
        composeTestRule.waitForIdle()

        // 3. In Adventure Setup: Step 1 -> Next
        composeTestRule.onNodeWithTag("adventure_setup_next_button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("adventure_setup_next_button").performClick()
        composeTestRule.waitForIdle()

        // Step 2 -> Next
        composeTestRule.onNodeWithTag("adventure_setup_next_button").performClick()
        composeTestRule.waitForIdle()

        // Step 3 -> Next
        composeTestRule.onNodeWithTag("adventure_setup_next_button").performClick()
        composeTestRule.waitForIdle()

        // Step 4 (Transport Mode) -> Next
        composeTestRule.onNodeWithTag("adventure_setup_next_button").performClick()
        composeTestRule.waitForIdle()

        // Step 5 -> Generate Button
        composeTestRule.onNodeWithTag("adventure_setup_generate_button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("adventure_setup_generate_button").performClick()
        advanceUntilIdle()
        composeTestRule.waitForIdle()

        // 4 & 5. Mission Gameplay: Started
        composeTestRule.onNodeWithTag("mission_current_quest_card").assertIsDisplayed()
        composeTestRule.onNodeWithTag("mission_complete_quest_button").assertExists()
        composeTestRule.onNodeWithTag("mission_take_photo_button").assertExists()

        // 6. Leave Mission to Home
        composeTestRule.onNodeWithTag("mission_back_to_home").assertIsDisplayed()
        composeTestRule.onNodeWithTag("mission_back_to_home").performClick()
        advanceUntilIdle()
        composeTestRule.waitForIdle()

        // 7. On Home: Verify Resume Mission card is visible and clickable
        composeTestRule.onNodeWithTag("home_resume_mission_card").assertIsDisplayed()
        composeTestRule.onNodeWithTag("home_resume_mission_button").assertIsDisplayed()

        // 8. Resume Mission: Tap Resume
        composeTestRule.onNodeWithTag("home_resume_mission_button").performClick()
        advanceUntilIdle()
        composeTestRule.waitForIdle()

        // Back in Mission Gameplay
        composeTestRule.onNodeWithTag("mission_current_quest_card").assertIsDisplayed()
        composeTestRule.onNodeWithTag("mission_complete_quest_button").assertExists()
    }
}
