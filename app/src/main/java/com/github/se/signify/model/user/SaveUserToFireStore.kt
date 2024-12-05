package com.github.se.signify.model.user

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.time.LocalDate

fun saveUserToFireStore() {
  val auth = FirebaseAuth.getInstance()
  val db = FirebaseFirestore.getInstance()
  val collectionPath = "users"
  val logTag = "FireStore"

  val currentUser = auth.currentUser

  if (currentUser != null) {
    val email = currentUser.email ?: "unknown"
    val userId = email.split("@")[0]
    val name = currentUser.displayName

    // Create a User object to store in Firestore
    val user =
        hashMapOf(
            "uid" to userId,
            "email" to email,
            "name" to name,
            "friends" to emptyList<String>(), // Initialize the friends list
            "friendRequests" to emptyList<String>(), // Initialize the friend requests list
            "ongoingChallenges" to emptyList<String>(), // Initialize the ongoing challenges list
            "pastChallenges" to emptyList<String>(), // Initialize the past challenges list
            "lastLoginDate" to LocalDate.now().toString(),
            "currentStreak" to 1L,
            "highestStreak" to 1L)

    // Check if the user already exists in FireStore
    val usersCollection = db.collection(collectionPath)
    val userDocRef = usersCollection.document(userId)

    userDocRef
        .get()
        .addOnSuccessListener { document ->
          if (!document.exists()) {
            // The user does not exist yet, so we add them
            userDocRef
                .set(user, SetOptions.merge())
                .addOnSuccessListener { Log.d(logTag, "User added successfully") }
                .addOnFailureListener { e -> Log.e(logTag, "Error adding user", e) }
          } else {
            Log.d(logTag, "User already exists")
          }
        }
        .addOnFailureListener { e -> Log.e(logTag, "Error checking user", e) }
  } else {
    Log.e(logTag, "User not logged in")
  }
}
