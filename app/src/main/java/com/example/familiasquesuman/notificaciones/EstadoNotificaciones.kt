package com.example.familiasquesuman.notificaciones

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.familiasquesuman.ui.navigation.Rutas

/** Estado compartido por toda la app (como SesionManager): las pantallas se redibujan solas. */
object EstadoNotificaciones {
    /** Numerito de la campana. */
    var noLeidas by mutableIntStateOf(0)

    /** Ruta a abrir porque se tocó una notificación del celular (NavGraph la consume). */
    var rutaPendiente by mutableStateOf<String?>(null)
}

/** Traduce el destino que manda el backend a una ruta de la app. null = no hay a dónde ir. */
object DestinosNotificacion {
    fun ruta(tipo: String, destinoTipo: String?, destinoId: String?): String? = when (destinoTipo) {
        "PUBLICACION" -> destinoId?.let { Rutas.DetallePublicacion.crearRuta(it) }
        "AVISO" -> Rutas.Comunidad.ruta // los avisos oficiales se ven en Comunidad (no tienen detalle propio)
        "MIS_PUBLICACIONES" -> Rutas.MisPublicaciones.ruta
        "INVITACIONES" -> Rutas.Notificaciones.ruta // la tarjeta de invitaciones está en la campana
        "ACTIVIDAD" -> destinoId?.let {
            // Al admin le sirve más la lista de inscritos; a la familia, el detalle de la actividad
            if (tipo == "ACTIVIDAD_LLENA") Rutas.AsistenciaActividades.crearRuta(it)
            else Rutas.ActividadDetalle.crearRuta(it)
        }
        "MODERACION" -> Rutas.AdminModeracionComunidad.ruta
        else -> null
    }
}