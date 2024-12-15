package com.github.se.signify.ui.screens.tutorial

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class ProvideElementPositionsTest {

  @get:Rule val composeTestRule = createComposeRule()

  private var positions: MutableMap<String, ElementPosition>? = null

  @Before
  fun setUp() {
    // Set up the shared content for all tests
    composeTestRule.setContent {
      ProvideElementPositions {
        Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
          // Add any additional test-specific tags if needed
        }
        positions = LocalElementPositions.current
      }
    }
  }

  @Test
  fun testProvideElementPositions_initializesCorrectly() {
    composeTestRule.onRoot().assertExists() // Verifies the composition exists
  }

  @Test
  fun testLocalElementPositions_providesMutableMap() {
    assert(
        positions?.isEmpty()
            ?: error("Element positions were not provided")) // Verifies the map is empty initially
  }

  @Test(expected = IllegalStateException::class)
  fun testLocalElementPositions_throwsErrorWhenNotProvided() {
    // Accessing LocalElementPositions outside of ProvideElementPositions should throw an exception
    composeTestRule.setContent { Box { LocalElementPositions.current } }
  }

  @Test
  fun testSaveElementPosition_addsPositionToMap() {
    val mockCoordinates = Mockito.mock(LayoutCoordinates::class.java)
    val density = Density(1f)

    // Mocking coordinates and size
    `when`(mockCoordinates.positionInWindow()).thenReturn(Offset(50f, 100f))
    `when`(mockCoordinates.size).thenReturn(IntSize(200, 400))

    // Save a position
    saveElementPosition(
        "testKey", mockCoordinates, density, positions ?: error("Positions not initialized"))

    val expectedPosition =
        ElementPosition(
            x = 50.dp,
            y = 76.dp, // 100 px - 24.dp padding
            width = 200.dp,
            height = 400.dp)

    // Assert the map contains the key and the correct value
    assert(positions?.containsKey("testKey") == true) {
      "Element positions should contain 'testKey'"
    }
    assert(positions?.get("testKey") == expectedPosition) {
      "Position should match expected values"
    }
  }

  @Test
  fun testSaveElementPosition_overwritesExistingKey() {
    val mockCoordinates = Mockito.mock(LayoutCoordinates::class.java)
    val density = Density(1f)

    // Mock initial position
    `when`(mockCoordinates.positionInWindow()).thenReturn(Offset(30f, 60f))
    `when`(mockCoordinates.size).thenReturn(IntSize(100, 200))
    saveElementPosition(
        "testKey", mockCoordinates, density, positions ?: error("Positions not initialized"))

    // Mock updated position
    `when`(mockCoordinates.positionInWindow()).thenReturn(Offset(90f, 120f))
    `when`(mockCoordinates.size).thenReturn(IntSize(300, 600))
    saveElementPosition(
        "testKey", mockCoordinates, density, positions ?: error("Positions not initialized"))

    val expectedPosition =
        ElementPosition(
            x = 90.dp,
            y = 96.dp, // 120 px - 24.dp padding
            width = 300.dp,
            height = 600.dp)

    // Assert the value is updated correctly
    assert(positions?.get("testKey") == expectedPosition) { "Position should be updated correctly" }
  }

  @Test
  fun testElementPositions_remainsIsolatedAcrossTests() {
    // Ensure a fresh map is provided for each test
    assert(positions?.isEmpty() == true) {
      "Positions map should be empty at the start of each test"
    }
  }
}
