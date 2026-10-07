package com.example.familiasquesuman.domain

enum class EstadoProyecto { ACTIVO, ANTERIOR }

enum class TipoApoyo { TIEMPO_TALENTO, APORTACION_ECONOMICA, MATERIAL }

data class OpcionApoyo(
    val tipo: TipoApoyo,
    val titulo: String,
    val descripcion: String
)

data class Proyecto(
    val id: String,
    val nombre: String,
    val logoUrl: String? = null,
    val estado: EstadoProyecto,
    val ubicacion: String,
    val participantes: String, // "25 Mujeres", "200 adultos mayores" — varía el texto, por eso String
    val descripcionCorta: String,
    val acercaDe: String,
    val descripcionLarga: String,
    val opcionesApoyo: List<OpcionApoyo>,
    val telefonoWhatsapp: String? = null,
    val telefonoLlamada: String? = null,
    val imagenesUrl: List<String> = emptyList(),
    val activo: Boolean = true
)