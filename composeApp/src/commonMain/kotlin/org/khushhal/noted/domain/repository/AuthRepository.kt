package org.khushhal.noted.domain.repository

import kotlinx.coroutines.flow.Flow
import org.khushhal.noted.util.ResultState

interface AuthRepository {
    suspend fun register(email: String, password: String): ResultState<String>
    suspend fun login(email: String, password: String): ResultState<String>
    //logout
}