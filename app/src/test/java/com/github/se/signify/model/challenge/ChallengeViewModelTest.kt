package com.github.se.signify.model.challenge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ChallengeViewModelTest {
  private lateinit var mockRepository: MockChallengeRepository
  private lateinit var challengeViewModel: ChallengeViewModel

  private val challengeId = "challengeId"
  private val player1Id = "player1Id"
  private val player2Id = "player2Id"
  private val mode = ChallengeMode.SPRINT

  @Before
  fun setUp() {
    mockRepository = MockChallengeRepository()
    challengeViewModel = ChallengeViewModel(mockRepository)
  }

  @Test
  fun `sendChallengeRequest triggers onSuccess and logs message`() {
    mockRepository.shouldSucceed = true // Simulate a successful repository operation

    challengeViewModel.sendChallengeRequest(player1Id, player2Id, mode, challengeId)

    assertTrue(mockRepository.sendChallengeCalled) // Verify repository interaction
    assertEquals(challengeId, mockRepository.lastSentChallenge) // Verify challenge details
  }

  @Test
  fun `sendChallengeRequest triggers onFailure and logs error`() {
    mockRepository.shouldSucceed = false // Simulate a failed repository operation
    mockRepository.exceptionToThrow = Exception("Simulated failure")

    challengeViewModel.sendChallengeRequest(player1Id, player2Id, mode, challengeId)

    assertTrue(mockRepository.sendChallengeCalled) // Verify repository interaction
    assertEquals(challengeId, mockRepository.lastSentChallenge) // Verify challenge details
  }

  @Test
  fun `deleteChallenge triggers onSuccess and logs message`() {
    mockRepository.shouldSucceed = true // Simulate a successful repository operation

    mockRepository.sendChallengeRequest(
        player1Id = player1Id,
        player2Id = player2Id,
        mode = mode,
        challengeId = challengeId,
        onSuccess = { /* Success */},
        onFailure = { /* No-op */})

    challengeViewModel.deleteChallenge(challengeId)

    assertTrue(mockRepository.deleteChallengeCalled) // Verify repository interaction
    assertEquals(challengeId, mockRepository.lastDeletedChallenge) // Verify challenge details
  }

  @Test
  fun `deleteChallenge triggers onFailure and logs error`() {
    // Arrange
    mockRepository.shouldSucceed = false // Simulate a failed repository operation
    mockRepository.exceptionToThrow = Exception("Simulated delete failure")

    challengeViewModel.deleteChallenge(challengeId)

    assertTrue(mockRepository.deleteChallengeCalled) // Verify repository interaction
    assertEquals(challengeId, mockRepository.lastDeletedChallenge) // Verify challenge details
    // Optionally, verify log output (requires mocking Log)
  }

  @Test
  fun `factory creates ChallengeViewModel with repository`() {
    val mockRepository = MockChallengeRepository()
    val factory = ChallengeViewModel.factory(mockRepository)
    val viewModel = factory.create(ChallengeViewModel::class.java)
    assertNotNull(viewModel)
  }
}
