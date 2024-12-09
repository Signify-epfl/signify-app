package com.github.se.signify.model.challenge


data class Challenge(
    val challengeId: String = "",
    val player1: String = "",
    val player2: String = "",
    val mode: String = "",
    val status: String = "",
    val round: Int = 1,
    val roundWords: List<String> = emptyList(),
    val player1Times: MutableList<Long> = mutableListOf(),
    val player2Times: MutableList<Long> = mutableListOf(),
    val player1WordsCompleted: MutableList<Int> = mutableListOf(),
    val player2WordsCompleted: MutableList<Int> = mutableListOf(),
    val player1RoundCompleted: MutableList<Boolean> = mutableListOf(),
    val player2RoundCompleted: MutableList<Boolean> = mutableListOf(),
    val gameStatus: String = ""

)



