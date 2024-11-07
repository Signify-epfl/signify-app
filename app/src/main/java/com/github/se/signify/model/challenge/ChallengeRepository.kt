package com.github.se.signify.model.challenge

interface ChallengeRepository {
  fun sendChallengeRequest(
      player1Id: String,
      player2Id: String,
      mode: String,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun deleteChallenge(challengeId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
}
