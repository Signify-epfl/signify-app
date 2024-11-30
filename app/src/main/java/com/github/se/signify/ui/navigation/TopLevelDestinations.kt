package com.github.se.signify.ui.navigation

import com.github.se.signify.R

data class TopLevelDestination(
    val route: String,
    val icon: Int,
    val textId: String,
    val requiresAuth: Boolean = true
)

object TopLevelDestinations {

  val CHALLENGE =
      TopLevelDestination(
          route = Route.CHALLENGE,
          icon = R.drawable.battleicon, // Custom battle icon
          textId = "Challenge",
          requiresAuth = true)

  val HOME =
      TopLevelDestination(
          route = Route.HOME,
          icon = R.drawable.homeicon, // Custom home icon
          textId = "Home",
          requiresAuth = false)

  val PROFILE =
      TopLevelDestination(
          route = Route.PROFILE,
          icon = R.drawable.profileicon, // Custom profile icon
          textId = "Profile",
          requiresAuth = true)
}

val LIST_TOP_LEVEL_DESTINATION =
    listOf(TopLevelDestinations.CHALLENGE, TopLevelDestinations.HOME, TopLevelDestinations.PROFILE)
