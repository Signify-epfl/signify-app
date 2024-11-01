package com.github.se.signify.model.challenge

data class Challenge(
    val challengeId: String = "",
    val player1: String = "",
    val player2: String = "",
    val status: String = "pending",
    val round: Int = 1,
    val player1Score: Int = 0,
    val player2Score: Int = 0,
    val currentGesture: String = "",
    val responses: Map<String, String> = emptyMap()
)