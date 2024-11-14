package com.github.se.signify.model.di

import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.hand.HandLandMarkRepository
import com.github.se.signify.model.quest.QuestRepository
import com.github.se.signify.model.user.UserRepository
import org.mockito.Mockito.mock

object MockDependencyProvider : DependencyProvider {
  override fun challengeRepository(): ChallengeRepository {
    return mock(ChallengeRepository::class.java)
  }

  override fun handLandMarkRepository(): HandLandMarkRepository {
    return mock(HandLandMarkRepository::class.java)
  }

  override fun questRepository(): QuestRepository {
    return mock(QuestRepository::class.java)
  }

  override fun userRepository(): UserRepository {
    return mock(UserRepository::class.java)
  }
}
