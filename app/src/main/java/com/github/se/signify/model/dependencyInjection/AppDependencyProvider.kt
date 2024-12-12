package com.github.se.signify.model.dependencyInjection

import com.github.se.signify.model.authentication.AuthService
import com.github.se.signify.model.authentication.FirebaseAuthService
import com.github.se.signify.model.authentication.FirebaseUserSession
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.challenge.FirestoreChallengeRepository
import com.github.se.signify.model.common.user.FirestoreUserRepository
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.home.feedback.FeedbackRepository
import com.github.se.signify.model.home.feedback.FirestoreFeedbackRepository
import com.github.se.signify.model.home.hand.HandLandmarkConfig
import com.github.se.signify.model.home.hand.HandLandmarkImplementation
import com.github.se.signify.model.home.hand.HandLandmarkRepository
import com.github.se.signify.model.home.quest.FirestoreQuestRepository
import com.github.se.signify.model.home.quest.QuestRepository
import com.github.se.signify.model.home.quiz.FirestoreQuizRepository
import com.github.se.signify.model.home.quiz.QuizRepository
import com.github.se.signify.model.profile.stats.FirestoreStatsRepository
import com.github.se.signify.model.profile.stats.StatsRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

private val appHandLandmarkConfig =
    HandLandmarkConfig("hand_landmarker.task", "RFC_model_ir9_opset19.onnx")

object AppDependencyProvider : DependencyProvider {
  override fun challengeRepository(): ChallengeRepository {
    return FirestoreChallengeRepository(FirebaseFirestore.getInstance())
  }

  override fun handLandMarkRepository(): HandLandmarkRepository {
    return HandLandmarkImplementation(appHandLandmarkConfig)
  }

  override fun questRepository(): QuestRepository {
    return FirestoreQuestRepository(Firebase.firestore)
  }

  override fun statsRepository(): StatsRepository {
    return FirestoreStatsRepository(Firebase.firestore)
  }

  override fun userRepository(): UserRepository {
    return FirestoreUserRepository(Firebase.firestore)
  }

  override fun quizRepository(): QuizRepository {
    return FirestoreQuizRepository(Firebase.firestore)
  }

  override fun feedbackRepository(): FeedbackRepository {
    return FirestoreFeedbackRepository(Firebase.firestore)
  }

  override fun userSession(): UserSession {
    return FirebaseUserSession(provideAuthService())
  }

  override fun provideAuthService(): AuthService {
    return FirebaseAuthService()
  }
}
