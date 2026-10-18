
package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.ui.screens.comunidad.PublicacionComunidadUi
import com.example.familiasquesuman.ui.screens.comunidad.TipoPublicacion
import com.example.familiasquesuman.ui.screens.comunidad.EstadoPublicacion

data class AutorPublicacionDto(
    @SerializedName("id") val id: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("foto_url") val fotoUrl: String? = null // "/imagenes/abc.jpg" o null

)

data class ImagenPublicacionDto(
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String,
    @SerializedName("orden") val orden: Int = 0
)

data class PublicacionDto(
    @SerializedName("id") val id: String,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("titulo") val titulo: String? = null,
    @SerializedName("contenido") val contenido: String,
    @SerializedName("ubicacion") val ubicacion: String? = null,
    @SerializedName("estado") val estado: String,
    @SerializedName("autor") val autor: AutorPublicacionDto,
    @SerializedName("categoria") val categoria: CategoriaDto? = null,
    @SerializedName("imagenes") val imagenes: List<ImagenPublicacionDto> = emptyList(),
    @SerializedName("creado_en") val creadoEn: String,
    @SerializedName("publicada_en") val publicadaEn: String? = null,
    @SerializedName("evento_inicia_en") val eventoIniciaEn: String? = null,
    @SerializedName("evento_termina_en") val eventoTerminaEn: String? = null,
    @SerializedName("publico_objetivo") val publicoObjetivo: String? = null,
    @SerializedName("documento_url") val documentoUrl: String? = null,
    @SerializedName("documento_nombre") val documentoNombre: String? = null,
    @SerializedName("total_likes") val totalLikes: Int = 0,
    @SerializedName("total_comentarios") val totalComentarios: Int = 0,
    @SerializedName("me_gusta") val meGusta: Boolean = false
)


fun PublicacionDto.toDomain(): PublicacionComunidadUi {
    val tipoUi = when (tipo) {
        "OFICIAL" -> TipoPublicacion.OFICIAL
        else -> TipoPublicacion.COMUNIDAD
    }

    val estadoUi = when (estado) {
        "PENDIENTE" -> EstadoPublicacion.EN_REVISION
        "APROBADA" -> EstadoPublicacion.PUBLICADA
        "RECHAZADA" -> EstadoPublicacion.RECHAZADA
        else -> null
    }

    val fecha = publicadaEn ?: creadoEn

    return PublicacionComunidadUi(
        id = id,
        tipo = tipoUi,
        autor = autor.nombre,
        tiempoRelativo = fecha,
        titulo = titulo,
        contenido = contenido,
        categoria = categoria?.nombre,
        imagenUrl = RetrofitInstance.urlCompleta(
            imagenes.firstOrNull()?.url
        ),
        numeroLikes = totalLikes,
        numeroComentarios = totalComentarios,
        meGusta = meGusta,
        fechaEvento = eventoIniciaEn,
        horarioEvento = if (eventoIniciaEn != null && eventoTerminaEn != null) {
            "$eventoIniciaEn - $eventoTerminaEn"
        } else {
            null
        },
        ubicacion = ubicacion,
        documentoNombre = documentoNombre,
        documentoDetalle = documentoUrl,
        estado = estadoUi,
        autorFotoUrl = autor.fotoUrl
    )
}
