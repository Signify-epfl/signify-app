package com.github.se.signify.model.home.exercise

import android.content.Context
import com.github.se.signify.R
import com.github.se.signify.model.navigation.Screen

/**
 * Enum class that regroup information of the different level of exercise
 *
 * @param id String to use for UI.
 * @param screen Corresponding route of the level.
 * @param wordsResourceId Resource ID for the list of words or sentences to be used in the exercise.
 * @param screenTag Tag used for identifying the screen in tests.
 * @param wordFilter Optional filter function to specify criteria for selecting words from the
 *   resource.
 */
enum class ExerciseLevel(
    val id: String,
    val screen: Screen,
    val wordsResourceId: Int,
    val screenTag: String,
    val wordFilter: ((String) -> Boolean)? = null
) {
  Easy(
      id = ExerciseLevelName.EASY,
      screen = Screen.EXERCISE_EASY,
      wordsResourceId = R.array.real_words,
      screenTag = "ExerciseScreenEasy",
      wordFilter = null),
  Medium(
      id = ExerciseLevelName.MEDIUM,
      screen = Screen.EXERCISE_MEDIUM,
      wordsResourceId = R.array.real_words_hard,
      screenTag = "ExerciseScreenMedium",
      wordFilter = { word: String -> word.length in 5..7 }),
  Hard(
      id = ExerciseLevelName.HARD,
      screen = Screen.EXERCISE_HARD,
      wordsResourceId = R.array.real_sentences,
      screenTag = "ExerciseScreenHard",
      wordFilter = null)
}

object ExerciseLevelName {
  const val EASY = "Easy"
  const val MEDIUM = "Medium"
  const val HARD = "Hard"

  fun getLevelName(context: Context, level: ExerciseLevel): String {
    return when (level) {
      ExerciseLevel.Easy -> context.getString(R.string.easy_exercises_text)
      ExerciseLevel.Medium -> context.getString(R.string.medium_exercises_text)
      ExerciseLevel.Hard -> context.getString(R.string.hard_exercises_text)
    }
  }
}
