package com.github.se.signify.model.navigation

import android.util.Log
import androidx.navigation.NavHostController
import com.github.se.signify.model.authentication.UserSession

open class NavigationActions(
    private val navController: NavHostController,
    private val userSession: UserSession,
) {

  private var lastBackPressedTime: Long = 0L
  private val BACK_PRESS_THRESHOLD = 500L // Time in milliseconds to prevent rapid back presses

  /**
   * Navigate to the specified top level destination.
   *
   * @param destination The top level destination to navigate to.
   */
  open fun navigateTo(destination: TopLevelDestination) {
    if (destination.requiresAuth && !userSession.isLoggedIn()) {
      onUnauthenticated()
      return
    }

    navController.navigate(destination.route) {
      // Clear the stack at each navigation to the main screens
      // avoid building up a large stack of destinations
      popUpTo(0) {
        saveState = true
        inclusive = true
      }

      // Avoid multiple copies of the same destination when reselecting same item
      launchSingleTop = true
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

    val route =
        if (params != null) {
          var routeWithParams = screen.route
          params.forEach { (key, value) ->
            routeWithParams = routeWithParams.replace("{$key}", value)
          }
          routeWithParams
        } else {
          screen.route
        }

    if (currentRoute() == route) return

    navController.navigate(route)
  }

  open fun goBack() {
    val currentTime = System.currentTimeMillis()
    if (currentTime - lastBackPressedTime < BACK_PRESS_THRESHOLD) {
      return // Ignore rapid back presses
    }
    lastBackPressedTime = currentTime

    if (!navController.popBackStack()) {
      Log.d("Navigation", "No more screens to pop back to.")
    }
  }

  open fun currentRoute(): String? {
    return navController.currentDestination?.route
  }

  private fun onUnauthenticated() {
    navigateTo(Screen.UNAUTHENTICATED)
  }
}
