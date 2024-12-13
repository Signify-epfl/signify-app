package com.github.se.signify.ui.screens.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag

sealed class HighlightedArea {
  data class HighlightedBoxArea(val elementPosition: ElementPosition, val overlayColor: Color) :
      HighlightedArea()

  data class FullScreenOverlayArea(val overlayColor: Color) : HighlightedArea()
}

@Composable
fun HighlightArea(area: HighlightedArea) {
  when (area) {
    is HighlightedArea.HighlightedBoxArea -> {
      HighlightedBox(elementPosition = area.elementPosition, overlayColor = area.overlayColor)
    }
    is HighlightedArea.FullScreenOverlayArea -> {
      FullScreenSemiTransparentOverlay(overlayColor = area.overlayColor)
    }
  }
}

@Composable
fun HighlightedBox(elementPosition: ElementPosition, overlayColor: Color) {

  Box(modifier = Modifier.fillMaxSize()) {
    // Top dark overlay above the highlight area
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(elementPosition.y) // Height until the top of the highlight area
                .background(overlayColor)
                .testTag("TopOverlay"))

    // Left dark overlay to the left of the highlight area
    Box(
        modifier =
            Modifier.width(elementPosition.x) // Width of the left margin before the highlight area
                .height(elementPosition.height) // Height of the highlight area
                .absoluteOffset(y = elementPosition.y) // Align with the top of the highlight
                .background(overlayColor)
                .testTag("LeftOverlay"))

    // Right dark overlay to the right of the highlight area
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(elementPosition.height) // Height of the highlight area
                .absoluteOffset(
                    x = elementPosition.x + elementPosition.width,
                    y = elementPosition.y) // Position after the highlight
                .background(overlayColor)
                .testTag("RightOverlay"))

    // Bottom dark overlay below the highlight area
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .fillMaxHeight()
                .absoluteOffset(
                    y = elementPosition.y + elementPosition.height) // Position after the highlight
                .background(overlayColor)
                .testTag("BottomOverlay"))

    // Border for the highlighted area
    Box(
        modifier =
            Modifier.absoluteOffset(
                    x = elementPosition.x, y = elementPosition.y) // Align with the highlighted area
                .size(width = elementPosition.width, height = elementPosition.height)
                .testTag("HighlightedBox"))
  }
}

@Composable
fun FullScreenSemiTransparentOverlay(overlayColor: Color) {
  Box(modifier = Modifier.fillMaxSize().background(overlayColor).testTag("FullScreenOverlay"))
}
