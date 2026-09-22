package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.SesionUsuario
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.ChatMensajeEntradaDto
import com.example.familiasquesuman.domain.MensajeChat

class ChatRepository {
    suspend fun enviarMensaje(texto: String): Result<MensajeChat> {
        return try {
            val token = SesionUsuario.token?.let { "Bearer $it" }
            val respuestaDto = RetrofitInstance.api.enviarMensajeChat(
                token = token,
                entrada = ChatMensajeEntradaDto(mensaje = texto)
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