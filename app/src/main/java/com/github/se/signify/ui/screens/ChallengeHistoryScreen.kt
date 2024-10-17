package com.github.se.signify.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.ui.ReusableTextButton
import com.github.se.signify.ui.navigation.NavigationActions

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
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
      content = {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).testTag("ChallengeHistoryContent"),
            horizontalAlignment = Alignment.CenterHorizontally) {
              // Arrow back button
              IconButton(
                  onClick = { navigationActions.navigateTo("Challenge") },
                  modifier =
                      Modifier.align(Alignment.Start)
                          .padding(bottom = 24.dp) // Added space below the arrow button
                          .testTag("BackButton")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black)
                  }
              Spacer(modifier = Modifier.height(80.dp))
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
                        color = Color.Black,
                        modifier = Modifier.testTag("FriendsChallengesText"))
                    Box(
                        modifier =
                            Modifier.size(50.dp)
                                .border(2.dp, colorResource(R.color.blue))
                                .background(Color.White)
                                .testTag("FriendsChallengesCountBox"),
                        contentAlignment = Alignment.Center) {
                          Text(
                              text = "$friendsChallengesAchieved",
                              fontSize = 20.sp,
                              color = Color.Black,
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
                        color = Color.Black,
                        modifier = Modifier.testTag("ChallengesCreatedText"))
                    Box(
                        modifier =
                            Modifier.size(50.dp)
                                .border(2.dp, colorResource(R.color.blue))
                                .background(Color.White)
                                .testTag("ChallengesCreatedCountBox"),
                        contentAlignment = Alignment.Center) {
                          Text(
                              text = "$challengesCreated",
                              fontSize = 20.sp,
                              color = Color.Black,
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
                  backgroundColor = Color.Black,
                  textSize = 30.sp,
                  textColor = Color.White,
              )
            }
      })
}
