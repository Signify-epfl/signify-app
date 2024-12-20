package com.github.se.signify.model.common.user

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.ChallengeId
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class UserViewModel(
    private val userSession: UserSession,
    private val repository: UserRepository,
) : ViewModel() {

  private val _friends = MutableStateFlow<List<String>>(emptyList())
  val friends: StateFlow<List<String>> = _friends

  private val _friendsRequests = MutableStateFlow<List<String>>(emptyList())
  val friendsRequests: StateFlow<List<String>> = _friendsRequests

  private val _userName = MutableStateFlow(" ")
  val userName: StateFlow<String> = _userName

  private val _profilePictureUrl = MutableStateFlow<String?>(null)
  val profilePictureUrl: StateFlow<String?> = _profilePictureUrl

  private val _searchResult = MutableStateFlow<User?>(null)
  val searchResult: StateFlow<User?> = _searchResult

  private val _errorState = MutableStateFlow<String?>(null)
  val errorState: StateFlow<String?> = _errorState

  private val _ongoingChallengeIds = MutableStateFlow<List<ChallengeId>>(emptyList())
  val ongoingChallengeIds: StateFlow<List<ChallengeId>> = _ongoingChallengeIds

  private val _unlockedQuests = MutableStateFlow("1")
  val unlockedQuests: StateFlow<String> = _unlockedQuests

  private val _streak = MutableStateFlow(0L)
  val streak: StateFlow<Long> = _streak

  private val _pastChallenges = MutableStateFlow<List<ChallengeId>>(emptyList())
  val pastChallenges: StateFlow<List<ChallengeId>> = _pastChallenges

  private val _completedQuests = MutableStateFlow<List<String>>(emptyList())
  val completedQuests: StateFlow<List<String>> = _completedQuests

  private val logTag = "UserViewModel"

  init {
    repository.init {}
  }

  // create factory
  companion object {
    fun factory(userSession: UserSession, repository: UserRepository): ViewModelProvider.Factory {
      return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return UserViewModel(userSession, repository) as T
        }
      }
    }
  }

  fun getFriendsList() {
    repository.getFriendsList(
        userSession.getUserId()!!,
        onSuccess = { friendsList -> _friends.value = friendsList },
        onFailure = { e -> Log.e(logTag, "Failed to get friends list: ${e.message}}") })
  }

  fun getRequestsFriendsList() {
    repository.getRequestsFriendsList(
        userSession.getUserId()!!,
        onSuccess = { requestsList -> _friendsRequests.value = requestsList },
        onFailure = { e -> Log.e(logTag, "Failed to get requests list: ${e.message}}") })
  }

  // Function to search for a user by their ID
  fun getUserById(userId: String) {
    repository.getUserById(
        userId,
        onSuccess = { user ->
          setSearchResult(user)
          clearErrorState() // Clear the error state on success.
        },
        onFailure = { e ->
          setSearchResult(null)
          setErrorState("${e.message}") // Update error state.
          Log.e(logTag, "Failed to get userId: ${e.message}}")
        })
  }

  fun setSearchResult(user: User?) {
    _searchResult.value = user
  }

  fun setErrorState(e: String?) {
    _errorState.value = e
  }

  fun clearErrorState() {
    _errorState.value = null // Clear any previous error messages
  }

  fun getUserName() {
    repository.getUserName(
        userSession.getUserId()!!,
        onSuccess = { userName -> _userName.value = userName },
        onFailure = { e -> Log.e(logTag, "Failed to get user name: ${e.message}}") })
  }

  fun updateUserName(newName: String) {
    repository.updateUserName(
        userSession.getUserId()!!,
        newName,
        onSuccess = { Log.d(logTag, "User name updated successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to update user name: ${e.message}") })
  }

  fun getProfilePictureUrl() {
    repository.getProfilePictureUrl(
        userSession.getUserId()!!,
        onSuccess = { profilePictureUrl -> _profilePictureUrl.value = profilePictureUrl },
        onFailure = { e -> Log.e(logTag, "Failed to get profile picture: ${e.message}}") })
  }

  fun updateProfilePictureUrl(newProfilePictureUrl: Uri?) {
    repository.updateProfilePictureUrl(
        userSession.getUserId()!!,
        newProfilePictureUrl,
        onSuccess = { Log.d(logTag, "Profile picture updated successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to update profile picture: ${e.message}") })
  }

  fun sendFriendRequest(targetUserId: String) {
    repository.sendFriendRequest(
        userSession.getUserId()!!,
        targetUserId,
        onSuccess = { Log.d(logTag, "Request sent successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to send request: ${e.message}") })
  }

  fun acceptFriendRequest(friendUserId: String) {
    repository.acceptFriendRequest(
        userSession.getUserId()!!,
        friendUserId,
        onSuccess = {
          Log.d(logTag, "Friend request accepted successfully.")
          getFriendsList()
          getRequestsFriendsList()
        },
        onFailure = { e -> Log.e(logTag, "Failed to accept friend request: ${e.message}") })
  }

  fun declineFriendRequest(friendUserId: String) {
    repository.declineFriendRequest(
        userSession.getUserId()!!,
        friendUserId,
        onSuccess = {
          Log.d(logTag, "Friend request declined successfully.")
          getFriendsList()
          getRequestsFriendsList()
        },
        onFailure = { e -> Log.e(logTag, "Failed to decline friend request: ${e.message}") })
  }

  fun removeFriend(friendUserId: String) {
    repository.removeFriend(
        userSession.getUserId()!!,
        friendUserId,
        onSuccess = {
          Log.d(logTag, "Friend removed successfully.")
          getFriendsList()
        },
        onFailure = { e -> Log.e(logTag, "Failed to remove friend: ${e.message}") })
  }

  fun addOngoingChallenge(userId: String, challengeId: String) {
    repository.addOngoingChallenge(
        userId,
        challengeId,
        onSuccess = { Log.d(logTag, "Challenge added to ongoing successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to add challenge to ongoing: ${e.message}") })
  }

  fun getOngoingChallenges() {
    repository.getOngoingChallenges(
        userSession.getUserId()!!,
        onSuccess = { _ongoingChallengeIds.value = it },
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
          _ongoingChallengeIds.value = _ongoingChallengeIds.value.filter { it != challengeId }
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
  fun checkAndUnlockNextQuest() {
    repository.getInitialQuestAccessDate(
        userSession.getUserId()!!,
        onSuccess = { initialAccessDate ->
          val currentDate = getCurrentDate()

          if (initialAccessDate == null) {
            // First time access, so set the initial access date
            repository.setInitialQuestAccessDate(
                userSession.getUserId()!!,
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

  fun updateStreak() {
    repository.updateStreak(
        userSession.getUserId()!!,
        onSuccess = { Log.d(logTag, "Streak update successful!") },
        onFailure = { e -> Log.e(logTag, "Error updating streak: ${e.message}") })
  }

  fun getStreak() {
    repository.getStreak(
        userSession.getUserId()!!,
        onSuccess = { s -> _streak.value = s },
        onFailure = { e -> Log.e(logTag, "Failed to get user's streak: ${e.message}}") })
  }

  fun addPastChallenge(userId: String, challengeId: String) {
    repository.getUserById(
        userId = userId,
        onSuccess = { user ->
          // Update the pastChallenges list in Firestore
          repository.updateUserField(
              userId = userId,
              fieldName = "pastChallenges",
              value = user.pastChallenges + challengeId, // Add the new challengeId
              onSuccess = { Log.d("UserViewModel", "Challenge added to pastChallenges") },
              onFailure = { e ->
                Log.e("UserViewModel", "Failed to add past challenge: ${e.message}")
              })
        },
        onFailure = { e -> Log.e("UserViewModel", "Failed to fetch user: ${e.message}") })
  }

  fun incrementField(userId: String, fieldName: String) {
    repository.getUserById(
        userId = userId,
        onSuccess = { user ->
          val currentValue =
              when (fieldName) {
                "challengesCreated" -> user.challengesCreated
                "challengesCompleted" -> user.challengesCompleted
                "challengesWon" -> user.challengesWon
                else -> throw IllegalArgumentException("Invalid field name")
              }

          // Update the field in Firestore
          repository.updateUserField(
              userId = userId,
              fieldName = fieldName,
              value = (currentValue) + 1,
              onSuccess = { Log.d("UserViewModel", "Field $fieldName incremented successfully") },
              onFailure = { e ->
                Log.e("UserViewModel", "Failed to increment field: ${e.message}")
              })
        },
        onFailure = { e -> Log.e("UserViewModel", "Failed to fetch user: ${e.message}") })
  }

  fun getPastChallenges() {
    repository.getPastChallenges(
        userSession.getUserId()!!,
        onSuccess = { pastChallengesList -> _pastChallenges.value = pastChallengesList },
        onFailure = { e -> Log.e("UserViewModel", "Failed to get past challenges: ${e.message}") })
  }

  fun markQuestAsCompleted(questIndex: String) {
    repository.markQuestAsCompleted(
        userId = userSession.getUserId()!!,
        questIndex = questIndex,
        onSuccess = {
          Log.d("UserViewModel", "Quest $questIndex marked as completed.")
          fetchCompletedQuests() // Refresh the completed quests list
        },
        onFailure = { e ->
          Log.e("UserViewModel", "Failed to mark quest $questIndex as completed: ${e.message}")
        })
  }

  fun fetchCompletedQuests() {
    repository.getCompletedQuests(
        userId = userSession.getUserId()!!,
        onSuccess = { quests ->
          _completedQuests.value = quests
          Log.d("UserViewModel", "Fetched completed quests: $quests")
        },
        onFailure = { e ->
          Log.e("UserViewModel", "Failed to fetch completed quests: ${e.message}")
        })
  }
}
