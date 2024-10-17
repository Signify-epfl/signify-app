package com.github.se.signify.ui.screens.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun SettingsScreen(profilePictureUrl: String?, navigationActions: NavigationActions) {
  var username by remember { mutableStateOf("Test Name 1") }
  var showEditOptions by remember { mutableStateOf(false) }

  Column(
      modifier = Modifier.fillMaxSize().padding(16.dp),
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
                    tint = colorResource(R.color.blue))
              }
            }
        // Editable Username

        Row(
            modifier =
                Modifier.border(2.dp, colorResource(R.color.black), RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(R.color.white))
                    .clickable { showEditOptions = true }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // Centrer le contenu dans la Row
            ) {
              Icon(
                  imageVector = Icons.Outlined.Edit,
                  contentDescription = "Edit Username",
                  tint = colorResource(R.color.black))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                  text = username,
                  fontSize = 20.sp,
                  color = colorResource(R.color.black),
                  fontWeight = FontWeight.Bold)
            }

        // Editable Profile Picture

        Row(
            modifier =
                Modifier.border(2.dp, colorResource(R.color.black), RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorResource(R.color.white))
                    .clickable { showEditOptions = true }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
              imageVector = Icons.Outlined.Edit,
              contentDescription = "Edit Profile Picture",
              tint = colorResource(R.color.black))
          Spacer(modifier = Modifier.width(8.dp))
          ProfilePicture(profilePictureUrl)
        }

        // Other Settings Section
        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorResource(R.color.dark_gray))
                    .padding(16.dp),
            contentAlignment = Alignment.Center) {
              Text(
                  text = "Other settings:\nLanguage,\n theme, ...",
                  fontSize = 16.sp,
                  color = colorResource(R.color.black),
                  fontWeight = FontWeight.Normal)
            }

        // Cancel and Save Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
          Button(
              onClick = { /* TODO: Implement cancel action */},
              colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.red)),
              modifier = Modifier.weight(1f)) {
                Text(text = "Cancel", color = colorResource(R.color.white))
              }
          Button(
              onClick = { /* TODO: Implement save action */},
              colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.green)),
              modifier = Modifier.weight(1f)) {
                Text(text = "Save", color = colorResource(R.color.white))
              }
        }
      }
}
