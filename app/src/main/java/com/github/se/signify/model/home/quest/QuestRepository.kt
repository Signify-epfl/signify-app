package com.github.se.signify.model.quest

interface QuestRepository {

  fun init(onSuccess: () -> Unit)

  fun getDailyQuest(onSuccess: (List<Quest>) -> Unit, onFailure: (Exception) -> Unit)
}
