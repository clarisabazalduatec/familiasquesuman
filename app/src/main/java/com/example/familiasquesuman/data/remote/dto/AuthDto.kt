package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class RegistroRequestDto(
    val nombre: String,
    val email: String,
    val password: String,
    @SerializedName("ciudad_id") val ciudadId: Int? = null
)

data class UsuarioDto(
    val id: String,
    val nombre: String,
    val email: String,
    val ciudad: CiudadDto? = null,
    // "USUARIO" o "ADMIN". Nullable porque Gson deja null si el backend no lo manda.
    val rol: String? = "USUARIO",
    // "/imagenes/abc.jpg" o null si no tiene foto
    @SerializedName("foto_url") val fotoUrl: String? = null,
    // false = falta escribir el código que llegó al correo. null (backend viejo) se toma como verificado.
    @SerializedName("correo_verificado") val correoVerificado: Boolean? = null
)

data class TokenResponseDto(
    val access_token: String,
    val token_type: String,
    val usuario: UsuarioDto
)

/** Respuesta de GET /usuarios/me */
data class PerfilDto(
    val id: String,
    val nombre: String,
    val email: String,
    val ciudad: CiudadDto?,
    val rol: String?,
    val parentesco: String?,
    val familia_id: String?,
    @SerializedName("foto_url") val fotoUrl: String? = null,
    @SerializedName("correo_verificado") val correoVerificado: Boolean? = null
) {
    fun aUsuarioDto() = UsuarioDto(
        id = id, nombre = nombre, email = email, ciudad = ciudad, rol = rol, fotoUrl = fotoUrl,
        correoVerificado = correoVerificado
    )
}

/** POST /auth/verificacion/confirmar */
data class CodigoVerificacionDto(val codigo: String)

/** Respuesta de POST /auth/verificacion/enviar */
data class EnvioCodigoDto(
    val mensaje: String,
    @SerializedName("expira_en_minutos") val expiraEnMinutos: Int = 10
)