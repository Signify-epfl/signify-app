package com.github.se.signify.model.challenge

import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.di.MockDependencyProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ChallengeViewModelTest {
  private lateinit var mockUserSession: UserSession
  private lateinit var mockRepository: MockChallengeRepository
  private lateinit var challengeViewModel: ChallengeViewModel

  private val challengeId = "challengeId"
  private val player1Id = "player1Id"
  private val player2Id = "player2Id"
  private val mode = ChallengeMode.SPRINT

  @Before
  fun setUp() {
    mockUserSession = MockDependencyProvider.userSession()
    mockRepository = MockChallengeRepository()
    challengeViewModel = ChallengeViewModel(mockUserSession, mockRepository)
  }

  @Test
  fun `sendChallengeRequest triggers onSuccess and logs message`() {
    challengeViewModel.sendChallengeRequest(player2Id, mode, challengeId)

    assertTrue(mockRepository.wasSendChallengeCalled())
    assertEquals(challengeId, mockRepository.lastSentChallengeId())
  }

  @Test
  fun `sendChallengeRequest triggers onFailure and logs error`() {
    mockRepository.shouldSucceed = false

    challengeViewModel.sendChallengeRequest(player2Id, mode, challengeId)

    assertTrue(mockRepository.wasSendChallengeCalled())
    assertEquals(challengeId, mockRepository.lastSentChallengeId())
  }

  @Test
  fun `deleteChallenge triggers onSuccess and logs message`() {
    mockRepository.sendChallengeRequest(
        player1Id = player1Id,
        player2Id = player2Id,
        mode = mode,
        challengeId = challengeId,
        onSuccess = { /* Success */},
        onFailure = { /* No-op */})

    challengeViewModel.deleteChallenge(challengeId)

    assertTrue(mockRepository.wasDeleteChallengeCalled())
    assertEquals(challengeId, mockRepository.lastDeletedChallengeId())
  }

  @Test
  fun `deleteChallenge triggers onFailure and logs error`() {
    mockRepository.shouldSucceed = false
    challengeViewModel.deleteChallenge(challengeId)

    assertTrue(mockRepository.wasDeleteChallengeCalled())
    assertEquals(challengeId, mockRepository.lastDeletedChallengeId())
  }

  @Test
  fun `factory creates ChallengeViewModel with repository`() {
    val factory = ChallengeViewModel.factory(mockUserSession, mockRepository)
    val viewModel = factory.create(ChallengeViewModel::class.java)
    assertNotNull(viewModel)
  }
}
