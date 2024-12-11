package com.github.se.signify.model.common.user

data class User(
    val uid: String = "",
    val name: String? = null,
    val email: String? = null,
    val profileImageUrl: String? = null,
    val friendRequests: List<String> = emptyList(),
    val friends: List<String> = emptyList(),
    val ongoingChallenges: List<String> = emptyList(),
    val pastChallenges: List<String> = emptyList(),
    val lastLoginDate: String = "",
    val currentStreak: Long = 1L,
    val highestStreak: Long = 1L,
    val challengesCreated: Int = 0,
    val challengesCompleted: Int = 0,
    val challengesWon: Int = 0
)
