package com.example.familiasquesuman.domain

enum class TipoCentro(val etiqueta: String) {
    ASILO("Asilos"),
    CASA_HOGAR("Casas hogar"),
    COMEDOR("Comedores")
}

data class CentroVisiteo(
    val id: String,
    val tipo: TipoCentro,
    val nombre: String,
    val descripcionCorta: String,
    val informacionGeneral: String,
    val necesidades: List<String>,
    val direccion: String,
    val logoUrl: String? = null,
    val comoAyudar: String? = "Donación en especie. Visita a los ancianos para convivir y platicar.",
    val recomendaciones: String? = "Hablar con anticipación para ver que actividades se recomiendan y en que horario.",
    val telefono: String? = "8282844311",
    val whatsapp: String? = "528282844311"
)
