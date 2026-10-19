package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.AsistenciaGuardarDto
import com.example.familiasquesuman.data.remote.dto.AsistenciaItemDto
import com.example.familiasquesuman.data.remote.dto.EncuestaInicialRequest
import com.example.familiasquesuman.data.remote.dto.InscripcionAdminDto
import com.example.familiasquesuman.data.remote.dto.InscripcionDto
import com.example.familiasquesuman.data.remote.dto.InscripcionGuardarDto
import com.example.familiasquesuman.data.remote.dto.MiInscripcionDto
import retrofit2.HttpException

class InscripcionRepository {

    private val api get() = RetrofitInstance.api

    suspend fun inscribir(
        actividadId: String,
        usuarioIds: List<String>,
        menorIds: List<String>,
        listaEspera: Boolean = false
    ): Result<InscripcionDto> = llamarApi {
        api.inscribirActividad(actividadId, InscripcionGuardarDto(usuarioIds, menorIds, listaEspera))
    }

    suspend fun modificarInscripcion(
        actividadId: String,
        usuarioIds: List<String>,
        menorIds: List<String>,
        listaEspera: Boolean = false
    ): Result<InscripcionDto> = llamarApi {
        api.modificarInscripcionActividad(actividadId, InscripcionGuardarDto(usuarioIds, menorIds, listaEspera))
    }

    /** null = el usuario/familia no está inscrito en esta actividad (el backend responde 404). */
    suspend fun miInscripcion(actividadId: String): Result<InscripcionDto?> = llamarApi {
        try {
            api.miInscripcionActividad(actividadId)
        } catch (e: HttpException) {
            if (e.code() == 404) null else throw e
        }
    }

    suspend fun cancelarInscripcion(actividadId: String): Result<Unit> = llamarApi {
        api.cancelarInscripcionActividad(actividadId)
    }

    /** Si no tiene familia o no tiene inscripciones y el backend responde 404, regresa lista vacía. */
    suspend fun misInscripciones(incluirCanceladas: Boolean = false): Result<List<MiInscripcionDto>> = llamarApi {
        try {
            api.misInscripcionesFamilia(incluirCanceladas)
        } catch (e: HttpException) {
            if (e.code() == 404) emptyList() else throw e
        }
    }

    suspend fun listarInscripcionesAdmin(actividadId: String): Result<List<InscripcionAdminDto>> = llamarApi {
        api.listarInscripcionesAdmin(actividadId)
    }

    suspend fun guardarAsistenciaAdmin(
        actividadId: String,
        items: List<AsistenciaItemDto>
    ): Result<List<InscripcionAdminDto>> = llamarApi {
        api.guardarAsistenciaAdmin(actividadId, AsistenciaGuardarDto(items))
    }

    suspend fun borrarInscripcionAdmin(
        actividadId: String,
        inscripcionId: Any
    ): Result<Unit> = llamarApi {
        api.borrarInscripcionAdmin(actividadId, inscripcionId)
    }

    suspend fun enviarEncuestaInicial(
        actividadId: String,
        request: EncuestaInicialRequest
    ): Result<Unit> = runCatching {
        val res = api.enviarEncuestaInicial(actividadId, request)
        if (!res.isSuccessful) {
            error("Error al enviar la encuesta inicial")
        }
    }
}
