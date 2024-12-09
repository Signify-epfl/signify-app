package com.github.se.signify.model.dependencyInjection

import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.home.feedback.FeedbackRepository
import com.github.se.signify.model.home.hand.HandLandMarkRepository
import com.github.se.signify.model.home.quest.QuestRepository
import com.github.se.signify.model.home.quiz.QuizRepository
import com.github.se.signify.model.profile.stats.StatsRepository
import com.github.se.signify.model.common.user.UserRepository

interface DependencyProvider {
  fun challengeRepository(): ChallengeRepository

  fun handLandMarkRepository(): HandLandMarkRepository

  fun questRepository(): QuestRepository

  fun statsRepository(): StatsRepository

  fun userRepository(): UserRepository

  fun quizRepository(): QuizRepository

  fun feedbackRepository(): FeedbackRepository

  fun userSession(): UserSession
}
