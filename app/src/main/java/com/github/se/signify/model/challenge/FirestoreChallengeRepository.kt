package com.github.se.signify.model.challenge

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreChallengeRepository(private val db: FirebaseFirestore) : ChallengeRepository {
  private val collectionPath = "challenges"

  override fun sendChallengeRequest(
      player1Id: String,
      player2Id: String,
      mode: ChallengeMode,
      challengeId: String,
      roundWords: List<String>,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {

    val challenge =
        Challenge(
            challengeId = challengeId,
            player1 = player1Id,
            player2 = player2Id,
            mode = mode.name,
            round = 1,
            roundWords = roundWords,
            player1Times = mutableListOf(),
            player2Times = mutableListOf(),
            player1RoundCompleted = mutableListOf(false, false, false),
            player2RoundCompleted = mutableListOf(false, false, false),
            gameStatus = "not_started")

    val batch = db.batch()
    val challengeRef = db.collection(collectionPath).document(challengeId)
    batch.set(challengeRef, challenge, SetOptions.merge())

    val player1ChallengeRef =
        db.collection("users").document(player1Id).collection("challenges").document(challengeId)
    val player2ChallengeRef =
        db.collection("users").document(player2Id).collection("challenges").document(challengeId)
    batch.set(player1ChallengeRef, challenge, SetOptions.merge())
    batch.set(player2ChallengeRef, challenge, SetOptions.merge())
    batch
        .commit()
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { exception -> onFailure(exception) }
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

  override fun getChallengeById(
      challengeId: String,
      onSuccess: (Challenge) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    db.collection("challenges")
        .document(challengeId)
        .get()
        .addOnSuccessListener { document ->
          val challenge = document.toObject(Challenge::class.java)
          if (challenge != null) {
            onSuccess(challenge)
          } else {
            onFailure(Exception("Challenge not found"))
          }
        }
        .addOnFailureListener { onFailure(it) }
  }

  override fun getChallenges(
      challengeIds: List<ChallengeId>,
      onSuccess: (List<Challenge>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val challenges = mutableListOf<Challenge>()
    val tasks =
        challengeIds.map { challengeId ->
          db.collection(collectionPath).document(challengeId).get()
        }

    Tasks.whenAllComplete(tasks)
        .addOnSuccessListener { taskResults ->
          taskResults.forEach { task ->
            if (task.isSuccessful) {
              val document = task.result as DocumentSnapshot
              val challenge = document.toObject(Challenge::class.java)
              if (challenge != null) {
                challenges.add(challenge)
              }
            }
          }
          onSuccess(challenges)
        }
        .addOnFailureListener { e -> onFailure(e) }
  }

  override fun updateChallenge(
      updatedChallenge: Challenge,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    db.collection("challenges")
        .document(updatedChallenge.challengeId)
        .set(updatedChallenge)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
  }

  override fun recordPlayerTime(
      challengeId: String,
      playerId: String,
      timeTaken: Long,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    assert(timeTaken > 0)
    db.collection("challenges")
        .document(challengeId)
        .update("playerTime", timeTaken)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
  }

  override fun updateWinner(
      challengeId: String,
      winnerId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val challengeRef = db.collection(collectionPath).document(challengeId)

    challengeRef
        .update("winner", winnerId)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { exception -> onFailure(exception) }
  }
}
