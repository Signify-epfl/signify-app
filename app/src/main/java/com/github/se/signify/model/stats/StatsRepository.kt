package com.github.se.signify.model.stats

interface StatsRepository {
    fun init(onSuccess: () -> Unit)

    fun getDays(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getLettersLearned(
        userId: String,
        onSuccess: (List<Char>) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getExerciseStatsEasy(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getExerciseStatsMedium(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getExerciseStatsHard(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getQuestStatsDaily(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getQuestStatsWeekly(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getChallengeStatsCompleted(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getChallengeStatsCreated(
        userId: String,
        onSuccess: (Int) -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun updateDays(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun resetDays(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun updateLettersLearned(
        userId: String,
        newLetter: Char,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun updateExerciseStatsEasy(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun updateExerciseStatsMedium(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun updateExerciseStatsHard(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun updateQuestStatsDaily(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun updateQuestStatsWeekly(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun updateChallengeStatsCompleted(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun updateChallengeStatsCreated(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )
}
