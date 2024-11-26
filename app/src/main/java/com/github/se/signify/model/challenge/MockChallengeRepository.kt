package com.github.se.signify.model.challenge

class MockChallengeRepository : ChallengeRepository {
  private val challenges = mutableMapOf<String, Challenge>()

  var shouldSucceed: Boolean = true
  private val exceptionToThrow: Exception = Exception("Simulated failure")

  private val sendChallengeCalls = mutableListOf<Challenge>()
  private val deleteChallengeCalls = mutableListOf<String>()

  fun lastSentChallengeId(): String? = sendChallengeCalls.lastOrNull()?.challengeId

  fun lastDeletedChallengeId(): String? = deleteChallengeCalls.lastOrNull()

  fun wasSendChallengeCalled(): Boolean = sendChallengeCalls.isNotEmpty()

  fun wasDeleteChallengeCalled(): Boolean = deleteChallengeCalls.isNotEmpty()

  fun getChallenge(challengeId: String): Challenge? = challenges[challengeId]

  fun getAllChallenges(): List<Challenge> = challenges.values.toList()

  fun getSendChallengeCalls(): List<Challenge> = sendChallengeCalls.toList()

  fun getDeleteChallengeCalls(): List<String> = deleteChallengeCalls.toList()

  fun setChallenges(newChallenges: List<Challenge>) {
    challenges.clear()
    challenges.putAll(newChallenges.associateBy { it.challengeId })
  }

  fun reset() {
    sendChallengeCalls.clear()
    deleteChallengeCalls.clear()
    challenges.clear()
  }

  override fun sendChallengeRequest(
      player1Id: String,
      player2Id: String,
      mode: ChallengeMode,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val newChallenge =
        Challenge(
            challengeId = challengeId,
            player1 = player1Id,
            player2 = player2Id,
            mode = mode.name,
            status = "pending")
    // track all calls, even unsuccessful ones
    sendChallengeCalls.add(newChallenge)
    if (!shouldSucceed) {
      onFailure(exceptionToThrow)
    } else {
      challenges[challengeId] = newChallenge
      onSuccess()
    }
  }

  override fun deleteChallenge(
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    deleteChallengeCalls.add(challengeId)
    if (!shouldSucceed) {
      onFailure(exceptionToThrow)
      return
    }
    if (!challenges.containsKey(challengeId)) {
      onFailure(Exception("Challenge with ID $challengeId not found"))
      return
    }
    challenges.remove(challengeId)
    onSuccess()
  }
}