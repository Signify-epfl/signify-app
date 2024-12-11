package com.github.se.signify

import android.app.Application
import com.github.se.signify.model.dependencyInjection.AppDependencyProvider
import com.github.se.signify.model.dependencyInjection.DependencyProvider

/**
 * `BaseApplication` is the foundational application class for the app, providing a standardized
 * setup for dependency injection.
 *
 * This class defines the `dependencyProvider` property, which serves as the central access point
 * for the app's dependencies. It uses `AppDependencyProvider` by default to supply production
 * dependencies.
 *
 * Key Features:
 * - Ensures consistent dependency injection across the app.
 * - Supports overriding in subclasses (e.g., `TestApplication`) for custom configurations.
 * - Uses the `lazy` delegate to initialize dependencies only when accessed.
 *
 * Usage:
 * - Extend `BaseApplication` to define specific application behaviors for different environments.
 * - Use the `dependencyProvider` property to access application-wide dependencies.
 */
open class BaseApplication : Application() {
  /**
   * Provides the application's dependency provider. By default, this is an instance of
   * `AppDependencyProvider` which supplies production-ready dependencies.
   */
  open val dependencyProvider: DependencyProvider by lazy { AppDependencyProvider }
}
