package com.github.se.signify.ui.screens.profile

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.ProfilePicture
import kotlinx.coroutines.delay

@Composable
fun FriendsListScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository,
    userViewModel: UserViewModel =
        viewModel(factory = UserViewModel.factory(userSession, userRepository))
) {

  val friendsString = stringResource(R.string.friends_text)
  val friendsRequestsString = stringResource(R.string.friends_requests_text)

  var errorMessage by remember { mutableStateOf("") }
  var selectedList by remember { mutableStateOf(friendsString) }

  AnnexScreenScaffold(
      navigationActions = navigationActions,
      testTag = "FriendsListScreen",
  ) {
    LaunchedEffect(Unit) {
      userViewModel.getFriendsList()
      userViewModel.getRequestsFriendsList()
    }

    Spacer(modifier = Modifier.height(32.dp))

    val friends = userViewModel.friends.collectAsState()
    val friendsRequests = userViewModel.friendsRequests.collectAsState()
    val searchResult = userViewModel.searchResult.collectAsState()
    val errorState = userViewModel.errorState.collectAsState()
    val userNotFoundText = stringResource(R.string.user_not_found_text)
    SearchBar { searchQuery ->
      if (searchQuery.isNotEmpty()) {
        try {
          userViewModel.getUserById(searchQuery)
        } catch (e: Exception) {
          errorMessage = userNotFoundText
        }
      }
    }

    errorMessage.let { message ->
      if (errorState.value != null) {
        errorMessage = userNotFoundText
      }
      if (errorMessage.isNotEmpty()) {
        ErrorMessage(userNotFoundText)
        // Dismiss the message
        LaunchedEffect(message) {
          delay(3000)
          errorMessage = ""
          userViewModel.clearErrorState()
        }
      }
    }

    if (searchResult.value != null) {
      Dialog(onDismissRequest = { userViewModel.setSearchResult(null) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth().padding(16.dp)) {
              Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.Center,
                  modifier = Modifier.fillMaxWidth().padding(16.dp)) {

                    //  Display the user's profile picture
                    ProfilePicture(searchResult.value!!.profileImageUrl)

                    // Display the user's name
                    Text(
                        text = searchResult.value!!.name.toString(),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (searchResult.value!!.uid == userSession.getUserId()) {
                      MyProfileButton(userViewModel, navigationActions)
                    } else if (friends.value.contains(searchResult.value!!.uid)) {
                      RemoveFriendButton(userViewModel)
                    } else {
                      AddFriendButton(userViewModel)
                    }

                    // Close button
                    val closeText = stringResource(R.string.close_text)
                    TextButton(onClick = { userViewModel.setSearchResult(null) }) {
                      Text(closeText, color = MaterialTheme.colorScheme.onSurface)
                    }
                  }
            }
      }
    }

    // Separation line
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        modifier = Modifier.padding(top = 24.dp, bottom = 4.dp))

    // Buttons for toggling lists
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {
          val numberFriends =
              if (friends.value.size > 1) friendsString else stringResource(R.string.friends_text)

          TextButton(
              { selectedList = friendsString },
              "${friends.value.size} $numberFriends",
              ButtonDefaults.buttonColors(
                  containerColor =
                      if (selectedList == friendsString) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)),
              "${friendsString}Button")

          val numberRequests =
              if (friendsRequests.value.size > 1) friendsRequestsString
              else stringResource(R.string.friends_text)

          TextButton(
              { selectedList = friendsRequestsString },
              "${friendsRequests.value.size} $numberRequests",
              ButtonDefaults.buttonColors(
                  containerColor =
                      if (selectedList == friendsRequestsString) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)),
              "${friendsRequestsString}Button")
        }

    // Separation line
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp))

    // Display the corresponding list based on the selected button
    when (selectedList) {

      // case Friends list
      friendsString -> {
        FriendListCard(
            title = stringResource(R.string.my_friends_text),
            items = friends.value,
            emptyMessage = stringResource(R.string.no_friend_text)) { friendName ->
              FriendItem(friendName, userViewModel)
            }
      }

      // case Friends Requests list

      friendsRequestsString -> {
        val requestFriendsText = stringResource(R.string.request_friends_text)
        val noNewFriendText = stringResource(R.string.no_new_friend_text)
        FriendListCard(
            title = requestFriendsText,
            items = friendsRequests.value,
            emptyMessage = noNewFriendText) { friendName ->
              FriendRequestItem(friendName, userViewModel)
            }
      }
    }
  }
}

