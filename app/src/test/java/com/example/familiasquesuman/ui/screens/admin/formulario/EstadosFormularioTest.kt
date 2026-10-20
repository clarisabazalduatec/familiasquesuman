package com.example.familiasquesuman.ui.screens.admin.formulario

import com.example.familiasquesuman.domain.Actividad
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class EstadosFormularioTest {

    @Test
    fun testActividadAFormularioConFechaStringSinYear() {
        val actividad = Actividad(
            id = "1",
            titulo = "Reforestación",
            descripcion = "Descripción",
            categoria = "Medio Ambiente",
            fecha = "Sáb, 24 de Oct",
            horario = "09:00 AM - 13:00 PM",
            ubicacion = "Parque",
            organizador = "Org",
            organizadorVerificado = true,
            acercaDe = "Acerca de",
            lugaresDisponibles = 10,
            lugaresTotales = 20,
            iniciaEn = null,
            terminaEn = null
        )

        val (_, form) = actividad.aFormulario()

        assertNotNull(form.fechaMillis)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = form.fechaMillis!!
        }

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) // 0-indexed, October = 9
        val day = cal.get(Calendar.DAY_OF_MONTH)

        assertTrue("El año debe ser mayor a 1970", year > 1970)
        assertEquals(9, month) // Octubre
        assertEquals(24, day)
        assertEquals(9 to 0, form.horaInicio)
        assertEquals(13 to 0, form.horaFin)
    }

    @Test
    fun testActividadAFormularioConIniciaEnValido() {
        val calSet = Calendar.getInstance(TimeZone.getTimeZone("America/Monterrey")).apply {
            set(2026, Calendar.OCTOBER, 24, 10, 30, 0)
        }
        val actividad = Actividad(
            id = "2",
            titulo = "Lectura",
            descripcion = "Desc",
            categoria = "Educación",
            fecha = "24 de Oct",
            horario = "10:30 AM - 12:00 PM",
            ubicacion = "Biblioteca",
            organizador = "Org",
            organizadorVerificado = false,
            acercaDe = "Acerca de",
            lugaresDisponibles = 5,
            lugaresTotales = 15,
            iniciaEn = calSet.time
        )

        val (_, form) = actividad.aFormulario()

        assertNotNull(form.fechaMillis)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = form.fechaMillis!!
        }

        assertEquals(2026, cal.get(Calendar.YEAR))
        assertEquals(Calendar.OCTOBER, cal.get(Calendar.MONTH))
        assertEquals(24, cal.get(Calendar.DAY_OF_MONTH))
        assertEquals(10 to 30, form.horaInicio)
    }
}
