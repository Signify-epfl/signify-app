package com.github.se.signify.ui.common

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class ProfileTest {
  @get:Rule val composeTestRule = createComposeRule()

  private val picturePath = "picturePath.jpg"

  @Test
  fun profilePictureIsDisplayed() {
    composeTestRule.setContent { ProfilePicture(picturePath) }

    composeTestRule.onNodeWithTag("ProfilePicture").assertExists()
  }

  @Test
  fun accountInformationIsDisplayed() {
    val userId = "userId"
    val userName = "userName"
    composeTestRule.setContent { AccountInformation(userId, userName, picturePath, 10) }

    composeTestRule.onNodeWithTag("UserInfo").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserId").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserId").assertTextEquals(userId)
    composeTestRule.onNodeWithTag("UserName").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserName").assertTextEquals(userName)
    composeTestRule.onNodeWithTag("ProfilePicture").assertExists()
    composeTestRule.onNodeWithTag("StreakCounter").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FlameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertTextEquals("10")
  }
}
