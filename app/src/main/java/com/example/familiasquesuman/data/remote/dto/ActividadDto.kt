package com.example.familiasquesuman.data.remote.dto

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.domain.Actividad
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class CiudadDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("estado") val estado: String? = null
)

data class OrganizacionDto(
    @SerializedName("id") val id: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("logo_url") val logoUrl: String? = null,
    @SerializedName("verificada") val verificada: Boolean = false
)

data class CategoriaDto(
    @SerializedName("id") val id: Int,
    @SerializedName("ambito") val ambito: String? = null,
    @SerializedName("nombre") val nombre: String
)

data class CategoriaCrearDto(
    @SerializedName("ambito") val ambito: String,
    @SerializedName("nombre") val nombre: String
)

data class ActividadDto(
    @SerializedName("id") val id: String,
    @SerializedName("tipo") val tipo: String? = null,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion_corta") val descripcionCorta: String? = null,
    @SerializedName("ubicacion") val ubicacion: String? = null,
    @SerializedName("ciudad") val ciudad: CiudadDto? = null,
    @SerializedName("organizacion") val organizacion: OrganizacionDto? = null,
    @SerializedName("imagenes") val imagenes: List<String>? = emptyList(),
    @SerializedName("categoria") val categoria: CategoriaDto? = null,
    @SerializedName("inicia_en") val iniciaEn: String? = null,
    @SerializedName("termina_en") val terminaEn: String? = null,
    @SerializedName("cupo_total") val cupoTotal: Int? = null,
    @SerializedName("plazas_disponibles") val plazasDisponibles: Int? = null,
    @SerializedName("acerca_de") val acercaDe: String? = null,
    @SerializedName("activa") val activa: Boolean? = null
)

data class ActividadGuardarDto(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion_corta") val descripcionCorta: String,
    @SerializedName("ubicacion") val ubicacion: String? = null,
    @SerializedName("ciudad_id") val ciudadId: Int? = null,
    @SerializedName("categoria_id") val categoriaId: Int,
    @SerializedName("organizacion_nombre") val organizacionNombre: String? = null,
    @SerializedName("inicia_en") val iniciaEn: String,
    @SerializedName("termina_en") val terminaEn: String,
    @SerializedName("cupo_total") val cupoTotal: Int,
    @SerializedName("acerca_de") val acercaDe: String? = null,
    @SerializedName("activa") val activa: Boolean = true,
    @SerializedName("imagenes") val imagenes: List<String> = emptyList()
)

private fun parseIsoDate(isoString: String?): Date? {
    if (isoString.isNullOrBlank()) return null
    return try {
        val clean = isoString.replace(Regex("\\.\\d+"), "")
        val hasTimezone = clean.endsWith("Z") || (clean.length > 10 && (clean.substring(10).contains("+") || clean.substring(10).contains("-")))
        if (hasTimezone) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US).parse(clean)
        } else {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("America/Monterrey")
            }.parse(clean)
        }
    } catch (_: Exception) {
        null
    }
}

private fun formatFecha(date: Date?): String {
    if (date == null) return ""
    val localeEsMx = Locale.forLanguageTag("es-MX")
    val sdf = SimpleDateFormat("EEE, d 'de' MMM", localeEsMx).apply {
        timeZone = TimeZone.getTimeZone("America/Monterrey")
    }
    val raw = sdf.format(date)
    return raw.replace(".", "").split(" ").joinToString(" ") { palabra ->
        if (palabra.equals("de", ignoreCase = true)) "de"
        else palabra.replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeEsMx) else it.toString() }
    }
}

private fun formatHorario(iniciaDate: Date?, terminaDate: Date?): String {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("America/Monterrey")
    }
    val inicio = iniciaDate?.let { timeFormat.format(it) } ?: ""
    val fin = terminaDate?.let { timeFormat.format(it) } ?: ""
    return when {
        inicio.isNotBlank() && fin.isNotBlank() -> "$inicio - $fin"
        inicio.isNotBlank() -> inicio
        else -> ""
    }
}

fun ActividadDto.toDomain(): Actividad {
    val iniciaDate = parseIsoDate(iniciaEn)
    val terminaDate = parseIsoDate(terminaEn)
    val ubicacionFinal = if (!ubicacion.isNullOrBlank()) {
        ubicacion
    } else {
        ciudad?.nombre ?: "Sin ubicación"
    }
    val organizadorNombre = organizacion?.nombre ?: "Organización comunitaria"
    val esVerificado = organizacion?.verificada ?: false
    val acercaDeTexto = acercaDe ?: descripcionCorta ?: ""

    return Actividad(
        id = id,
        titulo = titulo,
        descripcion = descripcionCorta ?: "",
        categoria = categoria?.nombre ?: "General",
        fecha = formatFecha(iniciaDate),
        horario = formatHorario(iniciaDate, terminaDate),
        ubicacion = ubicacionFinal,
        organizador = organizadorNombre,
        organizadorVerificado = esVerificado,
        acercaDe = acercaDeTexto,
        lugaresDisponibles = plazasDisponibles ?: 0,
        lugaresTotales = cupoTotal ?: 0,
        imagenes = imagenes.orEmpty().mapNotNull { RetrofitInstance.urlCompleta(it) },
        iniciaEn = iniciaDate,
        terminaEn = terminaDate,
        activa = activa ?: true,
        categoriaId = categoria?.id
    )
}
