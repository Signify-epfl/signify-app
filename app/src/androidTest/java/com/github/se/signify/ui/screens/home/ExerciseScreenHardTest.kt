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

@RunWith(AndroidJUnit4::class)
class ExerciseScreenHardTest {

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
    composeTestRule.setContent {
      ExerciseScreenHard(
          navigationActions = mockNavigationActions, handLandMarkViewModel = handLandMarkViewModel)
    }
  }

  @Test
  fun exerciseScreenHard_displaysComponentsCorrectly() {

    composeTestRule.onNodeWithTag("sentenceLayer").assertIsDisplayed()
    composeTestRule.onNodeWithTag("cameraPreview").assertIsDisplayed()
  }

  @Test
  fun imageIsDisplayed_ifImageExists() {

    composeTestRule.onNodeWithContentDescription("Sign image").assertIsDisplayed()
  }
}
