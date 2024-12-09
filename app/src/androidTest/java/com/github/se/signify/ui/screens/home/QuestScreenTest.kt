package com.github.se.signify.ui.screens.home

import android.content.Context
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.github.se.signify.R
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.model.quest.Quest
import com.github.se.signify.model.quest.QuestRepository
import com.github.se.signify.model.user.UserRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class QuestScreenTest {

  private lateinit var userSession: UserSession
  private lateinit var questRepository: QuestRepository
  private lateinit var userRepository: UserRepository
  private lateinit var navigationActions: NavigationActions

  val context: Context = ApplicationProvider.getApplicationContext()
  val quest_title: String = context.getString(R.string.quest_screen_title)
  val open_button: String = context.getString(R.string.open_quest_button)
  val closed_button: String = context.getString(R.string.closed_quest_button)

  private val sampleQuest =
      Quest(
          index = "1",
          title = "Sample Quest",
          description = "This is a sample quest description",
          videoPath = "")

  @get:Rule val composeTestRule = createComposeRule()

  @Before
  fun setUp() {
    userSession = MockUserSession()
    questRepository = mock(QuestRepository::class.java)
    userRepository = mock(UserRepository::class.java)
    navigationActions = mock(NavigationActions::class.java)

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
      )
    }

    composeTestRule.onNodeWithTag("QuestScreen").assertIsDisplayed()
  }

  @Test
  fun questBoxDisplaysCorrectInformation() {
    composeTestRule.setContent { QuestBox(quest = sampleQuest, isUnlocked = true) }

    composeTestRule.onNodeWithTag("QuestCard").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestHeader").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestActionButton").assertIsDisplayed()
  }

  @Test
  fun questActionButtonHasClickAction() {
    composeTestRule.setContent { QuestBox(quest = sampleQuest, isUnlocked = true) }

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
      )
    }

    // Check that the back button is displayed
    composeTestRule.onNodeWithContentDescription("BackButton").assertIsDisplayed()

    // Check that the title is displayed
    composeTestRule.onNodeWithText(quest_title).assertIsDisplayed()
  }

  @Test
  fun questBox_displaysCorrectInformationForUnlockedState() {
    composeTestRule.setContent { QuestBox(quest = sampleQuest, isUnlocked = true) }

    // Assert that the QuestBox is displayed with the correct content
    composeTestRule.onNodeWithTag("QuestCard").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestHeader").assertIsDisplayed()
    composeTestRule.onNodeWithText(open_button).assertIsDisplayed()
  }

  @Test
  fun questBox_displaysLockedButtonWhenLocked() {
    composeTestRule.setContent { QuestBox(quest = sampleQuest, isUnlocked = false) }

    // Assert that the button shows "Locked" when the quest is not unlocked
    composeTestRule.onNodeWithText(closed_button).assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestActionButton").assertIsNotEnabled()
  }

  @Test
  fun questBox_opensDialogWhenButtonClickedIfUnlocked() {
    composeTestRule.setContent { QuestBox(quest = sampleQuest, isUnlocked = true) }

    // Click the button to open the dialog
    composeTestRule.onNodeWithTag("QuestActionButton").performClick()

    // Check if dialog components are displayed
    composeTestRule.onNodeWithText(sampleQuest.title).assertIsDisplayed()
    composeTestRule.onNodeWithText(sampleQuest.description).assertIsDisplayed()
  }

  @Test
  fun questDescriptionDialog_displaysContentAndCloseButton() {
    composeTestRule.setContent { QuestDescriptionDialog(quest = sampleQuest, onDismiss = {}) }

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
      )
    }

    // Click the back button
    composeTestRule.onNodeWithContentDescription("BackButton").performClick()

    // Verify that navigationActions.goBack() was called
    verify(navigationActions).goBack()
  }

  @Test
  fun questBox_doesNotOpenDialogWhenLocked() {
    composeTestRule.setContent { QuestBox(quest = sampleQuest, isUnlocked = false) }

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
      QuestDescriptionDialog(quest = sampleQuest, onDismiss = { dismissCalled = true })
    }

    // Click the "Close" button
    composeTestRule.onNodeWithText("Close").performClick()

    // Verify that onDismiss was called
    assert(dismissCalled)
  }
}
