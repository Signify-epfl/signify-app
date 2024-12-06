package com.github.se.signify.model.quest

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class QuestRepositoryFireStore(private val db: FirebaseFirestore) : QuestRepository {

  private val collectionPath = "quests"

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
      val videoPath = "android.resource://com.github.se.signify/raw/thankyou"
      Quest(index = index, title = title, description = description, videoPath = videoPath)
    } catch (e: Exception) {
      null
    }
  }
}
