package com.github.se.signify.ui.screens.home

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.model.hand.HandLandMarkImplementation
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions

@RunWith(AndroidJUnit4::class)
class ExerciseScreenEasyTest {

  @get:Rule val composeTestRule = createComposeRule()
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  private lateinit var mockNavigationActions: NavigationActions
  private lateinit var handLandMarkViewModel: HandLandMarkViewModel
  private lateinit var mockOnNextLetter: (Int) -> Unit
  private lateinit var mockOnNextWord: (Int) -> Unit
  private lateinit var mockOnAllWordsComplete: () -> Unit

  @Before
  fun setup() {
    val context = mock(Context::class.java)
    val handLandMarkImplementation =
        HandLandMarkImplementation("hand_landmarker.task", "RFC_model_ir9_opset19.onnx")
    handLandMarkViewModel = HandLandMarkViewModel(handLandMarkImplementation, context)
    mockNavigationActions = mock(NavigationActions::class.java)
  }

  @Test
  fun exerciseScreenEasy_displaysComponentsCorrectly() {
    composeTestRule.setContent {
      ExerciseScreenEasy(
          navigationActions = mockNavigationActions, handLandMarkViewModel = handLandMarkViewModel)
    }
    // Verify if the back button is displayed
    composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    composeTestRule.onNodeWithTag("wordLayer").assertIsDisplayed()
    composeTestRule.onNodeWithTag("cameraPreview").assertIsDisplayed()
  }

  @Test
  fun imageIsDisplayed_ifImageExists() {
    composeTestRule.setContent {
      ExerciseScreenEasy(
          navigationActions = mockNavigationActions, handLandMarkViewModel = handLandMarkViewModel)
    }

    composeTestRule.onNodeWithContentDescription("Sign image").assertIsDisplayed()
  }

  @Test
  fun handleGestureMatchingTest() {
    mockOnNextLetter = mock<(Int) -> Unit>()
    mockOnNextWord = mock<(Int) -> Unit>()
    mockOnAllWordsComplete = mock<() -> Unit>()
    val detectedGesture = "A"
    val currentLetter = 'A'
    val currentLetterIndex = 0
    val currentWordIndex = 0
    val wordsList = listOf("apple")

    handleGestureMatching(
        detectedGesture = detectedGesture,
        currentLetter = currentLetter,
        currentLetterIndex = currentLetterIndex,
        currentWordIndex = currentWordIndex,
        wordsList = wordsList,
        onNextLetter = mockOnNextLetter,
        onNextWord = mockOnNextWord,
        onAllWordsComplete = mockOnAllWordsComplete)

    verify(mockOnNextLetter).invoke(currentLetterIndex + 1)
    verifyNoInteractions(mockOnNextWord)
    verifyNoInteractions(mockOnAllWordsComplete)
  }
}
