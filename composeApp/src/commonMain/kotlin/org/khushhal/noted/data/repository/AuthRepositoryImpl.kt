package org.khushhal.noted.data.repository

import org.khushhal.noted.data.remote.AuthApi
import org.khushhal.noted.domain.model.CurrentUser
import org.khushhal.noted.domain.model.User
import org.khushhal.noted.domain.repository.AuthRepository
import org.khushhal.noted.util.ResultState
import org.khushhal.noted.util.UserPreferences

class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun register(email: String, password: String): ResultState<String> {
        return try {
            val msg = authApi.register(email, password)
            ResultState.Success(msg)
        } catch (e: Exception) {
            ResultState.Failure(e.message ?: "Registration failed")
        }
    }

    override suspend fun login(email: String, password: String): ResultState<String> {
        return try {
            val token = authApi.login(email, password)
            if (!token.isNullOrEmpty()) {
                // Save user in memory
                CurrentUser.user = User(email = email, password = password, accessToken = token)
                ResultState.Success(token)
            } else {
                ResultState.Failure("Invalid credentials")
            }
        } catch (e: Exception) {
            ResultState.Failure(e.message ?: "Login failed")
        }
    }
}