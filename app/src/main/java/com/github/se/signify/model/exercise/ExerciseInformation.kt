package com.github.se.signify.model.exercise

import com.github.se.signify.R
import com.github.se.signify.ui.navigation.Screen

/**
 * Data class that regroup information of the different level of exercise
 *
 * @param exerciseLevel Represent the level of the exercise based of an enum
 *
 * level : String to use for UI. levelRoute : Corresponding route of the level. wordsResourceId :
 * Resource ID for the list of words or sentences to be used in the exercise. screenTag : Tag used
 * for identifying the screen in tests. wordFilter : Optional filter function to specify criteria
 * for selecting words from the resource.
 */
data class ExerciseInformation(val exerciseLevel: ExerciseLevel) {
  val level =
      when (exerciseLevel) {
        ExerciseLevel.Easy -> "Easy"
        ExerciseLevel.Medium -> "Medium"
        ExerciseLevel.Hard -> "Hard"
      }
  val levelRoute =
      when (exerciseLevel) {
        ExerciseLevel.Easy -> Screen.EXERCISE_EASY
        ExerciseLevel.Medium -> Screen.EXERCISE_MEDIUM
        ExerciseLevel.Hard -> Screen.EXERCISE_HARD
      }
  val wordsResourceId =
      when (exerciseLevel) {
        ExerciseLevel.Easy -> R.array.real_words
        ExerciseLevel.Medium -> R.array.real_words_hard
        ExerciseLevel.Hard -> R.array.real_sentences
      }
  val screenTag =
      when (exerciseLevel) {
        ExerciseLevel.Easy -> "ExerciseScreenEasy"
        ExerciseLevel.Medium -> "ExerciseScreenMedium"
        ExerciseLevel.Hard -> "ExerciseScreenHard"
      }
  val wordFilter =
      when (exerciseLevel) {
        ExerciseLevel.Easy -> null
        ExerciseLevel.Medium -> { word: String -> word.length in 5..7 }
        ExerciseLevel.Hard -> null
      }
}

enum class ExerciseLevel {
  Easy,
  Medium,
  Hard
}
