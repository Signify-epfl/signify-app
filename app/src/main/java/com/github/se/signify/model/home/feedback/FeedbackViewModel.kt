package com.github.se.signify.model.home.feedback

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.se.signify.model.authentication.UserSession

open class FeedbackViewModel(
    private val userSession: UserSession,
    private val feedbackRepository: FeedbackRepository,
) : ViewModel() {

  private val logTag = "FeedbackViewModel"

  // Function to save feedback in Firestore using FeedbackRepository
  fun saveFeedback(type: String, title: String, description: String, rating: Int) {
    feedbackRepository.saveFeedback(
        userSession.getUserId()!!,
        type,
        title,
        description,
        rating,
        onSuccess = { Log.d(logTag, "Feedback sent successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to send feedback request: ${e.message}") })
  }

  companion object {
    fun factory(
        userSession: UserSession,
        repository: FeedbackRepository
    ): ViewModelProvider.Factory {
      return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return FeedbackViewModel(userSession, repository) as T
        }
      }
    }
  }
}
