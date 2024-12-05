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
 * A function that returns the corresponding icon for the letter.
 *
 * @param letter The character from which we want the icon.
 */
fun getLetterIconResId(letter: Char): Int {
  return when (letter.lowercaseChar()) {
    'a' -> R.drawable.letter_a
    'b' -> R.drawable.letter_b
    'c' -> R.drawable.letter_c
    'd' -> R.drawable.letter_d
    'e' -> R.drawable.letter_e
    'f' -> R.drawable.letter_f
    'g' -> R.drawable.letter_g
    'h' -> R.drawable.letter_h
    'i' -> R.drawable.letter_i
    'j' -> R.drawable.letter_j
    'k' -> R.drawable.letter_k
    'l' -> R.drawable.letter_l
    'm' -> R.drawable.letter_m
    'n' -> R.drawable.letter_n
    'o' -> R.drawable.letter_o
    'p' -> R.drawable.letter_p
    'q' -> R.drawable.letter_q
    'r' -> R.drawable.letter_r
    's' -> R.drawable.letter_s
    't' -> R.drawable.letter_t
    'u' -> R.drawable.letter_u
    'v' -> R.drawable.letter_v
    'w' -> R.drawable.letter_w
    'x' -> R.drawable.letter_x
    'y' -> R.drawable.letter_y
    'z' -> R.drawable.letter_z
    else -> R.drawable.letter_a // Default case, just in case an unexpected value is passed
  }
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
  return when (letter.lowercaseChar()) {
    'a' -> R.string.tip_a
    'b' -> R.string.tip_b
    'c' -> R.string.tip_c
    'd' -> R.string.tip_d
    'e' -> R.string.tip_e
    'f' -> R.string.tip_f
    'g' -> R.string.tip_g
    'h' -> R.string.tip_h
    'i' -> R.string.tip_i
    'j' -> R.string.tip_j
    'k' -> R.string.tip_k
    'l' -> R.string.tip_l
    'm' -> R.string.tip_m
    'n' -> R.string.tip_n
    'o' -> R.string.tip_o
    'p' -> R.string.tip_p
    'q' -> R.string.tip_q
    'r' -> R.string.tip_r
    's' -> R.string.tip_s
    't' -> R.string.tip_t
    'u' -> R.string.tip_u
    'v' -> R.string.tip_v
    'w' -> R.string.tip_w
    'x' -> R.string.tip_x
    'y' -> R.string.tip_y
    'z' -> R.string.tip_z
    else -> R.string.tip_a
  }
}
