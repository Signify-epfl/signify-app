package com.github.se.signify.model.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _friends = MutableStateFlow<List<String>>(emptyList())
    val friends: StateFlow<List<String>> = _friends

    private val _friendsRequests = MutableStateFlow<List<String>>(emptyList())
    val friendsRequests: StateFlow<List<String>> = _friendsRequests

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
            onSuccess = {},
            onFailure = { e -> Log.e(logTag, "Failed to get userId: ${e.message}}") })
    }

    fun sendFriendRequest(currentUserId: String, targetUserId: String) {
        repository.sendFriendRequest(
            currentUserId,
            targetUserId,
            onSuccess = { Log.d(logTag, "Request sent successfully.") },
            onFailure = { e -> Log.e(logTag, "Failed to send request: ${e.message}") })
    }

    fun acceptFriendRequest(currentUserId: String, targetUserId: String) {
        repository.acceptFriendRequest(
            currentUserId,
            targetUserId,
            onSuccess = { Log.d(logTag, "Friend request accepted successfully.") },
            onFailure = { e -> Log.e(logTag, "Failed to accept friend request: ${e.message}") })
    }

    fun declineFriendRequest(currentUserId: String, targetUserId: String) {
        repository.declineFriendRequest(
            currentUserId,
            targetUserId,
            onSuccess = { Log.d(logTag, "Friend request declined successfully.") },
            onFailure = { e -> Log.e(logTag, "Failed to decline friend request: ${e.message}") })
    }

    fun removeFriend(currentUserId: String, targetUserId: String) {
        repository.removeFriend(
            currentUserId,
            targetUserId,
            onSuccess = { Log.d(logTag, "Friend removed successfully.") },
            onFailure = { e -> Log.e(logTag, "Failed to remove friend: ${e.message}") })
    }
}
