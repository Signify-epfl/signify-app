package com.github.se.signify.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.feedback.FeedbackOption
import com.github.se.signify.model.feedback.FeedbackRepository
import com.github.se.signify.model.feedback.FeedbackViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.UtilTextButton

@Composable
fun FeedbackScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    feedbackRepository: FeedbackRepository
) {
  val feedbackViewModel: FeedbackViewModel =
      viewModel(factory = FeedbackViewModel.factory(userSession, feedbackRepository))
  val context = LocalContext.current

  var selectedFeedbackType by remember { mutableStateOf(FeedbackOption.BUG_REPORT.category) }
  var feedbackTitle by remember { mutableStateOf(TextFieldValue("")) }
  var feedbackDescription by remember { mutableStateOf(TextFieldValue("")) }
  var selectedRating by remember { mutableIntStateOf(0) }
  var isLoading by remember { mutableStateOf(false) }

  AnnexScreenScaffold(navigationActions = navigationActions, testTagColumn = "FeedbackScreen") {
    FeedbackDropdown(
        selectedFeedbackType = selectedFeedbackType,
        onFeedbackTypeSelected = { selectedFeedbackType = it })

    FeedbackInputField(
        value = feedbackTitle,
        onValueChange = { feedbackTitle = it },
        label = "Feedback Title",
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).testTag("FeedbackTitleInput"))

    FeedbackInputField(
        value = feedbackDescription,
        onValueChange = { feedbackDescription = it },
        label = "Feedback Description",
        modifier =
            Modifier.fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 16.dp)
                .testTag("FeedbackDescriptionInput"))

    RatingSection(selectedRating = selectedRating, onRatingSelected = { selectedRating = it })

    UtilTextButton(
        onClickAction = {
          if (feedbackTitle.text.isNotEmpty() && feedbackDescription.text.isNotEmpty()) {
            isLoading = true
            feedbackViewModel.saveFeedback(
                type = selectedFeedbackType,
                title = feedbackTitle.text,
                description = feedbackDescription.text,
                rating = selectedRating)
            isLoading = false
            Toast.makeText(context, "Review sent", Toast.LENGTH_LONG).show()
            navigationActions.navigateTo(Screen.HOME)
          } else {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
          }
        },
        testTag = "SendFeedbackButton",
        text = "Send Feedback",
        backgroundColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onPrimary)

    LoadingIndicator(isLoading = isLoading)
  }
}

@Composable
private fun FeedbackDropdown(
    selectedFeedbackType: String,
    onFeedbackTypeSelected: (String) -> Unit
) {
  var expanded by remember { mutableStateOf(false) }
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .padding(bottom = 16.dp)
              .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
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
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .testTag("DropdownMenu")) {
              FeedbackOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = {
                      Text(text = option.category, color = MaterialTheme.colorScheme.onPrimary)
                    },
                    onClick = {
                      onFeedbackTypeSelected(option.category)
                      expanded = false
                    },
                    modifier = Modifier.testTag("DropdownMenuItem_${option.category}"))
              }
            }
      }
}

@Composable
private fun FeedbackInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
  OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      label = { Text(label) },
      textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onBackground),
      modifier = modifier)
}

@Composable
private fun RatingSection(selectedRating: Int, onRatingSelected: (Int) -> Unit) {
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
                  if (i <= selectedRating) MaterialTheme.colorScheme.primary
                  else MaterialTheme.colorScheme.onBackground,
              modifier =
                  Modifier.size(32.dp)
                      .clickable { onRatingSelected(i) }
                      .padding(horizontal = 4.dp)
                      .testTag("Star_$i"))
        }
      }
}

@Composable
private fun LoadingIndicator(isLoading: Boolean) {
  if (isLoading) {
    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp).testTag("LoadingIndicator"))
  }
}
