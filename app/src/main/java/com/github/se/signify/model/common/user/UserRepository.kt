package com.github.se.signify.model.common.user

import android.net.Uri
import com.github.se.signify.model.challenge.ChallengeId

const val USER_NOT_FOUND_MESSAGE = "User not found"

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

  fun getProfilePictureUrl(
      userId: String,
      onSuccess: (String?) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun updateProfilePictureUrl(
      userId: String,
      newProfilePictureUrl: Uri?,
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

  // TODO: This should only return the challenge IDs. The challenges themselves should be fetched
  // from the `ChallengeRepository`.
  fun getOngoingChallenges(
      userId: String,
      onSuccess: (List<ChallengeId>) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun removeOngoingChallenge(
      userId: String,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun getInitialQuestAccessDate(
      userId: String,
      onSuccess: (String?) -> Unit, // Date as "YYYY-MM-DD"
      onFailure: (Exception) -> Unit
  )

  fun setInitialQuestAccessDate(
      userId: String,
      date: String, // Date as "YYYY-MM-DD"
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun updateStreak(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)

  fun getStreak(userId: String, onSuccess: (Long) -> Unit, onFailure: (Exception) -> Unit)

  fun markQuestAsCompleted(
      userId: String,
      questIndex: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun getCompletedQuests(
      userId: String,
      onSuccess: (List<String>) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun addPastChallenge(userId: String, challengeId: String)

  fun getPastChallenges(
      userId: String,
      onSuccess: (List<Challenge>) -> Unit,
      onFailure: (Exception) -> Unit
  )

  fun updateUserField(
      userId: String,
      fieldName: String,
      value: Any,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )

  suspend fun getChallengesCompleted(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  )

  suspend fun getChallengesCreated(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  )

  suspend fun getChallengesWon(
      userId: String,
      onSuccess: (Int) -> Unit,
      onFailure: (Exception) -> Unit
  )
}
