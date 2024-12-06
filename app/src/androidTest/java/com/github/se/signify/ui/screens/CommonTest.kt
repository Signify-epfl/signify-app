package com.github.se.signify.ui.screens

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.R
import com.github.se.signify.model.di.AppDependencyProvider
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.AccountInformation
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.BasicButton
import com.github.se.signify.ui.CameraBox
import com.github.se.signify.ui.InfoPopup
import com.github.se.signify.ui.LearnedLetterList
import com.github.se.signify.ui.LetterList
import com.github.se.signify.ui.MainScreenScaffold
import com.github.se.signify.ui.NotImplementedYet
import com.github.se.signify.ui.ProfilePicture
import com.github.se.signify.ui.ScreenColumn
import com.github.se.signify.ui.SquareButton
import com.github.se.signify.ui.StatisticsTable
import com.github.se.signify.ui.StreakCounter
import com.github.se.signify.ui.TextButton
import com.github.se.signify.ui.TopBar
import com.github.se.signify.ui.navigation.NavigationActions
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class UtilsTest {

  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  private lateinit var navigationActions: NavigationActions
  private val picturePath = "file:///path/to/profile/picture.jpg"

  @Test
  fun utilTextButtonIsDisplayedWithCorrectText() {
    val textTag = "TestButton"
    val buttonText = "Click Me"

    // Set the content for the test
    composeTestRule.setContent {
      TextButton(
          onClick = {},
          testTag = textTag,
          text = buttonText,
          backgroundColor = Color.Blue,
          textColor = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier)
    }

    // Assert the button is displayed
    composeTestRule.onNodeWithTag(textTag).assertIsDisplayed()

    // Assert the button contains the correct text
    composeTestRule.onNodeWithText(buttonText).assertExists()
  }

  @Test
  fun utilTextButtonPerformsClick() {
    var clickCounter = 0
    val textTag = "TestButton"
    val buttonText = "Click Me"

    // Set the content for the test with a click listener that increments the counter
    composeTestRule.setContent {
      TextButton(
          onClick = { clickCounter++ },
          testTag = textTag,
          text = buttonText,
          backgroundColor = Color.Blue,
          textColor = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier)
    }

    // Perform a click action on the button
    composeTestRule.onNodeWithTag(textTag).performClick()

    // Assert the click listener was triggered
    assert(clickCounter == 1)
  }

  @Test
  fun squareButtonIsDisplayedAndClickable() {
    val label = "Test Button"
    composeTestRule.setContent {
      SquareButton(
          iconId = R.drawable.battleicon,
          onClick = {},
          text = label,
          size = 100,
          modifier = Modifier.testTag(label),
      )
    }

    // Assert that the button is displayed
    composeTestRule.onNodeWithText(label).assertIsDisplayed()

    // Assert that the button has a click action
    composeTestRule.onNodeWithText(label).assertHasClickAction()
  }

  @Test
  fun squareButtonPerformsClick() {
    var clicked = false
    val label = "Test Button"
    composeTestRule.setContent {
      SquareButton(
          iconId = R.drawable.battleicon,
          onClick = { clicked = true },
          text = label,
          size = 100,
          modifier = Modifier.testTag(label),
      )
    }

    // Perform click action on the button
    composeTestRule.onNodeWithText(label).performClick()

    // Assert that the click action was triggered
    assert(clicked)
  }

  // Test for UtilButton
  @Test
  fun utilButtonIsDisplayedAndClickable() {
    composeTestRule.setContent {
      BasicButton(
          onClick = {},
          icon = Icons.Outlined.Info,
          buttonTestTag = "UtilButton",
          iconTestTag = "UtilIcon",
          contentDescription = "Info")
    }

    // Assert that the button is displayed
    composeTestRule.onNodeWithTag("UtilButton").assertIsDisplayed()

    // Assert that the button has a click action
    composeTestRule.onNodeWithTag("UtilButton").assertHasClickAction()
  }

  @Test
  fun utilButtonPerformsClick() {
    var clicked = false
    composeTestRule.setContent {
      BasicButton(
          onClick = { clicked = true },
          icon = Icons.Outlined.Info,
          buttonTestTag = "UtilButton",
          iconTestTag = "UtilIcon",
          contentDescription = "Info")
    }

    // Perform click action on the button
    composeTestRule.onNodeWithTag("UtilButton").performClick()

    // Assert that the click action was triggered
    assert(clicked)
  }

  // Test for BackButton
  @Test
  fun backButtonIsDisplayedAndClickable() {
    composeTestRule.setContent { BackButton(onClick = {}) }

    // Assert that the back button is displayed
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()

    // Assert that the back button has a click action
    composeTestRule.onNodeWithTag("BackButton").assertHasClickAction()
  }

  @Test
  fun backButtonPerformsClick() {
    var clicked = false
    composeTestRule.setContent { BackButton(onClick = { clicked = true }) }

    // Perform click action on the back button
    composeTestRule.onNodeWithTag("BackButton").performClick()

    // Assert that the click action was triggered
    assert(clicked)
  }

  @Test
  fun topBarIsDisplayed() {
    composeTestRule.setContent { TopBar() }

    // Assert that the top bar is displayed
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
  }

  @Test
  fun screenColumnDisplaysCorrectInformation() {
    composeTestRule.setContent {
      ScreenColumn(
          padding = PaddingValues(16.dp),
          testTag = "ColumnScreen",
      ) {
        Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
      }
    }

    composeTestRule.onNodeWithTag("ColumnScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Text").assertIsDisplayed()
  }

  @Test
  fun mainScreenScaffoldDisplaysCorrectInformation() {
    navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent {
      MainScreenScaffold(
          navigationActions = navigationActions,
          testTag = "ScaffoldMainScreen",
          helpTitle = "Help",
          helpText = "This is the help text") {
            Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
          }
    }

    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BottomNavigationMenu").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ScaffoldMainScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Text").assertIsDisplayed()
  }

  @Test
  fun annexScreenScaffoldDisplaysCorrectInformation() {
    navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent {
      AnnexScreenScaffold(navigationActions = navigationActions, testTag = "ScaffoldAnnexeScreen") {
        Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
      }
    }

    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ScaffoldAnnexeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Text").assertIsDisplayed()
  }

  @Test
  fun infoPopupIsDisplayed() {
    val helpTitle = "Help"
    val helpText = "Little text for the help"
    composeTestRule.setContent {
      InfoPopup(onDismiss = {}, helpTitle = helpTitle, helpText = helpText)
    }

    composeTestRule.onNodeWithTag("InfoPopup").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupContent").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupTitle").assertTextEquals(helpTitle)
    composeTestRule.onNodeWithTag("InfoPopupBody").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupBody").assertTextEquals(helpText)
    composeTestRule.onNodeWithTag("InfoPopupCloseButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupCloseButton").assertHasClickAction()
  }

  @Test
  fun infoPopupPerformClickOnButton() {
    var clicked = false
    val helpTitle = "Help"
    val helpText = "Little text for the help"
    composeTestRule.setContent {
      InfoPopup(onDismiss = { clicked = true }, helpTitle = helpTitle, helpText = helpText)
    }

    composeTestRule.onNodeWithTag("InfoPopupCloseButton").performClick()
    assert(clicked)
  }

  @Test
  fun statisticsRowIsDisplayed() {
    val columnTestTag = "StatsColumn"
    val rowTestTag = "StatsRow"
    val lineText = "Line stats text"
    val lineTextTag = "LineTag"
    val statsTextList = listOf("test1", "test2")
    val statsNumberList = listOf("1", "2")
    composeTestRule.setContent {
      StatisticsTable(
          lineText = lineText,
          statsTexts = statsTextList,
          statsNumberList = statsNumberList,
          columnTestTag = columnTestTag,
          rowTestTag = rowTestTag,
          lineTextTestTag = lineTextTag)
    }

    composeTestRule.onNodeWithTag(columnTestTag).assertIsDisplayed()
    composeTestRule.onNodeWithTag(rowTestTag).assertIsDisplayed()
    composeTestRule.onNodeWithTag(lineTextTag).assertIsDisplayed()
    composeTestRule.onNodeWithTag(lineTextTag).assertTextEquals(lineText)
    statsTextList.forEach { statsText ->
      composeTestRule.onNodeWithTag(statsText).assertIsDisplayed().assertTextEquals(statsText)
    }
    statsNumberList.forEach { statsNumber ->
      composeTestRule.onNodeWithTag(statsNumber).assertIsDisplayed().assertTextEquals(statsNumber)
    }
  }

  @Test
  fun horizontalLetterListIsDisplayedAndScrollable() {
    val lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F')
    composeTestRule.setContent { LetterList(lettersLearned) }

    composeTestRule.onNodeWithTag("LettersList").assertIsDisplayed()
    val scrollableList = composeTestRule.onNodeWithTag("LettersList")
    for (letter in 'A'..'Z') {
      scrollableList.performScrollToNode(hasTestTag(letter.toString()))
      composeTestRule.onNodeWithTag(letter.toString()).assertIsDisplayed()
    }
    scrollableList.performScrollToNode(hasTestTag("A"))
    composeTestRule.onNodeWithTag("A").assertIsDisplayed()
  }

  @Test
  fun learnedLetterListIsDisplayed() {
    val lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F')
    composeTestRule.setContent { LearnedLetterList(lettersLearned) }

    composeTestRule.onNodeWithTag("AllLetterLearned").assertIsDisplayed()
    composeTestRule.onNodeWithTag("LettersBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("LettersList").assertIsDisplayed()
  }

  @Test
  fun streakCounterIsDisplayed() {
    composeTestRule.setContent { StreakCounter(10) }

    composeTestRule.onNodeWithTag("StreakCounter").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FlameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertTextEquals("10")
  }

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

  @Test
  fun notImplementedYetIsDisplayed() {
    val testTag = "Tag"
    val text = "Nothing for now"
    composeTestRule.setContent { NotImplementedYet(text, testTag) }

    composeTestRule.onNodeWithTag(testTag).onChild().assertIsDisplayed()
    composeTestRule.onNodeWithTag(testTag).onChild().assertTextEquals(text)
  }

  @Test
  fun cameraPreview_isDisplayed() {
    val context = mock(Context::class.java)
    val handLandMarkImplementation = AppDependencyProvider.handLandMarkRepository()
    val handLandMarkViewModel = HandLandMarkViewModel(handLandMarkImplementation, context)

    composeTestRule.setContent { CameraBox(handLandMarkViewModel) }
    composeTestRule.onNodeWithTag("cameraPreview").assertIsDisplayed()
  }
}
