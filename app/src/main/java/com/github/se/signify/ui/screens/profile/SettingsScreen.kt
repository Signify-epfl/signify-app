package com.github.se.signify.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.BasicButton
import com.github.se.signify.ui.common.ProfilePicture
import kotlinx.coroutines.launch

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

  val focusManager = LocalFocusManager.current
  Box(
      modifier =
          Modifier.fillMaxSize()
              .clickable(
                  onClick = { focusManager.clearFocus() },
                  indication = null,
                  interactionSource = remember { MutableInteractionSource() })) {
        AnnexScreenScaffold(
            navigationActions = navigationActions,
            testTag = "SettingsScreen",
            topBarButtons = listOf { LogoutButton(userSession, navigationActions) }) {
              Spacer(modifier = Modifier.height(32.dp))

              Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween) {
                    EditableUsernameField(userViewModel, userName.value, Modifier.weight(1f))

                    Spacer(modifier = Modifier.width(16.dp))

                    EditableProfilePictureField(
                        userViewModel, profilePictureUrl, Modifier.weight(1f))
                  }

              Spacer(modifier = Modifier.height(64.dp))

              Row(
                  modifier = Modifier.fillMaxWidth().clickable { onThemeChange(!isDarkTheme) },
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween) {
                    val modeText =
                        if (isDarkTheme) stringResource(R.string.dark_mode_text)
                        else stringResource(R.string.light_mode_text)
                    Text(
                        text = modeText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground)
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onThemeChange(it) },
                        modifier = Modifier.testTag("SwitchTag"))
                  }

              Spacer(modifier = Modifier.height(16.dp))

              // Switch between English and French
              LanguageSwitch(isFrench, onLanguageChange)
            }
      }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun EditableProfilePictureField(
    userViewModel: UserViewModel,
    profilePictureUrl: State<String?>,
    modifier: Modifier = Modifier,
) {
  val showDeleteDialog = remember { mutableStateOf(false) }
  val showEditDialog = remember { mutableStateOf(false) }
  var selectedUri by remember { mutableStateOf<Uri?>(null) }

  fun isDeleteEnabled(): Boolean {
    return selectedUri != null
  }

  LaunchedEffect(profilePictureUrl.value) { selectedUri = profilePictureUrl.value?.toUri() }

  val galleryLauncher =
      rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
          selectedUri = uri
          showEditDialog.value = true
        }
      }
  Row(
      modifier = modifier.background(MaterialTheme.colorScheme.background),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
  ) {
    Column {
      BasicButton(
          onClick = { galleryLauncher.launch("image/*") },
          iconTestTag = "editProfilePictureButtonIcon",
          contentDescription = "Edit Profile Picture",
          modifier = Modifier.testTag("editProfilePictureButton"),
          icon = Icons.Outlined.Edit,
          tint = MaterialTheme.colorScheme.onBackground,
      )

      Spacer(modifier = Modifier.height(16.dp))

      if (isDeleteEnabled()) {
        BasicButton(
            onClick = { showDeleteDialog.value = true },
            iconTestTag = "deleteProfilePictureButtonIcon",
            contentDescription = "Delete Profile Picture",
            modifier = Modifier.testTag("deleteProfilePictureButton"),
            icon = Icons.Outlined.Delete,
            tint = MaterialTheme.colorScheme.onBackground,
        )
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    ProfilePicture(selectedUri?.toString())

    ConfirmationDialog(
        showDeleteDialog,
        onConfirm = {
          selectedUri = null
          userViewModel.updateProfilePictureUrl(null)
        },
        onDismiss = {},
        title = stringResource(R.string.confirm_changes_title),
        message = stringResource(R.string.confirm_changes_message),
        confirmColor = MaterialTheme.colorScheme.error,
    )

    ConfirmationDialog(
        showEditDialog,
        onConfirm = { userViewModel.updateProfilePictureUrl(selectedUri) },
        onDismiss = { selectedUri = profilePictureUrl.value?.toUri() },
        title = stringResource(R.string.confirm_changes_title),
        message = stringResource(R.string.confirm_changes_message),
    )
  }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun EditableUsernameField(
    userViewModel: UserViewModel,
    userName: String,
    modifier: Modifier = Modifier,
) {
  val showConfirmationDialog = remember { mutableStateOf(false) }
  var newName by remember { mutableStateOf("") }

  Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
          val focusRequester = remember { FocusRequester() }

          BasicButton(
              onClick = { focusRequester.requestFocus() },
              iconTestTag = "editUsernameButtonIcon",
              contentDescription = "Edit Username",
              modifier = Modifier.testTag("editUsernameButton"),
              icon = Icons.Outlined.Edit,
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
                        if (!focusState.isFocused && newName != userName && newName.isNotEmpty()) {
                          showConfirmationDialog.value = true
                        }
                      }
                      .focusRequester(focusRequester)
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
  }
  ConfirmationDialog(
      showConfirmationDialog,
      onConfirm = { userViewModel.updateUserName(newName) },
      onDismiss = { newName = "" },
      title = stringResource(R.string.confirm_changes_title),
      message = stringResource(R.string.confirm_changes_message))
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun ConfirmationDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    message: String,
    confirmColor: Color = MaterialTheme.colorScheme.primary,
) {
  if (showDialog.value) {

    Dialog(
        onDismissRequest = {
          onDismiss()
          showDialog.value = false
        }) {
          Surface(
              shape = RoundedCornerShape(16.dp),
              color = MaterialTheme.colorScheme.surface,
              modifier =
                  Modifier.widthIn(min = 280.dp, max = 400.dp) // Set the dialog width
                      .padding(top = 150.dp)
                      .testTag("confirmationPopup")) {
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
                                modifier = Modifier.testTag("confirmationPopupConfirm"),
                                onClick = {
                                  onConfirm()
                                  showDialog.value = false // Close the dialog
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = confirmColor),
                            ) {
                              Text(confirmText)
                            }

                            // Cancel Button
                            Button(
                                modifier = Modifier.testTag("confirmationPopupCancel"),
                                onClick = {
                                  onDismiss()
                                  showDialog.value = false // Close the dialog
                                },
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.onSurface),
                            ) {
                              Text(cancelText)
                            }
                          }
                    }
              }
        }
  }
}

