package com.example.familiasquesuman.ui.screens.admin.formulario

enum class TipoContenidoAdmin(val titulo: String, val claveRuta: String) {
    ACTIVIDAD("Actividad", "actividad"),
    PROYECTO("Proyecto", "proyecto"),
    DONACION("Donación", "donacion");

    companion object {
        fun desdeRuta(clave: String): TipoContenidoAdmin =
            entries.firstOrNull { it.claveRuta == clave.lowercase() } ?: DONACION
    }
}
