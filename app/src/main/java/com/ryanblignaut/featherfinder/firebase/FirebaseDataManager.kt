/*
package com.ryanblignaut.featherfinder.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.ryanblignaut.featherfinder.model.Achievement
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.model.Goal
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
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

    suspend fun getUserAchievements(): Result<List<Achievement>> {
        val currentUser = FirebaseAuthManager.getCurrentUser()
        val userAchRef = usersRef.child(currentUser!!.uid).child("achievements")
        return fetchListDataFromFirebase(userAchRef)
    }

    suspend fun getObservationById(id: String): Result<BirdObservation?> {
        // Get the current user and their goals
        val currentUser = FirebaseAuthManager.getCurrentUser()
        val userObservationsRef = usersRef.child(currentUser!!.uid).child("observations").child(id)
        return getItem(userObservationsRef)
    }

    suspend fun getAllObservations(): Result<List<BirdObservation>> {
        // Get the current user and their goals
        val currentUser = FirebaseAuthManager.getCurrentUser()
        val userObservationsRef = usersRef.child(currentUser!!.uid).child("observations")
        return getItems(userObservationsRef)
    }


*/
/*
    suspend fun getAllGoals(): Result<List<Goal>> {
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
*//*


*/
/*
    fun saveGoal(goal: Goal): Result<Goal> {
        val currentUser = FirebaseAuthManager.getCurrentUser()
        val userGoalReg = usersRef.child(currentUser!!.uid).child("goals").child(goal.id)
        userGoalReg.setValue(goal)
        return Result.success(goal)
    }
*//*


  */
/*  suspend fun saveObservation(observation: BirdObservation): Result<BirdObservation> {
        val currentUser = FirebaseAuthManager.getCurrentUser()!!
        return saveDataToFirebase(
            usersRef.child(currentUser.uid).child("observations").child(observation.id), observation
        )
    }*//*


   */
/* suspend fun addUser(uid: String, username: String): Result<String> {
        return saveDataToFirebase(usersRef.child(uid).child("username"), username)
    }*//*


    private suspend inline fun <reified T> saveDataToFirebase(
        databaseReference: DatabaseReference,
        data: T,
    ): Result<T> {
        return suspendCancellableCoroutine { continuation ->
            databaseReference.setValue(data).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Result.success(data))
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    continuation.resume(Result.failure(Exception(errorMessage)))
                }
            }
        }
    }


    */
/*   private suspend inline fun <reified T> getItems(ref: DatabaseReference): Result<List<T>> {
           return try {
               // Refer to https://stackoverflow.com/questions/66072471/what-is-the-difference-between-get-and-addlistenerforsinglevalueevent
               // Why do you recommend using get() when addListenerForSingleValueEvent is mentioned so much more?
               // Basically the addlistenerforsinglevalueevent was the old way of doing things and get() is the new way of doing things.

               val dataSnapshot = ref.get().await()
               if (dataSnapshot.exists()) {
                   val items = mutableListOf<T>()
                   for (itemSnapshot in dataSnapshot.children) {
                       val item = itemSnapshot.getValue<T>()
                       item?.let { items.add(it) }
                   }
                   Result.success(items)
               } else {
                   Result.failure(ItemNotFoundExceptionFirebase())
               }
           } catch (e: Exception) {
               Result.failure(e)
           }
       }*//*


    private suspend inline fun <reified T> getItem(ref: DatabaseReference): Result<T?> {
        return makeFirebaseQuery(ref) { dataSnapshot ->
            dataSnapshot.getValue<T>()
        }
    }

    private suspend inline fun <reified T> getItems(ref: DatabaseReference): Result<List<T>> {
        return makeFirebaseQuery(ref) { dataSnapshot ->
            val items = mutableListOf<T>()
            for (itemSnapshot in dataSnapshot.children) {
                val item = itemSnapshot.getValue<T>()
                item?.let { items.add(it) }
            }
            items
        }
    }

    private suspend inline fun <reified T> makeFirebaseQuery(
        ref: DatabaseReference,
        run: (DataSnapshot) -> T,
    ): Result<T> {
        return try {
            val dataSnapshot = ref.get().await()
            if (dataSnapshot.exists()) {
                Result.success(run(dataSnapshot))
            } else {
                Result.failure(ItemNotFoundExceptionFirebase())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }


    class ItemNotFoundExceptionFirebase : Exception("No items found in the database")

    private suspend inline fun <reified T> fetchSingularDataFromFirebase(databaseReference: DatabaseReference): Result<T> {
        return suspendCancellableCoroutine { continuation ->
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val data = snapshot.getValue(T::class.java)
                        data?.let {
                            continuation.resume(Result.success(data))
                        } ?: run {
                            continuation.resume(Result.failure(Exception("Unable to get data for: ")))
                        }
                    } catch (e: DatabaseException) {
                        continuation.resumeWith(Result.failure(e))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWith(Result.failure(error.toException()))
                }
            })
        }
    }


    // Define a generic function to fetch data from Firebase Realtime Database
    private suspend inline fun <reified T> fetchListDataFromFirebase(databaseReference: DatabaseReference): Result<List<T>> {
        // Create a mutable list to store retrieved data
        val dataList = mutableListOf<T>()

        // Use suspendCancellableCoroutine to handle asynchronous operation in a suspend function
        try {
            val suspendCancellableCoroutine =
                suspendCancellableCoroutine<Result<List<T>>> { continuation ->
                    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // Loop through the snapshot of data
                            for (dataSnapshot in snapshot.children) {
                                // Deserialize each data snapshot into an object of type T
                                val data = dataSnapshot.getValue<T>()
                                data?.let { dataList.add(it) }
                            }
                            // Acts like a complete.
                            continuation.resume(Result.success(dataList))

                        }

                        override fun onCancelled(error: DatabaseError) {
                            // If an error occurs, resume the coroutine with an exception
                            continuation.resumeWithException(error.toException())
//                    continuation.resumeWith(Result.failure(error.toException()))
                        }
                    })
                }
            return suspendCancellableCoroutine
        } catch (e: Exception) {
            return Result.failure(e)
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


}*/
