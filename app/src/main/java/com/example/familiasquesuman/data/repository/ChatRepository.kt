package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.ChatMensajeEntradaDto
import com.example.familiasquesuman.domain.MensajeChat

class ChatRepository {
    suspend fun enviarMensaje(texto: String): Result<MensajeChat> {
        return try {
            // El token (si hay sesión) lo agrega el AuthInterceptor.
            val respuestaDto = RetrofitInstance.api.enviarMensajeChat(
                ChatMensajeEntradaDto(mensaje = texto)
            )
            Result.success(
                MensajeChat(
                    contenido = respuestaDto.respuesta,
                    esDelUsuario = false
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}