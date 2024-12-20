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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.se.signify.model.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.TopLevelDestination

data class HelpText(val title: String, val content: String)

/**
 * The scaffold for all main screens.
 *
 * @param navigationActions The navigationActions of the bottom navigation menu.
 * @param topLevelDestination The top level destination of the screen. This is used to highlight the
 *   correct tab in the bottom navigation menu.
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
    topLevelDestination: TopLevelDestination,
    testTag: String,
    helpText: HelpText? = null,
    topBarButtons: List<@Composable () -> Unit> = emptyList(),
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (ColumnScope.() -> Unit)
) {
  Scaffold(
      floatingActionButton = { floatingActionButton() },
      topBar = {
        TopBar(
            buttons = topBarButtons,
            helpText = helpText,
        )
      },
      bottomBar = { BottomBar(navigationActions, topLevelDestination) },
      content = { padding -> ScreenColumn(padding, testTag, content) })
}

/**
 * The scaffold for all annex screens.
 *
 * @param navigationActions The navigationActions of the bottom navigation menu.
 * @param testTag The test tag of the column (test tag of the screen).
 * @param topBarButtons A list of optional buttons to display in the top bar. They should be
 *   `Buttons.BasicButton()`s.
 * @param content A lambda function for the content of the column.
 */
@Composable
fun AnnexScreenScaffold(
    navigationActions: NavigationActions,
    testTag: String,
    helpText: HelpText? = null,
    topBarButtons: List<@Composable () -> Unit> = emptyList(),
    content: @Composable (ColumnScope.() -> Unit)
) {
  Scaffold(
      topBar = {
        TopBar(
            buttons =
                listOf<@Composable () -> Unit> { BackButton(navigationActions) } + topBarButtons,
            helpText = helpText,
        )
      },
      content = { padding -> ScreenColumn(padding, testTag, content) })
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun TopBar(buttons: List<@Composable () -> Unit>, helpText: HelpText?) {
  Column(modifier = Modifier.testTag("TopBar")) {
    TopLine()

    Row(
        modifier =
            Modifier.fillMaxWidth().padding(12.dp).background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
      buttons.forEach { it() }

      // Align the help button to the right
      if (buttons.isEmpty()) {
        Spacer(modifier = Modifier.weight(1f))
      }

      if (helpText != null) {
        HelpButton(helpText)
      }
    }

    SeparatorLine()
  }
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
              .padding(horizontal = 16.dp)
              .verticalScroll(rememberScrollState())
              .background(MaterialTheme.colorScheme.background)
              .testTag(testTag),
      horizontalAlignment = Alignment.CenterHorizontally) {
        content()
        Spacer(modifier = Modifier.height(32.dp))
      }
}

/** The decorative line at the top of screens. */
@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun TopLine() {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(3.dp)
              .background(MaterialTheme.colorScheme.primary)
              .testTag("TopLine"))
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun SeparatorLine() {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(1.dp)
              .background(MaterialTheme.colorScheme.outline)
              .testTag("SeparatorLine"))
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun HelpButton(helpText: HelpText) {
  var isHelpBoxVisible by remember { mutableStateOf(false) }
  BasicButton(
      onClick = { isHelpBoxVisible = !isHelpBoxVisible },
      iconTestTag = "HelpIcon",
      contentDescription = "Help",
      modifier = Modifier.testTag("HelpButton"),
      icon = Icons.Outlined.Info,
  )

  // Show popup when the help button is clicked
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
        color = MaterialTheme.colorScheme.background,
        modifier =
            Modifier.border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .testTag("HelpPopup")) {
          Column(
              modifier = Modifier.padding(16.dp).fillMaxWidth().testTag("HelpPopupContent"),
              horizontalAlignment = Alignment.CenterHorizontally) {
                // Title
                Text(
                    text =
                        buildAnnotatedString {
                          withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(helpText.title)
                          }
                        },
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.testTag("HelpPopupTitle"))

                Spacer(modifier = Modifier.height(8.dp))

                // Body
                Text(
                    text = helpText.content,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.testTag("HelpPopupBody"))
                Spacer(modifier = Modifier.height(16.dp))
              }
        }
  }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun BackButton(navigationActions: NavigationActions) {
  BasicButton(
      onClick = { navigationActions.goBack() },
      iconTestTag = "BackIcon",
      contentDescription = "Back",
      modifier = Modifier.testTag("BackButton"),
      icon = Icons.AutoMirrored.Filled.ArrowBack,
  )
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun BottomBar(navigationActions: NavigationActions, selected: TopLevelDestination) {
  Column {
    TopLine()
    BottomNavigationMenu(
        onTabSelect = { route -> navigationActions.navigateTo(route) },
        topLevelDestinations = LIST_TOP_LEVEL_DESTINATION,
        selected = selected,
    )
    SeparatorLine()
  }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun BottomNavigationMenu(
    onTabSelect: (TopLevelDestination) -> Unit,
    topLevelDestinations: List<TopLevelDestination>,
    selected: TopLevelDestination, // Provide a default value
) {
  NavigationBar(
      modifier = Modifier.fillMaxWidth().height(60.dp).testTag("BottomNavigationMenu"),
  ) {
    topLevelDestinations.forEach { topLevelDestination ->
      NavigationBarItem(
          icon = {
            Icon(
                painterResource(id = topLevelDestination.icon),
                contentDescription = null,
                modifier = Modifier.testTag("TabIcon_${topLevelDestination.route}"))
          }, // Load the drawable icons
          selected = topLevelDestination == selected,
          enabled = topLevelDestination != selected,
          onClick = { onTabSelect(topLevelDestination) },
          modifier =
              Modifier.clip(RoundedCornerShape(50.dp)).testTag("Tab_${topLevelDestination.route}"),
          colors =
              NavigationBarItemDefaults.colors(
                  selectedIconColor = MaterialTheme.colorScheme.primary,
                  indicatorColor = Color.Transparent,
                  unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                  disabledIconColor = MaterialTheme.colorScheme.primary,
              ))
    }
  }
}
