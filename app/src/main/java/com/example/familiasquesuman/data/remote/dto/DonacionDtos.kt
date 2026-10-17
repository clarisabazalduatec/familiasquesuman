package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName

// ---------- DTOs para LECTURA (GET) ----------
data class DonacionOpcionDto(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("monto") val monto: Double
)

data class DonacionDto(
    @SerializedName("id") val id: String,
    @SerializedName("tipo") val tipo: String? = null,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion_corta") val descripcionCorta: String,
    @SerializedName("ubicacion") val ubicacion: String? = null,
    @SerializedName("ciudad") val ciudad: CiudadDto? = null,
    @SerializedName("organizacion") val organizacion: OrganizacionDto? = null,
    @SerializedName("organizacion_nombre") val organizacionNombre: String? = null,
    @SerializedName("imagenes") val imagenes: List<String>? = emptyList(),
    @SerializedName("modalidad") val modalidad: String,
    @SerializedName("categoria") val categoria: CategoriaDto? = null,
    @SerializedName("meta") val meta: Double? = null,
    @SerializedName("recaudado") val recaudado: Double? = 0.0,
    @SerializedName("opciones_disponibles") val opcionesDisponibles: Int? = null,
    @SerializedName("descripcion_larga") val descripcionLarga: String? = null,
    @SerializedName("whatsapp") val whatsapp: String? = null,
    @SerializedName("condiciones_recepcion") val condicionesRecepcion: String? = null,
    @SerializedName("fecha_limite") val fechaLimite: String? = null,
    @SerializedName("horario") val horario: String? = null,
    @SerializedName("opciones") val opciones: List<DonacionOpcionDto>? = emptyList(),
    @SerializedName("etiquetas") val etiquetas: Map<String, List<String>>? = emptyMap(),
    @SerializedName("activa") val activa: Boolean? = true
)

// ---------- DTOs para ESCRITURA (POST / PUT) ----------
data class DonacionOpcionInDto(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("monto") val monto: Double
)

data class DonacionGuardarDto(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion_corta") val descripcionCorta: String,
    @SerializedName("modalidad") val modalidad: String,
    @SerializedName(value = "categoria") val categoria: String? = null,
    @SerializedName("ubicacion") val ubicacion: String? = null,
    @SerializedName("ciudad_id") val ciudadId: Int = 1,
    @SerializedName("categoria_id") val categoriaId: Int = 1,
    @SerializedName("meta") val meta: Double? = null,
    @SerializedName("recaudado") val recaudado: Double? = null,
    @SerializedName("descripcion_larga") val descripcionLarga: String? = null,
    @SerializedName("whatsapp") val whatsapp: String? = null,
    @SerializedName("condiciones_recepcion") val condicionesRecepcion: String? = null,
    @SerializedName("fecha_limite") val fechaLimite: String? = null,
    @SerializedName("horario") val horario: String? = null,
    @SerializedName("activa") val activa: Boolean = true,
    @SerializedName("imagenes") val imagenes: List<String> = emptyList(),
    @SerializedName("opciones") val opciones: List<DonacionOpcionInDto> = emptyList(),
    @SerializedName("organizacion_nombre") val organizacionNombre: String? = null
)