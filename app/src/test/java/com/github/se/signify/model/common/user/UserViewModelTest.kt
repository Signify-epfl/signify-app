package com.github.se.signify.model.common.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.net.toUri
import com.github.se.signify.model.authentication.MockUserSession
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.Challenge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class UserViewModelTest {

  @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

  private lateinit var userSession: UserSession
  private lateinit var userRepository: UserRepository
  private lateinit var userViewModel: UserViewModel

  private lateinit var currentUserId: String
  private val friendUserId = "friendUserId"
  private val challengeId = "challengeId"
  private val testChallenge = Challenge(challengeId = "challenge1")

  private val testDispatcher = UnconfinedTestDispatcher()

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    userSession = MockUserSession()
    userRepository = mock(UserRepository::class.java)
    userViewModel = UserViewModel(userSession, userRepository)

    currentUserId = userSession.getUserId()!!
  }

  @Test
  fun getFriendsListRequestCallsRepository() {
    userViewModel.getFriendsList()
    verify(userRepository).getFriendsList(eq(currentUserId), any(), any())
  }

  @Test
  fun getRequestsFriendsListRequestCallsRepository() {
    userViewModel.getRequestsFriendsList()
    verify(userRepository).getRequestsFriendsList(eq(currentUserId), any(), any())
  }

  @Test
  fun getUserByIdRequestCallsRepository() {
    userViewModel.getUserById(currentUserId)
    verify(userRepository).getUserById(eq(currentUserId), any(), any())
  }

  @Test
  fun getUserNameRequestCallsRepository() {
    userViewModel.getUserName()
    verify(userRepository).getUserName(eq(currentUserId), any(), any())
  }

  @Test
  fun updateUserNameRequestCallsRepository() {
    val newName = "newNameTest"
    userViewModel.updateUserName(newName)
    verify(userRepository).updateUserName(eq(currentUserId), eq(newName), any(), any())
  }

  @Test
  fun getProfilePictureUrlRequestCallsRepository() {
    userViewModel.getProfilePictureUrl()
    verify(userRepository).getProfilePictureUrl(eq(currentUserId), any(), any())
  }

  @Test
  fun updateProfilePictureUrlRequestCallsRepository() {
    val newUrl = "testUrl"
    userViewModel.updateProfilePictureUrl(newUrl.toUri())
    verify(userRepository)
        .updateProfilePictureUrl(eq(currentUserId), eq(newUrl.toUri()), any(), any())
  }

  @Test
  fun sendFriendRequestCallsRepository() {
    userViewModel.sendFriendRequest(friendUserId)
    verify(userRepository).sendFriendRequest(eq(currentUserId), eq(friendUserId), any(), any())
  }

  @Test
  fun acceptFriendRequestCallsRepository() {
    userViewModel.acceptFriendRequest(friendUserId)
    verify(userRepository).acceptFriendRequest(eq(currentUserId), eq(friendUserId), any(), any())
  }

  @Test
  fun declineFriendRequestCallsRepository() {
    userViewModel.declineFriendRequest(friendUserId)
    verify(userRepository).declineFriendRequest(eq(currentUserId), eq(friendUserId), any(), any())
  }

  @Test
  fun removeFriendCallsRepository() {
    userViewModel.removeFriend(friendUserId)
    verify(userRepository).removeFriend(eq(currentUserId), eq(friendUserId), any(), any())
  }

  @Test
  fun removeOngoingChallenge_callsRepository() {
    userViewModel.removeOngoingChallenge(currentUserId, challengeId)
    verify(userRepository).removeOngoingChallenge(eq(currentUserId), eq(challengeId), any(), any())
  }

  @Test
  fun addOngoingChallenge_callsRepository() {
    userViewModel.addOngoingChallenge(currentUserId, challengeId)
    verify(userRepository).addOngoingChallenge(eq(currentUserId), eq(challengeId), any(), any())
  }

  @Test
  fun getStreak_callsRepository() {
    userViewModel.getStreak()
    verify(userRepository).getStreak(eq(currentUserId), any(), any())
  }

  @Test
  fun updateStreak_callsRepository() {
    userViewModel.updateStreak()
    verify(userRepository).updateStreak(eq(currentUserId), any(), any())
  }

  @Test
  fun getFriendsListUpdatesFriendsOnSuccess() = runTest {
    // Arrange
    val friendsList = listOf("friend1", "friend2")
    doAnswer {
          val onSuccess = it.getArgument<(List<String>) -> Unit>(1)
          onSuccess(friendsList)
          null
        }
        .whenever(userRepository)
        .getFriendsList(eq(currentUserId), any(), any())

    // Act
    userViewModel.getFriendsList()

    // Assert
    // Verifies that the friends list is updated correctly in the ViewModel when the repository call
    // succeeds
    assertEquals(friendsList, userViewModel.friends.value)
  }

  @Test
  fun getFriendsListHandlesFailure() = runTest {
    // Arrange
    val exception = Exception("Failed to get friends list")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(2)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .getFriendsList(eq(currentUserId), any(), any())

    // Act
    userViewModel.getFriendsList()

    // Assert
    // Error is logged; verify the method call
    verify(userRepository).getFriendsList(eq(currentUserId), any(), any())

    // Verifies that the friends list in the ViewModel remains empty when the repository call fails
    assertTrue(userViewModel.friends.value.isEmpty())
  }

  @Test
  fun getUserByIdUpdatesSearchResultOnSuccess() = runTest {
    // Arrange
    val testUser = User(uid = "testUserId", name = "Test User")
    doAnswer {
          val onSuccess = it.getArgument<(User) -> Unit>(1)
          onSuccess(testUser)
          null
        }
        .whenever(userRepository)
        .getUserById(eq(currentUserId), any(), any())

    // Act
    userViewModel.getUserById(currentUserId)

    // Assert
    // Verifies that the search result is updated with the correct user data on success
    assertEquals(testUser, userViewModel.searchResult.value)
    // Ensures no error is present when the repository call is successful
    assertNull(userViewModel.errorState.value)
  }

  @Test
  fun getUserByIdUpdatesErrorStateOnFailure() = runTest {
    // Arrange
    val exception = Exception("User not found")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(2)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .getUserById(eq(currentUserId), any(), any())

    // Act
    userViewModel.getUserById(currentUserId)

    // Assert
    // Verifies that the search result remains null on failure
    assertNull(userViewModel.searchResult.value)
    // Ensures the error state is updated with the correct error message
    assertEquals("User not found", userViewModel.errorState.value)
  }

  @Test
  fun setSearchResultUpdatesSearchResult() {
    // Arrange
    val testUser = User(uid = "testUserId", name = "Test User")

    // Act
    userViewModel.setSearchResult(testUser)

    // Assert
    // Confirms that the search result is updated correctly in the ViewModel
    assertEquals(testUser, userViewModel.searchResult.value)
  }

  @Test
  fun setErrorStateUpdatesErrorState() {
    // Arrange
    val errorMessage = "An error occurred"

    // Act
    userViewModel.setErrorState(errorMessage)

    // Assert
    // Confirms that the error state is updated with the specified message
    assertEquals(errorMessage, userViewModel.errorState.value)
  }

  @Test
  fun clearErrorStateClearsErrorState() {
    // Arrange
    userViewModel.setErrorState("An error occurred")

    // Act
    userViewModel.clearErrorState()

    // Assert
    // Verifies that the error state is cleared properly
    assertNull(userViewModel.errorState.value)
  }

  @Test
  fun getUserNameUpdatesUserNameOnSuccess() = runTest {
    // Arrange
    val userName = "Test User"
    doAnswer {
          val onSuccess = it.getArgument<(String) -> Unit>(1)
          onSuccess(userName)
          null
        }
        .whenever(userRepository)
        .getUserName(eq(currentUserId), any(), any())

    // Act
    userViewModel.getUserName()

    // Assert
    // Verifies that the user name is updated correctly on a successful repository call
    assertEquals(userName, userViewModel.userName.value)
  }

  @Test
  fun getUserNameHandlesFailure() = runTest {
    // Arrange
    val exception = Exception("Failed to get user name")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(2)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .getUserName(eq(currentUserId), any(), any())

    // Act
    userViewModel.getUserName()

    // Assert
    // Ensures the repository method is called with the correct arguments
    verify(userRepository).getUserName(eq(currentUserId), any(), any())
  }

  @Test
  fun getProfilePictureUrlUpdatesProfileUrlOnSuccess() = runTest {
    // Arrange
    val profileUrl = "http://example.com/profile.jpg"
    doAnswer {
          val onSuccess = it.getArgument<(String?) -> Unit>(1)
          onSuccess(profileUrl)
          null
        }
        .whenever(userRepository)
        .getProfilePictureUrl(eq(currentUserId), any(), any())

    // Act
    userViewModel.getProfilePictureUrl()

    // Assert
    // Verifies that the profile picture URL is updated correctly on success
    assertEquals(profileUrl, userViewModel.profilePictureUrl.value)
  }

  @Test
  fun getProfilePictureUrlHandlesFailure() = runTest {
    // Arrange
    val exception = Exception("Failed to get profile picture")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(2)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .getProfilePictureUrl(eq(currentUserId), any(), any())

    // Act
    userViewModel.getProfilePictureUrl()

    // Assert
    // Ensures the repository method is called with the correct arguments
    verify(userRepository).getProfilePictureUrl(eq(currentUserId), any(), any())
  }

  @Test
  fun updateProfilePictureUrlCallsRepositoryAndHandlesSuccess() = runTest {
    // Arrange
    val uri = "http://example.com/new_profile.jpg".toUri()
    doAnswer {
          val onSuccess = it.getArgument<() -> Unit>(2)
          onSuccess()
          null
        }
        .whenever(userRepository)
        .updateProfilePictureUrl(eq(currentUserId), eq(uri), any(), any())

    // Act
    userViewModel.updateProfilePictureUrl(uri)

    // Assert
    // Confirms the repository method is called with the correct arguments when updating the profile
    // picture
    verify(userRepository).updateProfilePictureUrl(eq(currentUserId), eq(uri), any(), any())
  }

  @Test
  fun updateProfilePictureUrlHandlesFailure() = runTest {
    // Arrange
    val uri = "http://example.com/new_profile.jpg".toUri()
    val exception = Exception("Failed to update profile picture")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(3)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .updateProfilePictureUrl(eq(currentUserId), eq(uri), any(), any())

    // Act
    userViewModel.updateProfilePictureUrl(uri)

    // Assert
    // Confirms the repository method is called with the correct arguments even on failure
    verify(userRepository).updateProfilePictureUrl(eq(currentUserId), eq(uri), any(), any())
  }

  @Test
  fun getOngoingChallengesUpdatesChallengesOnSuccess() = runTest {
    // Arrange
    val challenges =
        listOf(Challenge(challengeId = "challenge1"), Challenge(challengeId = "challenge2"))
    doAnswer {
          val onSuccess = it.getArgument<(List<Challenge>) -> Unit>(1)
          onSuccess(challenges)
          null
        }
        .whenever(userRepository)
        .getOngoingChallenges(eq(currentUserId), any(), any())

    // Act
    userViewModel.getOngoingChallenges()

    // Assert
    // Verifies that the ongoing challenges list is updated correctly in the ViewModel
    assertEquals(challenges, userViewModel.ongoingChallenges.value)
  }

  @Test
  fun getOngoingChallengesHandlesFailure() = runTest {
    // Arrange
    val exception = Exception("Failed to fetch ongoing challenges")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(2)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .getOngoingChallenges(eq(currentUserId), any(), any())

    // Act
    userViewModel.getOngoingChallenges()

    // Assert
    // Ensures the repository method is called with the correct arguments
    verify(userRepository).getOngoingChallenges(eq(currentUserId), any(), any())
  }

  @Test
  fun removeOngoingChallengeHandlesFailure() = runTest {
    // Arrange
    val exception = Exception("Failed to remove challenge")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(3)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .removeOngoingChallenge(eq(currentUserId), eq(challengeId), any(), any())

    // Act
    userViewModel.removeOngoingChallenge(currentUserId, challengeId)

    // Assert
    // Confirms the repository method is called with the correct arguments when removing a challenge
    verify(userRepository).removeOngoingChallenge(eq(currentUserId), eq(challengeId), any(), any())
  }

  @Test
  fun checkAndUnlockNextQuestSetsInitialAccessDateWhenNull() = runTest {
    // Arrange
    doAnswer {
          val onSuccess = it.getArgument<(String?) -> Unit>(1)
          onSuccess(null)
          null
        }
        .whenever(userRepository)
        .getInitialQuestAccessDate(eq(currentUserId), any(), any())

    doAnswer {
          val onSuccess = it.getArgument<() -> Unit>(2)
          onSuccess()
          null
        }
        .whenever(userRepository)
        .setInitialQuestAccessDate(eq(currentUserId), any(), any(), any())

    // Act
    userViewModel.checkAndUnlockNextQuest()

    // Assert
    // Verifies that the unlocked quests value is updated correctly when the initial access date is
    // null
    assertEquals("1", userViewModel.unlockedQuests.value)
  }

  @Test
  fun checkAndUnlockNextQuestHandlesFailureInGetInitialQuestAccessDate() = runTest {
    // Arrange
    val exception = Exception("Failed to get initial quest access date")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(2)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .getInitialQuestAccessDate(eq(currentUserId), any(), any())

    // Act
    userViewModel.checkAndUnlockNextQuest()

    // Assert
    // Ensures the repository method is called with the correct arguments on failure
    verify(userRepository).getInitialQuestAccessDate(eq(currentUserId), any(), any())
  }

  @Test
  fun getStreakUpdatesStreakOnSuccess() = runTest {
    // Arrange
    val testStreak = 5L
    doAnswer {
          val onSuccess = it.getArgument<(Long) -> Unit>(1)
          onSuccess(testStreak)
          null
        }
        .whenever(userRepository)
        .getStreak(eq(currentUserId), any(), any())

    // Act
    userViewModel.getStreak()

    // Assert
    // Verifies that the streak value is updated correctly in the ViewModel
    assertEquals(testStreak, userViewModel.streak.value)
  }

  @Test
  fun getStreakHandlesFailure() = runTest {
    // Arrange
    val exception = Exception("Failed to get user's streak")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(2)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .getStreak(eq(currentUserId), any(), any())

    // Act
    userViewModel.getStreak()

    // Assert
    // Confirms the repository method is called with the correct arguments on failure
    verify(userRepository).getStreak(eq(currentUserId), any(), any())
  }
  // Test addPastChallenge
  @Test
  fun addPastChallengeUpdatesPastChallengesOnSuccess() = runTest {
    // Arrange
    val updatedPastChallenges = listOf("challenge1", "challenge2")
    doAnswer {
          val onSuccess = it.getArgument<(User) -> Unit>(1)
          onSuccess(User(uid = currentUserId, pastChallenges = listOf("challenge1")))
          null
        }
        .whenever(userRepository)
        .getUserById(eq(currentUserId), any(), any())

    doAnswer {
          val onSuccess = it.getArgument<() -> Unit>(2)
          onSuccess()
          null
        }
        .whenever(userRepository)
        .updateUserField(eq(currentUserId), eq("pastChallenges"), any(), any(), any())

    // Act
    userViewModel.addPastChallenge(currentUserId, "challenge2")

    // Assert
    verify(userRepository)
        .updateUserField(
            eq(currentUserId), eq("pastChallenges"), eq(updatedPastChallenges), any(), any())
  }

  @Test
  fun addPastChallengeHandlesFailureInFetchingUser() = runTest {
    // Arrange
    val exception = Exception("Failed to fetch user")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(2)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .getUserById(eq(currentUserId), any(), any())

    // Act
    userViewModel.addPastChallenge(currentUserId, "challenge2")

    // Assert
    verify(userRepository).getUserById(eq(currentUserId), any(), any())
  }

  @Test
  fun incrementFieldHandlesInvalidFieldName() = runTest {
    // Act & Assert
    try {
      userViewModel.incrementField(currentUserId, "invalidField")
    } catch (e: IllegalArgumentException) {
      assertEquals("Invalid field name", e.message)
    }
  }

  @Test
  fun incrementFieldHandlesFailureInFetchingUser() = runTest {
    // Arrange
    val exception = Exception("Failed to fetch user")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(2)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .getUserById(eq(currentUserId), any(), any())

    // Act
    userViewModel.incrementField(currentUserId, "challengesWon")

    // Assert
    verify(userRepository).getUserById(eq(currentUserId), any(), any())
  }

  // Test getPastChallenges
  @Test
  fun getPastChallengesUpdatesPastChallengesOnSuccess() = runTest {
    // Arrange
    val pastChallenges = listOf(testChallenge)
    doAnswer {
          val onSuccess = it.getArgument<(List<Challenge>) -> Unit>(1)
          onSuccess(pastChallenges)
          null
        }
        .whenever(userRepository)
        .getPastChallenges(eq(currentUserId), any(), any())

    // Act
    userViewModel.getPastChallenges()

    // Assert
    assertEquals(pastChallenges, userViewModel.pastChallenges.value)
  }

  @Test
  fun getPastChallengesHandlesFailure() = runTest {
    // Arrange
    val exception = Exception("Failed to fetch past challenges")
    doAnswer {
          val onFailure = it.getArgument<(Exception) -> Unit>(2)
          onFailure(exception)
          null
        }
        .whenever(userRepository)
        .getPastChallenges(eq(currentUserId), any(), any())

    // Act
    userViewModel.getPastChallenges()

    // Assert
    verify(userRepository).getPastChallenges(eq(currentUserId), any(), any())
    assertTrue(userViewModel.pastChallenges.value.isEmpty())
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }
}
