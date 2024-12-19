package com.github.se.signify.model.common.user

import android.net.Uri
import com.github.se.signify.model.challenge.ChallengeId

private val failureException: Exception = Exception("Simulated failure")

class MockUserRepository : UserRepository {
  private val users = mutableMapOf<String, User>()
  private var shouldFail: Boolean = false
  private val mockData =
      mutableMapOf(
          "user1" to
              mapOf("challengesCompleted" to 5, "challengesCreated" to 10, "challengesWon" to 3),
          "user2" to
              mapOf("challengesCompleted" to 8, "challengesCreated" to 4, "challengesWon" to 6))

  private val mockUsers =
      listOf(
          User(
              uid = "mock-token",
              name = "Active user",
              friends = listOf("user3"),
              friendRequests = listOf("user4", "user5"),
              lastLoginDate = "2024-11-29"),
          User(uid = "user3", name = "User 3", friends = listOf("mock-token")),
          User(uid = "user4", name = "User 4"),
          User(uid = "user5", name = "User 5"))

  fun succeed() {
    shouldFail = false
  }

  fun fail() {
    shouldFail = true
  }

  fun clearUsers() {
    users.clear()
  }

  fun addUser(user: User) {
    users[user.uid] = user
  }

  fun setUsers(users: List<User>) {
    this.users.clear()
    for (user in users) {
      addUser(user)
    }
  }

  fun removeUser(userId: String) {
    users.remove(userId)
  }

  override fun init(onSuccess: () -> Unit) {
    if (!checkFailure {}) return
    setUsers(mockUsers)
    onSuccess()
  }

  override fun getFriendsList(
      userId: String,
      onSuccess: (List<String>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    onSuccess(user.friends)
  }

  override fun getRequestsFriendsList(
      userId: String,
      onSuccess: (List<String>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    onSuccess(user.friendRequests)
  }

  override fun getUserById(
      userId: String,
      onSuccess: (User) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    onSuccess(user)
  }

  override fun getUserName(
      userId: String,
      onSuccess: (String) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user =
        users[userId] ?: return onFailure(NoSuchElementException("User not found for ID: $userId"))

    // Ensure user.name is non-nullable before passing it to onSuccess
    user.name?.let { onSuccess(it) }
        ?: onFailure(NoSuchElementException("Username is missing for user ID: $userId"))
  }

  override fun updateUserName(
      userId: String,
      newName: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    users[userId] = user.copy(name = newName)
    onSuccess()
  }

  override fun getProfilePictureUrl(
      userId: String,
      onSuccess: (String?) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    onSuccess(user.profileImageUrl)
  }

  override fun updateProfilePictureUrl(
      userId: String,
      newProfilePictureUrl: Uri?,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    users[userId] = user.copy(profileImageUrl = newProfilePictureUrl.toString())
    onSuccess()
  }

  override fun sendFriendRequest(
      currentUserId: String,
      targetUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(currentUserId, onFailure)) return
    if (!checkUser(targetUserId, onFailure)) return

    val targetUser = users.getValue(targetUserId)
    users[targetUserId] =
        targetUser.copy(friendRequests = targetUser.friendRequests + currentUserId)
    onSuccess()
  }

  override fun acceptFriendRequest(
      currentUserId: String,
      friendUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(currentUserId, onFailure)) return
    if (!checkUser(friendUserId, onFailure)) return

    val currentUser = users.getValue(currentUserId)
    val friendUser = users.getValue(friendUserId)
    users[currentUserId] =
        currentUser.copy(
            friendRequests = currentUser.friendRequests - friendUserId,
            friends = currentUser.friends + friendUserId)
    users[friendUserId] = friendUser.copy(friends = friendUser.friends + currentUserId)
    onSuccess()
  }

  override fun removeFriend(
      currentUserId: String,
      friendUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(currentUserId, onFailure)) return
    if (!checkUser(friendUserId, onFailure)) return

    val currentUser = users.getValue(currentUserId)
    val friendUser = users.getValue(friendUserId)

