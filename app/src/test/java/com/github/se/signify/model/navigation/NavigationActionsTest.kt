package com.github.se.signify.model.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.github.se.signify.model.auth.MockUserSession
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
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
  fun navigateToRestrictedTopLevelDestinationWhenUserIsAuthenticated() {
    val destination = TopLevelDestinations.PROFILE
    userSession.loggedIn = true

    navigationActions.navigateTo(destination)
    verify(navController).navigate(eq(Route.PROFILE), any<NavOptionsBuilder.() -> Unit>())
  }

  @Test
  fun navigateToRestrictedTopLevelDestinationWhenUserIsUnauthenticated() {
    val destination = TopLevelDestinations.PROFILE
    userSession.loggedIn = false

    navigationActions.navigateTo(destination)
    verify(navController).navigate(eq(Route.PROFILE), anyOrNull<NavOptionsBuilder.() -> Unit>())
    verify(navController, never())
        .navigate(eq(Screen.UNAUTHENTICATED.route), anyOrNull<NavOptionsBuilder.() -> Unit>())
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
    verify(navController).navigate(eq(screen.route), anyOrNull(), anyOrNull())
    verify(navController, never())
        .navigate(eq(Screen.UNAUTHENTICATED.route), anyOrNull(), anyOrNull())
  }

  @Test
  fun goBack() {
    navigationActions.goBack()

    verify(navController).popBackStack()
  }

  @Test
  fun currentRoute() {
    assertNull(navigationActions.currentRoute())
    navigationActions.navigateTo(Screen.DO_NOT_REQUIRE_AUTH)
    assertNull(navigationActions.currentRoute())

    verify(navController, times(2)).currentDestination
  }
}
