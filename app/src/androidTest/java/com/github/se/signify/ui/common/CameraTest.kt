package com.github.se.signify.ui.common

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.model.dependencyInjection.AppDependencyProvider
import com.github.se.signify.model.home.hand.HandLandmarkViewModel
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class CameraTest {
  @get:Rule val composeTestRule = createComposeRule()
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @Test
  fun cameraPreview_isDisplayed() {
    val context = mock(Context::class.java)
    val handLandMarkImplementation = AppDependencyProvider.handLandMarkRepository()
    val handLandmarkViewModel = HandLandmarkViewModel(handLandMarkImplementation, context)
    composeTestRule.setContent { CameraBox(handLandmarkViewModel, "cameraPreview") }
    composeTestRule.onNodeWithTag("cameraPreview").assertIsDisplayed()
  }
}
