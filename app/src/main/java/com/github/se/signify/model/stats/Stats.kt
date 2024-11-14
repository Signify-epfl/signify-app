package com.github.se.signify.model.stats

data class Stats(
    val lettersLearned: List<Char> = emptyList(),
    val easyExercise: Int = 0,
    val mediumExercise: Int = 0,
    val hardExercise: Int = 0,
    val dailyQuest: Int = 0,
    val weeklyQuest: Int = 0,
    val completedChallenge: Int = 0,
    val createdChallenge: Int = 0
)
