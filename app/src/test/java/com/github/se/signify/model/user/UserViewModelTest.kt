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

  private val testUser =
      User(
          name = "testUserName",
          uid = "testUserId",
          email = "testUser@gmail.com",
          profileImageUrl = null,
          friendRequests = listOf("fr1", "fr2", "fr3"),
          friends = listOf("f1", "f2", "f3"))

  private val currentUserId = "currentUserId"
  private val friendUserId = "friendUserId"

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
}
