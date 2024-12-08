package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.se.signify.R
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.ui.MainScreenScaffold
import com.github.se.signify.ui.SquareButton

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChallengeScreen(navigationActions: NavigationActions) {
  MainScreenScaffold(
      navigationActions = navigationActions,
      testTagColumn = "ChallengeScreen",
      helpTitle = stringResource(R.string.challenge_text),
      helpText = stringResource(R.string.description_challenge)) {
        Spacer(modifier = Modifier.height(64.dp))

        // Challenge square button
        SquareButton(
            iconRes = R.drawable.battleicon,
            label = stringResource(R.string.challenge_text),
            onClick = { navigationActions.navigateTo(Screen.NEW_CHALLENGE) },
            size = 240,
            modifier = Modifier.testTag("ChallengeButton"))
        Spacer(modifier = Modifier.height(32.dp))

        // History square button
        val historyText = stringResource(R.string.history_text)
        SquareButton(
            iconRes = R.drawable.historyicon,
            label = historyText,
            onClick = { navigationActions.navigateTo(Screen.CHALLENGE_HISTORY) },
            size = 240,
            modifier = Modifier.testTag("HistoryButton"))
      }
}
