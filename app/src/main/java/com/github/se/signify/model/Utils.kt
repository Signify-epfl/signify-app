package com.github.se.signify.model

import com.github.se.signify.R

/**
 * Retrieves the drawable resource ID for a letter's image.
 *
 * @param letter The character representing the image resource, typically an alphabet letter.
 * @return The integer ID of the drawable resource of the corresponding image.
 * @throws NoSuchFieldException if the picture name does not match any drawable in `R.drawable`.
 */
fun getImageResId(letter: Char): Int {
  val resName = "pic_${letter.lowercaseChar()}"
  return R.drawable::class.java.getDeclaredField(resName).getInt(null)
}

/**
 * Retrieves the drawable resource ID for a letter's icon.
 *
 * @param letter The letter representing the icon resource.
 * @return The integer ID of the drawable resource of the corresponding icon.
 * @throws NoSuchFieldException if the letter does not match any drawable in `R.drawable`.
 */
fun getIconResId(letter: Char): Int {
  val resName = "letter_${letter.lowercaseChar()}"
  return R.drawable::class.java.getDeclaredField(resName).getInt(null)
}

/**
 * Retrieves the string resource ID for the tip associated with a letter.
 *
 * @param letter The letter representing the tip resource.
 * @return The integer ID of the string resource of the corresponding tip.
 * @throws NoSuchFieldException if the tip letter does not match any resource in `R.string`.
 */
fun getTipResId(letter: Char): Int {
  return R.string::class.java.getDeclaredField("tip_${letter.lowercaseChar()}").getInt(null)
}
