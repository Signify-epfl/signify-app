package com.github.se.signify.model

import android.content.Context
import android.content.res.Configuration
import com.github.se.signify.R
import java.util.Locale

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
/**
 * Updates the application's language dynamically by changing the locale.
 *
 * This function sets the application's locale to the specified language code and updates the
 * configuration for the application's resources to reflect the language change.
 *
 * @param context The context of the application or activity. This is required to access the
 *   application's resources and configuration.
 * @param languageCode The language code for the desired language (e.g., "en" for English, "fr" for
 *   French). This should be a valid ISO 639-1 language code.
 *
 * Usage Example:
 * ```
 * updateLanguage(context, "fr") // Switches the application language to French
 * updateLanguage(context, "en") // Switches the application language to English
 * *
 */
fun updateLanguage(context: Context, languageCode: String) {
  require(listOf("en", "fr").contains(languageCode)) { "Unsupported language code: $languageCode" }
  val locale = Locale(languageCode)
  Locale.setDefault(locale)
  val config = Configuration()
  config.locale = locale
  context.resources.updateConfiguration(config, context.resources.displayMetrics)
}
