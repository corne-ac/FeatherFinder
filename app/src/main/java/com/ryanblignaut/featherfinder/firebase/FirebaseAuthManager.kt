package com.ryanblignaut.featherfinder.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

object FirebaseAuthManager {
    private val auth = FirebaseAuth.getInstance()
    // Get current User
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    //The below auth code was derived from Google Firebase
    //https://firebase.google.com/docs/auth/android/google-signin

    suspend fun registerUser(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { Result.success(it) }
                ?: Result.failure(AuthException("Login failed: Signed out"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(AuthException("Create user failed: Invalid credentials"))
        } catch (e: Exception) {
            Result.failure(AuthException("Create user failed: ${e.message}"))
        }
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { Result.success(it) }
                ?: Result.failure(AuthException("Login failed: Signed out"))
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(AuthException("Login failed: User not found"))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(AuthException("Login failed: Invalid credentials"))
        } catch (e: Exception) {
            Result.failure(AuthException("Login failed: ${e.message}"))
        }
    }

    class AuthException(message: String) : Exception(message)





}