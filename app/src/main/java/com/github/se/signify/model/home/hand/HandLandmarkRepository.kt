package com.github.se.signify.model.home.hand

import android.content.Context
import androidx.camera.core.ImageProxy
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult

/**
 * Interface for the HandLandMarkRepository which handles the initialization and processing of hand
 * landmark detection using MediaPipe's HandLandmarker. This repository abstracts the logic for hand
 * landmark processing and gesture recognition, exposing methods to process images and obtain
 * results.
 */
interface HandLandmarkRepository {

  /**
   * Initializes the HandLandmarker with the provided context. Handles success and failure callbacks
   * during the initialization process.
   *
   * @param context The application context used for initialization.
   * @param onSuccess Callback to be invoked when the initialization is successful.
   * @param onFailure Callback to be invoked when the initialization fails with an exception.
   */
  fun init(context: Context, onSuccess: () -> Unit, onFailure: (e: Exception) -> Unit)

  /**
   * Retrieves the result from the HandLandmarker, which contains the detected hand landmarks. The
   * result is provided via the onSuccess callback.
   *
   * @param onSuccess Callback that receives the result from the hand landmark detection.
   */
  fun getHandLandMarkerResult(onSuccess: (result: HandLandmarkerResult) -> Unit)

  /**
   * Processes a single ImageProxy (camera frame) for hand landmark detection. Invokes the onSuccess
   * callback with the HandLandmarkerResult if successful, or onFailure with an exception if an
   * error occurs during processing.
   *
   * @param imageProxy The camera frame to be processed.
   * @param onSuccess Callback invoked when the image is successfully processed.
   * @param onFailure Callback invoked when the processing fails with an exception.
   */
  fun processImageProxy(
      imageProxy: ImageProxy,
      onSuccess: (result: HandLandmarkerResult) -> Unit,
      onFailure: (e: Exception) -> Unit
  )

  /**
   * Returns the initialized HandLandmarker object used for hand landmark detection.
   *
   * @return The HandLandmarker object.
   */
  fun getHandLandmarker(): HandLandmarker

  /**
   * Retrieves the output of the gesture recognition process. This method returns the recognized
   * gesture as a string.
   *
   * @return A string representing the recognized hand gesture.
   */
  fun gestureOutput(): String

  /**
   * Processes an ImageProxy using a throttled mechanism to avoid redundant processing. The result
   * of the hand landmark detection is passed through the onSuccess callback. If an error occurs
   * during processing, onFailure is invoked with the exception.
   *
   * @param imageProxy The camera frame to be processed.
   * @param onSuccess Callback invoked when the image is successfully processed.
   * @param onFailure Callback invoked when the processing fails with an exception.
   */
  fun processImageProxyThrottled(
      imageProxy: ImageProxy,
      onSuccess: (result: HandLandmarkerResult) -> Unit,
      onFailure: (e: Exception) -> Unit
  )
  /**
   * Sets the solution (recognized gesture) output.
   *
   * @param solution The intended solution to be set.
   */
  fun setSolution(solution: String)
}
