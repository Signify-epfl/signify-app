package com.github.se.signify.model.challenge

data class Challenge(
    val challengeId: String = "",
    val player1: String = "",
    val player2: String = "",
    val mode: String = "",
    var round: Int = 1,
    val roundWords: List<String> = listOf(),
    val player1Times: MutableList<Long> = mutableListOf(),
    val player2Times: MutableList<Long> = mutableListOf(),
    var player1RoundCompleted: List<Boolean> = mutableListOf(false, false, false),
    var player2RoundCompleted: List<Boolean> = mutableListOf(false, false, false),
    var gameStatus: String = "not_started",
    var winner: String? = null // New field for winner
)
