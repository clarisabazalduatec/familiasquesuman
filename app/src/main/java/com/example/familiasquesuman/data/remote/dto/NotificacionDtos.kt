package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName

/** Respuesta de GET /notificaciones */
data class NotificacionDto(
    val id: Long,
    // PUBLICACION_APROBADA, PUBLICACION_RECHAZADA, NUEVO_COMENTARIO, NUEVOS_LIKES, INVITACION_FAMILIA,
    // AVISO_OFICIAL, RECORDATORIO_ACTIVIDAD, PUBLICACION_POR_REVISAR (admin), ACTIVIDAD_LLENA (admin)
    val tipo: String,
    val titulo: String,
    val cuerpo: String,
    // A qué pantalla lleva: PUBLICACION, AVISO, MIS_PUBLICACIONES, INVITACIONES, ACTIVIDAD, MODERACION
    @SerializedName("destino_tipo") val destinoTipo: String?,
    @SerializedName("destino_id") val destinoId: String?,
    val leida: Boolean,
    @SerializedName("creado_en") val creadoEn: String // ISO en UTC: "2026-10-16T21:43:00.123Z"
)

/** Respuesta de GET /notificaciones/resumen */
data class ResumenNotificacionesDto(
    @SerializedName("no_leidas") val noLeidas: Int,
    @SerializedName("ultimo_id") val ultimoId: Long?
)