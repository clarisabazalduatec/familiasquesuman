package com.example.familiasquesuman.data.remote.dto

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.domain.CentroVisiteo
import com.example.familiasquesuman.domain.TipoCentro
import com.google.gson.annotations.SerializedName

/**
 * Sirve para GET /centros (resumen), GET /centros/{id} (detalle) y las rutas de admin:
 * lo que no venga en la respuesta simplemente llega en null.
 */
data class CentroDto(
    val id: String,
    val tipo: String,
    val nombre: String,
    @SerializedName("descripcion_corta") val descripcionCorta: String,
    val direccion: String,
    @SerializedName("logo_url") val logoUrl: String?,
    val verificado: Boolean?,

    // detalle
    @SerializedName("informacion_general") val informacionGeneral: String?,
    val necesidades: List<String>?,
    @SerializedName("como_ayudar") val comoAyudar: String?,
    val recomendaciones: String?,
    val telefono: String?,
    val whatsapp: String?,
    @SerializedName("mapa_url") val mapaUrl: String?,

    // solo admin (en lo público siempre es true)
    val activo: Boolean?
) {
    fun aDominio(): CentroVisiteo = CentroVisiteo(
        id = id,
        tipo = runCatching { TipoCentro.valueOf(tipo) }.getOrDefault(TipoCentro.ASILO),
        nombre = nombre,
        activo = activo ?: true,
        descripcionCorta = descripcionCorta,
        informacionGeneral = informacionGeneral.orEmpty(),
        necesidades = necesidades.orEmpty(),
        direccion = direccion,
        mapaUrl = mapaUrl?.ifBlank { null },
        logoUrl = RetrofitInstance.urlCompleta(logoUrl),
        comoAyudar = comoAyudar?.ifBlank { null },
        recomendaciones = recomendaciones?.ifBlank { null },
        telefono = telefono?.ifBlank { null },
        whatsapp = whatsapp?.ifBlank { null },
        verificado = verificado ?: true
    )
}

/** Lo que manda el admin (POST /centros y PUT /centros/{id}). */
data class CentroGuardarDto(
    val tipo: String,
    val nombre: String,
    @SerializedName("descripcion_corta") val descripcionCorta: String,
    @SerializedName("informacion_general") val informacionGeneral: String,
    val direccion: String,
    @SerializedName("mapa_url") val mapaUrl: String?,
    @SerializedName("logo_url") val logoUrl: String?,
    @SerializedName("como_ayudar") val comoAyudar: String?,
    val recomendaciones: String?,
    val telefono: String?,
    val whatsapp: String?,
    val verificado: Boolean,
    val activo: Boolean,
    val necesidades: List<String>
)