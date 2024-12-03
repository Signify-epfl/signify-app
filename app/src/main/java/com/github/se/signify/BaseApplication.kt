package com.github.se.signify

import android.app.Application
import com.github.se.signify.model.di.AppDependencyProvider
import com.github.se.signify.model.di.DependencyProvider

open class BaseApplication : Application() {
  open val dependencyProvider: DependencyProvider by lazy { AppDependencyProvider }

  companion object {
    lateinit var instance: BaseApplication
      private set
  }

  override fun onCreate() {
    super.onCreate()
    instance = this
  }
}
