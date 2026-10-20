package com.example.familiasquesuman.domain

enum class TipoCentro(val etiqueta: String) {
    ASILO("Asilos"),
    CASA_HOGAR("Casas hogar"),
    COMEDOR("Comedores")
}

data class CentroVisiteo(
    val id: String,
    val tipo: TipoCentro,
    val nombre: String,                 // título
    val activo: Boolean,
    val descripcionCorta: String,
    val informacionGeneral: String,     // descripción larga
    val necesidades: List<String>,
    val direccion: String,              // texto que se muestra bajo el título
    val mapaUrl: String? = null,        // link de "Cómo llegar" (Google Maps, etc.)
    val logoUrl: String? = null,
    val comoAyudar: String? = null,
    val recomendaciones: String? = null,
    val telefono: String? = null,
    val whatsapp: String? = null,
    val verificado: Boolean = true,
    val ciudadId: Int? = null,
    val ciudadNombre: String? = null
)