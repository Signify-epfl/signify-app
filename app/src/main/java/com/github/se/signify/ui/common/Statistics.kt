package com.github.se.signify.ui.common

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R

/**
 * A scrollable list of letters. The letters learned by a user are highlighted.
 *
 * @param lettersLearned The list of character already learned.
 */
@Composable
fun LearnedLetterList(lettersLearned: List<Char>) {
  Text(
      text = stringResource(R.string.all_letters_text),
      fontWeight = FontWeight.Bold,
      fontSize = 16.sp,
      color = MaterialTheme.colorScheme.onBackground,
      modifier = Modifier.testTag("AllLetterLearned"))
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
              .clip(RoundedCornerShape(8.dp))
              .padding(12.dp)
              .testTag("LettersBox")) {
        LetterList(lettersLearned)
      }
}
/**
 * A table of user statistics.
 *
 * Important to note that the lists must have the same size.
 *
 * @param columnTestTag The principal tag for the column.
 * @param rowTestTag The principal tag for the row of stats.
 * @param lineText The text description for the statistic to show.
 * @param lineTextTestTag The text tag of the lineText.
 * @param statsTexts The list of type of stats to display.
 * @param statsNumberList The list of number for stats to display.
 */
@Composable
fun StatisticsTable(
    lineText: String,
    statsTexts: List<String>,
    statsNumberList: List<String>,
    columnTestTag: String,
    rowTestTag: String,
    lineTextTestTag: String
) {
  // Ensure that the lists have the same size
  require(statsTexts.size == statsNumberList.size) { "The lists must have the same size." }
  // Construction of the statistic column
  Column(
      modifier = Modifier.fillMaxWidth().testTag(columnTestTag),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center) {
        Text(
            text = lineText,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.testTag(lineTextTestTag))
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth().testTag(rowTestTag),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
              for (index in statsTexts.indices) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                      Text(
                          text = statsTexts[index],
                          fontSize = 12.sp,
                          color = MaterialTheme.colorScheme.onBackground,
                          modifier = Modifier.testTag(statsTexts[index]))
                      Spacer(modifier = Modifier.width(12.dp))
                      Row(
                          modifier =
                              Modifier.size(40.dp)
                                  .border(
                                      2.dp,
                                      MaterialTheme.colorScheme.onBackground,
                                      RoundedCornerShape(12.dp))
                                  .clip(RoundedCornerShape(12.dp))
                                  .background(MaterialTheme.colorScheme.background),
                          horizontalArrangement = Arrangement.Center,
                          verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = statsNumberList[index],
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                // textAlign = TextAlign.Center,
                                modifier = Modifier.testTag(statsNumberList[index]))
                          }
                    }
              }
            }
      }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun LetterList(learnedLetters: List<Char>) {
  val allLetters = ('A'..'Z').toList() // All capital letters from A to Z
  val scrollState = rememberScrollState()
  Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState).testTag("LettersList")) {
    allLetters.forEach { letter ->
      val isLearned = letter in learnedLetters
      Text(
          text = letter.toString(),
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color =
              if (isLearned) MaterialTheme.colorScheme.primary
              else MaterialTheme.colorScheme.primary.copy(alpha = .5f),
          modifier = Modifier.padding(horizontal = 8.dp).testTag(letter.toString()))
    }
  }
}
/**
 * A streak counter that displays a user's streak in days.
 *
 * @param days A long value for the number of days (streak).
 */
@Composable
fun StreakCounter(days: Long) {
  Row(
      modifier = Modifier.testTag("StreakCounter"),
      verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = R.drawable.flame),
            contentDescription = "Streak Icon",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(32.dp).testTag("FlameIcon"))
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$days",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 20.sp,
            modifier = Modifier.testTag("NumberOfDays"))
      }
}
