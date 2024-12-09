package com.github.se.signify.ui.screens.home

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.test.annotation.UiThreadTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.model.dependencyInjection.AppDependencyProvider
import com.github.se.signify.model.home.hand.HandLandmarkViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class ASLRecognitionTest : LifecycleOwner {

  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private lateinit var handLandMarkViewModel: HandLandmarkViewModel
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
    val handLandMarkImplementation = AppDependencyProvider.handLandMarkRepository()
    handLandMarkViewModel = HandLandmarkViewModel(handLandMarkImplementation, context)

    `when`(navigationActions.currentRoute()).thenReturn(Screen.PRACTICE.route)

    composeTestRule.setContent {
      ASLRecognition(
          handLandMarkViewModel = handLandMarkViewModel, navigationActions = navigationActions)
    }
  }

  @Test
  fun allComponentsAreDisplayed() {
    // Modify this to scroll to each component
    composeTestRule.onNodeWithTag("practiceButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("cameraPreview").assertIsDisplayed()
    composeTestRule.onNodeWithTag("gestureOverlayView").assertIsDisplayed()
  }

  @Test
  fun buttonIsWorkingAsIntended() {
    composeTestRule.onNodeWithTag("practiceButton").performClick()
  }

  override val lifecycle: Lifecycle
    get() = lifecycleRegistry
}
