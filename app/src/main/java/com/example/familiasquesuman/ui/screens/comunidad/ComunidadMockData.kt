package com.example.familiasquesuman.ui.screens.comunidad

import com.example.familiasquesuman.R


enum class TipoPublicacion { OFICIAL, COMUNIDAD }
enum class EstadoPublicacion { EN_REVISION, PUBLICADA, RECHAZADA }


data class ComentarioUi(
    val autor: String,
    val tiempo: String,
    val comentario: String,
    val likes: Int = 0,
    val numeroComentarios: Int = 0,
    val meGusta: Boolean = false,
    val autorFotoUrl: String? = null,
)


data class PublicacionComunidadUi(
    val id: String,
    val tipo: TipoPublicacion,
    val autor: String,
    val tiempoRelativo: String,
    val titulo: String? = null,
    val contenido: String,
    val categoria: String? = null,
    val imagenUrl: String? = null,
    val numeroLikes: Int = 0,
    val numeroComentarios: Int = 0,
    val meGusta: Boolean = false,
    val fechaEvento: String? = null,
    val horarioEvento: String? = null,
    val ubicacion: String? = null,
    val publicoObjetivo: String? = null,
    val documentoNombre: String? = null,
    val documentoDetalle: String? = null,
    val estado: EstadoPublicacion? = null,
    val comentarios: List<ComentarioUi> = emptyList(),
    val autorFotoUrl: String? = null
)



