package com.github.se.signify.model.challenge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class MockChallengeRepositoryTest {

  private lateinit var mockRepository: MockChallengeRepository
  private val player1Id = "player1"
  private val player2Id = "player2"
  private val challengeId = "challenge123"
  private val mode = ChallengeMode.SPRINT

  @Before
  fun setUp() {
    mockRepository = MockChallengeRepository()
  }

  @Test
  fun `sendChallengeRequest succeeds when shouldSucceed is true`() {
    mockRepository.shouldSucceed = true
    mockRepository.sendChallengeRequest(
        player1Id,
        player2Id,
        mode,
        challengeId,
        onSuccess = {},
        onFailure = { fail("onFailure should not be called") })
    assertNotNull(mockRepository.getChallenge(challengeId))
  }

  @Test
  fun `sendChallengeRequest fails when shouldSucceed is false`() {
    mockRepository.shouldSucceed = false
    mockRepository.sendChallengeRequest(
        player1Id,
        player2Id,
        mode,
        challengeId,
        onSuccess = { fail("onSuccess should not be called") },
        onFailure = { /* Success */})
  }

  @Test
  fun `deleteChallenge succeeds when challenge exists`() {
    mockRepository.shouldSucceed = true
    mockRepository.sendChallengeRequest(player1Id, player2Id, mode, challengeId, {}, {})
    mockRepository.deleteChallenge(
        challengeId, onSuccess = {}, onFailure = { fail("onFailure should not be called") })
    assertNull(mockRepository.getChallenge(challengeId))
  }

  @Test
  fun `deleteChallenge fails when challenge does not exist`() {
    mockRepository.shouldSucceed = true
    mockRepository.deleteChallenge(
        challengeId, onSuccess = { fail("onSuccess should not be called") }, onFailure = {})
  }

  @Test
  fun `deleteChallenge fails when shouldSucceed is false`() {
    mockRepository.shouldSucceed = false
    mockRepository.deleteChallenge(
        challengeId, onSuccess = { fail("onSuccess should not be called") }, onFailure = {})
  }

  @Test
  fun `getAllChallenges returns all stored challenges`() {
    mockRepository.shouldSucceed = true
    mockRepository.sendChallengeRequest(player1Id, player2Id, mode, challengeId, {}, {})
    assertEquals(1, mockRepository.getAllChallenges().size)
  }

  @Test
  fun `sendChallengeRequest sets sendChallengeCalled to true`() {
    mockRepository.sendChallengeRequest(
        player1Id = "player1",
        player2Id = "player2",
        mode = ChallengeMode.SPRINT,
        challengeId = "challenge123",
        onSuccess = {},
        onFailure = {})
    assertTrue(mockRepository.sendChallengeCalled)
  }

  @Test
  fun `deleteChallenge sets deleteChallengeCalled to true`() {
    mockRepository.deleteChallenge(challengeId = "challenge123", onSuccess = {}, onFailure = {})
    assertTrue(mockRepository.deleteChallengeCalled)
  }

  @Test
  fun `sendChallengeRequest updates lastSentChallenge`() {
    mockRepository.sendChallengeRequest(
        player1Id = "player1",
        player2Id = "player2",
        mode = ChallengeMode.SPRINT,
        challengeId = "challenge123",
        onSuccess = {},
        onFailure = {})
    assertEquals("challenge123", mockRepository.lastSentChallenge)
  }

  @Test
  fun `deleteChallenge updates lastDeletedChallenge`() {
    mockRepository.sendChallengeRequest(
        player1Id = "player1",
        player2Id = "player2",
        mode = ChallengeMode.SPRINT,
        challengeId = "challenge123",
        onSuccess = {},
        onFailure = {})
    mockRepository.deleteChallenge(challengeId = "challenge123", onSuccess = {}, onFailure = {})
    assertEquals("challenge123", mockRepository.lastDeletedChallenge)
  }
}
