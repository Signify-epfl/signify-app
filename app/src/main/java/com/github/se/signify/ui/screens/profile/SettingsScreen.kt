package com.github.se.signify.ui.screens.profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.NotImplementedYet
import com.github.se.signify.ui.ProfilePicture
import com.github.se.signify.ui.navigation.NavigationActions
import java.io.File
import java.io.FileOutputStream

@Composable
fun SettingsScreen(
    navigationActions: NavigationActions,
    userRepository: UserRepository,
) {
  val userViewModel: UserViewModel = viewModel(factory = UserViewModel.factory(userRepository))

  val context = LocalContext.current

  LaunchedEffect(Unit) {
    userViewModel.getUserName(currentUserId)
    userViewModel.getProfilePictureUrl(currentUserId)
  }

  val userName = userViewModel.userName.collectAsState()
  val profilePictureUrl = userViewModel.profilePictureUrl.collectAsState()

  var newName by remember { mutableStateOf("") }
  var selectedImageUrl by remember { mutableStateOf(profilePictureUrl.value) }
  LaunchedEffect(profilePictureUrl.value) { selectedImageUrl = profilePictureUrl.value }

  val galleryLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
          val file = uriToFile(context, uri)
          if (file != null) {
            selectedImageUrl = file.absolutePath
          }
        }
      }

  AnnexScreenScaffold(navigationActions = navigationActions, testTagColumn = "SettingsScreen") {
    // Editable Profile Picture
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
      Row(
          modifier =
              Modifier.border(
                      2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                  .clip(RoundedCornerShape(8.dp))
                  .background(MaterialTheme.colorScheme.background)
                  .padding(horizontal = 24.dp, vertical = 16.dp),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Icon(
              modifier = Modifier.clickable { galleryLauncher.launch("image/*") },
              imageVector = Icons.Outlined.Edit,
              contentDescription = "Edit Profile Picture",
              tint = MaterialTheme.colorScheme.onBackground,
          )

          Spacer(modifier = Modifier.height(16.dp))

          Icon(
              modifier = Modifier.clickable { selectedImageUrl = null },
              imageVector = Icons.Outlined.Delete,
              contentDescription = "Delete Profile Picture",
              tint = MaterialTheme.colorScheme.onBackground,
          )
        }

        Spacer(modifier = Modifier.width(8.dp))
        ProfilePicture(selectedImageUrl)
      }
    }
    Spacer(modifier = Modifier.height(32.dp))

    // Editable Username
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
      Row(
          modifier =
              Modifier.border(
                      2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                  .clip(RoundedCornerShape(8.dp))
                  .background(MaterialTheme.colorScheme.background)
                  .padding(horizontal = 24.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit Username",
                tint = MaterialTheme.colorScheme.onBackground)

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = newName,
                onValueChange = { newName = it },
                placeholder = {
                  Text(userName.value, color = MaterialTheme.colorScheme.onBackground)
                },
                modifier =
                    Modifier.widthIn(max = 200.dp)
                        .padding(vertical = 8.dp)
                        .background(MaterialTheme.colorScheme.background),
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        cursorColor = MaterialTheme.colorScheme.onBackground))
          }
    }
    Spacer(modifier = Modifier.height(32.dp))

    // Other Settings Section
    NotImplementedYet(testTag = "OtherSettings", text = "Other settings:\nLanguage,\nTheme,\n...")
    Spacer(modifier = Modifier.height(32.dp))

    // Cancel and Save Buttons
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      ActionButtons(
          {
            newName = ""
            selectedImageUrl = profilePictureUrl.value
          },
          MaterialTheme.colorScheme.error,
          "Cancel",
          Modifier.weight(1f))

      ActionButtons(
          {
            if (newName.isNotBlank()) {
              userViewModel.updateUserName(currentUserId, newName)
            }
            userViewModel.updateProfilePictureUrl(currentUserId, selectedImageUrl)
          },
          MaterialTheme.colorScheme.primary,
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
        Text(
            text = text, color = MaterialTheme.colorScheme.background, fontWeight = FontWeight.Bold)
      }
}

fun uriToFile(context: Context, uri: Uri): File? {
  val contentResolver = context.contentResolver
  val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")

  try {
    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val outputStream = FileOutputStream(file)

    inputStream.use { input -> outputStream.use { output -> input.copyTo(output) } }
    return file
  } catch (e: Exception) {
    e.printStackTrace()
  }
  return null
}
