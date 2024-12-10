package com.github.se.signify.model.home.feedback

import android.content.Context
import com.github.se.signify.R

data class Feedback(
    val uid: String = "",
    val type: String = "",
    val title: String = "",
    val description: String = "",
    val rating: Int = 0
)

enum class FeedbackOption(val category: String) {
  BUG_REPORT("Bug Report"),
  FEATURE_SUGGESTION("Feature Suggestion"),
  QUESTION("Question"),
  OTHER("Other");

  fun getText(context: Context): String {
    return when (this) {
      BUG_REPORT -> context.getString(R.string.bug_report_text)
      FEATURE_SUGGESTION -> context.getString(R.string.feature_suggestion_text)
      QUESTION -> context.getString(R.string.question_text)
      OTHER -> context.getString(R.string.other_text)
    }
  }
}
