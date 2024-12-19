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
import com.github.se.signify.model.navigation.TopLevelDestinations
import com.github.se.signify.ui.screens.home.HomeScreen

private val overlayColor
  @Composable get() = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8F)

@Composable
fun TutorialScreen(navigationActions: NavigationActions, onFinish: () -> Unit) {
  var step by remember { mutableIntStateOf(0) }

  // Retrieving positions from CompositionLocal
  val elementPositions: Map<String, ElementPosition> = LocalElementPositions.current.toMap()
  val dictionaryPos = elementPositions["LetterDictionary"]
  val cameraPos = elementPositions["CameraFeedbackButton"]
  val exercisesPos = elementPositions["ExerciseList"]
  val questsPos = elementPositions["QuestsButton"]
  val quizPos = elementPositions["QuizButton"]
  val feedbackPos = elementPositions["FeedbackButton"]

  // Define the tutorial steps in a list
  val tutorialSteps =
      listOf(
          TutorialStep(
              text = stringResource(R.string.tutorial_welcomeText),
              highlightArea = HighlightedArea.FullScreenOverlayArea(overlayColor = overlayColor),
              textTag = "WelcomeTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_dictionaryText),
              highlightArea = createHighlightedArea(dictionaryPos),
              textTag = "DictionaryTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_cameraFeedbackText),
              highlightArea = createHighlightedArea(cameraPos),
              textTag = "CameraFeedbackTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_exerciseText),
              highlightArea = createHighlightedArea(exercisesPos),
              textTag = "ExerciseTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_questsText),
              highlightArea = createHighlightedArea(questsPos),
              textTag = "QuestsTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_quizText),
              highlightArea = createHighlightedArea(quizPos),
              textTag = "QuizTextTag"),
          TutorialStep(
              text = stringResource(R.string.tutorial_feedbackText),
              highlightArea = createHighlightedArea(feedbackPos),
              textTag = "FeedbackTextTag"),
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
              navigationActions.navigateTo(TopLevelDestinations.HOME)
              onFinish()
            } else {
              step++
            }
          },
          textTag = currentStep.textTag)
    }

    SkipButton(
        onSkip = {
          navigationActions.navigateTo(TopLevelDestinations.HOME)
          onFinish()
        })
  }
}

@Composable
fun SkipButton(
    onSkip: () -> Unit, // Action to perform when the button is clicked
) {
  Box(modifier = Modifier.fillMaxSize().padding(top = 24.dp), contentAlignment = Alignment.TopEnd) {
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

@Composable
private fun createHighlightedArea(
    position: ElementPosition?,
): HighlightedArea {
  return if (position != null) {
    HighlightedArea.HighlightedBoxArea(
        elementPosition =
            ElementPosition(
                x = position.x, y = position.y, width = position.width, height = position.height),
        overlayColor = overlayColor)
  } else {
    HighlightedArea.FullScreenOverlayArea(overlayColor = overlayColor)
  }
}
