package com.github.se.signify.model.challenge

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChallengeViewModel(private val repository: ChallengeRepository) : ViewModel() {
    private val _challenge = MutableStateFlow<Challenge?>(null)
    val challenge: StateFlow<Challenge?> = _challenge

    private val logTag = "ChallengeViewModel"

    fun sendChallengeRequest(player1Id: String, player2Id: String, mode: String,challengeId: String) {
        repository.sendChallengeRequest(
            player1Id,
            player2Id,
            mode,
            challengeId,
            onSuccess = { Log.d(logTag, "Challenge request sent successfully.") },
            onFailure = { e -> Log.e(logTag, "Failed to send challenge request: ${e.message}") }
        )
    }

    fun acceptChallenge(challengeId: String) {
        repository.acceptChallenge(
            challengeId,
            onSuccess = { Log.d(logTag, "Challenge accepted successfully.") },
            onFailure = { e -> Log.e(logTag, "Failed to accept challenge: ${e.message}") }
        )
    }

    fun submitGesture(challengeId: String, userId: String, gesture: String) {
        repository.submitGesture(
            challengeId,
            userId,
            gesture,
            onSuccess = { Log.d(logTag, "Gesture submitted successfully.") },
            onFailure = { e -> Log.e(logTag, "Failed to submit gesture: ${e.message}") }
        )
    }

    fun getChallengeUpdates(challengeId: String) {
        repository.getChallengeUpdates(
            challengeId,
            onUpdate = { challenge -> _challenge.value = challenge },
            onFailure = { e -> Log.e(logTag, "Failed to get challenge updates: ${e.message}") }
        )
    }

    // create factory
    companion object {
        val Factory: ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ChallengeViewModel(ChallengeRepositoryFireStore(FirebaseFirestore.getInstance())) as T
                }
            }
    }
}
