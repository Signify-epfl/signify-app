@file:Suppress("DEPRECATION")

package com.github.se.signify.model.authentication

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class FirebaseAuthService(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val googleSignInHelper: (Intent) -> Task<GoogleSignInAccount> = { intent ->
      GoogleSignIn.getSignedInAccountFromIntent(intent)
    },
    private val credentialProvider: (String, String?) -> AuthCredential = { idToken, secret ->
      GoogleAuthProvider.getCredential(idToken, secret)
    }
) : AuthService {
  lateinit var googleSignInClient: GoogleSignInClient

  override suspend fun signInWithGoogle(idToken: String): Boolean {
    val credential = credentialProvider(idToken, null)
    val authResult = firebaseAuth.signInWithCredential(credential).await()
    return authResult.user != null
  }

  override suspend fun signOut(): Boolean {
    firebaseAuth.signOut()
    return true
  }

  override fun getCurrentUser(): String? {
    return firebaseAuth.currentUser?.email
  }

  override fun handleAuthResult(
      result: ActivityResultWrapper,
      onAuthComplete: (AuthResult) -> Unit,
      onAuthError: (ApiException) -> Unit
  ) {
    if (result.resultCode == android.app.Activity.RESULT_OK && result.data != null) {
      val task = googleSignInHelper(result.data)
      try {
        val account = task.getResult(ApiException::class.java)
        val idToken = account?.idToken
        if (idToken != null) {
          val credential = credentialProvider(idToken, null)
          firebaseAuth
              .signInWithCredential(credential)
              .addOnSuccessListener { authResult -> onAuthComplete(authResult) }
              .addOnFailureListener { exception ->
                if (exception is ApiException) {
                  onAuthError(exception)
                } else {
                  Log.e("FirebaseAuthService", "Unexpected error", exception)
                }
              }
        } else {
          Log.e("FirebaseAuthService", "idToken is null")
          onAuthError(ApiException(Status.RESULT_INTERNAL_ERROR))
        }
      } catch (e: ApiException) {
        Log.e("FirebaseAuthService", "Google Sign-In failed", e)
        onAuthError(e)
      }
    } else {
      Log.e("FirebaseAuthService", "Invalid ActivityResult")
      onAuthError(ApiException(Status.RESULT_INTERNAL_ERROR))
    }
  }

  override fun getSignInIntent(context: Context, token: String): Intent {
    if (!this::googleSignInClient.isInitialized) {
      initializeGoogleSignInClient(context, token)
    }
    return googleSignInClient.signInIntent
  }

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun initializeGoogleSignInClient(context: Context, token: String) {
    val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .build()
    googleSignInClient = GoogleSignIn.getClient(context, gso)
  }

  override fun isMocked(): Boolean = false

  class ActivityResultWrapper(val resultCode: Int, val data: Intent?)
}
