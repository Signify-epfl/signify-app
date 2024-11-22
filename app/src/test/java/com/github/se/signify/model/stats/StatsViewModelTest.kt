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

  private val userId = "testUser"

  @Before
  fun setUp() {
    userSession = MockDependencyProvider.userSession()
    statsRepositoryFirestore = mock(StatsRepository::class.java)
    statsViewModel = StatsViewModel(userSession, statsRepositoryFirestore)
  }

  @Test
  fun getLettersLearnedShouldUpdateLettersLearnedFlowOnSuccess() {
    statsViewModel.getLettersLearned()
    verify(statsRepositoryFirestore).getLettersLearned(eq(userId), any(), any())
  }

  @Test
  fun getEasyExerciseStatsShouldUpdateEasyFlowOnSuccess() {
    statsViewModel.getEasyExerciseStats()
    verify(statsRepositoryFirestore).getEasyExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getMediumExerciseStatsShouldUpdateMediumFlowOnSuccess() {
    statsViewModel.getMediumExerciseStats()
    verify(statsRepositoryFirestore).getMediumExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getHardExerciseStatsShouldUpdateHardFlowOnSuccess() {
    statsViewModel.getHardExerciseStats()
    verify(statsRepositoryFirestore).getHardExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun getDailyQuestStatsShouldUpdateDailyFlowOnSuccess() {
    statsViewModel.getDailyQuestStats()
    verify(statsRepositoryFirestore).getDailyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun getWeeklyQuestStatsShouldUpdateWeeklyFlowOnSuccess() {
    statsViewModel.getWeeklyQuestStats()
    verify(statsRepositoryFirestore).getWeeklyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun getCompletedChallengeStatsShouldUpdateCompletedFlowOnSuccess() {
    statsViewModel.getCompletedChallengeStats()
    verify(statsRepositoryFirestore).getCompletedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun getCreatedChallengeStatsShouldUpdateCreatedFlowOnSuccess() {
    statsViewModel.getCreatedChallengeStats()
    verify(statsRepositoryFirestore).getCreatedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun `updateLettersLearned should call repository updateLettersLearned`() {
    val newLetter = 'D'
    statsViewModel.updateLettersLearned(newLetter)
    verify(statsRepositoryFirestore).updateLettersLearned(eq(userId), eq(newLetter), any(), any())
  }

  @Test
  fun `updateEasyExerciseStats should call repository updateEasyExerciseStats`() {
    statsViewModel.updateEasyExerciseStats()
    verify(statsRepositoryFirestore).updateEasyExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun `updateMediumExerciseStats should call repository updateMediumExerciseStats`() {
    statsViewModel.updateMediumExerciseStats()
    verify(statsRepositoryFirestore).updateMediumExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun `updateHardExerciseStats should call repository updateHardExerciseStats`() {
    statsViewModel.updateHardExerciseStats()
    verify(statsRepositoryFirestore).updateHardExerciseStats(eq(userId), any(), any())
  }

  @Test
  fun `updateDailyQuestStats should call repository updateDailyQuestStats`() {
    statsViewModel.updateDailyQuestStats()
    verify(statsRepositoryFirestore).updateDailyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun `updateWeeklyQuestStats should call repository updateWeeklyQuestStats`() {
    statsViewModel.updateWeeklyQuestStats()
    verify(statsRepositoryFirestore).updateWeeklyQuestStats(eq(userId), any(), any())
  }

  @Test
  fun `updateCompletedChallengeStats should call repository updateCompletedChallengeStats`() {
    statsViewModel.updateCompletedChallengeStats()
    verify(statsRepositoryFirestore).updateCompletedChallengeStats(eq(userId), any(), any())
  }

  @Test
  fun `updateCreatedChallengeStats should call repository updateCreatedChallengeStats`() {
    statsViewModel.updateCreatedChallengeStats()
    verify(statsRepositoryFirestore).updateCreatedChallengeStats(eq(userId), any(), any())
  }
}
