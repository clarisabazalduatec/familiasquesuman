package com.example.familiasquesuman.data.remote.dto

/** Un mensaje anterior de la conversación en pantalla. rol = "usuario" o "asistente". */
data class ChatTurnoDto(val rol: String, val contenido: String)

data class ChatMensajeEntradaDto(
    val mensaje: String,
    val historial: List<ChatTurnoDto> = emptyList()
)

/** tipo = "actividad", "proyecto", "donacion" o "centro". */
data class ChatEnlaceDto(val tipo: String, val id: String, val titulo: String)

data class ChatMensajeRespuestaDto(
    val respuesta: String,
    val enlaces: List<ChatEnlaceDto>? = null
)