    users[currentUserId] = currentUser.copy(friends = currentUser.friends - friendUserId)
    users[friendUserId] = friendUser.copy(friends = friendUser.friends - currentUserId)
    onSuccess()
  }

  override fun declineFriendRequest(
      currentUserId: String,
      friendUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(currentUserId, onFailure)) return
    if (!checkUser(friendUserId, onFailure)) return

    val currentUser = users.getValue(currentUserId)
    users[currentUserId] =
        currentUser.copy(friendRequests = currentUser.friendRequests - friendUserId)
    onSuccess()
  }

  override fun addOngoingChallenge(
      userId: String,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    users[userId] = user.copy(ongoingChallenges = user.ongoingChallenges + challengeId)
    onSuccess()
  }

  // This mock returns empty challenges. The current implementation of `UserRepositoryFireStore`
  // returns the challenges from the Firestore database, which it should not be responsible for.
  // This behavior can't possibly be mocked here.
  // This method should be changed in the `UserRepository` interface to return only the challenge
  // IDs.
  override fun getOngoingChallenges(
      userId: String,
      onSuccess: (List<ChallengeId>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    onSuccess(user.ongoingChallenges)
  }

  override fun removeOngoingChallenge(
      userId: String,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    users[userId] = user.copy(ongoingChallenges = user.ongoingChallenges - challengeId)
    onSuccess()
  }

  override fun getInitialQuestAccessDate(
      userId: String,
      onSuccess: (String?) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    onSuccess(user.lastLoginDate)
  }

  override fun setInitialQuestAccessDate(
      userId: String,
      date: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    users[userId] = user.copy(lastLoginDate = date)
    onSuccess()
  }

  // This was simplified to not use the current date. It does not match the implementation of
  // `UserRepositoryFireStore`.
  override fun updateStreak(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    users[userId] = user.copy(currentStreak = user.currentStreak + 1)
    onSuccess()
  }

  override fun getStreak(
      userId: String,
      onSuccess: (Long) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return

    // This was done to match the implementation of the UserRepositoryFireStore.
    // TODO: Make this fail on invalid userIds
    val user = users[userId]
    if (user == null) {
      onSuccess(0)
      return
    }

    onSuccess(user.currentStreak)
  }

  override fun markQuestAsCompleted(
      userId: String,
      questIndex: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    // Add the quest index to the completedQuests list, avoiding duplicates
    users[userId] =
        user.copy(
            completedQuests =
                user.completedQuests.toMutableList().apply {
                  if (!contains(questIndex)) add(questIndex)
                })
    onSuccess()
  }

  override fun getCompletedQuests(
      userId: String,
      onSuccess: (List<String>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    onSuccess(user.completedQuests)
  }

  override fun addPastChallenge(
      userId: String,
      challengeId: String,
  ) {
    val user = users.getValue(userId)
    users[userId] = user.copy(pastChallenges = user.pastChallenges + challengeId) // Add challenge
  }

  @Suppress("UNCHECKED_CAST")
  override fun updateUserField(
      userId: String,
      fieldName: String,
      value: Any,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val user = users[userId]
    if (user == null) {
      onFailure(Exception("User not found"))
      return
    }

    val updatedUser =
        when (fieldName) {
          "challengesCreated" -> user.copy(challengesCreated = value as Int)
          "challengesCompleted" -> user.copy(challengesCompleted = value as Int)
          "challengesWon" -> user.copy(challengesWon = value as Int)
          "pastChallenges" -> user.copy(pastChallenges = value as List<String>)
          else -> throw IllegalArgumentException("Invalid field name")
        }

    users[userId] = updatedUser
    onSuccess()
  }

  override fun getPastChallenges(
      userId: String,
      onSuccess: (List<ChallengeId>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val user = users.getValue(userId)
    onSuccess(user.pastChallenges)
  }

  override suspend fun getChallengesCompleted(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val value = mockData[userId]?.get("challengesCompleted") ?: 0
    onSuccess(value)
  }

  override suspend fun getChallengesCreated(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val value = mockData[userId]?.get("challengesCreated") ?: 0
    onSuccess(value)
  }

  override suspend fun getChallengesWon(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    if (!checkUser(userId, onFailure)) return

    val value = mockData[userId]?.get("challengesWon") ?: 0
    onSuccess(value)
  }

  private fun checkFailure(onFailure: (Exception) -> Unit): Boolean {
    if (shouldFail) {
      onFailure(failureException)
      return false
    }
    return true
  }

  private fun checkUser(userId: String, onFailure: (Exception) -> Unit): Boolean {
    if (users[userId] == null) {
      onFailure(Exception(USER_NOT_FOUND_MESSAGE))
      return false
    }
    return true
  }
}
