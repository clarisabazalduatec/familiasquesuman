package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName

/** Paso 1 (POST /contacto/solicitar-codigo): los datos del mensaje. El backend manda un código al correo. */
data class ContactoRequestDto(
    val nombre: String,
    val correo: String,
    val mensaje: String
)

/** Respuesta del paso 1. */
data class ContactoCodigoResponseDto(
    val mensaje: String,
    @SerializedName("expira_en_minutos") val expiraEnMinutos: Int = 10
)

/** Paso 2 (POST /contacto/confirmar-codigo): el código de 6 dígitos que llegó al correo. */
data class ConfirmarCodigoDto(
    val correo: String,
    val codigo: String
)

/** Respuesta del paso 2: el mensaje ya se envió a Familias que Suman. */
data class ContactoResponseDto(
    val mensaje: String
)