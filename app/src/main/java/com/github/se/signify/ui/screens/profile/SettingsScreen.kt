package com.github.se.signify.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.ProfilePicture
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun SettingsScreen(
    navigationActions: NavigationActions,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {

  var newName by remember { mutableStateOf("") }
  LaunchedEffect(Unit) { userViewModel.getUserName(currentUserId) }
  val userName = userViewModel.userName.collectAsState()

  Column(
      modifier =
          Modifier.fillMaxSize()
              .padding(16.dp)
              .verticalScroll(rememberScrollState())
              .testTag("SettingsScreen"),
      verticalArrangement = Arrangement.spacedBy(64.dp)) {

        // Back Button
        BackButton { navigationActions.goBack() }

        // Editable Profile Picture
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
              Row(
                  modifier =
                      Modifier.border(2.dp, colorResource(R.color.blue), RoundedCornerShape(8.dp))
                          .clip(RoundedCornerShape(8.dp))
                          .background(colorResource(R.color.white))
                          .padding(horizontal = 24.dp, vertical = 16.dp),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Profile Picture",
                    tint = colorResource(R.color.black))
                Spacer(modifier = Modifier.width(8.dp))
                ProfilePicture(null)
              }
            }

        // Editable Username
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
              Row(
                  modifier =
                      Modifier.border(2.dp, colorResource(R.color.blue), RoundedCornerShape(8.dp))
                          .clip(RoundedCornerShape(8.dp))
                          .background(colorResource(R.color.white))
                          .padding(horizontal = 24.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit Username",
                        tint = colorResource(R.color.black))

                    Spacer(modifier = Modifier.width(8.dp))

                    TextField(
                        value = newName,
                        onValueChange = { newName = it },
                        placeholder = {
                          Text(userName.value, color = colorResource(R.color.dark_gray))
                        },
                        modifier =
                            Modifier.widthIn(max = 200.dp)
                                .padding(vertical = 8.dp)
                                .background(colorResource(R.color.white)),
                        colors =
                            TextFieldDefaults.colors(
                                focusedContainerColor = colorResource(R.color.white),
                                unfocusedContainerColor = colorResource(R.color.white),
                                focusedTextColor = colorResource(R.color.dark_gray),
                                cursorColor = colorResource(R.color.dark_gray)))
                  }
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
                  color = colorResource(R.color.white),
                  fontWeight = FontWeight.Normal)
            }

        // Cancel and Save Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
          ActionButtons(
              { newName = "" }, colorResource(R.color.dark_gray), "Cancel", Modifier.weight(1f))

          ActionButtons(
              {
                if (newName.isNotBlank()) {
                  userViewModel.updateUserName(currentUserId, newName)
                }
              },
              colorResource(R.color.blue),
              "Save",
              Modifier.weight(1f))
        }
      }
}

@Composable
fun ActionButtons(onClickAction: () -> Unit, color: Color, text: String, modifier: Modifier) {
  Button(
      onClick = onClickAction,
      colors = ButtonDefaults.buttonColors().copy(color),
      modifier = modifier) {
        Text(text = text, color = colorResource(R.color.white), fontWeight = FontWeight.Bold)
      }
}
