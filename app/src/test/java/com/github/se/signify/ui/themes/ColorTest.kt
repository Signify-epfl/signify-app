package com.github.se.signify.ui.themes

import com.github.se.signify.ui.theme.Background
import com.github.se.signify.ui.theme.BackgroundDark
import com.github.se.signify.ui.theme.Error
import com.github.se.signify.ui.theme.ErrorContainer
import com.github.se.signify.ui.theme.ErrorContainerDark
import com.github.se.signify.ui.theme.ErrorDark
import com.github.se.signify.ui.theme.OnBackground
import com.github.se.signify.ui.theme.OnBackgroundDark
import com.github.se.signify.ui.theme.OnError
import com.github.se.signify.ui.theme.OnErrorContainer
import com.github.se.signify.ui.theme.OnErrorContainerDark
import com.github.se.signify.ui.theme.OnErrorDark
import com.github.se.signify.ui.theme.OnPrimary
import com.github.se.signify.ui.theme.OnPrimaryContainer
import com.github.se.signify.ui.theme.OnPrimaryContainerDark
import com.github.se.signify.ui.theme.OnPrimaryDark
import com.github.se.signify.ui.theme.OnSecondary
import com.github.se.signify.ui.theme.OnSecondaryContainer
import com.github.se.signify.ui.theme.OnSecondaryContainerDark
import com.github.se.signify.ui.theme.OnSecondaryDark
import com.github.se.signify.ui.theme.OnSurface
import com.github.se.signify.ui.theme.OnSurfaceDark
import com.github.se.signify.ui.theme.OnSurfaceVariant
import com.github.se.signify.ui.theme.OnSurfaceVariantDark
import com.github.se.signify.ui.theme.OnTertiary
import com.github.se.signify.ui.theme.OnTertiaryContainer
import com.github.se.signify.ui.theme.OnTertiaryContainerDark
import com.github.se.signify.ui.theme.OnTertiaryDark
import com.github.se.signify.ui.theme.Outline
import com.github.se.signify.ui.theme.OutlineDark
import com.github.se.signify.ui.theme.Primary
import com.github.se.signify.ui.theme.PrimaryContainer
import com.github.se.signify.ui.theme.PrimaryContainerDark
import com.github.se.signify.ui.theme.PrimaryDark
import com.github.se.signify.ui.theme.Secondary
import com.github.se.signify.ui.theme.SecondaryContainer
import com.github.se.signify.ui.theme.SecondaryContainerDark
import com.github.se.signify.ui.theme.SecondaryDark
import com.github.se.signify.ui.theme.Surface
import com.github.se.signify.ui.theme.SurfaceDark
import com.github.se.signify.ui.theme.SurfaceVariant
import com.github.se.signify.ui.theme.SurfaceVariantDark
import com.github.se.signify.ui.theme.Tertiary
import com.github.se.signify.ui.theme.TertiaryContainer
import com.github.se.signify.ui.theme.TertiaryContainerDark
import com.github.se.signify.ui.theme.TertiaryDark
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
