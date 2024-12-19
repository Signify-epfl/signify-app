package com.github.se.signify.model.challenge

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.se.signify.model.authentication.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class ChallengeMode(val modeName: String) {
  SPRINT("Sprint"),
  CHRONO("Chrono"),
}

open class ChallengeViewModel(
    private val userSession: UserSession,
    private val repository: ChallengeRepository,
) : ViewModel() {
  private val _challenges = MutableStateFlow<List<Challenge>>(emptyList())
  val challenges: StateFlow<List<Challenge>> = _challenges

  private val logTag = "ChallengeViewModel"

  fun sendChallengeRequest(
      friendId: String,
      mode: ChallengeMode,
      challengeId: ChallengeId,
      roundWords: List<String>
  ) {
    repository.sendChallengeRequest(
        player1Id = userSession.getUserId()!!,
        player2Id = friendId,
        mode = mode,
        challengeId = challengeId,
        roundWords = roundWords,
        onSuccess = { Log.d("CreateAChallenge", "Challenge created successfully") },
        onFailure = { e -> Log.e("CreateAChallenge", "Failed to create challenge: ${e.message}") })
  }

  fun deleteChallenge(challengeId: ChallengeId) {
    repository.deleteChallenge(
        challengeId,
        onSuccess = { Log.d(logTag, "Challenge deleted successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to delete challenge: ${e.message}") })
  }

  fun getChallenges(challengeIds: List<ChallengeId>) {
    repository.getChallenges(
        challengeIds,
        onSuccess = { _challenges.value = it },
        onFailure = { e -> Log.e(logTag, "Failed to get challenges: ${e.message}") })
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
