package com.github.se.signify.model.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class QuestViewModel(private val repository: QuestRepository) : ViewModel() {

  // For now fetch all the quests
  private val quest_ = MutableStateFlow<List<Quest>>(emptyList())
  val quest: StateFlow<List<Quest>> = quest_.asStateFlow()

  init {
    repository.init { getDailyQuest() }
  }

  companion object {
    val Factory: ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
          @Suppress("UNCHECKED_CAST")
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return QuestViewModel(QuestRepositoryFireStore(Firebase.firestore)) as T
          }
        }
  }

  fun getDailyQuest() {
    repository.getDailyQuest(onSuccess = { quest_.value = it }, onFailure = {})
  }
}
