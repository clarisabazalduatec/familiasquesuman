package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.NotificacionDto
import com.example.familiasquesuman.data.remote.dto.ResumenNotificacionesDto
import com.example.familiasquesuman.notificaciones.EstadoNotificaciones

class NotificacionesRepository {

    private val api get() = RetrofitInstance.api

    /** Las más recientes primero (pantalla de la campana). */
    suspend fun listar(): Result<List<NotificacionDto>> = llamarApi { api.misNotificaciones() }

    suspend fun resumen(): Result<ResumenNotificacionesDto> = llamarApi { api.resumenNotificaciones() }

    suspend fun marcarLeida(id: Long): Result<Unit> = llamarApi { api.marcarNotificacionLeida(id) }

    suspend fun marcarTodas(): Result<Unit> = llamarApi { api.marcarTodasLeidas() }

    /** Actualiza el numerito de la campana (si falla, se queda el que había). */
    suspend fun refrescarContador() {
        resumen().onSuccess { EstadoNotificaciones.noLeidas = it.noLeidas }
    }
}