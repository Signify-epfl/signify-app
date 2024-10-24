package com.github.se.signify.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.ui.ReusableTextButton
import com.github.se.signify.ui.UtilButton
import com.github.se.signify.ui.getLetterIconResId
import com.github.se.signify.ui.navigation.BottomNavigationMenu
import com.github.se.signify.ui.navigation.LIST_TOP_LEVEL_DESTINATION
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

  Scaffold(
      bottomBar = {
        BottomNavigationMenu(
            onTabSelect = { route -> navigationActions.navigateTo(route) },
            tabList = LIST_TOP_LEVEL_DESTINATION,
            selectedItem = navigationActions.currentRoute())
      },
      content = { padding ->
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .testTag("HomeScreen"),
            horizontalAlignment = Alignment.CenterHorizontally) {
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
              ) {
                StreakCounter()
                UtilButton({}, "HelpButton", "HelpIcon", Icons.Outlined.Info, "Help")

                UtilButton(
                    onClick = { navigationActions.navigateTo("Quest") },
                    "QuestsButton",
                    "QuestIcon",
                    Icons.Outlined.DateRange,
                    "Quests")
              }

              Spacer(modifier = Modifier.height(16.dp))

              ReusableTextButton(
                  onClickAction = { /* Do nothing for now */},
                  textTag = "CameraFeedbackToggle",
                  text = "Toggle Camera",
                  height = 30.dp,
                  borderColor = colorResource(R.color.black),
                  backgroundColor = colorResource(R.color.blue),
                  textSize = 12.sp,
                  textColor = colorResource(R.color.white))

              Spacer(modifier = Modifier.height(16.dp))

              CameraFeedback(onClick = { navigationActions.navigateTo(Screen.PRACTICE) })

              Spacer(modifier = Modifier.height(16.dp))

              LetterDictionary()

              Spacer(modifier = Modifier.height(16.dp))

              ExerciseList(defaultExercises, exerciseOnClick)
            }
      })
}

// This should be hooked to the camera feedback screen later on.
@Composable
fun CameraFeedback(onClick: () -> Unit = {}) {
  Box(
      modifier =
          Modifier.aspectRatio(4f / 3f)
              .border(2.dp, colorResource(R.color.blue), RoundedCornerShape(8.dp))
              .background(colorResource(R.color.black), RoundedCornerShape(8.dp))
              .clickable { onClick() }
              .testTag("CameraFeedback")) {
        Text(
            text = "Camera\nFeedback",
            modifier = Modifier.align(Alignment.Center),
            color = colorResource(R.color.white),
            fontSize = 32.dp.value.sp)
      }
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
  LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      modifier =
          Modifier.fillMaxSize()
              .wrapContentHeight()
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

// This button should be replaced by a shared Composable later on.

// This counter should be replaced by a shared Composable later on.
@Composable
fun StreakCounter() {
  Row(modifier = Modifier.testTag("StreakCounter")) {
    Icon(
        painter = painterResource(id = R.drawable.flame),
        contentDescription = "Streak Icon",
        tint = colorResource(R.color.red),
        modifier = Modifier.size(20.dp).testTag("FlameIcon"))
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = "4",
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.red),
        fontSize = 20.dp.value.sp)
  }
}
