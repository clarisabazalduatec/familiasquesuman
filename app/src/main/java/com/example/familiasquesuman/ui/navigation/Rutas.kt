package com.example.familiasquesuman.ui.navigation

sealed class Rutas(val ruta: String) {
    object Onboarding : Rutas ("onborading")
    object Inicio : Rutas("inicio")
    object Actividades : Rutas("actividades")
    object Donar : Rutas("donar_screen")
    object Proyectos : Rutas("proyectos")
    object Directorio : Rutas("directorio")
    object Chatbot : Rutas("chatbot")
    object Login : Rutas("login")
    object Comunidad : Rutas("comunidad")
    object NuevaPublicacion : Rutas("nueva_publicacion")
    object Admin : Rutas("admin")
    object Notificaciones : Rutas("notificaciones")
    object Perfil : Rutas("perfil")
    
    object ActividadDetalle : Rutas("actividad_detalle/{id}") {
        fun crearRuta(id: Int) = "actividad_detalle/$id"
    }
    
    object ActividadParticipar : Rutas("actividad_participar/{id}") {
        fun crearRuta(id: Int) = "actividad_participar/$id"
    }

    object ActividadSeleccionParticipantes : Rutas("actividad_seleccion/{id}") {
        fun crearRuta(id: Int) = "actividad_seleccion/$id"
    }

    object ActividadConfirmacion : Rutas("actividad_confirmacion/{id}") {
        fun crearRuta(id: Int) = "actividad_confirmacion/$id"
    }

    object ResultadoPublicacion : Rutas("resultado_publicacion/{fueExitoso}") {
        fun crearRuta(fueExitoso: Boolean) = "resultado_publicacion/$fueExitoso"
    }
}