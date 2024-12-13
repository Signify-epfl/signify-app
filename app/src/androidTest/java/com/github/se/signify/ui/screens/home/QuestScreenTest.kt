package com.github.se.signify.ui.screens.home

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.R
import com.github.se.signify.model.authentication.MockUserSession
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.dependencyInjection.AppDependencyProvider
import com.github.se.signify.model.home.hand.HandLandmarkViewModel
import com.github.se.signify.model.home.quest.Quest
import com.github.se.signify.model.home.quest.QuestRepository
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.model.profile.stats.StatsRepository
import com.github.se.signify.model.profile.stats.StatsViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`

class QuestScreenTest {

  private lateinit var userSession: UserSession
  private lateinit var questRepository: QuestRepository
  private lateinit var userRepository: UserRepository
  private lateinit var statsRepository: StatsRepository
  private lateinit var navigationActions: NavigationActions
  private lateinit var handLandmarkViewModel: HandLandmarkViewModel
  private lateinit var userViewModel: UserViewModel
  private lateinit var statsViewModel: StatsViewModel

  val context: Context = ApplicationProvider.getApplicationContext()
  val quest_title: String = context.getString(R.string.quest_screen_title_text)
  val open_button: String = context.getString(R.string.open_quest_button_text)
  val closed_button: String = context.getString(R.string.closed_quest_button_text)
  val close_dialog: String = context.getString(R.string.close_text)
  val fingerspell_the_word_hello: String =
      context.getString(R.string.fingerspell_title_text, "hello")
  val use_your_cam_for_hello: String = context.getString(R.string.try_fingerspell_text, "hello")

  private val sampleQuest =
      Quest(
          index = "1",
          title = "Sample Quest",
          description = "This is a sample quest description",
          videoPath = "")

  @get:Rule val composeTestRule = createComposeRule()
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @Before
  fun setUp() {
    val context = mock(Context::class.java)
    val handLandMarkImplementation = AppDependencyProvider.handLandMarkRepository()
    userSession = MockUserSession()
    questRepository = mock(QuestRepository::class.java)
    userRepository = mock(UserRepository::class.java)
    statsRepository = mock(StatsRepository::class.java)
    navigationActions = mock(NavigationActions::class.java)
    handLandmarkViewModel = HandLandmarkViewModel(handLandMarkImplementation, context)
    userViewModel = UserViewModel(userSession, userRepository)
    statsViewModel = StatsViewModel(userSession, statsRepository)

    `when`(navigationActions.currentRoute()).thenReturn(Screen.QUEST.route)
  }

  @Test
  fun hasRequiredComponent() {
    composeTestRule.setContent {
      QuestScreen(
          userSession = userSession,
          navigationActions = navigationActions,
          userRepository = userRepository,
          questRepository = questRepository,
          handLandMarkViewModel = handLandmarkViewModel,
          statsRepository = statsRepository)
    }

    composeTestRule.onNodeWithTag("QuestScreen").assertIsDisplayed()
  }

  @Test
  fun questBoxDisplaysCorrectInformation() {
    composeTestRule.setContent {
      QuestBox(
          quest = sampleQuest,
          isUnlocked = true,
          handLandMarkViewModel = handLandmarkViewModel,
          userViewModel = userViewModel,
          statsViewModel = statsViewModel)
    }

    composeTestRule.onNodeWithTag("QuestCard").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestHeader").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestActionButton").assertIsDisplayed()
  }

  @Test
  fun questActionButtonHasClickAction() {
    composeTestRule.setContent {
      QuestBox(
          quest = sampleQuest,
          isUnlocked = true,
          handLandMarkViewModel = handLandmarkViewModel,
          userViewModel = userViewModel,
          statsViewModel = statsViewModel)
    }

    composeTestRule.onNodeWithTag("QuestActionButton").assertHasClickAction()
  }

  @Test
  fun questScreen_displaysBackButtonAndTitle() {
    composeTestRule.setContent {
      QuestScreen(
          userSession = userSession,
          navigationActions = navigationActions,
          questRepository = questRepository,
          userRepository = userRepository,
          handLandMarkViewModel = handLandmarkViewModel,
          statsRepository = statsRepository)
    }

    // Check that the back button is displayed
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()

    // Check that the title is displayed
    composeTestRule.onNodeWithText(quest_title).assertIsDisplayed()
  }

  @Test
  fun questBox_displaysCorrectInformationForUnlockedState() {
    composeTestRule.setContent {
      QuestBox(
          quest = sampleQuest,
          isUnlocked = true,
          handLandMarkViewModel = handLandmarkViewModel,
          userViewModel = userViewModel,
          statsViewModel = statsViewModel)
    }

    // Assert that the QuestBox is displayed with the correct content
    composeTestRule.onNodeWithTag("QuestCard").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestHeader").assertIsDisplayed()
    composeTestRule.onNodeWithText(open_button).assertIsDisplayed()
  }

  @Test
  fun questBox_displaysLockedButtonWhenLocked() {
    composeTestRule.setContent {
      QuestBox(
          quest = sampleQuest,
          isUnlocked = false,
          handLandMarkViewModel = handLandmarkViewModel,
          userViewModel = userViewModel,
          statsViewModel = statsViewModel)
    }

    // Assert that the button shows "Locked" when the quest is not unlocked
    composeTestRule.onNodeWithText(closed_button).assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestActionButton").assertIsNotEnabled()
  }

  @Test
  fun questBox_opensDialogWhenButtonClickedIfUnlocked() {
    composeTestRule.setContent {
      QuestBox(
          quest = sampleQuest,
          isUnlocked = true,
          handLandMarkViewModel = handLandmarkViewModel,
          userViewModel = userViewModel,
          statsViewModel = statsViewModel)
    }

    // Click the button to open the dialog
    composeTestRule.onNodeWithTag("QuestActionButton").performClick()

    // Check if dialog components are displayed
    composeTestRule.onNodeWithText(sampleQuest.title).assertIsDisplayed()
    composeTestRule.onNodeWithText(sampleQuest.description).assertIsDisplayed()
  }

  @Test
  fun questDescriptionDialog_displaysContentAndCloseButton() {
    composeTestRule.setContent {
      QuestDescriptionDialog(
          quest = sampleQuest,
          onDismiss = {},
          handLandMarkViewModel = handLandmarkViewModel,
          userViewModel = userViewModel,
          statsViewModel = statsViewModel)
    }

    // Assert dialog title and description are displayed
    composeTestRule.onNodeWithText(sampleQuest.title + " in ASL sign language").assertIsDisplayed()
    composeTestRule.onNodeWithText(sampleQuest.description).assertIsDisplayed()

    // Check the "Close" button
    composeTestRule.onNodeWithText("Close").assertIsDisplayed().assertHasClickAction()
  }

  @Test
  fun questScreen_backButtonNavigatesBack() {
    composeTestRule.setContent {
      QuestScreen(
          userSession = userSession,
          navigationActions = navigationActions,
          questRepository = questRepository,
          userRepository = userRepository,
          handLandMarkViewModel = handLandmarkViewModel,
          statsRepository = statsRepository)
    }

    // Click the back button
    composeTestRule.onNodeWithTag("BackButton").performClick()

    // Verify that navigationActions.goBack() was called
    verify(navigationActions).goBack()
  }

  @Test
  fun questBox_doesNotOpenDialogWhenLocked() {
    composeTestRule.setContent {
      QuestBox(
          quest = sampleQuest,
          isUnlocked = false,
          handLandMarkViewModel = handLandmarkViewModel,
          userViewModel = userViewModel,
          statsViewModel = statsViewModel)
    }

    // Try to click the button when it's locked
    composeTestRule.onNodeWithTag("QuestActionButton").performClick()

    // Verify that the dialog does not open
    composeTestRule
        .onNodeWithText("Quest: Learn about letter ${sampleQuest.title}")
        .assertDoesNotExist()
  }

  @Test
  fun questDescriptionDialog_callsOnDismissWhenClosed() {
    var dismissCalled = false

    composeTestRule.setContent {
      QuestDescriptionDialog(
          quest = sampleQuest,
          onDismiss = { dismissCalled = true },
          handLandMarkViewModel = handLandmarkViewModel,
          userViewModel = userViewModel,
          statsViewModel = statsViewModel)
    }

    // Click the "Close" button
    composeTestRule.onNodeWithText(close_dialog).performClick()

    // Verify that onDismiss was called
    assert(dismissCalled)
  }

  @Test
  fun handleGestureMatchingForWordTest() {
    // Mocks for callback functions
    val mockOnProgressUpdate = mock<(Int) -> Unit>()
    val mockOnWordComplete = mock<() -> Unit>()

    // Test inputs
    val detectedGesture = "A"
    val currentLetterIndex = 0
    val word = "APPLE"

    handleGestureMatchingForWord(
        detectedGesture = detectedGesture,
        currentLetterIndex = currentLetterIndex,
        word = word,
        onProgressUpdate = mockOnProgressUpdate,
        onWordComplete = mockOnWordComplete)

    // Verify that onProgressUpdate is called with the next letter index
    verify(mockOnProgressUpdate).invoke(currentLetterIndex + 1)

    // Ensure onWordComplete is not invoked, as we're only moving to the next letter
    verifyNoInteractions(mockOnWordComplete)
  }

  @Test
  fun handleGestureMatchingForWordTest_CompleteWord() {
    // Mocks for callback functions
    val mockOnProgressUpdate = mock<(Int) -> Unit>()
    val mockOnWordComplete = mock<() -> Unit>()

    // Test inputs
    val detectedGesture = "E"
    val currentLetterIndex = 4 // Last letter of "APPLE"
    val word = "APPLE"

    handleGestureMatchingForWord(
        detectedGesture = detectedGesture,
        currentLetterIndex = currentLetterIndex,
        word = word,
        onProgressUpdate = mockOnProgressUpdate,
        onWordComplete = mockOnWordComplete)

    // Verify that onWordComplete is called as the word is completed
    verify(mockOnWordComplete).invoke()

    // Ensure onProgressUpdate is not invoked, as the word is completed
    verifyNoInteractions(mockOnProgressUpdate)
  }

  @Test
  fun handleGestureMatchingForWordTest_WrongGesture() {
    // Mocks for callback functions
    val mockOnProgressUpdate = mock<(Int) -> Unit>()
    val mockOnWordComplete = mock<() -> Unit>()

    // Test inputs
    val detectedGesture = "B" // Wrong gesture
    val currentLetterIndex = 0
    val word = "APPLE"

    handleGestureMatchingForWord(
        detectedGesture = detectedGesture,
        currentLetterIndex = currentLetterIndex,
        word = word,
        onProgressUpdate = mockOnProgressUpdate,
        onWordComplete = mockOnWordComplete)

    // Ensure neither onProgressUpdate nor onWordComplete is invoked
    verifyNoInteractions(mockOnProgressUpdate)
    verifyNoInteractions(mockOnWordComplete)
  }

  @Test
  fun wordLayerIsCorrectlyDisplayed() {
    // Set up test input
    val testWord = "APPLE"
    val currentLetterIndex = 0

    // Launch the composable
    composeTestRule.setContent {
      WordLayer(word = testWord, currentLetterIndex = currentLetterIndex)
    }

    // Assert the Box with the "sentenceLayer" tag exists
    composeTestRule.onNodeWithTag("sentenceLayer").assertExists()

    // Assert the Text with the "CurrentWordTag" tag exists
    composeTestRule.onNodeWithTag("CurrentWordTag").assertExists()
  }

  @Test
  fun fingerSpellDialog_displaysCorrectTitleAndText() {
    composeTestRule.setContent {
      FingerSpellDialog(
          word = "hello",
          onDismiss = {},
          handLandMarkViewModel = handLandmarkViewModel,
          userViewModel = userViewModel,
          "1",
          statsViewModel = statsViewModel)
    }

    // Check if the dialog title is displayed
    composeTestRule.onNodeWithText(fingerspell_the_word_hello).assertIsDisplayed()

    // Check if the dialog instruction text is displayed
    composeTestRule.onNodeWithText(use_your_cam_for_hello).assertIsDisplayed()
  }

  @Test
  fun fingerSpellDialog_closeButtonClosesDialog() {
    var isDismissed = false
    composeTestRule.setContent {
      FingerSpellDialog(
          word = "test",
          onDismiss = { isDismissed = true },
          handLandMarkViewModel = handLandmarkViewModel,
          userViewModel = userViewModel,
          "1",
          statsViewModel = statsViewModel)
    }

    // Perform click on the "Close" button
    composeTestRule.onNodeWithText(close_dialog).performClick()

    // Verify that the dialog is dismissed
    assert(isDismissed)
  }
}
