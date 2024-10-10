package com.github.se.bootcamp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.bootcamp.ui.navigation.NavigationActions

@Composable
fun ExerciseScreen(navigationActions : NavigationActions, difficulty : DifficultyLevel, word : String) {

    val context = LocalContext.current

    // MutableState to keep track of the current letter
    var currentLetterIndex by rememberSaveable { mutableStateOf(0) }

    // Get the current letter based on the index
    val currentLetter = word[currentLetterIndex].toString()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF333333)),

        contentAlignment = Alignment.Center // Center all content in the Box
    ) {

        IconButton(
            onClick = { navigationActions.goBack() },
            modifier = Modifier
                .padding(16.dp) // Add some padding to position it away from the edges
                .align(Alignment.TopStart) // Align the button to the top-left corner
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White // Customize icon color if needed
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center content in the Column as well
        ) {

            // Display instructions based on difficulty level
            if (difficulty == DifficultyLevel.EASY) {

                val imageName = "letter_${currentLetter.lowercase()}"
                val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)

                // Display the image if the resource exists
                if (imageResId != 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( horizontal = 16.dp)
                            .height(200.dp)
                            .background(Color(0xFF05A9FB), shape = RoundedCornerShape(16.dp))
                            .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = "Sign image",
                            modifier = Modifier
                                .size(120.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                } else {
                    Text("Image for letter $currentLetter not found.")
                }

            }

            // Display the current word and letter

            val wordDisplay = buildString {

                append(word.substring(0, currentLetterIndex).lowercase())

                append(" ${word[currentLetterIndex].uppercase()} ")

                append(word.substring(currentLetterIndex + 1).lowercase())
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
                    .background(Color(0xFF05A9FB), shape = RoundedCornerShape(16.dp))
                    .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    wordDisplay,
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 24.sp // Set the font size to 24sp
                    )
                )
                //Text("Current Letter: $currentLetter",color = Color.White)
            }

            // CAMERA BOX

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(350.dp)
                    .background(Color.Black, shape = RoundedCornerShape(16.dp))
                    .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                //Text("Current Letter: $currentLetter",color = Color.White)
            }

        }

        // REMOVE THIS WHEN CAMERA SUCCESS

        Box(
            modifier = Modifier
                .fillMaxSize().padding(30.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    onSuccess(
                        currentLetterIndex = currentLetterIndex,
                        word = word,
                        onNextLetter = { nextIndex -> currentLetterIndex = nextIndex },
                        onWordComplete = {
                            Toast.makeText(context, "Word Completed!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .width(150.dp)  // Adjust the width of the button
                    .height(50.dp)  // Adjust the height of the button
            ) {
                Text(text = "Success")
            }

        }


    }
}
fun onSuccess(
    currentLetterIndex: Int,
    word: String,
    onNextLetter: (Int) -> Unit,
    onWordComplete: () -> Unit
) {
    if (currentLetterIndex < word.length - 1) {
        // Move to the next letter
        onNextLetter(currentLetterIndex + 1)
    } else {
        // If at the end of the word, display success
        onWordComplete()
    }
}

enum class DifficultyLevel {
    EASY, HARD
}