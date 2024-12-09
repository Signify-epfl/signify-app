package com.github.se.signify.ui.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.se.signify.R
import com.github.se.signify.model.getIconResId
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.CameraBox
import com.github.se.signify.ui.common.TextButton
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

/**
 * Composable that handles ASL recognition. It checks for camera permissions, launches the camera
 * preview, and displays recognized gestures and images.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ASLRecognition(
    handLandMarkViewModel: HandLandMarkViewModel,
    navigationActions: NavigationActions
) {
  val buttonUriString = stringResource(id = R.string.button_uri_string)
  val context = LocalContext.current
  AnnexScreenScaffold(navigationActions = navigationActions, testTag = "ASLRecognitionScreen") {
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(252.dp)
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.background)) {
          CameraBox(handLandMarkViewModel, "cameraPreview")
        }

    Spacer(modifier = Modifier.height(30.dp))

    GestureOverlayView(handLandMarkViewModel)

    Spacer(modifier = Modifier.height(20.dp))

    // Button: "More on ASL Alphabet"
    val moreOnASLAlphabetText = stringResource(id = R.string.more_on_asl_alphabet_text)
    TextButton(
        onClick = {
          val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(buttonUriString) }
          context.startActivity(intent)
        },
        testTag = "practiceButton",
        text = moreOnASLAlphabetText,
        backgroundColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onPrimary)
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
  val imageResource = if (gesture.isEmpty()) R.drawable.vector else getIconResId(gesture.first())
  Box(
      modifier =
          Modifier.width(336.dp)
              .height(200.dp)
              .clip(RoundedCornerShape(size = 10.dp))
              .background(MaterialTheme.colorScheme.primary)
              .border(
                  width = 2.dp,
                  color = MaterialTheme.colorScheme.primary,
                  shape = RoundedCornerShape(size = 10.dp))
              .testTag("handGestureImage")) {
        Icon(
            painter = painterResource(id = imageResource),
            contentDescription = "Letter gesture",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(200.dp).padding(16.dp).align(Alignment.Center))
      }
}

/** Draws the output text or the prompt to "Make a sign" on the screen. */
@Composable
fun DrawnOutPut(landmarks: List<NormalizedLandmark>?, text: String) {
  val makeASignText = stringResource(id = R.string.make_a_sign_text)
  val displayText = if (landmarks.isNullOrEmpty()) makeASignText else text
  val paintColor = MaterialTheme.colorScheme.background
  Box(
      modifier =
          Modifier.width(336.dp)
              .height(70.dp)
              .clip(RoundedCornerShape(10.dp)) // Rounds corners to match button style
              .background(MaterialTheme.colorScheme.primary)
              .border(
                  width = 2.dp,
                  color = MaterialTheme.colorScheme.primary,
                  shape = RoundedCornerShape(10.dp))
              .testTag("gestureOverlayView"),
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
