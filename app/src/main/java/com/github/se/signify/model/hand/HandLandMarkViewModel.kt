package com.github.se.signify.model.hand

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing hand landmark detection using the HandLandMarkRepository. This ViewModel
 * maintains the state of detected hand landmarks and provides functionality to process camera image
 * frames for hand recognition.
 *
 * @property handLandMarkRepository The repository responsible for processing images and providing
 *   hand landmark data.
 * @property context The application context used to initialize the repository.
 */
class HandLandMarkViewModel(
    private val handLandMarkRepository: HandLandMarkRepository,
    context: Context
) : ViewModel() {

  // Mutable state flow that holds the detected hand landmarks (or null if none detected)
  private val _handLandmarks = MutableStateFlow<List<NormalizedLandmark>?>(null)

  // Exposes the hand landmarks as an immutable state flow
  private val handLandmarks: StateFlow<List<NormalizedLandmark>?> = _handLandmarks

  // Initialization block to set up the HandLandMarkRepository with success/failure callbacks
  init {
    handLandMarkRepository.init(
        context = context,
        onSuccess = { Log.d("HandLandMarkViewModel", "HandLandMarkRepository initialized") },
        onFailure = {
          Log.e("HandLandMarkViewModel", "HandLandMarkRepository initialization failed")
        })
  }

  /**
   * Processes an ImageProxy (camera frame) for hand landmark detection using the repository's
   * throttled image processing method. Updates the landmarks state on success or logs an error on
   * failure.
   *
   * @param imageProxy The camera frame to be processed for hand landmarks.
   */
  fun processImageProxyThrottled(imageProxy: ImageProxy) {
    handLandMarkRepository.processImageProxy(
        imageProxy = imageProxy,
        onSuccess = { result ->
          viewModelScope.launch {
            _handLandmarks.value =
                result.landmarks().firstOrNull()
                    ?: emptyList() // Process the first hand's landmarks
          }
        },
        onFailure = { exception ->
          Log.e("HandLandMarkViewModel", "Error processing image", exception)
        })
  }

  /**
   * Returns the solution (predicted gesture) from the repository after processing.
   *
   * @return A string representing the recognized hand gesture.
   */
  fun getSolution(): String = handLandMarkRepository.gestureOutput()

  /**
   * Provides a StateFlow of the detected hand landmarks to be observed by the UI.
   *
   * @return A StateFlow containing a list of normalized landmarks or null if no landmarks detected.
   */
  fun landMarks(): StateFlow<List<NormalizedLandmark>?> = handLandmarks

  /** Factory class for creating instances of HandLandMarkViewModel. */
  companion object {
    fun provideFactory(
        context: Context,
        handLandMarkRepository: HandLandMarkRepository
    ): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
          @Suppress("UNCHECKED_CAST")
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HandLandMarkViewModel::class.java)) {
              return HandLandMarkViewModel(handLandMarkRepository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
          }
        }
  }
}
