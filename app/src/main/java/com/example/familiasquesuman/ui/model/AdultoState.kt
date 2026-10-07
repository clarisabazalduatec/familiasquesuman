package com.example.familiasquesuman.ui.model

import java.util.UUID

data class AdultoState(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String = "",
    val rol: String = "",
    val email: String = ""
)