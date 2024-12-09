package com.github.se.signify.ui.screens.profile

import android.annotation.SuppressLint
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
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.stats.StatsViewModel
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.common.AccountInformation
import com.github.se.signify.ui.common.BasicButton
import com.github.se.signify.ui.common.LearnedLetterList
import com.github.se.signify.ui.common.MainScreenScaffold
import com.github.se.signify.ui.common.SquareButton
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
      testTag = "ProfileScreen",
      helpTitle = "Profile",
      helpText = stringResource(R.string.help_profile_screen),
  ) {
    LaunchedEffect(Unit) {
      userViewModel.getUserName()
      userViewModel.getProfilePictureUrl()
      userViewModel.updateStreak()
      userViewModel.getStreak()
    }
    val userName = userViewModel.userName.collectAsState()
    val profilePictureUrl = userViewModel.profilePictureUrl.collectAsState()
    val streak = userViewModel.streak.collectAsState()
    val lettersLearned = statsViewModel.lettersLearned.collectAsState()
    var updatedProfilePicture by remember { mutableStateOf(profilePictureUrl.value) }

    LaunchedEffect(profilePictureUrl.value) { updatedProfilePicture = profilePictureUrl.value }

    // Settings button
    BasicButton(
        onClick = { navigationActions.navigateTo(Screen.SETTINGS) },
        iconTestTag = "SettingsIcon",
        icon = Icons.Outlined.Settings,
        contentDescription = "Settings",
        modifier = Modifier.testTag("SettingsButton"))
    Spacer(modifier = Modifier.height(32.dp))
    val unknownText = stringResource(R.string.unknown_text)
    // Top information
    AccountInformation(
        userId = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: unknownText,
        userName = userName.value,
        profilePictureUrl = updatedProfilePicture,
        streak = streak.value)

    Spacer(modifier = Modifier.height(32.dp))

    // Letters learned
    LearnedLetterList(lettersLearned = lettersLearned.value)
    Spacer(modifier = Modifier.height(32.dp))

    // Friends List button
    val myFriendsText = stringResource(R.string.my_friends_text)
    SquareButton(
        iconId = R.drawable.friendsicon,
        text = myFriendsText,
        onClick = { navigationActions.navigateTo(Screen.FRIENDS) },
        size = 200,
        modifier = Modifier.testTag("MyFriendsButton"))
    Spacer(modifier = Modifier.height(32.dp))

    // Statistics Button
    val myStatsText = stringResource(R.string.my_stats_text)
    SquareButton(
        iconId = R.drawable.statisticsicon,
        text = myStatsText,
        onClick = { navigationActions.navigateTo(Screen.STATS) },
        size = 200,
        modifier = Modifier.testTag("MyStatsButton"))
  }
}
