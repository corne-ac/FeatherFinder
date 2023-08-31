package com.ryanblignaut.featherfinder.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ryanblignaut.featherfinder.model.Achievement
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.model.Goal
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object FirebaseDataManager {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("users")

    //Firebase design data with for users and achievements. All users should be able to see all the achievements but they must also be able to see the ones that they have completed
    suspend fun getAllAchievements(): Map<String?, Achievement> {
        val achRef = database.getReference("achievements")
        return fetchMapDataFromFirebase(achRef)
    }

    suspend fun getUserAchievements(): List<Achievement> {
        val currentUser = FirebaseAuthManager.getCurrentUser()
        val userAchRef = usersRef.child(currentUser!!.uid).child("achievements")
        return fetchListDataFromFirebase(userAchRef)
    }
    suspend fun getAllObservations(): List<BirdObservation> {
        // Get the current user and their goals
        val currentUser = FirebaseAuthManager.getCurrentUser()
        val userGoalsRef = usersRef.child(currentUser!!.uid).child("observations")
        return fetchListDataFromFirebase(userGoalsRef)
    }

    suspend fun getAllGoals(): List<Goal> {
        // Get the current user and their goals
        val currentUser = FirebaseAuthManager.getCurrentUser()
        val userGoalsRef = usersRef.child(currentUser!!.uid).child("goals")
        return fetchListDataFromFirebase(userGoalsRef)
    }

    suspend fun getGoal(id: String): Result<Goal> {
        val currentUser = FirebaseAuthManager.getCurrentUser()
        val userGoalRef = usersRef.child(currentUser!!.uid).child("goals").child(id)
        return fetchSingularDataFromFirebase(userGoalRef)
    }

    fun saveGoal(goal: Goal): Result<Goal> {
        val currentUser = FirebaseAuthManager.getCurrentUser()
        val userGoalReg = usersRef.child(currentUser!!.uid).child("goals").child(goal.id)
        userGoalReg.setValue(goal)
        return Result.success(goal)
    }

    private suspend inline fun <reified T> fetchSingularDataFromFirebase(databaseReference: DatabaseReference): Result<T> {

        return suspendCancellableCoroutine { continuation ->
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue(T::class.java)
                    data.let {
                        Result.success(data)
                    }
                    continuation.resume(Result.failure(Exception("Unable to get data for: ")))
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWith(Result.failure(error.toException()))
                }
            })
        }
    }


    // Define a generic function to fetch data from Firebase Realtime Database
    private suspend inline fun <reified T> fetchListDataFromFirebase(databaseReference: DatabaseReference): List<T> {
        // Create a mutable list to store retrieved data
        val dataList = mutableListOf<T>()

        // Use suspendCancellableCoroutine to handle asynchronous operation in a suspend function
        return suspendCancellableCoroutine { continuation ->
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Loop through the snapshot of data
                    for (dataSnapshot in snapshot.children) {
                        // Deserialize each data snapshot into an object of type T
                        val data = dataSnapshot.getValue(T::class.java)
                        data?.let { dataList.add(it) }
                    }
                    // Acts like a complete.
                    continuation.resume(dataList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // If an error occurs, resume the coroutine with an exception
                    continuation.resumeWithException(error.toException())
//                    continuation.resumeWith(Result.failure(error.toException()))
                }
            })
        }
    }

    private suspend inline fun <reified T> fetchMapDataFromFirebase(databaseReference: DatabaseReference): Map<String?, T> {
        // Create a mutable list to store retrieved data
        val dataList = mutableMapOf<String?, T>()

        // Use suspendCancellableCoroutine to handle asynchronous operation in a suspend function
        return suspendCancellableCoroutine { continuation ->
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Loop through the snapshot of data
                    for (dataSnapshot in snapshot.children) {

                        // Deserialize each data snapshot into an object of type T
                        val data = dataSnapshot.getValue(T::class.java)
                        data?.let { dataList.put(dataSnapshot.key, it) }
                    }
                    // Acts like a complete.
                    continuation.resume(dataList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // If an error occurs, resume the coroutine with an exception
                    continuation.resumeWithException(error.toException())
//                    continuation.resumeWith(Result.failure(error.toException()))
                }
            })
        }
    }

}