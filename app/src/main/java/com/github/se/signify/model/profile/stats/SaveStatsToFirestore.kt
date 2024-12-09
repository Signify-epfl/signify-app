package com.github.se.signify.model.profile.stats

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

fun saveStatsToFirestore() {
  val auth = FirebaseAuth.getInstance()
  val db = FirebaseFirestore.getInstance()
  val collectionPath = "stats"
  val logTag = "FireStore"

  val currentUser = auth.currentUser

  if (currentUser != null) {
    val userId = currentUser.email?.split("@")?.get(0) ?: "unknown"

    // Create a default stats object to store in Firestore
    val userStats =
        hashMapOf(
            "lettersLearned" to emptyList<Char>(),
            "easyExercise" to 0,
            "mediumExercise" to 0,
            "hardExercise" to 0,
            "dailyQuest" to 0,
            "weeklyQuest" to 0,
            "completedChallenge" to 0,
            "createdChallenge" to 0,
            "wonChallenge" to 0)

    // Check if the stats document already exists for this user
    val statsDocRef = db.collection(collectionPath).document(userId)

    statsDocRef
        .get()
        .addOnSuccessListener { document ->
          if (!document.exists()) {
            // The stats document does not exist yet, so we add it
            statsDocRef
                .set(userStats, SetOptions.merge())
                .addOnSuccessListener { Log.d(logTag, "User stats added successfully") }
                .addOnFailureListener { e -> Log.e(logTag, "Error adding user stats", e) }
          } else {
            Log.d(logTag, "User stats already exist")
          }
        }
        .addOnFailureListener { e -> Log.e(logTag, "Error checking user stats", e) }
  } else {
    Log.e(logTag, "User not logged in")
  }
}
