package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.ReusableTextButton
import com.github.se.signify.ui.navigation.NavigationActions

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChallengeHistoryScreen(
    navigationActions: NavigationActions,
    friendsChallengesAchieved: Int,
    challengesCreated: Int
) {
  Scaffold(
      topBar = {
        // Top blue bar
        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .height(4.dp)
                    .background(colorResource(R.color.blue))
                    .testTag("TopBlueBar"))
      },
      content = { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).testTag("ChallengeHistoryScreen")) {
          // Back button aligned to the top-left corner
          BackButton { navigationActions.goBack() }

          Column(
              modifier =
                  Modifier.fillMaxSize()
                      .padding(
                          top = 80.dp,
                          start = 16.dp,
                          end = 16.dp) // Padding to avoid overlap with the back button
                      .testTag("ChallengeHistoryContent"),
              horizontalAlignment = Alignment.CenterHorizontally) {
                // Number of friends' challenges achieved
                Row(
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("FriendsChallengesRow"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                      Text(
                          text = "Number of friends challenges achieved",
                          fontSize = 16.sp,
                          color = colorResource(R.color.black),
                          modifier = Modifier.testTag("FriendsChallengesText"))
                      Box(
                          modifier =
                              Modifier.size(50.dp)
                                  .border(2.dp, colorResource(R.color.blue))
                                  .background(colorResource(R.color.white))
                                  .testTag("FriendsChallengesCountBox"),
                          contentAlignment = Alignment.Center) {
                            Text(
                                text = "$friendsChallengesAchieved",
                                fontSize = 20.sp,
                                color = colorResource(R.color.black),
                                modifier = Modifier.testTag("FriendsChallengesCount"))
                          }
                    }

                // Number of challenges created
                Row(
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("ChallengesCreatedRow"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                      Text(
                          text = "Number of challenges created",
                          fontSize = 16.sp,
                          color = colorResource(R.color.black),
                          modifier = Modifier.testTag("ChallengesCreatedText"))
                      Box(
                          modifier =
                              Modifier.size(50.dp)
                                  .border(2.dp, colorResource(R.color.blue))
                                  .background(colorResource(R.color.white))
                                  .testTag("ChallengesCreatedCountBox"),
                          contentAlignment = Alignment.Center) {
                            Text(
                                text = "$challengesCreated",
                                fontSize = 20.sp,
                                color = colorResource(R.color.black),
                                modifier = Modifier.testTag("ChallengesCreatedCount"))
                          }
                    }

                Spacer(modifier = Modifier.height(60.dp))

                // Graphs and statistics button
                ReusableTextButton(
                    onClickAction = { /* Do nothing for now */},
                    textTag = "GraphsStatisticsButton",
                    text = stringResource(R.string.graphs_history),
                    height = 240.dp,
                    borderColor = colorResource(R.color.blue),
                    backgroundColor = colorResource(R.color.black),
                    textSize = 30.sp,
                    textColor = colorResource(R.color.white))
              }
        }
      })
}
