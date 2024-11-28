package com.github.se.signify.ui.themes

import com.github.se.signify.ui.theme.*
import org.junit.Assert.assertTrue
import org.junit.Test

class ColorTest {

  private val predefinedColors =
      setOf(
          Primary,
          OnPrimary,
          PrimaryContainer,
          OnPrimaryContainer,
          Secondary,
          OnSecondary,
          SecondaryContainer,
          OnSecondaryContainer,
          Tertiary,
          OnTertiary,
          TertiaryContainer,
          OnTertiaryContainer,
          Error,
          OnError,
          ErrorContainer,
          OnErrorContainer,
          Background,
          OnBackground,
          Surface,
          OnSurface,
          SurfaceVariant,
          OnSurfaceVariant,
          Outline,
          PrimaryDark,
          OnPrimaryDark,
          PrimaryContainerDark,
          OnPrimaryContainerDark,
          SecondaryDark,
          OnSecondaryDark,
          SecondaryContainerDark,
          OnSecondaryContainerDark,
          TertiaryDark,
          OnTertiaryDark,
          TertiaryContainerDark,
          OnTertiaryContainerDark,
          ErrorDark,
          OnErrorDark,
          ErrorContainerDark,
          OnErrorContainerDark,
          BackgroundDark,
          OnBackgroundDark,
          SurfaceDark,
          OnSurfaceDark,
          SurfaceVariantDark,
          OnSurfaceVariantDark,
          OutlineDark)

  @Test
  fun testLightThemeColors() {
    assertTrue(predefinedColors.contains(Primary))
    assertTrue(predefinedColors.contains(OnPrimary))
    assertTrue(predefinedColors.contains(Secondary))
    assertTrue(predefinedColors.contains(OnSecondary))
    assertTrue(predefinedColors.contains(Tertiary))
    assertTrue(predefinedColors.contains(OnTertiary))
    assertTrue(predefinedColors.contains(Background))
    assertTrue(predefinedColors.contains(OnBackground))
    assertTrue(predefinedColors.contains(Surface))
    assertTrue(predefinedColors.contains(OnSurface))
    assertTrue(predefinedColors.contains(Error))
    assertTrue(predefinedColors.contains(OnError))
    assertTrue(predefinedColors.contains(ErrorContainer))
    assertTrue(predefinedColors.contains(OnErrorContainer))
    assertTrue(predefinedColors.contains(Outline))
    assertTrue(predefinedColors.contains(SurfaceVariant))
    assertTrue(predefinedColors.contains(OnSurfaceVariant))
  }

  @Test
  fun testDarkThemeColors() {
    assertTrue(predefinedColors.contains(PrimaryDark))
    assertTrue(predefinedColors.contains(OnPrimaryDark))
    assertTrue(predefinedColors.contains(SecondaryDark))
    assertTrue(predefinedColors.contains(OnSecondaryDark))
    assertTrue(predefinedColors.contains(TertiaryDark))
    assertTrue(predefinedColors.contains(OnTertiaryDark))
    assertTrue(predefinedColors.contains(BackgroundDark))
    assertTrue(predefinedColors.contains(OnBackgroundDark))
    assertTrue(predefinedColors.contains(SurfaceDark))
    assertTrue(predefinedColors.contains(OnSurfaceDark))
    assertTrue(predefinedColors.contains(ErrorDark))
    assertTrue(predefinedColors.contains(OnErrorDark))
    assertTrue(predefinedColors.contains(ErrorContainerDark))
    assertTrue(predefinedColors.contains(OnErrorContainerDark))
    assertTrue(predefinedColors.contains(OutlineDark))
    assertTrue(predefinedColors.contains(SurfaceVariantDark))
    assertTrue(predefinedColors.contains(OnSurfaceVariantDark))
  }
}
