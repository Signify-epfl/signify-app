package com.github.se.signify.ui.screens.home

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.signify.model.home.hand.HandLandmarkConfig
import com.github.se.signify.model.home.hand.HandLandmarkImplementation
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class HandLandmarkImplementationTest {

  private lateinit var handLandMarkImplementation: HandLandmarkImplementation
  private lateinit var context: Context
  private val config =
      HandLandmarkConfig(
          "main/assets/hand_landmarker.task", "main/assets/RFC_model_ir9_opset19.onnx")

  @Before
  fun setUp() {
    context = mock(Context::class.java)
    handLandMarkImplementation = HandLandmarkImplementation(config)
  }

  @Test
  fun test_init_success() {
    handLandMarkImplementation.init(
        context, onSuccess = { assert(true) }, onFailure = { assert(false) })
  }

  @Test
  fun test_init_failure() {
    val invalidConfig = HandLandmarkConfig("invalid_task", "invalid_model")
    val invalidHandLandmarkImplementation = HandLandmarkImplementation(invalidConfig)
    invalidHandLandmarkImplementation.init(context, { assert(false) }, { assert(true) })
  }

  @Test
  fun test_processImageProxyThrottled_success() {
    // Create a Bitmap to use in the test
    val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    val imageProxy = mock(ImageProxy::class.java)
    `when`(imageProxy.toBitmap()).thenReturn(bitmap)
    Log.e("test_processImageProxy_success", "imageProxy: $imageProxy")

    // Call the function under test
    handLandMarkImplementation.processImageProxyThrottled(
        imageProxy, onSuccess = { assert(true) }, onFailure = { assert(false) })
  }

  @Test
  fun test_setSolutionIsCalledAndgestureOutputAndCorrect() {
    handLandMarkImplementation.setSolution("A")
    assertEquals("A", handLandMarkImplementation.gestureOutput())
  }
}
