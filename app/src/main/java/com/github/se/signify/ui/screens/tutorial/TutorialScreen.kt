package com.github.se.signify.ui.screens.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.screens.home.HomeScreen

@Composable
fun TutorialScreen(navigationActions: NavigationActions, onFinish: () -> Unit) {
  var step by remember { mutableIntStateOf(0) }

  // Activate tutorial blocking during the tutorial
  LaunchedEffect(Unit) { navigationActions.tutorialActive = true }

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
              text = stringResource(R.string.tutorial_screen1),
              highlightArea = { FullScreenSemiTransparentOverlay() },
              onNext = { step++ })
      1 ->
          TutorialOverlay(
              text = stringResource(R.string.tutorial_screen2),
              highlightArea = {
                HighlightedBox(
                    xOffset = 90.dp,
                    yOffset = 280.dp,
                    highlightWidth = 230.dp,
                    highlightHeight = 190.dp)
              },
              onNext = { step++ })
      2 ->
          TutorialOverlay(
              text = stringResource(R.string.tutorial_last_screen),
              highlightArea = { FullScreenSemiTransparentOverlay() },
              onNext = onFinish)
    }
  }
}

@Composable
fun TutorialOverlay(text: String, highlightArea: @Composable () -> Unit, onNext: () -> Unit) {
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
          modifier = Modifier.padding(bottom = 16.dp))
      Button(
          onClick = onNext,
          colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
            Text("Next", color = MaterialTheme.colorScheme.onPrimary)
          }
    }
  }
}

@Composable
fun HighlightedBox(xOffset: Dp, yOffset: Dp, highlightWidth: Dp, highlightHeight: Dp) {
  // Fixed colors for overlay and border
  val overlayColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8F)
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
    color: Color =
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8F) // Default semi-transparent black
) {
  Box(modifier = Modifier.fillMaxSize().background(color))
}
