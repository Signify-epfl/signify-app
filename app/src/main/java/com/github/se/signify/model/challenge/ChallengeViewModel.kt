package com.github.se.signify.model.challenge

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.se.signify.model.auth.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class ChallengeMode(val modeName: String) {
  SPRINT("Sprint"),
  CHRONO("Chrono"),
}

open class ChallengeViewModel(
    private val userSession: UserSession,
    private val challengeRepository: ChallengeRepository,
) : ViewModel() {
  private val _challenge = MutableStateFlow<Challenge?>(null)
  val challenge: StateFlow<Challenge?> = _challenge

  private val logTag = "ChallengeViewModel"

  fun sendChallengeRequest(opponentId: String, mode: ChallengeMode, challengeId: String) {
    challengeRepository.sendChallengeRequest(
        userSession.getUserId()!!,
        opponentId,
        mode,
        challengeId,
        onSuccess = { Log.d(logTag, "Challenge request sent successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to send challenge request: ${e.message}") })
  }

  fun deleteChallenge(challengeId: String) {
    challengeRepository.deleteChallenge(
        challengeId,
        onSuccess = { Log.d(logTag, "Challenge deleted successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to delete challenge: ${e.message}") })
  }


  companion object {
    fun factory(
        userSession: UserSession,
        repository: ChallengeRepository,
    ): ViewModelProvider.Factory {
      return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return ChallengeViewModel(userSession, repository) as T
        }
      }
    }
  }
}
