package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName

// ---------- Lo que manda el backend ----------

data class MiembroDto(
    val id: String,
    val nombre: String,
    val email: String,
    val parentesco: String? // MAMA, PAPA, TUTOR, ABUELO, OTRO
)

data class MenorDto(
    val id: String,
    val nombre: String,
    @SerializedName("fecha_nacimiento") val fechaNacimiento: String, // "AAAA-MM-DD"
    val edad: Int
)

data class UsuarioBreveDto(
    val id: String,
    val nombre: String,
    val email: String
)

data class InvitacionEnviadaDto(
    val id: String,
    val invitado: UsuarioBreveDto,
    val estado: String,
    @SerializedName("creado_en") val creadoEn: String
)

data class FamiliaDto(
    val id: String,
    val nombre: String?,
    @SerializedName("creado_en") val creadoEn: String,
    val adultos: List<MiembroDto>,
    val menores: List<MenorDto>,
    @SerializedName("invitaciones_pendientes") val invitacionesPendientes: List<InvitacionEnviadaDto>
)

data class FamiliaBreveDto(
    val id: String,
    val nombre: String?
)

data class InvitacionRecibidaDto(
    val id: String,
    val familia: FamiliaBreveDto,
    @SerializedName("invitado_por") val invitadoPor: UsuarioBreveDto?,
    val estado: String,
    @SerializedName("creado_en") val creadoEn: String
)

// ---------- Lo que manda la app ----------
// Gson no manda los campos null, así que en los PATCH solo viaja lo que cambió.

data class FamiliaCrearDto(
    val nombre: String?,
    val parentesco: String?
)

data class MenorGuardarDto(
    val nombre: String,
    @SerializedName("fecha_nacimiento") val fechaNacimiento: String
)

data class InvitacionCrearDto(
    val email: String
)

data class PerfilActualizarDto(
    val nombre: String? = null,
    val ubicacion: String? = null,
    val parentesco: String? = null
)

// ---------- Parentescos: código del backend <-> texto en pantalla ----------

object Parentescos {
    val opciones: List<Pair<String, String>> = listOf(
        "MAMA" to "Mamá",
        "PAPA" to "Papá",
        "TUTOR" to "Tutor/a",
        "ABUELO" to "Abuelo/a",
        "OTRO" to "Otro"
    )

    fun etiqueta(codigo: String?): String? =
        opciones.firstOrNull { it.first == codigo }?.second
}