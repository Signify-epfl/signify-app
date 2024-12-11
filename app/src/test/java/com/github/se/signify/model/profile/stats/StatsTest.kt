package com.github.se.signify.model.profile.stats

import org.junit.Assert.assertEquals
import org.junit.Test

class StatsTest {

  @Test
  fun defaultConstructorInitializePropertiesCorrectly() {
    val stats = Stats()

    assertEquals(emptyList<Char>(), stats.lettersLearned)
    assertEquals(0, stats.easyExercise)
    assertEquals(0, stats.mediumExercise)
    assertEquals(0, stats.hardExercise)
    assertEquals(0, stats.dailyQuest)
    assertEquals(0, stats.weeklyQuest)
    assertEquals(0, stats.completedChallenge)
    assertEquals(0, stats.createdChallenge)
    assertEquals(0, stats.wonChallenge)
    assertEquals(emptyList<Long>(), stats.timePerLetter)
  }

  @Test
  fun parameterizedConstructorInitializesPropertiesCorrectly() {
    val stats =
        Stats(
            lettersLearned = listOf('A', 'B', 'C'),
            easyExercise = 5,
            mediumExercise = 3,
            hardExercise = 2,
            dailyQuest = 3,
            weeklyQuest = 0,
            completedChallenge = 1,
            createdChallenge = 1,
            wonChallenge = 1,
            timePerLetter = listOf(1000L, 1024L, 777L))

    assertEquals(listOf('A', 'B', 'C'), stats.lettersLearned)
    assertEquals(5, stats.easyExercise)
    assertEquals(3, stats.mediumExercise)
    assertEquals(2, stats.hardExercise)
    assertEquals(3, stats.dailyQuest)
    assertEquals(0, stats.weeklyQuest)
    assertEquals(1, stats.completedChallenge)
    assertEquals(1, stats.createdChallenge)
    assertEquals(1, stats.wonChallenge)
    assertEquals(listOf(1000L, 1024L, 777L), stats.timePerLetter)
  }
}
