package com.github.se.signify.model.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StatsViewModel(private val repository: StatsRepository) : ViewModel() {

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

  private val logTag = "StatsViewModel"

  init {
    repository.init {}
  }

  fun factory(repository: StatsRepository): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
      @Suppress("UNCHECKED_CAST")
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatsViewModel(repository) as T
      }
    }
  }

  private fun logSuccess(m: String) = Log.d(logTag, m)

  private fun logError(m: String, e: Exception) = Log.e(logTag, "$m: ${e.message}")

  fun getLettersLearned(userId: String) {
    repository.getLettersLearned(
        userId,
        onSuccess = { letters -> _lettersLearned.value = letters },
        onFailure = { e -> logError("Error fetching letters learned:", e) })
  }

  fun getEasyExerciseStats(userId: String) {
    repository.getEasyExerciseStats(
        userId,
        onSuccess = { easyStats -> _easy.value = easyStats },
        onFailure = { e -> logError("Error fetching easy exercise stats:", e) })
  }

  fun getMediumExerciseStats(userId: String) {
    repository.getMediumExerciseStats(
        userId,
        onSuccess = { mediumStats -> _medium.value = mediumStats },
        onFailure = { e -> logError("Error fetching medium exercise stats:", e) })
  }

  fun getHardExerciseStats(userId: String) {
    repository.getHardExerciseStats(
        userId,
        onSuccess = { hardStats -> _hard.value = hardStats },
        onFailure = { e -> logError("Error fetching hard exercise stats:", e) })
  }

  fun getDailyQuestStats(userId: String) {
    repository.getDailyQuestStats(
        userId,
        onSuccess = { dailyQuest -> _daily.value = dailyQuest },
        onFailure = { e -> logError("Error fetching daily quest stats:", e) })
  }

  fun getWeeklyQuestStats(userId: String) {
    repository.getWeeklyQuestStats(
        userId,
        onSuccess = { weeklyQuest -> _weekly.value = weeklyQuest },
        onFailure = { e -> logError("Error fetching weekly quest stats:", e) })
  }

  fun getCompletedChallengeStats(userId: String) {
    repository.getCompletedChallengeStats(
        userId,
        onSuccess = { completedChallenge -> _completed.value = completedChallenge },
        onFailure = { e -> logError("Error fetching completed challenge Stats:", e) })
  }

  fun getCreatedChallengeStats(userId: String) {
    repository.getCreatedChallengeStats(
        userId,
        onSuccess = { createdChallenge -> _created.value = createdChallenge },
        onFailure = { e -> logError("Error fetching created challenge Stats:", e) })
  }

  fun updateLettersLearned(userId: String, newLetter: Char) {
    repository.updateLettersLearned(
        userId,
        newLetter,
        onSuccess = { logSuccess("Letterss learned updated successfully.") },
        onFailure = { e -> logError("Error updating letters learned:", e) })
  }

  fun updateEasyExerciseStats(userId: String) {
    repository.updateEasyExerciseStats(
        userId,
        onSuccess = { logSuccess("Easy exercise stats updated successfully.") },
        onFailure = { e -> logError("Error updating easy exercise stats:", e) })
  }

  fun updateMediumExerciseStats(userId: String) {
    repository.updateMediumExerciseStats(
        userId,
        onSuccess = { logSuccess("Medium exercise stats updated successfully.") },
        onFailure = { e -> logError("Error updating medium exercise stats:", e) })
  }

  fun updateHardExerciseStats(userId: String) {
    repository.updateHardExerciseStats(
        userId,
        onSuccess = { logSuccess("Hard exercise stats updated successfully.") },
        onFailure = { e -> logError("Error updating hard exercise stats:", e) })
  }

  fun updateDailyQuestStats(userId: String) {
    repository.updateDailyQuestStats(
        userId,
        onSuccess = { logSuccess("Daily quest stats updated successfully.") },
        onFailure = { e -> logError("Error updating daily quest stats:", e) })
  }

  fun updateWeeklyQuestStats(userId: String) {
    repository.updateWeeklyQuestStats(
        userId,
        onSuccess = { logSuccess("Weekly quest stats updated successfully.") },
        onFailure = { e -> logError("Error updating weekly quest stats:", e) })
  }

  fun updateCompletedChallengeStats(userId: String) {
    repository.updateCompletedChallengeStats(
        userId,
        onSuccess = { logSuccess("Completed challenge stats updated successfully.") },
        onFailure = { e -> logError("Error updating completed challenge stats:", e) })
  }

  fun updateCreatedChallengeStats(userId: String) {
    repository.updateCreatedChallengeStats(
        userId,
        onSuccess = { logSuccess("Created challenge stats updated successfully.") },
        onFailure = { e -> logError("Error updating created challenge stats:", e) })
  }
}
