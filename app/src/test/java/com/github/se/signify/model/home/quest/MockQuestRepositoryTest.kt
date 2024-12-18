package com.github.se.signify.model.home.quest

import android.os.Looper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class MockQuestRepositoryTest {

  private lateinit var mockQuestRepository: MockQuestRepository

  private val doNotFail: (Exception) -> Unit = { fail("Should not fail") }
  private val doNotSucceed: () -> Unit = { fail("Should not succeed") }
  private val doNotSucceedAny: (Any?) -> Unit = { doNotSucceed() }

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

  @Before
  fun setUp() {
    mockQuestRepository = MockQuestRepository()
    mockQuestRepository.succeed() // Default to succeed
  }

  @Test
  fun succeedWorks() {
    var successCount = 0
    val onSuccess: () -> Unit = { successCount += 1 }

    mockQuestRepository.init(onSuccess)

    mockQuestRepository.getDailyQuest(
        onSuccess = { quests ->
          successCount += 1
          assertEquals(2, quests.size)
          assertEquals(mockedQuests, quests)
        },
        onFailure = doNotFail)

    shadowOf(Looper.getMainLooper()).idle()
    assertEquals(2, successCount) // Two success callbacks should have fired
  }

  @Test
  fun failWorks() {
    var failureCount = 0
    val onFailure: (Exception) -> Unit = { failureCount += 1 }

    mockQuestRepository.fail() // Simulate failure

    mockQuestRepository.getDailyQuest(onSuccess = doNotSucceedAny, onFailure = onFailure)

    shadowOf(Looper.getMainLooper()).idle()
    assertEquals(1, failureCount) // One failure callback should have fired
  }

  @Test
  fun getDailyQuestReturnsCorrectData() {
    mockQuestRepository.getDailyQuest(
        onSuccess = { quests ->
          assertEquals(2, quests.size)
          assertTrue(quests.containsAll(mockedQuests))
        },
        onFailure = doNotFail)
  }
}
