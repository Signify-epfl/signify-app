package com.github.se.signify.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.HelpText
import com.github.se.signify.ui.common.TextButton

/**
 * A composable function that displays a screen for offline mode.
 *
 * This screen informs the user that they are offline and provides a "Log In" button to navigate to
 * the authentication screen (Screen.AUTH). The screen utilizes a scaffold with a bottom navigation
 * menu and a consistent design from utility components.
 *
 * @param navigationActions An instance of [NavigationActions] used for navigation within the app.
 */
@Composable
fun UnauthenticatedScreen(navigationActions: NavigationActions) {
  val offlineModeText = stringResource(R.string.offline_mode_text)
  val offModeText = stringResource(R.string.off_mode_text)

  AnnexScreenScaffold(
      navigationActions = navigationActions,
      testTag = "UnauthenticatedScreen",
      helpText =
          HelpText(title = offModeText, content = stringResource(R.string.help_offline_mode_text)),
      content = {
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
              Text(
                  text = offlineModeText,
                  style =
                      TextStyle(
                          fontSize = 24.sp,
                          fontWeight = FontWeight.Bold,
                          color = Color.Black, // Black text
                          textAlign = TextAlign.Center),
                  modifier =
                      Modifier.padding(bottom = 16.dp)
                          .testTag("UnauthenticatedText") // Space below the text
                  )

              // "Log In" button using UtilTextButton
              TextButton(
                  onClick = {
                    navigationActions.navigateTo(Screen.AUTH)
                  }, // Navigate to Screen.AUTH
                  testTag = "logInButton",
                  text = stringResource(R.string.log_in_text),
                  backgroundColor = MaterialTheme.colorScheme.primary,
                  textColor = MaterialTheme.colorScheme.onPrimary,
                  modifier = Modifier)
            }
      })
}
