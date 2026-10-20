
package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.CrearAvisoOficialRequest
import com.example.familiasquesuman.data.remote.dto.toDomain
import com.example.familiasquesuman.ui.screens.comunidad.PublicacionComunidadUi
import com.example.familiasquesuman.ui.screens.comunidad.ComentarioUi
import com.example.familiasquesuman.data.remote.dto.CrearComentarioRequest
import kotlin.coroutines.cancellation.CancellationException
import com.example.familiasquesuman.data.remote.dto.CrearPublicacionRequest
import com.example.familiasquesuman.data.remote.dto.RechazarPublicacionRequest
import com.example.familiasquesuman.data.remote.dto.PublicacionDto

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

    // Obtener las publicaciones del usuario actual
    suspend fun obtenerMisPublicaciones(): Result<List<PublicacionDto>> {
        return try {
            val publicaciones = RetrofitInstance.api.listarMisPublicaciones()
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
    // Crear una publicación de Comunidad para revisión
    suspend fun crearPublicacion(
        titulo: String,
        contenido: String,
        categoriaId: Int?,
        imagenes: List<String>,
        ciudadId: Int? = null,
        ubicacion: String? = null
    ): Result<PublicacionComunidadUi> {
        return try {
            val datos = CrearPublicacionRequest(
                titulo = titulo,
                contenido = contenido,
                ubicacion = ubicacion,
                ciudadId = ciudadId,
                categoriaId = categoriaId,
                imagenes = imagenes
            )
            val respuesta = RetrofitInstance.api.crearPublicacionComunidad(datos)
            Result.success(respuesta.toDomain())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear Aviso Oficial (solo administradores)
    suspend fun crearAvisoOficial(
        titulo: String?,
        contenido: String,
        imagenes: List<String>,
        eventoIniciaEn: String? = null,
        eventoTerminaEn: String? = null,
        publicoObjetivo: String? = null,
        ubicacion: String? = null
    ): Result<Unit> {
        return try {
            val datos = CrearAvisoOficialRequest(
                titulo = titulo,
                contenido = contenido,
                ubicacion = ubicacion,
                imagenes = imagenes,
                eventoIniciaEn = eventoIniciaEn,
                eventoTerminaEn = eventoTerminaEn,
                publicoObjetivo = publicoObjetivo,
            )
            RetrofitInstance.api.crearAvisoOficial(datos)
            Result.success(Unit)
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

    // ---------- Admin: Moderación de Comunidad ----------
    // Obtener publicaciones pendientes
    suspend fun obtenerPublicacionesPendientes(): Result<List<PublicacionComunidadUi>> {
        return try {
            val dtos = RetrofitInstance.api.listarPublicacionesPendientes()
            Result.success(dtos.map { it.toDomain() })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Últimas 10 publicaciones aprobadas
    suspend fun obtenerPublicacionesAprobadas(): Result<List<PublicacionDto>> {
        return try {
            val publicaciones = RetrofitInstance.api.listarPublicacionesAprobadas()
            Result.success(publicaciones)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Últimas 10 publicaciones rechazadas
    suspend fun obtenerPublicacionesRechazadas(): Result<List<PublicacionDto>> {
        return try {
            val publicaciones = RetrofitInstance.api.listarPublicacionesRechazadas()
            Result.success(publicaciones)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Aprobar una publicación
    suspend fun aprobarPublicacion(id: String): Result<Unit> {
        return try {
            RetrofitInstance.api.aprobarPublicacion(id)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Rechazar una publicación con motivo
    suspend fun rechazarPublicacion(
        id: String,
        motivo: String
    ): Result<Unit> {
        return try {
            val datos = RechazarPublicacionRequest(motivoRechazo = motivo)
            RetrofitInstance.api.rechazarPublicacion(id, datos)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
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
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404 || e.code() == 204) {
                Result.success(emptyList())
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.success(emptyList())
        }
    }

    // Eliminar publicación (admin)
    suspend fun eliminarPublicacion(id: String): Result<Unit> {
        return try {
            RetrofitInstance.api.eliminarPublicacion(id)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}
