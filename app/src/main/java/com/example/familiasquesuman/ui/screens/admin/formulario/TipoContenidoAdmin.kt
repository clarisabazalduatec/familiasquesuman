package com.example.familiasquesuman.ui.screens.admin.formulario

import com.example.familiasquesuman.ui.navigation.Rutas

enum class TipoContenidoAdmin(val titulo: String, val claveRuta: String) {
    ACTIVIDAD("Actividad", "actividad"),
    PROYECTO("Proyecto", "proyecto"),
    DONACION("Donación", "donacion"),
    DIRECTORIO("Centro", "directorio");

    /** Ruta para abrir el formulario en modo CREAR con este tipo ya seleccionado. */
    fun rutaCrear(): String = "${Rutas.AdminCrearContenido.ruta}?tipo=$claveRuta"

    companion object {
        // Las rutas mandan el tipo como texto ("actividad", "proyecto", "donacion", "directorio")
        fun desdeRuta(clave: String): TipoContenidoAdmin =
            entries.firstOrNull { it.claveRuta == clave.lowercase() } ?: DONACION
    }
}
