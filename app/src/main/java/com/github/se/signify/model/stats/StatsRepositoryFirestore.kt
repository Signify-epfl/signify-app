package com.github.se.signify.model.stats

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class StatsRepositoryFirestore(private val db: FirebaseFirestore): StatsRepository {

    private val collectionPath = "stats"

    private fun getDocument(userId: String) = db.collection(collectionPath).document(userId)

    override fun init(onSuccess: () -> Unit) {
        Firebase.auth.addAuthStateListener {
            if (it.currentUser != null) {
                onSuccess()
            }
        }
    }

    override fun getDays(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .get()
            .addOnSuccessListener { document ->
                val days = document.getLong("days")?.toInt() ?: 0
                onSuccess(days)
            }
            .addOnFailureListener(onFailure)
    }

    override fun getLettersLearned(
        userId: String,
        onSuccess: (List<Char>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId).get()
            .addOnSuccessListener { document ->
                val lettersLearned = document.get("lettersLearned") as? List<*>
                // Convert to List<Char> if the list is not null and contains valid Char elements
                val charList = lettersLearned?.filterIsInstance<Char>() ?: emptyList()
                onSuccess(charList)
            }
            .addOnFailureListener(onFailure)
    }

    override fun getExerciseStatsEasy(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .get()
            .addOnSuccessListener { document ->
                val easy = document.getLong("exerciseStats.easy")?.toInt() ?: 0
                onSuccess(easy)
            }
            .addOnFailureListener(onFailure)
    }

    override fun getExerciseStatsMedium(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .get()
            .addOnSuccessListener { document ->
                val medium = document.getLong("exerciseStats.medium")?.toInt() ?: 0
                onSuccess(medium)
            }
            .addOnFailureListener(onFailure)
    }

    override fun getExerciseStatsHard(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .get()
            .addOnSuccessListener { document ->
                val hard = document.getLong("exerciseStats.hard")?.toInt() ?: 0
                onSuccess(hard)
            }
            .addOnFailureListener(onFailure)
    }

    override fun getQuestStatsDaily(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .get()
            .addOnSuccessListener { document ->
                val daily = document.getLong("questStats.daily")?.toInt() ?: 0
                onSuccess(daily)
            }
            .addOnFailureListener(onFailure)
    }

    override fun getQuestStatsWeekly(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .get()
            .addOnSuccessListener { document ->
                val weekly = document.getLong("questStats.weekly")?.toInt() ?: 0
                onSuccess(weekly)
            }
            .addOnFailureListener(onFailure)
    }

    override fun getChallengeStatsCompleted(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .get()
            .addOnSuccessListener { document ->
                val completed = document.getLong("challengeStats.completed")?.toInt() ?: 0
                onSuccess(completed)
            }
            .addOnFailureListener(onFailure)
    }

    override fun getChallengeStatsCreated(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .get()
            .addOnSuccessListener { document ->
                val created = document.getLong("challengeStats.created")?.toInt() ?: 0
                onSuccess(created)
            }
            .addOnFailureListener(onFailure)
    }

    override fun updateDays(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .update("days", FieldValue.increment(1))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    override fun resetDays(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .update("days", 0)
            .addOnSuccessListener { onSuccess() }
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

    override fun updateExerciseStatsEasy(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .update("exerciseStats.easy", FieldValue.increment(1))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    override fun updateExerciseStatsMedium(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .update("exerciseStats.medium", FieldValue.increment(1))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    override fun updateExerciseStatsHard(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .update("exerciseStats.hard", FieldValue.increment(1))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    override fun updateQuestStatsDaily(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .update("questStats.daily", FieldValue.increment(1))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    override fun updateQuestStatsWeekly(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .update("questStats.weekly", FieldValue.increment(1))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    override fun updateChallengeStatsCompleted(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .update("challengeStats.completed", FieldValue.increment(1))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }

    override fun updateChallengeStatsCreated(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        getDocument(userId)
            .update("challengeStats.created", FieldValue.increment(1))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onFailure)
    }
}
