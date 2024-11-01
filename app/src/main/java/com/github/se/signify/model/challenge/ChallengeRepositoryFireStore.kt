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

  override fun acceptChallenge(
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    db.collection(collectionPath)
        .document(challengeId)
        .update("status", "active")
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
  }

  override fun submitGesture(
      challengeId: String,
      userId: String,
      gesture: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    db.collection(collectionPath)
        .document(challengeId)
        .update("responses.$userId", gesture)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
  }

  override fun getChallengeUpdates(
      challengeId: String,
      onUpdate: (Challenge) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    db.collection(collectionPath).document(challengeId).addSnapshotListener { documentSnapshot, e ->
      if (e != null) {
        onFailure(e)
        return@addSnapshotListener
      }

      if (documentSnapshot != null && documentSnapshot.exists()) {
        val challenge = documentSnapshot.toObject(Challenge::class.java)
        if (challenge != null) {
          onUpdate(challenge)
        }
      }
    }
  }
}
