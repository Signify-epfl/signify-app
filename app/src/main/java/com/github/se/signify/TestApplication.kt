package com.github.se.signify

import com.github.se.signify.model.di.DependencyProvider
import com.github.se.signify.model.di.MockDependencyProvider

class TestApplication : BaseApplication() {
  override val dependencyProvider: DependencyProvider by lazy { MockDependencyProvider }
}
