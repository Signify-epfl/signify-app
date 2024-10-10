package com.github.se.bootcamp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.bootcamp.R
import com.github.se.bootcamp.ui.navigation.BottomNavigationMenu
import com.github.se.bootcamp.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.bootcamp.ui.navigation.NavigationActions


@Composable
fun PracticeScreen(navigationActions: NavigationActions) {

    var showHelp by remember { mutableStateOf(false) }


    Scaffold(
        bottomBar = {
            BottomNavigationMenu(
                onTabSelect = { route -> navigationActions.navigateTo(route) },
                tabList = LIST_TOP_LEVEL_DESTINATION,
                selectedItem = navigationActions.currentRoute()
            )
        },
        content = { pd ->
            Text(
                modifier = Modifier.padding(pd),
                text = "Practice Screen"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
            ) {
                // Help button in the top-right corner
                HelpButton(modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                    onClick = {
                        showHelp = true
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center, // Center buttons vertically
                    horizontalAlignment = Alignment.CenterHorizontally // Center buttons horizontally
                ) {
                    DifficultyButton(text = "Easy", onClick =
                    {
                        val word = "FGINSY" // You can modify this dynamically
                        navigationActions.navigateToExerciseScreen(
                            DifficultyLevel.EASY.toString(),
                            word
                        )
                    })

                    Spacer(modifier = Modifier.height(24.dp))

                    DifficultyButton(text = "Hard", onClick = {
                        val word = "FGINSY" // You can modify this dynamically
                        navigationActions.navigateToExerciseScreen(
                            DifficultyLevel.HARD.toString(),
                            word
                        )
                    })
                }

                FlameIcon(modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp))

                if (showHelp) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xAA000000)) // Semi-transparent background
                            .padding(16.dp)
                            .clickable(
                                enabled = true,
                                onClick = {}, // Block other clicks
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    append("Here, you can put your ASL knowledge to the test with fun and interactive exercises. ")
                                    append("\n\n")

                                    append("You will find two levels of difficulty to challenge yourself: ")
                                    append("\n\n")

                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Green
                                        )
                                    ) {
                                        append("• Easy mode: ")
                                    }
                                    append("You will have the support of sign demos to guide you as you practice.")
                                    append("\n\n")

                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Red
                                        )
                                    ) {
                                        append("• Hard mode: ")
                                    }
                                    append("No sign demos, you have to rely on your memory to recall the ASL signs you have learned.")
                                },
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Close button to hide the help text
                            TextButton(onClick = { showHelp = false }) {
                                Text("Close")
                            }
                        }
                    }
                }

            }
        }
    )
}

@Composable
fun DifficultyButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF05A9FB),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(300.dp)
            .height(200.dp)
            .shadow(20.dp, RoundedCornerShape(16.dp))
            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
    ) {
        Text(text = text, fontSize = 30.sp, color = Color.White)
    }

}

@Composable
fun HelpButton(modifier: Modifier, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = "?",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun FlameIcon(modifier: Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.flame),
        contentDescription = "Flame",
        tint = Color.Red,
        modifier = modifier.size(24.dp)
    )
}
