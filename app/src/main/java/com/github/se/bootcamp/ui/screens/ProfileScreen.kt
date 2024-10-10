package com.github.se.bootcamp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberImagePainter
import com.github.se.bootcamp.R
import com.github.se.bootcamp.ui.navigation.BottomNavigationMenu
import com.github.se.bootcamp.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.bootcamp.ui.navigation.NavigationActions


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    userId: String,
    userName: String,
    profilePictureUrl: String?,
    numberOfDays: Int,
    lettersLearned: List<Char>,
    easyExercises: Int,
    hardExercises: Int,
    dailyQuests: Int,
    weeklyQuests: Int,
    onGraphClick: () -> Unit,
    navigationActions: NavigationActions
) {
    Scaffold (
        bottomBar = {
            BottomNavigationMenu(
                onTabSelect = { route -> navigationActions.navigateTo(route) },
                tabList = LIST_TOP_LEVEL_DESTINATION,
                selectedItem = navigationActions.currentRoute())
        },
        content = { Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            var isHelpBoxVisible by remember { mutableStateOf(false) }

            // Top row with Settings and Help buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(onClick = {isHelpBoxVisible = !isHelpBoxVisible},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF05A9FB))) {
                    Icon(Icons.Outlined.Info, tint = Color.White, contentDescription = "Help")
                }


                Button(onClick = { navigationActions.navigateTo("Settings") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF05A9FB))
                ) {
                    Icon(Icons.Outlined.Settings , tint = Color.White, contentDescription = "Settings")
                }



                if (isHelpBoxVisible) {
                    Dialog(onDismissRequest = { isHelpBoxVisible = false }) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(16.dp)
                                .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("This is your personal space where you can:\n" +
                                        "\n" +
                                        "- View and edit your profile.\n" +
                                        "\n" +
                                        "- Track your progress: See how far you have come in your ASL journey, with clear insights into what you have learned and what is next.",
                                    color = Color.Black)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { isHelpBoxVisible = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF05A9FB))) {
                                    Text("Close")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User Info: Display Test ID and Test Name in a Column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = userId, fontWeight = FontWeight.Bold)  // Test ID
                    Text(text = userName, fontWeight = FontWeight.Bold)  // Test Name
                }

                Spacer(modifier = Modifier.width(24.dp))

                // Profile Picture and Number of Days

                ProfilePicture(profilePictureUrl)

                Spacer(modifier = Modifier.width(24.dp))

                Box(
                    modifier = Modifier
                        //.background(Color.Blue, shape = RoundedCornerShape(8.dp))
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.flame), // Replace with your icon resource
                            contentDescription = "Days Icon",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$numberOfDays days",
                            //color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(64.dp))

            // Letters learned
            Text(text = "All letters learned", fontWeight = FontWeight.Bold)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .padding(12.dp)
                //.background(Color(0xFFDCF1FA))
            ) {
                HorizontalLetterList(lettersLearned)
            }

            Spacer(modifier = Modifier.height(64.dp))



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Number of exercises achieved (Easy & Hard boxes)
                Text(text = "Exercises \n achieved", fontWeight = FontWeight.Bold)

                // Easy Exercises
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Easy :")
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCF1FA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text =  if (easyExercises > 0) "$easyExercises" else "Ø",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                //Spacer(modifier = Modifier.width(32.dp)) // Space between Easy and Hard

                // Hard Exercises
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Hard :")
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCF1FA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (hardExercises > 0) "$hardExercises" else "Ø",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Number of quests achieved
                Text(text = "Questions \n achieved", fontWeight = FontWeight.Bold)

                // Daily Quests
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Daily :")
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCF1FA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text =  if (dailyQuests > 0) "$dailyQuests" else "Ø",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                //Spacer(modifier = Modifier.width(24.dp))

                // Weekly Quests
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Weekly :")
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCF1FA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (weeklyQuests > 0) "$weeklyQuests" else "Ø",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Friends List button
            Button(onClick = {navigationActions.navigateTo("Friends")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF05A9FB)),
                modifier = Modifier.fillMaxWidth()) {
                Text("My Friends", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Performance Graph Button

            Button(onClick = onGraphClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF05A9FB)),
                modifier = Modifier.fillMaxWidth()) {
                Text("Performance Graph", fontWeight = FontWeight.Bold)
            }



            Spacer(modifier = Modifier.height(64.dp))
        }
        }
    )
}

@Composable
fun HorizontalLetterList(lettersLearned: List<Char>) {
    val allLetters = ('A'..'Z').toList() // All capital letters from A to Z
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
    ) {
        allLetters.forEach { letter ->
            val isLearned = letter in lettersLearned
            Text(
                text = letter.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isLearned) Color(0xFF2E7D32) else Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ProfilePicture(profilePictureUrl: String?) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
    ) {
        profilePictureUrl?.let {
            // Load image with Coil or any other image loading library
            Image(
                painter = rememberImagePainter(data = it),
                contentDescription = "Profile picture",
                modifier = Modifier.fillMaxSize()
            )
        } ?: Text(
            text = "Profile",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

