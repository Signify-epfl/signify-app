package com.github.se.signify.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.navigation.NavigationActions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

val currentUserId = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: "unknown"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsListScreen(
    navigationActions: NavigationActions,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {
  var searchQuery by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf("") }

  Column(modifier = Modifier.fillMaxSize().padding(16.dp).testTag("FriendsListScreen")) {
    LaunchedEffect(Unit) {
      userViewModel.getFriendsList(currentUserId)
      userViewModel.getRequestsFriendsList(currentUserId)
    }

    val friends = userViewModel.friends.collectAsState()
    val friendsRequests = userViewModel.friendsRequests.collectAsState()
    val searchResult = userViewModel.searchResult.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
      // Back Button
      BackButton { navigationActions.goBack() }

      Spacer(modifier = Modifier.height(16.dp))

      // Search Bar
      TextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          shape = RoundedCornerShape(16.dp),
          modifier =
              Modifier.fillMaxWidth()
                  .padding(start = 40.dp, end = 40.dp)
                  .border(
                      BorderStroke(2.dp, colorResource(R.color.dark_gray)),
                      RoundedCornerShape(16.dp))
                  .testTag("SearchBar"),
          placeholder = { Text("Search by user ID", color = colorResource(R.color.white)) },
          colors =
              TextFieldDefaults.textFieldColors(
                  containerColor = colorResource(R.color.blue),
                  focusedTextColor = colorResource(R.color.white),
                  cursorColor = colorResource(R.color.dark_gray)),
          singleLine = true,
          trailingIcon = {
            IconButton(
                onClick = {
                  if (searchQuery.isNotEmpty()) {
                    try {
                      userViewModel.getUserById(searchQuery)
                      if (searchResult.value == null) {
                        errorMessage = "User not found"
                      }
                    } catch (e: Exception) {
                      errorMessage = "Error : ${e.message}"
                    }
                  }
                },
                modifier = Modifier.testTag("OnSearchButton")) {
                  Icon(
                      imageVector = Icons.Default.Search,
                      contentDescription = "Search",
                      tint = colorResource(R.color.white),
                  )
                }
          })

      errorMessage.let { message ->
        Text(
            text = message,
            color = colorResource(R.color.red),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            fontWeight = FontWeight.Bold)
        // Dismiss the message
        LaunchedEffect(message) {
          delay(3000)
          errorMessage = ""
        }
      }

      if (searchResult.value != null && searchResult.value!!.uid != currentUserId) {
        Dialog(onDismissRequest = { userViewModel.setSearchResult(null) }) {
          Surface(
              shape = RoundedCornerShape(16.dp),
              color = colorResource(R.color.white),
              modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)) {

                      // TODO: add the profile picture

                      // Display the user's name
                      Text(
                          text = searchResult.value!!.name.toString(), fontWeight = FontWeight.Bold)
                      Spacer(modifier = Modifier.height(8.dp))

                      // Check if the users are friends
                      if (friends.value.contains(searchResult.value!!.uid)) {
                        // Remove Friend button
                        Button(
                            onClick = {
                              userViewModel.removeFriend(currentUserId, searchResult.value!!.uid)
                              userViewModel.setSearchResult(null)
                            },
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.blue),
                                    contentColor = colorResource(R.color.dark_gray)),
                            modifier = Modifier.fillMaxWidth()) {
                              Text("Remove Friend")
                            }
                      } else {
                        // Add Friend button
                        Button(
                            onClick = {
                              userViewModel.sendFriendRequest(
                                  currentUserId, searchResult.value!!.uid)
                              userViewModel.setSearchResult(null)
                            },
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.blue),
                                    contentColor = colorResource(R.color.dark_gray)),
                            modifier = Modifier.fillMaxWidth()) {
                              Text("Add Friend")
                            }
                      }

                      // Close button
                      TextButton(onClick = { userViewModel.setSearchResult(null) }) {
                        Text("Close", color = colorResource(R.color.dark_gray))
                      }
                    }
              }
        }
      }

      Spacer(modifier = Modifier.height(32.dp))

      // My Friends List
      FriendListCard(
          title = "My friends list", items = friends.value, emptyMessage = "You have no friends") {
              friendName ->
            FriendItem(friendName = friendName, userViewModel = userViewModel)
          }

      Spacer(modifier = Modifier.height(32.dp))

      // New Friends Demands
      FriendListCard(
          title = "New friends demands",
          items = friendsRequests.value,
          emptyMessage = "No new friend requests") { friendRequestName ->
            FriendRequestItem(friendRequestName = friendRequestName, userViewModel = userViewModel)
          }
    }
  }
}

