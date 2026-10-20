package com.example.familiasquesuman.data.remote.dto

data class ContactoRequestDto(
    val nombre: String,
    val correo: String,
    val mensaje: String
)

data class ContactoResponseDto(
    val mensaje: String
)