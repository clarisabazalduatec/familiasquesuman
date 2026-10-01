package com.example.familiasquesuman.domain

enum class TipoDonacion {
    CAMPANA,
    ESPECIE
}

data class Donacion(
    val id: String,
    val titulo: String,
    val descripcionCorta: String,
    val descripcionLarga: String,
    val fundacion: String,
    val imagenUrl: String,
    val tipo: TipoDonacion,

    // Filtros
    val categoria: String? = null,

    // Barra de progreso
    val recaudado: Float = 0f,
    val meta: Float = 0f,
    val textoProgreso: String = "",

    // Contacto y extras
    val telefonoWhatsapp: String? = null,
    val ubicacion: String? = null,
    val condiciones: String? = null,

    // Para Campañas
    val opcionesDisponibles: Int? = null,
    val opcionesDetalle: List<Pair<String, String>>? = null,
    val comoAyudar: String? = null,

    // Para Especie (Centros de Acopio)
    val direccionCompleta: String? = null,
    val categoriasEspecie: List<String>? = null,
    val destinatarios: List<String>? = null,
    val condicionesArticulos: List<String>? = null,
    val metodosEntrega: List<String>? = null,
    val condicionesRecepcion: String? = null
)