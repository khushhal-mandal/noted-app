package org.khushhal.noted.presentation.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.khushhal.noted.data.remote.AuthApi
import org.khushhal.noted.domain.model.CurrentUser
import org.khushhal.noted.domain.model.User
import org.khushhal.noted.util.ResultState
import org.khushhal.noted.util.UserRepository

class AuthScreenModel(
    private val authApi: AuthApi,
    val userPreferences: UserRepository
) {

    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    /** Login user and save token in CurrentUser */
    fun login(email: String, password: String) {
        coroutineScope.launch {
            _state.value = _state.value.copy(loginState = ResultState.Loading)
            try {
                val token = authApi.login(email, password)
                if (!token.isNullOrEmpty()) {
                    userPreferences.saveUser(email = email, password = password, token = token)
                    CurrentUser.user = User(email = email, accessToken = token)
                    _state.value = _state.value.copy(loginState = ResultState.Success(token))
                } else {
                    _state.value = _state.value.copy(loginState = ResultState.Failure("Invalid credentials"))
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(loginState = ResultState.Failure(e.message ?: "Unknown error"))
            }
        }
    }

    /** Register user */
    fun register(email: String, password: String) {
        coroutineScope.launch {
            _state.value = _state.value.copy(registerState = ResultState.Loading)
            try {
                val msg = authApi.register(email, password)
                _state.value = _state.value.copy(registerState = ResultState.Success(msg))
            } catch (e: Exception) {
                _state.value = _state.value.copy(registerState = ResultState.Failure(e.message ?: "Registration failed"))
            }
        }
    }
}

data class AuthState(
    val loginState: ResultState<String> = ResultState.Loading,
    val registerState: ResultState<String> = ResultState.Loading
)