package com.github.se.signify.model.stats

import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.di.MockDependencyProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class StatsViewModelTest {

  private lateinit var userSession: UserSession
  private lateinit var mockStatsRepository: MockStatsRepository
  private lateinit var statsViewModel: StatsViewModel

  private lateinit var userId: String

  private val initialStats =
      Stats(
          lettersLearned = listOf('A', 'B'),
          easyExercise = 5,
          mediumExercise = 3,
          hardExercise = 2,
          dailyQuest = 1,
          weeklyQuest = 1,
          completedChallenge = 4,
          createdChallenge = 2,
          wonChallenge = 3)

  @Before
  fun setUp() {
    userSession = MockDependencyProvider.userSession()
    mockStatsRepository = MockStatsRepository()
    statsViewModel = StatsViewModel(userSession, mockStatsRepository)
    userId = userSession.getUserId()!!

    mockStatsRepository.setStatsForUser(userId, initialStats)
  }

  private fun assertGetStatSuccess(
      statName: String,
      getAction: () -> Unit,
      expectedValue: Int,
      stateFlowValue: () -> Int
  ) {
    getAction()

    assertTrue(mockStatsRepository.wasMethodCalled(statName))

    assertEquals(expectedValue, stateFlowValue())
  }

  private fun assertGetStatFailure(
      statName: String,
      getAction: () -> Unit,
      stateFlowValue: () -> Int
  ) {
    mockStatsRepository.shouldSucceed = false

    getAction()

    assertTrue(mockStatsRepository.wasMethodCalled(statName))

    assertEquals(0, stateFlowValue())
  }

  private fun assertUpdateStatSuccess(
      methodName: String,
      updateAction: () -> Unit,
      getAction: () -> Unit,
      expectedValue: Int,
      stateFlowValue: () -> Int
  ) {
    updateAction()

    assertTrue(mockStatsRepository.wasMethodCalled(methodName))

    getAction()

    assertEquals(expectedValue, stateFlowValue())
  }

  private fun assertUpdateStatFailure(
      methodName: String,
      updateAction: () -> Unit,
      getAction: () -> Unit,
      expectedValue: Int,
      stateFlowValue: () -> Int
  ) {
    mockStatsRepository.shouldSucceed = false

    updateAction()

    assertTrue(mockStatsRepository.wasMethodCalled(methodName))

    mockStatsRepository.shouldSucceed = true
    getAction()

    assertEquals(expectedValue, stateFlowValue())
  }

  @Test
  fun getLettersLearnedShouldHandleSuccessCorrectly() {
    statsViewModel.getLettersLearned()

    assertTrue(mockStatsRepository.wasMethodCalled("getLettersLearned"))

    assertEquals(initialStats.lettersLearned, statsViewModel.lettersLearned.value)
  }

  @Test
  fun getLettersLearnedShouldHandleFailureCorrectly() {
    mockStatsRepository.shouldSucceed = false

    statsViewModel.getLettersLearned()

    assertTrue(mockStatsRepository.wasMethodCalled("getLettersLearned"))

    assertEquals(emptyList<Char>(), statsViewModel.lettersLearned.value)
  }

  @Test
  fun getEasyExerciseStatsShouldHandleSuccessCorrectly() {
    assertGetStatSuccess(
        statName = "getEasyExerciseStats",
        getAction = { statsViewModel.getEasyExerciseStats() },
        expectedValue = initialStats.easyExercise,
        stateFlowValue = { statsViewModel.easy.value })
  }

  @Test
  fun getEasyExerciseStatsShouldHandleFailureCorrectly() {
    assertGetStatFailure(
        statName = "getEasyExerciseStats",
        getAction = { statsViewModel.getEasyExerciseStats() },
        stateFlowValue = { statsViewModel.easy.value })
  }

  @Test
  fun getMediumExerciseStatsShouldHandleSuccessCorrectly() {
    assertGetStatSuccess(
        statName = "getMediumExerciseStats",
        getAction = { statsViewModel.getMediumExerciseStats() },
        expectedValue = initialStats.mediumExercise,
        stateFlowValue = { statsViewModel.medium.value })
  }

  @Test
  fun getMediumExerciseStatsShouldHandleFailureCorrectly() {
    assertGetStatFailure(
        statName = "getMediumExerciseStats",
        getAction = { statsViewModel.getMediumExerciseStats() },
        stateFlowValue = { statsViewModel.medium.value })
  }

  @Test
  fun getHardExerciseStatsShouldHandleSuccessCorrectly() {
    assertGetStatSuccess(
        statName = "getHardExerciseStats",
        getAction = { statsViewModel.getHardExerciseStats() },
        expectedValue = initialStats.hardExercise,
        stateFlowValue = { statsViewModel.hard.value })
  }

  @Test
  fun getHardExerciseStatsShouldHandleFailureCorrectly() {
    assertGetStatFailure(
        statName = "getHardExerciseStats",
        getAction = { statsViewModel.getHardExerciseStats() },
        stateFlowValue = { statsViewModel.hard.value })
  }

  @Test
  fun getDailyQuestStatsShouldHandleSuccessCorrectly() {
    assertGetStatSuccess(
        statName = "getDailyQuestStats",
        getAction = { statsViewModel.getDailyQuestStats() },
        expectedValue = initialStats.dailyQuest,
        stateFlowValue = { statsViewModel.daily.value })
  }

  @Test
  fun getDailyQuestStatsShouldHandleFailureCorrectly() {
    assertGetStatFailure(
        statName = "getDailyQuestStats",
        getAction = { statsViewModel.getDailyQuestStats() },
        stateFlowValue = { statsViewModel.daily.value })
  }

  @Test
  fun getWeeklyQuestStatsShouldHandleSuccessCorrectly() {
    assertGetStatSuccess(
        statName = "getWeeklyQuestStats",
        getAction = { statsViewModel.getWeeklyQuestStats() },
        expectedValue = initialStats.weeklyQuest,
        stateFlowValue = { statsViewModel.weekly.value })
  }

  @Test
  fun getWeeklyQuestStatsShouldHandleFailureCorrectly() {
    assertGetStatFailure(
        statName = "getWeeklyQuestStats",
        getAction = { statsViewModel.getWeeklyQuestStats() },
        stateFlowValue = { statsViewModel.weekly.value })
  }

  @Test
  fun getCompletedChallengeStatsShouldHandleSuccessCorrectly() {
    assertGetStatSuccess(
        statName = "getCompletedChallengeStats",
        getAction = { statsViewModel.getCompletedChallengeStats() },
        expectedValue = initialStats.completedChallenge,
        stateFlowValue = { statsViewModel.completed.value })
  }

  @Test
  fun getCompletedChallengeStatsShouldHandleFailureCorrectly() {
    assertGetStatFailure(
        statName = "getCompletedChallengeStats",
        getAction = { statsViewModel.getCompletedChallengeStats() },
        stateFlowValue = { statsViewModel.completed.value })
  }

  @Test
  fun getCreatedChallengeStatsShouldHandleSuccessCorrectly() {
    assertGetStatSuccess(
        statName = "getCreatedChallengeStats",
        getAction = { statsViewModel.getCreatedChallengeStats() },
        expectedValue = initialStats.createdChallenge,
        stateFlowValue = { statsViewModel.created.value })
  }

  @Test
  fun getCreatedChallengeStatsShouldHandleFailureCorrectly() {
    assertGetStatFailure(
        statName = "getCreatedChallengeStats",
        getAction = { statsViewModel.getCreatedChallengeStats() },
        stateFlowValue = { statsViewModel.created.value })
  }

  @Test
  fun getWonChallengeStatsShouldHandleSuccessCorrectly() {
    assertGetStatSuccess(
        statName = "getWonChallengeStats",
        getAction = { statsViewModel.getWonChallengeStats() },
        expectedValue = initialStats.wonChallenge,
        stateFlowValue = { statsViewModel.won.value })
  }

  @Test
  fun getWonChallengeStatsShouldHandleFailureCorrectly() {
    assertGetStatFailure(
        statName = "getWonChallengeStats",
        getAction = { statsViewModel.getWonChallengeStats() },
        stateFlowValue = { statsViewModel.won.value })
  }

  @Test
  fun updateLettersLearnedShouldHandleSuccessCorrectly() {
    val newLetter = 'D'

    statsViewModel.updateLettersLearned(newLetter)

    assertTrue(mockStatsRepository.wasMethodCalled("updateLettersLearned"))

    statsViewModel.getLettersLearned()

    assertEquals(initialStats.lettersLearned + newLetter, statsViewModel.lettersLearned.value)
  }

  @Test
  fun updateLettersLearnedShouldHandleFailureCorrectly() {
    val newLetter = 'D'

    mockStatsRepository.shouldSucceed = false

    statsViewModel.updateLettersLearned(newLetter)

    assertTrue(mockStatsRepository.wasMethodCalled("updateLettersLearned"))

    mockStatsRepository.shouldSucceed = true
    statsViewModel.getLettersLearned()

    assertEquals(initialStats.lettersLearned, statsViewModel.lettersLearned.value)
  }

  @Test
  fun updateEasyExerciseStatsShouldHandleSuccessCorrectly() {
    assertUpdateStatSuccess(
        methodName = "updateEasyExerciseStats",
        updateAction = { statsViewModel.updateEasyExerciseStats() },
        getAction = { statsViewModel.getEasyExerciseStats() },
        expectedValue = initialStats.easyExercise + 1,
        stateFlowValue = { statsViewModel.easy.value })
  }

  @Test
  fun updateEasyExerciseStatsShouldHandleFailureCorrectly() {
    assertUpdateStatFailure(
        methodName = "updateEasyExerciseStats",
        updateAction = { statsViewModel.updateEasyExerciseStats() },
        getAction = { statsViewModel.getEasyExerciseStats() },
        expectedValue = initialStats.easyExercise,
        stateFlowValue = { statsViewModel.easy.value })
  }

  @Test
  fun updateMediumExerciseStatsShouldHandleSuccessCorrectly() {
    assertUpdateStatSuccess(
        methodName = "updateMediumExerciseStats",
        updateAction = { statsViewModel.updateMediumExerciseStats() },
        getAction = { statsViewModel.getMediumExerciseStats() },
        expectedValue = initialStats.mediumExercise + 1,
        stateFlowValue = { statsViewModel.medium.value })
  }

  @Test
  fun updateMediumExerciseStatsShouldHandleFailureCorrectly() {
    assertUpdateStatFailure(
        methodName = "updateMediumExerciseStats",
        updateAction = { statsViewModel.updateMediumExerciseStats() },
        getAction = { statsViewModel.getMediumExerciseStats() },
        expectedValue = initialStats.mediumExercise,
        stateFlowValue = { statsViewModel.medium.value })
  }

  @Test
  fun updateHardExerciseStatsShouldHandleSuccessCorrectly() {
    assertUpdateStatSuccess(
        methodName = "updateHardExerciseStats",
        updateAction = { statsViewModel.updateHardExerciseStats() },
        getAction = { statsViewModel.getHardExerciseStats() },
        expectedValue = initialStats.hardExercise + 1,
        stateFlowValue = { statsViewModel.hard.value })
  }

  @Test
  fun updateHardExerciseStatsShouldHandleFailureCorrectly() {
    assertUpdateStatFailure(
        methodName = "updateHardExerciseStats",
        updateAction = { statsViewModel.updateHardExerciseStats() },
        getAction = { statsViewModel.getHardExerciseStats() },
        expectedValue = initialStats.hardExercise,
        stateFlowValue = { statsViewModel.hard.value })
  }

  @Test
  fun updateDailyQuestStatsShouldHandleSuccessCorrectly() {
    assertUpdateStatSuccess(
        methodName = "updateDailyQuestStats",
        updateAction = { statsViewModel.updateDailyQuestStats() },
        getAction = { statsViewModel.getDailyQuestStats() },
        expectedValue = initialStats.dailyQuest + 1,
        stateFlowValue = { statsViewModel.daily.value })
  }

  @Test
  fun updateDailyQuestStatsShouldHandleFailureCorrectly() {
    assertUpdateStatFailure(
        methodName = "updateDailyQuestStats",
        updateAction = { statsViewModel.updateDailyQuestStats() },
        getAction = { statsViewModel.getDailyQuestStats() },
        expectedValue = initialStats.dailyQuest,
        stateFlowValue = { statsViewModel.daily.value })
  }

  @Test
  fun updateWeeklyQuestStatsShouldHandleSuccessCorrectly() {
    assertUpdateStatSuccess(
        methodName = "updateWeeklyQuestStats",
        updateAction = { statsViewModel.updateWeeklyQuestStats() },
        getAction = { statsViewModel.getWeeklyQuestStats() },
        expectedValue = initialStats.weeklyQuest + 1,
        stateFlowValue = { statsViewModel.weekly.value })
  }

  @Test
  fun updateWeeklyQuestStatsShouldHandleFailureCorrectly() {
    assertUpdateStatFailure(
        methodName = "updateWeeklyQuestStats",
        updateAction = { statsViewModel.updateWeeklyQuestStats() },
        getAction = { statsViewModel.getWeeklyQuestStats() },
        expectedValue = initialStats.weeklyQuest,
        stateFlowValue = { statsViewModel.weekly.value })
  }

  @Test
  fun updateCompletedChallengeStatsShouldHandleSuccessCorrectly() {
    assertUpdateStatSuccess(
        methodName = "updateCompletedChallengeStats",
        updateAction = { statsViewModel.updateCompletedChallengeStats() },
        getAction = { statsViewModel.getCompletedChallengeStats() },
        expectedValue = initialStats.completedChallenge + 1,
        stateFlowValue = { statsViewModel.completed.value })
  }

  @Test
  fun updateCompletedChallengeStatsShouldHandleFailureCorrectly() {
    assertUpdateStatFailure(
        methodName = "updateCompletedChallengeStats",
        updateAction = { statsViewModel.updateCompletedChallengeStats() },
        getAction = { statsViewModel.getCompletedChallengeStats() },
        expectedValue = initialStats.completedChallenge,
        stateFlowValue = { statsViewModel.completed.value })
  }

  @Test
  fun updateCreatedChallengeStatsShouldHandleSuccessCorrectly() {
    assertUpdateStatSuccess(
        methodName = "updateCreatedChallengeStats",
        updateAction = { statsViewModel.updateCreatedChallengeStats() },
        getAction = { statsViewModel.getCreatedChallengeStats() },
        expectedValue = initialStats.createdChallenge + 1,
        stateFlowValue = { statsViewModel.created.value })
  }

  @Test
  fun updateCreatedChallengeStatsShouldHandleFailureCorrectly() {
    assertUpdateStatFailure(
        methodName = "updateCreatedChallengeStats",
        updateAction = { statsViewModel.updateCreatedChallengeStats() },
        getAction = { statsViewModel.getCreatedChallengeStats() },
        expectedValue = initialStats.createdChallenge,
        stateFlowValue = { statsViewModel.created.value })
  }

  @Test
  fun updateWonChallengeStatsShouldHandleSuccessCorrectly() {
    assertUpdateStatSuccess(
        methodName = "updateWonChallengeStats",
        updateAction = { statsViewModel.updateWonChallengeStats() },
        getAction = { statsViewModel.getWonChallengeStats() },
        expectedValue = initialStats.wonChallenge + 1,
        stateFlowValue = { statsViewModel.won.value })
  }

  @Test
  fun updateWonChallengeStatsShouldHandleFailureCorrectly() {
    assertUpdateStatFailure(
        methodName = "updateWonChallengeStats",
        updateAction = { statsViewModel.updateWonChallengeStats() },
        getAction = { statsViewModel.getWonChallengeStats() },
        expectedValue = initialStats.wonChallenge,
        stateFlowValue = { statsViewModel.won.value })
  }
}
