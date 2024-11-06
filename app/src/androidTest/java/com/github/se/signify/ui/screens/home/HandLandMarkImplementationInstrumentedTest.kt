package com.github.se.signify.ui.screens.home

import android.content.Context
import androidx.camera.core.ImageProxy
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.signify.model.hand.HandLandMarkImplementation
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class HandLandMarkImplementationInstrumentedTest {

  private lateinit var handLandMarkImplementation: HandLandMarkImplementation
  private lateinit var context: Context
  private val pathToTask = "main/assets/hand_landmarker.task"
  private val pathToModel = "main/assets/RFC_model_ir9_opset19.onnx"

  @Before
  fun setUp() {
    context = mock(Context::class.java)
    handLandMarkImplementation = HandLandMarkImplementation(pathToTask, pathToModel)
  }

  @Test
  fun test_init_success() {
    handLandMarkImplementation.init(
        context, onSuccess = { assert(true) }, onFailure = { assert(false) })
  }

  @Test
  fun test_init_failure() {
    val invalidPathToTask = "invalid_task"
    val invalidPathToModel = "invalid_model"
    val invalidHandLandMarkImplementation =
        HandLandMarkImplementation(invalidPathToTask, invalidPathToModel)
    invalidHandLandMarkImplementation.init(context, { assert(false) }, { assert(true) })
  }

  @Test
  fun test_processImageProxy_success() {
    val imageProxy = mock(ImageProxy::class.java)

    handLandMarkImplementation.init(
        context,
        onSuccess = {
          handLandMarkImplementation.processImageProxy(
              imageProxy, onSuccess = {}, onFailure = { assert(false) })
        },
        onFailure = { assert(false) })
  }

  @Test
  fun test_gestureOutput_success() {
    val imageProxy = mock(ImageProxy::class.java)

    handLandMarkImplementation.init(
        context,
        onSuccess = {
          handLandMarkImplementation.processImageProxy(
              imageProxy,
              onSuccess = {
                val gesture = handLandMarkImplementation.gestureOutput()
                assert(gesture.isNotEmpty())
              },
              onFailure = { assert(false) })
        },
        onFailure = { assert(false) })
  }
}
