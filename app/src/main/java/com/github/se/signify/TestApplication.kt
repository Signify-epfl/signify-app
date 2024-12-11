package com.github.se.signify

import com.github.se.signify.model.dependencyInjection.DependencyProvider
import com.github.se.signify.model.dependencyInjection.MockDependencyProvider

/**
 * `TestApplication` is a specialized implementation of the `BaseApplication` class designed
 * specifically for testing purposes.
 *
 * This class overrides the default `DependencyProvider` with `MockDependencyProvider` to supply
 * mock dependencies during test execution, enabling test isolation and reducing reliance on real
 * services.
 *
 * Key Features:
 * - Uses `MockDependencyProvider` to provide mocked versions of services and dependencies.
 * - Ensures seamless testing by decoupling the app from production services.
 * - Supports test environments configured with a custom test runner.
 *
 * Usage: `TestApplication` is automatically used in test scenarios where a custom
 * `DependencyProvider` setup is required.
 */
class TestApplication : BaseApplication() {
  /**
   * Overrides the default `DependencyProvider` to use `MockDependencyProvider`. This ensures all
   * dependencies are mocked and isolated for testing purposes.
   */
  override val dependencyProvider: DependencyProvider by lazy { MockDependencyProvider }
}
