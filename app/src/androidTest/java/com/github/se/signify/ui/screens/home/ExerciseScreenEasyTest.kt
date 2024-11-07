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

@RunWith(AndroidJUnit4::class)
class ExerciseScreenEasyTest {

  @get:Rule val composeTestRule = createComposeRule()
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  private lateinit var mockNavigationActions: NavigationActions
  private lateinit var handLandMarkViewModel: HandLandMarkViewModel

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
  fun changeLetterSucces() {

    // Set the content with the ExerciseScreenEasy
    composeTestRule.setContent {
      ExerciseScreenEasy(
          navigationActions = mockNavigationActions, handLandMarkViewModel = handLandMarkViewModel)
    }
    val word = getFirstWord()
    val expectedLetter = word[0].toString()

    // Set the solution in the ViewModel
    handLandMarkViewModel.setSolution(expectedLetter)
    // Reset landmarks to default to trigger the event
    handLandMarkViewModel.resetLandmarksToDefault()

    // Assert that the current solution matches the expected letter
    val actualSolution = handLandMarkViewModel.getSolution()
    assert(expectedLetter == actualSolution) {
      "Expected solution ($expectedLetter) did not match actual solution ($actualSolution)"
    }
  }
}
