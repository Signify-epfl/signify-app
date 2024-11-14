package com.github.se.signify.ui.screens.home

import androidx.compose.runtime.Composable
import com.github.se.signify.R
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun ExerciseScreenMedium(
    navigationActions: NavigationActions,
    handLandMarkViewModel: HandLandMarkViewModel
) {
  ExerciseScreenCommon(
      navigationActions = navigationActions,
      handLandMarkViewModel = handLandMarkViewModel,
      wordsResourceId = R.array.real_words_hard,
      screenTag = "ExerciseScreenMedium",
      wordFilter = { it.length in 5..7 })
}
