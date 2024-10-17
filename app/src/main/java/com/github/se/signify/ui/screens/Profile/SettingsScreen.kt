package com.github.se.signify.ui.screens.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun SettingsScreen(profilePictureUrl: String?, navigationActions: NavigationActions) {
  var username by remember { mutableStateOf("Test Name 1") }
  var showEditOptions by remember { mutableStateOf(false) }

  Column(
      modifier =
          Modifier.fillMaxSize()
              // .background(Color.DarkGray)
              .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(64.dp)) {

        // Back Button
        Box(
            modifier =
                Modifier.fillMaxWidth() // Occupe toute la largeur disponible
                    .align(Alignment.Start) // Aligne uniquement l'icône à gauche
            ) {
              IconButton(onClick = { navigationActions.goBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF05A9FB))
              }
            }
        // Editable Username

        Row(
            modifier =
                Modifier.border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFDCF1FA))
                    .clickable { showEditOptions = true }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // Centrer le contenu dans la Row
            ) {
              Icon(
                  imageVector = Icons.Outlined.Edit,
                  contentDescription = "Edit Username",
                  tint = Color.Black)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                  text = username,
                  fontSize = 20.sp,
                  color = Color.Black,
                  fontWeight = FontWeight.Bold)
            }

        // Editable Profile Picture

        Row(
            modifier =
                Modifier.border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFDCF1FA))
                    .clickable { showEditOptions = true }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
              imageVector = Icons.Outlined.Edit,
              contentDescription = "Edit Profile Picture",
              tint = Color.Black)
          Spacer(modifier = Modifier.width(8.dp))
          ProfilePicture(profilePictureUrl)
        }

        // Other Settings Section
        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
                    .padding(16.dp),
            contentAlignment = Alignment.Center) {
              Text(
                  text = "Other settings:\nLanguage,\n theme, ...",
                  fontSize = 16.sp,
                  color = Color.Black,
                  fontWeight = FontWeight.Normal)
            }

        // Cancel and Save Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
          Button(
              onClick = { /* TODO: Implement cancel action */},
              colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD32F2F)),
              modifier = Modifier.weight(1f)) {
                Text(text = "Cancel", color = Color.White)
              }
          Button(
              onClick = { /* TODO: Implement save action */},
              colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2E7D32)),
              modifier = Modifier.weight(1f)) {
                Text(text = "Save", color = Color.White)
              }
        }
      }
}
