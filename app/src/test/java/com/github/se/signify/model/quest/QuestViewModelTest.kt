package com.github.se.signify.model.quest

import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any

class QuestViewModelTest{

    private lateinit var questRepository: QuestRepository
    private lateinit var questViewModel: QuestViewModel

    @Before
    fun setUp() {
        questRepository = mock(QuestRepository::class.java)
        questViewModel = QuestViewModel(questRepository)
    }

    @Test
    fun getDailyQuestCallsRepository() {
        questViewModel.getDailyQuest()
        verify(questRepository).getDailyQuest(any(), any())
    }

}