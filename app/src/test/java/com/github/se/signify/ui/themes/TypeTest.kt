package com.github.se.signify.ui.themes

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.se.signify.ui.theme.Typography
import org.junit.Assert.assertEquals
import org.junit.Test

class TypeTest {
  @Test
  fun testBodyLargeTypographyProperties() {
    // Check that bodyLarge has the correct properties
    with(Typography.bodyLarge) {
      assertEquals(FontWeight.Normal, fontWeight)
      assertEquals(16.sp, fontSize)
      assertEquals(24.sp, lineHeight)
      assertEquals(0.5.sp, letterSpacing)
    }
  }
}
