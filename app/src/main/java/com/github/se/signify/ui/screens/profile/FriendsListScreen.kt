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
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.ProfilePicture
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Route
import kotlinx.coroutines.delay

@Composable
fun FriendsListScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository,
    userViewModel: UserViewModel =
        viewModel(factory = UserViewModel.factory(userSession, userRepository))
) {

  val friendsString = stringResource(R.string.friends)
  val friendsRequestsString = stringResource(R.string.friends_requests)

  var errorMessage by remember { mutableStateOf("") }
  var selectedList by remember { mutableStateOf(friendsString) }

  AnnexScreenScaffold(navigationActions = navigationActions, testTagColumn = "FriendsListScreen") {
    LaunchedEffect(Unit) {
      userViewModel.getFriendsList()
      userViewModel.getRequestsFriendsList()
    }

    val friends = userViewModel.friends.collectAsState()
    val friendsRequests = userViewModel.friendsRequests.collectAsState()
    val searchResult = userViewModel.searchResult.collectAsState()
    val errorState = userViewModel.errorState.collectAsState()

    SearchBar { searchQuery ->
      if (searchQuery.isNotEmpty()) {
        try {
          userViewModel.getUserById(searchQuery)
        } catch (e: Exception) {
          errorMessage = "Error : ${e.message}"
        }
      }
    }

    errorMessage.let { message ->
      if (errorState.value != null) {
        errorMessage = errorState.value!!
      }
      if (errorMessage.isNotEmpty()) {
        ErrorMessage(message)
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
                    TextButton(onClick = { userViewModel.setSearchResult(null) }) {
                      Text("Close", color = MaterialTheme.colorScheme.onSurface)
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
          val numberFriends = if (friends.value.size > 1) friendsString else "Friend"

          TextButton(
              { selectedList = friendsString },
              "${friends.value.size} $numberFriends",
              ButtonDefaults.buttonColors(
                  containerColor =
                      if (selectedList == friendsString) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)),
              "${friendsString}Button")

          val numberRequests =
              if (friendsRequests.value.size > 1) friendsRequestsString else "Request"

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
      friendsString ->
          FriendListCard(
              title = "My $friendsString list",
              items = friends.value,
              emptyMessage = "You have no $friendsString") { friendName ->
                FriendItem(friendName, userViewModel)
              }

      // case Friends Requests list
      friendsRequestsString ->
          FriendListCard(
              title = "New $friendsString $friendsRequestsString",
              items = friendsRequests.value,
              emptyMessage = "No new friend $friendsRequestsString") { friendName ->
                FriendRequestItem(friendName, userViewModel)
              }
    }
  }
}

@Composable
fun AddFriendButton(userViewModel: UserViewModel) {
  val context = LocalContext.current
  Button(
      onClick = {
        userViewModel.sendFriendRequest(userViewModel.searchResult.value!!.uid)
        userViewModel.setSearchResult(null)
        Toast.makeText(context, "Request sent.", Toast.LENGTH_SHORT).show()
      },
      colors =
          ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary),
      modifier = Modifier.fillMaxWidth()) {
        Text("Add Friend")
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
        Text("Remove Friend")
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
        navigationActions.navigateTo(Route.PROFILE)
        userViewModel.setSearchResult(null)
      },
      colors =
          ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary),
      modifier = Modifier.fillMaxWidth()) {
        Text("My Profile")
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
      placeholder = { Text("Search by user ID", color = MaterialTheme.colorScheme.onBackground) },
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
    emptyMessage: String = "No items available",
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

    // Button "Remove"
    ActionButton(
        { showDialog.value = true },
        Icons.Default.Delete,
        MaterialTheme.colorScheme.error,
        "Remove")

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
              "Accept")

          // Button "Decline"
          ActionButton(
              { userViewModel.declineFriendRequest(friendRequestName) },
              Icons.Default.Delete,
              MaterialTheme.colorScheme.error,
              "Decline")
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
                  Text(
                      text = "Are you sure you want to remove this friend?",
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface,
                      modifier = Modifier.padding(bottom = 16.dp))
                  Row(
                      horizontalArrangement = Arrangement.SpaceEvenly,
                      modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                              onClickAction()
                              Toast.makeText(context, "Friend removed.", Toast.LENGTH_SHORT).show()
                              showDialog.value = false // Close the dialog
                            },
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError)) {
                              Text("Yes")
                            }
                        Button(
                            onClick = { showDialog.value = false }, // Close the dialog
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary)) {
                              Text("No")
                            }
                      }
                }
          }
    }
  }
}
