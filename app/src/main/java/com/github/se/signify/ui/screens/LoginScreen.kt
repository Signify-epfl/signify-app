package com.github.se.signify.ui.screens

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.ui.navigation.NavigationActions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(navigationActions: NavigationActions) {
  val context = LocalContext.current

  val launcher =
      rememberFirebaseAuthLauncher(
          onAuthComplete = { result ->
            Log.d("SignInScreen", "User signed in: ${result.user?.displayName}")
            Toast.makeText(context, "Login successful!", Toast.LENGTH_LONG).show()
            navigationActions.navigateTo("Challenge")
          },
          onAuthError = {
            Log.e("SignInScreen", "Failed to sign in: ${it.statusCode}")
            Toast.makeText(context, "Login Failed!", Toast.LENGTH_LONG).show()
          })

  val token = stringResource(id = R.string.default_web_client_id)

  // Gradient brush for background (to be updated with startY and endY)
  val gradient =
      Brush.verticalGradient(
          colors =
              listOf(
                  Color(0xFFFFFFFF),
                  Color(0xFFFFFFFF), // White at the top
                  Color(0xFFFFFFFF),
                  Color(0xFF05A9FB) // Light blue at the bottom
                  ))
  // The main container for the screen
  Scaffold(
      modifier = Modifier.fillMaxSize(),
      content = { padding ->
        Column(
            modifier = Modifier.fillMaxSize().background(brush = gradient),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
          // App Logo Image
          Image(
              painter = painterResource(id = R.drawable.vector),
              contentDescription = "image description",
              // contentScale = ContentScale.None,
              modifier =
                  Modifier.padding(3.dp)
                      .width(139.dp)
                      .height(81.dp)
                      .background(color = Color(0xFFFFFFFF)))

          Spacer(modifier = Modifier.height(70.dp))

          // Welcome Text

          Text(
              text = "Signify is what you need to communicate with deaf and hard of hearing people",
              style =
                  TextStyle(
                      fontSize = 32.sp,
                      lineHeight = 30.sp,
                      // fontFamily = FontFamily(Font(R.font.roboto)),
                      fontWeight = FontWeight(400),
                      color = Color(0xFF05A9FB),
                      textAlign = TextAlign.Center,
                      letterSpacing = 0.25.sp,
                  ),
              modifier = Modifier.width(250.dp).height(200.dp))

          Spacer(modifier = Modifier.height(120.dp))

          // Authenticate With Google Button
          GoogleSignInButton(
              onSignInClick = {
                val gso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(token)
                        .requestEmail()
                        .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                launcher.launch(googleSignInClient.signInIntent)
              })
        }
      })
}

@Composable
fun GoogleSignInButton(onSignInClick: () -> Unit) {
  Button(
      onClick = onSignInClick,
      colors = ButtonDefaults.buttonColors(containerColor = Color.White), // Button color
      shape = RoundedCornerShape(50), // Circular edges for the button
      border = BorderStroke(1.dp, Color.White),
      modifier = Modifier.padding(8.dp).height(48.dp)) {
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
              Text(
                  text = "Sign in with Google",
                  style =
                      TextStyle(
                          fontSize = 14.sp,
                          lineHeight = 17.sp,
                          fontWeight = FontWeight(500),
                          color = Color(0xFF3C4043),
                          textAlign = TextAlign.Center,
                          letterSpacing = 0.25.sp,
                      ))
            }
      }
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
  val scope = rememberCoroutineScope()
  return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      result ->
    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
    try {
      val account = task.getResult(ApiException::class.java)!!
      val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
      scope.launch {
        val authResult = Firebase.auth.signInWithCredential(credential).await()
        onAuthComplete(authResult)
      }
    } catch (e: ApiException) {
      onAuthError(e)
    }
  }
}
