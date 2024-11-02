package com.github.se.signify.model.user

data class User(
    val uid: String = "",
    val name: String? = null,
    val email: String? = null,
    val profileImageUrl: String? = null,
    val friendRequests: List<String> = emptyList(),
    val friends: List<String> = emptyList(),
    val ongoingChallenges: List<String> = emptyList(), // New field for ongoing challenges
    val pastChallenges: List<String> = emptyList() // New field for past challenges
)
