package com.example.familiasquesuman.data.remote.dto

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class UsuarioDto(
    val id: String,
    val nombre: String,
    val email: String,
    val ubicacion: String?
)

data class TokenResponseDto(
    val access_token: String,
    val token_type: String,
    val usuario: UsuarioDto
)