package com.github.se.signify.model.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class FirebaseAuthService : AuthService {
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
}
