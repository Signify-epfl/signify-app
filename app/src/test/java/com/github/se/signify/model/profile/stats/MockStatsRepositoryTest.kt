package com.github.se.signify.model.stats

import com.github.se.signify.model.profile.stats.MockStatsRepository
import com.github.se.signify.model.profile.stats.Stats
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MockStatsRepositoryTest(
    private val getStatName: String,
    private val updateStatName: String,
    private val getAction: (String, (Int) -> Unit, (Exception) -> Unit) -> Unit,
    private val updateAction: (String, () -> Unit, (Exception) -> Unit) -> Unit,
    private val expectedValueGet: Int,
    private val expectedValueUpdate: Int
) {

  companion object {
    private val mockStatsRepository = MockStatsRepository()
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

    @JvmStatic
    @Parameterized.Parameters
    fun data(): Collection<Array<Any>> {
      mockStatsRepository.setStatsForUser(userId, initialStats)

      return listOf(
          arrayOf(
              "getEasyExerciseStats",
              "updateEasyExerciseStats",
              mockStatsRepository::getEasyExerciseStats,
              mockStatsRepository::updateEasyExerciseStats,
              initialStats.easyExercise,
              initialStats.easyExercise + 1),
          arrayOf(
              "getMediumExerciseStats",
              "updateMediumExerciseStats",
              mockStatsRepository::getMediumExerciseStats,
              mockStatsRepository::updateMediumExerciseStats,
              initialStats.mediumExercise,
              initialStats.mediumExercise + 1),
          arrayOf(
              "getHardExerciseStats",
              "updateHardExerciseStats",
              mockStatsRepository::getHardExerciseStats,
              mockStatsRepository::updateHardExerciseStats,
              initialStats.hardExercise,
              initialStats.hardExercise + 1),
          arrayOf(
              "getDailyQuestStats",
              "updateDailyQuestStats",
              mockStatsRepository::getDailyQuestStats,
              mockStatsRepository::updateDailyQuestStats,
              initialStats.dailyQuest,
              initialStats.dailyQuest + 1),
          arrayOf(
              "getWeeklyQuestStats",
              "updateWeeklyQuestStats",
              mockStatsRepository::getWeeklyQuestStats,
              mockStatsRepository::updateWeeklyQuestStats,
              initialStats.weeklyQuest,
              initialStats.weeklyQuest + 1),
          arrayOf(
              "getCompletedChallengeStats",
              "updateCompletedChallengeStats",
              mockStatsRepository::getCompletedChallengeStats,
              mockStatsRepository::updateCompletedChallengeStats,
              initialStats.completedChallenge,
              initialStats.completedChallenge + 1),
          arrayOf(
              "getCreatedChallengeStats",
              "updateCreatedChallengeStats",
              mockStatsRepository::getCreatedChallengeStats,
              mockStatsRepository::updateCreatedChallengeStats,
              initialStats.createdChallenge,
              initialStats.createdChallenge + 1),
          arrayOf(
              "getWonChallengeStats",
              "updateWonChallengeStats",
              mockStatsRepository::getWonChallengeStats,
              mockStatsRepository::updateWonChallengeStats,
              initialStats.wonChallenge,
              initialStats.wonChallenge + 1))
    }
  }

  @Before
  fun resetStats() {
    mockStatsRepository.reset()
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

    assertNull(mockStatsRepository.getStatsForUser(userId))
    assertTrue(mockStatsRepository.getMethodCalls().isEmpty())
    assertTrue(mockStatsRepository.wasMethodCalled("getEasyExerciseStats").not())
    assertTrue(mockStatsRepository.wasMethodCalled("updateEasyExerciseStats").not())
  }

  @Test
  fun `getStat should return the correct value`() {
    getAction(
        userId, { stat -> assertEquals(expectedValueGet, stat) }, { fail("This should not fail") })
    assertTrue(mockStatsRepository.wasMethodCalled(getStatName))
  }

  @Test
  fun `getStat should fail`() {
    mockStatsRepository.shouldSucceed = false
    getAction(
        userId,
        { fail("This should not succeed") },
        { exception -> assertEquals("Simulated failure", exception.message) })
    assertTrue(mockStatsRepository.wasMethodCalled(getStatName))
  }

  @Test
  fun `updateStat should return the correct value`() {
    updateAction(userId, {}, { fail("This should not fail") })

    assertTrue(mockStatsRepository.wasMethodCalled(updateStatName))

    getAction(
        userId,
        { stat -> assertEquals(expectedValueUpdate, stat) },
        { fail("This should not fail") })
  }

  @Test
  fun `updateStat should fail and not modify`() {
    mockStatsRepository.shouldSucceed = false
    updateAction(
        userId,
        { fail("This should not succeed") },
        { exception -> assertEquals("Simulated failure", exception.message) })

    assertTrue(mockStatsRepository.wasMethodCalled(updateStatName))

    mockStatsRepository.shouldSucceed = true

    getAction(
        userId, { stat -> assertEquals(expectedValueGet, stat) }, { fail("This should not fail") })
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
        userId, newLetter, onSuccess = {}, onFailure = { fail("This should not fail") })

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
}
