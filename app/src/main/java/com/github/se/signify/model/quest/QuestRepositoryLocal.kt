package com.github.se.signify.model.quest

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class QuestRepositoryLocal(private val context: Context) : QuestRepository {

  override fun getQuests(onSuccess: (List<Quest>) -> Unit, onFailure: (Exception) -> Unit) {
    try {
      // Load the JSON file from assets
      val inputStream = context.assets.open("quests.json")
      val json = inputStream.bufferedReader().use { it.readText() }

      // Parse the JSON into a list of Quest objects
      val type = object : TypeToken<List<Quest>>() {}.type
      val quests: List<Quest> = Gson().fromJson(json, type)

      // Return the quests
      onSuccess(quests)
    } catch (e: Exception) {
      // Handle any errors that occur
      onFailure(e)
    }
  }
}
