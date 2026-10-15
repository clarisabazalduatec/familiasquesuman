package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.ChatMensajeEntradaDto
import com.example.familiasquesuman.data.remote.dto.ChatTurnoDto
import com.example.familiasquesuman.domain.EnlaceChat
import com.example.familiasquesuman.domain.MensajeChat

class ChatRepository {
    /** historial = la conversación que hay en pantalla (el backend no guarda nada). */
    suspend fun enviarMensaje(texto: String, historial: List<ChatTurnoDto>): Result<MensajeChat> {
        return try {
            // El token (si hay sesión) lo agrega el AuthInterceptor.
            val respuestaDto = RetrofitInstance.api.enviarMensajeChat(
                ChatMensajeEntradaDto(mensaje = texto, historial = historial)
            )
            Result.success(
                MensajeChat(
                    contenido = respuestaDto.respuesta,
                    esDelUsuario = false,
                    enlaces = respuestaDto.enlaces.orEmpty().map {
                        EnlaceChat(tipo = it.tipo, id = it.id, titulo = it.titulo)
                    }
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}