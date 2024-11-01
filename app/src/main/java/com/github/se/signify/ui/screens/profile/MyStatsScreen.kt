package com.github.se.signify.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun MyStatsScreen(
    navigationActions: NavigationActions,
    numberOfDays: Int,
    lettersLearned: List<Char>,
    exercisesAchieved: List<Int>,
    questsAchieved: List<Int>
) {
  Scaffold(
      topBar = {
        // Top blue bar
        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .testTag("TopBlueBar"))
      },
      content = { padding ->
        Column(
            modifier =
                Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(16.dp)
                    .testTag("MyStatsScreen"),
            horizontalAlignment = Alignment.CenterHorizontally) {
              // Number of days
              BackButton { navigationActions.goBack() }
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.flame),
                        contentDescription = "Days Icon",
                        tint = colorResource(R.color.red),
                        modifier = Modifier.size(32.dp).testTag("flameIcon"))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$numberOfDays days",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("numberOfDays"))
                  }

              Spacer(modifier = Modifier.height(32.dp))

              // Letters learned
              Text(
                  text = "All letters learned",
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = colorResource(R.color.dark_gray),
                  modifier = Modifier.testTag("allLetterLearned"))
              Box(
                  modifier =
                      Modifier.fillMaxWidth()
                          .border(2.dp, colorResource(R.color.dark_gray), RoundedCornerShape(12.dp))
                          .clip(RoundedCornerShape(8.dp))
                          .padding(12.dp)
                          .testTag("lettersBox")) {
                    HorizontalLetterList(lettersLearned)
                  }

              Spacer(modifier = Modifier.height(64.dp))

              // Number of exercises achieved
              Row(
                  modifier =
                      Modifier.fillMaxWidth().padding(vertical = 8.dp).testTag("ExercisesRow"),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Number of exercises achieved",
                        fontSize = 16.sp,
                        color = colorResource(R.color.black),
                        modifier = Modifier.testTag("ExercisesText"))
                    Box(
                        modifier =
                            Modifier.size(50.dp)
                                .border(2.dp, colorResource(R.color.blue))
                                .clip(RoundedCornerShape(12.dp))
                                .background(colorResource(R.color.white))
                                .testTag("ExercisesEasyCountBox"),
                        contentAlignment = Alignment.Center) {
                          Column(
                              modifier = Modifier.fillMaxSize(),
                              horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "EASY",
                                    fontSize = 12.sp,
                                    color = colorResource(R.color.black),
                                    modifier = Modifier.testTag("Easy"))
                                Text(
                                    text = "${exercisesAchieved[0]}",
                                    fontSize = 20.sp,
                                    color = colorResource(R.color.black),
                                    modifier = Modifier.testTag("ExercisesEasyCount"))
                              }
                        }
                    Box(
                        modifier =
                            Modifier.size(50.dp)
                                .border(2.dp, colorResource(R.color.blue))
                                .clip(RoundedCornerShape(12.dp))
                                .background(colorResource(R.color.white))
                                .testTag("ExercisesHardCountBox"),
                        contentAlignment = Alignment.Center) {
                          Column(
                              modifier = Modifier.fillMaxSize(),
                              horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "HARD",
                                    fontSize = 12.sp,
                                    color = colorResource(R.color.black),
                                    modifier = Modifier.testTag("Hard"))
                                Text(
                                    text = "${exercisesAchieved[1]}",
                                    fontSize = 20.sp,
                                    color = colorResource(R.color.black),
                                    modifier = Modifier.testTag("ExercisesHardCount"))
                              }
                        }
                  }

              Spacer(modifier = Modifier.height(12.dp))

              // Number of quests achieved
              Row(
                  modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).testTag("QuestsRow"),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Number of quests achieved",
                        fontSize = 16.sp,
                        color = colorResource(R.color.black),
                        modifier = Modifier.testTag("QuestsText"))
                    Box(
                        modifier =
                            Modifier.size(50.dp)
                                .border(2.dp, colorResource(R.color.blue))
                                .clip(RoundedCornerShape(12.dp))
                                .background(colorResource(R.color.white))
                                .testTag("DailyQuestCountBox"),
                        contentAlignment = Alignment.Center) {
                          Column(
                              modifier = Modifier.fillMaxSize(),
                              horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "DAILY",
                                    fontSize = 12.sp,
                                    color = colorResource(R.color.black),
                                    modifier = Modifier.testTag("Daily"))
                                Text(
                                    text = "${questsAchieved[0]}",
                                    fontSize = 20.sp,
                                    color = colorResource(R.color.black),
                                    modifier = Modifier.testTag("DailyQuestCount"))
                              }
                        }
                    Box(
                        modifier =
                            Modifier.size(50.dp)
                                .border(2.dp, colorResource(R.color.blue))
                                .clip(RoundedCornerShape(12.dp))
                                .background(colorResource(R.color.white))
                                .testTag("WeeklyQuestsCountBox"),
                        contentAlignment = Alignment.Center) {
                          Column(
                              modifier = Modifier.fillMaxSize(),
                              horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "WEEKLY",
                                    fontSize = 12.sp,
                                    color = colorResource(R.color.black),
                                    modifier = Modifier.testTag("Weekly"))
                                Text(
                                    text = "${questsAchieved[1]}",
                                    fontSize = 20.sp,
                                    color = colorResource(R.color.black),
                                    modifier = Modifier.testTag("WeeklyQuestsCount"))
                              }
                        }
                  }

              Spacer(modifier = Modifier.height(60.dp))

              // Graphs and statistics
              Box(
                  modifier =
                      Modifier.fillMaxWidth()
                          .height(240.dp)
                          .clip(RoundedCornerShape(12.dp))
                          .background(colorResource(R.color.black))
                          .padding(16.dp)
                          .testTag("GraphsAndStats"),
                  contentAlignment = Alignment.Center) {
                    Text(
                        text = "Graphs and stats",
                        fontSize = 16.sp,
                        color = colorResource(R.color.white),
                        fontWeight = FontWeight.Normal)
                  }
            }
      })
}
