package com.github.se.signify.model.user

import com.github.se.signify.model.challenge.Challenge
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class UserRepositoryFireStore(private val db: FirebaseFirestore) : UserRepository {

  private val collectionPath = "users"
  private val friendsListPath = "friends"
  private val friendRequestsListPath = "friendRequests"
  private val challengesCollectionPath = "challenges"
  private val usernamePath = "name"
  private val profilePicturePath = "profileImageUrl"

  override fun init(onSuccess: () -> Unit) {
    Firebase.auth.addAuthStateListener {
      if (it.currentUser != null) {
        onSuccess()
      }
    }
  }

  override fun getFriendsList(
      userId: String,
      onSuccess: (List<String>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val userRef = db.collection(collectionPath).document(userId)

    // Use of addSnapshotListener for instant updates
    userRef.addSnapshotListener { documentSnapshot, e ->
      if (e != null) {
        onFailure(e)
        return@addSnapshotListener
      }

      if (documentSnapshot != null && documentSnapshot.exists()) {
        val friends = (documentSnapshot[friendsListPath] as? List<*>)?.filterIsInstance<String>()
        onSuccess(friends ?: emptyList())
      } else {
        onSuccess(emptyList())
      }
    }
  }

  override fun getRequestsFriendsList(
      userId: String,
      onSuccess: (List<String>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {

    val userRef = db.collection(collectionPath).document(userId)

    // Use of addSnapshotListener for instant updates
    userRef.addSnapshotListener { documentSnapshot, e ->
      if (e != null) {
        onFailure(e)
        return@addSnapshotListener
      }

      if (documentSnapshot != null && documentSnapshot.exists()) {
        val friendsRequests =
            (documentSnapshot[friendRequestsListPath] as? List<*>)?.filterIsInstance<String>()
        onSuccess(friendsRequests ?: emptyList())
      } else {
        onSuccess(emptyList())
      }
    }
  }

  // Search for a user by their userId
  override fun getUserById(
      userId: String,
      onSuccess: (User) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val userRef = db.collection(collectionPath).document(userId)

    userRef
        .get()
        .addOnSuccessListener { document ->
          if (document.exists()) {
            try {
              // Make sure the document is properly converted into the User object
              val user = document.toObject(User::class.java)
              if (user != null) {
                onSuccess(user)
              } else {
                onFailure(Exception("Failed to parse user data"))
              }
            } catch (e: Exception) {
              onFailure(e)
            }
          } else {
            onFailure(Exception("User not found"))
          }
        }
        .addOnFailureListener { e -> onFailure(e) }
  }

  override fun getUserName(
      userId: String,
      onSuccess: (String) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val userRef = db.collection(collectionPath).document(userId)

    // Use of addSnapshotListener for instant updates
    userRef.addSnapshotListener { documentSnapshot, e ->
      if (e != null) {
        onFailure(e)
        return@addSnapshotListener
      }

      if (documentSnapshot != null && documentSnapshot.exists()) {
        val userName = documentSnapshot[usernamePath] as? String
        onSuccess(userName ?: "unknown")
      } else {
        onSuccess("unknown")
      }
    }
  }

  override fun updateUserName(
      userId: String,
      newName: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {

    val userRef = db.collection(collectionPath).document(userId)
    try {
      userRef
          .update(usernamePath, newName)
          .addOnSuccessListener {
            onSuccess() // Invoke the onSuccess callback
          }
          .addOnFailureListener { e ->
            onFailure(e) // Trigger onFailure callback
          }
    } catch (e: Exception) {
      onFailure(e) // Trigger onFailure callback
    }
  }

  override fun getProfilePictureUrl(
      userId: String,
      onSuccess: (String?) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val userRef = db.collection(collectionPath).document(userId)

    // Use of addSnapshotListener for instant updates
    userRef.addSnapshotListener { documentSnapshot, e ->
      if (e != null) {
        onFailure(e)
        return@addSnapshotListener
      }

      if (documentSnapshot != null && documentSnapshot.exists()) {
        val profilePictureUrl = documentSnapshot[profilePicturePath] as? String
        onSuccess(profilePictureUrl)
      } else {
        onSuccess(null)
      }
    }
  }

  override fun updateProfilePictureUrl(
      userId: String,
      newProfilePictureUrl: String?,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {

    val userRef = db.collection(collectionPath).document(userId)
    try {
      userRef
          .update(profilePicturePath, newProfilePictureUrl)
          .addOnSuccessListener {
            onSuccess() // Invoke the onSuccess callback
          }
          .addOnFailureListener { e ->
            onFailure(e) // Trigger onFailure callback
          }
    } catch (e: Exception) {
      onFailure(e) // Trigger onFailure callback
    }
  }

  override fun sendFriendRequest(
      currentUserId: String,
      targetUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val targetUserRef = db.collection(collectionPath).document(targetUserId)

    targetUserRef
        .update(friendRequestsListPath, FieldValue.arrayUnion(currentUserId))
        .addOnSuccessListener {
          onSuccess() // Trigger onSuccess callback
        }
        .addOnFailureListener { e ->
          onFailure(e) // Trigger onFailure callback
        }
  }

  override fun acceptFriendRequest(
      currentUserId: String,
      friendUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val currentUserRef = db.collection(collectionPath).document(currentUserId)
    val friendUserRef = db.collection(collectionPath).document(friendUserId)

    currentUserRef
        .update(friendsListPath, FieldValue.arrayUnion(friendUserId))
        .addOnSuccessListener {
          friendUserRef
              .update(friendsListPath, FieldValue.arrayUnion(currentUserId))
              .addOnSuccessListener {
                currentUserRef
                    .update(friendRequestsListPath, FieldValue.arrayRemove(friendUserId))
                    .addOnSuccessListener {
                      onSuccess() // Trigger onSuccess callback
                    }
                    .addOnFailureListener { e ->
                      onFailure(e) // Trigger onFailure callback
                    }
              }
              .addOnFailureListener { e ->
                onFailure(e) // Trigger onFailure callback
              }
        }
        .addOnFailureListener { e ->
          onFailure(e) // Trigger onFailure callback
        }
  }

  override fun declineFriendRequest(
      currentUserId: String,
      friendUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val currentUserRef = db.collection(collectionPath).document(currentUserId)

    currentUserRef
        .update(friendRequestsListPath, FieldValue.arrayRemove(friendUserId))
        .addOnSuccessListener {
          onSuccess() // Trigger onSuccess callback
        }
        .addOnFailureListener { e ->
          onFailure(e) // Trigger onFailure callback
        }
  }

  override fun removeFriend(
      currentUserId: String,
      friendUserId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val currentUserRef = db.collection(collectionPath).document(currentUserId)
    val friendUserRef = db.collection(collectionPath).document(friendUserId)

    currentUserRef
        .update(friendsListPath, FieldValue.arrayRemove(friendUserId))
        .addOnSuccessListener {
          friendUserRef
              .update(friendsListPath, FieldValue.arrayRemove(currentUserId))
              .addOnSuccessListener {
                onSuccess() // Trigger onSuccess callback
              }
              .addOnFailureListener { e ->
                onFailure(e) // Trigger onFailure callback
              }
        }
        .addOnFailureListener { e ->
          onFailure(e) // Trigger onFailure callback
        }
  }

  override fun addOngoingChallenge(
      userId: String,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val userRef = db.collection(collectionPath).document(userId)
    userRef
        .update("ongoingChallenges", FieldValue.arrayUnion(challengeId))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
  }

  override fun getOngoingChallenges(
      userId: String,
      onSuccess: (List<Challenge>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val userDocRef = db.collection(collectionPath).document(userId)

    userDocRef
        .get()
        .addOnSuccessListener { document ->
          if (!document.exists()) {
            onSuccess(emptyList())
            return@addOnSuccessListener
          }

          val challengeIds =
              document.get("ongoingChallenges") as? List<*>
                  ?: run {
                    onSuccess(emptyList())
                    return@addOnSuccessListener
                  }

          fetchChallengesByIds(challengeIds, onSuccess, onFailure)
        }
        .addOnFailureListener { e -> onFailure(e) }
  }

  private fun fetchChallengesByIds(
      challengeIds: List<*>,
      onSuccess: (List<Challenge>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val challenges = mutableListOf<Challenge>()
    val totalChallenges = challengeIds.size

    for (challengeId in challengeIds) {
      db.collection(challengesCollectionPath)
          .document(challengeId.toString())
          .get()
          .addOnSuccessListener { challengeDoc ->
            if (challengeDoc.exists()) {
              val challenge = challengeDoc.toObject(Challenge::class.java)
              if (challenge != null) {
                challenges.add(challenge)
              }
            }
            checkAllChallengesFetched(challenges, totalChallenges, onSuccess)
          }
          .addOnFailureListener { e -> onFailure(e) }
    }
  }

  private fun checkAllChallengesFetched(
      challenges: List<Challenge>,
      totalChallenges: Int,
      onSuccess: (List<Challenge>) -> Unit
  ) {
    if (challenges.size == totalChallenges) {
      onSuccess(challenges)
    }
  }

  override fun removeOngoingChallenge(
      userId: String,
      challengeId: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val userRef = db.collection(collectionPath).document(userId)
    userRef
        .update("ongoingChallenges", FieldValue.arrayRemove(challengeId))
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
  }

  override fun getInitialQuestAccessDate(
      userId: String,
      onSuccess: (String?) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val userDocRef = db.collection("users").document(userId)

    userDocRef
        .get()
        .addOnSuccessListener { document ->
          if (document != null && document.exists()) {
            val initialDate = document.getString("initialQuestAccessDate")
            onSuccess(initialDate) // Pass the date to the success callback
          } else {
            onSuccess(null) // No date set yet
          }
        }
        .addOnFailureListener { exception ->
          onFailure(exception) // Pass the exception to the failure callback
        }
  }

  // Function to set initial quest access date
  override fun setInitialQuestAccessDate(
      userId: String,
      date: String,
      onSuccess: () -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val userDocRef = db.collection("users").document(userId)

    userDocRef
        .update("initialQuestAccessDate", date)
        .addOnSuccessListener {
          onSuccess() // Success callback after updating the date
        }
        .addOnFailureListener { _ ->
          userDocRef
              .set(mapOf("initialQuestAccessDate" to date))
              .addOnSuccessListener { onSuccess() }
              .addOnFailureListener { onFailure(it) }
        }
  }

  override fun updateStreak(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val userDocRef = db.collection("users").document(userId)

    userDocRef
        .get()
        .addOnSuccessListener { document ->
          val lastLoginDate = document.getString("lastLoginDate")
          val currentStreak = document.getLong("currentStreak") ?: 0
          val highestStreak = document.getLong("highestStreak") ?: 0

          val today = LocalDate.now()
          val lastLogin = lastLoginDate?.let { LocalDate.parse(it) }

          val updatedData = mutableMapOf<String, Any>()
          updatedData["lastLoginDate"] = today.toString()

          if (lastLogin != null) {
            val daysBetween = ChronoUnit.DAYS.between(lastLogin, today)

            when {
              daysBetween == 1L -> {
                // Continuation of the streak
                val newStreak = currentStreak + 1
                updatedData["currentStreak"] = newStreak
                updatedData["highestStreak"] = maxOf(highestStreak, newStreak)
              }
              daysBetween > 1L -> {
                // Streak interrupted
                updatedData["currentStreak"] = 1
              }
            // No additional changes needed for same-day login
            }
          } else {
            // First login
            updatedData["currentStreak"] = 1
            updatedData["highestStreak"] = maxOf(highestStreak, 1)
          }

          // Always update Firestore, even if only the lastLoginDate changes
          userDocRef
              .update(updatedData)
              .addOnSuccessListener {
                onSuccess() // Call the success function
              }
              .addOnFailureListener { e ->
                onFailure(e) // Call the error function
              }
        }
        .addOnFailureListener { e ->
          onFailure(e) // Handle errors when fetching data
        }
  }

  override fun getStreak(
      userId: String,
      onSuccess: (Long) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    val userRef = db.collection(collectionPath).document(userId)

    // Use of addSnapshotListener for instant updates
    userRef.addSnapshotListener { documentSnapshot, e ->
      if (e != null) {
        onFailure(e)
        return@addSnapshotListener
      }

      if (documentSnapshot != null && documentSnapshot.exists()) {
        val streak = documentSnapshot["currentStreak"] as Long
        onSuccess(streak)
      } else {
        onSuccess(0)
      }
    }
  }
}
