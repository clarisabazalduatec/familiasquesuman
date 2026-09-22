package com.example.familiasquesuman.data.remote

import com.example.familiasquesuman.data.remote.dto.LoginRequestDto
import com.example.familiasquesuman.data.remote.dto.TokenResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface FamiliasApiService {
    @POST("auth/login")
    suspend fun login(@Body datos: LoginRequestDto): TokenResponseDto
}
