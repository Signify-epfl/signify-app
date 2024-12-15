package com.github.se.signify.model.common

import android.content.Context
import android.content.res.Configuration
import com.github.se.signify.R
import com.github.se.signify.model.profile.stats.StatsViewModel
import java.util.Locale
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.math.mean

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
/**
 * Convert a time in millisecond to second
 *
 * @param time in millisecond
 * @return the time in second
 * @throws IllegalArgumentException if the time is negative.
 */
fun timeConversion(time: Long): Double {
  require(time >= 0) { "Time must be >= 0" }
  return time / 1000.0
}
/**
 * Function that calculate the time taken to complete an exercise and call the update for the stats
 * repository
 *
 * @param statsViewModel used to update the stats repository
 * @param startTimestamp to calculate the time taken to do the exercise
 * @return the new startTimestamp
 */
fun calculateTimePerLetter(statsViewModel: StatsViewModel, startTimestamp: Long): Long {
  val endTimestamp = System.currentTimeMillis()
  statsViewModel.updateTimePerLetter(endTimestamp - startTimestamp)
  return endTimestamp
}

const val TimePerLetter = "TimePerLetter"
const val TimePerLetterIndex = "TimePerLetterIndex"
const val TimePerLetterAverage = "TimePerLetterAverage"
/**
 * Function that create a DataFrame for time tracking.
 *
 * @param timePerLetter The list of time in second
 */
fun createDataFrame(timePerLetter: List<Double>): DataFrame<*> {
  val timePerLetterIndex = List(timePerLetter.size) { index -> index }
  val timePerLetterAverage = List(timePerLetter.size) { timePerLetter.mean() }
  return dataFrameOf(
    TimePerLetter to timePerLetter,
    TimePerLetterIndex to timePerLetterIndex,
    TimePerLetterAverage to timePerLetterAverage)
}
