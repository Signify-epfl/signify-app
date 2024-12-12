package com.github.se.signify.ui.screens.profile

import android.content.ContentResolver
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.core.net.toUri
import com.github.se.signify.model.authentication.MockUserSession
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.ui.common.ProfilePicture
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq

class SettingsScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private lateinit var userSession: UserSession
  private lateinit var userRepository: UserRepository
  private lateinit var userViewModel: UserViewModel
  private lateinit var context: Context
  private lateinit var contentResolver: ContentResolver

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    userSession = MockUserSession()
    userRepository = mock(UserRepository::class.java)
    userViewModel = UserViewModel(userSession, userRepository)
    val picturePath = "file:///path/to/profile/picture.jpg"

    composeTestRule.setContent {
      SettingsScreen(navigationActions, userSession, userRepository, false, {}, false, {})
      ProfilePicture(picturePath)
    }

    // Mock context and contentResolver
    context = mock(Context::class.java)
    contentResolver = mock(ContentResolver::class.java)

    // Stub the contentResolver in the mocked context
    `when`(context.contentResolver).thenReturn(contentResolver)
  }

  @Test
  fun testSettingsScreenDisplaysCorrectInformation() {

    // Check if the username is displayed
    composeTestRule.onNodeWithText(userViewModel.userName.value).assertIsDisplayed()

    // Check if the edit username icon is displayed
    composeTestRule.onNodeWithContentDescription("Edit Username").assertIsDisplayed()

    // Check if the edit profile picture icon is displayed
    composeTestRule.onNodeWithContentDescription("Edit Profile Picture").assertIsDisplayed()

    // Check if the delete profile picture icon is displayed
    composeTestRule.onNodeWithContentDescription("Delete Profile Picture").assertIsDisplayed()

    // Check if the "Other settings" section is displayed
    composeTestRule.onNodeWithText("Other settings:\nLanguage,\nTheme,\n...").assertIsDisplayed()

    // Check if the back button is displayed
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()
  }

  @Test
  fun testBackButtonNavigatesBack() {
    // Click the back button
    composeTestRule.onNodeWithTag("BackButton").performClick()

    // Check if back navigation was triggered
    verify(navigationActions).goBack()
  }

  @Test
  fun testDialogAppearsAfterPressingEnter() {
    val originalName = userViewModel.userName.value
    val newName = "Updated Username"

    composeTestRule.onNodeWithText(originalName).performTextInput(newName)

    composeTestRule.onNodeWithText(newName).performImeAction()

    composeTestRule.onNodeWithText("Confirm Changes").assertIsDisplayed()
  }

  @Test
  fun testPressingConfirmUpdatesName() {
    val newName = "Updated Username"

    composeTestRule.onNodeWithTag("usernameTextField").performTextInput(newName)

    composeTestRule.onNodeWithTag("usernameTextField").performImeAction()

    composeTestRule.onNodeWithText("Confirm").performClick()

    composeTestRule.onNodeWithTag("usernameTextField").assertTextEquals(newName)

    verify(userRepository)
        .updateUserName(Mockito.anyString(), eq(newName), anyOrNull(), anyOrNull())
  }

  @Test
  fun testPressingCancelRevertsName() {
    val originalName = userViewModel.userName.value
    val newName = "Updated Username"

    composeTestRule.onNodeWithTag("usernameTextField").performTextInput(newName)

    composeTestRule.onNodeWithTag("usernameTextField").performImeAction()

    composeTestRule.onNodeWithText("Cancel").performClick()

    composeTestRule.onNodeWithTag("usernameTextField").assertTextEquals(originalName)

    verify(userRepository, never())
        .updateUserName(Mockito.anyString(), eq(newName), anyOrNull(), anyOrNull())
  }

  @Test
  fun testDeleteProfilePictureShowsDialog() {
    composeTestRule.onNodeWithContentDescription("Delete Profile Picture").performClick()

    composeTestRule.onNodeWithText("Confirm Changes").assertIsDisplayed()
  }

  @Test
  fun testDeleteProfilePictureConfirm() {
    val newProfilePicturePath = "file:///path/to/new/profile/picture.jpg"
    composeTestRule.runOnIdle {
      userViewModel.updateProfilePictureUrl(newProfilePicturePath.toUri())
    }
    composeTestRule.onNodeWithContentDescription("Delete Profile Picture").performClick()

    composeTestRule.onNodeWithText("Confirm").performClick()

    verify(userRepository)
        .updateProfilePictureUrl(Mockito.anyString(), eq(null), anyOrNull(), anyOrNull())
  }

  @Test
  fun testDeleteProfilePictureCancel() {
    val newProfilePicturePath = "file:///path/to/new/profile/picture.jpg"
    composeTestRule.runOnIdle {
      userViewModel.updateProfilePictureUrl(newProfilePicturePath.toUri())
    }
    composeTestRule.onNodeWithContentDescription("Delete Profile Picture").performClick()

    composeTestRule.onNodeWithText("Cancel").performClick()

    verify(userRepository, never())
        .updateProfilePictureUrl(Mockito.anyString(), eq(null), anyOrNull(), anyOrNull())
  }

  @Test
  fun testUserProfilePictureDisplaysImageWhenUriIsNotNull() {
    composeTestRule.onNodeWithTag("ProfilePicture").assertExists()
  }

  @Test
  fun testLanguageSwitch() {
    composeTestRule.onNodeWithTag("LanguageSwitch").assertIsDisplayed().performClick()
  }
}
