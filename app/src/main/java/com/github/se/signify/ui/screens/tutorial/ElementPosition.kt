package com.github.se.signify.ui.screens.tutorial

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp

data class ElementPosition(val x: Dp, val y: Dp, val width: Dp, val height: Dp)

val LocalElementPositions =
    compositionLocalOf<MutableMap<String, ElementPosition>> {
      error("No element positions provided")
    }

@Composable
fun ProvideElementPositions(content: @Composable () -> Unit) {
  val elementPositions = remember { mutableStateMapOf<String, ElementPosition>() }
  CompositionLocalProvider(LocalElementPositions provides elementPositions) { content() }
}

/**
 * Saves the position and size of a UI element into a mutable map.
 *
 * This function calculates the element's position in the window's coordinate space, converts pixel
 * values to density-independent pixels (dp), and stores them in the map as an `ElementPosition`
 * object.
 *
 * @param key A unique key to identify the element. It will be used as the key in the map.
 * @param coordinates The layout coordinates of the element, providing its position and size in
 *   pixels.
 * @param density The screen density, used to convert pixel (px) values to dp.
 * @param map A mutable map where the position and size of the element will be saved. The key is a
 *   `String`, and the value is an `ElementPosition` object.
 * @return This function does not return anything (Unit), but it updates the mutable map with the
 *   position and size of the specified element.
 */
fun saveElementPosition(
    key: String,
    coordinates: androidx.compose.ui.layout.LayoutCoordinates,
    density: androidx.compose.ui.unit.Density,
    map: MutableMap<String, ElementPosition>
) {
  val position = coordinates.positionInRoot()
  val size = coordinates.size
  map[key] =
      ElementPosition(
          x = with(density) { position.x.toDp() },
          y = with(density) { position.y.toDp() },
          width = with(density) { size.width.toDp() },
          height = with(density) { size.height.toDp() })
}
