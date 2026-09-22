package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.SesionUsuario
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.LoginRequestDto

class AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val respuesta = RetrofitInstance.api.login(LoginRequestDto(email, password))
            SesionUsuario.guardarSesion(respuesta.access_token, respuesta.usuario)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}