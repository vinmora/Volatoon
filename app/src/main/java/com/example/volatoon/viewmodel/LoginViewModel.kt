package com.example.volatoon.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volatoon.model.Account
import com.example.volatoon.model.RegisterData
import com.example.volatoon.model.authData
import com.example.volatoon.model.apiService
import com.example.volatoon.utils.DataStoreManager
import com.google.gson.Gson
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import android.util.Log
import com.example.volatoon.model.UpdatePasswordRequest
import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseUser


class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val TAG = "LoginViewModel"
    private val GOOGLE_PASSWORD_PREFIX = "GOOGLE_AUTH_"

    data class LoginState(
        val isLogin: Boolean = false,
        val loading: Boolean = false,
        val data: authData? = null,
        val error: String? = null,
        val isGoogleSignIn: Boolean = false
    )

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    data class APIError(
        val status: Int,
        val message: String,
    )

    fun loginUser(accountData: Account, dataStoreManager: DataStoreManager) {
        viewModelScope.launch {
            try {
                _loginState.value =
                    _loginState.value.copy(loading = true)
                val response = apiService.loginUser(accountData)

                if (response.code() != 200) {
                    val errorBody: APIError = Gson().fromJson(
                        response.errorBody()!!.charStream(),
                        APIError::class.java
                    );
                    throw Exception(errorBody.message)
                }

                _loginState.value = _loginState.value.copy(
                    isLogin = true,
                    loading = false,
                    data = response.body(),
                    error = null
                )

                dataStoreManager.saveToDataStore(response.body()!!.token)

            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(
                    loading = false,
                    error = "${e.message}"
                )
            }
        }
    }

    fun handleGoogleSignInResult(
        idToken: String,
        dataStoreManager: DataStoreManager
    ) {
        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(loading = true)
                Log.d(TAG, "Starting Google Sign-In process with idToken: ${idToken.take(10)}...")

                val currentUser = auth.currentUser
                if (currentUser == null) {
                    Log.e(TAG, "No Firebase user found after authentication")
                    throw Exception("No user signed in")
                }

                val email = currentUser.email ?: throw Exception("Email not found")
                val googlePassword = "${GOOGLE_PASSWORD_PREFIX}${currentUser.email}"

                // Check if user exists
                val userExists = checkUserExists(email)
                if (userExists) {
                    // Attempt to login
                    try {
                        loginWithGoogle(email, googlePassword, dataStoreManager)
                    } catch (e: Exception) {
                        // If login fails, update password and try again
                        Log.d(TAG, "Login failed, updating password and retrying", e)
                        updatePasswordAndLogin(email, googlePassword, dataStoreManager)
                    }
                } else {
                    // Register new user
                    registerNewUser(currentUser, googlePassword, dataStoreManager)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Google Sign-In failed", e)
                _loginState.value = _loginState.value.copy(
                    loading = false,
                    error = "Google Sign-In failed: ${e.message}"
                )
            }
        }
    }

    private suspend fun checkUserExists(email: String): Boolean {
        return try {
            val response = apiService.findUserByEmail(email)
            response.isSuccessful && response.body()?.userData != null
        } catch (e: Exception) {
            Log.e(TAG, "Error checking user existence: ${e.message}")
            false
        }
    }

    private suspend fun registerNewUser(currentUser: FirebaseUser, googlePassword: String, dataStoreManager: DataStoreManager) {
        val registerData = RegisterData(
            fullName = currentUser.displayName ?: "",
            userName = currentUser.email?.split("@")?.get(0) ?: "",
            email = currentUser.email ?: "",
            password = googlePassword
        )
        val registerResponse = apiService.registerUser(registerData)
        if (registerResponse.code() == 201) {
            Log.d(TAG, "Registration successful")
            loginWithGoogle(currentUser.email ?: "", googlePassword, dataStoreManager)
        } else {
            val errorMessage = Gson().fromJson(
                registerResponse.errorBody()?.charStream(),
                APIError::class.java
            ).message
            throw Exception(errorMessage)
        }
    }

    private suspend fun updatePasswordAndLogin(email: String, newPassword: String, dataStoreManager: DataStoreManager) {
        // Create UpdatePasswordRequest object
        val updatePasswordRequest = UpdatePasswordRequest(
            email = email,
            newPassword = newPassword
        )

        // Update the password in your backend
        val updateResponse = apiService.updatePassword(updatePasswordRequest)
        if (updateResponse.isSuccessful) {
            loginWithGoogle(email, newPassword, dataStoreManager)
        } else {
            throw Exception("Failed to update password")
        }
    }




    private suspend fun loginWithGoogle(email: String, password: String, dataStoreManager: DataStoreManager) {
        try {
            Log.d(TAG, "Attempting Google login for email: $email")

            val response = apiService.loginUser(Account(email, password))

            if (!response.isSuccessful) {
                val errorMessage = try {
                    val errorBody: APIError = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        APIError::class.java
                    )
                    errorBody.message
                } catch (e: Exception) {
                    "Login failed: ${response.code()}"
                }
                throw Exception(errorMessage)
            }

            response.body()?.let { authData ->
                _loginState.value = _loginState.value.copy(
                    isLogin = true,
                    loading = false,
                    data = authData,
                    error = null
                )
                dataStoreManager.saveToDataStore(authData.token)
            } ?: throw Exception("Empty response body")

        } catch (e: Exception) {
            Log.e(TAG, "Login error", e)
            _loginState.value = _loginState.value.copy(
                loading = false,
                error = e.message ?: "Unknown error occurred"
            )
        }
    }
}




