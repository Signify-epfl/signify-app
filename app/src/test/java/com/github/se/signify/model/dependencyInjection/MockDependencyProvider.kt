package com.github.se.signify.model.dependencyInjection

import com.github.se.signify.model.authentication.MockUserSession
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.home.feedback.FeedbackRepository
import com.github.se.signify.model.home.hand.HandLandmarkRepository
import com.github.se.signify.model.home.quest.QuestRepository
import com.github.se.signify.model.home.quiz.QuizRepository
import com.github.se.signify.model.profile.stats.StatsRepository
import org.mockito.Mockito.mock

object MockDependencyProvider : DependencyProvider {
  override fun challengeRepository(): ChallengeRepository {
    return mock(ChallengeRepository::class.java)
  }

  override fun handLandMarkRepository(): HandLandmarkRepository {
    return mock(HandLandmarkRepository::class.java)
  }

  override fun questRepository(): QuestRepository {
    return mock(QuestRepository::class.java)
  }

  override fun statsRepository(): StatsRepository {
    return mock(StatsRepository::class.java)
  }

  override fun userRepository(): UserRepository {
    return mock(UserRepository::class.java)
  }

  override fun quizRepository(): QuizRepository {
    return mock(QuizRepository::class.java)
  }

  override fun feedbackRepository(): FeedbackRepository {
    return mock(FeedbackRepository::class.java)
  }

  override fun userSession(): UserSession {
    return MockUserSession()
  }
}
