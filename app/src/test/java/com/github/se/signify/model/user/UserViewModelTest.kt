package com.github.se.signify.model.user

import androidx.core.net.toUri
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.auth.UserSession
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

class UserViewModelTest {
  private lateinit var userSession: UserSession
  private lateinit var userRepository: UserRepository
  private lateinit var userViewModel: UserViewModel

  private val currentUserId = "currentUserId"
  private val friendUserId = "friendUserId"
  private val challengeId = "challengeId"

  @Before
  fun setUp() {
    userSession = MockUserSession()
    userRepository = mock(UserRepository::class.java)
    userViewModel = UserViewModel(userSession, userRepository)
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
}
