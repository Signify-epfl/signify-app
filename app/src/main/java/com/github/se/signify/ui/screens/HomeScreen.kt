package com.github.se.signify.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
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
import com.github.se.signify.ui.navigation.BottomNavigationMenu
import com.github.se.signify.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.ui.navigation.NavigationActions

data class Exercise(val name: String)

@Composable
fun HomeScreen(navigationActions: NavigationActions) {
  // Placeholder exercises
  val exerciseOnClick: Exercise.() -> Unit = { navigationActions.navigateTo("Practice") }
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
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
              ) {
                StreakCounter()
                QuestsButton(onClick = { navigationActions.navigateTo("Quest") })
              }

              Spacer(modifier = Modifier.height(16.dp))

              CameraFeedbackToggle()

              Spacer(modifier = Modifier.height(16.dp))

              CameraFeedback(onClick = { navigationActions.navigateTo("MainAim") })

              Spacer(modifier = Modifier.height(16.dp))

              LetterDictionary()

              Spacer(modifier = Modifier.height(16.dp))

              ExerciseList(defaultExercises, exerciseOnClick)
            }

        HelpButton()
      })
}

@Composable
fun QuestsButton(onClick: () -> Unit = {}) {
  IconButton(
      onClick = { onClick() },
      modifier =
          Modifier.clip(CircleShape)
              .border(2.dp, colorResource(R.color.black), CircleShape)
              .background(colorResource(R.color.blue))
              .testTag("QuestsButton")) {
        Icon(
            Icons.Outlined.DateRange,
            tint = colorResource(R.color.black),
            contentDescription = "Quests")
      }
}

@Composable
fun CameraFeedbackToggle() {
  Button(
      onClick = {},
      colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.blue)),
      border = BorderStroke(2.dp, colorResource(R.color.black)),
      modifier = Modifier.testTag("CameraFeedbackToggle"),
  ) {
    Text("Toggle Camera")
  }
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
  Row(
      modifier = Modifier.fillMaxWidth().wrapContentHeight().testTag("LetterDictionary"),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {}, modifier = Modifier.testTag("LetterDictionaryBack")) {
          Icon(
              Icons.AutoMirrored.Outlined.ArrowBack,
              tint = colorResource(R.color.black),
              contentDescription = "Back")
        }
        Box(
            modifier =
                Modifier.border(2.dp, colorResource(R.color.blue), RoundedCornerShape(8.dp))
                    .background(colorResource(R.color.dark_gray), RoundedCornerShape(8.dp))
                    .padding(8.dp)) {
              Row {
                Text(text = "S =", color = colorResource(R.color.blue), fontSize = 32.dp.value.sp)
                Icon(
                    painter = painterResource(id = R.drawable.letter_s),
                    contentDescription = "Letter gesture",
                    tint = colorResource(R.color.blue),
                    modifier = Modifier.size(32.dp))
              }
            }
        IconButton(onClick = {}, modifier = Modifier.testTag("LetterDictionaryForward")) {
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
@Composable
fun HelpButton() {
  Box(modifier = Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.TopCenter) {
    IconButton(
        onClick = {},
        modifier =
            Modifier.clip(CircleShape)
                .background(colorResource(R.color.blue))
                .testTag("HelpButton"),
    ) {
      Icon(
          Icons.Outlined.Info,
          tint = colorResource(R.color.white),
          contentDescription = "Help",
          modifier = Modifier.testTag("HelpIcon"))
    }
  }
}

// This counter should be replaced by a shared Composable later on.
@Composable
fun StreakCounter() {
  Row(modifier = Modifier.testTag("StreakCounter")) {
    Icon(
        painter = painterResource(id = R.drawable.flame),
        contentDescription = "Streak Icon",
        tint = colorResource(R.color.red),
        modifier = Modifier.size(32.dp).testTag("FlameIcon"))
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = "4",
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.red),
        fontSize = 32.dp.value.sp)
  }
}
