package com.github.se.signify.model.feedback

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore

open class FeedbackViewModel(private val feedbackRepository: FeedbackRepository) : ViewModel() {

  private val logTag = "FeedbackViewModel"

  // Function to save feedback in Firestore using FeedbackRepository
  fun saveFeedback(uid: String, type: String, title: String, description: String, rating: Int) {
    feedbackRepository.saveFeedback(
        uid,
        type,
        title,
        description,
        rating,
        onSuccess = { Log.d(logTag, "Feedback sent successfully.") },
        onFailure = { e -> Log.e(logTag, "Failed to send feedback request: ${e.message}") })
  }

  // Companion object for creating the factory for FeedbackViewModel
  companion object {
    val Factory: ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
          @Suppress("UNCHECKED_CAST")
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FeedbackViewModel(FeedbackRepositoryFireStore(FirebaseFirestore.getInstance()))
                as T
          }
        }
  }
}
