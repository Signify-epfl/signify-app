package com.github.se.bootcamp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.github.se.bootcamp.ui.Greeting
import com.github.se.bootcamp.ui.theme.BootcampTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      BootcampTheme { Surface(modifier = Modifier.fillMaxSize()) { Greeting(name = "Android") } }
    }
  }
}
