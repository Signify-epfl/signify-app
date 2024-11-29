package com.github.se.signify.ui.screens.home

import androidx.compose.runtime.Composable
import com.github.se.signify.model.exercise.ExerciseInformation
import com.github.se.signify.model.exercise.ExerciseLevel
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
      exerciseInformation = ExerciseInformation(ExerciseLevel.Medium))
}
