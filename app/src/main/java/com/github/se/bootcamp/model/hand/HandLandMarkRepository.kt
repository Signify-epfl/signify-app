package com.github.se.bootcamp.model.hand

import android.content.Context
import androidx.camera.core.ImageProxy
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmark
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult


interface HandLandMarkRepository {
    // Initialization with success and failure handling
    fun init(
        context: Context,
        onSuccess: () -> Unit,
        onFailure: (e: Exception) -> Unit
    )

    // Method to retrieve hand landmark detection results
    fun getHandLandMarkerResult(
        onSuccess: (result: HandLandmarkerResult) -> Unit)

    // Method to process an image and return hand landmarks
    fun processImageProxy(
        imageProxy: ImageProxy,
        onSuccess: (result: HandLandmarkerResult) -> Unit,
        onFailure: (e: Exception) -> Unit
    )
    fun getHandLandmarker() : HandLandmarker
    fun getSolution() : String
    fun processImageProxyThrottled(imageProxy: ImageProxy, onSuccess: (result: HandLandmarkerResult) -> Unit, onFailure: (e: Exception) -> Unit)
}