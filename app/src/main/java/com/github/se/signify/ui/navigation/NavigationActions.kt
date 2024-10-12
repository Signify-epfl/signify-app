package com.github.se.signify.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

open class NavigationActions(
    private val navController: NavHostController,
) {
    open fun navigateTo(destination: TopLevelDestination) {

        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
                inclusive = true
            }

            // Avoid multiple copies of the same destination when reselecting same item
            launchSingleTop = true

            // Restore state when reselecting a previously selected item
            //if (destination.route != Route.AUTH) {
              //  restoreState = true
            //}
        }
    }

    open fun navigateTo(screen: String) {
        navController.navigate(screen)
    }

    open fun goBack() {
        navController.popBackStack()
    }

    open fun currentRoute(): String {
        return navController.currentDestination?.route ?: ""
    }
}
