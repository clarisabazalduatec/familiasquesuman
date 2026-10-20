package com.example.familiasquesuman.domain

import java.util.Date

data class Actividad(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val fecha: String,
    val horario: String,
    val ubicacion: String,
    val organizador: String,
    val organizadorVerificado: Boolean,
    val acercaDe: String,
    val lugaresDisponibles: Int,
    val lugaresTotales: Int,
    val imagenes: List<String> = emptyList(),
    val iniciaEn: Date? = null,
    val terminaEn: Date? = null,
    val activa: Boolean = true,
    val categoriaId: Int? = null,
    val ubicacionUrl: String? = null,
    val ciudadId: Int? = null
)
