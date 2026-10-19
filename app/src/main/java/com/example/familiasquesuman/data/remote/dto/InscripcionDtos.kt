package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName

data class InscripcionGuardarDto(
    @SerializedName("usuario_ids") val usuarioIds: List<String> = emptyList(),
    @SerializedName("menor_ids") val menorIds: List<String> = emptyList(),
    @SerializedName("lista_espera") val listaEspera: Boolean = false
)

data class ParticipanteDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("tipo") val tipo: String? = null, // "ADULTO" o "MENOR"
    @SerializedName("usuario_id") val usuarioId: String? = null,
    @SerializedName("menor_id") val menorId: String? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("asistencia") val asistencia: String? = null // "PENDIENTE", "ASISTIO", "FALTO"
)

data class InscripcionDto(
    @SerializedName("id") val id: String = "",
    @SerializedName("actividad_id") val actividadId: String? = null,
    @SerializedName("familia_id") val familiaId: String? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("posicion_espera") val posicionEspera: Int? = null,
    @SerializedName("creado_en") val creadoEn: String? = null,
    @SerializedName("cancelada_en") val canceladaEn: String? = null,
    @SerializedName("participantes") val participantes: List<ParticipanteDto> = emptyList()
)

data class MiInscripcionDto(
    @SerializedName("id") val id: String = "",
    @SerializedName("actividad_id") val actividadId: String? = null,
    @SerializedName("familia_id") val familiaId: String? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("posicion_espera") val posicionEspera: Int? = null,
    @SerializedName("creado_en") val creadoEn: String? = null,
    @SerializedName("cancelada_en") val canceladaEn: String? = null,
    @SerializedName("participantes") val participantes: List<ParticipanteDto> = emptyList(),
    @SerializedName("actividad") val actividad: ActividadDto? = null,
    @SerializedName("encuesta_respondida") val encuestaRespondida: Boolean = false
) {
    fun toInscripcionDto(): InscripcionDto = InscripcionDto(
        id = id,
        actividadId = actividadId,
        familiaId = familiaId,
        estado = estado,
        posicionEspera = posicionEspera,
        creadoEn = creadoEn,
        canceladaEn = canceladaEn,
        participantes = participantes
    )
}

data class InscripcionAdminDto(
    @SerializedName("id") val id: String = "",
    @SerializedName("actividad_id") val actividadId: String? = null,
    @SerializedName("familia_id") val familiaId: String? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("posicion_espera") val posicionEspera: Int? = null,
    @SerializedName("creado_en") val creadoEn: String? = null,
    @SerializedName("cancelada_en") val canceladaEn: String? = null,
    @SerializedName("participantes") val participantes: List<ParticipanteDto> = emptyList(),
    @SerializedName("familia") val familia: FamiliaBreveDto? = null,
    @SerializedName("inscrito_por") val inscritoPor: UsuarioBreveDto? = null
) {
    fun toInscripcionDto(): InscripcionDto = InscripcionDto(
        id = id,
        actividadId = actividadId,
        familiaId = familiaId,
        estado = estado,
        posicionEspera = posicionEspera,
        creadoEn = creadoEn,
        canceladaEn = canceladaEn,
        participantes = participantes
    )
}

data class AsistenciaItemDto(
    @SerializedName("participante_id") val participanteId: Int = 0,
    @SerializedName("asistencia") val asistencia: String = "PENDIENTE"
)

data class AsistenciaGuardarDto(
    @SerializedName("items") val items: List<AsistenciaItemDto> = emptyList()
)

data class EncuestaInicialRequest(
    @SerializedName("motivos")
    val motivos: List<String>,

    @SerializedName("otro_motivo")
    val otroMotivo: String? = null,

    @SerializedName("expectativas")
    val expectativas: List<String>,

    @SerializedName("otra_expectativa")
    val otraExpectativa: String? = null
)

data class EstadoEncuestasResponse(
    @SerializedName("encuesta_inicial_respondida") val encuestaInicialRespondida: Boolean,
    @SerializedName("encuesta_final_respondida") val encuestaFinalRespondida: Boolean
)