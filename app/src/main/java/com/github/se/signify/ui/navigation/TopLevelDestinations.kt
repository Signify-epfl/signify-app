package com.github.se.signify.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelDestination(
    val route: String,
    val icon: ImageVector,
    val textId: String
)

object TopLevelDestinations {

    val PRACTICE = TopLevelDestination(
        route = Route.PRACTICE,
        icon = Icons.Outlined.Edit,
        textId = "Practice"
    )

    val PROFILE = TopLevelDestination(
        route = Route.PROFILE,
        icon = Icons.Outlined.Person,
        textId = "Profile"
    )

    val MAIN_AIM = TopLevelDestination(
        route = Route.MAIN_AIM,
        icon = Icons.Outlined.ThumbUp,
        textId = "Main"
    )

    val QUEST = TopLevelDestination(
        route = Route.QUEST,
        icon = Icons.Outlined.DateRange,
        textId = "Quest"
    )

    val CHALLENGE = TopLevelDestination(
        route = Route.CHALLENGE,
        icon = Icons.Outlined.Star,
        textId = "Challenge"
    )
}

val LIST_TOP_LEVEL_DESTINATION = listOf(
    TopLevelDestinations.PRACTICE,
    TopLevelDestinations.MAIN_AIM,
    TopLevelDestinations.PROFILE,
    TopLevelDestinations.QUEST,
    TopLevelDestinations.CHALLENGE
)