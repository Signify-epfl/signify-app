package com.github.se.signify.ui.screens.home

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.quest.Quest
import com.github.se.signify.model.quest.QuestRepository
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
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

  private val sampleQuest =
      Quest(index = "1", title = "Sample Quest", description = "This is a sample quest description")

  @get:Rule val composeTestRule = createComposeRule()

  @Before
  fun setUp() {
    userSession = MockUserSession()
    questRepository = mock(QuestRepository::class.java)
    userRepository = mock(UserRepository::class.java)
    navigationActions = mock(NavigationActions::class.java)

    `when`(navigationActions.currentRoute()).thenReturn(Screen.QUEST)
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
    composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()

    // Check that the title is displayed
    composeTestRule.onNodeWithText("Your daily quests").assertIsDisplayed()
  }

  @Test
  fun questBox_displaysCorrectInformationForUnlockedState() {
    composeTestRule.setContent { QuestBox(quest = sampleQuest, isUnlocked = true) }

    // Assert that the QuestBox is displayed with the correct content
    composeTestRule.onNodeWithTag("QuestCard").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestHeader").assertIsDisplayed()
    composeTestRule.onNodeWithText("Let’s Go!").assertIsDisplayed()
  }

  @Test
  fun questBox_displaysLockedButtonWhenLocked() {
    composeTestRule.setContent { QuestBox(quest = sampleQuest, isUnlocked = false) }

    // Assert that the button shows "Locked" when the quest is not unlocked
    composeTestRule.onNodeWithText("Locked").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestActionButton").assertIsNotEnabled()
  }

  @Test
  fun questBox_opensDialogWhenButtonClickedIfUnlocked() {
    composeTestRule.setContent { QuestBox(quest = sampleQuest, isUnlocked = true) }

    // Click the button to open the dialog
    composeTestRule.onNodeWithTag("QuestActionButton").performClick()

    // Check if dialog components are displayed
    composeTestRule
        .onNodeWithText("Quest: Learn about letter ${sampleQuest.title}")
        .assertIsDisplayed()
    composeTestRule.onNodeWithText(sampleQuest.description).assertIsDisplayed()
  }

  @Test
  fun questDescriptionDialog_displaysContentAndCloseButton() {
    composeTestRule.setContent { QuestDescriptionDialog(quest = sampleQuest, onDismiss = {}) }

    // Assert dialog title and description are displayed
    composeTestRule
        .onNodeWithText("Quest: Learn about letter ${sampleQuest.title}")
        .assertIsDisplayed()
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
    composeTestRule.onNodeWithContentDescription("Back").performClick()

    // Verify that navigationActions.goBack() was called
    verify(navigationActions).goBack()
  }

  @Test
  fun questDescriptionDialog_displaysImageForQuest() {
    composeTestRule.setContent { QuestDescriptionDialog(quest = sampleQuest, onDismiss = {}) }

    // Check if the Image for the quest is displayed
    composeTestRule
        .onNodeWithContentDescription("Image for letter ${sampleQuest.title}")
        .assertIsDisplayed()
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
