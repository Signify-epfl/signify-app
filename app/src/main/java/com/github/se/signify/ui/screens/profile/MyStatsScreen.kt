package com.github.se.signify.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun MyStatsScreen(navigationActions: NavigationActions) {
  Column(modifier = Modifier.testTag("StatsScreen")) { Text(text = "My stats Screen") }
}
