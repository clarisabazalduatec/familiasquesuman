package com.example.familiasquesuman.domain

data class MensajeChat(
    val contenido: String,
    val esDelUsuario: Boolean,
    /** Iniciativas que menciona la respuesta: se muestran como botones que abren su detalle. */
    val enlaces: List<EnlaceChat> = emptyList()
)

/** tipo = "actividad", "proyecto", "donacion" o "centro". */
data class EnlaceChat(
    val tipo: String,
    val id: String,
    val titulo: String
)