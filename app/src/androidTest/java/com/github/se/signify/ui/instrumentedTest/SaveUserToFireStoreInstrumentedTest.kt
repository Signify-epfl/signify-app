package com.github.se.signify.ui.instrumentedTest

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.signify.model.user.saveUserToFireStore
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.Before
import org.junit.Test

class SaveUserToFireStoreInstrumentedTest {
  private lateinit var auth: FirebaseAuth
  private lateinit var firestore: FirebaseFirestore

  @Before
  fun setUp() {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    FirebaseApp.initializeApp(context)

    auth = FirebaseAuth.getInstance()
    firestore = FirebaseFirestore.getInstance()
    firestore.useEmulator("10.0.2.2", 8080)
    auth.useEmulator("10.0.2.2", 9099)
  }

  @Test
  fun saveUserToFireStoreTestWhenLoggedIn() {
    auth.signInWithEmailAndPassword("testuser@gmail.com", "password123").addOnCompleteListener {
        task ->
      if (task.isSuccessful) {
        val currentUser: FirebaseUser? = auth.currentUser
        Log.d("InstrumentedTest", "Logged in user: ${currentUser?.email}")
        saveUserToFireStore()

        firestore
            .collection("users")
            .document("testuser")
            .get()
            .addOnSuccessListener { document ->
              if (document.exists()) {
                Log.d("InstrumentedTest", "User saved successfully")
                assert(document.getString("email") == "testuser@gmail.com")
                assert(document.getString("name") == null) // Assuming displayName is null
                assert(true)
              } else {
                assert(false)
              }
            }
            .addOnFailureListener { _ -> assert(false) }
      } else {
        assert(false)
      }
    }

    // Idle main looper (if needed) to wait for async tasks
    Thread.sleep(5000)
  }
}
