package com.github.se.signify.model.home.quest

private val failureException: Exception = Exception("Simulated failure")

class MockQuestRepository : QuestRepository {

  // Internal state to simulate failure or success
  private var shouldFail: Boolean = false

  // Pre-existing list of mock quests
  private val mockedQuests =
      mutableListOf<Quest>(
          Quest(
              index = "1",
              title = "Hello",
              description = "Learn how to greet someone in ASL",
              videoPath = "android.resource://com.github.se.signify/raw/hello"),
          Quest(
              index = "2",
              title = "Goodbye",
              description = "Learn how to say goodbye in ASL",
              videoPath = "android.resource://com.github.se.signify/raw/goodbye"),
      )

  // Function to simulate a success scenario
  fun succeed() {
    shouldFail = false
  }

  // Function to simulate a failure scenario
  fun fail() {
    shouldFail = true
  }

  // Initialize repository
  override fun init(onSuccess: () -> Unit) {
    if (!checkFailure {}) return
    onSuccess()
  }

  // Return daily quests or simulate failure
  override fun getDailyQuest(onSuccess: (List<Quest>) -> Unit, onFailure: (Exception) -> Unit) {
    if (!checkFailure(onFailure)) return
    onSuccess(mockedQuests)
  }

  // Helper function to check for failure
  private fun checkFailure(onFailure: (Exception) -> Unit): Boolean {
    if (shouldFail) {
      onFailure(failureException)
      return false
    }
    return true
  }
}
