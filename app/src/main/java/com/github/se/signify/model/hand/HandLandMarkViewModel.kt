package com.github.se.signify.model.hand

import android.content.Context
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HandLandMarkViewModel(private val handLandMarkRepository: HandLandMarkRepository, context: Context) : ViewModel() {
    // StateFlow to store the for one hand landmarks
    private val _handLandmarks = MutableStateFlow<List<NormalizedLandmark>?>(null)
    private val handLandmarks: StateFlow<List<NormalizedLandmark>?> = _handLandmarks
    
    // Initializing the repository
    init {
        handLandMarkRepository.init(
            context = context,
            onSuccess = {
                Log.d("HandLandMarkViewModel", "HandLandMarkRepository initialized")
            },
            onFailure = {
                Log.e("HandLandMarkViewModel", "HandLandMarkRepository initialization failed")
            }
        )
    }

    // Process the image and update hand landmarks
    fun processImageProxy(imageProxy: ImageProxy) {
        handLandMarkRepository.processImageProxy(
            imageProxy = imageProxy,
            onSuccess = { result ->
                viewModelScope.launch {
                    _handLandmarks.value = result.landmarks()[0] // First hand
                }
            },
            onFailure = { exception ->
                Log.e("HandLandMarkViewModel", "Error processing image", exception)
            }
        )
    }

    // Function to expose landmarks as a StateFlow
    fun landMarks(): StateFlow<List<NormalizedLandmark>?> = handLandmarks
    fun getHandLandmarker() : HandLandmarker = handLandMarkRepository.getHandLandmarker()
    fun processImageProxyThrottled(imageProxy: ImageProxy) {
        handLandMarkRepository.processImageProxyThrottled(
            imageProxy = imageProxy,
            onSuccess = { result ->
                viewModelScope.launch {
                    _handLandmarks.value = result.landmarks()[0] // First hand
                }
            },
            onFailure = { exception ->
                Log.e("HandLandMarkViewModel", "Error processing image", exception)
            }
        )
    }
    fun getSolution() : String = handLandMarkRepository.getSolution()
}