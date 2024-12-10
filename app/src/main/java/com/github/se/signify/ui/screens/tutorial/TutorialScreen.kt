package com.github.se.signify.ui.screens.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.ui.screens.home.HomeScreen

private val overlayColor
  @Composable get() = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8F)

@Composable
fun TutorialScreen(navigationActions: NavigationActions, onFinish: () -> Unit) {
  var step by remember { mutableIntStateOf(0) }

  Box(modifier = Modifier.fillMaxSize()) {
    // Display the HomeScreen in the background
    HomeScreen(navigationActions)

    // Add an overlay to block input and gestures
    Box(
        modifier =
            Modifier.fillMaxSize().background(Color.Transparent).clickable(
                indication = null,
                interactionSource =
                    remember {
                      MutableInteractionSource()
                    }) { /* Block all interactions, including scrolling */})

    // Overlay for tutorial steps
    when (step) {
      0 ->
          TutorialOverlay(
              text = stringResource(R.string.tutorial_screen1_text),
              highlightArea = { FullScreenSemiTransparentOverlay() },
              onNext = { step++ },
              textTag = "WelcomeTextTag")
      1 ->
          TutorialOverlay(
              text = stringResource(R.string.tutorial_screen2_text),
              highlightArea = {
                HighlightedBox(
                    xOffset = 90.dp,
                    yOffset = 280.dp,
                    highlightWidth = 230.dp,
                    highlightHeight = 190.dp)
              },
              onNext = { step++ },
              textTag = "ExerciseTextTag")
      2 ->
          TutorialOverlay(
              text = stringResource(R.string.tutorial_last_screen_text),
              highlightArea = { FullScreenSemiTransparentOverlay() },
              onNext = {
                navigationActions.navigateTo(Screen.HOME)
                onFinish()
              },
              textTag = "CompletionTextTag")
    }
  }
}

@Composable
fun TutorialOverlay(
    text: String,
    highlightArea: @Composable () -> Unit,
    onNext: () -> Unit,
    textTag: String
) {
  Box(modifier = Modifier.fillMaxSize().testTag("HighlightArea")) {

    // Highlight the relevant area
    highlightArea()

    // Tutorial message and button
    Column(
        modifier =
            Modifier.align(Alignment.BottomCenter)
                .padding(start = 32.dp, end = 32.dp, bottom = 150.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
          text = text,
          color = MaterialTheme.colorScheme.onPrimary,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(bottom = 16.dp).testTag(textTag))
      Button(
          onClick = onNext,
          modifier = Modifier.testTag("nextButton"),
          colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
            Text(stringResource(R.string.next_text), color = MaterialTheme.colorScheme.onPrimary)
          }
    }
  }
}

@Composable
fun HighlightedBox(xOffset: Dp, yOffset: Dp, highlightWidth: Dp, highlightHeight: Dp) {
  // Fixed colors for overlay and border

  val borderColor = MaterialTheme.colorScheme.tertiary

  Box(modifier = Modifier.fillMaxSize()) {
    // Top dark overlay above the highlight area
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(yOffset) // Height until the top of the highlight area
                .background(overlayColor))

    // Left dark overlay to the left of the highlight area
    Box(
        modifier =
            Modifier.width(xOffset) // Width of the left margin before the highlight area
                .height(highlightHeight) // Height of the highlight area
                .absoluteOffset(y = yOffset) // Align with the top of the highlight
                .background(overlayColor))

    // Right dark overlay to the right of the highlight area
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(highlightHeight) // Height of the highlight area
                .absoluteOffset(
                    x = xOffset + highlightWidth, y = yOffset) // Position after the highlight
                .background(overlayColor))

    // Bottom dark overlay below the highlight area
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .fillMaxHeight()
                .absoluteOffset(y = yOffset + highlightHeight) // Position after the highlight
                .background(overlayColor))

    // Border for the highlighted area
    Box(
        modifier =
            Modifier.absoluteOffset(x = xOffset, y = yOffset) // Align with the highlighted area
                .size(width = highlightWidth, height = highlightHeight)
                .border(3.dp, borderColor) // Highlight border
        )
  }
}

@Composable
fun FullScreenSemiTransparentOverlay(
    color: Color = overlayColor // Default semi-transparent black
) {
  Box(modifier = Modifier.fillMaxSize().background(color))
}
