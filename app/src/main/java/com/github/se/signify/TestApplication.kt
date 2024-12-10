package com.github.se.signify

import com.github.se.signify.model.dependencyInjection.DependencyProvider
import com.github.se.signify.model.dependencyInjection.MockDependencyProvider

class TestApplication : BaseApplication() {
  override val dependencyProvider: DependencyProvider by lazy { MockDependencyProvider }
}
