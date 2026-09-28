package com.example.familiasquesuman.ui.navigation

sealed class Rutas(val ruta: String) {
    object Inicio : Rutas("inicio")
    object Actividades : Rutas("actividades")
    object Donar : Rutas("donar_screen")
    object Proyectos : Rutas("proyectos")
    object Directorio : Rutas("directorio")
    object Chatbot : Rutas("chatbot")
    object Login : Rutas("login")
    object Comunidad : Rutas("comunidad")
    object NuevaPublicacion : Rutas("nueva_publicacion")
    object Notificaciones : Rutas("notificaciones")
    object ResultadoPublicacion : Rutas("resultado_publicacion/{fueExitoso}") {
        fun crearRuta(fueExitoso: Boolean) = "resultado_publicacion/$fueExitoso"
    }
}