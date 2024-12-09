package com.github.se.signify.model.stats

import androidx.test.core.app.ApplicationProvider
import com.github.se.signify.model.profile.stats.StatsRepositoryFirestore
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StatsRepositoryFirestoreTest {
  @Mock private lateinit var mockAuth: FirebaseAuth
  @Mock private lateinit var mockCurrentUser: FirebaseUser
  @Mock private lateinit var mockFirestore: FirebaseFirestore
  @Mock private lateinit var mockCollectionReference: CollectionReference
  @Mock private lateinit var mockUserDocument: DocumentReference
  @Mock private lateinit var mockDocumentSnapshot: DocumentSnapshot

  private lateinit var statsRepositoryFirestore: StatsRepositoryFirestore

  private val userId = "testUser"

  @Before
  fun setup() {
    MockitoAnnotations.openMocks(this)

    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    mockAuth = mock(FirebaseAuth::class.java)
    mockCurrentUser = mock(FirebaseUser::class.java)
    mockFirestore = mock(FirebaseFirestore::class.java)
    mockCollectionReference = mock(CollectionReference::class.java)
    mockUserDocument = mock(DocumentReference::class.java)

    statsRepositoryFirestore = StatsRepositoryFirestore(mockFirestore)
    `when`(mockFirestore.collection("stats")).thenReturn(mockCollectionReference)
    `when`(mockCollectionReference.document(userId)).thenReturn(mockUserDocument)
    `when`(mockFirestore.collection("stats").document(userId)).thenReturn(mockUserDocument)
  }

  @Test
  fun getLettersLearnedShouldReturnLettersListWhenDocumentExists() {
    val documentSnapshot: DocumentSnapshot = mock()
    `when`(documentSnapshot.get("lettersLearned")).thenReturn(listOf('A', 'B', 'C'))
    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    statsRepositoryFirestore.getLettersLearned(
        userId,
        onSuccess = { letters -> assertEquals(listOf('A', 'B', 'C'), letters) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun getLettersLearnedShouldHandleEmptyOrMissingList() {
    val documentSnapshot: DocumentSnapshot = mock()
    `when`(documentSnapshot.get("lettersLearned")).thenReturn(null)
    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    statsRepositoryFirestore.getLettersLearned(
        userId,
        onSuccess = { letters -> assertEquals(emptyList<Char>(), letters) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun getEasyExerciseStatsShouldReturnTheCorrectStat() {
    val documentSnapshot: DocumentSnapshot = mock()
    `when`(documentSnapshot.getLong("easyExercise")).thenReturn(10L)
    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    statsRepositoryFirestore.getEasyExerciseStats(
        userId,
        onSuccess = { easyCount -> assertEquals(10, easyCount) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun getMediumExerciseStatsShouldReturnTheCorrectStat() {
    val documentSnapshot: DocumentSnapshot = mock()
    `when`(documentSnapshot.getLong("mediumExercise")).thenReturn(10L)
    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    statsRepositoryFirestore.getMediumExerciseStats(
        userId,
        onSuccess = { mediumCount -> assertEquals(10, mediumCount) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun getHardExerciseStatsShouldReturnTheCorrectStat() {
    val documentSnapshot: DocumentSnapshot = mock()
    `when`(documentSnapshot.getLong("hardExercise")).thenReturn(10L)
    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    statsRepositoryFirestore.getHardExerciseStats(
        userId,
        onSuccess = { hardCount -> assertEquals(10, hardCount) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun getDailyQuestStatsShouldReturnTheCorrectStat() {
    val documentSnapshot: DocumentSnapshot = mock()
    `when`(documentSnapshot.getLong("dailyQuest")).thenReturn(10L)
    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    statsRepositoryFirestore.getDailyQuestStats(
        userId,
        onSuccess = { dailyCount -> assertEquals(10, dailyCount) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun getWeeklyQuestStatsShouldReturnTheCorrectStat() {
    val documentSnapshot: DocumentSnapshot = mock()
    `when`(documentSnapshot.getLong("weeklyQuest")).thenReturn(10L)
    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    statsRepositoryFirestore.getWeeklyQuestStats(
        userId,
        onSuccess = { weeklyCount -> assertEquals(10, weeklyCount) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun getCompletedChallengeStatsShouldReturnTheCorrectStat() {
    val documentSnapshot: DocumentSnapshot = mock()
    `when`(documentSnapshot.getLong("completedChallenge")).thenReturn(10L)
    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    statsRepositoryFirestore.getCompletedChallengeStats(
        userId,
        onSuccess = { completedCount -> assertEquals(10, completedCount) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun getCreatedChallengeStatsShouldReturnTheCorrectStat() {
    val documentSnapshot: DocumentSnapshot = mock()
    `when`(documentSnapshot.getLong("createdChallenge")).thenReturn(10L)
    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    statsRepositoryFirestore.getCreatedChallengeStats(
        userId,
        onSuccess = { createdCount -> assertEquals(10, createdCount) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun getWonChallengeStatsShouldReturnTheCorrectStats() {
    val documentSnapshot: DocumentSnapshot = mock()
    `when`(documentSnapshot.getLong("wonChallenge")).thenReturn(10L)
    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    statsRepositoryFirestore.getWonChallengeStats(
        userId,
        onSuccess = { wonCount -> assertEquals(10, wonCount) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun updateLettersLearnedShouldAddANewLetterToLettersLearned() {
    val newLetter = 'D'
    `when`(mockUserDocument.update(eq("lettersLearned"), any())).thenReturn(Tasks.forResult(null))

    statsRepositoryFirestore.updateLettersLearned(
        userId,
        newLetter,
        onSuccess = {
          verify(mockUserDocument)
              .update(eq("lettersLearned"), eq(FieldValue.arrayUnion(newLetter)))
        },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun updateEasyExerciseStatsShouldIncrementEasyExerciseCount() {
    `when`(mockUserDocument.update(eq("easyExercise"), any())).thenReturn(Tasks.forResult(null))

    statsRepositoryFirestore.updateEasyExerciseStats(
        userId,
        onSuccess = { verify(mockUserDocument).update("easyExercise", FieldValue.increment(1)) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun updateMediumExerciseStatsShouldIncrementMediumExerciseCount() {
    `when`(mockUserDocument.update(eq("mediumExercise"), any())).thenReturn(Tasks.forResult(null))

    statsRepositoryFirestore.updateMediumExerciseStats(
        userId,
        onSuccess = { verify(mockUserDocument).update("mediumExercise", FieldValue.increment(1)) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun updateHardExerciseStatsShouldIncrementHardExerciseCount() {
    `when`(mockUserDocument.update(eq("hardExercise"), any())).thenReturn(Tasks.forResult(null))

    statsRepositoryFirestore.updateHardExerciseStats(
        userId,
        onSuccess = { verify(mockUserDocument).update("hardExercise", FieldValue.increment(1)) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun updateDailyQuestStatsShouldIncrementDailyQuestCount() {
    `when`(mockUserDocument.update(eq("dailyQuest"), any())).thenReturn(Tasks.forResult(null))

    statsRepositoryFirestore.updateDailyQuestStats(
        userId,
        onSuccess = { verify(mockUserDocument).update("dailyQuest", FieldValue.increment(1)) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun updateWeeklyQuestStatsShouldIncrementWeeklyQuestCount() {
    `when`(mockUserDocument.update(eq("weeklyQuest"), any())).thenReturn(Tasks.forResult(null))

    statsRepositoryFirestore.updateWeeklyQuestStats(
        userId,
        onSuccess = { verify(mockUserDocument).update("weeklyQuest", FieldValue.increment(1)) },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun updateCompletedChallengeStatsShouldIncrementCompletedChallengeCount() {
    `when`(mockUserDocument.update(eq("completedChallenge"), any()))
        .thenReturn(Tasks.forResult(null))

    statsRepositoryFirestore.updateCompletedChallengeStats(
        userId,
        onSuccess = {
          verify(mockUserDocument).update("completedChallenge", FieldValue.increment(1))
        },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun updateCreatedChallengeStatsShouldIncrementCreatedChallengeCount() {
    `when`(mockUserDocument.update(eq("createdChallenge"), any())).thenReturn(Tasks.forResult(null))

    statsRepositoryFirestore.updateCreatedChallengeStats(
        userId,
        onSuccess = {
          verify(mockUserDocument).update("createdChallenge", FieldValue.increment(1))
        },
        onFailure = { fail("onFailure should not be called") })
  }

  @Test
  fun updateWonChallengeStatsShouldIncrementWonChallengeCount() {
    `when`(mockUserDocument.update(eq("wonChallenge"), any())).thenReturn(Tasks.forResult(null))

    statsRepositoryFirestore.updateWonChallengeStats(
        userId,
        onSuccess = { verify(mockUserDocument).update("wonChallenge", FieldValue.increment(1)) },
        onFailure = { fail("onFailure should not be called") })
  }
}
