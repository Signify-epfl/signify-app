package com.github.se.signify.model.auth

import com.google.firebase.auth.FirebaseAuth

class FirebaseUserSession : UserSession {
    override fun getUserId(): String {
        // TODO: Fix this to return a unique userId.
        return FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: "unknown"
    }

    override suspend fun login(): Boolean {
        TODO("Login through Firebase. This is currently hardcoded in the login screen.")
    }

    override suspend fun logout() {
        return FirebaseAuth.getInstance().signOut()
    }

    override fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }
}