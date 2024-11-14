package com.github.se.signify.model.user

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

class UserViewModelTest {
  private lateinit var userRepository: UserRepository
  private lateinit var userViewModel: UserViewModel

  private val currentUserId = "currentUserId"
  private val friendUserId = "friendUserId"
  private val challengeId = "challengeId"

  @Before
  fun setUp() {
    userRepository = mock(UserRepository::class.java)
    userViewModel = UserViewModel(userRepository)
  }

  @Test
  fun getFriendsListRequestCallsRepository() {
    userViewModel.getFriendsList(currentUserId)
    verify(userRepository).getFriendsList(eq(currentUserId), any(), any())
  }

  @Test
  fun getRequestsFriendsListRequestCallsRepository() {
    userViewModel.getRequestsFriendsList(currentUserId)
    verify(userRepository).getRequestsFriendsList(eq(currentUserId), any(), any())
  }

  @Test
  fun getUserByIdRequestCallsRepository() {
    userViewModel.getUserById(currentUserId)
    verify(userRepository).getUserById(eq(currentUserId), any(), any())
  }

  @Test
  fun getUserNameRequestCallsRepository() {
    userViewModel.getUserName(currentUserId)
    verify(userRepository).getUserName(eq(currentUserId), any(), any())
  }

  @Test
  fun updateUserNameRequestCallsRepository() {
    val newName = "newNameTest"
    userViewModel.updateUserName(currentUserId, newName)
    verify(userRepository).updateUserName(eq(currentUserId), eq(newName), any(), any())
  }

  @Test
  fun getProfilePictureUrlRequestCallsRepository() {
    userViewModel.getProfilePictureUrl(currentUserId)
    verify(userRepository).getProfilePictureUrl(eq(currentUserId), any(), any())
  }

  @Test
  fun updateProfilePictureUrlRequestCallsRepository() {
    val newUrl = "testUrl"
    userViewModel.updateProfilePictureUrl(currentUserId, newUrl)
    verify(userRepository).updateProfilePictureUrl(eq(currentUserId), eq(newUrl), any(), any())
  }

  @Test
  fun sendFriendRequestCallsRepository() {
    userViewModel.sendFriendRequest(currentUserId, friendUserId)
    verify(userRepository).sendFriendRequest(eq(currentUserId), eq(friendUserId), any(), any())
  }

  @Test
  fun acceptFriendRequestCallsRepository() {
    userViewModel.acceptFriendRequest(currentUserId, friendUserId)
    verify(userRepository).acceptFriendRequest(eq(currentUserId), eq(friendUserId), any(), any())
  }

  @Test
  fun declineFriendRequestCallsRepository() {
    userViewModel.declineFriendRequest(currentUserId, friendUserId)
    verify(userRepository).declineFriendRequest(eq(currentUserId), eq(friendUserId), any(), any())
  }

  @Test
  fun removeFriendCallsRepository() {
    userViewModel.removeFriend(currentUserId, friendUserId)
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
    userViewModel.getStreak(currentUserId)
    verify(userRepository).getStreak(eq(currentUserId), any(), any())
  }

  @Test
  fun updateStreak_callsRepository() {
    userViewModel.updateStreak(currentUserId)
    verify(userRepository).updateStreak(eq(currentUserId), any(), any())
  }
}
