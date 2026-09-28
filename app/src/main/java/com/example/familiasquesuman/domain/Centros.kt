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
    val direccion: String
)
