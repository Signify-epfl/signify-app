package com.github.se.signify.ui.navigation

import com.github.se.signify.R

data class TopLevelDestination(val route: String, val icon: Int, val textId: String)

object TopLevelDestinations {

  val CHALLENGE =
      TopLevelDestination(
          route = Route.CHALLENGE,
          icon = R.drawable.battleicon, // Custom battle icon
          textId = "Challenge")

  val MAIN_AIM =
      TopLevelDestination(
          route = Route.MAIN_AIM,
          icon = R.drawable.homeicon, // Custom home icon
          textId = "Main")

  val PROFILE =
      TopLevelDestination(
          route = Route.PROFILE,
          icon = R.drawable.profileicon, // Custom profile icon
          textId = "Profile")
}

val LIST_TOP_LEVEL_DESTINATION =
    listOf(
        TopLevelDestinations.CHALLENGE, TopLevelDestinations.MAIN_AIM, TopLevelDestinations.PROFILE)
