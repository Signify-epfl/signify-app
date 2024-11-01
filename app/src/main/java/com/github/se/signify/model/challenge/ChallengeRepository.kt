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

    fun acceptChallenge(
        challengeId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun submitGesture(
        challengeId: String,
        userId: String,
        gesture: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    )

    fun getChallengeUpdates(
        challengeId: String,
        onUpdate: (Challenge) -> Unit,
        onFailure: (Exception) -> Unit
    )
}