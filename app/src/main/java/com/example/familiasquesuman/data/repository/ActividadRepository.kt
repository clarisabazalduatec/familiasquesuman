package com.example.familiasquesuman.data.repository

import android.content.ContentResolver
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.ActividadGuardarDto
import com.example.familiasquesuman.data.remote.dto.CategoriaCrearDto
import com.example.familiasquesuman.data.remote.dto.CategoriaDto
import com.example.familiasquesuman.data.remote.dto.toDomain
import com.example.familiasquesuman.domain.Actividad

class ActividadRepository(
    private val imagenes: ImagenRepository = ImagenRepository()
) {
    suspend fun obtenerActividades(ciudadId: Int? = null): Result<List<Actividad>> = llamarApi {
        RetrofitInstance.api.getActividades(ciudadId).map { it.toDomain() }
    }

    suspend fun obtenerActividadPorId(id: String): Result<Actividad> = llamarApi {
        RetrofitInstance.api.getActividadDetalle(id).toDomain()
    }

    suspend fun obtenerCategorias(): Result<List<String>> = llamarApi {
        RetrofitInstance.api.getCategorias("ACTIVIDAD")
            .map { it.nombre }
            .filter { it.isNotBlank() }
            .distinct()
    }

    // ---------- Admin ----------

    suspend fun listarAdmin(): Result<List<Actividad>> = llamarApi {
        RetrofitInstance.api.listarActividadesAdmin().map { it.toDomain() }
    }

    suspend fun detalleAdmin(id: String): Result<Actividad> = llamarApi {
        RetrofitInstance.api.detalleActividadAdmin(id).toDomain()
    }

    suspend fun guardar(
        id: String?,
        datos: ActividadGuardarDto,
        resolver: ContentResolver
    ): Result<Actividad> = llamarApi {
        val imagenesSubidas = datos.imagenes.map { url ->
            imagenes.subirSiHaceFalta(resolver, url)
        }
        val final = datos.copy(imagenes = imagenesSubidas)
        val respuesta = if (id == null) {
            RetrofitInstance.api.crearActividad(final)
        } else {
            RetrofitInstance.api.editarActividad(id, final)
        }
        respuesta.toDomain()
    }

    suspend fun borrar(id: String): Result<Unit> = llamarApi {
        RetrofitInstance.api.borrarActividad(id)
    }

    suspend fun obtenerCategoriasDto(): Result<List<CategoriaDto>> = llamarApi {
        RetrofitInstance.api.getCategorias("ACTIVIDAD")
    }

    suspend fun crearCategoria(nombre: String): Result<CategoriaDto> = llamarApi {
        RetrofitInstance.api.crearCategoria(CategoriaCrearDto(ambito = "ACTIVIDAD", nombre = nombre))
    }
}
