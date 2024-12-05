package com.github.se.signify.model.feedback

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
  OTHER("Other")
}
