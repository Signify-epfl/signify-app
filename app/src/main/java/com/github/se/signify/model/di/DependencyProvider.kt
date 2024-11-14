package com.github.se.signify.model.di

import android.content.Context
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.hand.HandLandMarkRepository
import com.github.se.signify.model.quest.QuestRepository
import com.github.se.signify.model.user.UserRepository

interface DependencyProvider {
    fun challengeRepository(): ChallengeRepository
    fun handLandMarkRepository(): HandLandMarkRepository
    fun questRepository(): QuestRepository
    fun userRepository(): UserRepository
}