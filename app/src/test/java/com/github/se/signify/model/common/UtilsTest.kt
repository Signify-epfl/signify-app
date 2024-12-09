package com.github.se.signify.model.common

import com.github.se.signify.R
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UtilsTest {
  @Test
  fun getImageResId_returnsCorrectDrawableResource() {
    val letter = 'A'
    val expectedResId = R.drawable.pic_a
    val actualResId = getImageResId(letter)
    assertEquals(
        "getImageResId should return the correct drawable resource ID for 'A'",
        expectedResId,
        actualResId)
  }

  @Test
  fun getIconResId_returnsCorrectDrawableResource() {
    val letter = 'A'
    val expectedResId = R.drawable.letter_a
    val actualResId = getIconResId(letter)
    assertEquals(
        "getIconResId should return the correct drawable resource ID for 'A'",
        expectedResId,
        actualResId)
  }

  @Test
  fun getTipResId_returnsCorrectStringResourceForAllLetters() {
    val expectedResourceMap =
        mapOf(
            'A' to R.string.tip_a,
            'B' to R.string.tip_b,
            'C' to R.string.tip_c,
            'D' to R.string.tip_d,
            'E' to R.string.tip_e,
            'F' to R.string.tip_f,
            'G' to R.string.tip_g,
            'H' to R.string.tip_h,
            'I' to R.string.tip_i,
            'J' to R.string.tip_j,
            'K' to R.string.tip_k,
            'L' to R.string.tip_l,
            'M' to R.string.tip_m,
            'N' to R.string.tip_n,
            'O' to R.string.tip_o,
            'P' to R.string.tip_p,
            'Q' to R.string.tip_q,
            'R' to R.string.tip_r,
            'S' to R.string.tip_s,
            'T' to R.string.tip_t,
            'U' to R.string.tip_u,
            'V' to R.string.tip_v,
            'W' to R.string.tip_w,
            'X' to R.string.tip_x,
            'Y' to R.string.tip_y,
            'Z' to R.string.tip_z)
    expectedResourceMap.forEach { (letter, expectedResId) ->
      val actualResId = getTipResId(letter)
      assertEquals(
          "getTipResId should return the correct string resource ID for '$letter'",
          expectedResId,
          actualResId)
    }
  }
}
