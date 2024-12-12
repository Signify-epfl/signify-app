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
      roundWords: List<String>,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {

    val newChallenge =
        Challenge(
            challengeId = challengeId,
            player1 = player1Id,
            player2 = player2Id,
            mode = mode.name,
            status = "pending",
            round = 1,
            roundWords = roundWords,
            player1Times = mutableListOf(),
            player2Times = mutableListOf(),
            player1RoundCompleted = MutableList(3) { false }, // 3 rounds, all uncompleted initially
            player2RoundCompleted = MutableList(3) { false },
            gameStatus = "not_started")

    // Track all calls, even unsuccessful ones
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

  override fun getChallengeById(
      challengeId: String,
      onSuccess: (Challenge) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!shouldSucceed) {
      onFailure(exceptionToThrow)
      return
    }

    val challenge = challenges[challengeId]
    if (challenge != null) {
      onSuccess(challenge)
    } else {
      onFailure(Exception("Challenge with ID $challengeId not found"))
    }
  }

  override fun getChallenges(
      challengeIds: List<ChallengeId>,
      onSuccess: (List<Challenge>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!shouldSucceed) {
      onFailure(exceptionToThrow)
      return
    }

    val challenges = challengeIds.map { challenges[it] }
    onSuccess(challenges.filterNotNull())
  }

  override fun updateChallenge(
      updatedChallenge: Challenge,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!shouldSucceed) {
      onFailure(exceptionToThrow)
      return
    }

    val existingChallenge = challenges[updatedChallenge.challengeId]
    if (existingChallenge != null) {
      challenges[updatedChallenge.challengeId] = updatedChallenge
      onSuccess()
    } else {
      onFailure(Exception("Challenge with ID ${updatedChallenge.challengeId} not found"))
    }
  }

  override fun recordPlayerTime(
      challengeId: String,
      playerId: String,
      timeTaken: Long,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!shouldSucceed) {
      onFailure(exceptionToThrow)
      return
    }

    val challenge = challenges[challengeId]
    if (challenge != null) {
      // Update the player's time accordingly (assuming player1Times or player2Times)
      when (playerId) {
        challenge.player1 -> {
          challenge.player1Times.add(timeTaken)
        }
        challenge.player2 -> {
          challenge.player2Times.add(timeTaken)
        }
        else -> {
          onFailure(Exception("Invalid player ID"))
          return
        }
      }
      onSuccess()
    } else {
      onFailure(Exception("Challenge with ID $challengeId not found"))
    }
  }
}
