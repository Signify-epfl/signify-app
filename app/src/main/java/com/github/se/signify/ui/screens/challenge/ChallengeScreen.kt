package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
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
import com.github.se.signify.ui.SquareButton
import com.github.se.signify.ui.UtilButton
import com.github.se.signify.ui.navigation.BottomNavigationMenu
import com.github.se.signify.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Route

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChallengeScreen(navigationActions: NavigationActions) {
  var isHelpBoxVisible by remember { mutableStateOf(false) }

  Scaffold(
      bottomBar = {
        BottomNavigationMenu(
            onTabSelect = { route -> navigationActions.navigateTo(route) },
            tabList = LIST_TOP_LEVEL_DESTINATION,
            selectedItem = navigationActions.currentRoute(),
            modifier = Modifier.testTag("BottomNavigationMenu"))
      },
      content = {
        Column(
            modifier =
                Modifier.fillMaxSize().background(Color.White).testTag("ChallengeScreenContent"),
            horizontalAlignment = Alignment.CenterHorizontally) {
              // Top blue line
              Box(
                  modifier =
                      Modifier.fillMaxWidth()
                          .height(4.dp)
                          .background(colorResource(R.color.blue))
                          .testTag("TopBlueLine"))

              // Info button centered 24dp (1cm) below the blue line
              Spacer(modifier = Modifier.height(24.dp))

              UtilButton(
                  { isHelpBoxVisible = !isHelpBoxVisible },
                  "InfoButton",
                  "InfoIcon",
                  Icons.Outlined.Info,
                  "Help")

              Spacer(modifier = Modifier.height(90.dp)) // Space before the square buttons

              // Column containing the Challenge and History square buttons, stacked vertically
              Column(
                  modifier = Modifier.fillMaxWidth().testTag("ButtonsColumn"),
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.spacedBy(50.dp)) {
                    // Challenge square button
                    SquareButton(
                        iconRes = R.drawable.battleicon,
                        label = "Challenge",
                        onClick = { navigationActions.navigateTo(Route.NEW_CHALLENGE) },
                        size = 240.dp,
                        iconSize = 160.dp,
                        labelFontSize = 32.sp,
                        iconTint = Color.DarkGray,
                        textColor = Color.DarkGray,
                        modifier = Modifier.testTag("ChallengeButton"))

                    // History square button
                    SquareButton(
                        iconRes = R.drawable.historyicon,
                        label = "History",
                        onClick = { navigationActions.navigateTo(Route.CHALLENGE_HISTORY) },
                        size = 240.dp,
                        iconSize = 160.dp,
                        labelFontSize = 32.sp,
                        iconTint = Color.DarkGray,
                        textColor = Color.DarkGray,
                        5.dp,
                        modifier = Modifier.testTag("HistoryButton"))
                  }

              // Show popup when the info button is clicked
              if (isHelpBoxVisible) {
                InfoPopup(onDismiss = { isHelpBoxVisible = false })
              }
            }
      })
}

@Composable
fun InfoPopup(onDismiss: () -> Unit) {
  Dialog(onDismissRequest = { onDismiss() }) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = colorResource(R.color.blue), // Blue background for the popup
        modifier =
            Modifier.border(
                    2.dp,
                    Color.Black,
                    RoundedCornerShape(12.dp)) // Ensure the black border wraps the popup
                .padding(0.dp)
                .testTag("InfoPopup") // Remove padding for the border
        ) {
          Column(
              modifier = Modifier.padding(16.dp).fillMaxWidth().testTag("InfoPopupContent"),
              horizontalAlignment = Alignment.CenterHorizontally) {
                // Title "Challenge" centered and underlined
                Text(
                    text =
                        buildAnnotatedString {
                          withStyle(
                              style =
                                  SpanStyle(
                                      fontWeight = FontWeight.Bold,
                                      textDecoration = TextDecoration.Underline)) {
                                append("Challenge")
                              }
                        },
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("InfoPopupTitle"))

                Spacer(modifier = Modifier.height(8.dp))

                // Body text centered under the title
                Text(
                    text =
                        stringResource(
                            id = R.string.description_challenge), // Text from strings.xml
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("InfoPopupBody"))

                Spacer(modifier = Modifier.height(16.dp))

                // Close button for the popup
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.testTag("InfoPopupCloseButton")) {
                      Text(text = "Close", color = Color.White)
                    }
              }
        }
  }
}
