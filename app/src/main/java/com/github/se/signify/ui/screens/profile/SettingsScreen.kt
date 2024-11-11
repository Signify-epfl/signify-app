package com.github.se.signify.ui.screens.profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.navigation.NavigationActions
import java.io.File
import java.io.FileOutputStream

@Composable
fun SettingsScreen(
    navigationActions: NavigationActions,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {
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

  Column(
      modifier =
          Modifier.fillMaxSize()
              .verticalScroll(rememberScrollState())
              .background(MaterialTheme.colorScheme.background)
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
                      Modifier.border(
                              2.dp,
                              MaterialTheme.colorScheme.outlineVariant,
                              RoundedCornerShape(8.dp))
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
                UserProfilePicture(selectedImageUrl)
              }
            }

        // Editable Username
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
              Row(
                  modifier =
                      Modifier.border(
                              2.dp,
                              MaterialTheme.colorScheme.outlineVariant,
                              RoundedCornerShape(8.dp))
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

        // Other Settings Section
        Box(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
            contentAlignment = Alignment.Center) {
              Text(
                  text = "Other settings:\nLanguage,\n theme, ...",
                  fontSize = 16.sp,
                  color = MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Normal)
            }

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

@Composable
fun UserProfilePicture(profilePictureUri: String?) {
  if (profilePictureUri != null) {
    AsyncImage(
        model = Uri.parse(profilePictureUri),
        contentDescription = "Profile Picture",
        modifier =
            Modifier.size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .testTag("profilePictureTag"),
        contentScale = ContentScale.Crop) // Crop the image to fit within the bounds
  } else {
    // Default placeholder for no image
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "Default Profile Picture",
        modifier = Modifier.size(120.dp).testTag("default_profile_picture"))
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
