package com.example.familiasquesuman.data

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.example.familiasquesuman.domain.Actividad

/**
 * Agrega una actividad al calendario DEL TELÉFONO (Google Calendar, Samsung, etc.).
 *
 * Abre la app de calendario con el evento ya lleno (título, fecha, hora, lugar y descripción);
 * el usuario solo toca "Guardar". No pide permisos.
 *
 * Ojo: esto NO tiene nada que ver con el calendario dentro de la app, que se llena solo
 * con las inscripciones que regresa el backend.
 */
object CalendarioTelefono {

    /** Abre el calendario del teléfono con el evento listo. false si no hay app de calendario. */
    fun agregarUna(context: Context, actividad: Actividad): Boolean {
        val inicio = actividad.iniciaEn ?: return false
        val fin = actividad.terminaEn ?: inicio
        val intent = Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI).apply {
            putExtra(CalendarContract.Events.TITLE, actividad.titulo)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, inicio.time)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, fin.time)
            putExtra(CalendarContract.Events.EVENT_LOCATION, actividad.ubicacion)
            putExtra(CalendarContract.Events.DESCRIPTION, descripcion(actividad))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            context.startActivity(intent)
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }

    private fun descripcion(actividad: Actividad): String =
        "${actividad.descripcion}\n\nOrganiza: ${actividad.organizador}\nFamilias que Suman"
}