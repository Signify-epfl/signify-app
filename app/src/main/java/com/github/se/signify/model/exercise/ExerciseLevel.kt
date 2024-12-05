package com.github.se.signify.model.exercise

import com.github.se.signify.R
import com.github.se.signify.model.navigation.Screen

/**
 * Enum class that regroup information of the different level of exercise
 *
 * @param levelName String to use for UI.
 * @param levelScreen Corresponding route of the level.
 * @param wordsResourceId Resource ID for the list of words or sentences to be used in the exercise.
 * @param screenTag Tag used for identifying the screen in tests.
 * @param wordFilter Optional filter function to specify criteria for selecting words from the
 *   resource.
 */
enum class ExerciseLevel(
    val levelName: String,
    val levelScreen: Screen,
    val wordsResourceId: Int,
    val screenTag: String,
    val wordFilter: ((String) -> Boolean)? = null
) {
  Easy(
      levelName = "Easy",
      levelScreen = Screen.EXERCISE_EASY,
      wordsResourceId = R.array.real_words,
      screenTag = "ExerciseScreenEasy",
      wordFilter = null),
  Medium(
      levelName = "Medium",
      levelScreen = Screen.EXERCISE_MEDIUM,
      wordsResourceId = R.array.real_words_hard,
      screenTag = "ExerciseScreenMedium",
      wordFilter = { word: String -> word.length in 5..7 }),
  Hard(
      levelName = "Hard",
      levelScreen = Screen.EXERCISE_HARD,
      wordsResourceId = R.array.real_sentences,
      screenTag = "ExerciseScreenHard",
      wordFilter = null)
}
