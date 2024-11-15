package com.github.se.signify.model.challenge

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class ChallengeMode(val modeName: String) {
  SPRINT("Sprint"),
  CHRONO("Chrono"),
}

open class ChallengeViewModel(private val repository: ChallengeRepository) : ViewModel() {
  private val _challenge = MutableStateFlow<Challenge?>(null)
  val challenge: StateFlow<Challenge?> = _challenge

  private val logTag = "ChallengeViewModel"

  fun sendChallengeRequest(
      player1Id: String,
      player2Id: String,
      mode: ChallengeMode,
      challengeId: String
  ) {
    repository.sendChallengeRequest(
        player1Id,
        player2Id,
        mode,
        challengeId,
        onSuccess = { Log.d(logTag, "Challenge request sent successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to send challenge request: ${e.message}") })
  }

  fun deleteChallenge(challengeId: String) {
    repository.deleteChallenge(
        challengeId,
        onSuccess = { Log.d(logTag, "Challenge deleted successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to delete challenge: ${e.message}") })
  }

  companion object {
    fun factory(repository: ChallengeRepository): ViewModelProvider.Factory {
      return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return ChallengeViewModel(repository) as T
        }
      }
    }
  }
}
