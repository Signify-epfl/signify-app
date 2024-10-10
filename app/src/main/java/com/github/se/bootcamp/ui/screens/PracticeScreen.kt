package com.github.se.bootcamp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.bootcamp.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PracticeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray) // Background color
    ) {
        // Help button in the top-right corner
        HelpButton(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center, // Center buttons vertically
            horizontalAlignment = Alignment.CenterHorizontally // Center buttons horizontally
        ) {
            DifficultyButton(text = "Easy", onClick = { /* Handle Easy click */ })
            Spacer(modifier = Modifier.height(24.dp)) // Space between the buttons
            DifficultyButton(text = "Hard", onClick = { /* Handle Hard click */ })
        }
        FlameIcon(modifier = Modifier.align(Alignment.TopStart).padding(16.dp))

    }
}
@Composable
fun DifficultyButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF05A9FB),  // Background color using hex value
            contentColor = Color.White           // Text color
        ),
        shape = RoundedCornerShape(16.dp), // Rounded corners
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
fun HelpButton(modifier: Modifier) {
    IconButton(
        onClick = { /* Handle help button click */ },
        modifier = modifier
    ) {
        Icon(
            // CHANGE THIS TO ? ICON
            painter = painterResource(id = R.drawable.flame),
            contentDescription = "Help",
            tint = Color.Blue
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