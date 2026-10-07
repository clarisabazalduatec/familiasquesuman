package com.example.familiasquesuman.ui.model

import java.util.UUID

data class MenorState(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String = "",
    val fechaNacimiento: String = ""
)