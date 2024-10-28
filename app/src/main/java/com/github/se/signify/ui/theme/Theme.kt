package com.github.se.signify.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.github.se.signify.R

@Composable
fun SignifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
  val LightColorScheme =
      lightColorScheme(
          primary = colorResource(id = R.color.blue), secondary = colorResource(id = R.color.white)
          /* Other default colors to override
          background = Color(0xFFFFFBFE),
          surface = Color(0xFFFFFBFE),
          onPrimary = Color.White,
          onSecondary = Color.White,
          onTertiary = Color.White,
          onBackground = Color(0xFF1C1B1F),
          onSurface = Color(0xFF1C1B1F),
          */
          )

  /*
    For future use if we implement a dark theme
    val DarkColorScheme =
        darkColorScheme(primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80)
  val colorScheme =
      when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
      }
     */

  MaterialTheme(colorScheme = LightColorScheme, typography = Typography, content = content)
}
