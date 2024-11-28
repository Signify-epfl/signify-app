package com.github.se.signify.model.challenge

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.se.signify.model.auth.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


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

    private var currentChallenge: Challenge? = null

    // Start a new challenge game in "chrono" mode
    fun startGame(challengeId: String) {
        viewModelScope.launch {
            challengeRepository.getChallengeById(challengeId, onSuccess = { challenge ->
                if (challenge.mode == "chrono") {
                    currentChallenge = challenge.copy(
                        gameStatus = "in_progress"
                    )
                    currentChallenge?.let { updatedChallenge ->
                        challengeRepository.updateChallenge(
                            updatedChallenge,
                            onSuccess = {
                                // Successfully started the game
                            },
                            onFailure = {
                                // Handle failure during game start
                            }
                        )
                    }
                }
            }, onFailure = {
                // Handle failure during challenge retrieval
            })
        }
    }

    // Record the time taken by a player asynchronously
    fun getChallengeById(challengeId: String, onSuccess: (Challenge) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            challengeRepository.getChallengeById(challengeId, onSuccess, onFailure)
        }
    }

    fun updateChallenge(updatedChallenge: Challenge, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            challengeRepository.updateChallenge(updatedChallenge, onSuccess, onFailure)
        }
    }

    fun recordPlayerTime(challengeId: String, playerId: String, timeTaken: Long, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            challengeRepository.recordPlayerTime(challengeId, playerId, timeTaken, onSuccess, onFailure)
        }
    }

    // Method to get the current challenge's status (e.g., for UI)
    fun getCurrentChallengeStatus(): String? {
        return currentChallenge?.gameStatus
    }

    // Method to check if it's the player's turn to play
    fun isPlayerTurn(playerId: String): Boolean {
        val challenge = currentChallenge ?: return false
        return if (playerId == challenge.player1) {
            !challenge.player1RoundCompleted[challenge.round - 1]
        } else {
            !challenge.player2RoundCompleted[challenge.round - 1]
        }
    }

    // Helper to get the word for the current round (for display purposes)
    fun getCurrentRoundWord(): String? {
        val challenge = currentChallenge ?: return null
        return if (challenge.round <= challenge.roundWords.size) {
            challenge.roundWords[challenge.round - 1]  // Index based on 1-based round
        } else {
            null
        }
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
