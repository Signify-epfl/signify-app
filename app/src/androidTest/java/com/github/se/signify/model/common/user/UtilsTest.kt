package com.github.se.signify.model.common.user

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.github.se.signify.model.common.updateLanguage
import java.util.Locale
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UtilsTest {

  private lateinit var context: Context

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
  }

  @Test
  fun updateLanguage_updatesLocaleCorrectly() {
    val initialLocale = Locale.getDefault()
    val targetLanguageCode = "fr"
    val targetLocale = Locale(targetLanguageCode)

    // Call the function to update the language
    updateLanguage(context, targetLanguageCode)

    // Verify that the app's locale has been updated
    val updatedLocale = context.resources.configuration.locales[0]
    Assert.assertEquals(
        "The app's locale should be updated to the target language", targetLocale, updatedLocale)

    // Cleanup: Restore the initial locale
    updateLanguage(context, initialLocale.language)
  }

  @Test
  fun updateLanguage_throwsErrorWhenInvalidLanguageCode() {
    val targetLanguageCode = "DE"
    // Call the function to update the language
    Assert.assertThrows(IllegalArgumentException::class.java) {
      updateLanguage(context, targetLanguageCode)
    }
  }
}
