package com.github.se.signify.model.home.feedback

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FeedbackRepositoryFireStore(private val db: FirebaseFirestore) : FeedbackRepository {

  private val collectionPath = "feedback"

  override fun saveFeedback(
      uid: String,
      type: String,
      title: String,
      description: String,
      rating: Int,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val challenge =
        hashMapOf(
            "uid" to uid,
            "type" to type,
            "title" to title,
            "description" to description,
            "rating" to rating)

    db.collection(collectionPath)
        .document(title)
        .set(challenge, SetOptions.merge())
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
  }
}
