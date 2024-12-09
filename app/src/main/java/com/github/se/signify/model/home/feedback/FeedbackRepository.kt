package com.github.se.signify.model.home.feedback

interface FeedbackRepository {
  fun saveFeedback(
      uid: String,
      type: String,
      title: String,
      description: String,
      rating: Int,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  )
}
