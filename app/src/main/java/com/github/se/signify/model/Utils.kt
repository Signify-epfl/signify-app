package com.github.se.signify.model

import com.github.se.signify.R

/**
 * Retrieves the drawable resource ID for an image based on a provided letter. This function
 * constructs the resource name using the letter (e.g., "pic_a" for 'A') and uses reflection to
 * access the corresponding drawable resource ID in `R.drawable`.
 *
 * @param letter The character representing the image resource, typically an alphabet letter.
 * @return The integer ID of the drawable resource corresponding to the constructed resource name.
 * @throws NoSuchFieldException if the resource name does not match any drawable in `R.drawable`.
 */
fun getImageResId(letter: Char): Int {
  val resName = "pic_${letter.lowercaseChar()}"
  return R.drawable::class.java.getDeclaredField(resName).getInt(null)
}

/**
 * Retrieves the drawable resource ID for an icon based on a provided letter. Constructs the
 * resource name using the letter (e.g., "letter_a" for 'A') and uses reflection to find the
 * corresponding drawable resource ID in `R.drawable`.
 *
 * @param letter The character representing the icon resource, typically an alphabet letter.
 * @return The integer ID of the drawable resource for the icon corresponding to the constructed
 *   resource name.
 * @throws NoSuchFieldException if the resource name does not match any drawable in `R.drawable`.
 */
fun getIconResId(letter: Char): Int {
  val resName = "letter_${letter.lowercaseChar()}"
  return R.drawable::class.java.getDeclaredField(resName).getInt(null)
}

/**
 * Retrieves the string resource ID for a tip based on the provided letter. Maps each letter from
 * 'A' to 'Z' to a specific string resource in `R.string` (e.g., 'A' maps to `R.string.tip_a`). If
 * the letter does not match any case, it defaults to `R.string.tip_a`.
 *
 * @param letter The character representing the tip resource, typically an uppercase alphabet
 *   letter.
 * @return The integer ID of the string resource for the corresponding tip.
 */
fun getTipResId(letter: Char): Int {
  return R.string::class.java.getDeclaredField("tip_${letter.lowercaseChar()}").getInt(null)
}
