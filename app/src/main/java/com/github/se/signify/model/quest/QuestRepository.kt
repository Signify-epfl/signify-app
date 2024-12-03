package com.github.se.signify.model.quest

interface QuestRepository {
  fun getQuests(onSuccess: (List<Quest>) -> Unit, onFailure: (Exception) -> Unit)
}
