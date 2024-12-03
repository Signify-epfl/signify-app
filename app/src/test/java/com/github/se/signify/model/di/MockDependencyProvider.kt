package com.github.se.signify.model.di

import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.feedback.FeedbackRepository
import com.github.se.signify.model.hand.HandLandMarkRepository
import com.github.se.signify.model.quiz.QuizRepository
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.user.UserRepository
import org.mockito.Mockito.mock

object MockDependencyProvider : DependencyProvider {
  override fun challengeRepository(): ChallengeRepository {
    return mock(ChallengeRepository::class.java)
  }

  override fun handLandMarkRepository(): HandLandMarkRepository {
    return mock(HandLandMarkRepository::class.java)
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
