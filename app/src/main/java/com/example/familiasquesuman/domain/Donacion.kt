package com.example.familiasquesuman.domain

// Nos sirve para saber en qué pestaña mostrar la tarjeta
enum class TipoDonacion {
    CAMPANA,
    ARTICULO
}

data class Donacion(
    val id: String,
    val titulo: String,
    val descripcionCorta: String,
    val descripcionLarga: String, // Para cuando se seleccione la opción de "Ver más"
    val fundacion: String,
    val imagenUrl: String,
    val tipo: TipoDonacion,

    // Filtros para la sección de "Tengo algo para donar"
    val categoria: String? = null,

    // Barra de progreso
    val recaudado: Float,
    val meta: Float,
    val textoProgreso: String,

    // --- Contacto y extras ---
    val telefonoWhatsapp: String? = null,
    val ubicacion: String? = null,
    val destinatarios: String? = null,
    val condiciones: String? = null
)