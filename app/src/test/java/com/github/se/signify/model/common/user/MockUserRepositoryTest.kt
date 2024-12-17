package com.github.se.signify.model.common.user

import android.net.Uri
import android.os.Looper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class MockUserRepositoryTest {

  private lateinit var mockUserRepository: MockUserRepository

  private val doNotFail: (Exception) -> Unit = { fail("Should not fail") }
  private val doNotSucceed: () -> Unit = { fail("Should not succeed") }
  private val doNotSucceedAny: (Any?) -> Unit = { doNotSucceed() }

  private val blankUser = User(uid = "blankUserId")
  private val activeUser =
      User(
          uid = "userId",
          name = "name",
          email = "email",
          profileImageUrl = "profileImageUrl",
          friendRequests = listOf("friendRequest1, friendRequest2"),
          friends = listOf("friend1, friend2"),
          ongoingChallenges = listOf("ongoingChallenge1, ongoingChallenge2"),
          pastChallenges = listOf("pastChallenge1, pastChallenge2"),
          lastLoginDate = "lastLoginDate",
          currentStreak = 1,
          highestStreak = 1,
          challengesCreated = 4,
          challengesCompleted = 2,
          challengesWon = 2)
  private val otherUser =
      User(
          uid = "otherUserId",
          name = "otherName",
          email = "otherEmail",
          profileImageUrl = "otherProfileImageUrl",
          friendRequests = listOf("otherFriendRequest1, otherFriendRequest2"),
          friends = listOf("otherFriend1, otherFriend2"),
          ongoingChallenges = listOf("otherOngoingChallenge1, otherOngoingChallenge2"),
          pastChallenges = listOf("otherPastChallenge1, otherPastChallenge2"),
          lastLoginDate = "otherLastLoginDate",
          currentStreak = 2,
          highestStreak = 2,
          challengesCreated = 4,
          challengesCompleted = 2,
          challengesWon = 2)
  private val users = listOf(blankUser, activeUser, otherUser)
  private val profilePictureUrl = Uri.parse("profilePictureUrl")

  @Before
  fun setUp() {
    mockUserRepository = MockUserRepository()
    mockUserRepository.setUsers(users)
    mockUserRepository.succeed()
  }

  @Test
  fun succeedWorks() {
    var success = 0
    val onSuccess: () -> Unit = { success += 1 }
    val onSuccessAny: (Any?) -> Unit = { onSuccess() }

    mockUserRepository.init(onSuccess)
    mockUserRepository.getFriendsList(blankUser.uid, onSuccessAny, doNotFail)
    mockUserRepository.getRequestsFriendsList(blankUser.uid, onSuccessAny, doNotFail)
    mockUserRepository.getUserById(blankUser.uid, { success += 1 }, doNotFail)
    mockUserRepository.getUserName(blankUser.uid, onSuccessAny, doNotFail)
    mockUserRepository.updateUserName(blankUser.uid, "newName", onSuccess, doNotFail)
    mockUserRepository.getProfilePictureUrl(blankUser.uid, onSuccessAny, doNotFail)
    mockUserRepository.updateProfilePictureUrl(
        blankUser.uid, profilePictureUrl, onSuccess, doNotFail)
    mockUserRepository.sendFriendRequest(blankUser.uid, otherUser.uid, onSuccess, doNotFail)
    mockUserRepository.acceptFriendRequest(blankUser.uid, otherUser.uid, onSuccess, doNotFail)
    mockUserRepository.removeFriend(blankUser.uid, otherUser.uid, onSuccess, doNotFail)
    mockUserRepository.declineFriendRequest(blankUser.uid, otherUser.uid, onSuccess, doNotFail)
    mockUserRepository.addOngoingChallenge(blankUser.uid, "challengeId", onSuccess, doNotFail)
    mockUserRepository.getOngoingChallenges(blankUser.uid, onSuccessAny, doNotFail)
    mockUserRepository.removeOngoingChallenge(blankUser.uid, "challengeId", onSuccess, doNotFail)
    mockUserRepository.getInitialQuestAccessDate(blankUser.uid, onSuccessAny, doNotFail)
    mockUserRepository.setInitialQuestAccessDate(blankUser.uid, "2024-01-01", onSuccess, doNotFail)
    mockUserRepository.updateStreak(blankUser.uid, onSuccess, doNotFail)
    mockUserRepository.getStreak(blankUser.uid, onSuccessAny, doNotFail)

    shadowOf(Looper.getMainLooper()).idle()
    assertEquals(19, success)
  }

  @Test
  fun failWorks() {
    var failure = 0
    val onFailure: (Exception) -> Unit = { failure += 1 }

    mockUserRepository.fail()

    mockUserRepository.getFriendsList(blankUser.uid, doNotSucceedAny, onFailure)
    mockUserRepository.getRequestsFriendsList(blankUser.uid, doNotSucceedAny, onFailure)
    mockUserRepository.getUserById(blankUser.uid, doNotSucceedAny, onFailure)
    mockUserRepository.getUserName(blankUser.uid, doNotSucceedAny, onFailure)
    mockUserRepository.updateUserName(blankUser.uid, "newName", doNotSucceed, onFailure)
    mockUserRepository.getProfilePictureUrl(blankUser.uid, doNotSucceedAny, onFailure)
    mockUserRepository.updateProfilePictureUrl(
        blankUser.uid, profilePictureUrl, doNotSucceed, onFailure)
    mockUserRepository.sendFriendRequest(blankUser.uid, otherUser.uid, doNotSucceed, onFailure)
    mockUserRepository.acceptFriendRequest(blankUser.uid, otherUser.uid, doNotSucceed, onFailure)
    mockUserRepository.removeFriend(blankUser.uid, otherUser.uid, doNotSucceed, onFailure)
    mockUserRepository.declineFriendRequest(blankUser.uid, otherUser.uid, doNotSucceed, onFailure)
    mockUserRepository.addOngoingChallenge(blankUser.uid, "challengeId", doNotSucceed, onFailure)
    mockUserRepository.getOngoingChallenges(blankUser.uid, doNotSucceedAny, onFailure)
    mockUserRepository.removeOngoingChallenge(blankUser.uid, "challengeId", doNotSucceed, onFailure)
    mockUserRepository.getInitialQuestAccessDate(blankUser.uid, doNotSucceedAny, onFailure)
    mockUserRepository.setInitialQuestAccessDate(
        blankUser.uid, "2024-01-01", doNotSucceed, onFailure)
    mockUserRepository.updateStreak(blankUser.uid, doNotSucceed, onFailure)
    mockUserRepository.getStreak(blankUser.uid, doNotSucceedAny, onFailure)

    shadowOf(Looper.getMainLooper()).idle()
    assertEquals(18, failure)
  }

  @Test
  fun failsOnInvalidUserId() {
    val userId = "invalidUserId"
    var failure = 0
    val onFailure: (Exception) -> Unit = { failure += 1 }

    mockUserRepository.getFriendsList(userId, doNotSucceedAny, onFailure)
    mockUserRepository.getRequestsFriendsList(userId, doNotSucceedAny, onFailure)
    mockUserRepository.getUserById(userId, doNotSucceedAny, onFailure)
    mockUserRepository.getUserName(userId, doNotSucceedAny, onFailure)
    mockUserRepository.updateUserName(userId, "newName", doNotSucceed, onFailure)
    mockUserRepository.getProfilePictureUrl(userId, doNotSucceedAny, onFailure)
    mockUserRepository.updateProfilePictureUrl(userId, profilePictureUrl, doNotSucceed, onFailure)
    mockUserRepository.sendFriendRequest(userId, otherUser.uid, doNotSucceed, onFailure)
    mockUserRepository.sendFriendRequest(activeUser.uid, userId, doNotSucceed, onFailure)
    mockUserRepository.acceptFriendRequest(userId, otherUser.uid, doNotSucceed, onFailure)
    mockUserRepository.acceptFriendRequest(activeUser.uid, userId, doNotSucceed, onFailure)
    mockUserRepository.removeFriend(userId, otherUser.uid, doNotSucceed, onFailure)
    mockUserRepository.removeFriend(activeUser.uid, userId, doNotSucceed, onFailure)
    mockUserRepository.declineFriendRequest(userId, otherUser.uid, doNotSucceed, onFailure)
    mockUserRepository.declineFriendRequest(activeUser.uid, userId, doNotSucceed, onFailure)
    mockUserRepository.addOngoingChallenge(userId, "challengeId", doNotSucceed, onFailure)
    mockUserRepository.getOngoingChallenges(userId, doNotSucceedAny, onFailure)
    mockUserRepository.removeOngoingChallenge(userId, "challengeId", doNotSucceed, onFailure)
    mockUserRepository.getInitialQuestAccessDate(userId, doNotSucceedAny, onFailure)
    mockUserRepository.setInitialQuestAccessDate(userId, "2024-01-01", doNotSucceed, onFailure)
    mockUserRepository.updateStreak(userId, doNotSucceed, onFailure)

    shadowOf(Looper.getMainLooper()).idle()
    assertEquals(21, failure)
  }

  @Test
  fun clearUsersWorks() {
    mockUserRepository.clearUsers()

    users.forEach { user -> mockUserRepository.getUserById(user.uid, doNotSucceedAny) {} }
  }

  @Test
  fun addUserWorks() {
    mockUserRepository.addUser(blankUser)

    mockUserRepository.getUserById(
        blankUser.uid, { response -> assertNotNull(response) }, doNotFail)
  }

  @Test
  fun setUsersWorks() {
    mockUserRepository.setUsers(listOf(blankUser, activeUser))

    mockUserRepository.getUserById(
        blankUser.uid, { response -> assertNotNull(response) }, doNotFail)
    mockUserRepository.getUserById(
        activeUser.uid, { response -> assertNotNull(response) }, doNotFail)
    mockUserRepository.getUserById(otherUser.uid, doNotSucceedAny) {}
  }

  @Test
  fun removeUserWorks() {
    mockUserRepository.removeUser(blankUser.uid)
    mockUserRepository.removeUser(activeUser.uid)

    mockUserRepository.getUserById(blankUser.uid, doNotSucceedAny) {}
    mockUserRepository.getUserById(activeUser.uid, doNotSucceedAny) {}
    mockUserRepository.getUserById(
        otherUser.uid, { response -> assertNotNull(response) }, doNotFail)
  }

  @Test
  fun getFriendsListWorks() {
    mockUserRepository.getFriendsList(
        blankUser.uid, { friends -> assertTrue(friends.isEmpty()) }, doNotFail)

    mockUserRepository.getFriendsList(
        activeUser.uid,
        { friends -> assertEquals(activeUser.friends.toSet(), friends.toSet()) },
        doNotFail)
  }

  @Test
  fun getRequestsFriendsListWorks() {
    mockUserRepository.getRequestsFriendsList(
        blankUser.uid, { requests -> assertTrue(requests.isEmpty()) }, doNotFail)

    mockUserRepository.getRequestsFriendsList(
        activeUser.uid,
        { requests -> assertEquals(activeUser.friendRequests.toSet(), requests.toSet()) },
        doNotFail)
  }

  @Test
  fun getUserByIdWorks() {
    mockUserRepository.getUserById(blankUser.uid, { assertEquals(blankUser, it) }, doNotFail)

    mockUserRepository.getUserById(activeUser.uid, { assertEquals(activeUser, it) }, doNotFail)
  }

  @Test
  fun getUserNameWorks() {
    mockUserRepository.getUserName(blankUser.uid, { assertEquals("unknown", it) }, doNotFail)

    mockUserRepository.getUserName(activeUser.uid, { assertEquals(activeUser.name, it) }, doNotFail)
  }

  @Test
  fun updateUserNameWorks() {
    val newName = "newName"
    mockUserRepository.updateUserName(
        activeUser.uid,
        newName,
        {
          mockUserRepository.getUserName(activeUser.uid, { assertEquals(newName, it) }, doNotFail)
        },
        doNotFail)
  }

  @Test
  fun getProfilePictureUrlWorks() {
    mockUserRepository.getProfilePictureUrl(blankUser.uid, { assertNull(it) }, doNotFail)

    mockUserRepository.getProfilePictureUrl(
        activeUser.uid, { assertEquals(activeUser.profileImageUrl, it) }, doNotFail)
  }

  @Test
  fun updateProfilePictureUrlWorks() {
    mockUserRepository.updateProfilePictureUrl(
        activeUser.uid,
        profilePictureUrl,
        {
          mockUserRepository.getProfilePictureUrl(
              activeUser.uid, { assertEquals(profilePictureUrl.toString(), it) }, doNotFail)
        },
        doNotFail)
  }

  @Test
  fun sendFriendRequestWorks() {
    mockUserRepository.sendFriendRequest(
        activeUser.uid,
        otherUser.uid,
        {
          mockUserRepository.getRequestsFriendsList(
              otherUser.uid, { assertTrue(it.contains(activeUser.uid)) }, doNotFail)
        },
        doNotFail)
  }

  @Test
  fun acceptFriendRequestWorks() {
    val receiver =
        User(
            uid = "receiverId",
            friendRequests = listOf(blankUser.uid),
        )

    mockUserRepository.addUser(receiver)

    mockUserRepository.acceptFriendRequest(
        receiver.uid,
        blankUser.uid,
        {
          mockUserRepository.getFriendsList(
              receiver.uid, { assertTrue(it.contains(blankUser.uid)) }, doNotFail)
          mockUserRepository.getFriendsList(
              blankUser.uid, { assertTrue(it.contains(receiver.uid)) }, doNotFail)
          mockUserRepository.getRequestsFriendsList(
              receiver.uid, { assertFalse(it.contains(blankUser.uid)) }, doNotFail)
        },
        doNotFail)
  }

  @Test
  fun removeFriendWorks() {
    val firstUserId = "firstUserId"
    val secondUserId = "secondUserId"
    val firstUser =
        User(
            uid = "firstUserId",
            friends = listOf(secondUserId),
        )
    val secondUser =
        User(
            uid = "secondUserId",
            friends = listOf(firstUserId),
        )

    mockUserRepository.addUser(firstUser)
    mockUserRepository.addUser(secondUser)

    mockUserRepository.removeFriend(
        firstUserId,
        secondUserId,
        {
          mockUserRepository.getFriendsList(
              firstUserId, { assertFalse(it.contains(secondUserId)) }, doNotFail)
          mockUserRepository.getFriendsList(
              secondUserId, { assertFalse(it.contains(firstUserId)) }, doNotFail)
        },
        doNotFail)
  }

  @Test
  fun declineFriendRequestWorks() {
    val receiver =
        User(
            uid = "receiverId",
            friendRequests = listOf(blankUser.uid),
        )

    mockUserRepository.addUser(receiver)

    mockUserRepository.declineFriendRequest(
        receiver.uid,
        blankUser.uid,
        {
          mockUserRepository.getRequestsFriendsList(
              receiver.uid, { assertFalse(it.contains(blankUser.uid)) }, doNotFail)
        },
        doNotFail)
  }

  // This is a shallow test, as getting challenges should be changed to return a list of challenge
  // IDs
  @Test
  fun addOngoingChallengeWorks() {
    val challengeId = "challengeId"
    mockUserRepository.addOngoingChallenge(
        activeUser.uid,
        challengeId,
        {
          mockUserRepository.getOngoingChallenges(
              activeUser.uid,
              { challengeIds -> assertTrue(challengeIds.any { it == challengeId }) },
              doNotFail)
        },
        doNotFail)
  }

  @Test
  fun getOngoingChallengesWorks() {
    mockUserRepository.getOngoingChallenges(blankUser.uid, { assertTrue(it.isEmpty()) }, doNotFail)

    mockUserRepository.getOngoingChallenges(
        activeUser.uid,
        { challenges ->
          assertEquals(activeUser.ongoingChallenges.toSet(), challenges.map { it }.toSet())
        },
        doNotFail)
  }

  @Test
  fun removeOngoingChallengeWorks() {
    val challengeId = "challengeId"
    val user =
        User(
            uid = "userId",
            ongoingChallenges = listOf(challengeId),
        )

    mockUserRepository.addUser(user)

    mockUserRepository.removeOngoingChallenge(
        user.uid,
        challengeId,
        {
          mockUserRepository.getOngoingChallenges(
              user.uid,
              { challenges -> assertFalse(challenges.any { it == challengeId }) },
              doNotFail)
        },
        doNotFail)
  }

  @Test
  fun getInitialQuestAccessDateWorks() {
    mockUserRepository.getInitialQuestAccessDate(
        blankUser.uid, { assertEquals(blankUser.lastLoginDate, it) }, doNotFail)

    mockUserRepository.getInitialQuestAccessDate(
        activeUser.uid, { assertEquals(activeUser.lastLoginDate, it) }, doNotFail)
  }

  @Test
  fun setInitialQuestAccessDateWorks() {
    val date = "2024-01-01"
    mockUserRepository.setInitialQuestAccessDate(
        activeUser.uid,
        date,
        {
          mockUserRepository.getInitialQuestAccessDate(
              activeUser.uid, { assertEquals(date, it) }, doNotFail)
        },
        doNotFail)
  }

  @Test
  fun updateStreakWorks() {
    mockUserRepository.updateStreak(
        activeUser.uid,
        {
          mockUserRepository.getStreak(
              activeUser.uid,
              { streak -> assertEquals(activeUser.currentStreak + 1, streak) },
              doNotFail)
        },
        doNotFail)
  }

  @Test
  fun getStreakWorks() {
    mockUserRepository.getStreak(
        blankUser.uid, { assertEquals(blankUser.currentStreak, it) }, doNotFail)

    mockUserRepository.getStreak(
        activeUser.uid, { assertEquals(activeUser.currentStreak, it) }, doNotFail)
  }

  @Test
  fun markQuestAsCompletedWorks() {
    val userId = activeUser.uid
    val questIndex = "quest1"

    // Act
    mockUserRepository.markQuestAsCompleted(
        userId,
        questIndex,
        {
          // Verify the quest is added to the completed quests list
          mockUserRepository.getCompletedQuests(
              userId,
              { completedQuests -> assertTrue(completedQuests.contains(questIndex)) },
              doNotFail)
        },
        doNotFail)
  }

  @Test
  fun markQuestAsCompletedAvoidsDuplicates() {
    val userId = activeUser.uid
    val questIndex = "quest1"

    // Add the quest twice
    mockUserRepository.markQuestAsCompleted(userId, questIndex, {}, doNotFail)
    mockUserRepository.markQuestAsCompleted(userId, questIndex, {}, doNotFail)

    // Verify the quest is only added once
    mockUserRepository.getCompletedQuests(
        userId,
        { completedQuests -> assertEquals(1, completedQuests.count { it == questIndex }) },
        doNotFail)
  }

  @Test
  fun markQuestAsCompletedFailsForInvalidUser() {
    val invalidUserId = "invalidUserId"
    val questIndex = "quest1"

    mockUserRepository.markQuestAsCompleted(
        invalidUserId,
        questIndex,
        doNotSucceed,
        { exception ->
          assertNotNull(exception)
          assertEquals("User not found", exception.message)
        })
  }

  @Test
  fun getCompletedQuestsWorks() {
    val userId = activeUser.uid
    val questIndex1 = "quest1"
    val questIndex2 = "quest2"

    // Add completed quests
    mockUserRepository.markQuestAsCompleted(userId, questIndex1, {}, doNotFail)
    mockUserRepository.markQuestAsCompleted(userId, questIndex2, {}, doNotFail)

    // Verify the completed quests list
    mockUserRepository.getCompletedQuests(
        userId,
        { completedQuests ->
          assertTrue(completedQuests.contains(questIndex1))
          assertTrue(completedQuests.contains(questIndex2))
        },
        doNotFail)
  }

  @Test
  fun getCompletedQuestsFailsForInvalidUser() {
    val invalidUserId = "invalidUserId"

    mockUserRepository.getCompletedQuests(
        invalidUserId,
        doNotSucceedAny,
        { exception ->
          assertNotNull(exception)
          assertEquals("User not found", exception.message)
        })
  }

  @Test
  fun addPastChallengeWorks() {
    val challengeId = "newPastChallenge"
    mockUserRepository.addPastChallenge(activeUser.uid, challengeId)

    mockUserRepository.getUserById(
        activeUser.uid,
        { user -> assertTrue(user.pastChallenges.contains(challengeId)) },
        doNotFail)
  }

  @Test
  fun incrementFieldWorksForValidFields() {
    // Increment the fields using `updateUserField`
    mockUserRepository.getUserById(
        activeUser.uid,
        { user ->
          mockUserRepository.updateUserField(
              activeUser.uid,
              "challengesCreated",
              user.challengesCreated + 1,
              onSuccess = {},
              onFailure = { fail("Should not fail") })
          mockUserRepository.updateUserField(
              activeUser.uid,
              "challengesCompleted",
              user.challengesCompleted + 1,
              onSuccess = {},
              onFailure = { fail("Should not fail") })
          mockUserRepository.updateUserField(
              activeUser.uid,
              "challengesWon",
              user.challengesWon + 1,
              onSuccess = {},
              onFailure = { fail("Should not fail") })
        },
        onFailure = { fail("User not found") })

    // Assert the updated values
    mockUserRepository.getUserById(
        activeUser.uid,
        {
          assertEquals(5, it.challengesCreated)
          assertEquals(3, it.challengesCompleted)
          assertEquals(3, it.challengesWon)
        },
        onFailure = { fail("Should not fail") })
  }

  @Test
  fun incrementFieldFailsForInvalidField() {
    mockUserRepository.getUserById(
        activeUser.uid,
        { user ->
          try {
            // Attempt to update an invalid field
            mockUserRepository.updateUserField(
                activeUser.uid,
                "invalidField",
                user.challengesCreated + 1,
                onSuccess = { fail("Should not succeed") },
                onFailure = { exception -> assertEquals("Invalid field name", exception.message) })
          } catch (e: IllegalArgumentException) {
            assertEquals("Invalid field name", e.message)
          }
        },
        onFailure = { fail("User not found") })

    // Assert that the valid fields remain unchanged
    mockUserRepository.getUserById(
        activeUser.uid,
        { user ->
          assertEquals(4, user.challengesCreated) // No change
          assertEquals(2, user.challengesCompleted) // No change
        },
        onFailure = { fail("Should not fail") })
  }

  @Test
  fun updateUserFieldWorksForValidField() {
    mockUserRepository.updateUserField(
        activeUser.uid,
        "challengesCreated",
        20,
        {
          mockUserRepository.getUserById(
              activeUser.uid, { user -> assertEquals(20, user.challengesCreated) }, doNotFail)
        },
        doNotFail)
  }

  @Test
  fun updateUserFieldFailsForInvalidField() {
    try {
      mockUserRepository.updateUserField(
          activeUser.uid, "invalidField", 20, doNotSucceed, doNotFail)
    } catch (e: IllegalArgumentException) {
      assertEquals("Invalid field name", e.message)
    }
  }

  @Test
  fun updateUserFieldFailsForMissingUser() {
    mockUserRepository.updateUserField(
        "nonExistentUser", "challengesCompleted", 10, doNotSucceed) { exception ->
          assertEquals("User not found", exception.message)
        }
  }

  @Test
  fun getPastChallengesWorks() {
    mockUserRepository.getPastChallenges(blankUser.uid, { assertTrue(it.isEmpty()) }, doNotFail)

    mockUserRepository.getPastChallenges(
        activeUser.uid,
        { challenges ->
          assertEquals(activeUser.pastChallenges.toSet(), challenges.map { it }.toSet())
        },
        doNotFail)
  }

  @Test
  fun getPastChallengesFailsForMissingUser() {
    mockUserRepository.getPastChallenges("nonExistentUser", doNotSucceedAny) { exception ->
      assertEquals("User not found", exception.message)
    }
  }

  @Test
  fun getChallengesCreatedReturnsCorrectValue() = runTest {
    var result: Int? = null
    val onSuccess: (Int) -> Unit = { value -> result = value }
    val onFailure: (Exception) -> Unit = { fail("Should not fail") }

    mockUserRepository.getChallengesCreated(activeUser.uid, onSuccess, onFailure)

    shadowOf(Looper.getMainLooper()).idle() // Simulate asynchronous execution
    assertEquals(0, result)
  }

  @Test
  fun getChallengesCompletedReturnsCorrectValue() = runTest {
    var result: Int? = null
    val onSuccess: (Int) -> Unit = { value -> result = value }
    val onFailure: (Exception) -> Unit = { fail("Should not fail") }

    mockUserRepository.getChallengesCompleted(activeUser.uid, onSuccess, onFailure)

    shadowOf(Looper.getMainLooper()).idle()
    assertEquals(0, result)
  }

  @Test
  fun getChallengesWonReturnsCorrectValue() = runTest {
    var result: Int? = null
    val onSuccess: (Int) -> Unit = { value -> result = value }
    val onFailure: (Exception) -> Unit = { fail("Should not fail") }

    mockUserRepository.getChallengesWon(activeUser.uid, onSuccess, onFailure)

    shadowOf(Looper.getMainLooper()).idle()
    assertEquals(0, result)
  }
}
