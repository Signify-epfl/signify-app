package com.github.se.signify.model.home.quest

import com.github.se.signify.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreQuestRepository(private val db: FirebaseFirestore) : QuestRepository {

  private val collectionPath = "quests"
  private val videoPathBase = "android.resource://com.github.se.signify/raw/"

  override fun init(onSuccess: () -> Unit) {
    Firebase.auth.addAuthStateListener {
      if (it.currentUser != null) {
        onSuccess()
      }
    }
  }

  override fun getDailyQuest(onSuccess: (List<Quest>) -> Unit, onFailure: (Exception) -> Unit) {
    db.collection(collectionPath).get().addOnCompleteListener { task ->
      if (task.isSuccessful) {
        val quests =
            task.result
                ?.mapNotNull { document -> documentToQuest(document) }
                ?.sortedBy { it.index.toInt() } ?: emptyList() // Sort by index field
        onSuccess(quests)
      } else {
        task.exception?.let { e -> onFailure(e) }
      }
    }
  }

  internal fun documentToQuest(document: DocumentSnapshot): Quest? {
    return try {
      val title = document.getString("title") ?: return null
      val description = document.getString("description") ?: return null
      val index = document.getString("index") ?: return null
      val videoPath = fetchVideo(title)
      Quest(index = index, title = title, description = description, videoPath = videoPath)
    } catch (e: Exception) {
      null
    }
  }

  internal fun fetchVideo(word: String): String {
    val sanitizedWord = word.replace(" ", "").lowercase() // Remove spaces and convert to lowercase
    val videoPath = "$videoPathBase$sanitizedWord"

    // Check if the video path is valid
    return if (isVideoPathValid(sanitizedWord)) videoPath else "$videoPathBase/hello"
  }

  // function to check if the video exists
  internal fun isVideoPathValid(resourceName: String): Boolean {
    return try {
      val resId = R.raw::class.java.getField(resourceName).getInt(null)
      resId != 0 // If the resource ID exists, it's valid
    } catch (e: Exception) {
      false // Resource not found
    }
  }
}
