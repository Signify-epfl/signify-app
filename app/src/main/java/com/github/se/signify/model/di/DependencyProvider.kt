package com.github.se.signify.model.di

import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.feedback.FeedbackRepository
import com.github.se.signify.model.hand.HandLandMarkRepository
import com.github.se.signify.model.quest.QuestRepository
import com.github.se.signify.model.quiz.QuizRepository
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.user.UserRepository

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
