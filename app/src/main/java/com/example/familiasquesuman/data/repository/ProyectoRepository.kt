package com.example.familiasquesuman.data.repository

import android.content.ContentResolver
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.ProyectoGuardarDto
import com.example.familiasquesuman.domain.Proyecto

class ProyectoRepository(
    private val imagenes: ImagenRepository = ImagenRepository()
) {

    /** Proyectos visibles (activos y anteriores). */
    suspend fun listar(): Result<List<Proyecto>> = llamarApi {
        RetrofitInstance.api.listarProyectos().map { it.aDominio() }
    }

    suspend fun detalle(id: String): Result<Proyecto> = llamarApi {
        RetrofitInstance.api.detalleProyecto(id).aDominio()
    }

    // ---------- Admin ----------

    /**
     * Crea (id == null) o reemplaza un proyecto. Si el logo es una foto del teléfono,
     * primero la sube y guarda la URL que regresa el backend.
     */
    suspend fun guardar(
        id: String?,
        datos: ProyectoGuardarDto,
        resolver: ContentResolver
    ): Result<Proyecto> = llamarApi {
        val logo = datos.logoUrl?.let { imagenes.subirSiHaceFalta(resolver, it) }
        val final = datos.copy(logoUrl = logo)
        val respuesta = if (id == null) {
            RetrofitInstance.api.crearProyecto(final)
        } else {
            RetrofitInstance.api.editarProyecto(id, final)
        }
        respuesta.aDominio()
    }

    suspend fun borrar(id: String): Result<Unit> = llamarApi {
        RetrofitInstance.api.borrarProyecto(id)
    }
}