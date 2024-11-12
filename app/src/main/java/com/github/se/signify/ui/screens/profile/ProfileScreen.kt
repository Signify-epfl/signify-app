package com.github.se.signify.ui.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.se.signify.R
import com.github.se.signify.ui.AccountInformation
import com.github.se.signify.ui.LearnedLetterList
import com.github.se.signify.ui.MainScreenScaffold
import com.github.se.signify.ui.SquareButton
import com.github.se.signify.ui.UtilButton
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Route

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    userId: String,
    userName: String,
    profilePictureUrl: String?,
    numberOfDays: Int,
    lettersLearned: List<Char>,
    navigationActions: NavigationActions
) {
  MainScreenScaffold(
      navigationActions = navigationActions,
      testTagColumn = "ProfileScreen",
      helpTitle = "Profile",
      helpText = stringResource(R.string.help_profile_screen),
  ) {
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
        userId = userId,
        userName = userName,
        profilePictureUrl = profilePictureUrl,
        days = numberOfDays)
    Spacer(modifier = Modifier.height(32.dp))

    // Letters learned
    LearnedLetterList(lettersLearned = lettersLearned)
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
