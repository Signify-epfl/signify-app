package com.github.se.signify.ui.navigation

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.github.se.signify.R
import com.github.se.signify.model.auth.UserSession

open class NavigationActions(
    private val context: Context,
    private val navController: NavHostController,
    private val userSession: UserSession
) {
  /**
   * Navigate to the specified top level destination.
   *
   * @param destination The top level destination to navigate to.
   */
  open fun navigateTo(destination: TopLevelDestination) {

    navigateTo(destination.screen) {
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
  open fun navigateTo(screen: Screen, builder: NavOptionsBuilder.() -> Unit = {}) {
    if (screen.requiresAuth && !userSession.isLoggedIn()) {
      // TODO: Display a popup dialog to prompt the user to log in
      Toast.makeText(context, context.getString(R.string.unauthenticated_error), Toast.LENGTH_SHORT)
          .show()
    } else {
      navController.navigate(screen.route, builder)
    }
  }

  open fun goBack() {
    navController.popBackStack()
  }

  open fun currentRoute(): String {
    return navController.currentDestination?.route ?: ""
  }
}
