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
import com.github.se.signify.ui.common.MainScreenScaffold
import com.github.se.signify.ui.common.SquareButton

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChallengeScreen(navigationActions: NavigationActions) {
  MainScreenScaffold(
      navigationActions = navigationActions,
      testTag = "ChallengeScreen",
      helpTitle = "Challenge",
      helpText = stringResource(R.string.description_challenge)) {
        Spacer(modifier = Modifier.height(64.dp))

        // Challenge square button
        SquareButton(
            iconId = R.drawable.battleicon,
            onClick = { navigationActions.navigateTo(Screen.NEW_CHALLENGE) },
            text = "Challenge",
            testTag = "ChallengeButton",
            size = 240,
        )
        Spacer(modifier = Modifier.height(32.dp))

        // History square button
        SquareButton(
            iconId = R.drawable.historyicon,
            onClick = { navigationActions.navigateTo(Screen.CHALLENGE_HISTORY) },
            text = "History",
            testTag = "HistoryButton",
            size = 240,
        )
      }
}
