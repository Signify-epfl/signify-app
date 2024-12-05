package com.github.se.signify.ui.screens.home

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.model.di.AppDependencyProvider
import com.github.se.signify.model.exercise.ExerciseLevel
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.model.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions

@RunWith(AndroidJUnit4::class)
class ExerciseScreenTest {

  @get:Rule val composeTestRule = createComposeRule()
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  private lateinit var mockNavigationActions: NavigationActions
  private lateinit var handLandMarkViewModel: HandLandMarkViewModel

  @Before
  fun setup() {
    val context = mock(Context::class.java)
    val handLandMarkImplementation = AppDependencyProvider.handLandMarkRepository()
    handLandMarkViewModel = HandLandMarkViewModel(handLandMarkImplementation, context)
    mockNavigationActions = mock(NavigationActions::class.java)
  }

  @Test
  fun exerciseScreenDisplaysComponentsCorrectly() {
    composeTestRule.setContent {
      ExerciseScreen(
          navigationActions = mockNavigationActions,
          handLandMarkViewModel = handLandMarkViewModel,
          exerciseLevel = ExerciseLevel.Easy)
    }
    composeTestRule.onNodeWithTag("sentenceLayer").assertIsDisplayed()
    composeTestRule.onNodeWithTag("cameraPreview").assertIsDisplayed()
  }

  @Test
  fun imageIsDisplayedIfImageExists() {
    composeTestRule.setContent {
      ExerciseScreen(
          navigationActions = mockNavigationActions,
          handLandMarkViewModel = handLandMarkViewModel,
          exerciseLevel = ExerciseLevel.Easy)
    }

    composeTestRule.onNodeWithContentDescription("Sign image").assertIsDisplayed()
  }

  @Test
  fun handleGestureMatchingTest() {
    // Mocks for callback functions
    val mockOnProgressUpdate = mock<(Int, Int, Int) -> Unit>()
    val mockOnAllSentencesComplete = mock<() -> Unit>()

    // Test inputs
    val detectedGesture = "A"
    val currentLetterIndex = 0
    val currentWordIndex = 0
    val currentSentenceIndex = 0
    val sentencesList = listOf("apple is good", "banana is better")

    handleGestureMatching(
        detectedGesture = detectedGesture,
        currentLetterIndex = currentLetterIndex,
        currentWordIndex = currentWordIndex,
        currentSentenceIndex = currentSentenceIndex,
        sentencesList = sentencesList,
        onProgressUpdate = mockOnProgressUpdate,
        onAllSentencesComplete = mockOnAllSentencesComplete)

    // Verify that onProgressUpdate is called with the next letter index
    verify(mockOnProgressUpdate)
        .invoke(currentLetterIndex + 1, currentWordIndex, currentSentenceIndex)

    // Ensure onAllSentencesComplete is not invoked, as we're only moving to the next letter
    verifyNoInteractions(mockOnAllSentencesComplete)
  }
}
