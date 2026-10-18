package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.data.remote.FamiliasApiService
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.DonacionDto
import com.example.familiasquesuman.data.remote.dto.DonacionGuardarDto
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.TipoDonacion

interface DonacionRepository {
    // 1. Agrega el parámetro aquí con valor por defecto false
    suspend fun obtenerDonaciones(modalidad: String? = null, incluirDesactivadas: Boolean = true): List<Donacion>
    suspend fun obtenerDonacionPorId(id: String): Donacion
    suspend fun crearDonacion(datos: DonacionGuardarDto): Donacion
    suspend fun actualizarDonacion(id: String, datos: DonacionGuardarDto): Donacion
    suspend fun eliminarDonacion(id: String)
}

class DonacionRepositoryImpl(
    private val apiService: FamiliasApiService
) : DonacionRepository {

    // 2. Recibe el parámetro aquí y pásalo al apiService
    override suspend fun obtenerDonaciones(modalidad: String?, incluirDesactivadas: Boolean): List<Donacion> {
        val dtos = apiService.listarDonaciones(modalidad = modalidad, incluirDesactivadas = incluirDesactivadas)
        return dtos.map { it.toDomain() }
    }

    override suspend fun obtenerDonacionPorId(id: String): Donacion {
        val dto = apiService.detalleDonacion(id)
        return dto.toDomain()
    }

    override suspend fun crearDonacion(datos: DonacionGuardarDto): Donacion {
        val dto = apiService.crearDonacion(datos)
        return dto.toDomain()
    }

    override suspend fun actualizarDonacion(id: String, datos: DonacionGuardarDto): Donacion {
        val dto = apiService.editarDonacion(id, datos)
        return dto.toDomain()
    }

    override suspend fun eliminarDonacion(id: String) {
        apiService.borrarDonacion(id)
    }
}

fun DonacionDto.toDomain(): Donacion {
    val tipoDonacion = when (this.modalidad.uppercase()) {
        "TIEMPO" -> TipoDonacion.TIEMPO
        "ESPECIE" -> TipoDonacion.ESPECIE
        else -> TipoDonacion.CAMPANA
    }

    val recaudadoVal = this.recaudado?.toFloat() ?: 0f
    val metaVal = this.meta?.toFloat() ?: 0f

    val textoProgresoCalc = when (tipoDonacion) {
        TipoDonacion.CAMPANA -> "$${recaudadoVal.toInt()} recaudados"
        TipoDonacion.ESPECIE -> "${recaudadoVal.toInt()} entregadas"
        TipoDonacion.TIEMPO -> "Voluntariado"
    }

    val listaImagenesRaw = this.imagenes ?: emptyList()
    val listaImagenesUrl = listaImagenesRaw.mapNotNull { RetrofitInstance.urlCompleta(it) }
    val primeraImagen = listaImagenesUrl.firstOrNull() ?: ""
    val listaOpciones = this.opciones ?: emptyList()
    val mapaEtiquetas = this.etiquetas ?: emptyMap()

    // Lógica simplificada para evitar errores de Kotlin con los nulos (?)
    val nombreOrg = this.organizacionNombre
    val nombreRel = this.organizacion?.nombre
    val fundacionFinal = when {
        !nombreOrg.isNullOrBlank() -> nombreOrg
        !nombreRel.isNullOrBlank() -> nombreRel
        else -> ""
    }

    return Donacion(
        id = this.id,
        titulo = this.titulo,
        descripcionCorta = this.descripcionCorta,
        descripcionLarga = this.descripcionLarga.orEmpty(),

        // Asignamos el resultado seguro
        fundacion = fundacionFinal,

        imagenUrl = primeraImagen,
        tipo = tipoDonacion,
        categoria = this.categoria?.nombre.orEmpty(),
        recaudado = recaudadoVal,
        meta = metaVal,
        textoProgreso = textoProgresoCalc,
        telefonoWhatsapp = this.whatsapp.orEmpty(),
        ubicacion = this.ubicacion.orEmpty(),
        condiciones = this.condicionesRecepcion.orEmpty(),
        opcionesDisponibles = this.opcionesDisponibles ?: listaOpciones.size.takeIf { it > 0 },
        opcionesDetalle = listaOpciones.map { Pair(it.titulo, "$${it.monto.toInt()}") },
        direccionCompleta = this.ubicacion.orEmpty(),
        categoriasEspecie = mapaEtiquetas["categorias_especie"],
        destinatarios = mapaEtiquetas["destinatarios"],
        condicionesArticulos = mapaEtiquetas["condiciones_articulos"],
        metodosEntrega = mapaEtiquetas["metodos_entrega"],
        condicionesRecepcion = this.condicionesRecepcion.orEmpty(),
        fechaLimite = this.fechaLimite.orEmpty(),
        horario = this.horario.orEmpty(),
        imagenesUrl = listaImagenesUrl,
        activa = this.activa ?: true
    )
}