package com.github.se.signify.ui

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.se.signify.R
import com.github.se.signify.model.hand.HandLandMarkViewModel

/**
 * A reusable composable function that creates an outlined button with an icon.
 *
 * @param onClickAction A lambda function to execute when the button is clicked.
 * @param icon The ImageVector representing the icon to be displayed in the button.
 * @param iconDescription A string description of the icon, used for accessibility and testing.
 */
@Composable
fun ReusableButtonWithIcon(onClickAction: () -> Unit, icon: ImageVector, iconDescription: String) {
  OutlinedButton(
      onClick = onClickAction,
      modifier = Modifier.padding(8.dp).testTag(iconDescription + "Button"),
      border =
          ButtonDefaults.outlinedButtonBorder.copy(
              width = 2.dp, brush = SolidColor(colorResource(R.color.dark_gray))),
      colors = ButtonDefaults.outlinedButtonColors(colorResource(R.color.blue)),
  ) {
    Icon(icon, tint = colorResource(R.color.dark_gray), contentDescription = iconDescription)
  }
}

/**
 * A reusable composable function that creates an outlined button with customizable text.
 *
 * @param onClickAction A lambda function to execute when the button is clicked.
 * @param testTag A string used for testing, which serves as the tag for the button.
 * @param text The text to be displayed inside the button.
 * @param backgroundColor The background color of the button.
 */
@Composable
fun ReusableTextButton(
    onClickAction: () -> Unit,
    testTag: String,
    text: String,
    backgroundColor: Color,
) {
  OutlinedButton(
      onClick = onClickAction,
      border =
          ButtonDefaults.outlinedButtonBorder.copy(
              width = 2.dp, brush = SolidColor(colorResource(R.color.black))),
      colors = ButtonDefaults.buttonColors(backgroundColor),
      modifier = Modifier.fillMaxWidth().height(40.dp).testTag(testTag)) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.black),
            fontSize = 20.sp,
            textAlign = TextAlign.Center)
      }
}

@Composable
fun SquareButton(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    size: Dp,
    iconSize: Dp, // Icon size passed here
    labelFontSize: TextUnit,
    iconTint: Color,
    textColor: Color,
    iconOffset: Dp = 0.dp,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
  Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier =
          modifier
              .size(size) // Button size
              .border(2.dp, colorResource(R.color.black), RoundedCornerShape(12.dp)) // Black border
              .background(colorResource(R.color.blue), RoundedCornerShape(12.dp)) // Blue inside
              .padding(16.dp)
              .clickable { onClick() }) {

        // Text on top of the icon
        Text(
            text = label,
            fontSize = labelFontSize,
            color = textColor,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Icon in the center of the square
        Box(contentAlignment = Alignment.Center) {
          Icon(
              painter = painterResource(id = iconRes),
              contentDescription = label,
              tint = iconTint, // Set icon tint to dark gray
              modifier =
                  Modifier.size(iconSize) // Applying the icon size
                      .offset(x = iconOffset) // Use the offset if specified
              )
        }
      }
}

@Composable
fun UtilButton(
    onClick: () -> Unit,
    testTagButton: String,
    testTagIcon: String,
    icon: ImageVector,
    contentDescription: String
) {
  Box(
      modifier =
          Modifier.size(30.dp)
              .background(colorResource(R.color.white), shape = RoundedCornerShape(30))
              .border(2.dp, colorResource(R.color.white), shape = RoundedCornerShape(30))
              .clickable { onClick() }
              .testTag(testTagButton),
      contentAlignment = Alignment.Center) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = colorResource(R.color.blue),
            modifier = Modifier.size(30.dp).testTag(testTagIcon))
      }
}

@Composable
fun BackButton(onClick: () -> Unit) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      horizontalArrangement = Arrangement.Start) {
        IconButton(onClick = { onClick() }, modifier = Modifier.testTag("BackButton")) {
          Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "BackButton",
              tint = colorResource(R.color.blue))
        }
      }
}

fun getLetterIconResId(letter: Char): Int {
  return when (letter) {
    'a' -> R.drawable.letter_a
    'b' -> R.drawable.letter_b
    'c' -> R.drawable.letter_c
    'd' -> R.drawable.letter_d
    'e' -> R.drawable.letter_e
    'f' -> R.drawable.letter_f
    'g' -> R.drawable.letter_g
    'h' -> R.drawable.letter_h
    'i' -> R.drawable.letter_i
    'j' -> R.drawable.letter_j
    'k' -> R.drawable.letter_k
    'l' -> R.drawable.letter_l
    'm' -> R.drawable.letter_m
    'n' -> R.drawable.letter_n
    'o' -> R.drawable.letter_o
    'p' -> R.drawable.letter_p
    'q' -> R.drawable.letter_q
    'r' -> R.drawable.letter_r
    's' -> R.drawable.letter_s
    't' -> R.drawable.letter_t
    'u' -> R.drawable.letter_u
    'v' -> R.drawable.letter_v
    'w' -> R.drawable.letter_w
    'x' -> R.drawable.letter_x
    'y' -> R.drawable.letter_y
    'z' -> R.drawable.letter_z
    else -> R.drawable.letter_a // Default case, just in case an unexpected value is passed
  }
}
/**
 * Composable function that serves as a placeholder for the camera preview. This function requests
 * camera permission and displays the camera preview if permission is granted.
 *
 * @param handLandMarkViewModel The ViewModel responsible for managing hand landmark detection.
 * @param modifier Modifier to be applied to the camera placeholder.
 */
@Composable
fun CameraPlaceholder(handLandMarkViewModel: HandLandMarkViewModel, modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val previewView = remember { PreviewView(context) }
  var permissionGranted by remember { mutableStateOf(false) }

  // Permission launcher to request camera permission
  val permissionLauncher =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.RequestPermission(),
          onResult = { isGranted ->
            permissionGranted = isGranted
            if (!isGranted) {
              Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
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
            modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(350.dp)
                .background(colorResource(R.color.blue), shape = RoundedCornerShape(16.dp))
                .border(2.dp, colorResource(R.color.white), shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center) {
          AndroidView(
              factory = { previewView },
              modifier =
                  Modifier.fillMaxWidth()
                      .fillMaxHeight()
                      .clip(RoundedCornerShape(16.dp))
                      .testTag("cameraPreview"))

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
            modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(350.dp)
                .background(colorResource(R.color.black), shape = RoundedCornerShape(16.dp))
                .border(2.dp, colorResource(R.color.white), shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center) {
          Text("Camera permission required", color = colorResource(R.color.white))
        }
  }
}
