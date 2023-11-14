package com.ryanblignaut.featherfinder.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.model.BirdObsDetails
import com.ryanblignaut.featherfinder.model.BirdObsTitle
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.model.FullBirdObservation
import com.ryanblignaut.featherfinder.model.FullGoal
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.model.GoalTitle
import com.ryanblignaut.featherfinder.model.SelfId
import com.ryanblignaut.featherfinder.model.UserAchievement
import com.ryanblignaut.featherfinder.model.UserSettings
import com.ryanblignaut.featherfinder.utils.DateTimeConvert
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

    suspend fun requestFullGoalList(): Result<List<FullGoal>?> {

        return getDataFirestoreAuth { getDataFirestoreCollection(it.collection("goals_full")) }
        // This has to return  a fullgoal list so we can test the Adapter code for the goalsList
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
            // We want the goal to have long for start time and end time so we will convert back.
            // We night have wanted a custom
            val start = DateTimeConvert.fromStringToLong(goal.goalDetail.startTime)
            val end = DateTimeConvert.fromStringToLong(goal.goalDetail.endTime)
            saveGoalFull(
                it, FullGoal(
                    goalIdResult.toString(),
                    goal.goalTitle.name,
                    goal.goalTitle.description,
                    start,
                    end
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

    private suspend fun saveGoalFull(id: String, goal: FullGoal): Result<String> {
        return saveDataFirestoreAuth {
            goal.id = id
            it.collection("goals_full").add(goal).await().id
        }
    }

    //This below delete code DOES NOT WORK, fek knows how it should be done

    suspend fun deleteGoal(id: String): Result<String?> {
        return getDataFirestoreAuth {
            it.collection("goals_full").document(id).delete().await()
            return Result.success("Success")
        }
        /*    deleteFirestoreAuth {
                try {
                    database.collection("goals_full").document(id).delete().await()
                    database.collection("goals_titles").document(id).delete().await()
                    database.collection("goals_descriptions").document(id).delete().await()
                    Result.success("Success")  // Success, return a Result with Unit
                } catch (e: Exception) {
                    Result.failure(e)  // An error occurred, return a Result with the exception
                }
            }*/
    }


    private inline fun deleteFirestoreAuth(run: (DocumentReference) -> Result<String>) {
        FirebaseAuthManager.getCurrentUser()?.let { currentUser ->
            val userRef = database.collection("users").document(currentUser.uid)
            run(userRef)
        } ?: Result.failure(Exception("User not authenticated"))
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
        val dataList = mutableListOf<T>()


        for (document in snapshot) {
            val data = document.toObject(T::class.java)
            if (data is SelfId)
                data.selfId = document.id

            dataList.add(data)
        }
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
            val achievementsQuery = userRef.collection("achievements").document("achievementData")
            val achievementsSnapshot = achievementsQuery.get().await()
            val achievementsData = achievementsSnapshot.toObject(UserAchievement::class.java)
            if (achievementsData != null) {
                Result.success(achievementsData)
            } else {
                Result.failure(Exception("Failed to parse user achievements"))
            }
        }
    }

    suspend fun updateDayStreak(loginDaysStreak: Int) {
        saveDataFirestoreAuth {
            val document = it.collection("achievements").document("achievementData")
            document.set(
                mapOf(
                    "loginDaysStreak" to loginDaysStreak,
                    "lastLoginTime" to System.currentTimeMillis()
                ), SetOptions.merge()
            ).await()
            ""
        }
    }

    private suspend fun updateBirdsCaptured() {
        saveDataFirestoreAuth {
            it.collection("achievements").document("achievementData").set(
                mapOf("totalBirdsSeen" to FieldValue.increment(1)), SetOptions.merge()
            ).await()
            ""
        }
    }

    private suspend fun updateGoalCompleted(value: Long) {
        saveDataFirestoreAuth {
            it.collection("achievements").document("achievementData").set(
                mapOf("totalGoalsComp" to FieldValue.increment(value)), SetOptions.merge()
            ).await()
            ""
        }
    }

    suspend fun requestUrgentGoal(): Result<FullGoal?> = getDataFirestoreAuth {
        Result.success(
            it.collection("goals_full").orderBy("endTime").whereEqualTo("goalCompleted", false)
                .limit(1).get()
                .await()
                .documents.getOrNull(0)?.toObject(FullGoal::class.java)
        )
    }

    suspend fun completeGoal(id: String) {
        saveDataFirestoreAuth {
            it.collection("goals_full").document(id).set(
                mapOf("goalCompleted" to true), SetOptions.merge()
            ).await()
            ""
        }
    }

    suspend fun removeCompletionOnGoal(id: String) {
        updateGoalCompleted(-1)
        saveDataFirestoreAuth {
            it.collection("goals_full").document(id).set(
                mapOf("goalCompleted" to false), SetOptions.merge()
            ).await()
            ""
        }
    }


//    fun requestUrgentGoal(): Result<Goal?>? {
//
//    }


}