@Composable
fun FriendListCard(
    title: String,
    items: List<String>,
    emptyMessage: String = "No items available",
    content: @Composable (String) -> Unit
) {
  Card(
      modifier = Modifier.fillMaxWidth().padding(start = 40.dp, end = 40.dp),
      colors = CardDefaults.cardColors(containerColor = colorResource(R.color.blue)),
      shape = RoundedCornerShape(16.dp),
      border = BorderStroke(2.dp, colorResource(R.color.dark_gray)),
  ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
              text = title,
              color = colorResource(R.color.dark_gray),
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              modifier = Modifier.padding(bottom = 4.dp).align(Alignment.CenterHorizontally))

          if (items.isEmpty()) {
            Text(
                text = emptyMessage,
                color = colorResource(R.color.white),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp).align(Alignment.CenterHorizontally))
          } else {
            LazyColumn(modifier = Modifier.height(200.dp).testTag(title)) {
              items(items.size) { index -> content(items[index]) }
            }
          }
        }
  }
}

@Composable
fun FriendItem(
    friendName: String,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {
  Card(
      shape = RoundedCornerShape(50.dp),
      modifier =
          Modifier.fillMaxWidth()
              .padding(8.dp)
              .border(2.dp, colorResource(R.color.dark_gray), RoundedCornerShape(50.dp)),
      colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
      elevation = CardDefaults.cardElevation(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
              // Name of the friend
              Text(
                  text = friendName,
                  color = colorResource(R.color.dark_gray),
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(start = 16.dp))

              // Button "Remove"
              Button(
                  onClick = { userViewModel.removeFriend(currentUserId, friendName) },
                  colors = ButtonDefaults.buttonColors(colorResource(R.color.dark_gray)),
                  shape = RoundedCornerShape(50.dp),
              ) {
                Text(text = "Remove", color = colorResource(R.color.white))
              }
            }
      }
}

@Composable
fun FriendRequestItem(
    friendRequestName: String,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
) {
  Card(
      shape = RoundedCornerShape(50.dp),
      modifier =
          Modifier.fillMaxWidth()
              .padding(8.dp)
              .border(2.dp, colorResource(R.color.dark_gray), RoundedCornerShape(50.dp)),
      colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
      elevation = CardDefaults.cardElevation(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
              // Name of the friend
              Text(
                  text = friendRequestName,
                  color = colorResource(R.color.dark_gray),
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(start = 16.dp))

              Row(
                  horizontalArrangement = Arrangement.spacedBy(16.dp),
                  verticalAlignment = Alignment.CenterVertically) {

                    // Button "Accept"
                    IconButton(
                        onClick = {
                          userViewModel.acceptFriendRequest(currentUserId, friendRequestName)
                        },
                        modifier = Modifier.background(colorResource(R.color.blue), CircleShape)) {
                          Icon(
                              imageVector = Icons.Default.AddCircle,
                              contentDescription = "Accept",
                              tint = colorResource(R.color.white),
                          )
                        }

                    // Button "Decline"
                    IconButton(
                        onClick = {
                          userViewModel.declineFriendRequest(currentUserId, friendRequestName)
                        },
                        modifier =
                            Modifier.background(colorResource(R.color.dark_gray), CircleShape)) {
                          Icon(
                              imageVector = Icons.Default.Delete,
                              contentDescription = "Decline",
                              tint = colorResource(R.color.white),
                          )
                        }
                  }
            }
      }
}
