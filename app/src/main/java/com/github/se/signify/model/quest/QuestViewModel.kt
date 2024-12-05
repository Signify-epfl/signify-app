package com.github.se.signify.model.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestViewModel(
    private val repository: QuestRepository,
) : ViewModel() {

  private val quest_ = MutableStateFlow<List<Quest>>(emptyList())
  val quest: StateFlow<List<Quest>> = quest_.asStateFlow()

  init {
    fetchAllQuests()
  }

  fun fetchAllQuests() {
    CoroutineScope(Dispatchers.IO).launch {
      repository.getQuests(
          onSuccess = { quests -> quest_.value = quests },
          onFailure = { exception -> exception.printStackTrace() })
    }
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
}
