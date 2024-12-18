package com.github.se.signify.model.challenge

data class Challenge(
    val challengeId: String = "",
    val player1: String = "",
    val player2: String = "",
    val mode: String = "", // "chrono" or other types
    var round: Int = 1, // Current round (1, 2, or 3)
    val roundWords: List<String> = listOf(), // Words for each round
    var player1Times: MutableList<Long> = mutableListOf(), // Player 1's times for each round
    var player2Times: MutableList<Long> = mutableListOf(), // Player 2's times for each round
    var player1RoundCompleted: List<Boolean> =
        mutableListOf(false, false, false), // Track if player 1 completed each round
    var player2RoundCompleted: List<Boolean> =
        mutableListOf(false, false, false), // Track if player 2 completed each round
    var player1WordsCompleted: MutableList<Int> = mutableListOf(),
    var player2WordsCompleted: MutableList<Int> = mutableListOf(),
    var gameStatus: String =
        "not_started", // Possible values: "not_started", "in_progress", "completed"
    var winner: String? = null
)
