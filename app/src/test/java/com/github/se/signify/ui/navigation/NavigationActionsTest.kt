package com.github.se.signify.ui.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Route
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.model.navigation.TopLevelDestination
import com.github.se.signify.model.navigation.TopLevelDestinations
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

class NavigationActionsTest {

  private lateinit var navController: NavHostController
  private lateinit var userSession: MockUserSession
  private lateinit var navigationActions: NavigationActions

  @Before
  fun setUp() {
    navController = mock()
    userSession = MockUserSession()
    navigationActions = NavigationActions(navController, userSession)
  }

  @Test
  fun test() {
    navigationActions.navigateTo(Screen.AUTH)
    verify(navController).navigate(eq(Screen.AUTH.route), anyOrNull(), anyOrNull())
  }

  @Test
  fun navigateToUnrestrictedTopLevelDestinationWhenUserIsAuthenticated() {
    val destination = TopLevelDestinations.HOME
    userSession.loggedIn = true

    navigationActions.navigateTo(destination)
    verify(navController).navigate(eq(Route.HOME), any<NavOptionsBuilder.() -> Unit>())
  }

  @Test
  fun navigateToUnrestrictedTopLevelDestinationWhenUserIsUnauthenticated() {
    val destination = TopLevelDestination("", 0, "", false)
    userSession.loggedIn = false

    navigationActions.navigateTo(destination)
    verify(navController).navigate(eq(""), anyOrNull<NavOptionsBuilder.() -> Unit>())
  }

  @Test
  fun navigateToUnrestrictedScreenWhenUserIsAuthenticated() {
    val screen = Screen.DO_NOT_REQUIRE_AUTH
    userSession.loggedIn = true

    navigationActions.navigateTo(screen)
    verify(navController).navigate(eq(screen.route), anyOrNull(), anyOrNull())
  }

  @Test
  fun navigateToUnrestrictedScreenWhenUserIsUnauthenticated() {
    val screen = Screen.DO_NOT_REQUIRE_AUTH
    userSession.loggedIn = false

    navigationActions.navigateTo(screen)
    verify(navController).navigate(eq(screen.route), anyOrNull(), anyOrNull())
  }

  @Test
  fun navigateToRestrictedScreenWhenUserIsAuthenticated() {
    val screen = Screen.REQUIRE_AUTH
    userSession.loggedIn = true

    navigationActions.navigateTo(screen)

    verify(navController).navigate(eq(screen.route), anyOrNull(), anyOrNull())
  }

  @Test
  fun navigateToRestrictedScreenWhenUserIsUnauthenticated() {
    val screen = Screen.REQUIRE_AUTH
    userSession.loggedIn = false

    navigationActions.navigateTo(screen)

    verify(navController, never()).navigate(eq(screen.route), anyOrNull(), anyOrNull())
  }

  @Test
  fun goBack() {
    navigationActions.goBack()

    verify(navController).popBackStack()
  }
}
