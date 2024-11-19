package com.github.se.signify.model.quiz

import android.util.Log
import com.github.se.signify.ui.getLetterIconResId
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class QuizRepositoryFireStore(private val db: FirebaseFirestore) : QuizRepository {

    private val collectionPath = "quizzes"

    override fun init(onSuccess: () -> Unit) {
        Firebase.auth.addAuthStateListener {
            if (it.currentUser != null) {
                onSuccess()
            }
        }
    }

    override fun getQuizQuestions(onSuccess: (List<QuizQuestion>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionPath).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val quests =
                    task.result
                        ?.mapNotNull { document -> documentToQuiz(document) }
                        ?: emptyList()
                onSuccess(quests)
            } else {
                task.exception?.let { e -> onFailure(e) }
            }
        }
    }

    internal fun documentToQuiz(document: DocumentSnapshot): QuizQuestion? {
        return try {
            val correctWord = document.getString("word") ?: return null
            val confusers = (document.get("confusers") as? List<*>) ?.filterIsInstance<String>() ?: return null
            val signs = getSignsForWord(correctWord)
            QuizQuestion(correctWord = correctWord, confusers = confusers, signs = signs)
        } catch (e: Exception) {
            Log.e("QuizRepository", "Exception during document conversion: ${e.message}", e)
            null
        }
    }

    private fun getSignsForWord(word: String): List<Int> {
        return word.lowercase().map { char ->
            getLetterIconResId(char)
        }
    }
}
