package com.github.se.signify.model.stats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StatsViewModel(private val repository: StatsRepository) : ViewModel() {

    private val _days = MutableStateFlow(0)
    val days: StateFlow<Int> = _days

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

    companion object {
        val Factory: ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return StatsViewModel(StatsRepositoryFirestore(Firebase.firestore)) as T
                }
            }
    }

    private fun logSuccess(m: String) = Log.d(logTag, m)

    private fun logError(m: String, e: Exception) = Log.e(logTag, "$m: ${e.message}")

    fun getDays(userId: String) {
        repository.getDays(
            userId,
            onSuccess = { daysValue -> _days.value = daysValue },
            onFailure = { e -> logError("Error fetching days streak:", e) }
        )
    }

    fun getLettersLearned(userId: String) {
        repository.getLettersLearned(
            userId,
            onSuccess = { letters -> _lettersLearned.value = letters },
            onFailure = { e -> logError("Error fetching letters learned:", e) }
        )
    }

    fun getExerciseStatsEasy(userId: String) {
        repository.getExerciseStatsEasy(
            userId,
            onSuccess = { easyStats -> _easy.value = easyStats},
            onFailure = { e -> logError("Error fetching easy exercise stats:", e) }
        )
    }

    fun getExerciseStatsMedium(userId: String) {
        repository.getExerciseStatsMedium(
            userId,
            onSuccess = { mediumStats -> _medium.value = mediumStats},
            onFailure = { e -> logError("Error fetching medium exercise stats:", e) }
        )
    }

    fun getExerciseStatsHard(userId: String) {
        repository.getExerciseStatsHard(
            userId,
            onSuccess = { hardStats -> _hard.value = hardStats},
            onFailure = { e -> logError("Error fetching hard exercise stats:", e) }
        )
    }

    fun getQuestStatsDaily(userId: String) {
        repository.getQuestStatsDaily(
            userId,
            onSuccess = { dailyQuests -> _daily.value = dailyQuests },
            onFailure = { e -> logError("Error fetching daily quest stats:", e) }
        )
    }

    fun getQuestStatsWeekly(userId: String) {
        repository.getQuestStatsWeekly(
            userId,
            onSuccess = { weeklyQuests -> _weekly.value = weeklyQuests },
            onFailure = { e -> logError("Error fetching weekly quest stats:", e) }
        )
    }

    fun getChallengeStatsCompleted(userId: String) {
        repository.getChallengeStatsCompleted(
            userId,
            onSuccess = { completedChallenge -> _completed.value = completedChallenge },
            onFailure = { e -> logError("Error fetching completed challenge Stats:", e) }
        )
    }

    fun getChallengeStatsCreated(userId: String) {
        repository.getChallengeStatsCreated(
            userId,
            onSuccess = { createdChallenge -> _created.value = createdChallenge },
            onFailure = { e -> logError("Error fetching created challenge Stats:", e) }
        )
    }

    fun updateDays(userId: String) {
        repository.updateDays(
            userId,
            onSuccess = { logSuccess("Days streak updated successfully.") },
            onFailure = { e -> logError("Error updating days streak:", e) }
        )
    }

    fun resetDays(userId: String) {
        repository.resetDays(
            userId,
            onSuccess = { logSuccess("Days streak reset successfully.") },
            onFailure = { e -> logError("Error reset days streak:", e) }
        )
    }

    fun updateLettersLearned(userId: String, newLetter: Char) {
        repository.updateLettersLearned(
            userId,
            newLetter,
            onSuccess = { logSuccess("Letterss learned updated successfully.") },
            onFailure = { e -> logError("Error updating letters learned:", e) }
        )
    }

    fun updateExerciseStatsEasy(userId: String) {
        repository.updateExerciseStatsEasy(
            userId,
            onSuccess = { logSuccess("Easy exercise stats updated successfully.") },
            onFailure = { e -> logError("Error updating easy exercise stats:", e) }
        )
    }

    fun updateExerciseStatsMedium(userId: String) {
        repository.updateExerciseStatsMedium(
            userId,
            onSuccess = { logSuccess("Medium exercise stats updated successfully.") },
            onFailure = { e -> logError("Error updating medium exercise stats:", e) }
        )
    }

    fun updateExerciseStatsHard(userId: String) {
        repository.updateExerciseStatsHard(
            userId,
            onSuccess = { logSuccess("Hard exercise stats updated successfully.") },
            onFailure = { e -> logError("Error updating hard exercise stats:", e) }
        )
    }

    fun updateQuestStatsDaily(userId: String) {
        repository.updateQuestStatsDaily(
            userId,
            onSuccess = { logSuccess("Daily quest stats updated successfully.") },
            onFailure = { e -> logError("Error updating daily quest stats:", e) }
        )
    }

    fun updateQuestStatsWeekly(userId: String) {
        repository.updateQuestStatsWeekly(
            userId,
            onSuccess = { logSuccess("Weekly quest stats updated successfully.") },
            onFailure = { e -> logError("Error updating weekly quest stats:", e) }
        )
    }

    fun updateChallengeStatsCompleted(userId: String) {
        repository.updateChallengeStatsCompleted(
            userId,
            onSuccess = { logSuccess("Completed challenge stats updated successfully.") },
            onFailure = { e -> logError("Error updating completed challenge stats:", e) }
        )
    }

    fun updateChallengeStatsCreated(userId: String) {
        repository.updateChallengeStatsCreated(
            userId,
            onSuccess = { logSuccess("Created challenge stats updated successfully.") },
            onFailure = { e -> logError("Error updating created challenge stats:", e) }
        )
    }
}
