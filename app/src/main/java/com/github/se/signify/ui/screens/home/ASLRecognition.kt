package com.github.se.signify.ui.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.se.signify.R
import com.github.se.signify.model.common.getIconResId
import com.github.se.signify.model.home.hand.HandLandmarkViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.CameraBox
import com.github.se.signify.ui.common.LetterDictionary
import com.github.se.signify.ui.common.TextButton

/**
 * Composable that handles ASL recognition. It checks for camera permissions, launches the camera
 * preview, and displays recognized gestures and images.
 */
@Composable
fun ASLRecognition(
    handLandmarkViewModel: HandLandmarkViewModel,
    navigationActions: NavigationActions
) {
  val buttonUriString = stringResource(id = R.string.button_uri_string_text)
  val context = LocalContext.current

  val coroutineScope = rememberCoroutineScope()

  val landmarksState = handLandmarkViewModel.landMarks().collectAsState()
  val detectedGesture = handLandmarkViewModel.getSolution()

  val imageResource =
      if (landmarksState.value.isNullOrEmpty()) R.drawable.vector
      else getIconResId(detectedGesture.first())

  val makeASignText = stringResource(id = R.string.make_a_sign_text)
  val displayText = if (landmarksState.value.isNullOrEmpty()) makeASignText else detectedGesture
  val paintColor = MaterialTheme.colorScheme.background
  AnnexScreenScaffold(navigationActions = navigationActions, testTag = "ASLRecognitionScreen") {
    Spacer(modifier = Modifier.height(32.dp))
    Box(
        modifier =
            Modifier.fillMaxWidth().weight(1f).background(MaterialTheme.colorScheme.background)) {
          CameraBox(handLandmarkViewModel, "cameraPreview")
        }

    Spacer(modifier = Modifier.height(16.dp))

    LetterDictionary(
        coroutineScope = coroutineScope, numbOfHeaders = integerResource(R.integer.scroll_offset))

    Spacer(modifier = Modifier.height(16.dp))

    Box(
        modifier =
            Modifier.fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center) {
          Column(
              modifier = Modifier.fillMaxSize().padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally) {
                // Draws the output text or the prompt to "Make a sign" on the screen.
                HandGestureOverlay(
                    modifier = Modifier.fillMaxSize().weight(1f).testTag("gestureOverlayView"),
                    displayText = displayText,
                    paintColor = paintColor)
                // Displays the image associated with the detected ASL hand gesture
                Icon(
                    painter = painterResource(id = imageResource),
                    contentDescription = "Letter gesture",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxSize().weight(3f).testTag("handGestureImage"))
              }
        }

    Spacer(modifier = Modifier.height(16.dp))
    TextButton(
        onClick = {
          val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(buttonUriString) }
          context.startActivity(intent)
        },
        testTag = "practiceButton",
        text = stringResource(id = R.string.more_on_asl_alphabet_text),
        backgroundColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onPrimary)
  }
}

@Composable
fun HandGestureOverlay(modifier: Modifier, displayText: String, paintColor: Color) {
  Canvas(modifier = modifier) {
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
