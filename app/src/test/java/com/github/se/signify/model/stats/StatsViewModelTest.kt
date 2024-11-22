package com.github.se.signify.model.stats

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

class StatsViewModelTest {

  private lateinit var statsRepositoryFirestore: StatsRepository
  private lateinit var statsViewModel: StatsViewModel

  private val userId = "testUser"

  @Before
  fun setUp() {
    statsRepositoryFirestore = mock(StatsRepository::class.java)
    statsViewModel = StatsViewModel(statsRepositoryFirestore)
  }

  @Test
  fun getLettersLearnedShouldCallRepositoryGetLettersLearned() {
    statsViewModel.getLettersLearned(userId)
    verify(statsRepositoryFirestore).getLettersLearned(eq(userId), any(), any())
  }

  @Test
  fun getEasyExerciseStatsShouldCallRepositoryGetEasyExerciseStats() {
    statsViewModel.getEasyExerciseStats(userId)
    verify(statsRepositoryFirestore).getEasyExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getMediumExerciseStatsShouldCallRepositoryGetMediumExerciseStats() {
    statsViewModel.getMediumExerciseStats(userId)
    verify(statsRepositoryFirestore).getMediumExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getHardExerciseStatsShouldCallRepositoryGetHardExerciseStats() {
    statsViewModel.getHardExerciseStats(userId)
    verify(statsRepositoryFirestore).getHardExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getDailyQuestStatsShouldCallRepositoryGetDailyQuestStats() {
    statsViewModel.getDailyQuestStats(userId)
    verify(statsRepositoryFirestore).getDailyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun getWeeklyQuestStatsShouldCallRepositoryGetWeeklyQuestStats() {
    statsViewModel.getWeeklyQuestStats(userId)
    verify(statsRepositoryFirestore).getWeeklyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun getCompletedChallengeStatsShouldCallRepositoryGetCompletedChallengeStats() {
    statsViewModel.getCompletedChallengeStats(userId)
    verify(statsRepositoryFirestore).getCompletedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun getCreatedChallengeStatsShouldCallRepositoryGetCreatedChallengeStats() {
    statsViewModel.getCreatedChallengeStats(userId)
    verify(statsRepositoryFirestore).getCreatedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun getWonChallengeStatsShouldCallRepositoryGetWonChallengeStats() {
    statsViewModel.getWonChallengeStats(userId)
    verify(statsRepositoryFirestore).getWonChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun updateLettersLearnedShouldCallRepositoryUpdateLettersLearned() {
    val newLetter = 'D'
    statsViewModel.updateLettersLearned(userId, newLetter)
    verify(statsRepositoryFirestore).updateLettersLearned(eq(userId), eq(newLetter), any(), any())
  }

  @Test
  fun updateEasyExerciseStatsShouldCallRepositoryUpdateEasyExerciseStats() {
    statsViewModel.updateEasyExerciseStats(userId)
    verify(statsRepositoryFirestore).updateEasyExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun updateMediumExerciseStatsShouldCallRepositoryUpdateMediumExerciseStats() {
    statsViewModel.updateMediumExerciseStats(userId)
    verify(statsRepositoryFirestore).updateMediumExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun updateHardExerciseStatsShouldCallRepositoryUpdateHardExerciseStats() {
    statsViewModel.updateHardExerciseStats(userId)
    verify(statsRepositoryFirestore).updateHardExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun updateDailyQuestStatsShouldCallRepositoryUpdateDailyQuestStats() {
    statsViewModel.updateDailyQuestStats(userId)
    verify(statsRepositoryFirestore).updateDailyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun updateWeeklyQuestStatsShouldCallRepositoryUpdateWeeklyQuestStats() {
    statsViewModel.updateWeeklyQuestStats(userId)
    verify(statsRepositoryFirestore).updateWeeklyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun updateCompletedChallengeStatsShouldCallRepositoryUpdateCompletedChallengeStats() {
    statsViewModel.updateCompletedChallengeStats(userId)
    verify(statsRepositoryFirestore).updateCompletedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun updateCreatedChallengeStatsShouldCallRepositoryUpdateCreatedChallengeStats() {
    statsViewModel.updateCreatedChallengeStats(userId)
    verify(statsRepositoryFirestore).updateCreatedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun updateWonChallengeStatsShouldCallRepositoryUpdateWonChallengeStats() {
    statsViewModel.updateWonChallengeStats(userId)
    verify(statsRepositoryFirestore).updateWonChallengeStats(eq(userId), any(), any())
  }
}
