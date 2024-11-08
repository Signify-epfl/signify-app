package com.github.se.signify.ui.screens.profile

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.navigation.NavigationActions
import java.io.File
import java.io.FileNotFoundException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.fail
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
  private lateinit var userRepository: UserRepository
  private lateinit var userViewModel: UserViewModel
  private lateinit var context: Context
  private lateinit var contentResolver: ContentResolver

  private val testUserID = "currentUserId"

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    userRepository = mock(UserRepository::class.java)
    userViewModel = UserViewModel(userRepository)
    val picturePath = "file:///path/to/profile/picture.jpg"

    composeTestRule.setContent {
      SettingsScreen(navigationActions, userViewModel)
      UserProfilePicture(picturePath)
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
    composeTestRule.onNodeWithText("Other settings:\nLanguage,\n theme, ...").assertIsDisplayed()

    // Check if the Cancel button is displayed
    composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()

    // Check if the Save button is displayed
    composeTestRule.onNodeWithText("Save").assertIsDisplayed()

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
  fun testUsernameTextFieldInputUpdates() {
    // Define a new name for testing
    val newName = "Updated Username"

    // Enter the new name into the TextField
    composeTestRule.onNodeWithText(userViewModel.userName.value).performTextInput(newName)

    // Verify that the TextField now contains the new name
    composeTestRule.onNodeWithText(newName).assertIsDisplayed()
  }

  @Test
  fun testSaveButtonUpdatesUserNameAndProfilePicture() {
    // Simulate entering a new name
    val newName = "New Username"
    composeTestRule.onNodeWithText(userViewModel.userName.value).performTextInput(newName)

    // Simulate selecting a new profile picture
    val newProfilePicturePath = "file:///path/to/new/profile/picture.jpg"
    composeTestRule.runOnIdle {
      userViewModel.updateProfilePictureUrl(testUserID, newProfilePicturePath)
    }

    // Click the Save button
    composeTestRule.onNodeWithText("Save").performClick()

    // Verify that the updateUserName function in the ViewModel is called with the correct name
    verify(userRepository)
        .updateUserName(Mockito.anyString(), eq(newName), anyOrNull(), anyOrNull())

    // Verify that the updateProfilePictureUrl function in the ViewModel is called with the correct
    // path
    verify(userRepository)
        .updateProfilePictureUrl(
            Mockito.anyString(), eq(newProfilePicturePath), anyOrNull(), anyOrNull())
  }

  @Test
  fun testSaveButtonDisabledWhenTextFieldIsEmpty() {
    // Ensure the TextField is empty
    composeTestRule.onNodeWithText(userViewModel.userName.value).performTextClearance()

    // Click the Save button
    composeTestRule.onNodeWithText("Save").performClick()

    // Verify that updateUserName is not called since the input was empty
    verify(userRepository, never())
        .updateUserName(Mockito.anyString(), Mockito.anyString(), anyOrNull(), anyOrNull())
  }

  @Test
  fun testCancelButtonClearsInput() {
    // Simulate entering a new name
    val newName = "Temporary Name"
    composeTestRule.onNodeWithText(userViewModel.userName.value).performTextInput(newName)

    // Simulate selecting a new profile picture
    val newProfilePicturePath = "file:///path/to/new/profile/picture.jpg"
    composeTestRule.runOnIdle {
      userViewModel.updateProfilePictureUrl(testUserID, newProfilePicturePath)
    }

    // Click the Cancel button
    composeTestRule.onNodeWithText("Cancel").performClick()

    // Verify that the TextField is cleared
    composeTestRule.onNodeWithText("Temporary Name").assertDoesNotExist()

    // Check that the original profile picture URL is restored
    composeTestRule.runOnIdle {
      val restoredProfilePictureUrl = userViewModel.profilePictureUrl.value
      assert(restoredProfilePictureUrl != newProfilePicturePath) {
        "Profile picture URL was not reset"
      }
    }
  }

  @Test
  fun testEditProfilePictureIconClick() {
    // Click the Edit Profile Picture icon
    composeTestRule.onNodeWithContentDescription("Edit Profile Picture").performClick()
  }

  @Test
  fun testUserProfilePictureDisplaysImageWhenUriIsNotNull() {

    // Verify that the AsyncImage is displayed with the mock profile picture
    composeTestRule.onNodeWithTag("profilePictureTag").assertExists()
  }

  @Test
  fun testUserProfilePictureDisplaysPlaceholderWhenUriIsNull() {

    // Verify that the default placeholder is displayed
    composeTestRule.onNodeWithTag("default_profile_picture").assertExists()
  }

  @Test
  fun testUriToFileReturnsFileWhenUriIsValid() {
    // Arrange
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val testData = "This is a test".toByteArray()

    // Create a temporary file and write test data into it
    val tempFile = File(context.cacheDir, "testFile.txt").apply { writeBytes(testData) }

    // Create a URI directly from the file path
    val fileUri = Uri.fromFile(tempFile)

    // Act
    val resultFile = uriToFile(context, fileUri)

    // Assert
    assertNotNull("File should not be null", resultFile)
    assertEquals(
        "File content should match test data", String(resultFile!!.readBytes()), String(testData))
  }

  @Test
  fun testUriToFileReturnsNullWhenUriIsInvalid() {
    // Arrange
    val context = InstrumentationRegistry.getInstrumentation().targetContext

    // Use a non-existent URI
    val invalidUri = Uri.parse("content://invalid/uri")

    // Act
    val resultFile = uriToFile(context, invalidUri)

    // Assert
    assertNull("File should be null when URI is invalid", resultFile)
  }

  @Test
  fun testUriToFileHandlesException() {
    // Arrange
    val mockUri = Uri.parse("content://mockuri")
    val contentResolver = InstrumentationRegistry.getInstrumentation().targetContext.contentResolver

    // Simulate exception by providing a non-existent URI
    try {
      contentResolver.openInputStream(mockUri)
      fail("Expected an exception to be thrown")
    } catch (e: FileNotFoundException) {
      // Expected behavior
    }

    // Act
    val resultFile = uriToFile(InstrumentationRegistry.getInstrumentation().targetContext, mockUri)

    // Assert
    assertNull("File should be null when an exception occurs", resultFile)
  }
}
