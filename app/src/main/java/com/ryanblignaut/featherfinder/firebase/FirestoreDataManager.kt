package com.ryanblignaut.featherfinder.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.ryanblignaut.featherfinder.model.BirdObsDetails
import com.ryanblignaut.featherfinder.model.BirdObsTitle
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.model.FullBirdObservation
import com.ryanblignaut.featherfinder.model.Fullgoal
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.model.GoalTitle
import com.ryanblignaut.featherfinder.model.UserAchievement
import com.ryanblignaut.featherfinder.model.UserSettings
import kotlinx.coroutines.tasks.await

object FirestoreDataManager {
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun saveObservation(observation: BirdObservation): Result<String> {
        saveDataFirestoreAuth {
            val OBS = FullBirdObservation(
                observation.birdObsTitle.birdSpecies,
                observation.birdObsTitle.date,
                observation.birdObsDetails.time,
                observation.birdObsDetails.notes,
                observation.birdObsDetails.lat,
                observation.birdObsDetails.long
            )
            it.collection("observations_full").add(OBS).await().id
        }
        val idResult: Result<String> = saveDataFirestoreAuth {
            it.collection("observations").add(observation.birdObsDetails).await().id
        }
        // Fail if the idResult is a failure, else save the observation list data.
        val finalBirdId = idResult.fold({
            saveObsList(it, observation.birdObsTitle)
        }, Result.Companion::failure)
        updateBirdsCaptured()
        return finalBirdId

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

    suspend fun requestObservationIdListFiltered(
        filterTime: String,
        nameSort: Boolean,
    ): Result<List<BirdObsTitle>?> {
        return getDataFirestoreAuth {
            getDataFirestoreCollectionFilterSort(
                it.collection("observations_list"), filterTime, nameSort
            )
        }
    }

    suspend fun requestObservationById(id: String): Result<BirdObsDetails?> {
        return getDataFirestoreAuth { getDataFirestore(it.collection("observations").document(id)) }
    }

    suspend fun requestAllObservations(): Result<List<FullBirdObservation>?> {
        return getDataFirestoreAuth { getDataFirestoreCollection(it.collection("observations_full")) }
    }


    suspend fun requestGoalTitleList(): Result<List<GoalTitle>?> {
        return getDataFirestoreAuth { getDataFirestoreCollection(it.collection("goals_titles")) }
    }

    suspend fun requestFullGoalList(): Result<List<Fullgoal>?> {
        return getDataFirestoreAuth { getDataFirestoreCollection(it.collection("goals_full")) } // This has to return  a fullgoal list so we can test the Adapter code for the goalsList
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
            saveGoalFull(
                it, Fullgoal(
                    goalIdResult.toString(),
                    goal.goalTitle.name.toString(),
                    goal.goalTitle.description,
                    goal.goalDetail.startTime,
                    goal.goalDetail.endTime
                )
            )
        }, Result.Companion::failure)
    }

    private suspend fun saveGoalTitle(id: String, goal: GoalTitle): Result<String> {
        return saveDataFirestoreAuth {
            goal.id = id
            it.collection("goals_titles").add(goal).await().id
        }
    }

    private suspend fun saveGoalFull(id: String, goal: Fullgoal): Result<String> {
        return saveDataFirestoreAuth {
            goal.id = id
            it.collection("goals_full").add(goal).await().id
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

    private suspend inline fun <reified T> getDataFirestoreCollection(collection: CollectionReference): Result<List<T>?> {
        val snapshot = collection.get().await()
//        if (snapshot.isEmpty) return Result.failure(Exception("Collection is empty"))

        val dataList = mutableListOf<T>()
        for (document in snapshot) dataList.add(document.toObject(T::class.java))
        return Result.success(dataList)
    }


    private suspend inline fun <reified T> getDataFirestoreCollectionFilterSort(
        collection: CollectionReference,
        filterTime: String,
        nameSort: Boolean,
    ): Result<List<T>?> {
        var customCollection: Query = collection

        if (filterTime.isNotEmpty()) customCollection =
            customCollection.whereEqualTo("date", filterTime)

        if (nameSort) customCollection = customCollection.orderBy("birdSpecies")


        val snapshot = customCollection.get().await()

        val dataList = mutableListOf<T>()
        for (document in snapshot) dataList.add(document.toObject(T::class.java))
        return Result.success(dataList)

    }

    suspend fun requestUserAchievements(): Result<UserAchievement?> {
        return getDataFirestoreAuth { userRef ->
            val achievementsQuery = userRef.collection("achievements").document()
            val achievementsSnapshot = achievementsQuery.get().await()
            val achievementsData = achievementsSnapshot.toObject(UserAchievement::class.java)
            if (achievementsData != null) {
                Result.success(achievementsData)
            } else {
                Result.failure(Exception("Failed to parse user achievements"))
            }

            /*  if (!achievementsSnapshot.isEmpty) {
                  // If a settings document exists, parse and return it
                  val achievementsData =
                      achievementsSnapshot.documents[0].toObject(UserAchievement::class.java)
                  if (achievementsData != null) {
                      Result.success(achievementsData)
                  } else {
                      Result.failure(Exception("Failed to parse user achievements"))
                  }*/
        }
    }

    suspend fun updateDayStreak(loginDaysStreak: Int) {
        saveDataFirestoreAuth {
            val document = it.collection("achievements").document("achievements")
            document
                .update("loginDaysStreak", loginDaysStreak).await()
            document.update("lastLoginTime", System.currentTimeMillis()).await()
            ""
        }
    }

    suspend fun updateBirdsCaptured() {
        saveDataFirestoreAuth {
            val document = it.collection("achievements").document()
//            document.update("totalBirdsSeen", FieldValue.increment(1)).await()

            document.set(
                mapOf("totalBirdsSeen" to FieldValue.increment(1)),
                SetOptions.merge()
            ).await()

            ""
        }
    }


}