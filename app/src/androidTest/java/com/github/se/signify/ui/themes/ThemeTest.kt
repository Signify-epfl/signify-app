package com.github.se.signify.ui.themes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.signify.R
import com.github.se.signify.ui.theme.SignifyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ThemeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun VerifyPrimaryAndSecondaryColors() {
        // Set up the SignifyTheme
        composeTestRule.setContent {
            SignifyTheme {
                TestColorScreen()
            }
        }

        // Retrieve the expected colors from resources
        // Verify the primary color (blue) box is displayed
        composeTestRule.onNodeWithTag("primaryColorBox").assertIsDisplayed()

        // Verify the secondary color (white) box is displayed
        composeTestRule.onNodeWithTag("secondaryColorBox").assertIsDisplayed()
    }
}

@Composable
fun TestColorScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .testTag("primaryColorBox")
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .testTag("secondaryColorBox")
    )
}