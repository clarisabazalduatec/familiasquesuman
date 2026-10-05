package com.example.familiasquesuman.ui.navigation

sealed class Rutas(val ruta: String) {
    object Onboarding : Rutas ("onborading")
    object Inicio : Rutas("inicio")
    object Actividades : Rutas("actividades")
    object Donar : Rutas("donar_screen")
    object Proyectos : Rutas("proyectos")
    object Directorio : Rutas("directorio")
    
    object DirectorioDetalle : Rutas("directorio_detalle/{centroId}") {
        fun crearRuta(centroId: String) = "directorio_detalle/$centroId"
    }

    object Chatbot : Rutas("chatbot")
    object Login : Rutas("login")
    object Comunidad : Rutas("comunidad")

    object SobreNosotros : Rutas("sobre_nosotros")

    object ContactoAyuda : Rutas("contacto_ayuda")
    object NuevaPublicacion : Rutas("nueva_publicacion")
    object Admin : Rutas("admin")
    object AdminModeracionComunidad : Rutas("admin_moderacion_comunidad")
    object AdminModeracionActividades : Rutas("admin_moderacion_actividades")
    object AdminModeracionProyectos : Rutas("admin_moderacion_proyectos")
    object AdminActualizacionDonaciones : Rutas("admin_actualizacion_donaciones")
    object AdminCrearContenido : Rutas("admin_crear_contenido")
    
    object AdminEditarContenido : Rutas("admin_editar_contenido/{tipo}/{id}") {
        fun crearRuta(tipo: String, id: String) = "admin_editar_contenido/$tipo/$id"
    }

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
    object ProyectoDetalle : Rutas("proyecto_detalle/{proyectoId}") {
        fun crearRuta(proyectoId: String) = "proyecto_detalle/$proyectoId"
    }

    object MisPublicaciones :
        Rutas("mis_publicaciones")

    object NuevaPublicacionOficial :
        Rutas("nueva_publicacion_oficial")

    object DetallePublicacion :
        Rutas("detalle_publicacion/{id}") {

        fun crearRuta(id: String): String {
            return "detalle_publicacion/$id"
        }
    }
}
