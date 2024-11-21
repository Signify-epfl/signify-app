package com.github.se.signify.model.challenge

class MockChallengeRepository : ChallengeRepository {
  private val challenges = mutableMapOf<String, Challenge>()

  var shouldSucceed: Boolean = true // Determines whether the operation succeeds
  var exceptionToThrow: Exception = Exception("Simulated failure") // Exception for failures

  var sendChallengeCalled: Boolean = false
  var deleteChallengeCalled: Boolean = false

  var lastSentChallenge: String? = null
  var lastDeletedChallenge: String? = null

  override fun sendChallengeRequest(
      player1Id: String,
      player2Id: String,
      mode: ChallengeMode,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    sendChallengeCalled = true
    lastSentChallenge = challengeId

    if (shouldSucceed) {
      val newChallenge =
          Challenge(
              challengeId = challengeId,
              player1 = player1Id,
              player2 = player2Id,
              mode = mode.name,
              status = "pending")
      challenges[challengeId] = newChallenge
      onSuccess()
    } else {
      onFailure(exceptionToThrow)
    }
  }

  override fun deleteChallenge(
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    deleteChallengeCalled = true
    lastDeletedChallenge = challengeId

    if (shouldSucceed) {
      if (challenges.remove(challengeId) != null) {
        onSuccess()
      } else {
        onFailure(Exception("Challenge with ID $challengeId not found"))
      }
    } else {
      onFailure(exceptionToThrow)
    }
  }

  fun getChallenge(challengeId: String): Challenge? {
    return challenges[challengeId]
  }

  fun getAllChallenges(): List<Challenge> {
    return challenges.values.toList()
  }

  fun setChallenges(newChallenges: List<Challenge>) {
    challenges.clear()
    challenges.putAll(newChallenges.associateBy { it.challengeId })
  }
}