/**
 * A composable function that provides a toggle to switch between French (FR) and English (EN).
 *
 * @param isFrench A Boolean value representing the current language setting. `true` indicates
 *   French, and `false` indicates English.
 * @param onLanguageChange A callback function invoked when the language switch is toggled. The
 *   callback receives a Boolean value indicating the new language state.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun LanguageSwitch(isFrench: Boolean, onLanguageChange: (Boolean) -> Unit) {
  var expanded by remember { mutableStateOf(false) }
  var selectedLanguage by remember { mutableStateOf(if (isFrench) "FR" else "EN") }

  Column {
    Row(
        modifier =
            Modifier.testTag("LanguageRow")
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
          Text(
              text = selectedLanguage,
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onBackground)

          Icon(
              imageVector =
                  if (expanded) Icons.Filled.KeyboardArrowDown
                  else Icons.AutoMirrored.Filled.KeyboardArrowRight,
              contentDescription = "Expand language menu",
              tint = MaterialTheme.colorScheme.onBackground)
        }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth()) {
          DropdownMenuItem(
              text = { Text(stringResource(R.string.english_text)) },
              onClick = {
                selectedLanguage = "EN"
                onLanguageChange(false)
                expanded = false
              })
          DropdownMenuItem(
              text = { Text(stringResource(R.string.french_text)) },
              onClick = {
                selectedLanguage = "FR"
                onLanguageChange(true)
                expanded = false
              })
        }
  }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun LogoutButton(userSession: UserSession, navigationActions: NavigationActions) {

  // State to manage the visibility of the confirmation dialog
  val showLogoutDialog = remember { mutableStateOf(false) }

  // Logout Button UI
  BasicButton(
      onClick = { showLogoutDialog.value = true }, // Show confirmation dialog on click
      iconTestTag = "logoutButtonIcon",
      contentDescription = "Logout",
      modifier = Modifier.testTag("logoutButton"),
      icon = Icons.AutoMirrored.Outlined.ExitToApp,
      tint = MaterialTheme.colorScheme.error,
  )

  // Confirmation Dialog for Logout
  ConfirmationDialog(
      showDialog = showLogoutDialog,
      onConfirm = {
        // Perform logout and navigate to login/welcome screen
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
          userSession.logout()
          navigationActions.navigateTo(Screen.AUTH)
        }
      },
      onDismiss = { showLogoutDialog.value = false }, // Close dialog without action
      title = stringResource(id = R.string.confirm_logout_title),
      message = stringResource(id = R.string.confirm_logout_message),
      confirmColor = MaterialTheme.colorScheme.error,
  )
}
