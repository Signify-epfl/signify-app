package com.github.se.signify.ui.screens

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.SignifyAppPreview
import com.github.se.signify.model.di.AppDependencyProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class MainActivityTest {
  @get:Rule val composeTestRule = createComposeRule()
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @Before
  fun setUp() {
    val context = mock(Context::class.java)

    composeTestRule.setContent {
      // Set the content with the mocked context
      SignifyAppPreview(context, AppDependencyProvider, false, {}, false, {})
    }
  }

  @Test
  fun startsAtWelcomeScreen() {
    composeTestRule.onNodeWithTag("WelcomeScreen").assertIsDisplayed()
  }
}
