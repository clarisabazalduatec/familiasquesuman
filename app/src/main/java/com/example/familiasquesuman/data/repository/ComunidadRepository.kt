
package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.toDomain
import com.example.familiasquesuman.ui.screens.comunidad.PublicacionComunidadUi
import com.example.familiasquesuman.ui.screens.comunidad.ComentarioUi
import com.example.familiasquesuman.data.remote.dto.CrearComentarioRequest
import kotlin.coroutines.cancellation.CancellationException

class ComunidadRepository {

    suspend fun obtenerPublicaciones(): Result<List<PublicacionComunidadUi>> {
        return try {
            val dtos = RetrofitInstance.api.listarPublicacionesComunidad()
            val publicaciones = dtos.map { it.toDomain() }
            Result.success(publicaciones)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun obtenerPublicacionPorId(id: String): Result<PublicacionComunidadUi> {
        return try {
            val dto = RetrofitInstance.api.detallePublicacionComunidad(id)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerAvisosOficiales(): Result<List<PublicacionComunidadUi>> {
        return try {
            val dtos = RetrofitInstance.api.listarAvisosOficiales()
            val avisos = dtos.map { it.toDomain() }
            Result.success(avisos)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //likes
    suspend fun darLike(id: String): Result<Unit> {
        return try {
            RetrofitInstance.api.darLikePublicacion(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun quitarLike(id: String): Result<Unit> {
        return try {
            RetrofitInstance.api.quitarLikePublicacion(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //comentarios

    suspend fun publicarComentario(
        publicacionId: String,
        contenido: String
    ): Result<ComentarioUi> {
        return try {
            val respuesta = RetrofitInstance.api.publicarComentario(
                id = publicacionId,
                comentario = CrearComentarioRequest(contenido = contenido)
            )

            Result.success(respuesta.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun obtenerComentarios(publicacionId: String): Result<List<ComentarioUi>> {
        return try {
            val comentarios = RetrofitInstance.api.obtenerComentariosPublicacion(publicacionId)
            Result.success(comentarios.map { it.toDomain() })
        } catch (e: Exception) { Result.failure(e) }
    }


}
