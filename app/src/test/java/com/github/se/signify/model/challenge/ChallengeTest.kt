package com.github.se.signify.model.challenge

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ChallengeTest {

  @Test
  fun `default constructor initializes properties correctly`() {
    val challenge = Challenge()

    assertEquals("", challenge.challengeId)
    assertEquals("", challenge.player1)
    assertEquals("", challenge.player2)
    assertEquals("", challenge.mode)
    assertEquals("pending", challenge.status)
    assertEquals(1, challenge.round)
    assertEquals(0, challenge.player1Score)
    assertEquals(0, challenge.player2Score)
    assertEquals("", challenge.currentGesture)
    assertEquals(emptyMap<String, String>(), challenge.responses)
  }

  @Test
  fun `parameterized constructor initializes properties correctly`() {
    val responses = mapOf("player1" to "response1", "player2" to "response2")
    val challenge =
        Challenge(
            challengeId = "challenge123",
            player1 = "player1",
            player2 = "player2",
            mode = "sprint",
            status = "active",
            round = 2,
            player1Score = 5,
            player2Score = 3,
            currentGesture = "wave",
            responses = responses)

    assertEquals("challenge123", challenge.challengeId)
    assertEquals("player1", challenge.player1)
    assertEquals("player2", challenge.player2)
    assertEquals("sprint", challenge.mode)
    assertEquals("active", challenge.status)
    assertEquals(2, challenge.round)
    assertEquals(5, challenge.player1Score)
    assertEquals(3, challenge.player2Score)
    assertEquals("wave", challenge.currentGesture)
    assertEquals(responses, challenge.responses)
  }

  @Test
  fun `equality check between two identical Challenge objects`() {
    val challenge1 =
        Challenge(
            challengeId = "challenge123",
            player1 = "player1",
            player2 = "player2",
            mode = "sprint",
            status = "active",
            round = 2,
            player1Score = 5,
            player2Score = 3,
            currentGesture = "wave",
            responses = mapOf("player1" to "response1"))
    val challenge2 =
        Challenge(
            challengeId = "challenge123",
            player1 = "player1",
            player2 = "player2",
            mode = "sprint",
            status = "active",
            round = 2,
            player1Score = 5,
            player2Score = 3,
            currentGesture = "wave",
            responses = mapOf("player1" to "response1"))

    assertEquals(challenge1, challenge2)
    assertEquals(challenge1.hashCode(), challenge2.hashCode())
  }

  @Test
  fun `inequality check between different Challenge objects`() {
    val challenge1 = Challenge(challengeId = "challenge123")
    val challenge2 = Challenge(challengeId = "challenge456")

    assertNotEquals(challenge1, challenge2)
  }

  @Test
  fun `copy function creates a new Challenge with modified properties`() {
    val challenge =
        Challenge(
            challengeId = "challenge123",
            player1 = "player1",
            player2 = "player2",
            mode = "sprint",
            status = "active",
            round = 2,
            player1Score = 5,
            player2Score = 3,
            currentGesture = "wave",
            responses = mapOf("player1" to "response1"))

    val modifiedChallenge = challenge.copy(status = "completed", player1Score = 10)

    assertEquals("challenge123", modifiedChallenge.challengeId)
    assertEquals("player1", modifiedChallenge.player1)
    assertEquals("player2", modifiedChallenge.player2)
    assertEquals("sprint", modifiedChallenge.mode)
    assertEquals("completed", modifiedChallenge.status) // Modified
    assertEquals(2, modifiedChallenge.round)
    assertEquals(10, modifiedChallenge.player1Score) // Modified
    assertEquals(3, modifiedChallenge.player2Score)
    assertEquals("wave", modifiedChallenge.currentGesture)
    assertEquals(mapOf("player1" to "response1"), modifiedChallenge.responses)
  }
}
