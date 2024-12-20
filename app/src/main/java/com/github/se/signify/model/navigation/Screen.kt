package com.github.se.signify.model.navigation

enum class Screen(val route: String, val requiresAuth: Boolean = true) {
  WELCOME("Welcome Screen", false),
  AUTH("Auth Screen", false),
  UNAUTHENTICATED("Unauthenticated Screen", false),
  HOME("Home Screen", false),
  PRACTICE("Practice Screen", false),
  EXERCISE_EASY("Easy Exercise Screen", false),
  EXERCISE_MEDIUM("Medium Exercise Screen", false),
  EXERCISE_HARD("Hard Exercise Screen", false),
  QUEST("Quest Screen"),
  FEEDBACK("Feedback Screen"),
  QUIZ("Quiz Screen", false),
  PROFILE("Profile Screen"),
  FRIENDS("Friends Screen"),
  SETTINGS("Settings Screen"),
  CHALLENGE("Challenge Screen"),
  NEW_CHALLENGE("NewChallenge Screen"),
  CREATE_CHALLENGE("CreateChallenge Screen"),
  CHALLENGE_HISTORY("ChallengeHistory Screen"),
  CHRONO_CHALLENGE("ChronoChallenge Screen/{challengeId}"),
  SPRINT_CHALLENGE("SprintChallenge Screen/{challengeId}"),
  TUTORIAL("Tutorial Screen", false),

  // For testing purposes
  REQUIRE_AUTH("RequireAuth", true),
  DO_NOT_REQUIRE_AUTH("DoNotRequireAuth", false)
}
