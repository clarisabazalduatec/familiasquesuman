
package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.example.familiasquesuman.ui.screens.comunidad.ComentarioUi

data class AutorComentarioDto(val id: String, val nombre: String)
data class ComentarioDto(
    val id: String,
    @SerializedName("publicacion_id")
    val publicacionId: String,
    val contenido: String,
    @SerializedName("creado_en")
    val creadoEn: String,
    val autor: AutorComentarioDto
)

data class CrearComentarioRequest(val contenido: String)
fun ComentarioDto.toDomain(): ComentarioUi {
    return ComentarioUi(
        autor = autor.nombre,
        tiempo = creadoEn.substring(0, 10).split("-").reversed().joinToString("/"),
        comentario = contenido
    )
}
