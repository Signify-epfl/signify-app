package com.github.se.signify.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.CameraPlaceholder
import com.github.se.signify.ui.MainScreenScaffold
import com.github.se.signify.ui.StreakCounter
import com.github.se.signify.ui.UtilButton
import com.github.se.signify.ui.UtilTextButton
import com.github.se.signify.ui.getLetterIconResId
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen

data class Exercise(val name: String)

@Composable
fun HomeScreen(navigationActions: NavigationActions) {
  // Define navigation based on exercise name
  val exerciseOnClick: (Exercise) -> Unit = { exercise ->
    when (exercise.name) {
      "Easy" -> navigationActions.navigateTo(Screen.EXERCISE_EASY)
      "Medium" -> navigationActions.navigateTo(Screen.EXERCISE_HARD)
      "Hard" -> navigationActions.navigateTo(Screen.EXERCISE_HARD)
      else -> navigationActions.navigateTo("EXERCISE_UNKNOWN")
    }
  }

  val defaultExercises =
      listOf(
          Exercise("Easy"),
          Exercise("Medium"),
          Exercise("Hard"),
      )

  MainScreenScaffold(
      navigationActions = navigationActions,
      testTagColumn = "HomeScreen",
      helpTitle = "Home",
      helpText = stringResource(R.string.help_home_screen),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      // This will be updated to show the streak count
      StreakCounter(0, false)

      UtilButton(
          onClick = { navigationActions.navigateTo("Quest") },
          "QuestsButton",
          "QuestIcon",
          Icons.Outlined.DateRange,
          "Quests")
    }

    Spacer(modifier = Modifier.height(16.dp))

    CameraFeedbackButton(onClick = { navigationActions.navigateTo(Screen.PRACTICE) })

    Spacer(modifier = Modifier.height(32.dp))

    LetterDictionary()

    Spacer(modifier = Modifier.height(32.dp))

    ExerciseList(defaultExercises, exerciseOnClick)
  }
}

@Composable
fun CameraFeedbackButton(onClick: () -> Unit = {}) {
  UtilTextButton(
      onClickAction = onClick,
      testTag = "CameraFeedbackButton",
      text = "Try it out",
      backgroundColor = colorResource(R.color.blue),
  )
}

@Composable
fun LetterDictionary() {
  // State to keep track of the current letter index
  var currentLetterIndex by remember { mutableIntStateOf(0) }
  val letters = ('a'..'z').toList() // List of letters from 'a' to 'z'

  Row(
      modifier = Modifier.fillMaxWidth().wrapContentHeight().testTag("LetterDictionary"),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically) {
        // Back Arrow Button
        IconButton(
            onClick = {
              // Update letter index when back arrow is clicked
              currentLetterIndex = (currentLetterIndex - 1 + letters.size) % letters.size
            },
            modifier = Modifier.testTag("LetterDictionaryBack")) {
              Icon(
                  Icons.AutoMirrored.Outlined.ArrowBack,
                  tint = colorResource(R.color.black),
                  contentDescription = "Back")
            }

        // Box to display letter and icon
        val currentLetter = letters[currentLetterIndex]
        Box(
            modifier =
                Modifier.border(2.dp, colorResource(R.color.blue), RoundedCornerShape(8.dp))
                    .background(colorResource(R.color.dark_gray), RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .testTag("LetterDisplayBox")) {
              Row {
                Text(
                    text = "${currentLetter.uppercaseChar()} =",
                    color = colorResource(R.color.blue),
                    fontSize = 32.dp.value.sp,
                    modifier = Modifier.testTag("LetterText_${currentLetter.uppercaseChar()}"))
                Icon(
                    painter = painterResource(id = getLetterIconResId(currentLetter)),
                    contentDescription = "Letter gesture",
                    tint = colorResource(R.color.blue),
                    modifier =
                        Modifier.size(32.dp).testTag("LetterIcon_${currentLetter.uppercaseChar()}"))
              }
            }

        // Forward Arrow Button
        IconButton(
            onClick = {
              // Update letter index when forward arrow is clicked
              currentLetterIndex = (currentLetterIndex + 1) % letters.size
            },
            modifier = Modifier.testTag("LetterDictionaryForward")) {
              Icon(
                  Icons.AutoMirrored.Outlined.ArrowForward,
                  tint = colorResource(R.color.black),
                  contentDescription = "Forward")
            }
      }
}

@Composable
fun ExerciseList(exercises: List<Exercise>, onExerciseClick: (exercise: Exercise) -> Unit) {
  LazyHorizontalGrid(
      rows = GridCells.Adaptive(128.dp),
      modifier =
          Modifier.fillMaxWidth()
              .border(2.dp, colorResource(R.color.black), RoundedCornerShape(8.dp))
              .clip(RoundedCornerShape(8.dp))
              .background(colorResource(R.color.dark_gray))
              .heightIn(max = 128.dp)
              .padding(8.dp)
              .testTag("ExerciseList"),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(exercises) { exercise -> ExerciseButton(exercise, onExerciseClick) }
      }
}

@Composable
fun ExerciseButton(exercise: Exercise, onClick: (exercise: Exercise) -> Unit) {
  Button(
      onClick = { onClick(exercise) },
      modifier =
          Modifier.aspectRatio(2f)
              .fillMaxWidth()
              .border(2.dp, colorResource(R.color.black), RoundedCornerShape(8.dp))
              .testTag("${exercise.name}ExerciseButton"),
      shape = RoundedCornerShape(8.dp),
      colors =
          ButtonDefaults.buttonColors(colorResource(R.color.blue), colorResource(R.color.black))) {
        Text(exercise.name)
      }
}
