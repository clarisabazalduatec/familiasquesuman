package com.example.familiasquesuman.domain

enum class TipoNotificacion {
    PARTICIPACION, INSIGNIA, COMENTARIO, DONACION, TALLER
}

data class Notificacion(
    val id: String,
    val tipo: TipoNotificacion,
    val titulo: String,
    val descripcion: String,
    val tiempo: String,
    val leida: Boolean = false
)