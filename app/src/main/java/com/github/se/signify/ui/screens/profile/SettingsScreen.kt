package com.github.se.signify.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.NotImplementedYet
import com.github.se.signify.ui.common.ProfilePicture
import com.github.se.signify.ui.theme.Surface

@Composable
fun SettingsScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    isFrench: Boolean,
    onLanguageChange: (Boolean) -> Unit
) {
  val userViewModel: UserViewModel =
      viewModel(factory = UserViewModel.factory(userSession, userRepository))

  LaunchedEffect(Unit) {
    userViewModel.getUserName()
    userViewModel.getProfilePictureUrl()
  }

  val userName = userViewModel.userName.collectAsState()
  val profilePictureUrl = userViewModel.profilePictureUrl.collectAsState()

  var selectedImageUrl by remember { mutableStateOf(profilePictureUrl.value) }

  LaunchedEffect(profilePictureUrl.value) { selectedImageUrl = profilePictureUrl.value }

  val focusManager = LocalFocusManager.current
  Box(
      modifier =
          Modifier.fillMaxSize()
              .clickable(
                  onClick = { focusManager.clearFocus() },
                  indication = null,
                  interactionSource = remember { MutableInteractionSource() })) {
        AnnexScreenScaffold(navigationActions = navigationActions, testTag = "SettingsScreen") {
          Spacer(modifier = Modifier.height(32.dp))

          Row(
              modifier =
                  Modifier.fillMaxWidth().padding(16.dp).clickable { onThemeChange(!isDarkTheme) },
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween) {
                val modeText =
                    if (isDarkTheme) stringResource(R.string.dark_mode_text)
                    else stringResource(R.string.light_mode_text)
                Text(
                    text = modeText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground)
                Switch(checked = isDarkTheme, onCheckedChange = { onThemeChange(it) })
              }
          // Switch between English and French
          LanguageSwitch(isFrench, onLanguageChange)

          EditableProfilePicture(
              userViewModel = userViewModel,
              selectedImageUrl = selectedImageUrl,
          )

          Spacer(modifier = Modifier.height(32.dp))

          EditableUsernameField(userViewModel, userName.value)

          Spacer(modifier = Modifier.height(32.dp))

          // Other Settings Section To be removed after all settings are completed !
          NotImplementedYet(
              testTag = "OtherSettings", text = "Other settings:\nLanguage,\nTheme,\n...")
          Spacer(modifier = Modifier.height(32.dp))
        }
      }
}

@Composable
fun EditableProfilePicture(
    userViewModel: UserViewModel,
    selectedImageUrl: String?,
) {

  var showDeleteDialog = remember { mutableStateOf(false) }
  var showEditDialog = remember { mutableStateOf(false) }
  var selectedUri by remember { mutableStateOf<Uri?>(null) }

  val galleryLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
          selectedUri = uri
          showEditDialog.value = true
        }
      }

  Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    Row(
        modifier =
            Modifier.border(
                    2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
          Column {
            Icon(
                modifier = Modifier.clickable { galleryLauncher.launch("image/*") },
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit Profile Picture",
                tint = MaterialTheme.colorScheme.onBackground)

            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                modifier = Modifier.clickable { showDeleteDialog.value = true },
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete Profile Picture",
                tint = MaterialTheme.colorScheme.onBackground)
          }

          Spacer(modifier = Modifier.width(8.dp))

          ProfilePicture(selectedImageUrl)
        }
  }

  ConfirmationDialog(
      showDeleteDialog,
      onConfirm = { userViewModel.updateProfilePictureUrl(null) },
      onDismiss = {},
      title = stringResource(R.string.confirm_changes_title),
      message = stringResource(R.string.confirm_changes_message))

  ConfirmationDialog(
      showEditDialog,
      onConfirm = {
        userViewModel.updateProfilePictureUrl(selectedUri)
        selectedUri = null
      },
      onDismiss = { selectedUri = null },
      title = stringResource(R.string.confirm_changes_title),
      message = stringResource(R.string.confirm_changes_message))
}

@Composable
fun EditableUsernameField(userViewModel: UserViewModel, userName: String) {
  var showConfirmationDialog = remember { mutableStateOf(false) }
  var newName by remember { mutableStateOf("") }
  var hasInitialized by remember { mutableStateOf(false) }

  Column(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally) {
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
                  placeholder = { Text(userName, color = MaterialTheme.colorScheme.onBackground) },
                  modifier =
                      Modifier.widthIn(max = 200.dp)
                          .padding(vertical = 8.dp)
                          .background(MaterialTheme.colorScheme.background)
                          .onFocusChanged { focusState ->
                            if (hasInitialized &&
                                !focusState.isFocused &&
                                newName != userName &&
                                newName.isNotEmpty()) {
                              showConfirmationDialog.value = true
                            }
                            if (!hasInitialized) hasInitialized = true
                          }
                          .testTag("usernameTextField"),
                  colors =
                      TextFieldDefaults.colors(
                          focusedContainerColor = MaterialTheme.colorScheme.background,
                          unfocusedContainerColor = MaterialTheme.colorScheme.background,
                          focusedTextColor = MaterialTheme.colorScheme.onBackground,
                          cursorColor = MaterialTheme.colorScheme.onBackground),
                  keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                  keyboardActions =
                      KeyboardActions(
                          onDone = {
                            if (newName != userName && newName.isNotEmpty()) {
                              showConfirmationDialog.value = true
                            }
                          }),
                  singleLine = true)
            }

        ConfirmationDialog(
            showConfirmationDialog,
            onConfirm = { userViewModel.updateUserName(newName) },
            onDismiss = { newName = userName },
            title = stringResource(R.string.confirm_changes_title),
            message = stringResource(R.string.confirm_changes_message))
      }
}

@Composable
fun ConfirmationDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    message: String
) {
  if (showDialog.value) {

    Dialog(onDismissRequest = { showDialog.value = false }) {
      Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.surface,
          modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                  // Dialog Title
                  Text(
                      text = title,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface,
                      modifier = Modifier.padding(bottom = 16.dp))

                  // Dialog Message
                  Text(
                      text = message,
                      color = MaterialTheme.colorScheme.onSurface,
                      modifier = Modifier.padding(bottom = 16.dp))

                  // Buttons Row
                  Row(
                      horizontalArrangement = Arrangement.SpaceEvenly,
                      modifier = Modifier.fillMaxWidth()) {
                        val confirmText = stringResource(R.string.confirm)
                        val cancelText = stringResource(R.string.cancel)

                        // Confirm Button
                        Button(
                            onClick = {
                              onConfirm()
                              showDialog.value = false // Close the dialog
                            },
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary)) {
                              Text(confirmText)
                            }

                        // Cancel Button
                        Button(
                            onClick = {
                              onDismiss()
                              showDialog.value = false // Close the dialog
                            },
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError)) {
                              Text(cancelText)
                            }
                      }
                }
          }
    }
  }
}

@Composable
fun LanguageSwitch(isFrench: Boolean, onLanguageChange: (Boolean) -> Unit) {
  Row(
      modifier =
          Modifier.fillMaxWidth()
              .padding(16.dp)
              .clickable { onLanguageChange(!isFrench) }
              .testTag("LanguageSwitch"),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = if (isFrench) "FR" else "EN",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground)
        Switch(checked = isFrench, onCheckedChange = { onLanguageChange(it) })
      }
}
