package com.github.se.signify.ui.screens.home

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.test.annotation.UiThreadTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.model.hand.HandLandMarkImplementation
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class ASLRecognitionTest : LifecycleOwner {

  @get:Rule val cameraAccess = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private lateinit var handLandMarkViewModel: HandLandMarkViewModel
  private lateinit var lifecycleRegistry: LifecycleRegistry

  @Before
  @UiThreadTest
  fun setup() {
    lifecycleRegistry = LifecycleRegistry(this)
    lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED

    lifecycleRegistry.currentState = Lifecycle.State.CREATED
    lifecycleRegistry.currentState = Lifecycle.State.STARTED

    val context = mock(Context::class.java)

    navigationActions = mock(NavigationActions::class.java)
    val handLandMarkImplementation =
        HandLandMarkImplementation("hand_landmarker.task", "RFC_model_ir9_opset19.onnx")
    handLandMarkViewModel = HandLandMarkViewModel(handLandMarkImplementation, context)

    `when`(navigationActions.currentRoute()).thenReturn(Screen.PRACTICE)

    composeTestRule.setContent {
      ASLRecognition(
          handLandMarkViewModel = handLandMarkViewModel, navigationActions = navigationActions)
    }
  }

  @Test
  fun allComponentsAreDisplayed() {
    composeTestRule.onNodeWithTag("practiceButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("cameraPreview").assertIsDisplayed()
    composeTestRule.onNodeWithTag("gestureOverlayView").assertIsDisplayed()
    composeTestRule.onNodeWithTag("aslRecognitionTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("bottomNavigationMenu").assertIsDisplayed()
  }

  override val lifecycle: Lifecycle
    get() = lifecycleRegistry
}
