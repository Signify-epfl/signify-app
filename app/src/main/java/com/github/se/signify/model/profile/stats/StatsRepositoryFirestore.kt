package com.github.se.signify.model.profile.stats

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class StatsRepositoryFirestore(private val db: FirebaseFirestore) : StatsRepository {

  private val collectionPath = "stats"

  private fun getDocument(userId: String) = db.collection(collectionPath).document(userId)

  override fun init(onSuccess: () -> Unit) {
    Firebase.auth.addAuthStateListener {
      if (it.currentUser != null) {
        onSuccess()
      }
    }
  }

  override fun getLettersLearned(
      userId: String,
      onSuccess: (List<Char>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .get()
        .addOnSuccessListener { document ->
          val lettersLearned = document.get("lettersLearned") as? List<*>
          // Convert to List<Char> if the list is not null and contains valid Char elements
          val charList = lettersLearned?.filterIsInstance<Char>() ?: emptyList()
          onSuccess(charList)
        }
        .addOnFailureListener(onFailure)
  }

  override fun getEasyExerciseStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .get()
        .addOnSuccessListener { document ->
          val easy = document.getLong("easyExercise")?.toInt() ?: 0
          onSuccess(easy)
        }
        .addOnFailureListener(onFailure)
  }

  override fun getMediumExerciseStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .get()
        .addOnSuccessListener { document ->
          val medium = document.getLong("mediumExercise")?.toInt() ?: 0
          onSuccess(medium)
        }
        .addOnFailureListener(onFailure)
  }

  override fun getHardExerciseStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .get()
        .addOnSuccessListener { document ->
          val hard = document.getLong("hardExercise")?.toInt() ?: 0
          onSuccess(hard)
        }
        .addOnFailureListener(onFailure)
  }

  override fun getDailyQuestStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .get()
        .addOnSuccessListener { document ->
          val daily = document.getLong("dailyQuest")?.toInt() ?: 0
          onSuccess(daily)
        }
        .addOnFailureListener(onFailure)
  }

  override fun getWeeklyQuestStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .get()
        .addOnSuccessListener { document ->
          val weekly = document.getLong("weeklyQuest")?.toInt() ?: 0
          onSuccess(weekly)
        }
        .addOnFailureListener(onFailure)
  }

  override fun getCompletedChallengeStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .get()
        .addOnSuccessListener { document ->
          val completed = document.getLong("completedChallenge")?.toInt() ?: 0
          onSuccess(completed)
        }
        .addOnFailureListener(onFailure)
  }

  override fun getCreatedChallengeStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .get()
        .addOnSuccessListener { document ->
          val created = document.getLong("createdChallenge")?.toInt() ?: 0
          onSuccess(created)
        }
        .addOnFailureListener(onFailure)
  }

  override fun getWonChallengeStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .get()
        .addOnSuccessListener { document ->
          val won = document.getLong("wonChallenge")?.toInt() ?: 0
          onSuccess(won)
        }
        .addOnFailureListener(onFailure)
  }

  override fun updateLettersLearned(
      userId: String,
      newLetter: Char,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .update("lettersLearned", FieldValue.arrayUnion(newLetter))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener(onFailure)
  }

  override fun updateEasyExerciseStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .update("easyExercise", FieldValue.increment(1))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener(onFailure)
  }

  override fun updateMediumExerciseStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .update("mediumExercise", FieldValue.increment(1))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener(onFailure)
  }

  override fun updateHardExerciseStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .update("hardExercise", FieldValue.increment(1))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener(onFailure)
  }

  override fun updateDailyQuestStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .update("dailyQuest", FieldValue.increment(1))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener(onFailure)
  }

  override fun updateWeeklyQuestStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .update("weeklyQuest", FieldValue.increment(1))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener(onFailure)
  }

  override fun updateCompletedChallengeStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .update("completedChallenge", FieldValue.increment(1))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener(onFailure)
  }

  override fun updateCreatedChallengeStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .update("createdChallenge", FieldValue.increment(1))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener(onFailure)
  }

  override fun updateWonChallengeStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getDocument(userId)
        .update("wonChallenge", FieldValue.increment(1))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener(onFailure)
  }
}
