package com.github.se.signify.model.stats

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class MockStatsRepositoryTest {
  private lateinit var mockStatsRepository: MockStatsRepository
  private val userId = "testUser"

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
    mockStatsRepository = MockStatsRepository()
    mockStatsRepository.setStatsForUser(userId, initialStats)
  }

  @Test
  fun `init should call onSuccess if shouldSucceed is true`() {
    mockStatsRepository.init {}
    assertTrue(mockStatsRepository.wasMethodCalled("init"))
  }

  @Test
  fun `init should not call onSuccess if shouldSucceed is false`() {
    mockStatsRepository.shouldSucceed = false
    mockStatsRepository.init { fail("This should not be run") }
    assertTrue(mockStatsRepository.wasMethodCalled("init"))
  }

  @Test
  fun `wasMethodCalled should return true for called methods`() {
    mockStatsRepository.getEasyExerciseStats(userId, {}, {})
    assertTrue(mockStatsRepository.wasMethodCalled("getEasyExerciseStats"))
  }

  @Test
  fun `wasMethodCalled should return false for methods not called`() {
    assertFalse(mockStatsRepository.wasMethodCalled("updateMediumExerciseStats"))
  }

  @Test
  fun `getMethodCalls should return list of called methods`() {
    mockStatsRepository.getEasyExerciseStats(userId, {}, {})
    mockStatsRepository.updateEasyExerciseStats(userId, {}, {})
    val methodCalls = mockStatsRepository.getMethodCalls()
    assertEquals(listOf("getEasyExerciseStats", "updateEasyExerciseStats"), methodCalls)
  }

  @Test
  fun `reset should clear stats and method calls`() {
    mockStatsRepository.getEasyExerciseStats(userId, {}, {})
    mockStatsRepository.updateEasyExerciseStats(userId, {}, {})

    mockStatsRepository.reset()

    assertTrue(mockStatsRepository.getMethodCalls().isEmpty())
    assertTrue(mockStatsRepository.wasMethodCalled("getEasyExerciseStats").not())
    assertTrue(mockStatsRepository.wasMethodCalled("updateEasyExerciseStats").not())
  }

  @Test
  fun `getLettersLearned should return correct value on success`() {
    mockStatsRepository.getLettersLearned(
        userId,
        onSuccess = { lettersLearned -> assertEquals(initialStats.lettersLearned, lettersLearned) },
        onFailure = { fail("This should not fail") })
    assertTrue(mockStatsRepository.wasMethodCalled("getLettersLearned"))
  }

  @Test
  fun `getLettersLearned should fail when shouldSucceed is false`() {
    mockStatsRepository.shouldSucceed = false
    mockStatsRepository.getLettersLearned(
        userId,
        onSuccess = { fail("This should not succeed") },
        onFailure = { exception -> assertEquals("Simulated failure", exception.message) })
    assertTrue(mockStatsRepository.wasMethodCalled("getLettersLearned"))
  }

  @Test
  fun `updateLettersLearned should add a new letter on success`() {
    val newLetter = 'D'

    mockStatsRepository.updateLettersLearned(
        userId,
        newLetter,
        onSuccess = { /* Success callback */},
        onFailure = { fail("This should not fail") })

    assertTrue(mockStatsRepository.wasMethodCalled("updateLettersLearned"))

    mockStatsRepository.getLettersLearned(
        userId,
        onSuccess = { lettersLearned ->
          assertEquals(initialStats.lettersLearned + newLetter, lettersLearned)
        },
        onFailure = { fail("This should not fail") })
  }

  @Test
  fun `updateLettersLearned should not modify letters on failure`() {
    val newLetter = 'D'
    mockStatsRepository.shouldSucceed = false

    mockStatsRepository.updateLettersLearned(
        userId,
        newLetter,
        onSuccess = { fail("This should not succeed") },
        onFailure = { exception -> assertEquals("Simulated failure", exception.message) })

    assertTrue(mockStatsRepository.wasMethodCalled("updateLettersLearned"))

    mockStatsRepository.shouldSucceed = true
    mockStatsRepository.getLettersLearned(
        userId,
        onSuccess = { lettersLearned -> assertEquals(initialStats.lettersLearned, lettersLearned) },
        onFailure = { fail("This should not fail") })
  }

  private fun verifyGetStat(
      statName: String,
      expectedValue: Int,
      getStat: (String, (Int) -> Unit, (Exception) -> Unit) -> Unit
  ) {
    getStat(
        userId,
        { stat -> assertEquals(expectedValue, stat) }, // Success callback
        { fail("This should not fail") } // Failure callback
        )
    assertTrue(mockStatsRepository.wasMethodCalled(statName))
  }

  private fun verifyGetStatFailure(
      statName: String,
      getStat: (String, (Int) -> Unit, (Exception) -> Unit) -> Unit
  ) {
    mockStatsRepository.shouldSucceed = false
    getStat(
        userId,
        { fail("This should not succeed") }, // Success callback
        { exception -> assertEquals("Simulated failure", exception.message) } // Failure callback
        )
    assertTrue(mockStatsRepository.wasMethodCalled(statName))
  }

  private fun verifyUpdateStat(
      statName: String,
      incrementedValue: Int,
      updateStat: (String, () -> Unit, (Exception) -> Unit) -> Unit,
      getStat: (String, (Int) -> Unit, (Exception) -> Unit) -> Unit
  ) {
    updateStat(
        userId,
        {}, // Success callback
        { fail("This should not fail") } // Failure callback
        )

    assertTrue(mockStatsRepository.wasMethodCalled(statName))

    getStat(
        userId,
        { stat -> assertEquals(incrementedValue, stat) }, // Success callback
        { fail("This should not fail") } // Failure callback
        )
  }

  private fun verifyUpdateStatFailure(
      statName: String,
      expectedValue: Int,
      updateStat: (String, () -> Unit, (Exception) -> Unit) -> Unit,
      getStat: (String, (Int) -> Unit, (Exception) -> Unit) -> Unit
  ) {
    mockStatsRepository.shouldSucceed = false
    updateStat(
        userId,
        { fail("This should not succeed") }, // Success callback
        { exception -> assertEquals("Simulated failure", exception.message) } // Failure callback
        )

    assertTrue(mockStatsRepository.wasMethodCalled(statName))

    mockStatsRepository.shouldSucceed = true

    getStat(
        userId,
        { stat -> assertEquals(expectedValue, stat) }, // Success callback
        { fail("This should not fail") } // Failure callback
        )
  }

  @Test
  fun `getEasyExerciseStats should return correct value on success`() {
    verifyGetStat(
        "getEasyExerciseStats",
        initialStats.easyExercise,
        mockStatsRepository::getEasyExerciseStats)
  }

  @Test
  fun `getEasyExerciseStats should fail when shouldSucceed is false`() {
    verifyGetStatFailure("getEasyExerciseStats", mockStatsRepository::getEasyExerciseStats)
  }

  @Test
  fun `updateEasyExerciseStats should increment stat on success`() {
    verifyUpdateStat(
        "updateEasyExerciseStats",
        initialStats.easyExercise + 1,
        mockStatsRepository::updateEasyExerciseStats,
        mockStatsRepository::getEasyExerciseStats)
  }

  @Test
  fun `updateEasyExerciseStats should not modify stats on failure`() {
    verifyUpdateStatFailure(
        "updateEasyExerciseStats",
        initialStats.easyExercise,
        mockStatsRepository::updateEasyExerciseStats,
        mockStatsRepository::getEasyExerciseStats)
  }

  @Test
  fun `getMediumExerciseStats should return correct value on success`() {
    verifyGetStat(
        "getMediumExerciseStats",
        initialStats.mediumExercise,
        mockStatsRepository::getMediumExerciseStats)
  }

  @Test
  fun `getMediumExerciseStats should fail when shouldSucceed is false`() {
    verifyGetStatFailure("getMediumExerciseStats", mockStatsRepository::getMediumExerciseStats)
  }

  @Test
  fun `updateMediumExerciseStats should increment stat on success`() {
    verifyUpdateStat(
        "updateMediumExerciseStats",
        initialStats.mediumExercise + 1,
        mockStatsRepository::updateMediumExerciseStats,
        mockStatsRepository::getMediumExerciseStats)
  }

  @Test
  fun `updateMediumExerciseStats should not modify stats on failure`() {
    verifyUpdateStatFailure(
        "updateMediumExerciseStats",
        initialStats.mediumExercise,
        mockStatsRepository::updateMediumExerciseStats,
        mockStatsRepository::getMediumExerciseStats)
  }

  @Test
  fun `getHardExerciseStats should return correct value on success`() {
    verifyGetStat(
        "getHardExerciseStats",
        initialStats.hardExercise,
        mockStatsRepository::getHardExerciseStats)
  }

  @Test
  fun `getHardExerciseStats should fail when shouldSucceed is false`() {
    verifyGetStatFailure("getHardExerciseStats", mockStatsRepository::getHardExerciseStats)
  }

  @Test
  fun `updateHardExerciseStats should increment stat on success`() {
    verifyUpdateStat(
        "updateHardExerciseStats",
        initialStats.hardExercise + 1,
        mockStatsRepository::updateHardExerciseStats,
        mockStatsRepository::getHardExerciseStats)
  }

  @Test
  fun `updateHardExerciseStats should not modify stats on failure`() {
    verifyUpdateStatFailure(
        "updateHardExerciseStats",
        initialStats.hardExercise,
        mockStatsRepository::updateHardExerciseStats,
        mockStatsRepository::getHardExerciseStats)
  }

  @Test
  fun `getDailyQuestStats should return correct value on success`() {
    verifyGetStat(
        "getDailyQuestStats", initialStats.dailyQuest, mockStatsRepository::getDailyQuestStats)
  }

  @Test
  fun `getDailyQuestStats should fail when shouldSucceed is false`() {
    verifyGetStatFailure("getDailyQuestStats", mockStatsRepository::getDailyQuestStats)
  }

  @Test
  fun `updateDailyQuestStats should increment stat on success`() {
    verifyUpdateStat(
        "updateDailyQuestStats",
        initialStats.dailyQuest + 1,
        mockStatsRepository::updateDailyQuestStats,
        mockStatsRepository::getDailyQuestStats)
  }

  @Test
  fun `updateDailyQuestStats should not modify stats on failure`() {
    verifyUpdateStatFailure(
        "updateDailyQuestStats",
        initialStats.dailyQuest,
        mockStatsRepository::updateDailyQuestStats,
        mockStatsRepository::getDailyQuestStats)
  }

  @Test
  fun `getWeeklyQuestStats should return correct value on success`() {
    verifyGetStat(
        "getWeeklyQuestStats", initialStats.weeklyQuest, mockStatsRepository::getWeeklyQuestStats)
  }

  @Test
  fun `getWeeklyQuestStats should fail when shouldSucceed is false`() {
    verifyGetStatFailure("getWeeklyQuestStats", mockStatsRepository::getWeeklyQuestStats)
  }

  @Test
  fun `updateWeeklyQuestStats should increment stat on success`() {
    verifyUpdateStat(
        "updateWeeklyQuestStats",
        initialStats.weeklyQuest + 1,
        mockStatsRepository::updateWeeklyQuestStats,
        mockStatsRepository::getWeeklyQuestStats)
  }

  @Test
  fun `updateWeeklyQuestStats should not modify stats on failure`() {
    verifyUpdateStatFailure(
        "updateWeeklyQuestStats",
        initialStats.weeklyQuest,
        mockStatsRepository::updateWeeklyQuestStats,
        mockStatsRepository::getWeeklyQuestStats)
  }

  @Test
  fun `getCompletedChallengeStats should return correct value on success`() {
    verifyGetStat(
        "getCompletedChallengeStats",
        initialStats.completedChallenge,
        mockStatsRepository::getCompletedChallengeStats)
  }

  @Test
  fun `getCompletedChallengeStats should fail when shouldSucceed is false`() {
    verifyGetStatFailure(
        "getCompletedChallengeStats", mockStatsRepository::getCompletedChallengeStats)
  }

  @Test
  fun `updateCompletedChallengeStats should increment stat on success`() {
    verifyUpdateStat(
        "updateCompletedChallengeStats",
        initialStats.completedChallenge + 1,
        mockStatsRepository::updateCompletedChallengeStats,
        mockStatsRepository::getCompletedChallengeStats)
  }

  @Test
  fun `updateCompletedChallengeStats should not modify stats on failure`() {
    verifyUpdateStatFailure(
        "updateCompletedChallengeStats",
        initialStats.completedChallenge,
        mockStatsRepository::updateCompletedChallengeStats,
        mockStatsRepository::getCompletedChallengeStats)
  }

  @Test
  fun `getCreatedChallengeStats should return correct value on success`() {
    verifyGetStat(
        "getCreatedChallengeStats",
        initialStats.createdChallenge,
        mockStatsRepository::getCreatedChallengeStats)
  }

  @Test
  fun `getCreatedChallengeStats should fail when shouldSucceed is false`() {
    verifyGetStatFailure("getCreatedChallengeStats", mockStatsRepository::getCreatedChallengeStats)
  }

  @Test
  fun `updateCreatedChallengeStats should increment stat on success`() {
    verifyUpdateStat(
        "updateCreatedChallengeStats",
        initialStats.createdChallenge + 1,
        mockStatsRepository::updateCreatedChallengeStats,
        mockStatsRepository::getCreatedChallengeStats)
  }

  @Test
  fun `updateCreatedChallengeStats should not modify stats on failure`() {
    verifyUpdateStatFailure(
        "updateCreatedChallengeStats",
        initialStats.createdChallenge,
        mockStatsRepository::updateCreatedChallengeStats,
        mockStatsRepository::getCreatedChallengeStats)
  }

  @Test
  fun `getWonChallengeStats should return correct value on success`() {
    verifyGetStat(
        "getWonChallengeStats",
        initialStats.wonChallenge,
        mockStatsRepository::getWonChallengeStats)
  }

  @Test
  fun `getWonChallengeStats should fail when shouldSucceed is false`() {
    verifyGetStatFailure("getWonChallengeStats", mockStatsRepository::getWonChallengeStats)
  }

  @Test
  fun `updateWonChallengeStats should increment stat on success`() {
    verifyUpdateStat(
        "updateWonChallengeStats",
        initialStats.wonChallenge + 1,
        mockStatsRepository::updateWonChallengeStats,
        mockStatsRepository::getWonChallengeStats)
  }

  @Test
  fun `updateWonChallengeStats should not modify stats on failure`() {
    verifyUpdateStatFailure(
        "updateWonChallengeStats",
        initialStats.wonChallenge,
        mockStatsRepository::updateWonChallengeStats,
        mockStatsRepository::getWonChallengeStats)
  }
}
