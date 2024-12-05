package com.github.se.signify.model.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.github.se.signify.model.auth.UserSession

open class NavigationActions(
    private val navController: NavHostController,
    private val userSession: UserSession
) {
  /**
   * Navigate to the specified top level destination.
   *
   * @param destination The top level destination to navigate to.
   */
  open fun navigateTo(destination: TopLevelDestination) {
    val route =
        if (destination.requiresAuth && !userSession.isLoggedIn()) {
          Screen.UNAUTHENTICATED.route
        } else {
          destination.route
        }
    navController.navigate(route) {
      // Pop up to the start destination of the graph to
      // avoid building up a large stack of destinations
      popUpTo(navController.graph.findStartDestination().id) {
        saveState = true
        inclusive = true
      }

      // Avoid multiple copies of the same destination when reselecting same item
      launchSingleTop = true

      // Restore state when reselecting a previously selected item
      // if (destination.route != Route.AUTH) {
      //  restoreState = true
      // }

    }
  }

  /**
   * Navigate to the specified screen.
   *
   * @param screen The screen to navigate to.
   */
  open fun navigateTo(screen: Screen, params: Map<String, String>? = null) {
    if (screen.requiresAuth && !userSession.isLoggedIn()) {
      onUnauthenticated()
      return
    }
    navController.navigate(screen.route)
  }

  open fun goBack() {
    navController.popBackStack()
  }

  open fun currentRoute(): String? {
    return navController.currentDestination?.route
  }

  private fun onUnauthenticated() {
    navigateTo(Screen.UNAUTHENTICATED)
  }
}
