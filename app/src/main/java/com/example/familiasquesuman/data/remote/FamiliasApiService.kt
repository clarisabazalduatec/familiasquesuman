package com.example.familiasquesuman.data.remote

import com.example.familiasquesuman.data.remote.dto.ChatMensajeEntradaDto
import com.example.familiasquesuman.data.remote.dto.ChatMensajeRespuestaDto
import com.example.familiasquesuman.data.remote.dto.LoginRequestDto
import com.example.familiasquesuman.data.remote.dto.TokenResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FamiliasApiService {
    @POST("auth/login")
    suspend fun login(@Body datos: LoginRequestDto): TokenResponseDto

    @POST("chat")
    suspend fun enviarMensajeChat(
        @Header("Authorization") token: String?,
        @Body entrada: ChatMensajeEntradaDto
    ): ChatMensajeRespuestaDto
}
