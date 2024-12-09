package com.github.se.signify.ui.common

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * An image of a profile picture. If the URL is null, a default icon is displayed.
 *
 * @param profilePictureUrl A string for the URL of the picture.
 */
@Composable
fun ProfilePicture(profilePictureUrl: String?) {
  if (profilePictureUrl != null) {
    AsyncImage(
        model = Uri.parse(profilePictureUrl),
        contentDescription = "Profile Picture",
        modifier =
            Modifier.size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .testTag("ProfilePicture"),
        contentScale = ContentScale.Crop) // Crop the image to fit within the bounds
  } else {
    // Default placeholder for no image
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "Default Profile Picture",
        modifier = Modifier.size(120.dp).testTag("DefaultProfilePicture"),
        tint = MaterialTheme.colorScheme.onBackground)
  }
}

/**
 * A recap of a user's account information.
 *
 * @param userId A string for the user id.
 * @param userName A string for the personalized user name.
 * @param profilePictureUrl A string for the profile picture URL.
 * @param streak The user's streak in days.
 */
@Composable
fun AccountInformation(userId: String, userName: String, profilePictureUrl: String?, streak: Long) {
  Row(
      modifier = Modifier.fillMaxWidth().testTag("UserInfo"),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically) {
        // User Info : user id and user name
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Text(
              text = userId,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground,
              modifier = Modifier.testTag("UserId"))
          Text(
              text = userName,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground,
              modifier = Modifier.testTag("UserName"))
        }

        // Profile Picture
        ProfilePicture(profilePictureUrl)

        // Number of days
        StreakCounter(streak)
      }
}
