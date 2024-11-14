package com.github.se.signify.ui.screens.home

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.signify.model.di.AppDependencyProvider
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

  private lateinit var mockNavigationActions: NavigationActions
  private lateinit var handLandMarkViewModel: HandLandMarkViewModel

  @Before
  fun setup() {
    val context = mock(Context::class.java)
    val handLandMarkImplementation = AppDependencyProvider.handLandMarkRepository()
    handLandMarkViewModel = HandLandMarkViewModel(handLandMarkImplementation, context)
    mockNavigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent {
      ExerciseScreenHard(
          navigationActions = mockNavigationActions, handLandMarkViewModel = handLandMarkViewModel)
    }
  }

  @Test
  fun exerciseScreenHard_displaysComponentsCorrectly() {

    composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    composeTestRule.onNodeWithTag("wordLayer").assertIsDisplayed()
    composeTestRule.onNodeWithTag("cameraPreview").assertIsDisplayed()
  }

  @Test
  fun imageIsDisplayed_ifImageExists() {

    composeTestRule.onNodeWithContentDescription("Sign image").assertIsDisplayed()
  }
}
