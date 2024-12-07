package com.github.se.signify.model.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MockAuthService : AuthService {
    override suspend fun signInWithGoogle(idToken: String): Boolean {
        return true
    }

    override suspend fun signOut(): Boolean {
        return true
    }
    override fun getSignInIntent(context: Context, token: String):Intent{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .setAccountName("mock-token")
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context,gso)
        return googleSignInClient.signInIntent
    }

    override fun isMocked(): Boolean = true

    override fun getCurrentUser(): String {
        return "mock-token@example.com"
    }

    override fun handleAuthResult(
        result: ActivityResult,
        onAuthComplete: (AuthResult) -> Unit,
        onAuthError: (ApiException) -> Unit
    ) {
            val idToken = "mock-token"
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnSuccessListener { authResult ->
                    Log.d("MockAuthService", "Mock sign-in successful")
                    onAuthComplete(authResult)
                }
                .addOnFailureListener { exception ->
                    Log.e("MockAuthService", "Mock sign-in failed", exception)
                    if (exception is ApiException) {
                        onAuthError(exception)
                    }
        }
    }

}