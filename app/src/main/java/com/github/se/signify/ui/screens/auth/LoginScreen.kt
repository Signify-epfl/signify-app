@file:Suppress("DEPRECATION") // We were told to use deprecated Google Authentication API

package com.github.se.signify.ui.screens.auth

/**
 * This file contains the implementation of the `LoginScreen` and related composable functions for
 * handling the user login process in the application. It includes:
 * - UI components for login and offline mode.
 * - Integration with Google Authentication via a deprecated API.
 * - Mock authentication support for testing environments.
 */
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.model.authentication.AuthService
import com.github.se.signify.model.authentication.FirebaseAuthService
import com.github.se.signify.model.authentication.MockAuthService
import com.github.se.signify.model.common.user.saveUserToFirestore
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.model.profile.stats.saveStatsToFirestore
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult

/**
 * Composable function that represents the Login Screen.
 *
 * @param navigationActions Actions to navigate between app screens.
 * @param showTutorial Callback to display the tutorial.
 * @param authService Authentication service, defaults to `MockAuthService` for testing.
 */
@Composable
fun LoginScreen(
    navigationActions: NavigationActions,
    showTutorial: () -> Unit,
    authService: AuthService = MockAuthService()
) {
  val context = LocalContext.current
  val loginSuccessfullText = stringResource(R.string.login_successful_text)
  val loginFailedText = stringResource(R.string.login_successful_text)

  val token = stringResource(id = R.string.default_web_client_id)
  val authLauncher =
      rememberAuthLauncher(
          onAuthComplete = { _ ->
            Toast.makeText(context, loginSuccessfullText, Toast.LENGTH_LONG).show()
            saveUserToFirestore()
            saveStatsToFirestore()
            navigationActions.navigateTo(Screen.HOME)
            showTutorial()
          },
          onAuthError = {
            Log.e("SignInScreen", "Failed to sign in: ${it.statusCode}")
            Toast.makeText(context, loginFailedText, Toast.LENGTH_LONG).show()
          },
          authService = authService)
  // Gradient brush for background
  val gradient =
      Brush.verticalGradient(
          colors =
              listOf(
                  MaterialTheme.colorScheme.background,
                  MaterialTheme.colorScheme.background,
                  MaterialTheme.colorScheme.background,
                  MaterialTheme.colorScheme.primary))

  Scaffold(
      modifier = Modifier.fillMaxSize(),
      content = { padding ->
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .padding(padding)
                    .background(brush = gradient)
                    .testTag("LoginScreen"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
          // App Logo Image
          Icon(
              painter = painterResource(id = R.drawable.vector),
              contentDescription = "icon description",
              tint = MaterialTheme.colorScheme.primary,
              modifier =
                  Modifier.padding(3.dp)
                      .width(139.dp)
                      .height(81.dp)
                      .background(
                          color = MaterialTheme.colorScheme.background) // Primary background
              )

          Spacer(modifier = Modifier.height(70.dp))

          // Welcome Text
          Text(
              modifier =
                  Modifier.fillMaxWidth(0.8f)
                      .padding(vertical = 16.dp) // Adds spacing around the text
                      .testTag("IntroMessage"),
              text = stringResource(R.string.signify_welcome_text),
              style =
                  TextStyle(
                      fontSize = 32.sp,
                      lineHeight = 40.sp,
                      fontWeight = FontWeight(400),
                      color = MaterialTheme.colorScheme.primary,
                      textAlign = TextAlign.Center,
                      letterSpacing = 0.25.sp,
                  ))

          Spacer(modifier = Modifier.height(120.dp))

          // Authenticate With Google Button
          GoogleSignInButton(
              onSignInClick = {
                if (authService.isMocked()) {
                  navigationActions.navigateTo(Screen.HOME)
                  //  /!\ Add mocks here Like quiz, challenge, etc. /!\
                } else {
                  authLauncher.launch(authService.getSignInIntent(context, token))
                }
              })
          val offlineModeText = stringResource(R.string.offline_mode_text)
          SkipLoginButton {
            Log.d("LoginScreen", "Proceeding in offline state.")
            Toast.makeText(context, offlineModeText, Toast.LENGTH_LONG).show()
            showTutorial()
            navigationActions.navigateTo(Screen.HOME)
          }
        }
      })
}
/**
 * A button for Google Sign-In, styled with a Google logo and text.
 *
 * @param onSignInClick Callback to handle sign-in click events.
 */
@Composable
fun GoogleSignInButton(onSignInClick: () -> Unit) {
  Button(
      modifier = Modifier.padding(8.dp).height(48.dp).testTag("loginButton"),
      onClick = onSignInClick,
      colors =
          ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.background), // Button color
      shape = RoundedCornerShape(50), // Circular edges for the button
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.background),
  ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.width(200.dp)) {
          // Load the Google logo from resources
          Image(
              painter = painterResource(id = R.drawable.google_logo),
              contentDescription = "Google Logo",
              modifier =
                  Modifier.size(30.dp) // Size of the Google logo
                      .padding(end = 8.dp))

          // Text for the button
          val signInText = stringResource(R.string.sign_in_text)
          Text(
              text = signInText,
              style =
                  TextStyle(
                      fontSize = 14.sp,
                      lineHeight = 17.sp,
                      fontWeight = FontWeight(500),
                      color = MaterialTheme.colorScheme.onBackground,
                      textAlign = TextAlign.Center,
                      letterSpacing = 0.25.sp,
                  ))
        }
  }
}
/**
 * Remembers an authentication launcher for handling Google Sign-In results.
 *
 * @param onAuthComplete Callback for successful authentication.
 * @param onAuthError Callback for authentication errors.
 * @param authService The authentication service to handle results.
 */
@Composable
fun rememberAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit,
    authService: AuthService
): ManagedActivityResultLauncher<Intent, ActivityResult> {
  return rememberLauncherForActivityResult(
      contract = ActivityResultContracts.StartActivityForResult()) { result ->
        val activityResultWrapper =
            FirebaseAuthService.ActivityResultWrapper(result.resultCode, result.data)
        authService.handleAuthResult(activityResultWrapper, onAuthComplete, onAuthError)
      }
}
/**
 * A button for skipping the login process and proceeding in offline mode.
 *
 * @param onOfflineClick Callback to handle offline mode selection.
 */
@Composable
fun SkipLoginButton(onOfflineClick: () -> Unit) {
  Button(
      modifier = Modifier.padding(8.dp).height(48.dp).testTag("skipLoginButton"),
      onClick = onOfflineClick,
      colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
      shape = RoundedCornerShape(50),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.background)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.width(200.dp).testTag("skipLoginButton")) {
              Icon(
                  imageVector = Icons.Default.Home,
                  contentDescription = "Offline Mode Icon",
                  modifier = Modifier.size(30.dp).padding(end = 8.dp),
                  tint = MaterialTheme.colorScheme.primary)

              // Text for the button
              val skipLoginText = stringResource(R.string.skip_login_text)
              Text(
                  text = skipLoginText,
                  style =
                      TextStyle(
                          fontSize = 14.sp,
                          lineHeight = 17.sp,
                          fontWeight = FontWeight(500),
                          color = MaterialTheme.colorScheme.onBackground,
                          textAlign = TextAlign.Center,
                          letterSpacing = 0.25.sp,
                      ))
            }
      }
}
