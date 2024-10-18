package com.github.se.signify.ui.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.se.signify.R
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.navigation.BottomNavigationMenu
import com.github.se.signify.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.ui.navigation.NavigationActions
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import java.util.concurrent.Executors

// Map to associate each letter with its corresponding drawable resource for ASL gestures
val gestureImageMap =
    mapOf(
        "A" to R.drawable.letter_a,
        "B" to R.drawable.letter_b,
        "C" to R.drawable.letter_c,
        "D" to R.drawable.letter_d,
        "E" to R.drawable.letter_e,
        "F" to R.drawable.letter_f,
        "G" to R.drawable.letter_g,
        "H" to R.drawable.letter_h,
        "I" to R.drawable.letter_i,
        "J" to R.drawable.letter_j,
        "K" to R.drawable.letter_k,
        "L" to R.drawable.letter_l,
        "M" to R.drawable.letter_m,
        "N" to R.drawable.letter_n,
        "O" to R.drawable.letter_o,
        "P" to R.drawable.letter_p,
        "Q" to R.drawable.letter_q,
        "R" to R.drawable.letter_r,
        "S" to R.drawable.letter_s,
        "T" to R.drawable.letter_t,
        "U" to R.drawable.letter_u,
        "V" to R.drawable.letter_v,
        "W" to R.drawable.letter_w,
        "X" to R.drawable.letter_x,
        "Y" to R.drawable.letter_y,
        "Z" to R.drawable.letter_z)
/**
 * Composable that handles ASL recognition. It checks for camera permissions, launches the camera
 * preview, and displays recognized gestures and images.
 */
@Composable
fun ASLRecognition(
    handLandMarkViewModel: HandLandMarkViewModel,
    navigationActions: NavigationActions
) {
  val context = LocalContext.current
  var permissionGranted by remember { mutableStateOf(false) }

  if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
      PackageManager.PERMISSION_GRANTED) {
    permissionGranted = true
  } else {
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
    Scaffold(
        topBar = {
          TopAppBar(
              title = { Text("Practice ASL & Test it !") },
              backgroundColor = Color.Transparent, // Transparent background
              contentColor = MaterialTheme.colors.onSurface,
              navigationIcon = {
                IconButton(onClick = { navigationActions.goBack() }) {
                  Icon(
                      imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                      contentDescription = "Back")
                }
              })
        },
        content = { paddingValues ->
          LazyColumn(
              modifier =
                  Modifier.background(colorResource(R.color.white))
                      .padding(paddingValues)
                      .padding(start = 40.dp, end = 40.dp),
              horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                  Box(
                      modifier =
                          Modifier.width(336.dp)
                              .height(252.dp)
                              .background(color = colorResource(R.color.white)),
                  ) {
                    CameraPreviewWithAnalysisView(handLandMarkViewModel)
                  }
                }

                item { Spacer(modifier = Modifier.height(30.dp)) }

                item { GestureOverlayView(handLandMarkViewModel) }

                item { Spacer(modifier = Modifier.height(20.dp)) }

                item {
                  // Button: "More on American Sign Language"
                  Button(
                      onClick = { /* For now, do nothing */},
                      colors =
                          ButtonDefaults.buttonColors(colorResource(R.color.blue)), // Blue color
                      modifier =
                          Modifier.width(336.dp) // Match the width of the box above
                              .height(50.dp)) {
                        Text(
                            text = "More on ASL Alphabet",
                            color = colorResource(R.color.white)) // One-line text with white color
                  }
                }
              }
        },
        bottomBar = {
          BottomNavigationMenu(
              onTabSelect = { route -> navigationActions.navigateTo(route) },
              tabList = LIST_TOP_LEVEL_DESTINATION,
              selectedItem = navigationActions.currentRoute())
        })
  }
}

/**
 * Composable that overlays gesture detection on the UI. Displays landmarks and the recognized hand
 * gesture image.
 */
@Composable
fun GestureOverlayView(handLandMarkViewModel: HandLandMarkViewModel) {
  val landmarksState = handLandMarkViewModel.landMarks().collectAsState()
  val detectedGesture = handLandMarkViewModel.getSolution()

  DrawnOutPut(landmarks = landmarksState.value, text = detectedGesture)
  Spacer(modifier = Modifier.height(30.dp))
  HandGestureImage(gesture = detectedGesture)
}
/** Displays the image associated with the detected ASL hand gesture. */
@Composable
fun HandGestureImage(gesture: String) {
  val imageResource = gestureImageMap[gesture] ?: R.drawable.vector
  Box(
      modifier =
          Modifier.border(
                  width = 3.dp,
                  color = colorResource(R.color.blue),
                  shape = RoundedCornerShape(size = 10.dp))
              .width(332.dp)
              .height(270.dp)
              .background(colorResource(R.color.blue))
              .padding(start = 116.dp, top = 85.dp, end = 116.dp, bottom = 85.dp)) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "Detected Gesture $gesture",
            modifier = Modifier.size(120.dp).padding(16.dp))
      }
}
/** Draws the output text or the prompt to "Make a sign" on the screen. */
@Composable
fun DrawnOutPut(landmarks: List<NormalizedLandmark>?, text: String) {
  val displayText =
      if (landmarks.isNullOrEmpty()) {
        "Make a sign"
      } else {
        text
      }

  val paintColor = colorResource(R.color.white)
  Box(
      modifier =
          Modifier.width(336.dp)
              .height(70.dp)
              .background(color = colorResource(R.color.blue))
              .padding(vertical = 9.dp),
      contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val fontSize = 80f
          val textX = size.width * 0.5f
          val textY = (size.height / 2) + (fontSize / 3)

          drawContext.canvas.nativeCanvas.apply {
            val paint =
                android.graphics.Paint().apply {
                  color = paintColor.toArgb()
                  textSize = fontSize
                  isAntiAlias = true
                  textAlign = android.graphics.Paint.Align.CENTER
                }
            drawText(displayText, textX, textY, paint)
          }
        }
      }
}

/**
 * Sets up the camera preview with image analysis for hand landmark detection. The camera is bound
 * to the lifecycle and processes images for analysis using the MediaPipe library.
 */
@SuppressLint("RestrictedApi")
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
              Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3).build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
              }

          val imageAnalysis =
              ImageAnalysis.Builder()
                  .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                  .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
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
