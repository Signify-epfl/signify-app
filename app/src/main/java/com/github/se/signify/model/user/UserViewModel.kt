package com.github.se.signify.model.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.se.signify.model.challenge.Challenge
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class UserViewModel(private val repository: UserRepository) : ViewModel() {

  private val _friends = MutableStateFlow<List<String>>(emptyList())
  val friends: StateFlow<List<String>> = _friends

  private val _friendsRequests = MutableStateFlow<List<String>>(emptyList())
  val friendsRequests: StateFlow<List<String>> = _friendsRequests

  private val _userName = MutableStateFlow("unknown")
  val userName: StateFlow<String> = _userName

  private val _profilePictureUrl = MutableStateFlow<String?>(null)
  val profilePictureUrl: StateFlow<String?> = _profilePictureUrl

  private val _searchResult = MutableStateFlow<User?>(null)
  val searchResult: StateFlow<User?> = _searchResult

  private val _ongoingChallenges = MutableStateFlow<List<Challenge>>(emptyList())
  val ongoingChallenges: StateFlow<List<Challenge>> = _ongoingChallenges

  private val logTag = "UserViewModel"

  init {
    repository.init {}
  }

  // create factory
  companion object {
    val Factory: ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
          @Suppress("UNCHECKED_CAST")
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(UserRepositoryFireStore(Firebase.firestore)) as T
          }
        }
  }

  fun getFriendsList(currentUserId: String) {
    repository.getFriendsList(
        currentUserId,
        onSuccess = { friendsList -> _friends.value = friendsList },
        onFailure = { e -> Log.e(logTag, "Failed to get friends list: ${e.message}}") })
  }

  fun getRequestsFriendsList(currentUserId: String) {
    repository.getRequestsFriendsList(
        currentUserId,
        onSuccess = { requestsList -> _friendsRequests.value = requestsList },
        onFailure = { e -> Log.e(logTag, "Failed to get requests list: ${e.message}}") })
  }

  // Function to search for a user by their ID
  fun getUserById(userId: String) {
    repository.getUserById(
        userId,
        onSuccess = { user -> setSearchResult(user) },
        onFailure = { e ->
          setSearchResult(null)
          Log.e(logTag, "Failed to get userId: ${e.message}}")
        })
  }

  fun setSearchResult(user: User?) {
    _searchResult.value = user
  }

  fun getUserName(currentUserId: String) {
    repository.getUserName(
        currentUserId,
        onSuccess = { userName -> _userName.value = userName },
        onFailure = { e -> Log.e(logTag, "Failed to get user name: ${e.message}}") })
  }

  fun updateUserName(currentUserId: String, newName: String) {
    repository.updateUserName(
        currentUserId,
        newName,
        onSuccess = { Log.d(logTag, "User name updated successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to update user name: ${e.message}") })
  }

  fun getProfilePictureUrl(currentUserId: String) {
    repository.getProfilePictureUrl(
        currentUserId,
        onSuccess = { profilePictureUrl -> _profilePictureUrl.value = profilePictureUrl },
        onFailure = { e -> Log.e(logTag, "Failed to get profile picture: ${e.message}}") })
  }

  fun updateProfilePictureUrl(currentUserId: String, newProfilePictureUrl: String?) {
    repository.updateProfilePictureUrl(
        currentUserId,
        newProfilePictureUrl,
        onSuccess = { Log.d(logTag, "Profile picture updated successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to update profile picture: ${e.message}") })
  }

  fun sendFriendRequest(currentUserId: String, targetUserId: String) {
    repository.sendFriendRequest(
        currentUserId,
        targetUserId,
        onSuccess = { Log.d(logTag, "Request sent successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to send request: ${e.message}") })
  }

  fun acceptFriendRequest(currentUserId: String, friendUserId: String) {
    repository.acceptFriendRequest(
        currentUserId,
        friendUserId,
        onSuccess = { Log.d(logTag, "Friend request accepted successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to accept friend request: ${e.message}") })
  }

  fun declineFriendRequest(currentUserId: String, friendUserId: String) {
    repository.declineFriendRequest(
        currentUserId,
        friendUserId,
        onSuccess = { Log.d(logTag, "Friend request declined successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to decline friend request: ${e.message}") })
  }

  fun removeFriend(currentUserId: String, friendUserId: String) {
    repository.removeFriend(
        currentUserId,
        friendUserId,
        onSuccess = { Log.d(logTag, "Friend removed successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to remove friend: ${e.message}") })
  }

  fun addOngoingChallenge(userId: String, challengeId: String) {
    repository.addOngoingChallenge(
        userId,
        challengeId,
        onSuccess = { Log.d(logTag, "Challenge added to ongoing successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to add challenge to ongoing: ${e.message}") })
  }

  fun getOngoingChallenges(userId: String) {
    repository.getOngoingChallenges(
        userId,
        onSuccess = { challenges -> _ongoingChallenges.value = challenges },
        onFailure = { e ->
          Log.e("UserViewModel", "Failed to fetch ongoing challenges: ${e.message}")
        })
  }

  fun removeOngoingChallenge(userId: String, challengeId: String) {
    repository.removeOngoingChallenge(
        userId,
        challengeId,
        onSuccess = {
          // Update ongoingChallenges after removal
          _ongoingChallenges.value =
              _ongoingChallenges.value.filter { it.challengeId != challengeId }
        },
        onFailure = { e ->
          Log.e("UserViewModel", "Failed to remove challenge from ongoing: ${e.message}")
        })
  }
}
