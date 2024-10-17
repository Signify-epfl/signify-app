package com.github.se.signify.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import java.util.concurrent.Executors

@Composable
fun ASLRecognition(handLandMarkViewModel: HandLandMarkViewModel) {
  val context = LocalContext.current
  var permissionGranted by remember { mutableStateOf(false) }

  // Checking camera permission
  if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
      PackageManager.PERMISSION_GRANTED) {
    permissionGranted = true
  } else {
    // Request camera permission
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted
          ->
          permissionGranted = isGranted
          if (!isGranted) {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
          }
        }

    LaunchedEffect(Unit) { permissionLauncher.launch(Manifest.permission.CAMERA) }
  }

  if (permissionGranted) {
    CameraPreviewView()
    LandMarkView(handLandMarkViewModel)
  }
}

@Composable
fun CameraPreviewView() {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val previewView = remember { PreviewView(context) }
  AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
  LaunchedEffect(Unit) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener(
        {
          val cameraProvider = cameraProviderFuture.get()

          // Select back camera as default
          val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

          // Setup preview use case
          val preview =
              Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
          try {
            // Bind camera lifecycle to the preview
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
          } catch (e: Exception) {
            e.printStackTrace()
          }
        },
        ContextCompat.getMainExecutor(context))
  }
}

@Composable
fun LandMarkView(handLandMarkViewModel: HandLandMarkViewModel) {
  val landmarksState = handLandMarkViewModel.landMarks().collectAsState()
  Log.d("HandLandMarkViewModel", "LandMarkView ${landmarksState.value?.get(0)}")
  // Camera preview with image analysis
  CameraPreviewWithAnalysisView(handLandMarkViewModel)

  // Draw the hand landmarks
  DrawHandLandmarks(landmarks = landmarksState.value, handLandMarkViewModel.getSolution())
}

@Composable
fun CameraPreviewWithAnalysisView(handLandMarkViewModel: HandLandMarkViewModel) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val previewView = remember { PreviewView(context) }
  AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

  LaunchedEffect(Unit) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener(
        {
          val cameraProvider = cameraProviderFuture.get()

          val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

          val preview =
              Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }

          val imageAnalysis =
              ImageAnalysis.Builder()
                  .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                  .build()

          imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
            handLandMarkViewModel.processImageProxyThrottled(imageProxy)
          }

          try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
          } catch (e: Exception) {
            e.printStackTrace()
          }
        },
        ContextCompat.getMainExecutor(context))
  }
}

@Composable
fun DrawHandLandmarks(landmarks: List<NormalizedLandmark>?, text: String) {
  if (landmarks == null || landmarks.isEmpty()) return

  Canvas(modifier = Modifier.fillMaxSize()) {
    // Define the connections between landmarks (adjust based on hand model)
    val connections =
        listOf(
            listOf(0, 1, 2, 3, 4), // Thumb
            listOf(0, 5, 6, 7, 8), // Index Finger
            listOf(0, 9, 10, 11, 12), // Middle Finger
            listOf(0, 13, 14, 15, 16), // Ring Finger
            listOf(0, 17, 18, 19, 20) // Pinky
            )

    // Scaling factors to map landmarks to screen size
    val widthScale = size.width
    val heightScale = size.height

    // Custom styles for the landmark points and connections
    val pointRadius = 10f // Radius of the points
    val pointColor = Color.Cyan // Color for the points
    val lineColor = Color.White // Color for the lines
    val lineWidth = 5f // Line thickness

    // Iterate over the connection lists and draw lines between the connected points
    connections.forEach { connection ->
      val path = Path()
      connection.forEachIndexed { index, pointIndex ->
        val landmark = landmarks[pointIndex]
        val x = landmark.y() * widthScale
        val y = landmark.x() * heightScale

        if (index == 0) {
          path.moveTo(x, y)
        } else {
          path.lineTo(x, y)
        }

        // Draw a circle for each landmark point
        drawCircle(
            color = pointColor,
            radius = pointRadius,
            center = androidx.compose.ui.geometry.Offset(x, y))
      }
      // Draw the path for the connected landmarks
      drawPath(
          path = path,
          color = lineColor, // Line color
          style =
              Stroke(
                  width = lineWidth, // Line thickness
                  cap = StrokeCap.Round,
                  join = StrokeJoin.Round))
    }

    val fontSize = 60f // Make the font size large
    val textColor = Color.Magenta // Set a fancy color for the text
    val textX = size.width * 0.5f // X coordinate at the center
    val textY = size.height * 0.1f // Y coordinate near the top

    drawContext.canvas.nativeCanvas.apply {
      // Set up the paint for the text
      val paint =
          android.graphics.Paint().apply {
            this.color = textColor.toArgb()
            this.textSize = fontSize
            this.isAntiAlias = true
            this.setShadowLayer(
                10f, 0f, 0f, android.graphics.Color.BLACK) // Adding a shadow for a fancier look
            this.textAlign = android.graphics.Paint.Align.CENTER
          }
      drawText(text, textX, textY, paint)
    }
  }
}
