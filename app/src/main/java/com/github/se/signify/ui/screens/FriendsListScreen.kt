package com.github.se.signify.ui.screens

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun FriendsListScreen(
    currentFriends: List<String>,
    friendRequests: List<String>,
    onRemoveFriend: (String) -> Unit,
    onAcceptFriendRequest: (String) -> Unit,
    onRejectFriendRequest: (String) -> Unit,
    onSearchUser: (String) -> Unit,
    navigationActions: NavigationActions
) {
  var searchQuery by remember { mutableStateOf("") }

  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    // Back Button
    IconButton(onClick = { navigationActions.goBack() }) {
      Icon(
          imageVector = Icons.Default.ArrowBack,
          contentDescription = "Back",
          tint = Color(0xFF05A9FB))
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Search Bar
    TextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier =
            Modifier.fillMaxWidth()
                .border(BorderStroke(2.dp, Color(0xFF05A9FB)), RoundedCornerShape(16.dp)),
        placeholder = { Text("Search with user ID") },
        colors =
            TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.Black,
                textColor = Color.Black),
        singleLine = true,
        trailingIcon = {
          IconButton(onClick = { onSearchUser(searchQuery) }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF05A9FB))
          }
        })

    Spacer(modifier = Modifier.height(24.dp))

    // My Friends List
    Text(
        text = "My friends list",
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(bottom = 8.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFF05A9FB)),
        backgroundColor = Color(0xFFDCF1FA)) {
          Column(modifier = Modifier.padding(16.dp)) {
            currentFriends.forEach { friend -> FriendItem(friend, onRemoveFriend) }
          }
        }

    Spacer(modifier = Modifier.height(24.dp))

    // New Friends Demands
    Text(
        text = "New friends demand",
        color = Color.Black,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(bottom = 8.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFF05A9FB)),
        backgroundColor = Color(0xFFDCF1FA)) {
          Column(modifier = Modifier.padding(16.dp)) {
            friendRequests.forEach { friendRequest ->
              FriendRequestItem(friendRequest, onAcceptFriendRequest, onRejectFriendRequest)
            }
          }
        }
  }
}

@Composable
fun FriendItem(friendName: String, onRemoveFriend: (String) -> Unit) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically) {
        Text(text = friendName, color = Color.Black, modifier = Modifier.weight(1f))
        Button(
            onClick = { onRemoveFriend(friendName) },
            colors = ButtonDefaults.buttonColors(Color(0xFFD32F2F))) {
              Text(text = "Remove", color = Color.White)
            }
      }
}

@Composable
fun FriendRequestItem(
    friendRequestName: String,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit
) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically) {
        Text(text = friendRequestName, color = Color.Black, modifier = Modifier.weight(1f))
        IconButton(onClick = { onAccept(friendRequestName) }) {
          Icon(
              imageVector = Icons.Default.AddCircle,
              contentDescription = "Accept",
              tint = Color(0xFF2E7D32))
        }
        IconButton(onClick = { onReject(friendRequestName) }) {
          Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Reject",
              tint = Color(0xFFD32F2F))
        }
      }
}
