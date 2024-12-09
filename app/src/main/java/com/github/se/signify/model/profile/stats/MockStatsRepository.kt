package com.github.se.signify.model.stats

class MockStatsRepository : StatsRepository {

  private val stats = mutableMapOf<String, Stats>()

  var shouldSucceed: Boolean = true

  private val exceptionToThrow: Exception = Exception("Simulated failure")

  private val methodCalls = mutableListOf<String>()

  fun wasMethodCalled(methodName: String): Boolean = methodCalls.contains(methodName)

  fun getMethodCalls(): List<String> = methodCalls.toList()

  fun setStatsForUser(userId: String, stats: Stats) {
    this.stats[userId] = stats
  }

  fun getStatsForUser(userId: String): Stats? {
    return stats[userId]
  }

  fun reset() {
    shouldSucceed = true
    stats.clear()
    methodCalls.clear()
  }

  private fun trackCall(methodName: String) {
    methodCalls.add(methodName)
  }

  override fun init(onSuccess: () -> Unit) {
    trackCall("init")
    if (shouldSucceed) {
      onSuccess()
    }
  }

  override fun getLettersLearned(
      userId: String,
      onSuccess: (List<Char>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    trackCall("getLettersLearned")
    if (!shouldSucceed) {
      onFailure(exceptionToThrow)
    } else {
      onSuccess(stats[userId]?.lettersLearned ?: emptyList())
    }
  }

  override fun getEasyExerciseStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getStat(userId, { it.easyExercise }, "getEasyExerciseStats", onSuccess, onFailure)
  }

  override fun getMediumExerciseStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getStat(userId, { it.mediumExercise }, "getMediumExerciseStats", onSuccess, onFailure)
  }

  override fun getHardExerciseStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getStat(userId, { it.hardExercise }, "getHardExerciseStats", onSuccess, onFailure)
  }

  override fun getDailyQuestStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getStat(userId, { it.dailyQuest }, "getDailyQuestStats", onSuccess, onFailure)
  }

  override fun getWeeklyQuestStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getStat(userId, { it.weeklyQuest }, "getWeeklyQuestStats", onSuccess, onFailure)
  }

  override fun getCompletedChallengeStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getStat(userId, { it.completedChallenge }, "getCompletedChallengeStats", onSuccess, onFailure)
  }

  override fun getCreatedChallengeStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getStat(userId, { it.createdChallenge }, "getCreatedChallengeStats", onSuccess, onFailure)
  }

  override fun getWonChallengeStats(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    getStat(userId, { it.wonChallenge }, "getWonChallengeStats", onSuccess, onFailure)
  }

  override fun updateLettersLearned(
      userId: String,
      newLetter: Char,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    updateStat(
        userId,
        { it.copy(lettersLearned = it.lettersLearned + newLetter) },
        "updateLettersLearned",
        onSuccess,
        onFailure)
  }

  override fun updateEasyExerciseStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    updateStat(
        userId,
        { it.copy(easyExercise = it.easyExercise + 1) },
        "updateEasyExerciseStats",
        onSuccess,
        onFailure)
  }

  override fun updateMediumExerciseStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    updateStat(
        userId,
        { it.copy(mediumExercise = it.mediumExercise + 1) },
        "updateMediumExerciseStats",
        onSuccess,
        onFailure)
  }

  override fun updateHardExerciseStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    updateStat(
        userId,
        { it.copy(hardExercise = it.hardExercise + 1) },
        "updateHardExerciseStats",
        onSuccess,
        onFailure)
  }

  override fun updateDailyQuestStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    updateStat(
        userId,
        { it.copy(dailyQuest = it.dailyQuest + 1) },
        "updateDailyQuestStats",
        onSuccess,
        onFailure)
  }

  override fun updateWeeklyQuestStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    updateStat(
        userId,
        { it.copy(weeklyQuest = it.weeklyQuest + 1) },
        "updateWeeklyQuestStats",
        onSuccess,
        onFailure)
  }

  override fun updateCompletedChallengeStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    updateStat(
        userId,
        { it.copy(completedChallenge = it.completedChallenge + 1) },
        "updateCompletedChallengeStats",
        onSuccess,
        onFailure)
  }

  override fun updateCreatedChallengeStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    updateStat(
        userId,
        { it.copy(createdChallenge = it.createdChallenge + 1) },
        "updateCreatedChallengeStats",
        onSuccess,
        onFailure)
  }

  override fun updateWonChallengeStats(
      userId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    updateStat(
        userId,
        { it.copy(wonChallenge = it.wonChallenge + 1) },
        "updateWonChallengeStats",
        onSuccess,
        onFailure)
  }

  private fun getStat(
      userId: String,
      getter: (Stats) -> Int,
      methodName: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    trackCall(methodName)
    if (!shouldSucceed) {
      onFailure(exceptionToThrow)
    } else {
      val userStats = stats[userId] ?: Stats()
      onSuccess(getter(userStats))
    }
  }

  private fun updateStat(
      userId: String,
      updater: (Stats) -> Stats,
      methodName: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    trackCall(methodName)
    if (!shouldSucceed) {
      onFailure(exceptionToThrow)
    } else {
      val userStats = stats.getOrPut(userId) { Stats() }
      stats[userId] = updater(userStats)
      onSuccess()
    }
  }
}
