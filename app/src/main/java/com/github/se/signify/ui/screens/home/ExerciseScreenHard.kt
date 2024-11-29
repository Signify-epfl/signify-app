package com.github.se.signify.ui.screens.home

import androidx.compose.runtime.Composable
import com.github.se.signify.model.exercise.ExerciseInformation
import com.github.se.signify.model.exercise.ExerciseLevel
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.navigation.NavigationActions

/**
 * Composable function for the Hard Level Exercise Screen. This screen allows the user to practice
 * recognizing letters in American Sign Language (ASL) by using the hand landmarks detected by the
 * device's camera.
 *
 * @param navigationActions A collection of navigation actions that can be triggered from this
 *   screen.
 * @param handLandMarkViewModel The ViewModel responsible for managing hand landmark detection.
 */
@Composable
fun ExerciseScreenHard(
    navigationActions: NavigationActions,
    handLandMarkViewModel: HandLandMarkViewModel
) {
  ExerciseScreenCommon(
      navigationActions = navigationActions,
      handLandMarkViewModel = handLandMarkViewModel,
      exerciseInformation = ExerciseInformation(ExerciseLevel.Hard))
}
