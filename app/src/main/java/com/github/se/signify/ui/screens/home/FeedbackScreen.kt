package com.github.se.signify.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.model.feedback.FeedbackViewModel
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.UtilTextButton
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import com.github.se.signify.ui.screens.profile.currentUserId

@Composable
fun FeedbackScreen(
    navigationActions: NavigationActions,
    feedbackViewModel: FeedbackViewModel = viewModel(factory = FeedbackViewModel.Factory)
) {
  val context = LocalContext.current

  // Feedback Type Options
  val feedbackOptions = listOf("Bug Report", "Feature Suggestion", "Question", "Other")
  var selectedFeedbackType by remember { mutableStateOf(feedbackOptions[0]) }

  // Feedback Title and Description State
  var feedbackTitle by remember { mutableStateOf(TextFieldValue("")) }
  var feedbackDescription by remember { mutableStateOf(TextFieldValue("")) }

  // Rating State
  var selectedRating by remember { mutableIntStateOf(0) }

  // Loading state to show loading indicator when saving feedback
  var isLoading by remember { mutableStateOf(false) }

  Scaffold(
      topBar = {
        Column {
          // Top blue bar
          Box(
              modifier =
                  Modifier.fillMaxWidth()
                      .height(4.dp)
                      .background(MaterialTheme.colorScheme.primary)
                      .testTag("TopBlueBar"))

          // Back Button
          BackButton { navigationActions.goBack() }
        }
      },
      content = { padding ->
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .testTag("FeedbackScreenContent"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
              // Feedback Type Dropdown
              var expanded by remember { mutableStateOf(false) }

              Box(
                  modifier =
                      Modifier.fillMaxWidth()
                          .padding(bottom = 16.dp)
                          .border(
                              1.dp,
                              Color.Gray,
                              RoundedCornerShape(8.dp)) // Added border to dropdown button
                          .background(MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(8.dp))
                          .clickable { expanded = !expanded }
                          .padding(16.dp)
                          .testTag("FeedbackTypeDropdown")) {
                    Text(text = selectedFeedbackType)
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier =
                            Modifier.fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.onBackground,
                                    RoundedCornerShape(
                                        8.dp)) // White background for the dropdown menu
                                .testTag("DropdownMenu")) {
                          feedbackOptions.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                  Text(
                                      text = option,
                                      color =
                                          MaterialTheme.colorScheme
                                              .onPrimary // Black text for readability
                                      )
                                },
                                onClick = {
                                  selectedFeedbackType = option
                                  expanded = false
                                },
                                modifier = Modifier.testTag("DropdownMenuItem_$option"))
                          }
                        }
                  }

              // Feedback Title Input
              OutlinedTextField(
                  value = feedbackTitle,
                  onValueChange = { feedbackTitle = it },
                  label = { Text("Feedback Title") },
                  textStyle =
                      LocalTextStyle.current.copy(
                          color =
                              MaterialTheme.colorScheme
                                  .onBackground), // Set text color to black or onBackground
                  modifier =
                      Modifier.fillMaxWidth().padding(bottom = 16.dp).testTag("FeedbackTitleInput"))

              // Feedback Description Input
              OutlinedTextField(
                  value = feedbackDescription,
                  onValueChange = { feedbackDescription = it },
                  label = { Text("Feedback Description") },
                  textStyle =
                      LocalTextStyle.current.copy(
                          color =
                              MaterialTheme.colorScheme
                                  .onBackground), // Set text color to black or onBackground
                  modifier =
                      Modifier.fillMaxWidth()
                          .height(150.dp)
                          .padding(bottom = 16.dp)
                          .testTag("FeedbackDescriptionInput"))

              // Rating Section
              Text(
                  text = "Give us your valuable rating!",
                  fontSize = 18.sp,
                  color = MaterialTheme.colorScheme.onBackground,
                  modifier = Modifier.padding(bottom = 8.dp).testTag("RatingTitle"))

              Row(
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(bottom = 24.dp).testTag("RatingStars")) {
                    for (i in 1..5) {
                      Icon(
                          imageVector = Icons.Outlined.Star,
                          contentDescription = "Star $i",
                          tint =
                              if (i <= selectedRating) Color.Yellow
                              else MaterialTheme.colorScheme.onBackground,
                          modifier =
                              Modifier.size(32.dp)
                                  .clickable { selectedRating = i }
                                  .padding(horizontal = 4.dp)
                                  .testTag("Star_$i"))
                    }
                  }

              // Send Feedback Button using UtilTextButton
              UtilTextButton(
                  onClickAction = {
                    if (feedbackTitle.text.isNotEmpty() && feedbackDescription.text.isNotEmpty()) {
                      val userId = currentUserId

                      // Set isLoading to true to show the loading indicator
                      isLoading = true

                      // Save Feedback to Firestore
                      feedbackViewModel.saveFeedback(
                          uid = userId,
                          type = selectedFeedbackType,
                          title = feedbackTitle.text,
                          description = feedbackDescription.text,
                          rating = selectedRating)

                      // Handle success (UI update after calling saveFeedback)
                      isLoading = false
                      Toast.makeText(context, "Review sent", Toast.LENGTH_LONG).show()
                      navigationActions.navigateTo(Screen.HOME) // Navigate back to Home
                    } else {
                      Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT)
                          .show()
                    }
                  },
                  testTag = "SendFeedbackButton",
                  text = "Send Feedback",
                  backgroundColor = MaterialTheme.colorScheme.primary)

              // Loading Indicator
              if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 16.dp).testTag("LoadingIndicator"))
              }
            }
      })
}
