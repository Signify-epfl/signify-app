package com.github.se.signify.model.home.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class QuestViewModel(
    private val repository: QuestRepository,
) : ViewModel() {

  // For now fetch all the quests
  private val quest_ = MutableStateFlow<List<Quest>>(emptyList())
  val quest: StateFlow<List<Quest>> = quest_.asStateFlow()

  init {
    repository.init { getDailyQuest() }
  }

  companion object {
    fun factory(repository: QuestRepository): ViewModelProvider.Factory {
      return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return QuestViewModel(repository) as T
        }
      }
    }
  }

  fun getDailyQuest() {
    repository.getDailyQuest(onSuccess = { quest_.value = it }, onFailure = {})
  }
}