@Composable
fun AddFriendButton(userViewModel: UserViewModel) {
  val context = LocalContext.current
  val requestSentText = stringResource(R.string.request_sent_text)
  Button(
      onClick = {
        userViewModel.sendFriendRequest(userViewModel.searchResult.value!!.uid)
        userViewModel.setSearchResult(null)
        Toast.makeText(context, requestSentText, Toast.LENGTH_SHORT).show()
      },
      colors =
          ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary),
      modifier = Modifier.fillMaxWidth()) {
        val addFriendText = stringResource(R.string.add_friend_text)
        Text(addFriendText)
      }
}

@SuppressLint("UnrememberedMutableState", "StateFlowValueCalledInComposition")
@Composable
fun RemoveFriendButton(userViewModel: UserViewModel) {
  val showDialog = mutableStateOf(false) // State to control dialog visibility

  Button(
      onClick = { showDialog.value = true }, // Show the dialog when button is clicked
      colors =
          ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.error,
              contentColor = MaterialTheme.colorScheme.onError),
      modifier = Modifier.fillMaxWidth()) {
        val removeFriendText = stringResource(R.string.remove_friend_text)
        Text(removeFriendText)
      }

  ConfirmationDialog(showDialog) {
    userViewModel.removeFriend(userViewModel.searchResult.value!!.uid)
    userViewModel.setSearchResult(null)
  }
}

@Composable
fun MyProfileButton(userViewModel: UserViewModel, navigationActions: NavigationActions) {
  Button(
      onClick = {
        navigationActions.navigateTo(Screen.PROFILE)
        userViewModel.setSearchResult(null)
      },
      colors =
          ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary),
      modifier = Modifier.fillMaxWidth()) {
        val myProfileText = stringResource(R.string.my_profile_text)
        Text(myProfileText)
      }
}

@Composable
fun ErrorMessage(message: String) {
  Text(
      text = message,
      color = MaterialTheme.colorScheme.error,
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      fontWeight = FontWeight.Bold)
}

@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
) {
  var searchQuery by remember { mutableStateOf("") }
  val searchByUserIdText = stringResource(R.string.search_by_user_ID_text)
  TextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      shape = RoundedCornerShape(16.dp),
      modifier =
          Modifier.fillMaxWidth()
              .padding(start = 30.dp, end = 30.dp)
              .border(
                  BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
                  RoundedCornerShape(16.dp))
              .testTag("SearchBar"),
      placeholder = { Text(searchByUserIdText, color = MaterialTheme.colorScheme.onBackground) },
      colors =
          TextFieldDefaults.colors(
              focusedContainerColor = MaterialTheme.colorScheme.background,
              unfocusedContainerColor = MaterialTheme.colorScheme.background,
              focusedTextColor = MaterialTheme.colorScheme.onBackground,
              cursorColor = MaterialTheme.colorScheme.onBackground),
      singleLine = true,
      trailingIcon = {
        ActionButton(
            {
              onSearch(searchQuery)
              searchQuery = ""
            },
            Icons.Default.Search,
            MaterialTheme.colorScheme.background,
            "Search")
      })
}

