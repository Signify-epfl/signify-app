package com.github.se.signify.model.user

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserRepositoryFireStore(private val db: FirebaseFirestore) : UserRepository {

  private val collectionPath = "users"
  private val friendsListPath = "friends"
  private val friendRequestsListPath = "friendRequests"

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
        val friends = documentSnapshot.get(friendsListPath) as? List<String>
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
        val friendsRequests = documentSnapshot.get(friendRequestsListPath) as? List<String>
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
        val userName = documentSnapshot.get("name") as? String
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
          .update("name", newName)
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
}
