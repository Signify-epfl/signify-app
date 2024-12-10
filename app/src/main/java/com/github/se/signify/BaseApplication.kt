package com.github.se.signify

import android.app.Application
import com.github.se.signify.model.dependencyInjection.AppDependencyProvider
import com.github.se.signify.model.dependencyInjection.DependencyProvider

open class BaseApplication : Application() {
  open val dependencyProvider: DependencyProvider by lazy { AppDependencyProvider }
}
