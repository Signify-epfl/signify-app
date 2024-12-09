package com.github.se.signify.model.dependencyInjection

import com.github.se.signify.model.authentication.FirebaseUserSession
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.challenge.ChallengeRepositoryFireStore
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserRepositoryFireStore
import com.github.se.signify.model.home.feedback.FeedbackRepository
import com.github.se.signify.model.home.feedback.FeedbackRepositoryFireStore
import com.github.se.signify.model.home.hand.HandLandMarkConfig
import com.github.se.signify.model.home.hand.HandLandMarkImplementation
import com.github.se.signify.model.home.hand.HandLandMarkRepository
import com.github.se.signify.model.home.quest.QuestRepository
import com.github.se.signify.model.home.quest.QuestRepositoryFireStore
import com.github.se.signify.model.home.quiz.QuizRepository
import com.github.se.signify.model.home.quiz.QuizRepositoryFireStore
import com.github.se.signify.model.profile.stats.StatsRepository
import com.github.se.signify.model.profile.stats.StatsRepositoryFirestore
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

private val appHandLandMarkConfig =
    HandLandMarkConfig("hand_landmarker.task", "RFC_model_ir9_opset19.onnx")

object AppDependencyProvider : DependencyProvider {
  override fun challengeRepository(): ChallengeRepository {
    return ChallengeRepositoryFireStore(FirebaseFirestore.getInstance())
  }

  override fun handLandMarkRepository(): HandLandMarkRepository {
    return HandLandMarkImplementation(appHandLandMarkConfig)
  }

  override fun questRepository(): QuestRepository {
    return QuestRepositoryFireStore(Firebase.firestore)
  }

  override fun statsRepository(): StatsRepository {
    return StatsRepositoryFirestore(Firebase.firestore)
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
    return FirebaseUserSession()
  }
}
