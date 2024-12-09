package com.github.se.signify.model.stats

import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.dependencyInjection.MockDependencyProvider
import com.github.se.signify.model.profile.stats.MockStatsRepository
import com.github.se.signify.model.profile.stats.Stats
import com.github.se.signify.model.profile.stats.StatsViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class StatsViewModelTest(
    private val getStatName: String,
    private val updateStatName: String,
    private val getAction: () -> Unit,
    private val updateAction: () -> Unit,
    private val expectedValueGet: Int,
    private val expectedValueUpdate: Int,
    private val stateFlowValue: () -> Int
) {

  companion object {
    private lateinit var mockUserSession: UserSession
    private lateinit var statsViewModel: StatsViewModel
    private lateinit var mockStatsRepository: MockStatsRepository

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

    @JvmStatic
    @BeforeClass
    fun setup() {
      mockUserSession = MockDependencyProvider.userSession()
      mockStatsRepository = MockStatsRepository()
      userId = mockUserSession.getUserId()!!
      statsViewModel = StatsViewModel(mockUserSession, mockStatsRepository)
      mockStatsRepository.setStatsForUser(userId, initialStats)
    }

    @JvmStatic
    @Parameterized.Parameters
    fun data(): Collection<Array<Any>> {
      return listOf(
          arrayOf(
              "getEasyExerciseStats",
              "updateEasyExerciseStats",
              { statsViewModel.getEasyExerciseStats() },
              { statsViewModel.updateEasyExerciseStats() },
              initialStats.easyExercise,
              initialStats.easyExercise + 1,
              { statsViewModel.easy.value }),
          arrayOf(
              "getMediumExerciseStats",
              "updateMediumExerciseStats",
              { statsViewModel.getMediumExerciseStats() },
              { statsViewModel.updateMediumExerciseStats() },
              initialStats.mediumExercise,
              initialStats.mediumExercise + 1,
              { statsViewModel.medium.value }),
          arrayOf(
              "getHardExerciseStats",
              "updateHardExerciseStats",
              { statsViewModel.getHardExerciseStats() },
              { statsViewModel.updateHardExerciseStats() },
              initialStats.hardExercise,
              initialStats.hardExercise + 1,
              { statsViewModel.hard.value }),
          arrayOf(
              "getDailyQuestStats",
              "updateDailyQuestStats",
              { statsViewModel.getDailyQuestStats() },
              { statsViewModel.updateDailyQuestStats() },
              initialStats.dailyQuest,
              initialStats.dailyQuest + 1,
              { statsViewModel.daily.value }),
          arrayOf(
              "getWeeklyQuestStats",
              "updateWeeklyQuestStats",
              { statsViewModel.getWeeklyQuestStats() },
              { statsViewModel.updateWeeklyQuestStats() },
              initialStats.weeklyQuest,
              initialStats.weeklyQuest + 1,
              { statsViewModel.weekly.value }),
          arrayOf(
              "getCompletedChallengeStats",
              "updateCompletedChallengeStats",
              { statsViewModel.getCompletedChallengeStats() },
              { statsViewModel.updateCompletedChallengeStats() },
              initialStats.completedChallenge,
              initialStats.completedChallenge + 1,
              { statsViewModel.completed.value }),
          arrayOf(
              "getCreatedChallengeStats",
              "updateCreatedChallengeStats",
              { statsViewModel.getCreatedChallengeStats() },
              { statsViewModel.updateCreatedChallengeStats() },
              initialStats.createdChallenge,
              initialStats.createdChallenge + 1,
              { statsViewModel.created.value }),
          arrayOf(
              "getWonChallengeStats",
              "updateWonChallengeStats",
              { statsViewModel.getWonChallengeStats() },
              { statsViewModel.updateWonChallengeStats() },
              initialStats.wonChallenge,
              initialStats.wonChallenge + 1,
              { statsViewModel.won.value }))
    }
  }
  // Issues with how the test case resets when parametrised,
  // doing 2 blocks one @BeforeClass and one @Before fixes it for the moment.
  @Before
  fun setupDependencies() {
    mockUserSession = MockDependencyProvider.userSession()
    mockStatsRepository = MockStatsRepository()
    userId = mockUserSession.getUserId()!!
    statsViewModel = StatsViewModel(mockUserSession, mockStatsRepository)
    mockStatsRepository.setStatsForUser(userId, initialStats)
  }

  @Test
  fun `getIntFunctions should handle success correctly`() {
    getAction()
    assertTrue(mockStatsRepository.wasMethodCalled(getStatName))
    assertEquals(expectedValueGet, stateFlowValue())
  }

  @Test
  fun `getIntFunctions should handle failure correctly`() {
    mockStatsRepository.shouldSucceed = false
    getAction()
    assertTrue(mockStatsRepository.wasMethodCalled(getStatName))
    assertEquals(0, stateFlowValue())
  }

  @Test
  fun `updateIntFunctions should handle success correctly`() {
    updateAction()

    assertTrue(mockStatsRepository.wasMethodCalled(updateStatName))

    getAction()

    assertEquals(expectedValueUpdate, stateFlowValue())
  }

  @Test
  fun `updateIntFunctions should handle failure correctly`() {
    mockStatsRepository.shouldSucceed = false

    updateAction()

    assertTrue(mockStatsRepository.wasMethodCalled(updateStatName))

    mockStatsRepository.shouldSucceed = true
    getAction()

    assertEquals(expectedValueGet, stateFlowValue())
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
}
