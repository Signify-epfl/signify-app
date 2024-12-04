package com.github.se.signify.model.di

import com.github.se.signify.model.auth.AuthService
import com.github.se.signify.model.auth.FirebaseUserSession
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
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserRepositoryFireStore
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

private val appHandLandMarkConfig =
    HandLandMarkConfig("hand_landmarker.task", "RFC_model_ir9_opset19.onnx")

class MockDependencyProvider : DependencyProvider {
  override fun challengeRepository(): ChallengeRepository {
    return MockChallengeRepository()
  }

  override fun handLandMarkRepository(): HandLandMarkRepository {
    return HandLandMarkImplementation(appHandLandMarkConfig)
  }

  override fun questRepository(): QuestRepository {
    return QuestRepositoryFireStore(Firebase.firestore)
  }

  override fun statsRepository(): StatsRepository {
    return MockStatsRepository()
  }

  override fun userRepository(): UserRepository {
    return UserRepositoryFireStore(Firebase.firestore)
  }

  override fun quizRepository(): QuizRepository {
    return QuizRepositoryFireStore(Firebase.firestore)
  }

  override fun feedbackRepository(): FeedbackRepository {
    return FeedbackRepositoryFireStore(Firebase.firestore)
  }

  override fun userSession(): UserSession {
    return FirebaseUserSession(provideAuthService())
  }

  override fun provideAuthService(): AuthService {
    return MockAuthService()
  }
}

class MockAuthService : AuthService {

  override suspend fun signInWithGoogle(idToken: String): Boolean {
    return true
  }

  override suspend fun signOut(): Boolean {
    return true
  }

  override fun getCurrentUser(): String {
    return "foo@example.com"
  }
}
