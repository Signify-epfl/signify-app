package com.github.se.signify.model.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.se.signify.model.challenge.Challenge
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
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

  private val _unlockedQuests = MutableStateFlow("1")
  val unlockedQuests: StateFlow<String> = _unlockedQuests

  private val _streak = MutableStateFlow(0L)
  val streak: StateFlow<Long> = _streak

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

  private fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date()) // Returns current date as "YYYY-MM-DD"
  }

  // Calculate the number of days between two dates
  private fun calculateDaysBetween(startDate: String, endDate: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val start = dateFormat.parse(startDate)
    val end = dateFormat.parse(endDate)
    val diff = end!!.time - start!!.time
    return TimeUnit.MILLISECONDS.toDays(diff)
  }

  // Check if a new quest should be unlocked based on the initial access date
  fun checkAndUnlockNextQuest(currentUserId: String) {
    repository.getInitialQuestAccessDate(
        currentUserId,
        onSuccess = { initialAccessDate ->
          val currentDate = getCurrentDate()

          if (initialAccessDate == null) {
            // First time access, so set the initial access date
            repository.setInitialQuestAccessDate(
                currentUserId,
                date = currentDate,
                onSuccess = { Log.d("UserViewModel", "Initial access date set.") },
                onFailure = { e ->
                  Log.e("UserViewModel", "Failed to set initial access date: ${e.message}")
                })
            _unlockedQuests.value = "1" // Only the first quest is unlocked initially
          } else {
            // Calculate the number of days passed since the initial access date
            val daysSinceInitialAccess = calculateDaysBetween(initialAccessDate, currentDate)

            // Update the unlocked quests count based on days passed
            val newUnlockedQuests =
                (daysSinceInitialAccess + 1).toInt() // +1 to count the first day
            if (newUnlockedQuests > _unlockedQuests.value.toInt()) {
              _unlockedQuests.value = newUnlockedQuests.toString()
              Log.d("UserViewModel", "Unlocked quests updated to $newUnlockedQuests")
            }
          }
        },
        onFailure = { e ->
          Log.e("UserViewModel", "Failed to get initial quest access date: ${e.message}")
        })
  }

  fun updateStreak(currentUserId: String) {
    repository.updateStreak(
        currentUserId,
        onSuccess = { Log.d(logTag, "Streak update successful!") },
        onFailure = { e -> Log.e(logTag, "Error updating streak: ${e.message}") })
  }

  fun getStreak(currentUserId: String) {
    repository.getStreak(
        currentUserId,
        onSuccess = { s -> _streak.value = s },
        onFailure = { e -> Log.e(logTag, "Failed to get user's streak: ${e.message}}") })
  }
}
