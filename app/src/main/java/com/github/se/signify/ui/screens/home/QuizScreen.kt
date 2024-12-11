package com.github.se.signify.ui.screens.home

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.home.quiz.QuizQuestion
import com.github.se.signify.model.home.quiz.QuizRepository
import com.github.se.signify.model.home.quiz.QuizViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.ui.common.AnnexScreenScaffold

@Composable
fun QuizScreen(navigationActions: NavigationActions, quizRepository: QuizRepository) {
  val quizViewModel: QuizViewModel = viewModel(factory = QuizViewModel.factory(quizRepository))

  val currentQuiz by quizViewModel.currentQuiz.collectAsState()
  var selectedOption by remember { mutableStateOf<String?>(null) }

  val context = LocalContext.current

  AnnexScreenScaffold(
      navigationActions = navigationActions,
      testTag = "QuizScreen",
  ) {
    Spacer(modifier = Modifier.height(32.dp))

    val quizTimeText = stringResource(R.string.quiz_time_text)
    Text(
        text = quizTimeText,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().testTag("QuizTitle"))

    Spacer(modifier = Modifier.height(24.dp))

    if (currentQuiz != null) {
      val shuffledOptions =
          remember(currentQuiz) {
            currentQuiz!!.confusers.plus(currentQuiz!!.correctWord).shuffled()
          }
      val correctAnswerText = stringResource(R.string.correct_answer_text)
      val incorrectAnswerText = stringResource(R.string.incorrect_answer_text)
      QuizContent(
          currentQuiz = currentQuiz!!,
          shuffledOptions = shuffledOptions,
          selectedOption = selectedOption,
          onOptionSelected = { selectedOption = it },
          onSubmit = {
            quizViewModel.submitAnswer(
                selectedOption = it,
                onCorrect = {
                  Toast.makeText(context, correctAnswerText, Toast.LENGTH_SHORT).show()
                },
                onIncorrect = {
                  Toast.makeText(context, incorrectAnswerText, Toast.LENGTH_SHORT).show()
                })
            selectedOption = null
          })
    } else {
      NoQuizAvailable()
    }
  }
}

@Composable
fun QuizContent(
    currentQuiz: QuizQuestion,
    shuffledOptions: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    onSubmit: (String) -> Unit
) {
  LazyRow(
      modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).testTag("SignsRow"),
      horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = currentQuiz.signs) { signResId ->
          Icon(
              painter = painterResource(id = signResId),
              tint = MaterialTheme.colorScheme.primary,
              contentDescription = "Sign for Letter",
              modifier =
                  Modifier.size(80.dp)
                      .clip(RoundedCornerShape(12.dp))
                      .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                      .testTag("SignImage"))
        }
      }
  val chooseCorrectAnswer = stringResource(R.string.choose_correct_answer_text)
  Text(
      text = chooseCorrectAnswer,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onBackground,
      modifier = Modifier.padding(bottom = 12.dp).testTag("QuizPrompt"))

  Column(
      modifier = Modifier.fillMaxWidth().testTag("OptionsColumn"),
      verticalArrangement = Arrangement.spacedBy(8.dp)) {
        shuffledOptions.forEach { option ->
          Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier =
                  Modifier.fillMaxWidth()
                      .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                      .padding(12.dp)
                      .testTag("OptionRow")) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) },
                    colors =
                        RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor =
                                MaterialTheme.colorScheme.onSurfaceVariant // Added unselected color
                            ),
                    modifier = Modifier.testTag("OptionRadioButton"))
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

  Button(
      onClick = { if (selectedOption != null) onSubmit(selectedOption) },
      enabled = selectedOption != null,
      colors =
          ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary // Updated from background
              ),
      modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("SubmitButton"),
      shape = RoundedCornerShape(50),
  ) {
    val submitText = stringResource(R.string.submit_text)
    Text(text = submitText)
  }
}

@Composable
fun NoQuizAvailable() {
  val noQuizAvailableText = stringResource(R.string.no_quiz_available)
  Column(
      modifier = Modifier.fillMaxSize().testTag("NoQuizContainer"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = noQuizAvailableText,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground, // Updated from onSurface
            modifier = Modifier.padding(bottom = 16.dp).testTag("NoQuizzesText"))
      }
}
