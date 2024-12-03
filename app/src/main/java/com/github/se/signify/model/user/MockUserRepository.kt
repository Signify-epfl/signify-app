package com.github.se.signify.model.user

import android.net.Uri
import com.github.se.signify.model.challenge.Challenge

private val failureException: Exception = Exception("Simulated failure")

class MockUserRepository : UserRepository {
  private val users = mutableMapOf<String, User>()
  private var shouldFail: Boolean = false

  fun succeed() {
    shouldFail = false
  }

  fun fail() {
    shouldFail = true
  }

  fun setUsers(users: List<User>) {
    this.users.clear()
    for (user in users) {
      addUser(user)
    }
  }

  fun addUser(user: User) {
    users[user.uid] = user
  }

  fun removeUser(userId: String) {
    users.remove(userId)
  }

  override fun init(onSuccess: () -> Unit) {
    onSuccess()
  }

  override fun getFriendsList(
      userId: String,
      onSuccess: (List<String>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    onSuccess(user.friends)
  }

  override fun getRequestsFriendsList(
      userId: String,
      onSuccess: (List<String>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    onSuccess(user.friendRequests)
  }

  override fun getUserById(
      userId: String,
      onSuccess: (User) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    onSuccess(user)
  }

  override fun getUserName(
      userId: String,
      onSuccess: (String) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!

    // This was done to match the implementation of the UserRepositoryFireStore
    // TODO: Make this non-nullable
    onSuccess(user.name ?: "unknown")
  }

  override fun updateUserName(
      userId: String,
      newName: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    users[userId] = user.copy(name = newName)
    onSuccess()
  }

  override fun getProfilePictureUrl(
      userId: String,
      onSuccess: (String?) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    onSuccess(user.profileImageUrl)
  }

  override fun updateProfilePictureUrl(
      userId: String,
      newProfilePictureUrl: Uri?,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    users[userId] = user.copy(profileImageUrl = newProfilePictureUrl.toString())
    onSuccess()
  }

  override fun sendFriendRequest(
      currentUserId: String,
      targetUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(currentUserId, onFailure)) return
    if (checkUser(targetUserId, onFailure)) return

    val targetUser = users[targetUserId]!!
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
    if (checkFailure(onFailure)) return
    if (checkUser(currentUserId, onFailure)) return
    if (checkUser(friendUserId, onFailure)) return

    val currentUser = users[currentUserId]!!
    val friendUser = users[friendUserId]!!

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
    if (checkFailure(onFailure)) return
    if (checkUser(currentUserId, onFailure)) return
    if (checkUser(friendUserId, onFailure)) return

    val currentUser = users[currentUserId]!!
    val friendUser = users[friendUserId]!!

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
    if (checkFailure(onFailure)) return
    if (checkUser(currentUserId, onFailure)) return
    if (checkUser(friendUserId, onFailure)) return

    val currentUser = users[currentUserId]!!
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
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
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
      onSuccess: (List<Challenge>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    val challenges = user.ongoingChallenges.map { Challenge(it) }
    onSuccess(challenges)
  }

  override fun removeOngoingChallenge(
      userId: String,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    users[userId] = user.copy(ongoingChallenges = user.ongoingChallenges - challengeId)
    onSuccess()
  }

  override fun getInitialQuestAccessDate(
      userId: String,
      onSuccess: (String?) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    onSuccess(user.lastLoginDate)
  }

  override fun setInitialQuestAccessDate(
      userId: String,
      date: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    users[userId] = user.copy(lastLoginDate = date)
    onSuccess()
  }

  // This was simplified to not use the current date. It does not match the implementation of
  // `UserRepositoryFireStore`.
  override fun updateStreak(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    users[userId] = user.copy(currentStreak = user.currentStreak + 1)
    onSuccess()
  }

  override fun getStreak(
      userId: String,
      onSuccess: (Long) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (checkFailure(onFailure)) return
    if (checkUser(userId, onFailure)) return

    val user = users[userId]!!
    onSuccess(user.currentStreak)
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
