package com.github.se.signify.ui.screens.profile

import android.content.ContentResolver
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import com.github.se.signify.model.authentication.MockUserSession
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.ui.common.ProfilePicture
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
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

    `when`(
            userRepository.getProfilePictureUrl(
                anyString(), any<(String?) -> Unit>(), any<(Exception) -> Unit>()))
        .thenAnswer { invocation ->
          val onSuccess = invocation.getArgument<(String?) -> Unit>(1)
          onSuccess(picturePath)
          null
        }

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
    composeTestRule.onNodeWithTag("usernameTextField").assertIsDisplayed()

    // Check if the edit username icon is displayed
    composeTestRule.onNodeWithContentDescription("Edit Username").assertIsDisplayed()

    // Check if the edit profile picture icon is displayed
    composeTestRule.onNodeWithTag("editProfilePictureButton").assertIsDisplayed()

    // Check if the delete profile picture icon is displayed
    composeTestRule.onNodeWithTag("deleteProfilePictureButton").assertIsDisplayed()

    // Check if the back button is displayed
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()

    // Check if the logout button is displayed
    composeTestRule.onNodeWithTag("logoutButton").assertIsDisplayed()


    composeTestRule.onRoot().printToLog("TAG")
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
    val newName = "Updated Username"

    composeTestRule.onNodeWithTag("usernameTextField").performTextInput(newName)

    composeTestRule.onNodeWithTag("usernameTextField").performImeAction()

    composeTestRule.onNodeWithTag("confirmationPopup").assertIsDisplayed()
  }

  @Test
  fun testDialogAppearsAfterLosingFocus() {
    val newName = "Updated Username"

    composeTestRule.onNodeWithTag("usernameTextField").performTextInput(newName)

    composeTestRule.onRoot().performClick()

    composeTestRule.onNodeWithTag("confirmationPopup").assertIsDisplayed()
  }

  @Test
  fun testPressingConfirmUpdatesName() {
    val newName = "Updated Username"

    composeTestRule.onNodeWithTag("usernameTextField").performTextInput(newName)

    composeTestRule.onNodeWithTag("usernameTextField").performImeAction()

    composeTestRule.onNodeWithTag("confirmationPopupConfirm").performClick()

    composeTestRule.onNodeWithTag("usernameTextField").assertTextEquals(newName)

    verify(userRepository)
        .updateUserName(Mockito.anyString(), Mockito.anyString(), anyOrNull(), anyOrNull())
  }

  @Test
  fun testPressingCancelDoesntUpdateName() {
    val newName = "Updated Username"

    composeTestRule.onNodeWithTag("usernameTextField").performTextInput(newName)

    composeTestRule.onNodeWithTag("usernameTextField").performImeAction()

    composeTestRule.onNodeWithTag("confirmationPopupCancel").performClick()

    verify(userRepository, never())
        .updateProfilePictureUrl(anyString(), anyOrNull(), anyOrNull(), anyOrNull())
  }

  @Test
  fun testDeleteProfilePictureShowsDialog() {

    composeTestRule.onNodeWithTag("deleteProfilePictureButton").performClick()

    composeTestRule.onNodeWithTag("confirmationPopup").assertIsDisplayed()
  }

  @Test
  fun testDeleteProfilePictureConfirm() {

    composeTestRule.onNodeWithTag("deleteProfilePictureButton").performClick()

    composeTestRule.onNodeWithTag("confirmationPopupConfirm").performClick()

    verify(userRepository)
        .updateProfilePictureUrl(Mockito.anyString(), eq(null), anyOrNull(), anyOrNull())
  }

  @Test
  fun testDeleteProfilePictureCancel() {
    composeTestRule.onNodeWithTag("deleteProfilePictureButton").performClick()

    composeTestRule.onNodeWithTag("confirmationPopupCancel").performClick()

    verify(userRepository, never())
        .updateProfilePictureUrl(anyString(), anyOrNull(), anyOrNull(), anyOrNull())
  }

  @Test
  fun testUserProfilePictureDisplaysImageWhenUriIsNotNull() {
    composeTestRule.onNodeWithTag("ProfilePicture").assertExists()
  }

  @Test
  fun testLanguageSwitch() {
    composeTestRule.onNodeWithTag("LanguageSwitch").assertIsDisplayed().performClick()
  }

  @Test
  fun logoutButton_callsLogoutAndNavigatesToWelcome() = runBlocking{

    // Click the logout button
    composeTestRule.onNodeWithTag("logoutButton").performClick()

    // Verify navigation to Welcome screen
    verify(navigationActions).navigateTo(Screen.WELCOME)
  }

}
