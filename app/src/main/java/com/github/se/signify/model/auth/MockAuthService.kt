@file:Suppress("DEPRECATION")

package com.github.se.signify.model.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult

class MockAuthService : AuthService {
  override suspend fun signInWithGoogle(idToken: String): Boolean {
    return true
  }

  override suspend fun signOut(): Boolean {
    return true
  }

  override fun getSignInIntent(context: Context, token: String): Intent {
    val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .setAccountName("mock-token")
            .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    return googleSignInClient.signInIntent
  }

  override fun isMocked(): Boolean = true

  override fun getCurrentUser(): String {
    return "mock-token@example.com"
  }

  override fun handleAuthResult(
      result: FirebaseAuthService.ActivityResultWrapper,
      onAuthComplete: (AuthResult) -> Unit,
      onAuthError: (ApiException) -> Unit
  ) {
    // Does nothing special as it needs to be used by FirebaseAuthService which is not the case of a
    // MockedAuthService
  }
}
