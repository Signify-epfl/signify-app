package com.github.se.signify.ui.screens.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.ui.screens.home.HomeScreen

@Composable
fun TutorialScreen(navigationActions: NavigationActions, onFinish: () -> Unit) {
  var step by remember { mutableIntStateOf(0) }

  // Retrieving positions from CompositionLocal
  val elementPositions = LocalElementPositions.current
  val dictionaryPos = elementPositions["LetterDictionary"]
  val cameraPos = elementPositions["CameraFeedbackButton"]
  val exercisesPos = elementPositions["ExerciseList"]
  val questsPos = elementPositions["QuestsButton"]
  val quizPos = elementPositions["QuizButton"]
  val feedbackPos = elementPositions["FeedbackButton"]

  val overlayColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8F)

  // Define the tutorial steps in a list
  val tutorialSteps =
      listOf(
          TutorialStep(
              text = stringResource(R.string.tutorial_welcomeText),
              highlightArea = HighlightedArea.FullScreenOverlayArea(overlayColor = overlayColor),
              textTag = "WelcomeTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_dictionaryText),
              highlightArea =
                  if (dictionaryPos != null) {
                    HighlightedArea.HighlightedBoxArea(
                        elementPosition =
                            ElementPosition(
                                x = dictionaryPos.x,
                                y = dictionaryPos.y,
                                width = dictionaryPos.width,
                                height = dictionaryPos.height),
                        overlayColor = overlayColor)
                  } else {
                    HighlightedArea.FullScreenOverlayArea(overlayColor = overlayColor)
                  },
              textTag = "ExerciseTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_cameraFeedbackText),
              highlightArea =
                  if (cameraPos != null) {
                    HighlightedArea.HighlightedBoxArea(
                        elementPosition =
                            ElementPosition(
                                x = cameraPos.x,
                                y = cameraPos.y,
                                width = cameraPos.width,
                                height = cameraPos.height),
                        overlayColor = overlayColor)
                  } else {
                    HighlightedArea.FullScreenOverlayArea(overlayColor = overlayColor)
                  },
              textTag = "ExerciseTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_exerciseText),
              highlightArea =
                  if (exercisesPos != null) {
                    HighlightedArea.HighlightedBoxArea(
                        elementPosition =
                            ElementPosition(
                                x = exercisesPos.x,
                                y = exercisesPos.y,
                                width = exercisesPos.width,
                                height = exercisesPos.height),
                        overlayColor = overlayColor)
                  } else {
                    HighlightedArea.FullScreenOverlayArea(overlayColor = overlayColor)
                  },
              textTag = "ExerciseTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_questsText),
              highlightArea =
                  if (questsPos != null) {
                    HighlightedArea.HighlightedBoxArea(
                        elementPosition =
                            ElementPosition(
                                x = questsPos.x,
                                y = questsPos.y,
                                width = questsPos.width,
                                height = questsPos.height),
                        overlayColor = overlayColor)
                  } else {
                    HighlightedArea.FullScreenOverlayArea(overlayColor = overlayColor)
                  },
              textTag = "ExerciseTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_quizzText),
              highlightArea =
                  if (quizPos != null) {
                    HighlightedArea.HighlightedBoxArea(
                        elementPosition =
                            ElementPosition(
                                x = quizPos.x,
                                y = quizPos.y,
                                width = quizPos.width,
                                height = quizPos.height),
                        overlayColor = overlayColor)
                  } else {
                    HighlightedArea.FullScreenOverlayArea(overlayColor = overlayColor)
                  },
              textTag = "ExerciseTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_feedbackText),
              highlightArea =
                  if (feedbackPos != null) {
                    HighlightedArea.HighlightedBoxArea(
                        ElementPosition(
                            x = feedbackPos.x,
                            y = feedbackPos.y,
                            width = feedbackPos.width,
                            height = feedbackPos.height),
                        overlayColor)
                  } else {
                    HighlightedArea.FullScreenOverlayArea(overlayColor)
                  },
              textTag = "ExerciseTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_completionText),
              highlightArea = HighlightedArea.FullScreenOverlayArea(overlayColor),
              textTag = "CompletionTextTag"))

  Box(modifier = Modifier.fillMaxSize()) {
    // Display the HomeScreen in the background
    HomeScreen(navigationActions)

    // Add the blocking overlay
    BlockingInteractionsOverlay()

    // Render the current tutorial step
    if (step in tutorialSteps.indices) {
      val isLastStep = step == tutorialSteps.size - 1
      val currentStep = tutorialSteps[step]
      TutorialOverlay(
          text = currentStep.text,
          highlightArea = currentStep.highlightArea,
          onNext = {
            if (isLastStep) {
              navigationActions.navigateTo(Screen.HOME)
              onFinish()
            } else {
              step++
            }
          },
          textTag = currentStep.textTag)
    }

    SkipButton(
        onSkip = {
          navigationActions.navigateTo(Screen.HOME)
          onFinish()
        })
  }
}

@Composable
fun SkipButton(
    onSkip: () -> Unit, // Action to perform when the button is clicked
) {
  Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.TopEnd) {
    TextButton(onClick = onSkip, modifier = Modifier.testTag("SkipTutorialButton")) {
      Text(
          text = stringResource(R.string.skip_tutorial),
          color = MaterialTheme.colorScheme.onPrimary,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          textDecoration = TextDecoration.Underline)
    }
  }
}

@Composable
fun BlockingInteractionsOverlay() {
  Box(
      modifier =
          Modifier.fillMaxSize().background(Color.Transparent).clickable(
              indication = null,
              interactionSource =
                  remember { MutableInteractionSource() }) { /* Block all interactions */})
}
