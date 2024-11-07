package com.github.se.signify.ui.screens

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.R
import com.github.se.signify.model.hand.HandLandMarkImplementation
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.AccountInformation
import com.github.se.signify.ui.AllLetterLearned
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.CameraPlaceholder
import com.github.se.signify.ui.ColumnScreen
import com.github.se.signify.ui.HorizontalLetterList
import com.github.se.signify.ui.InfoPopup
import com.github.se.signify.ui.NotImplementedYet
import com.github.se.signify.ui.ProfilePicture
import com.github.se.signify.ui.ReusableButtonWithIcon
import com.github.se.signify.ui.ReusableTextButton
import com.github.se.signify.ui.ScaffoldAnnexeScreen
import com.github.se.signify.ui.ScaffoldMainScreen
import com.github.se.signify.ui.SquareButton
import com.github.se.signify.ui.StatisticsRow
import com.github.se.signify.ui.StreakCounter
import com.github.se.signify.ui.TopBar
import com.github.se.signify.ui.UtilButton
import com.github.se.signify.ui.navigation.NavigationActions
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class UtilsTest {

  @get:Rule val composeTestRule = createComposeRule()
  
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)
  
  private lateinit var navigationActions: NavigationActions

  @Test
  fun reusableButtonWithIconIsDisplayedAndClickable() {
    val iconDescription = "Info"

    // Set the content for the test
    composeTestRule.setContent {
      ReusableButtonWithIcon(
          onClickAction = {}, icon = Icons.Outlined.Info, iconDescription = iconDescription)
    }

    // Assert the button is displayed
    composeTestRule.onNodeWithTag(iconDescription + "Button").assertIsDisplayed()

    // Assert the button has a click action
    composeTestRule.onNodeWithTag(iconDescription + "Button").assertHasClickAction()
  }

  @Test
  fun reusableButtonWithIconPerformsClick() {
    var clickCounter = 0
    val iconDescription = "Info"

    // Set the content for the test with a click listener that increments the counter
    composeTestRule.setContent {
      ReusableButtonWithIcon(
          onClickAction = { clickCounter++ },
          icon = Icons.Outlined.Info,
          iconDescription = iconDescription)
    }

    // Perform a click action on the button
    composeTestRule.onNodeWithTag(iconDescription + "Button").performClick()

    // Assert the click listener was triggered
    assert(clickCounter == 1)
  }

  @Test
  fun reusableTextButtonIsDisplayedWithCorrectText() {
    val textTag = "TestButton"
    val buttonText = "Click Me"

    // Set the content for the test
    composeTestRule.setContent {
      ReusableTextButton(
          onClickAction = {},
          testTag = textTag,
          text = buttonText,
          backgroundColor = Color.Blue,
      )
    }

    // Assert the button is displayed
    composeTestRule.onNodeWithTag(textTag).assertIsDisplayed()

    // Assert the button contains the correct text
    composeTestRule.onNodeWithText(buttonText).assertExists()
  }

  @Test
  fun reusableTextButtonPerformsClick() {
    var clickCounter = 0
    val textTag = "TestButton"
    val buttonText = "Click Me"

    // Set the content for the test with a click listener that increments the counter
    composeTestRule.setContent {
      ReusableTextButton(
          onClickAction = { clickCounter++ },
          testTag = textTag,
          text = buttonText,
          backgroundColor = Color.Blue,
      )
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
          iconRes = R.drawable.battleicon,
          label = label,
          onClick = {},
          size = 100.dp,
          iconSize = 50.dp,
          labelFontSize = 16.sp,
          iconTint = Color.Gray,
          textColor = Color.White,
          modifier = Modifier.testTag(label))
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
          iconRes = R.drawable.battleicon,
          label = label,
          onClick = { clicked = true },
          size = 100.dp,
          iconSize = 50.dp,
          labelFontSize = 16.sp,
          iconTint = Color.Gray,
          textColor = Color.White,
          modifier = Modifier.testTag(label))
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
      UtilButton(
          onClick = {},
          testTagButton = "UtilButton",
          testTagIcon = "UtilIcon",
          icon = Icons.Outlined.Info,
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
      UtilButton(
          onClick = { clicked = true },
          testTagButton = "UtilButton",
          testTagIcon = "UtilIcon",
          icon = Icons.Outlined.Info,
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
    composeTestRule.onNodeWithTag("TopBlueBar").assertIsDisplayed()
  }

  // TODO: test the bottom bar after the refactor of the BottomNavigationMenu()
  @Test fun bottomBarIsDisplayed() {}

  @Test
  fun columnScreenDisplaysCorrectInformation() {
    composeTestRule.setContent {
      ColumnScreen(
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
  fun scaffoldMainScreenDisplaysCorrectInformation() {
    navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent {
      ScaffoldMainScreen(
          navigationActions = navigationActions,
          testTagColumn = "ScaffoldMainScreen",
          helpTitle = "Help",
          helpText = "This is the help text") {
            Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
          }
    }

    composeTestRule.onNodeWithTag("TopBlueBar").assertIsDisplayed()
    // TODO: test the bottom bar after the refactor of the BottomNavigationMenu()
    composeTestRule.onNodeWithTag("ScaffoldMainScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Text").assertIsDisplayed()
  }

  @Test
  fun scaffoldAnnexeScreenDisplaysCorrectInformation() {
    navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent {
      ScaffoldAnnexeScreen(
          navigationActions = navigationActions, testTagColumn = "ScaffoldAnnexeScreen") {
            Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
          }
    }

    composeTestRule.onNodeWithTag("TopBlueBar").assertIsDisplayed()
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
    val rowTestTag = "Stats"
    val lineText = "Line stats text"
    val lineTextTag = "LineTag"
    val columnTextList = listOf(listOf("test", "10"))
    val columnTextSPList = listOf(listOf(12, 20))
    val columnTextTagList = listOf("BoxTag")
    composeTestRule.setContent {
      StatisticsRow(
          rowTestTag = rowTestTag,
          lineText = lineText,
          lineTextTag = lineTextTag,
          columnTextList = columnTextList,
          columnTextSPList = columnTextSPList,
          columnTextTagList = columnTextTagList)
    }

    composeTestRule.onNodeWithTag(rowTestTag).assertIsDisplayed()
    composeTestRule.onNodeWithTag(lineTextTag).assertIsDisplayed()
    composeTestRule.onNodeWithTag(lineTextTag).assertTextEquals(lineText)
    composeTestRule.onNodeWithTag("BoxTag").assertIsDisplayed()
    composeTestRule.onNodeWithTag("test").assertTextEquals("test")
    composeTestRule.onNodeWithTag("10").assertTextEquals("10")
  }

  @Test
  fun horizontalLetterListIsDisplayedAndScrollable() {
    val lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F')
    composeTestRule.setContent { HorizontalLetterList(lettersLearned) }

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
  fun allLetterLearnedIsDisplayed() {
    val lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F')
    composeTestRule.setContent { AllLetterLearned(lettersLearned) }

    composeTestRule.onNodeWithTag("AllLetterLearned").assertIsDisplayed()
    composeTestRule.onNodeWithTag("LettersBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("LettersList").assertIsDisplayed()
  }

  @Test
  fun streakCounterIsDisplayed() {
    composeTestRule.setContent { StreakCounter(10, false) }

    composeTestRule.onNodeWithTag("StreakCounter").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FlameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertTextEquals("10")
  }

  @Test
  fun profilePictureIsDisplayed() {
    composeTestRule.setContent { ProfilePicture(null) }

    composeTestRule.onNodeWithTag("ProfilePicture").assertIsDisplayed()
  }

  @Test
  fun accountInformationIsDisplayed() {
    val userId = "userId"
    val userName = "userName"
    composeTestRule.setContent { AccountInformation(userId, userName, null, 10) }

    composeTestRule.onNodeWithTag("UserInfo").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserId").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserId").assertTextEquals(userId)
    composeTestRule.onNodeWithTag("UserName").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserName").assertTextEquals(userName)
    composeTestRule.onNodeWithTag("ProfilePicture").assertIsDisplayed()
    composeTestRule.onNodeWithTag("StreakCounter").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FlameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertTextEquals("10 days")
  }

  @Test
  fun notImplementedYetIsDisplayed() {
    val testTag = "Tag"
    val text = "Nothing for now"
    composeTestRule.setContent { NotImplementedYet(testTag, text) }

    composeTestRule.onNodeWithTag(testTag).onChild().assertIsDisplayed()
    composeTestRule.onNodeWithTag(testTag).onChild().assertTextEquals(text)

  @Test
  fun cameraPreview_isDisplayed() {
    val context = mock(Context::class.java)
    val handLandMarkImplementation =
        HandLandMarkImplementation("hand_landmarker.task", "RFC_model_ir9_opset19.onnx")
    val handLandMarkViewModel = HandLandMarkViewModel(handLandMarkImplementation, context)

    composeTestRule.setContent { CameraPlaceholder(handLandMarkViewModel) }
    composeTestRule.onNodeWithTag("cameraPreview").assertIsDisplayed()
  }
}