@Composable
fun FriendListCard(
    title: String,
    items: List<String>,
    emptyMessage: String = stringResource(R.string.no_item_available_text),
    content: @Composable (String) -> Unit
) {
  Card(
      modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
      shape = RoundedCornerShape(16.dp),
      border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
  ) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
              text = title,
              color = MaterialTheme.colorScheme.onPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              modifier = Modifier.padding(bottom = 4.dp).align(Alignment.CenterHorizontally))

          if (items.isEmpty()) {
            Text(
                text = emptyMessage,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp).align(Alignment.CenterHorizontally))
          } else {
            LazyColumn(modifier = Modifier.height(450.dp).testTag(title)) {
              items(items.size) { index -> content(items[index]) }
            }
          }
        }
  }
}

@Composable
fun FriendCard(name: String, actions: @Composable RowScope.() -> Unit) {
  Card(
      shape = RoundedCornerShape(50.dp),
      modifier =
          Modifier.fillMaxWidth()
              .padding(8.dp)
              .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(50.dp)),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
      elevation = CardDefaults.cardElevation(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
              UserTextName(name) // Name of the friend
              actions()
            }
      }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun FriendItem(friendName: String, userViewModel: UserViewModel) {

  val showDialog = mutableStateOf(false) // State to control dialog visibility

  FriendCard(friendName) {
    val removeText = stringResource(R.string.remove_text)
    // Button "Remove"
    ActionButton(
        { showDialog.value = true },
        Icons.Default.Delete,
        MaterialTheme.colorScheme.error,
        removeText)

    ConfirmationDialog(showDialog) { userViewModel.removeFriend(friendName) }
  }
}

@Composable
fun FriendRequestItem(friendRequestName: String, userViewModel: UserViewModel) {

  FriendCard(friendRequestName) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically) {

          // Button "Accept"
          ActionButton(
              { userViewModel.acceptFriendRequest(friendRequestName) },
              Icons.Default.AddCircle,
              MaterialTheme.colorScheme.primary,
              stringResource(R.string.accept_text))

          // Button "Decline"
          ActionButton(
              { userViewModel.declineFriendRequest(friendRequestName) },
              Icons.Default.Delete,
              MaterialTheme.colorScheme.error,
              stringResource(R.string.decline_text))
        }
  }
}

@Composable
fun ActionButton(
    onClickAction: () -> Unit,
    icon: ImageVector,
    backGroundColor: Color,
    contentDescription: String
) {
  IconButton(
      onClick = onClickAction,
      modifier = Modifier.background(color = backGroundColor, CircleShape)) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onBackground,
        )
      }
}

@Composable
fun UserTextName(name: String) {
  Text(
      text = name,
      color = MaterialTheme.colorScheme.onBackground,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(start = 16.dp))
}

@Composable
fun TextButton(onClickAction: () -> Unit, text: String, color: ButtonColors, testTag: String) {
  Button(onClick = onClickAction, colors = color, modifier = Modifier.testTag(testTag)) {
    Text(text = text)
  }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ConfirmationDialog(showDialog: MutableState<Boolean>, onClickAction: () -> Unit) {
  if (showDialog.value) {
    val context = LocalContext.current

    Dialog(onDismissRequest = { showDialog.value = false }) {
      Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.surface,
          modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                  val confirmingRemovalFriendText =
                      stringResource(R.string.confirm_friend_removal_text)
                  Text(
                      text = confirmingRemovalFriendText,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface,
                      modifier = Modifier.padding(bottom = 16.dp))
                  Row(
                      horizontalArrangement = Arrangement.SpaceEvenly,
                      modifier = Modifier.fillMaxWidth()) {
                        val removedFriendText = stringResource(R.string.removed_friend_text)
                        val yesText = stringResource(R.string.yes_button_text)
                        val noText = stringResource(R.string.no_button_text)
                        Button(
                            onClick = {
                              onClickAction()
                              Toast.makeText(context, removedFriendText, Toast.LENGTH_SHORT).show()
                              showDialog.value = false // Close the dialog
                            },
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                )) {
                              Text(yesText)
                            }
                        Button(
                            onClick = { showDialog.value = false }, // Close the dialog
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface,
                                )) {
                              Text(noText)
                            }
                      }
                }
          }
    }
  }
}
