package com.github.se.signify.model.dependencyInjection

import com.github.se.signify.model.authentication.AuthService
import com.github.se.signify.model.authentication.MockAuthService
import com.github.se.signify.model.authentication.MockUserSession
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.challenge.MockChallengeRepository
import com.github.se.signify.model.common.user.MockUserRepository
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.home.feedback.FeedbackRepository
import com.github.se.signify.model.home.feedback.FirestoreFeedbackRepository
import com.github.se.signify.model.home.hand.HandLandmarkConfig
import com.github.se.signify.model.home.hand.HandLandmarkImplementation
import com.github.se.signify.model.home.hand.HandLandmarkRepository
import com.github.se.signify.model.home.quest.FirestoreQuestRepository
import com.github.se.signify.model.home.quest.QuestRepository
import com.github.se.signify.model.home.quiz.MockQuizRepository
import com.github.se.signify.model.home.quiz.QuizRepository
import com.github.se.signify.model.profile.stats.MockStatsRepository
import com.github.se.signify.model.profile.stats.StatsRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

private val appHandLandmarkConfig =
    HandLandmarkConfig("hand_landmarker.task", "RFC_model_ir9_opset19.onnx")

object MockDependencyProvider : DependencyProvider {
  override fun challengeRepository(): ChallengeRepository {
    return MockChallengeRepository()
  }

  override fun handLandMarkRepository(): HandLandmarkRepository {
    return HandLandmarkImplementation(appHandLandmarkConfig)
  }

  override fun questRepository(): QuestRepository {
    return FirestoreQuestRepository(Firebase.firestore) // TO BE REPLACED BY THE ACTUAL MOCK
  }

  override fun statsRepository(): StatsRepository {
    return MockStatsRepository()
  }

  override fun userRepository(): UserRepository {
    return MockUserRepository()
  }

  override fun quizRepository(): QuizRepository {
    return MockQuizRepository()
  }

  override fun feedbackRepository(): FeedbackRepository {
    return FirestoreFeedbackRepository(Firebase.firestore) // TO BE REPLACED BY THE ACTUAL MOCK
  }

  override fun userSession(): UserSession {
    return MockUserSession()
  }

  override fun provideAuthService(): AuthService {
    return MockAuthService()
  }
}
