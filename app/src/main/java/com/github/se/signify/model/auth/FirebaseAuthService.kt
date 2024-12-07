package com.github.se.signify.model.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.ui.res.stringResource
import com.github.se.signify.BaseApplication
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class FirebaseAuthService : AuthService {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    override suspend fun signInWithGoogle(idToken: String): Boolean {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = FirebaseAuth.getInstance().signInWithCredential(credential).await()
        return authResult.user != null
    }

    override suspend fun signOut(): Boolean {
        FirebaseAuth.getInstance().signOut()
        return true
    }

    override fun getCurrentUser(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }

    override fun handleAuthResult(
        result: ActivityResult,
        onAuthComplete: (AuthResult) -> Unit,
        onAuthError: (ApiException) -> Unit
    ) {
        if (result.resultCode == android.app.Activity.RESULT_OK && result.data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    val credential = GoogleAuthProvider.getCredential(idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnSuccessListener { authResult ->
                            onAuthComplete(authResult)
                        }
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

    private fun initializeGoogleSignInClient(context: Context, token: String) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context,gso)
    }
    override fun isMocked(): Boolean = false
}