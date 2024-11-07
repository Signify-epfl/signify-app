package com.github.se.signify.ui.screens.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.se.signify.ui.AccountInformation
import com.github.se.signify.ui.AllLetterLearned
import com.github.se.signify.ui.NotImplementedYet
import com.github.se.signify.ui.ScaffoldAnnexeScreen
import com.github.se.signify.ui.StatisticsRow
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun MyStatsScreen(
    navigationActions: NavigationActions,
    userId: String,
    userName: String,
    profilePictureUrl: String?,
    numberOfDays: Int,
    lettersLearned: List<Char>,
    exercisesAchieved: List<Int>,
    questsAchieved: List<Int>
) {
  ScaffoldAnnexeScreen(
      navigationActions = navigationActions,
      testTagColumn = "MyStatsScreen",
  ) {
    // Top information
    AccountInformation(
        userId = userId,
        userName = userName,
        profilePictureUrl = profilePictureUrl,
        days = numberOfDays)
    Spacer(modifier = Modifier.height(32.dp))

    // Letters learned
    AllLetterLearned(lettersLearned = lettersLearned)
    Spacer(modifier = Modifier.height(64.dp))

    // Number of exercises achieved
    StatisticsRow(
        rowTestTag = "ExercisesRow",
        lineText = "Number of exercises achieved",
        lineTextTag = "ExercisesText",
        columnTextList =
            listOf(
                listOf("EASY", "${exercisesAchieved[0]}"),
                listOf("HARD", "${exercisesAchieved[1]}")),
        columnTextSPList = listOf(listOf(12, 20), listOf(12, 20)),
        columnTextTagList = listOf("ExercisesEasyCountBox", "ExercisesHardCountBox"))
    Spacer(modifier = Modifier.height(12.dp))

    // Number of quests achieved
    StatisticsRow(
        rowTestTag = "QuestsRow",
        lineText = "Number of quests achieved",
        lineTextTag = "QuestsText",
        columnTextList =
            listOf(
                listOf("DAILY", "${questsAchieved[0]}"), listOf("WEEKLY", "${questsAchieved[1]}")),
        columnTextSPList = listOf(listOf(12, 20), listOf(12, 20)),
        columnTextTagList = listOf("DailyQuestCountBox", "WeeklyQuestsCountBox"))
    Spacer(modifier = Modifier.height(64.dp))

    // Graphs and Stats
    NotImplementedYet(testTag = "GraphsAndStats", text = "Graphs and Stats")
  }
}
