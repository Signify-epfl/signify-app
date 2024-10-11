package com.github.se.bootcamp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.github.se.bootcamp.ui.navigation.BottomNavigationMenu
import com.github.se.bootcamp.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.bootcamp.ui.navigation.NavigationActions

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChallengeScreen(
    navigationActions: NavigationActions
) {
    Scaffold(
        bottomBar = {
            Column {
                // Bottom blue line moved 2cm lower
                Spacer(modifier = Modifier.height(75.6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color(0xFF05A9FB))
                )
                BottomNavigationMenu(
                    onTabSelect = { route -> navigationActions.navigateTo(route) },
                    tabList = LIST_TOP_LEVEL_DESTINATION,
                    selectedItem = navigationActions.currentRoute()
                )
            }
        },
        content = {
            var isHelpBoxVisible by remember { mutableStateOf(false) }

            // Main Column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)  // Whole background now white
                    .padding(top = 32.dp), // Adjusted padding to move everything lower
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top blue line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color(0xFF05A9FB))
                )

                // Top row with Info button aligned to the right (blue button without border)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.End // Aligned to the right
                ) {
                    Button(
                        onClick = { isHelpBoxVisible = !isHelpBoxVisible },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF05A9FB)), // Blue button without border
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)) // Ensure the shape matches exactly
                    ) {
                        Icon(Icons.Outlined.Info, tint = Color.White, contentDescription = "Help")
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
                                    Text(
                                        "This is your personal space where you can:\n" +
                                                "\n" +
                                                "- View and edit your profile.\n" +
                                                "\n" +
                                                "- Track your progress: See how far you have come in your ASL journey, with clear insights into what you have learned and what is next.",
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { isHelpBoxVisible = false },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF05A9FB))
                                    ) {
                                        Text("Close")
                                    }
                                }
                            }
                        }
                    }
                }

                // Space of 2cm (75.6dp) between Info button and My Friends button
                Spacer(modifier = Modifier.height(75.6.dp))

                // My Friends Button (blue background, reduced corner radius for more rectangular shape)
                Button(
                    onClick = { navigationActions.navigateTo("Friends") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF05A9FB)),  // Blue button background
                    modifier = Modifier
                        .width(250.dp)  // Reduced width for the button
                        .height(65.dp)  // Reduced height
                          // Dark gray border, more rectangular (corner radius 4.dp)
                        .clip(RoundedCornerShape(4.dp))  // Reduced corner radius for more rectangular shape
                ) {
                    Text(
                        "My Friends",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp, // Reduced text size
                        color = Color.DarkGray  // Text in dark gray
                    )
                }

                // Space of 2cm (75.6dp) between My Friends button and Challenges with friends box
                Spacer(modifier = Modifier.height(75.6.dp))

                // Large Box with "Challenges with friends" (remains unchanged)
                Box(
                    modifier = Modifier
                        .width(250.dp)  // Reduced width to make it more square
                        .height(250.dp)  // Height also reduced for a square shape
                        .border(1.dp, Color.White, RoundedCornerShape(12.dp))  // White border
                        .clip(RoundedCornerShape(12.dp))  // Ensure the shape matches exactly
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Challenges with friends",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }

                // Space of 2cm (75.6dp) between Challenges with friends box and Challenge History button
                Spacer(modifier = Modifier.height(75.6.dp))

                // Challenge History Button (blue background, reduced corner radius for more rectangular shape)
                Button(
                    onClick = { /* Navigate to Challenge History */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF05A9FB)),  // Blue button background
                    modifier = Modifier
                        .width(250.dp)  // Reduced width for the button
                        .height(65.dp)  // Reduced height
                        .clip(RoundedCornerShape(4.dp))  // Reduced corner radius for more rectangular shape
                ) {
                    Text(
                        "Challenge History",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp, // Reduced text size
                        color = Color.DarkGray  // Text in dark gray
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Bottom blue line moved 2cm lower
                Spacer(modifier = Modifier.height(75.6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color(0xFF05A9FB))
                )
            }
        }
    )
}
