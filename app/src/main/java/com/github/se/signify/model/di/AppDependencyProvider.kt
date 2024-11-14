package com.github.se.signify.model.di

import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.challenge.ChallengeRepositoryFireStore
import com.github.se.signify.model.hand.HandLandMarkConfig
import com.github.se.signify.model.hand.HandLandMarkImplementation
import com.github.se.signify.model.hand.HandLandMarkRepository
import com.github.se.signify.model.quest.QuestRepository
import com.github.se.signify.model.quest.QuestRepositoryFireStore
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserRepositoryFireStore
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

private val appHandLandMarkConfig = HandLandMarkConfig("hand_landmarker.task", "RFC_model_ir9_opset19.onnx")

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

    override fun userRepository(): UserRepository {
        return UserRepositoryFireStore(Firebase.firestore)
    }
}