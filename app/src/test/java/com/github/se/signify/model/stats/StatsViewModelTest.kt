package com.github.se.signify.model.stats

import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.di.MockDependencyProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

class StatsViewModelTest {

  private lateinit var userSession: UserSession
  private lateinit var statsRepositoryFirestore: StatsRepository
  private lateinit var statsViewModel: StatsViewModel

  private lateinit var userId: String

  @Before
  fun setUp() {
    userSession = MockDependencyProvider.userSession()
    statsRepositoryFirestore = mock(StatsRepository::class.java)
    statsViewModel = StatsViewModel(userSession, statsRepositoryFirestore)

    userId = userSession.getUserId()!!
  }

  @Test
  fun getLettersLearnedShouldCallRepositoryGetLettersLearned() {
    statsViewModel.getLettersLearned()
    verify(statsRepositoryFirestore).getLettersLearned(eq(userId), any(), any())
  }

  @Test
  fun getEasyExerciseStatsShouldCallRepositoryGetEasyExerciseStats() {
    statsViewModel.getEasyExerciseStats()
    verify(statsRepositoryFirestore).getEasyExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getMediumExerciseStatsShouldCallRepositoryGetMediumExerciseStats() {
    statsViewModel.getMediumExerciseStats()
    verify(statsRepositoryFirestore).getMediumExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getHardExerciseStatsShouldCallRepositoryGetHardExerciseStats() {
    statsViewModel.getHardExerciseStats()
    verify(statsRepositoryFirestore).getHardExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getDailyQuestStatsShouldCallRepositoryGetDailyQuestStats() {
    statsViewModel.getDailyQuestStats()
    verify(statsRepositoryFirestore).getDailyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun getWeeklyQuestStatsShouldCallRepositoryGetWeeklyQuestStats() {
    statsViewModel.getWeeklyQuestStats()
    verify(statsRepositoryFirestore).getWeeklyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun getCompletedChallengeStatsShouldCallRepositoryGetCompletedChallengeStats() {
    statsViewModel.getCompletedChallengeStats()
    verify(statsRepositoryFirestore).getCompletedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun getCreatedChallengeStatsShouldCallRepositoryGetCreatedChallengeStats() {
    statsViewModel.getCreatedChallengeStats()
    verify(statsRepositoryFirestore).getCreatedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun getWonChallengeStatsShouldCallRepositoryGetWonChallengeStats() {
    statsViewModel.getWonChallengeStats()
    verify(statsRepositoryFirestore).getWonChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun updateLettersLearnedShouldCallRepositoryUpdateLettersLearned() {
    val newLetter = 'D'
    statsViewModel.updateLettersLearned(newLetter)
    verify(statsRepositoryFirestore).updateLettersLearned(eq(userId), eq(newLetter), any(), any())
  }

  @Test
  fun updateEasyExerciseStatsShouldCallRepositoryUpdateEasyExerciseStats() {
    statsViewModel.updateEasyExerciseStats()
    verify(statsRepositoryFirestore).updateEasyExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun updateMediumExerciseStatsShouldCallRepositoryUpdateMediumExerciseStats() {
    statsViewModel.updateMediumExerciseStats()
    verify(statsRepositoryFirestore).updateMediumExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun updateHardExerciseStatsShouldCallRepositoryUpdateHardExerciseStats() {
    statsViewModel.updateHardExerciseStats()
    verify(statsRepositoryFirestore).updateHardExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun updateDailyQuestStatsShouldCallRepositoryUpdateDailyQuestStats() {
    statsViewModel.updateDailyQuestStats()
    verify(statsRepositoryFirestore).updateDailyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun updateWeeklyQuestStatsShouldCallRepositoryUpdateWeeklyQuestStats() {
    statsViewModel.updateWeeklyQuestStats()
    verify(statsRepositoryFirestore).updateWeeklyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun updateCompletedChallengeStatsShouldCallRepositoryUpdateCompletedChallengeStats() {
    statsViewModel.updateCompletedChallengeStats()
    verify(statsRepositoryFirestore).updateCompletedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun updateCreatedChallengeStatsShouldCallRepositoryUpdateCreatedChallengeStats() {
    statsViewModel.updateCreatedChallengeStats()
    verify(statsRepositoryFirestore).updateCreatedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun updateWonChallengeStatsShouldCallRepositoryUpdateWonChallengeStats() {
    statsViewModel.updateWonChallengeStats()
    verify(statsRepositoryFirestore).updateWonChallengeStats(eq(userId), any(), any())
  }
}
