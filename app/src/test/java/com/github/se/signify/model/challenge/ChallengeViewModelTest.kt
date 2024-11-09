package com.github.se.signify.model.challenge

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

class ChallengeViewModelTest {

  private lateinit var challengeRepository: ChallengeRepository
  private lateinit var challengeViewModel: ChallengeViewModel

  private val challengeId = "challengeId"
  private val player1Id = "player1Id"
  private val player2Id = "player2Id"
  private val mode = "sprint"

  @Before
  fun setUp() {
    challengeRepository = mock(ChallengeRepository::class.java)
    challengeViewModel = ChallengeViewModel(challengeRepository)
  }

  @Test
  fun sendChallengeRequest_callsRepository() {
    // Act
    challengeViewModel.sendChallengeRequest(player1Id, player2Id, mode, challengeId)

    // Assert: Verify that the repository's sendChallengeRequest method was called
    verify(challengeRepository)
        .sendChallengeRequest(eq(player1Id), eq(player2Id), eq(mode), eq(challengeId), any(), any())
  }

  @Test
  fun deleteChallenge_callsRepository() {
    // Act
    challengeViewModel.deleteChallenge(challengeId)

    // Assert: Verify that the repository's deleteChallenge method was called
    verify(challengeRepository).deleteChallenge(eq(challengeId), any(), any())
  }
}
