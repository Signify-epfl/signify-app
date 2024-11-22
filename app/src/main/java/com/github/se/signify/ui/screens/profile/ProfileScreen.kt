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
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.stats.StatsViewModel
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.AccountInformation
import com.github.se.signify.ui.LearnedLetterList
import com.github.se.signify.ui.MainScreenScaffold
import com.github.se.signify.ui.SquareButton
import com.github.se.signify.ui.UtilButton
import com.github.se.signify.ui.WhiteOfflineScreen
import com.github.se.signify.ui.isOfflineState
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository,
    statsRepository: StatsRepository,
    userViewModel: UserViewModel =
        viewModel(factory = UserViewModel.factory(userSession, userRepository)),
    statsViewModel: StatsViewModel =
        viewModel(factory = StatsViewModel.factory(userSession, statsRepository))
) {
  if (isOfflineState) {
    WhiteOfflineScreen(navigationActions = navigationActions)
  } else {
    MainScreenScaffold(
        navigationActions = navigationActions,
        testTagColumn = "ProfileScreen",
        helpTitle = "Profile",
        helpText = stringResource(R.string.help_profile_screen),
    ) {
      LaunchedEffect(Unit) {
        userViewModel.getUserName(currentUserId)
        userViewModel.getProfilePictureUrl(currentUserId)
        userViewModel.updateStreak(currentUserId)
        userViewModel.getStreak(currentUserId)
      }
      val userName = userViewModel.userName.collectAsState()
      val profilePictureUrl = userViewModel.profilePictureUrl.collectAsState()
      val streak = userViewModel.streak.collectAsState()
      val lettersLearned = statsViewModel.lettersLearned.collectAsState()
      var updatedProfilePicture by remember { mutableStateOf(profilePictureUrl.value) }

      LaunchedEffect(profilePictureUrl.value) { updatedProfilePicture = profilePictureUrl.value }

      // Settings button
      UtilButton(
          onClick = { navigationActions.navigateTo(Screen.SETTINGS) },
          buttonTestTag = "SettingsButton",
          iconTestTag = "SettingsIcon",
          icon = Icons.Outlined.Settings,
          contentDescription = "Settings")
      Spacer(modifier = Modifier.height(32.dp))

      // Top information
      AccountInformation(
          userId = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: "unknown",
          userName = userName.value,
          profilePictureUrl = updatedProfilePicture,
          days = streak.value)

      Spacer(modifier = Modifier.height(32.dp))

      // Letters learned
      LearnedLetterList(lettersLearned = lettersLearned.value)
      Spacer(modifier = Modifier.height(32.dp))

      // Friends List button
      SquareButton(
          iconRes = R.drawable.friendsicon,
          label = "My Friends",
          onClick = { navigationActions.navigateTo(Screen.FRIENDS) },
          size = 200,
          modifier = Modifier.testTag("MyFriendsButton"))
      Spacer(modifier = Modifier.height(32.dp))

      // Statistics Button
      SquareButton(
          iconRes = R.drawable.statisticsicon,
          label = "My Stats",
          onClick = { navigationActions.navigateTo(Screen.STATS) },
          size = 200,
          modifier = Modifier.testTag("MyStatsButton"))
    }
  }
}
