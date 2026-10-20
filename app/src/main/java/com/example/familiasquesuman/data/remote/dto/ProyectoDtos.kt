package com.example.familiasquesuman.data.remote.dto

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.domain.EstadoProyecto
import com.example.familiasquesuman.domain.OpcionApoyo
import com.example.familiasquesuman.domain.Proyecto
import com.example.familiasquesuman.domain.TipoApoyo
import com.google.gson.annotations.SerializedName

data class OpcionApoyoDto(
    val tipo: String,
    val titulo: String,
    val descripcion: String
)

/**
 * Sirve para GET /proyectos (resumen) y GET /proyectos/{id} (detalle):
 * en el resumen, los campos del detalle simplemente llegan en null.
 */
data class ProyectoDto(
    val id: String,
    val titulo: String,
    @SerializedName("descripcion_corta") val descripcionCorta: String,
    val ubicacion: String?,
    val ciudad: CiudadDto?,
    val imagenes: List<String>?,
    val estado: String,
    val beneficiarios: String,
    @SerializedName("logo_url") val logoUrl: String?,

    // solo en el detalle
    @SerializedName("acerca_de") val acercaDe: String?,
    @SerializedName("descripcion_larga") val descripcionLarga: String?,
    val whatsapp: String?,
    val telefono: String?,
    @SerializedName("como_ayudar") val comoAyudar: String?,
    @SerializedName("instagram_url") val instagramUrl: String?,
    @SerializedName("opciones_apoyo") val opcionesApoyo: List<OpcionApoyoDto>?
) {
    /** Convierte al modelo que ya usan las pantallas. */
    fun aDominio(): Proyecto = Proyecto(
        id = id,
        nombre = titulo,
        logoUrl = RetrofitInstance.urlCompleta(logoUrl ?: imagenes?.firstOrNull()),
        estado = runCatching { EstadoProyecto.valueOf(estado) }.getOrDefault(EstadoProyecto.ACTIVO),
        ubicacion = ciudad?.nombre ?: "",
        participantes = beneficiarios,
        descripcionCorta = descripcionCorta,
        acercaDe = acercaDe.orEmpty(),
        descripcionLarga = descripcionLarga.orEmpty(),
        opcionesApoyo = opcionesApoyo.orEmpty().map {
            OpcionApoyo(
                tipo = runCatching { TipoApoyo.valueOf(it.tipo) }.getOrDefault(TipoApoyo.TIEMPO_TALENTO),
                titulo = it.titulo,
                descripcion = it.descripcion
            )
        },
        telefonoWhatsapp = whatsapp?.ifBlank { null },
        telefonoLlamada = telefono?.ifBlank { null },
        imagenesUrl = imagenes.orEmpty().mapNotNull { RetrofitInstance.urlCompleta(it) },
        comoAyudar = comoAyudar?.ifBlank { null },
        instagramUrl = instagramUrl?.ifBlank { null },
        ubicacionUrl = ubicacion?.ifBlank { null },
        ciudadId = ciudad?.id
    )
}


// ---------- Lo que manda el admin (POST /proyectos y PUT /proyectos/{id}) ----------

data class OpcionApoyoGuardarDto(
    val titulo: String,
    val descripcion: String
)

/** Gson no manda los null: lo que vaya en null queda vacío en el backend. */
data class ProyectoGuardarDto(
    val titulo: String,
    @SerializedName("descripcion_corta") val descripcionCorta: String,
    val beneficiarios: String,
    val estado: String, // "ACTIVO" o "ANTERIOR"
    val ubicacion: String?,
    @SerializedName("ciudad_id") val ciudadId: Int?,
    @SerializedName("acerca_de") val acercaDe: String?,
    @SerializedName("descripcion_larga") val descripcionLarga: String?,
    @SerializedName("como_ayudar") val comoAyudar: String?,
    @SerializedName("instagram_url") val instagramUrl: String?,
    val whatsapp: String?,
    val telefono: String?,
    @SerializedName("logo_url") val logoUrl: String?,
    @SerializedName("opciones_apoyo") val opcionesApoyo: List<OpcionApoyoGuardarDto>
)

data class ImagenSubidaDto(
    val url: String // relativa, p. ej. "/media/abc.jpg"
)