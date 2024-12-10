package com.github.se.signify.model.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult

interface AuthService {
  suspend fun signInWithGoogle(idToken: String): Boolean

  suspend fun signOut(): Boolean

  fun getCurrentUser(): String?

  fun handleAuthResult(
      result: FirebaseAuthService.ActivityResultWrapper,
      onAuthComplete: (AuthResult) -> Unit,
      onAuthError: (ApiException) -> Unit
  )

  fun getSignInIntent(context: Context, token: String): Intent

  fun isMocked(): Boolean
}
