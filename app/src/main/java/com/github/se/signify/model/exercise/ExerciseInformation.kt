package com.github.se.signify.model.exercise

import com.github.se.signify.R

/**
 * Data class that regroup information of the different level of exercise
 *
 * @param exerciseLevel Represent the level of the exercise based of an enum
 *
 * wordsResourceId : Resource ID for the list of words or sentences to be used in the exercise.
 * screenTag : Tag used for identifying the screen in tests. wordFilter : Optional filter function
 * to specify criteria for selecting words from the resource.
 */
data class ExerciseInformation(val exerciseLevel: ExerciseLevel) {
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
