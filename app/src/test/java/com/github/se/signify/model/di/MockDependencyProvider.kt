package com.github.se.signify.model.di

import android.content.Context
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.hand.HandLandMarkRepository
import com.github.se.signify.model.quest.QuestRepository
import com.github.se.signify.model.user.UserRepository
import org.mockito.Mockito.mock

class MockDependencyProvider : DependencyProvider {
    override fun challengeRepository(context: Context): ChallengeRepository {
        return mock(ChallengeRepository::class.java)
    }

    override fun handLandMarkRepository(context: Context): HandLandMarkRepository {
        return mock(HandLandMarkRepository::class.java)
    }

    override fun questRepository(context: Context): QuestRepository {
        return mock(QuestRepository::class.java)
    }

    override fun userRepository(context: Context): UserRepository {
        return mock(UserRepository::class.java)
    }
}