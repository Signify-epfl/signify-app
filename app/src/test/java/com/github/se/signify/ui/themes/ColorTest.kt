package com.github.se.signify.ui.themes

import androidx.compose.ui.graphics.Color
import com.github.se.signify.ui.theme.md_theme_dark_background
import com.github.se.signify.ui.theme.md_theme_dark_error
import com.github.se.signify.ui.theme.md_theme_dark_errorContainer
import com.github.se.signify.ui.theme.md_theme_dark_inverseOnSurface
import com.github.se.signify.ui.theme.md_theme_dark_inverseSurface
import com.github.se.signify.ui.theme.md_theme_dark_onBackground
import com.github.se.signify.ui.theme.md_theme_dark_onError
import com.github.se.signify.ui.theme.md_theme_dark_onErrorContainer
import com.github.se.signify.ui.theme.md_theme_dark_onPrimary
import com.github.se.signify.ui.theme.md_theme_dark_onSecondary
import com.github.se.signify.ui.theme.md_theme_dark_onSurface
import com.github.se.signify.ui.theme.md_theme_dark_onTertiary
import com.github.se.signify.ui.theme.md_theme_dark_outline
import com.github.se.signify.ui.theme.md_theme_dark_outlineVariant
import com.github.se.signify.ui.theme.md_theme_dark_primary
import com.github.se.signify.ui.theme.md_theme_dark_secondary
import com.github.se.signify.ui.theme.md_theme_dark_surface
import com.github.se.signify.ui.theme.md_theme_dark_tertiary
import com.github.se.signify.ui.theme.md_theme_light_background
import com.github.se.signify.ui.theme.md_theme_light_error
import com.github.se.signify.ui.theme.md_theme_light_errorContainer
import com.github.se.signify.ui.theme.md_theme_light_inverseOnSurface
import com.github.se.signify.ui.theme.md_theme_light_inverseSurface
import com.github.se.signify.ui.theme.md_theme_light_onBackground
import com.github.se.signify.ui.theme.md_theme_light_onError
import com.github.se.signify.ui.theme.md_theme_light_onErrorContainer
import com.github.se.signify.ui.theme.md_theme_light_onPrimary
import com.github.se.signify.ui.theme.md_theme_light_onSecondary
import com.github.se.signify.ui.theme.md_theme_light_onSurface
import com.github.se.signify.ui.theme.md_theme_light_onTertiary
import com.github.se.signify.ui.theme.md_theme_light_outline
import com.github.se.signify.ui.theme.md_theme_light_outlineVariant
import com.github.se.signify.ui.theme.md_theme_light_primary
import com.github.se.signify.ui.theme.md_theme_light_secondary
import com.github.se.signify.ui.theme.md_theme_light_surface
import com.github.se.signify.ui.theme.md_theme_light_tertiary
import org.junit.Assert.assertTrue
import org.junit.Test

class ColorTest {

  private val predefinedColors =
      setOf(
          Color(0xFF05A9FB), // blue
          Color(0xFF000000), // black
          Color(0xFFD32F2F), // red
          Color(0xFF2E7D32), // green
          Color(0xFFFFFFFF), // white
          Color(0xFF333333), // darkGray
          Color(0xFFE5B6B1) // lightRed
          )

  @Test
  fun testLightThemeColors() {
    assertTrue(predefinedColors.contains(md_theme_light_primary))
    assertTrue(predefinedColors.contains(md_theme_light_onPrimary))
    assertTrue(predefinedColors.contains(md_theme_light_secondary))
    assertTrue(predefinedColors.contains(md_theme_light_onSecondary))
    assertTrue(predefinedColors.contains(md_theme_light_tertiary))
    assertTrue(predefinedColors.contains(md_theme_light_onTertiary))
    assertTrue(predefinedColors.contains(md_theme_light_background))
    assertTrue(predefinedColors.contains(md_theme_light_onBackground))
    assertTrue(predefinedColors.contains(md_theme_light_surface))
    assertTrue(predefinedColors.contains(md_theme_light_onSurface))
    assertTrue(predefinedColors.contains(md_theme_light_inverseSurface))
    assertTrue(predefinedColors.contains(md_theme_light_inverseOnSurface))
    assertTrue(predefinedColors.contains(md_theme_light_error))
    assertTrue(predefinedColors.contains(md_theme_light_onError))
    assertTrue(predefinedColors.contains(md_theme_light_errorContainer))
    assertTrue(predefinedColors.contains(md_theme_light_onErrorContainer))
    assertTrue(predefinedColors.contains(md_theme_light_outline))
    assertTrue(predefinedColors.contains(md_theme_light_outlineVariant))
  }

  @Test
  fun testDarkThemeColors() {
    assertTrue(predefinedColors.contains(md_theme_dark_primary))
    assertTrue(predefinedColors.contains(md_theme_dark_onPrimary))
    assertTrue(predefinedColors.contains(md_theme_dark_secondary))
    assertTrue(predefinedColors.contains(md_theme_dark_onSecondary))
    assertTrue(predefinedColors.contains(md_theme_dark_tertiary))
    assertTrue(predefinedColors.contains(md_theme_dark_onTertiary))
    assertTrue(predefinedColors.contains(md_theme_dark_background))
    assertTrue(predefinedColors.contains(md_theme_dark_onBackground))
    assertTrue(predefinedColors.contains(md_theme_dark_surface))
    assertTrue(predefinedColors.contains(md_theme_dark_onSurface))
    assertTrue(predefinedColors.contains(md_theme_dark_inverseSurface))
    assertTrue(predefinedColors.contains(md_theme_dark_inverseOnSurface))
    assertTrue(predefinedColors.contains(md_theme_dark_error))
    assertTrue(predefinedColors.contains(md_theme_dark_onError))
    assertTrue(predefinedColors.contains(md_theme_dark_errorContainer))
    assertTrue(predefinedColors.contains(md_theme_dark_onErrorContainer))
    assertTrue(predefinedColors.contains(md_theme_dark_outline))
    assertTrue(predefinedColors.contains(md_theme_dark_outlineVariant))
  }
}
