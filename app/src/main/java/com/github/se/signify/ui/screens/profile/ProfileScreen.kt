package com.github.se.signify.ui.screens.profile

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.model.navigation.TopLevelDestinations
import com.github.se.signify.model.profile.stats.StatsRepository
import com.github.se.signify.model.profile.stats.StatsViewModel
import com.github.se.signify.ui.common.AccountInformation
import com.github.se.signify.ui.common.BasicButton
import com.github.se.signify.ui.common.FriendsButton
import com.github.se.signify.ui.common.HelpText
import com.github.se.signify.ui.common.LearnedLetterList
import com.github.se.signify.ui.common.MainScreenScaffold
import com.github.se.signify.ui.common.StatisticsTable
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository,
    statsRepository: StatsRepository,
) {
  val userViewModel: UserViewModel =
      viewModel(factory = UserViewModel.factory(userSession, userRepository))
  val statsViewModel: StatsViewModel =
      viewModel(factory = StatsViewModel.factory(userSession, statsRepository))

  MainScreenScaffold(
      navigationActions = navigationActions,
      topLevelDestination = TopLevelDestinations.PROFILE,
      testTag = "ProfileScreen",
      helpText =
          HelpText(
              title = stringResource(R.string.profile_text),
              content = stringResource(R.string.help_profile_screen_text)),
      topBarButtons =
          listOf({ SettingsButton(navigationActions) }, { FriendsButton(navigationActions) }),
      content = {
        LaunchedEffect(Unit) {
          userViewModel.getUserName()
          userViewModel.getProfilePictureUrl()
          userViewModel.updateStreak()
          userViewModel.getStreak()
          statsViewModel.getEasyExerciseStats()
          statsViewModel.getMediumExerciseStats()
          statsViewModel.getHardExerciseStats()
          statsViewModel.getDailyQuestStats()
          statsViewModel.getWeeklyQuestStats()
        }
        val userName = userViewModel.userName.collectAsState()
        val profilePictureUrl = userViewModel.profilePictureUrl.collectAsState()
        val streak = userViewModel.streak.collectAsState()
        val lettersLearned = statsViewModel.lettersLearned.collectAsState()
        val easy = statsViewModel.easy.collectAsState()
        val medium = statsViewModel.medium.collectAsState()
        val hard = statsViewModel.hard.collectAsState()
        val daily = statsViewModel.daily.collectAsState()
        val weekly = statsViewModel.weekly.collectAsState()
        var updatedProfilePicture by remember { mutableStateOf(profilePictureUrl.value) }

        LaunchedEffect(profilePictureUrl.value) { updatedProfilePicture = profilePictureUrl.value }

        Spacer(modifier = Modifier.height(32.dp))

        val unknownText = stringResource(R.string.unknown_text)
        // Top information
        AccountInformation(
            userId =
                FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: unknownText,
            userName = userName.value,
            profilePictureUrl = updatedProfilePicture,
            streak = streak.value)

        Spacer(modifier = Modifier.height(32.dp))

        // Letters learned
        LearnedLetterList(lettersLearned = lettersLearned.value)
        Spacer(modifier = Modifier.height(32.dp))

        // Number of exercises achieved
        val exercisesText = stringResource(R.string.completed_exercise_count_text)
        val easyExercisesText = stringResource(R.string.easy_exercises_text)
        val mediumExercisesText = stringResource(R.string.medium_exercises_text)
        val hardExercisesText = stringResource(R.string.hard_exercises_text)
        StatisticsTable(
            columnTestTag = "ExercisesColumn",
            rowTestTag = "ExercisesRow",
            lineText = exercisesText,
            statsTexts = listOf(easyExercisesText, mediumExercisesText, hardExercisesText),
            statsNumberList = listOf("${easy.value}", "${medium.value}", "${hard.value}"),
            lineTextTestTag = "ExercisesText")
        Spacer(modifier = Modifier.height(32.dp))

        // Number of quests achieved
        val questsText = stringResource(R.string.completed_quest_count_text)
        val dailyQuestsText = stringResource(R.string.daily_quests_text)
        val weeklyQuestsText = stringResource(R.string.weekly_quests_text)
        StatisticsTable(
            columnTestTag = "QuestsColumn",
            rowTestTag = "QuestsRow",
            lineText = questsText,
            statsTexts = listOf(dailyQuestsText, weeklyQuestsText),
            statsNumberList = listOf("${daily.value}", "${weekly.value}"),
            lineTextTestTag = "QuestsText")
      },
  )
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun SettingsButton(navigationActions: NavigationActions) {
  BasicButton(
      onClick = { navigationActions.navigateTo(Screen.SETTINGS) },
      iconTestTag = "SettingsIcon",
      contentDescription = "Settings",
      modifier = Modifier.testTag("SettingsButton"),
      icon = Icons.Outlined.Settings)
}
