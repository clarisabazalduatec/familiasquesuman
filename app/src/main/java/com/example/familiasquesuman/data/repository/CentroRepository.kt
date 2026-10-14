package com.example.familiasquesuman.data.repository

import android.content.ContentResolver
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.CentroGuardarDto
import com.example.familiasquesuman.domain.CentroVisiteo

class CentroRepository(
    private val imagenes: ImagenRepository = ImagenRepository()
) {

    /** Centros activos (directorio público). */
    suspend fun listar(): Result<List<CentroVisiteo>> = llamarApi {
        RetrofitInstance.api.listarCentros().map { it.aDominio() }
    }

    suspend fun detalle(id: String): Result<CentroVisiteo> = llamarApi {
        RetrofitInstance.api.detalleCentro(id).aDominio()
    }

    // ---------- Admin ----------

    /** Todos los centros, incluidos los desactivados. */
    suspend fun listarAdmin(): Result<List<CentroVisiteo>> = llamarApi {
        RetrofitInstance.api.listarCentrosAdmin().map { it.aDominio() }
    }

    suspend fun detalleAdmin(id: String): Result<CentroVisiteo> = llamarApi {
        RetrofitInstance.api.detalleCentroAdmin(id).aDominio()
    }

    /** Crea (id == null) o reemplaza un centro. Si el logo es una foto del teléfono, primero la sube. */
    suspend fun guardar(
        id: String?,
        datos: CentroGuardarDto,
        resolver: ContentResolver
    ): Result<CentroVisiteo> = llamarApi {
        val logo = datos.logoUrl?.let { imagenes.subirSiHaceFalta(resolver, it) }
        val final = datos.copy(logoUrl = logo)
        val respuesta = if (id == null) {
            RetrofitInstance.api.crearCentro(final)
        } else {
            RetrofitInstance.api.editarCentro(id, final)
        }
        respuesta.aDominio()
    }

    suspend fun borrar(id: String): Result<Unit> = llamarApi {
        RetrofitInstance.api.borrarCentro(id)
    }
}