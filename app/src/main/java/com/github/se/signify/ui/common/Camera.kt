package com.github.se.signify.ui.common

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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.se.signify.R
import com.github.se.signify.model.hand.HandLandMarkViewModel

/**
 * The camera preview box. This function requests camera permission and displays the camera preview
 * if permission is granted.
 *
 * @param handLandMarkViewModel The ViewModel responsible for managing hand landmark detection.
 */
@Composable
fun CameraBox(handLandMarkViewModel: HandLandMarkViewModel, testTag: String = "cameraPreview") {
  val context = LocalContext.current
  val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
  val previewView = remember { PreviewView(context) }
  var permissionGranted by remember { mutableStateOf(false) }
  // Permission launcher to request camera permission
  val cameraPermissionDeniedText = stringResource(R.string.camera_permission_denied_text)
  val permissionLauncher =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.RequestPermission(),
          onResult = { isGranted ->
            permissionGranted = isGranted
            if (!isGranted) {
              Toast.makeText(context, cameraPermissionDeniedText, Toast.LENGTH_SHORT).show()
            }
          })
  // Check for permission on load
  LaunchedEffect(Unit) {
    permissionGranted =
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
    if (!permissionGranted) {
      permissionLauncher.launch(Manifest.permission.CAMERA)
    }
  }
  if (permissionGranted) {
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(350.dp)
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
                .border(
                    2.dp, MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
                .testTag(testTag),
        contentAlignment = Alignment.Center) {
          AndroidView(
              factory = { previewView },
              modifier = Modifier.fillMaxWidth().fillMaxHeight().clip(RoundedCornerShape(16.dp)))
          // Set up the camera preview
          LaunchedEffect(Unit) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener(
                {
                  val cameraProvider = cameraProviderFuture.get()
                  val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                  val preview =
                      Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                      }
                  val imageAnalysis =
                      ImageAnalysis.Builder()
                          .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                          .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                          .build()
                  imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    handLandMarkViewModel.processImageProxyThrottled(imageProxy)
                  }
                  try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageAnalysis)
                  } catch (e: Exception) {
                    Log.e("CameraPlaceholder", "Camera binding failed", e)
                  }
                },
                ContextCompat.getMainExecutor(context))
          }
        }
  } else {
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .padding(16.dp)
                .height(350.dp)
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
                .border(
                    2.dp, MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
                .testTag(testTag),
        contentAlignment = Alignment.Center) {
          Text(
              stringResource(R.string.camera_permission_required_text),
              color = MaterialTheme.colorScheme.errorContainer)
        }
  }
}
