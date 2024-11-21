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
  fun getLettersLearnedShouldUpdateLettersLearnedFlowOnSuccess() {
    statsViewModel.getLettersLearned(userId)
    verify(statsRepositoryFirestore).getLettersLearned(eq(userId), any(), any())
  }

  @Test
  fun getEasyExerciseStatsShouldUpdateEasyFlowOnSuccess() {
    statsViewModel.getEasyExerciseStats(userId)
    verify(statsRepositoryFirestore).getEasyExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getMediumExerciseStatsShouldUpdateMediumFlowOnSuccess() {
    statsViewModel.getMediumExerciseStats(userId)
    verify(statsRepositoryFirestore).getMediumExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getHardExerciseStatsShouldUpdateHardFlowOnSuccess() {
    statsViewModel.getHardExerciseStats(userId)
    verify(statsRepositoryFirestore).getHardExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getDailyQuestStatsShouldUpdateDailyFlowOnSuccess() {
    statsViewModel.getDailyQuestStats(userId)
    verify(statsRepositoryFirestore).getDailyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun getWeeklyQuestStatsShouldUpdateWeeklyFlowOnSuccess() {
    statsViewModel.getWeeklyQuestStats(userId)
    verify(statsRepositoryFirestore).getWeeklyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun getCompletedChallengeStatsShouldUpdateCompletedFlowOnSuccess() {
    statsViewModel.getCompletedChallengeStats(userId)
    verify(statsRepositoryFirestore).getCompletedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun getCreatedChallengeStatsShouldUpdateCreatedFlowOnSuccess() {
    statsViewModel.getCreatedChallengeStats(userId)
    verify(statsRepositoryFirestore).getCreatedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun `updateLettersLearned should call repository updateLettersLearned`() {
    val newLetter = 'D'
    statsViewModel.updateLettersLearned(userId, newLetter)
    verify(statsRepositoryFirestore).updateLettersLearned(eq(userId), eq(newLetter), any(), any())
  }

  @Test
  fun `updateEasyExerciseStats should call repository updateEasyExerciseStats`() {
    statsViewModel.updateEasyExerciseStats(userId)
    verify(statsRepositoryFirestore).updateEasyExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun `updateMediumExerciseStats should call repository updateMediumExerciseStats`() {
    statsViewModel.updateMediumExerciseStats(userId)
    verify(statsRepositoryFirestore).updateMediumExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun `updateHardExerciseStats should call repository updateHardExerciseStats`() {
    statsViewModel.updateHardExerciseStats(userId)
    verify(statsRepositoryFirestore).updateHardExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun `updateDailyQuestStats should call repository updateDailyQuestStats`() {
    statsViewModel.updateDailyQuestStats(userId)
    verify(statsRepositoryFirestore).updateDailyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun `updateWeeklyQuestStats should call repository updateWeeklyQuestStats`() {
    statsViewModel.updateWeeklyQuestStats(userId)
    verify(statsRepositoryFirestore).updateWeeklyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun `updateCompletedChallengeStats should call repository updateCompletedChallengeStats`() {
    statsViewModel.updateCompletedChallengeStats(userId)
    verify(statsRepositoryFirestore).updateCompletedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun `updateCreatedChallengeStats should call repository updateCreatedChallengeStats`() {
    statsViewModel.updateCreatedChallengeStats(userId)
    verify(statsRepositoryFirestore).updateCreatedChallengeStats(eq(userId), any(), any())
  }
}
