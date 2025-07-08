package com.data.remote.api

import com.data.remote.dto.AuthDto.LoginRequest
import com.data.remote.dto.AuthDto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
