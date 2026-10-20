package com.example.familiasquesuman.data

import android.content.Intent
import android.provider.CalendarContract
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.familiasquesuman.domain.Actividad
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

/**
 * RF-07 Agregar al calendario (prueba instrumentada: corre en el emulador o teléfono).
 * Revisa que el evento que se manda a la app de calendario lleve fecha, hora y lugar correctos.
 */
@RunWith(AndroidJUnit4::class)
class CalendarioTelefonoTest {

    private val inicio = Date(1_793_000_000_000L)               // fecha fija para la prueba
    private val fin = Date(inicio.time + 3 * 60 * 60 * 1000L)   // 3 horas después

    private fun actividad(iniciaEn: Date? = inicio, terminaEn: Date? = fin) = Actividad(
        id = "act-1",
        titulo = "Reforestación Parque Central",
        descripcion = "Plantaremos árboles nativos.",
        categoria = "Medio Ambiente",
        fecha = "",
        horario = "",
        ubicacion = "Monterrey",
        organizador = "Fundación Verde Vivo",
        organizadorVerificado = true,
        acercaDe = "",
        lugaresDisponibles = 10,
        lugaresTotales = 10,
        iniciaEn = iniciaEn,
        terminaEn = terminaEn
    )

    @Test
    fun eventoLlevaTituloFechaHoraYLugar() {
        val evento = CalendarioTelefono.crearEvento(actividad())

        assertNotNull(evento)
        evento!!
        assertEquals(Intent.ACTION_INSERT, evento.action)
        assertEquals(CalendarContract.Events.CONTENT_URI, evento.data)
        assertEquals("Reforestación Parque Central", evento.getStringExtra(CalendarContract.Events.TITLE))
        assertEquals(inicio.time, evento.getLongExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, -1))
        assertEquals(fin.time, evento.getLongExtra(CalendarContract.EXTRA_EVENT_END_TIME, -1))
        assertEquals("Monterrey", evento.getStringExtra(CalendarContract.Events.EVENT_LOCATION))
        assertTrue(evento.getStringExtra(CalendarContract.Events.DESCRIPTION)!!.contains("Fundación Verde Vivo"))
    }

    @Test
    fun sinHoraDeFinUsaLaDeInicio() {
        val evento = CalendarioTelefono.crearEvento(actividad(terminaEn = null))!!

        assertEquals(inicio.time, evento.getLongExtra(CalendarContract.EXTRA_EVENT_END_TIME, -1))
    }

    @Test
    fun actividadSinFechaNoGeneraEvento() {
        assertNull(CalendarioTelefono.crearEvento(actividad(iniciaEn = null)))
    }
}
