package com.github.se.signify.model.stats

data class Stats(
    val days: Int = 0,
    val lettersLearned: List<Char> = emptyList(),
    val exerciseStats: ExerciseStats = ExerciseStats(),
    val questStats: QuestStats = QuestStats(),
    val challengeStats: ChallengeStats = ChallengeStats()
)
