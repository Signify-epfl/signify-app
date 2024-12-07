package com.github.se.signify.model.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.se.signify.model.auth.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StatsViewModel(
    private val userSession: UserSession,
    private val repository: StatsRepository,
) : ViewModel() {

  private val _lettersLearned = MutableStateFlow<List<Char>>(emptyList())
  val lettersLearned: StateFlow<List<Char>> = _lettersLearned

  private val _easy = MutableStateFlow(0)
  val easy: StateFlow<Int> = _easy

  private val _medium = MutableStateFlow(0)
  val medium: StateFlow<Int> = _medium

  private val _hard = MutableStateFlow(0)
  val hard: StateFlow<Int> = _hard

  private val _daily = MutableStateFlow(0)
  val daily: StateFlow<Int> = _daily

  private val _weekly = MutableStateFlow(0)
  val weekly: StateFlow<Int> = _weekly

  private val _completed = MutableStateFlow(0)
  val completed: StateFlow<Int> = _completed

  private val _created = MutableStateFlow(0)
  val created: StateFlow<Int> = _created

  private val _won = MutableStateFlow(0)
  val won: StateFlow<Int> = _won

  private val logTag = "StatsViewModel"

  init {
    repository.init {}
  }

  companion object {
    fun factory(userSession: UserSession, repository: StatsRepository): ViewModelProvider.Factory {
      return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return StatsViewModel(userSession, repository) as T
        }
      }
    }
  }

  private fun logSuccess(m: String) = Log.d(logTag, m)

  private fun logError(m: String, e: Exception) = Log.e(logTag, "$m: ${e.message}")

  fun getLettersLearned() {
    repository.getLettersLearned(
        userSession.getUserId()!!,
        onSuccess = { letters -> _lettersLearned.value = letters },
        onFailure = { e -> logError("Error fetching letters learned:", e) })
  }

  fun getEasyExerciseStats() {
    repository.getEasyExerciseStats(
        userSession.getUserId()!!,
        onSuccess = { easyStats -> _easy.value = easyStats },
        onFailure = { e -> logError("Error fetching easy exercise stats:", e) })
  }

  fun getMediumExerciseStats() {
    repository.getMediumExerciseStats(
        userSession.getUserId()!!,
        onSuccess = { mediumStats -> _medium.value = mediumStats },
        onFailure = { e -> logError("Error fetching medium exercise stats:", e) })
  }

  fun getHardExerciseStats() {
    repository.getHardExerciseStats(
        userSession.getUserId()!!,
        onSuccess = { hardStats -> _hard.value = hardStats },
        onFailure = { e -> logError("Error fetching hard exercise stats:", e) })
  }

  fun getDailyQuestStats() {
    repository.getDailyQuestStats(
        userSession.getUserId()!!,
        onSuccess = { dailyQuest -> _daily.value = dailyQuest },
        onFailure = { e -> logError("Error fetching daily quest stats:", e) })
  }

  fun getWeeklyQuestStats() {
    repository.getWeeklyQuestStats(
        userSession.getUserId()!!,
        onSuccess = { weeklyQuest -> _weekly.value = weeklyQuest },
        onFailure = { e -> logError("Error fetching weekly quest stats:", e) })
  }

  fun getCompletedChallengeStats() {
    repository.getCompletedChallengeStats(
        userSession.getUserId()!!,
        onSuccess = { completedChallenge -> _completed.value = completedChallenge },
        onFailure = { e -> logError("Error fetching completed challenge Stats:", e) })
  }

  fun getCreatedChallengeStats() {
    repository.getCreatedChallengeStats(
        userSession.getUserId()!!,
        onSuccess = { createdChallenge -> _created.value = createdChallenge },
        onFailure = { e -> logError("Error fetching created challenge Stats:", e) })
  }

  fun getWonChallengeStats() {
    repository.getWonChallengeStats(
        userSession.getUserId()!!,
        onSuccess = { wonChallenge -> _won.value = wonChallenge },
        onFailure = { e -> logError("Error fetching won challenge Stats:", e) })
  }

  fun updateLettersLearned(newLetter: Char) {
    repository.updateLettersLearned(
        userSession.getUserId()!!,
        newLetter,
        onSuccess = { logSuccess("Letterss learned updated successfully.") },
        onFailure = { e -> logError("Error updating letters learned:", e) })
  }

  fun updateEasyExerciseStats() {
    repository.updateEasyExerciseStats(
        userSession.getUserId()!!,
        onSuccess = { logSuccess("Easy exercise stats updated successfully.") },
        onFailure = { e -> logError("Error updating easy exercise stats:", e) })
  }

  fun updateMediumExerciseStats() {
    repository.updateMediumExerciseStats(
        userSession.getUserId()!!,
        onSuccess = { logSuccess("Medium exercise stats updated successfully.") },
        onFailure = { e -> logError("Error updating medium exercise stats:", e) })
  }

  fun updateHardExerciseStats() {
    repository.updateHardExerciseStats(
        userSession.getUserId()!!,
        onSuccess = { logSuccess("Hard exercise stats updated successfully.") },
        onFailure = { e -> logError("Error updating hard exercise stats:", e) })
  }

  fun updateDailyQuestStats() {
    repository.updateDailyQuestStats(
        userSession.getUserId()!!,
        onSuccess = { logSuccess("Daily quest stats updated successfully.") },
        onFailure = { e -> logError("Error updating daily quest stats:", e) })
  }

  fun updateWeeklyQuestStats() {
    repository.updateWeeklyQuestStats(
        userSession.getUserId()!!,
        onSuccess = { logSuccess("Weekly quest stats updated successfully.") },
        onFailure = { e -> logError("Error updating weekly quest stats:", e) })
  }

  fun updateCompletedChallengeStats() {
    repository.updateCompletedChallengeStats(
        userSession.getUserId()!!,
        onSuccess = { logSuccess("Completed challenge stats updated successfully.") },
        onFailure = { e -> logError("Error updating completed challenge stats:", e) })
  }

  fun updateCreatedChallengeStats() {
    repository.updateCreatedChallengeStats(
        userSession.getUserId()!!,
        onSuccess = { logSuccess("Created challenge stats updated successfully.") },
        onFailure = { e -> logError("Error updating created challenge stats:", e) })
  }

  fun updateWonChallengeStats() {
    repository.updateWonChallengeStats(
        userSession.getUserId()!!,
        onSuccess = { logSuccess("Won challenge stats updated successfully.") },
        onFailure = { e -> logError("Error updating won challenge stats:", e) })
  }
}
