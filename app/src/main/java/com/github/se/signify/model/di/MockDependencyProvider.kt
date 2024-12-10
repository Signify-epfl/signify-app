package com.github.se.signify.model.di

import com.github.se.signify.model.auth.AuthService
import com.github.se.signify.model.auth.MockAuthService
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.challenge.MockChallengeRepository
import com.github.se.signify.model.feedback.FeedbackRepository
import com.github.se.signify.model.feedback.FeedbackRepositoryFireStore
import com.github.se.signify.model.hand.HandLandMarkConfig
import com.github.se.signify.model.hand.HandLandMarkImplementation
import com.github.se.signify.model.hand.HandLandMarkRepository
import com.github.se.signify.model.quest.QuestRepository
import com.github.se.signify.model.quest.QuestRepositoryFireStore
import com.github.se.signify.model.quiz.QuizRepository
import com.github.se.signify.model.quiz.QuizRepositoryFireStore
import com.github.se.signify.model.stats.MockStatsRepository
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.user.MockUserRepository
import com.github.se.signify.model.user.UserRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

private val appHandLandMarkConfig =
    HandLandMarkConfig("hand_landmarker.task", "RFC_model_ir9_opset19.onnx")

object MockDependencyProvider : DependencyProvider {
  override fun challengeRepository(): ChallengeRepository {
    return MockChallengeRepository()
  }

  override fun handLandMarkRepository(): HandLandMarkRepository {
    return HandLandMarkImplementation(appHandLandMarkConfig)
  }

  override fun questRepository(): QuestRepository {
    return QuestRepositoryFireStore(Firebase.firestore) // TO BE REPLACED BY THE ACTUAL MOCK
  }

  override fun statsRepository(): StatsRepository {
    return MockStatsRepository()
  }

  override fun userRepository(): UserRepository {
    return MockUserRepository()
  }

  override fun quizRepository(): QuizRepository {
    return QuizRepositoryFireStore(Firebase.firestore) // TO BE REPLACED BY THE ACTUAL MOCK
  }

  override fun feedbackRepository(): FeedbackRepository {
    return FeedbackRepositoryFireStore(Firebase.firestore) // TO BE REPLACED BY THE ACTUAL MOCK
  }

  override fun userSession(): UserSession {
    return MockUserSession()
  }

  override fun provideAuthService(): AuthService {
    return MockAuthService()
  }
}
