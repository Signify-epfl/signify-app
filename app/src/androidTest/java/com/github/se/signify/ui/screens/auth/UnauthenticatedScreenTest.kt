package com.github.se.signify.ui.screens.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class UnauthenticatedScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navigationActions: NavigationActions

    @Test
    fun unauthenticatedScreenDisplaysCorrectly() {
        navigationActions = mock(NavigationActions::class.java)
        composeTestRule.setContent { UnauthenticatedScreen(navigationActions = navigationActions) }

        composeTestRule.onNodeWithText("You are offline").assertIsDisplayed()

        composeTestRule.onNodeWithText("Log In").assertIsDisplayed()
        composeTestRule.onNodeWithTag("logInButtonInOfflineMode").assertIsDisplayed().performClick()
        verify(navigationActions).navigateTo(Screen.AUTH)
    }
}