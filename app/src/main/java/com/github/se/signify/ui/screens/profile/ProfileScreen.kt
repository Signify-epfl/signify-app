package com.github.se.signify.ui.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.AccountInformation
import com.github.se.signify.ui.LearnedLetterList
import com.github.se.signify.ui.MainScreenScaffold
import com.github.se.signify.ui.SquareButton
import com.github.se.signify.ui.UtilButton
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Route
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navigationActions: NavigationActions,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {
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

    // Settings button
    UtilButton(
        onClick = { navigationActions.navigateTo(Route.SETTINGS) },
        buttonTestTag = "SettingsButton",
        iconTestTag = "SettingsIcon",
        icon = Icons.Outlined.Settings,
        contentDescription = "Settings")
    Spacer(modifier = Modifier.height(24.dp))

    // Top information
    AccountInformation(
        userId = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: "unknown",
        userName = userName.value,
        profilePictureUrl = profilePictureUrl.value,
        days = streak.value)
    Spacer(modifier = Modifier.height(32.dp))

    // Letters learned
    LearnedLetterList(lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F'))
    Spacer(modifier = Modifier.height(64.dp))

    // Friends List button
    SquareButton(
        iconRes = R.drawable.friendsicon,
        label = "My Friends",
        onClick = { navigationActions.navigateTo(Route.FRIENDS) },
        size = 240,
        modifier = Modifier.testTag("MyFriendsButton"))
    Spacer(modifier = Modifier.height(32.dp))

    // Statistics Button
    SquareButton(
        iconRes = R.drawable.statisticsicon,
        label = "My Stats",
        onClick = { navigationActions.navigateTo(Route.STATS) },
        size = 240,
        modifier = Modifier.testTag("MyStatsButton"))
  }
}
