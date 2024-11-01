package com.github.se.signify.model.user

interface UserRepository {

  fun init(onSuccess: () -> Unit)

  fun getFriendsList(
      userId: String,
      onSuccess: (List<String>) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun getRequestsFriendsList(
      userId: String,
      onSuccess: (List<String>) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun getUserById(userId: String, onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit)

  fun getUserName(userId: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit)

  fun updateUserName(
      userId: String,
      newName: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun sendFriendRequest(
      currentUserId: String,
      targetUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun acceptFriendRequest(
      currentUserId: String,
      friendUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun removeFriend(
      currentUserId: String,
      friendUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun declineFriendRequest(
      currentUserId: String,
      friendUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun addOngoingChallenge(
      userId: String,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun removeOngoingChallenge(
      userId: String,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun addPastChallenge(
      userId: String,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )
}
