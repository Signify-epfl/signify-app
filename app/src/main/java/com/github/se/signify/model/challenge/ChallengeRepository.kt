package com.github.se.signify.model.challenge

import androidx.compose.runtime.Composable

interface ChallengeRepository {
  fun sendChallengeRequest(
      player1Id: String,
      player2Id: String,
      mode: ChallengeMode,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun deleteChallenge(challengeId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)

  fun getChallengeById(
      challengeId: String,
      onSuccess: (Challenge) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun updateChallenge(
      updatedChallenge: Challenge,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  // New function to record player time when a challenge round is completed
  fun recordPlayerTime(
      challengeId: String,
      playerId: String,
      timeTaken: Long,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )
}
