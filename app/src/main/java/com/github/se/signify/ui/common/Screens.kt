package com.github.se.signify.ui.common

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.se.signify.R
import com.github.se.signify.model.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.model.navigation.TopLevelDestination

data class HelpText(val title: String, val text: String)

/**
 * The scaffold for all main screens.
 *
 * @param navigationActions The navigationActions of the bottom navigation menu.
 * @param testTag The test tag of the column (test tag of the screen).
 * @param helpText The optional text for a help popup.
 * @param topBarButtons A list of optional buttons to display in the top bar. They should be
 *   `Buttons.BasicButton()`s.
 * @param floatingActionButton An optional floating action button. It should be a
 *   `Button.BasicButton()`.
 * @param content A lambda function for the content of the column.
 */
@Composable
fun MainScreenScaffold(
    navigationActions: NavigationActions,
    testTag: String,
    helpText: HelpText?,
    topBarButtons: List<@Composable () -> Unit> = emptyList(),
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (ColumnScope.() -> Unit)
) {
  Scaffold(
      floatingActionButton = { floatingActionButton() },
      topBar = { TopLine() },
      bottomBar = { BottomBar(navigationActions) },
      content = { padding ->
        ScreenColumn(
            padding,
            testTag,
        ) {
            if (helpText != null) {
                HelpButton(helpText)
            }
          content()

        }
      })
}
/**
 * The scaffold for all annex screens.
 *
 * @param navigationActions The navigationActions of the bottom navigation menu.
 * @param testTag The test tag of the column (test tag of the screen).
 * @param topBarButtons A list of optional buttons to display in the top bar. They should be `Buttons.BasicButton()`s.
 * @param content A lambda function for the content of the column.
 */
@Composable
fun AnnexScreenScaffold(
    navigationActions: NavigationActions,
    testTag: String,
    topBarButtons: List<@Composable () -> Unit> = emptyList(),
    content: @Composable ColumnScope.() -> Unit
) {
  Scaffold(
      topBar = { TopLine() },
      content = { padding ->
        ScreenColumn(padding, testTag) {
          BackButton { navigationActions.goBack() }
          content()
        }
      })
}
/**
 * The basic column for all screens. This is a helper function for `MainScreenScaffold()` and
 * `AnnexScreenScaffold()`.
 *
 * @param padding The default padding value of the column.
 * @param testTag The test tag of the column (test tag of the screen).
 * @param content A lambda function for the content of the column.
 */
@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun ScreenColumn(
    padding: PaddingValues,
    testTag: String,
    content: @Composable ColumnScope.() -> Unit
) {
  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(padding)
              .padding(16.dp)
              .verticalScroll(rememberScrollState())
              .background(MaterialTheme.colorScheme.background)
              .testTag(testTag),
      horizontalAlignment = Alignment.CenterHorizontally) {
        content()
      }
}

/** The decorative line at the top of screens. */
@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun TopLine() {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(5.dp)
              .background(MaterialTheme.colorScheme.primary)
              .testTag("TopBar"))
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun HelpButton(helpText: HelpText) {
    var isHelpBoxVisible by remember { mutableStateOf(false) }
    BasicButton(
        onClick = { isHelpBoxVisible = !isHelpBoxVisible },
        icon = Icons.Outlined.Info,
        iconTestTag = "InfoIcon",
        contentDescription = "Help",
        modifier = Modifier.testTag("InfoButton"),
    )

    // Show popup when the info button is clicked
    if (isHelpBoxVisible) {
        HelpPopup(
            onDismiss = { isHelpBoxVisible = false },
            helpText,
        )
    }
}

/**
 * The info popup for main screens.
 *
 * @param onDismiss A lambda function to execute when the button or outside the box or is clicked.
 * @param helpText The help popup to display.
 */
@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun HelpPopup(onDismiss: () -> Unit, helpText: HelpText) {
  Dialog(onDismissRequest = { onDismiss() }) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.background, // Background for the popup
        modifier =
            Modifier.border(
                    3.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(12.dp)) // Ensure the border wraps the popup
                .testTag("InfoPopup")) {
          Column(
              modifier = Modifier.padding(16.dp).fillMaxWidth().testTag("InfoPopupContent"),
              horizontalAlignment = Alignment.CenterHorizontally) {
                // Title centered and underlined
                Text(
                    text =
                        buildAnnotatedString {
                          withStyle(
                              style =
                                  SpanStyle(
                                      fontWeight = FontWeight.Bold,
                                      textDecoration = TextDecoration.Underline)) {
                                append(helpText.title)
                              }
                        },
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("InfoPopupTitle"))
                Spacer(modifier = Modifier.height(8.dp))
                // Body text centered under the title
                Text(
                    text = helpText.text,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("InfoPopupBody"))
                Spacer(modifier = Modifier.height(16.dp))
                // Close button for the popup
                Button(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("InfoPopupCloseButton")) {
                      Text(
                          text = stringResource(R.string.close_text),
                          color = MaterialTheme.colorScheme.onPrimary)
                    }
              }
        }
  }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun BackButton(onClick: () -> Unit) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      horizontalArrangement = Arrangement.Start) {
        IconButton(onClick = { onClick() }, modifier = Modifier.testTag("BackButton")) {
          Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "BackButton",
              tint = MaterialTheme.colorScheme.primary)
        }
      }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun BottomBar(navigationActions: NavigationActions) {
  BottomNavigationMenu(
      onTabSelect = { route -> navigationActions.navigateTo(route) },
      tabList = LIST_TOP_LEVEL_DESTINATION,
      selectedItem = navigationActions.currentRoute())
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun BottomNavigationMenu(
    onTabSelect: (TopLevelDestination) -> Unit,
    tabList: List<TopLevelDestination>,
    selectedItem: String? = Screen.HOME.route, // Provide a default value
) {
  Box(
      modifier =
          Modifier.border(2.dp, MaterialTheme.colorScheme.outlineVariant) // Top blue line
              .padding(bottom = 2.dp)
              .testTag("BottomNavigationMenu")) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            containerColor = MaterialTheme.colorScheme.background // Set background to white
            ) {
              tabList.forEach { tab ->
                NavigationBarItem(
                    icon = {
                      Icon(
                          painterResource(id = tab.icon),
                          contentDescription = null,
                          modifier = Modifier.testTag("TabIcon_${tab.route}"))
                    }, // Load the drawable icons
                    selected = tab.route == selectedItem,
                    onClick = { onTabSelect(tab) },
                    modifier = Modifier.clip(RoundedCornerShape(50.dp)))
              }
            }
      }
}
