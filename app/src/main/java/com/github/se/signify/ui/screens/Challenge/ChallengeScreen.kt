package com.github.se.signify.ui.screens.Challenge

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.se.signify.R
import com.github.se.signify.ui.navigation.BottomNavigationMenu
import com.github.se.signify.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Route

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                          .background(Color(0xFF05A9FB))
                          .testTag("TopBlueLine"))

              // Info button centered 24dp (1cm) below the blue line
              Spacer(modifier = Modifier.height(24.dp))

              Box(
                  modifier =
                      Modifier.size(30.dp)
                          .background(Color(0xFF05A9FB), shape = RoundedCornerShape(30))
                          .border(2.dp, Color.White, shape = RoundedCornerShape(30))
                          .clickable { isHelpBoxVisible = !isHelpBoxVisible }
                          .testTag("InfoButton"),
                  contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "Help",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp).testTag("InfoIcon"))
                  }

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
fun SquareButton(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    size: Dp,
    iconSize: Dp, // Icon size passed here
    labelFontSize: TextUnit,
    iconTint: Color,
    textColor: Color,
    iconOffset: Dp = 0.dp,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
  Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier =
          modifier
              .size(size) // Button size
              .border(2.dp, Color.Black, RoundedCornerShape(12.dp)) // Black border
              .background(Color(0xFF05A9FB), RoundedCornerShape(12.dp)) // Blue inside
              .padding(16.dp)
              .clickable { onClick() }) {

        // Text on top of the icon
        Text(
            text = label,
            fontSize = labelFontSize,
            color = textColor,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Icon in the center of the square
        Box(contentAlignment = Alignment.Center) {
          Icon(
              painter = painterResource(id = iconRes),
              contentDescription = label,
              tint = iconTint, // Set icon tint to dark gray
              modifier =
                  Modifier.size(iconSize) // Applying the icon size
                      .offset(x = iconOffset) // Use the offset if specified
              )
        }
      }
}

@Composable
fun InfoPopup(onDismiss: () -> Unit) {
  Dialog(onDismissRequest = { onDismiss() }) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF05A9FB), // Blue background for the popup
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
