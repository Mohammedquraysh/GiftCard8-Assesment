package com.data.repository

import com.data.remote.api.AuthApi
import com.data.remote.dto.AuthDto.LoginRequest
import com.domain.model.User
import com.presentation.common.resource.Resource
import com.util.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val preferencesManager: PreferencesManager
) {

    suspend fun login(username: String, password: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())

            val response = authApi.login(LoginRequest(username, password))

            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    val user = User(
                        id = loginResponse.id.toString(),
                        email = loginResponse.email,
                        username = loginResponse.username,
                        firstName = loginResponse.firstName,
                        lastName = loginResponse.lastName
                    )

                    // Save token and user ID
                    preferencesManager.saveToken(loginResponse.accessToken)
                    preferencesManager.saveUserId(loginResponse.id.toString())

                    emit(Resource.Success(user))
                } ?: emit(Resource.Error("Login failed"))
            } else {
                emit(Resource.Error("Invalid credentials"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Network error"))
        }
    }

     fun logout(): Flow<Resource<Unit>> = flow {
        try {
            preferencesManager.clearToken()
            preferencesManager.clearUserId()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Logout failed"))
        }
    }
}