package com.example.familiasquesuman.ui.model

data class HijoState(
    val id: Long = System.currentTimeMillis() + (0..1000).random(),
    var nombre: String = "",
    var fechaNacimiento: String = ""
)