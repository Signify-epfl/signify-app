package com.github.se.signify.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.model.quiz.QuizRepository
import com.github.se.signify.model.quiz.QuizViewModel
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun QuizScreen(navigationActions: NavigationActions, quizRepository: QuizRepository) {
  val quizViewModel: QuizViewModel = viewModel(factory = QuizViewModel.factory(quizRepository))

  // Observe the current quiz from the ViewModel
  val currentQuiz by quizViewModel.currentQuiz.collectAsState()
  var selectedOption by remember { mutableStateOf<String?>(null) }

  val context = LocalContext.current

  Scaffold(
      modifier = Modifier.fillMaxSize(),
  ) { padding ->
    if (currentQuiz != null) {
      val shuffledOptions =
          remember(currentQuiz) {
            currentQuiz!!.confusers.plus(currentQuiz!!.correctWord).shuffled()
          }

      Column(
          modifier = Modifier.fillMaxSize().padding(16.dp),
      ) {
        // Header with title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
              IconButton(onClick = { navigationActions.goBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary)
              }
              Spacer(modifier = Modifier.width(40.dp))

              Text(
                  text = "Quiz Time !",
                  fontWeight = FontWeight.Bold,
                  fontSize = 30.sp,
                  color = MaterialTheme.colorScheme.primary)
            }

        Spacer(modifier = Modifier.height(24.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(items = currentQuiz!!.signs) { signResId ->
                Image(
                    painter = painterResource(id = signResId),
                    contentDescription = "Sign for Letter",
                    modifier =
                        Modifier.size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)))
              }
            }

        // Display Quiz Options
        Text(
            text = "Choose the correct answer:",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp))
        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
              shuffledOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier.fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                            .padding(12.dp)) {
                      RadioButton(
                          selected = selectedOption == option,
                          onClick = { selectedOption = option },
                          colors =
                              RadioButtonDefaults.colors(
                                  selectedColor = MaterialTheme.colorScheme.primary))
                      Text(
                          text = option,
                          color = MaterialTheme.colorScheme.primary,
                          fontWeight = FontWeight.Bold,
                          fontSize = 20.sp,
                          modifier = Modifier.padding(start = 8.dp))
                    }
              }
            }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
              // Inside your Button's onClick
              if (selectedOption != null) {
                quizViewModel.submitAnswer(
                    selectedOption = selectedOption!!,
                    onCorrect = {
                      // Handle correct answer feedback (e.g., show a toast)
                      Toast.makeText(context, "Correct answer!", Toast.LENGTH_SHORT).show()
                    },
                    onIncorrect = {
                      // Handle incorrect answer feedback (e.g., show a toast)
                      Toast.makeText(context, "Incorrect answer, try again.", Toast.LENGTH_SHORT)
                          .show()
                    })
                selectedOption = null // Reset the selection
              }
            },
            enabled = selectedOption != null,
            colors =
                ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(50),
        ) {
          Text(text = "Submit")
        }
      }
    } else {
      // Show a message when there are no quizzes
      Column(
          modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No quizzes available.",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp))
          }
    }
  }
}
