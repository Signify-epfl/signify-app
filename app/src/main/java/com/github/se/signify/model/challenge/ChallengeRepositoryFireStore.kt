package com.github.se.signify.model.challenge

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ChallengeRepositoryFireStore(private val db: FirebaseFirestore) : ChallengeRepository {
  private val collectionPath = "challenges"

  override fun sendChallengeRequest(
      player1Id: String,
      player2Id: String,
      mode: String,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val challenge =
        hashMapOf(
            "challengeId" to challengeId,
            "player1" to player1Id,
            "player2" to player2Id,
            "status" to "pending",
            "round" to 1,
            "mode" to mode,
            "player1Score" to 0,
            "player2Score" to 0,
            "currentGesture" to "",
            "responses" to hashMapOf<String, String>())

    db.collection(collectionPath)
        .document(challengeId)
        .set(challenge, SetOptions.merge())
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
  }

  override fun deleteChallenge(
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    db.collection(collectionPath)
        .document(challengeId)
        .delete()
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
  }
}
