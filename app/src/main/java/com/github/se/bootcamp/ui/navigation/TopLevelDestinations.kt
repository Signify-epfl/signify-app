package com.github.se.bootcamp.ui.navigation

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
        textId = Screen.PRACTICE
    )

    val PROFILE = TopLevelDestination(
        route = Route.PROFILE,
        icon = Icons.Outlined.Person,
        textId = Screen.PROFILE
    )

    val MAIN_AIM = TopLevelDestination(
        route = Route.MAIN_AIM,
        icon = Icons.Outlined.ThumbUp,
        textId = Screen.MAIN_AIM
    )

    val QUEST = TopLevelDestination(
        route = Route.QUEST,
        icon = Icons.Outlined.DateRange,
        textId = Screen.QUEST
    )

    val CHALLENGE = TopLevelDestination(
        route = Route.CHALLENGE,
        icon = Icons.Outlined.Star,
        textId = Screen.CHALLENGE
    )
}

val LIST_TOP_LEVEL_DESTINATION = listOf(
    TopLevelDestinations.CHALLENGE,
    TopLevelDestinations.QUEST,
    TopLevelDestinations.MAIN_AIM,
    TopLevelDestinations.PRACTICE,
    TopLevelDestinations.PROFILE
)