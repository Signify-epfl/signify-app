package com.github.se.signify.ui.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberImagePainter
import com.github.se.signify.R
import com.github.se.signify.ui.ReusableTextButton
import com.github.se.signify.ui.UtilButton
import com.github.se.signify.ui.navigation.BottomNavigationMenu
import com.github.se.signify.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.ui.navigation.NavigationActions

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
  Scaffold(
      bottomBar = {
        BottomNavigationMenu(
            onTabSelect = { route -> navigationActions.navigateTo(route) },
            tabList = LIST_TOP_LEVEL_DESTINATION,
            selectedItem = navigationActions.currentRoute())
      },
      content = {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          var isHelpBoxVisible by remember { mutableStateOf(false) }

          // Top row with Settings and Help buttons
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween) {
                UtilButton(
                    { isHelpBoxVisible = !isHelpBoxVisible },
                    "InfoButton",
                    "InfoIcon",
                    Icons.Outlined.Info,
                    "Help")
                UtilButton(
                    { navigationActions.navigateTo("Settings") },
                    "settingsButton",
                    "settingsIcon",
                    Icons.Outlined.Settings,
                    "Settings")

                if (isHelpBoxVisible) {
                  Dialog(onDismissRequest = { isHelpBoxVisible = false }) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.LightGray,
                        modifier =
                            Modifier.padding(16.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    4.dp,
                                    colorResource(R.color.dark_gray),
                                    RoundedCornerShape(12.dp))) {
                          Column(
                              modifier = Modifier.padding(16.dp).fillMaxWidth(),
                              horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    stringResource(R.string.help_profile_screen),
                                    modifier = Modifier.testTag("helpProfileText"),
                                    color = colorResource(R.color.dark_gray))
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { isHelpBoxVisible = false },
                                    modifier = Modifier.testTag("closeButton"),
                                    colors =
                                        ButtonDefaults.buttonColors(colorResource(R.color.blue))) {
                                      Text("Close")
                                    }
                              }
                        }
                  }
                }
              }

          Spacer(modifier = Modifier.height(24.dp))

          Row(
              modifier = Modifier.fillMaxWidth().testTag("userInfo"),
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically) {

                // User Info : user id and user name
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                  Text(
                      text = userId,
                      fontWeight = FontWeight.Bold,
                      color = colorResource(R.color.dark_gray))
                  Text(
                      text = userName,
                      fontWeight = FontWeight.Bold,
                      color = colorResource(R.color.dark_gray))
                }

                Spacer(modifier = Modifier.width(24.dp))

                // Profile Picture
                ProfilePicture(profilePictureUrl)

                Spacer(modifier = Modifier.width(24.dp))

                // Number of days

                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                      painter =
                          painterResource(id = R.drawable.flame), // Replace with your icon resource
                      contentDescription = "Days Icon",
                      tint = Color.Red,
                      modifier = Modifier.size(32.dp).testTag("flameIcon"))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(text = "$numberOfDays days", fontWeight = FontWeight.Bold)
                }
              }

          Spacer(modifier = Modifier.height(64.dp))

          // Letters learned
          Text(
              text = "All letters learned",
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = colorResource(R.color.dark_gray))

          Box(
              modifier =
                  Modifier.fillMaxWidth()
                      .border(2.dp, colorResource(R.color.dark_gray), RoundedCornerShape(12.dp))
                      .clip(RoundedCornerShape(8.dp))
                      .padding(12.dp)
                      .testTag("lettersBox")) {
                HorizontalLetterList(lettersLearned)
              }

          Spacer(modifier = Modifier.height(64.dp))

          // Friends List button
          ReusableTextButton(
              { navigationActions.navigateTo("Friends") },
              "My FriendsButton",
              "My Friends",
              40.dp,
              Color.DarkGray,
              Color(0xFF05A9FB),
              16.sp,
              Color.DarkGray)

          Spacer(modifier = Modifier.height(32.dp))

          // Performance Graph Button
          ReusableTextButton(
              { navigationActions.navigateTo("Stats") },
              "My StatsButton",
              "My Stats",
              40.dp,
              Color.DarkGray,
              Color(0xFF05A9FB),
              16.sp,
              Color.DarkGray)

          Spacer(modifier = Modifier.height(64.dp))
        }
      })
}

@Composable
fun HorizontalLetterList(lettersLearned: List<Char>) {
  val allLetters = ('A'..'Z').toList() // All capital letters from A to Z
  val scrollState = rememberScrollState()

  Row(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState).testTag("lettersList")) {
    allLetters.forEach { letter ->
      val isLearned = letter in lettersLearned
      Text(
          text = letter.toString(),
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color = if (isLearned) colorResource(R.color.blue) else Color.Gray,
          modifier = Modifier.padding(horizontal = 8.dp))
    }
  }
}

@Composable
fun ProfilePicture(profilePictureUrl: String?) {
  Box(
      modifier =
          Modifier.size(80.dp)
              .clip(CircleShape)
              .background(colorResource(R.color.dark_gray))
              .testTag("profilePicture")) {
        profilePictureUrl?.let {
          // Load image with Coil or any other image loading library
          Image(
              painter = rememberImagePainter(data = it),
              contentDescription = "Profile picture",
              modifier = Modifier.fillMaxSize())
        }
            ?: Text(
                text = "Profile", modifier = Modifier.align(Alignment.Center), color = Color.White)
      }
}
