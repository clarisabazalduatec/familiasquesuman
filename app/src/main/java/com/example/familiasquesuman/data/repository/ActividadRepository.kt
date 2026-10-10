package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.toDomain
import com.example.familiasquesuman.domain.Actividad

class ActividadRepository {
    suspend fun obtenerActividades(): Result<List<Actividad>> {
        return try {
            val dtos = RetrofitInstance.api.getActividades()
            val actividades = dtos.map { it.toDomain() }
            Result.success(actividades)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerActividadPorId(id: String): Result<Actividad> {
        return try {
            val dto = RetrofitInstance.api.getActividadDetalle(id)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerCategorias(): Result<List<String>> {
        return try {
            val dtos = RetrofitInstance.api.getCategorias("ACTIVIDAD")
            val nombres = dtos.map { it.nombre }.filter { it.isNotBlank() }.distinct()
            Result.success(nombres)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
