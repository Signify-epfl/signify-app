package com.github.se.signify.model.profile.stats

interface StatsRepository {
  fun init(onSuccess: () -> Unit)

  fun getLettersLearned(
      userId: String,
      onSuccess: (List<Char>) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun getEasyExerciseStats(userId: String, onSuccess: (Int) -> Unit, onFailure: (Exception) -> Unit)

  fun getMediumExerciseStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun getHardExerciseStats(userId: String, onSuccess: (Int) -> Unit, onFailure: (Exception) -> Unit)

  fun getDailyQuestStats(userId: String, onSuccess: (Int) -> Unit, onFailure: (Exception) -> Unit)

  fun getWeeklyQuestStats(userId: String, onSuccess: (Int) -> Unit, onFailure: (Exception) -> Unit)

  fun getCompletedChallengeStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun getCreatedChallengeStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun getWonChallengeStats(userId: String, onSuccess: (Int) -> Unit, onFailure: (Exception) -> Unit)

  fun getTimePerLetter(
      userId: String,
      onSuccess: (List<Long>) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun updateLettersLearned(
      userId: String,
      newLetter: Char,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun updateEasyExerciseStats(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)

  fun updateMediumExerciseStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun updateHardExerciseStats(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)

  fun updateDailyQuestStats(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)

  fun updateWeeklyQuestStats(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)

  fun updateCompletedChallengeStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun updateCreatedChallengeStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun updateWonChallengeStats(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)

  fun updateTimePerLetter(
      userId: String,
      newTimePerLetter: List<Long>,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )
}
