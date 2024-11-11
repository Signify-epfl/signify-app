package com.github.se.signify.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class ThemeTest {
    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun verifyLightTheme() {
        composeTestRule.setContent { SignifyTheme(darkTheme = false) { TestContent("lightThemeContent") } }
        composeTestRule.onNodeWithTag("lightThemeContent").assertIsDisplayed()
    }

    @Test
    fun verifyDarkTheme() {
        composeTestRule.setContent { SignifyTheme(darkTheme = true) { TestContent("darkThemeContent") } }
        composeTestRule.onNodeWithTag("darkThemeContent").assertIsDisplayed()
    }
}

@Composable
fun TestContent(tag: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(tag)
    )
}
