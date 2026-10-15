package com.example.familiasquesuman.data.remote.dto

/** Un mensaje anterior de la conversación en pantalla. rol = "usuario" o "asistente". */
data class ChatTurnoDto(val rol: String, val contenido: String)

data class ChatMensajeEntradaDto(
    val mensaje: String,
    val historial: List<ChatTurnoDto> = emptyList()
)

data class ChatMensajeRespuestaDto(val respuesta: String)