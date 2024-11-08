package com.github.se.signify.model.hand

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker.HandLandmarkerOptions
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import java.io.File
import java.nio.FloatBuffer
import java.util.Collections
import java.util.concurrent.Executors

/**
 * Implementation of the HandLandMarkRepository interface using MediaPipe's HandLandmarker and ONNX
 * runtime for hand landmark detection and gesture recognition. This class handles the
 * initialization of the hand landmark detection model and the gesture classification using an ONNX
 * model for American Sign Language (ASL) recognition.
 *
 * @property pathToTask The file path to the MediaPipe hand detection model.
 * @property pathToModel The file path to the ONNX model used for gesture classification.
 */
class HandLandMarkImplementation(private val pathToTask: String, private val pathToModel: String) :
    HandLandMarkRepository {

  // HandLandmarker instance for detecting hand landmarks
  private var handLandmarker: HandLandmarker? = null

  // Stores the latest result from the HandLandmarker
  private var handLandMarkerResult: HandLandmarkerResult? = null

  // Path to the ONNX model used for gesture classification
  private lateinit var rfcModel: String

  // ONNX session used for running the classification model
  private lateinit var session: OrtSession

  // Time when the last frame was processed, used for throttling
  private var lastProcessedTime = 0L

  // Stores the solution (recognized gesture) output from the model
  private var solution = ""

  /**
   * Initializes the HandLandmarker and ONNX session with the provided context. Sets up the hand
   * detection model and gesture recognition model.
   *
   * @param context The application context used for initialization.
   * @param onSuccess Callback invoked when initialization is successful.
   * @param onFailure Callback invoked if initialization fails with an exception.
   */
  override fun init(context: Context, onSuccess: () -> Unit, onFailure: (e: Exception) -> Unit) {
    try {
      val baseOptions = BaseOptions.builder().setModelAssetPath(pathToTask).build()

      val options =
          HandLandmarkerOptions.builder()
              .setBaseOptions(baseOptions)
              .setMinHandDetectionConfidence(0.5f)
              .setNumHands(1)
              .setRunningMode(RunningMode.LIVE_STREAM)
              .setResultListener { result, _ ->
                handLandMarkerResult = result
                returnLivestreamResult(result)
              }
              .setErrorListener { returnLivestreamError(it) }
              .build()

      handLandmarker = HandLandmarker.createFromOptions(context, options)

      // Load the ONNX model from assets to a temporary file
      rfcModel =
          context.assets.open(pathToModel).use { inputStream ->
            val tempFile = File(context.cacheDir, pathToModel)
            inputStream.copyTo(tempFile.outputStream())
            tempFile.absolutePath
          }

      // Initialize the ONNX runtime environment and session
      val env = OrtEnvironment.getEnvironment()
      session = env.createSession(rfcModel)

      onSuccess()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  /**
   * Retrieves the latest hand landmarks result from the HandLandmarker.
   *
   * @param onSuccess Callback that receives the result of hand landmark detection.
   */
  override fun getHandLandMarkerResult(onSuccess: (result: HandLandmarkerResult) -> Unit) {
    onSuccess(handLandMarkerResult!!)
  }

  /**
   * Processes a camera frame (ImageProxy) to detect hand landmarks and recognize gestures. Converts
   * the image into a bitmap, processes it through the HandLandmarker, and updates the result. It
   * also runs gesture classification via the ONNX model.
   *
   * @param imageProxy The camera frame to process.
   * @param onSuccess Callback invoked when landmarks are successfully detected.
   * @param onFailure Callback invoked if the image processing fails.
   */
  override fun processImageProxy(
      imageProxy: ImageProxy,
      onSuccess: (result: HandLandmarkerResult) -> Unit,
      onFailure: (e: Exception) -> Unit
  ) {
    try {
      val bitmap = imageProxy.toBitmap()

      val frameTime = SystemClock.uptimeMillis()
      val mpImage = BitmapImageBuilder(bitmap).build()
      Executors.newSingleThreadExecutor().execute {
        handLandmarker?.detectAsync(mpImage, frameTime)

        if (handLandMarkerResult != null && handLandMarkerResult?.landmarks()?.size != 0) {
          onSuccess(handLandMarkerResult!!)
        } else {
          if (handLandMarkerResult != null) {
            onSuccess(handLandMarkerResult!!)
          }
        }
      }
    } catch (e: UnsupportedOperationException) {
      onFailure(e)
      Log.e("HandLandmarker", "Bitmap conversion failed")
    } catch (e: Exception) {
      onFailure(e)
      Log.e("HandLandmarker", "Error processing image proxy", e)
    } finally {
      imageProxy.close()
    }
  }

  /**
   * Handles errors that occur during the livestream hand landmark detection process.
   *
   * @param runtimeException The exception to be handled.
   */
  private fun returnLivestreamError(runtimeException: RuntimeException?) {
    runtimeException?.printStackTrace()
  }

  /**
   * Processes the result of hand landmark detection and runs the gesture classification model.
   * Converts the landmarks to the expected format for the ONNX model and retrieves the gesture.
   *
   * @param handLandmarkerResult The result from the HandLandmarker containing hand landmarks.
   */
  private fun returnLivestreamResult(
      handLandmarkerResult: HandLandmarkerResult?,
  ) {
    if (handLandmarkerResult == null || handLandmarkerResult.landmarks().isEmpty()) {
      return
    }

    val handednesses = handLandmarkerResult.handednesses()[0] // Assume the first hand
    Log.d("ModelOutput", "The handedness is: ${handednesses[0]}")
    val isRightHand =
        handednesses[0].categoryName() == "Left" // Mirror the hand for right-handed detection
    Log.d("ModelOutput", "Is right hand: $isRightHand")

    val landmarks = handLandmarkerResult.landmarks()[0] // Get the first hand's landmarks
    val dataFrame = mutableListOf<Float>()

    // Convert landmarks to the expected input format for ONNX model
    for (landmark in landmarks) {
      val rotatedX = if (isRightHand) 1.0f - landmark.y() else landmark.y()
      val rotatedY = landmark.x()
      dataFrame.add(rotatedX)
      dataFrame.add(rotatedY)
    }

    // Ensure the input size is always 84 floats (42 x, y pairs)
    while (dataFrame.size < 84) {
      dataFrame.add(0.0f)
    }

    // Create ONNX tensor input and run inference
    val floatArray = dataFrame.toFloatArray()
    val tensorInput =
        OnnxTensor.createTensor(
            OrtEnvironment.getEnvironment(), FloatBuffer.wrap(floatArray), longArrayOf(1, 84))

    val result = session.run(Collections.singletonMap("float_input", tensorInput))
    val x = result[0].value as Array<*>
    solution = x[0].toString()
    Log.d("ModelOutput", "The letter is: ${x[0]}")

    result.close()
    tensorInput.close()
  }

  /**
   * Returns the initialized HandLandmarker instance.
   *
   * @return The HandLandmarker object used for hand detection.
   */
  override fun getHandLandmarker(): HandLandmarker = handLandmarker!!

  /**
   * Processes the image proxy using a throttled mechanism, ensuring that frames are processed only
   * if a certain time interval (250ms) has passed since the last frame.
   *
   * @param imageProxy The camera frame to process.
   * @param onSuccess Callback invoked when landmarks are successfully detected.
   * @param onFailure Callback invoked if the image processing fails.
   */
  override fun processImageProxyThrottled(
      imageProxy: ImageProxy,
      onSuccess: (result: HandLandmarkerResult) -> Unit,
      onFailure: (e: Exception) -> Unit
  ) {
    val currentTime = SystemClock.uptimeMillis()
    if (currentTime - lastProcessedTime >= 250) {
      lastProcessedTime = currentTime
      processImageProxy(imageProxy, onSuccess, onFailure)
    } else {
      imageProxy.close()
    }
  }

  /**
   * Retrieves the latest gesture output (recognized ASL letter).
   *
   * @return A string representing the recognized gesture.
   */
  override fun gestureOutput(): String = solution

  /**
   * Sets the solution (recognized gesture) output.
   *
   * @param solution The intended solution to be set.
   */
  override fun setSolution(solution: String) {
    this.solution = solution
  }
}
