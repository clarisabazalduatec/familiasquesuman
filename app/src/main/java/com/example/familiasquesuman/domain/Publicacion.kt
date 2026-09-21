package com.example.familiasquesuman.domain

data class Publicacion(
    val id: String,
    val autor: String,
    val tiempoRelativo: String,
    val contenido: String,
    val esOficial: Boolean,
    val imagenUrl: String? = null,
    val numeroLikes: Int = 0,
    val numeroComentarios: Int = 0
)
