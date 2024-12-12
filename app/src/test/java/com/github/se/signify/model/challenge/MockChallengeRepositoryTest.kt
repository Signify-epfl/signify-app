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
    mockRepository.sendChallengeRequest(
        player1Id,
        player2Id,
        mode,
        challengeId,
        listOf("A", "B", "C"),
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
        listOf("A", "B", "C"),
        onSuccess = { fail("onSuccess should not be called") },
        onFailure = { exception -> assertEquals("Simulated failure", exception.message) })
  }

  @Test
  fun `deleteChallenge succeeds when challenge exists`() {
    mockRepository.sendChallengeRequest(
        player1Id, player2Id, mode, challengeId, listOf("A", "B", "C"), {}, {})
    mockRepository.deleteChallenge(
        challengeId, onSuccess = {}, onFailure = { fail("onFailure should not be called") })
    assertNull(mockRepository.getChallenge(challengeId))
  }

  @Test
  fun `deleteChallenge fails when challenge does not exist`() {
    mockRepository.deleteChallenge(
        challengeId, onSuccess = { fail("onSuccess should not be called") }, onFailure = {})
  }

  @Test
  fun `deleteChallenge fails when shouldSucceed is false`() {
    mockRepository.shouldSucceed = false
    mockRepository.deleteChallenge(
        challengeId,
        onSuccess = { fail("onSuccess should not be called") },
        onFailure = { exception -> assertEquals("Simulated failure", exception.message) })
  }

  @Test
  fun `getAllChallenges returns all stored challenges`() {
    mockRepository.sendChallengeRequest(
        player1Id, player2Id, mode, challengeId, listOf("A", "B", "C"), {}, {})
    assertEquals(1, mockRepository.getAllChallenges().size)
  }

  @Test
  fun `sendChallengeRequest sets sendChallengeCalled to true`() {
    mockRepository.sendChallengeRequest(
        player1Id = "player1",
        player2Id = "player2",
        mode = ChallengeMode.SPRINT,
        challengeId = "challenge123",
        listOf("A", "B", "C"),
        onSuccess = {},
        onFailure = {})
    assertTrue(mockRepository.wasSendChallengeCalled())
  }

  @Test
  fun `deleteChallenge sets deleteChallengeCalled to true`() {
    mockRepository.deleteChallenge(challengeId = "challenge123", onSuccess = {}, onFailure = {})
    assertTrue(mockRepository.wasDeleteChallengeCalled())
  }

  @Test
  fun `sendChallengeRequest updates lastSentChallenge`() {
    mockRepository.sendChallengeRequest(
        player1Id = "player1",
        player2Id = "player2",
        mode = ChallengeMode.SPRINT,
        challengeId = "challenge123",
        listOf("A", "B", "C"),
        onSuccess = {},
        onFailure = {})
    assertEquals("challenge123", mockRepository.lastSentChallengeId())
  }

  @Test
  fun `deleteChallenge updates lastDeletedChallenge`() {
    mockRepository.sendChallengeRequest(
        player1Id = "player1",
        player2Id = "player2",
        mode = ChallengeMode.SPRINT,
        challengeId = "challenge123",
        listOf("A", "B", "C"),
        onSuccess = {},
        onFailure = {})
    mockRepository.deleteChallenge(challengeId = "challenge123", onSuccess = {}, onFailure = {})
    assertEquals("challenge123", mockRepository.lastDeletedChallengeId())
  }

  @Test
  fun `setChallenges correctly sets the list`() {
    val newChallenges =
        listOf(
            Challenge("challenge1", "player1", "player2", "Sprint"),
            Challenge("challenge2", "player3", "player4", "Chrono"))

    mockRepository.setChallenges(newChallenges)

    val challenges = mockRepository.getAllChallenges()
    assertEquals(2, challenges.size)
    assertNotNull(challenges[0])
    assertNotNull(challenges[1])
    assertEquals("player1", challenges[0].player1)
  }

  @Test
  fun `reset clears all tracked calls and challenges`() {
    mockRepository.setChallenges(
        listOf(Challenge("challenge1", "player1", "player2", "Sprint")))
    mockRepository.sendChallengeRequest(
        "player1", "player2", ChallengeMode.SPRINT, "challenge1", listOf("A", "B", "C"), {}, {})
    mockRepository.deleteChallenge("challenge1", {}, {})

    mockRepository.reset()

    assertTrue(mockRepository.getSendChallengeCalls().isEmpty())
    assertTrue(mockRepository.getDeleteChallengeCalls().isEmpty())
    assertTrue(mockRepository.getAllChallenges().isEmpty())
  }

  @Test
  fun `getSendChallengeCalls returns all sent challenges`() {
    mockRepository.sendChallengeRequest(
        "player1", "player2", ChallengeMode.SPRINT, "challenge1", listOf("A", "B", "C"), {}, {})
    mockRepository.sendChallengeRequest(
        "player3", "player4", ChallengeMode.CHRONO, "challenge2", listOf("A", "B", "C"), {}, {})

    val sendCalls = mockRepository.getSendChallengeCalls()

    assertEquals(2, sendCalls.size)
    assertEquals("challenge1", sendCalls[0].challengeId)
    assertEquals("challenge2", sendCalls[1].challengeId)
  }

  @Test
  fun `getDeleteChallengeCalls returns all deleted challenge IDs`() {
    mockRepository.setChallenges(
        listOf(
            Challenge("challenge1", "player1", "player2", "Sprint"),
            Challenge("challenge2", "player3", "player4", "Chrono")))
    mockRepository.deleteChallenge("challenge1", {}, {})
    mockRepository.deleteChallenge("challenge2", {}, {})

    val deleteCalls = mockRepository.getDeleteChallengeCalls()

    assertEquals(2, deleteCalls.size)
    assertEquals("challenge1", deleteCalls[0])
    assertEquals("challenge2", deleteCalls[1])
  }

  @Test
  fun `getChallengeById succeeds when challenge exists`() {
    mockRepository.sendChallengeRequest(
        player1Id, player2Id, mode, challengeId, listOf("A", "B", "C"), {}, {})
    mockRepository.getChallengeById(
        challengeId = challengeId,
        onSuccess = { challenge ->
          assertNotNull(challenge)
          assertEquals(challengeId, challenge.challengeId)
        },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun `getChallengeById fails when challenge does not exist`() {
    mockRepository.getChallengeById(
        challengeId = "nonExistentChallenge",
        onSuccess = { fail("onSuccess should not be called") },
        onFailure = { exception ->
          assertEquals("Challenge with ID nonExistentChallenge not found", exception.message)
        })
  }

  @Test
  fun `updateChallenge succeeds when challenge exists`() {
    mockRepository.sendChallengeRequest(
        player1Id, player2Id, mode, challengeId, listOf("A", "B", "C"), {}, {})
    val updatedChallenge =
        Challenge(
            challengeId = challengeId,
            player1 = player1Id,
            player2 = player2Id,
            mode = "Sprint")

    mockRepository.updateChallenge(
        updatedChallenge = updatedChallenge,
        onSuccess = {
          val retrievedChallenge = mockRepository.getChallenge(challengeId)
          assertNotNull(retrievedChallenge)
        },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun `updateChallenge fails when challenge does not exist`() {
    val updatedChallenge =
        Challenge(
            challengeId = "nonExistentChallenge",
            player1 = player1Id,
            player2 = player2Id,
            mode = "Sprint")

    mockRepository.updateChallenge(
        updatedChallenge = updatedChallenge,
        onSuccess = { fail("onSuccess should not be called") },
        onFailure = { exception ->
          assertEquals("Challenge with ID nonExistentChallenge not found", exception.message)
        })
  }

  @Test
  fun `recordPlayerTime succeeds when challenge and player exist`() {
    mockRepository.sendChallengeRequest(
        player1Id, player2Id, mode, challengeId, listOf("A", "B", "C"), {}, {})
    mockRepository.recordPlayerTime(
        challengeId = challengeId,
        playerId = player1Id,
        timeTaken = 5000L,
        onSuccess = {
          val challenge = mockRepository.getChallenge(challengeId)
          assertNotNull(challenge)
          assertTrue(challenge!!.player1Times.contains(5000L))
        },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun `recordPlayerTime fails when challenge does not exist`() {
    mockRepository.recordPlayerTime(
        challengeId = "nonExistentChallenge",
        playerId = player1Id,
        timeTaken = 5000L,
        onSuccess = { fail("onSuccess should not be called") },
        onFailure = { exception ->
          assertEquals("Challenge with ID nonExistentChallenge not found", exception.message)
        })
  }

  @Test
  fun `recordPlayerTime fails when player ID is invalid`() {
    mockRepository.sendChallengeRequest(
        player1Id, player2Id, mode, challengeId, listOf("A", "B", "C"), {}, {})
    mockRepository.recordPlayerTime(
        challengeId = challengeId,
        playerId = "invalidPlayer",
        timeTaken = 5000L,
        onSuccess = { fail("onSuccess should not be called") },
        onFailure = { exception -> assertEquals("Invalid player ID", exception.message) })
  }

    @Test
    fun `updateWinner succeeds when challenge exists`() {
        // Arrange: Add a challenge to the repository
        mockRepository.sendChallengeRequest(
            player1Id = player1Id,
            player2Id = player2Id,
            mode = mode,
            challengeId = challengeId,
            roundWords = listOf("A", "B", "C"),
            onSuccess = {},
            onFailure = { fail("onFailure should not be called") })

        val winnerId = player1Id

        // Act: Update the winner
        mockRepository.updateWinner(
            challengeId = challengeId,
            winnerId = winnerId,
            onSuccess = {
                // Assert: Verify the winner was updated successfully
                val updatedChallenge = mockRepository.getChallenge(challengeId)
                assertNotNull(updatedChallenge)
                assertEquals(winnerId, updatedChallenge?.winner)
            },
            onFailure = { fail("onFailure should not be called") })
    }

    @Test
    fun `updateWinner fails when challenge does not exist`() {
        // Arrange: Use a non-existent challenge ID
        val nonExistentChallengeId = "nonExistentChallenge"
        val winnerId = player1Id

        // Act: Attempt to update the winner
        mockRepository.updateWinner(
            challengeId = nonExistentChallengeId,
            winnerId = winnerId,
            onSuccess = { fail("onSuccess should not be called") },
            onFailure = { exception ->
                // Assert: Verify the failure callback is called with the correct message
                assertEquals("Challenge not found", exception.message)
            })
    }
}
