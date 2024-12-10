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

  sealed class UpdateStatsEvent {
    data object Loading : UpdateStatsEvent()

    data object Idle : UpdateStatsEvent()

    data object Success : UpdateStatsEvent()

    data class Failure(val message: String) : UpdateStatsEvent()
  }

  fun resetUpdateStatsEvent() {
    _updateStatsEvent.value = UpdateStatsEvent.Idle
  }

  private val UNKNOWN_ERROR_MESSAGE = "Unknown error"

  private val _updateStatsEvent = MutableStateFlow<UpdateStatsEvent>(UpdateStatsEvent.Idle)
  val updateStatsEvent: StateFlow<UpdateStatsEvent> = _updateStatsEvent

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

  private val _timePerLetter = MutableStateFlow<List<Long>>(emptyList())
  val timePerLetter: StateFlow<List<Long>> = _timePerLetter

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
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.getLettersLearned(
        userSession.getUserId()!!,
        onSuccess = { letters ->
          _lettersLearned.value = letters
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error fetching letters learned:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun getEasyExerciseStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.getEasyExerciseStats(
        userSession.getUserId()!!,
        onSuccess = { easyStats ->
          _easy.value = easyStats
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error fetching easy exercise stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun getMediumExerciseStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.getMediumExerciseStats(
        userSession.getUserId()!!,
        onSuccess = { mediumStats ->
          _medium.value = mediumStats
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error fetching medium exercise stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun getHardExerciseStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.getHardExerciseStats(
        userSession.getUserId()!!,
        onSuccess = { hardStats ->
          _hard.value = hardStats
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error fetching hard exercise stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun getDailyQuestStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.getDailyQuestStats(
        userSession.getUserId()!!,
        onSuccess = { dailyQuest ->
          _daily.value = dailyQuest
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error fetching daily quest stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun getWeeklyQuestStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.getWeeklyQuestStats(
        userSession.getUserId()!!,
        onSuccess = { weeklyQuest ->
          _weekly.value = weeklyQuest
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error fetching weekly quest stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun getCompletedChallengeStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading
    repository.getCompletedChallengeStats(
        userSession.getUserId()!!,
        onSuccess = { completedChallenge ->
          _completed.value = completedChallenge
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error fetching completed challenge Stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun getCreatedChallengeStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.getCreatedChallengeStats(
        userSession.getUserId()!!,
        onSuccess = { createdChallenge ->
          _created.value = createdChallenge
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error fetching created challenge Stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun getWonChallengeStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.getWonChallengeStats(
        userSession.getUserId()!!,
        onSuccess = { wonChallenge ->
          _won.value = wonChallenge
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error fetching won challenge Stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun getTimePerLetter() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.getTimePerLetter(
        userSession.getUserId()!!,
        onSuccess = { time ->
          _timePerLetter.value = time
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error fetching time per letter:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun updateLettersLearned(newLetter: Char) {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.updateLettersLearned(
        userSession.getUserId()!!,
        newLetter,
        onSuccess = {
          logSuccess("Letters learned updated successfully.")
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error updating letters learned:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun updateEasyExerciseStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.updateEasyExerciseStats(
        userSession.getUserId()!!,
        onSuccess = {
          logSuccess("Easy exercise stats updated successfully.")
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error updating easy exercise stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun updateMediumExerciseStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.updateMediumExerciseStats(
        userSession.getUserId()!!,
        onSuccess = {
          logSuccess("Medium exercise stats updated successfully.")
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error updating medium exercise stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun updateHardExerciseStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.updateHardExerciseStats(
        userSession.getUserId()!!,
        onSuccess = {
          logSuccess("Hard exercise stats updated successfully.")
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error updating hard exercise stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun updateDailyQuestStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.updateDailyQuestStats(
        userSession.getUserId()!!,
        onSuccess = {
          logSuccess("Daily quest stats updated successfully.")
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error updating daily quest stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun updateWeeklyQuestStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.updateWeeklyQuestStats(
        userSession.getUserId()!!,
        onSuccess = {
          logSuccess("Weekly quest stats updated successfully.")
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error updating weekly quest stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun updateCompletedChallengeStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.updateCompletedChallengeStats(
        userSession.getUserId()!!,
        onSuccess = {
          logSuccess("Completed challenge stats updated successfully.")
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error updating completed challenge stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun updateCreatedChallengeStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.updateCreatedChallengeStats(
        userSession.getUserId()!!,
        onSuccess = {
          logSuccess("Created challenge stats updated successfully.")
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error updating created challenge stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun updateWonChallengeStats() {
    _updateStatsEvent.value = UpdateStatsEvent.Loading

    repository.updateWonChallengeStats(
        userSession.getUserId()!!,
        onSuccess = {
          logSuccess("Won challenge stats updated successfully.")
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error updating won challenge stats:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }

  fun updateTimePerLetter(newTime: Long) {
    _updateStatsEvent.value = UpdateStatsEvent.Loading
    getTimePerLetter()
    val newTimePerLetter: List<Long> = _timePerLetter.value
    newTimePerLetter.toMutableList().add(newTime)

    repository.updateTimePerLetter(
        userSession.getUserId()!!,
        newTimePerLetter,
        onSuccess = {
          logSuccess("Time per letter updated successfully.")
          _updateStatsEvent.value = UpdateStatsEvent.Success
        },
        onFailure = { e ->
          logError("Error updating time per letter:", e)
          _updateStatsEvent.value = UpdateStatsEvent.Failure(e.message ?: UNKNOWN_ERROR_MESSAGE)
        })
  }
}
