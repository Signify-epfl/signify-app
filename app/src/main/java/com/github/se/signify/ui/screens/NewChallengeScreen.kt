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
import com.github.se.signify.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.ui.ReusableTextButton
import com.github.se.signify.ui.navigation.NavigationActions

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
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
      content = {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).testTag("NewChallengeContent"),
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

              Spacer(modifier = Modifier.height(70.dp))

              // My Friends button
              ReusableTextButton(
                  { navigationActions.navigateTo("Friends") },
                  "MyFriendsButton",
                  "My Friends",
                  60.dp,
                  Color.DarkGray,
                  colorResource(R.color.blue),
                  26.sp,
                  Color.DarkGray,
              )

              Spacer(modifier = Modifier.height(40.dp)) // Increased space between buttons

              // Create a challenge button
              ReusableTextButton(
                  { /* Do nothing for now */},
                  "CreateChallengeButton",
                  "Create a Challenge",
                  60.dp,
                  Color.DarkGray,
                  colorResource(R.color.blue),
                  26.sp,
                  Color.DarkGray,
              )

              Spacer(
                  modifier = Modifier.height(60.dp)) // Increased space between buttons and the box

              // My Friends' Challenge section
              Box(
                  modifier =
                      Modifier.fillMaxWidth()
                          .border(2.dp, Color.Black)
                          .background(colorResource(R.color.blue))
                          .padding(16.dp)
                          .testTag("MyFriendsChallengeBox")) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.testTag("MyFriendsChallengeContent")) {
                          Text(
                              text = "My Friends Challenge",
                              fontSize = 20.sp,
                              color = Color.Black,
                              modifier = Modifier.testTag("FriendsChallengeTitle"))

                          Spacer(
                              modifier =
                                  Modifier.height(
                                      24.dp)) // Increased space between title and buttons

                          // Challenge buttons (only esthetic, not implemented the logic yet)
                          for (i in 1..3) {
                            ReusableTextButton(
                                { /* Do nothing for now */},
                                "ChallengeButton$i",
                                "Challenge $i",
                                40.dp,
                                Color.DarkGray,
                                Color.White,
                                20.sp,
                                Color.DarkGray)
                            Spacer(modifier = Modifier.height(16.dp))
                          }
                        }
                  }
            }
      })
}
