package com.github.se.bootcamp.ui.screens

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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.github.se.bootcamp.R



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
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onFriendsClick: () -> Unit,
    onGraphClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top row with Settings and Help buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onSettingsClick) {
                Text("Settings")
            }
            Button(onClick = onHelpClick) {
                Text("Help")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Info: Display Test ID and Test Name in a Column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                //modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = userId)  // Test ID
                Text(text = userName)  // Test Name
            }

            // Profile Picture and Number of Days

            ProfilePicture(profilePictureUrl)
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
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(32.dp))

        // Letters learned
        Text(text = "All letters learned")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color.White, RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            HorizontalLetterList(lettersLearned)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Number of exercises achieved (Easy & Hard boxes)
        Text(text = "Number of exercises achieved")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Easy Exercises
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Easy :")
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text =  if (easyExercises > 0) "$easyExercises" else "Ø",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(32.dp)) // Space between Easy and Hard

            // Hard Exercises
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Hard :")
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (hardExercises > 0) "$hardExercises" else "Ø",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Number of quests achieved
        Text(text = "Number of quests achieved")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Daily Quests
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Daily :")
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text =  if (dailyQuests > 0) "$dailyQuests" else "Ø",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            // Weekly Quests
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Weekly :")
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (weeklyQuests > 0) "$weeklyQuests" else "Ø",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Friends List button
        Button(onClick = onFriendsClick, modifier = Modifier.fillMaxWidth()) {
            Text("My Friends")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Performance Graph Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text("Performance Graph", style = MaterialTheme.typography.h6)
            // Implement graph here in the future, or call a click event for now
            Box(modifier = Modifier.fillMaxSize().clickable(onClick = onGraphClick))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
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
                color = if (isLearned) Color.Blue else Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ProfilePicture(profilePictureUrl: String?) {
    Box(
        modifier = Modifier
            .size(64.dp)
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

