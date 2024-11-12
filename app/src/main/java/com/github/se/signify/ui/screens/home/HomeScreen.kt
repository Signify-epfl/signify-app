package com.github.se.signify.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.ui.MainScreenScaffold
import com.github.se.signify.ui.StreakCounter
import com.github.se.signify.ui.UtilButton
import com.github.se.signify.ui.UtilTextButton
import com.github.se.signify.ui.getIconResId
import com.github.se.signify.ui.getImageResId
import com.github.se.signify.ui.getLetterIconResId
import com.github.se.signify.ui.getTipResId
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen

data class Exercise(val name: String, val route: String = "UNKNOWN_EXERCISE")

@Composable
fun HomeScreen(navigationActions: NavigationActions) {
  val defaultExercises =
      listOf(
          Exercise("Easy", Screen.EXERCISE_EASY),
          Exercise("Medium", Screen.EXERCISE_HARD),
          Exercise("Hard", Screen.EXERCISE_HARD),
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

    Spacer(modifier = Modifier.height(32.dp))

    CameraFeedbackButton(onClick = { navigationActions.navigateTo(Screen.PRACTICE) })

    Spacer(modifier = Modifier.height(32.dp))

    LetterDictionary()

    Spacer(modifier = Modifier.height(32.dp))

    ExerciseList(defaultExercises, navigationActions)

    Spacer(modifier = Modifier.height(32.dp))
    // New Box with the image, icon, and description
    CreateDictionaryWithImages()

    Spacer(modifier = Modifier.height(32.dp))
  }
}

@Composable
fun CameraFeedbackButton(onClick: () -> Unit = {}) {
  UtilTextButton(
      onClickAction = onClick,
      testTag = "CameraFeedbackButton",
      text = "Try it out",
      backgroundColor = MaterialTheme.colorScheme.primary,
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
                  tint = MaterialTheme.colorScheme.onBackground,
                  contentDescription = "Back")
            }

        // Box to display letter and icon
        val currentLetter = letters[currentLetterIndex]
        Box(
            modifier =
                Modifier.border(
                        2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .testTag("LetterDisplayBox")) {
              Row {
                Text(
                    text = "${currentLetter.uppercaseChar()} =",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 32.dp.value.sp,
                    modifier = Modifier.testTag("LetterText_${currentLetter.uppercaseChar()}"))
                Icon(
                    painter = painterResource(id = getLetterIconResId(currentLetter)),
                    contentDescription = "Letter gesture",
                    tint = MaterialTheme.colorScheme.onSurface,
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
                  tint = MaterialTheme.colorScheme.onBackground,
                  contentDescription = "Forward")
            }
      }
}

@Composable
fun ExerciseList(exercises: List<Exercise>, navigationActions: NavigationActions) {
  LazyHorizontalGrid(
      rows = GridCells.Adaptive(128.dp),
      modifier =
          Modifier.fillMaxWidth()
              .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surface)
              .heightIn(max = 128.dp)
              .padding(8.dp)
              .testTag("ExerciseList"),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(exercises) { exercise -> ExerciseButton(exercise, navigationActions) }
      }
}

@Composable
fun ExerciseButton(exercise: Exercise, navigationActions: NavigationActions) {
  Button(
      onClick = { navigationActions.navigateTo(exercise.route) },
      modifier =
          Modifier.aspectRatio(2f)
              .fillMaxWidth()
              .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
              .testTag("${exercise.name}ExerciseButton"),
      shape = RoundedCornerShape(8.dp),
      colors =
          ButtonDefaults.buttonColors(
              MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)) {
        Text(exercise.name, modifier = Modifier.testTag("${exercise.name}ExerciseButtonText"))
      }
}

@Composable
fun SignTipBox(letter: Char, modifier: Modifier = Modifier) {
  // Dynamically get the drawable resource IDs based on the letter
  val imageResId = getImageResId(letter)
  val iconResId = getIconResId(letter)
  val tipText = stringResource(id = getTipResId(letter))

  Box(
      modifier =
          modifier
              .padding(16.dp)
              .background(colorResource(R.color.white), RoundedCornerShape(8.dp))
              .padding(8.dp)
              .testTag("SignTipBox_$letter")) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
              // Displaying the main image, e.g., `pic_a.jpg`
              Image(
                  painter = painterResource(id = imageResId),
                  contentDescription = "Image for letter $letter",
                  modifier = Modifier.size(200.dp))

              Spacer(modifier = Modifier.height(16.dp))

              // Displaying the description text with the icon, e.g., `letter_a.png`
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Icon for letter $letter",
                    modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tipText,
                    fontSize = 16.sp,
                    color = colorResource(R.color.black),
                    modifier = Modifier.padding(8.dp))
              }
            }
      }
}

@Composable
fun CreateDictionaryWithImages() {
  Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {

    // Loop through each letter from A to Z
    ('A'..'Z').forEach { letter ->
      Text(
          text = "Letter $letter",
          fontSize = 20.sp,
          color = colorResource(R.color.black),
          modifier = Modifier.padding(vertical = 8.dp).testTag("LetterTextDict_$letter"))
      SignTipBox(letter = letter)
      Spacer(modifier = Modifier.height(16.dp)) // Add space between each box
    }
  }
}
