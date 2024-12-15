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
    assertEquals(1, challenge.round)
    assertEquals(emptyList<Long>(), challenge.player1Times)
    assertEquals(emptyList<Long>(), challenge.player2Times)
    assertEquals(listOf(false, false, false), challenge.player1RoundCompleted)
    assertEquals(listOf(false, false, false), challenge.player2RoundCompleted)
    assertEquals("not_started", challenge.gameStatus)
  }

  @Test
  fun `parameterized constructor initializes properties correctly`() {
    val player1Times = mutableListOf(5000L, 6000L, 7000L)
    val player2Times = mutableListOf(4000L, 3000L, 8000L)
    val player1RoundCompleted = mutableListOf(true, true, true)
    val player2RoundCompleted = mutableListOf(true, false, true)

    val challenge =
        Challenge(
            challengeId = "challenge123",
            player1 = "player1",
            player2 = "player2",
            mode = "chrono",
            round = 2,
            player1Times = player1Times,
            player2Times = player2Times,
            player1RoundCompleted = player1RoundCompleted,
            player2RoundCompleted = player2RoundCompleted,
            gameStatus = "in_progress")

    assertEquals("challenge123", challenge.challengeId)
    assertEquals("player1", challenge.player1)
    assertEquals("player2", challenge.player2)
    assertEquals("chrono", challenge.mode)
    assertEquals(2, challenge.round)
    assertEquals(player1Times, challenge.player1Times)
    assertEquals(player2Times, challenge.player2Times)
    assertEquals(player1RoundCompleted, challenge.player1RoundCompleted)
    assertEquals(player2RoundCompleted, challenge.player2RoundCompleted)
    assertEquals("in_progress", challenge.gameStatus)
  }

  @Test
  fun `equality check between two identical Challenge objects`() {
    val challenge1 =
        Challenge(
            challengeId = "challenge123",
            player1 = "player1",
            player2 = "player2",
            mode = "chrono",
            round = 2,
            player1Times = mutableListOf(5000L, 6000L, 7000L),
            player2Times = mutableListOf(4000L, 3000L, 8000L),
            player1RoundCompleted = mutableListOf(true, true, true),
            player2RoundCompleted = mutableListOf(true, false, true),
            gameStatus = "in_progress")
    val challenge2 =
        Challenge(
            challengeId = "challenge123",
            player1 = "player1",
            player2 = "player2",
            mode = "chrono",
            round = 2,
            player1Times = mutableListOf(5000L, 6000L, 7000L),
            player2Times = mutableListOf(4000L, 3000L, 8000L),
            player1RoundCompleted = mutableListOf(true, true, true),
            player2RoundCompleted = mutableListOf(true, false, true),
            gameStatus = "in_progress")

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
            mode = "chrono",
            round = 2,
            player1Times = mutableListOf(5000L, 6000L, 7000L),
            player2Times = mutableListOf(4000L, 3000L, 8000L),
            player1RoundCompleted = mutableListOf(true, true, true),
            player2RoundCompleted = mutableListOf(true, false, true),
            gameStatus = "in_progress")

    val modifiedChallenge = challenge.copy(round = 3)

    assertEquals("challenge123", modifiedChallenge.challengeId)
    assertEquals("player1", modifiedChallenge.player1)
    assertEquals("player2", modifiedChallenge.player2)
    assertEquals("chrono", modifiedChallenge.mode)
    assertEquals(3, modifiedChallenge.round) // Modified
    assertEquals(challenge.player1Times, modifiedChallenge.player1Times)
    assertEquals(challenge.player2Times, modifiedChallenge.player2Times)
    assertEquals(challenge.player1RoundCompleted, modifiedChallenge.player1RoundCompleted)
    assertEquals(challenge.player2RoundCompleted, modifiedChallenge.player2RoundCompleted)
    assertEquals(challenge.gameStatus, modifiedChallenge.gameStatus)
  }
}
