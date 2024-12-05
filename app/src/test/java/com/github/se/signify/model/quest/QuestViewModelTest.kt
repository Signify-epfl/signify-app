package com.github.se.signify.model.quest

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalCoroutinesApi::class)
class QuestViewModelTest {

  private lateinit var repository: QuestRepository
  private lateinit var viewModel: QuestViewModel

  @Before
  fun setUp() {
    repository = mock(QuestRepository::class.java)
    viewModel = QuestViewModel(repository)
  }

  @Test
  fun fetchAllQuests_updates_quest_flow_on_success() = runTest {
    // Arrange
    val mockQuests =
        listOf(
            Quest("1", "Title 1", "Description 1", "Path 1"),
            Quest("2", "Title 2", "Description 2", "Path 2"))
    doAnswer {
          val onSuccess = it.arguments[0] as (List<Quest>) -> Unit
          onSuccess(mockQuests)
        }
        .`when`(repository)
        .getQuests(anyOrNull(), anyOrNull())

    // Act
    viewModel.fetchAllQuests()
    advanceUntilIdle()

    // Assert
    assertEquals(mockQuests, viewModel.quest.first())
  }

  @Test
  fun fetchAllQuests_does_not_update_quest_flow_on_failure() = runTest {
    // Arrange
    val mockException = Exception("Failed to fetch quests")
    doAnswer {
          val onFailure = it.arguments[1] as (Exception) -> Unit
          onFailure(mockException)
        }
        .`when`(repository)
        .getQuests(anyOrNull(), anyOrNull())

    // Act
    viewModel.fetchAllQuests()
    advanceUntilIdle()

    // Assert
    assertTrue(viewModel.quest.first().isEmpty())
  }
}
