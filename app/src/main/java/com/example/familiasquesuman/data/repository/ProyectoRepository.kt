package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.domain.Proyecto

class ProyectoRepository {

    /** Proyectos visibles (activos y anteriores); el backend no regresa los desactivados. */
    suspend fun listar(): Result<List<Proyecto>> = llamarApi {
        RetrofitInstance.api.listarProyectos().map { it.aDominio() }
    }

    suspend fun detalle(id: String): Result<Proyecto> = llamarApi {
        RetrofitInstance.api.detalleProyecto(id).aDominio()
    }
}