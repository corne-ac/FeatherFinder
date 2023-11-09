package com.ryanblignaut.featherfinder.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.ryanblignaut.featherfinder.model.BirdObsDetails
import com.ryanblignaut.featherfinder.model.BirdObsTitle
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.model.GoalTitle
import com.ryanblignaut.featherfinder.model.UserSettings
import kotlinx.coroutines.tasks.await

object FirestoreDataManager {
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun saveObservation(observation: BirdObservation): Result<String> {
        val idResult: Result<String> = saveDataFirestoreAuth {
            it.collection("observations").add(observation.birdObsDetails).await().id
        }
        // Fail if the idResult is a failure, else save the observation list data.
        return idResult.fold({
            saveObsList(it, observation.birdObsTitle)
        }, Result.Companion::failure)
    }

    private suspend fun saveObsList(id: String, observation: BirdObsTitle): Result<String> {
        return saveDataFirestoreAuth {
            observation.id = id
            it.collection("observations_list").add(observation).await().id
        }
    }


    suspend fun getSettings(): Result<UserSettings?> {
        return getDataFirestoreAuth { userRef ->
//            getDataFirestore(it.collection("settings").document("settings"))

            val settingsQuery = userRef.collection("settings").limit(1)
            val settingsSnapshot = settingsQuery.get().await()
            if (!settingsSnapshot.isEmpty) {
                // If a settings document exists, parse and return it
                val settingsData = settingsSnapshot.documents[0].toObject(UserSettings::class.java)
                if (settingsData != null) {
                    Result.success(settingsData)
                } else {
                    Result.failure(Exception("Failed to parse user settings"))
                }
            } else {
                return Result.success(null)
            }
        }
    }

    suspend fun saveSettings(settings: UserSettings): Result<String> {
        return saveDataFirestoreAuth { userRef ->
            /*  it.collection("settings").add(settings).await().id*/

            val settingsQuery = userRef.collection("settings").limit(1)
            val existingSettings = settingsQuery.get().await()
            if (existingSettings.isEmpty) {
                // If no settings document exists, create a new one
                val newSettingsRef = userRef.collection("settings").document()
                newSettingsRef.set(settings).await()
                newSettingsRef.id
            } else {
                // If a settings document exists, update it
                val existingSettingsDoc = existingSettings.documents[0]
                existingSettingsDoc.reference.set(settings, SetOptions.merge()).await()
                existingSettingsDoc.id
            }

        }
    }

    suspend fun requestObservationIdList(): Result<List<BirdObsTitle>?> {
        return getDataFirestoreAuth { getDataFirestoreCollection(it.collection("observations_list")) }
    }
    suspend fun requestObservationIdList1(): Result<List<BirdObsTitle>?> {
        return getDataFirestoreAuth { getDataFirestoreCollection(it.collection("observations_list").orderBy("test")) }
    }
    suspend fun requestObservationById(id: String): Result<BirdObsDetails?> {
        return getDataFirestoreAuth { getDataFirestore(it.collection("observations").document(id)) }
    }


    suspend fun requestGoalTitleList(): Result<List<GoalTitle>?> {
        return getDataFirestoreAuth { getDataFirestoreCollection(it.collection("goals_titles")) }
    }

    suspend fun addUser(username: String): Result<String> {
        return saveDataFirestoreAuth {
            it.set(mapOf("username" to username)).await()
            return@saveDataFirestoreAuth "Success"
        }
    }

    suspend fun saveGoal(goal: Goal): Result<String> {
        val goalIdResult = saveDataFirestoreAuth {
            it.collection("goals_descriptions").add(goal.goalDetail).await().id
        }
        return goalIdResult.fold({
            saveGoalTitle(it, goal.goalTitle)
        }, Result.Companion::failure)
    }

    private suspend fun saveGoalTitle(id: String, goal: GoalTitle): Result<String> {
        return saveDataFirestoreAuth {
            goal.id = id
            it.collection("goals_titles").add(goal).await().id
        }
    }


    private inline fun <reified T> getDataFirestoreAuth(run: (DocumentReference) -> Result<T?>): Result<T?> {
        return FirebaseAuthManager.getCurrentUser()?.let { currentUser ->
            val userRef = database.collection("users").document(currentUser.uid)
            return run(userRef)
        } ?: Result.failure(Exception("User not authenticated"))
    }

    private inline fun saveDataFirestoreAuth(run: (DocumentReference) -> String): Result<String> {

        return FirebaseAuthManager.getCurrentUser()?.let { currentUser ->
            val userRef = database.collection("users").document(currentUser.uid)
            return Result.success(run(userRef))
        } ?: Result.failure(Exception("User not authenticated"))
    }

    private suspend inline fun <reified T> getDataFirestore(document: DocumentReference): Result<T?> {
        val snapshot = document.get().await()
        if (!snapshot.exists()) return Result.failure(Exception("Document does not exist"))

        val data = snapshot.toObject(T::class.java)
        return Result.success(data)
    }

    private suspend inline fun <reified T> getDataFirestoreCollection(collection: Query): Result<List<T>?> {
        val snapshot = collection.get().await()

//        if (snapshot.isEmpty) return Result.failure(Exception("Collection is empty"))

        val dataList = mutableListOf<T>()
        for (document in snapshot) dataList.add(document.toObject(T::class.java))
        return Result.success(dataList)
    }

}