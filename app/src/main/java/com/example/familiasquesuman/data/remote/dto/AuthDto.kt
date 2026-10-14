package com.example.familiasquesuman.data.remote.dto

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class RegistroRequestDto(
    val nombre: String,
    val email: String,
    val password: String,
    val ubicacion: String? = null
)

data class UsuarioDto(
    val id: String,
    val nombre: String,
    val email: String,
    val ubicacion: String?,
    // "USUARIO" o "ADMIN". Nullable porque Gson deja null si el backend no lo manda.
    val rol: String? = "USUARIO"
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
    val ubicacion: String?,
    val rol: String?,
    val parentesco: String?,
    val familia_id: String?
) {
    fun aUsuarioDto() = UsuarioDto(id = id, nombre = nombre, email = email, ubicacion = ubicacion, rol = rol)
}