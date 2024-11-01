package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.ReusableTextButton
import com.github.se.signify.ui.navigation.NavigationActions

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewChallengeScreen(navigationActions: NavigationActions) {
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
        Box(modifier = Modifier.fillMaxSize().padding(padding).testTag("NewChallengeScreen")) {
          // Back button aligned to the top-left corner
          BackButton { navigationActions.goBack() }

          Column(
              modifier =
                  Modifier.fillMaxSize()
                      .padding(
                          top = 80.dp,
                          start = 16.dp,
                          end = 16.dp) // Padding to avoid overlap with the back button
                      .testTag("NewChallengeContent"),
              horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(70.dp))

                // My Friends button
                ReusableTextButton(
                    onClickAction = { navigationActions.navigateTo("Friends") },
                    textTag = "MyFriendsButton",
                    text = "My Friends",
                    height = 60.dp,
                    borderColor = colorResource(R.color.dark_gray),
                    backgroundColor = colorResource(R.color.blue),
                    textSize = 26.sp,
                    textColor = colorResource(R.color.dark_gray))

                Spacer(modifier = Modifier.height(40.dp)) // Increased space between buttons

                // Create a challenge button
                ReusableTextButton(
                    onClickAction = { navigationActions.navigateTo("CreateChallenge") },
                    textTag = "CreateChallengeButton",
                    text = "Create a Challenge",
                    height = 60.dp,
                    borderColor = colorResource(R.color.dark_gray),
                    backgroundColor = colorResource(R.color.blue),
                    textSize = 26.sp,
                    textColor = colorResource(R.color.dark_gray))

                Spacer(
                    modifier =
                        Modifier.height(60.dp)) // Increased space between buttons and the box

                // My Friends' Challenge section
                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .border(2.dp, colorResource(R.color.black))
                            .background(colorResource(R.color.blue))
                            .padding(16.dp)
                            .testTag("MyFriendsChallengeBox")) {
                      Column(
                          horizontalAlignment = Alignment.CenterHorizontally,
                          modifier = Modifier.testTag("MyFriendsChallengeContent")) {
                            Text(
                                text = "My Friends Challenge",
                                fontSize = 20.sp,
                                color = colorResource(R.color.black),
                                modifier = Modifier.testTag("FriendsChallengeTitle"))

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        24.dp)) // Increased space between title and buttons

                            // Challenge buttons (only esthetic, not implemented the logic yet)
                            for (i in 1..3) {
                              ReusableTextButton(
                                  onClickAction = { /* Do nothing for now */},
                                  textTag = "ChallengeButton$i",
                                  text = "Challenge $i",
                                  height = 40.dp,
                                  borderColor = colorResource(R.color.dark_gray),
                                  backgroundColor = colorResource(R.color.white),
                                  textSize = 20.sp,
                                  textColor = colorResource(R.color.dark_gray))
                              Spacer(modifier = Modifier.height(16.dp))
                            }
                          }
                    }
              }
        }
      })
}
