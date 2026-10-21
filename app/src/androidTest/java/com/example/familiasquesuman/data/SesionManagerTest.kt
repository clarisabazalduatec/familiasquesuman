package com.example.familiasquesuman.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.familiasquesuman.data.remote.dto.CiudadDto
import com.example.familiasquesuman.data.remote.dto.UsuarioDto
import com.example.familiasquesuman.ui.model.TipoSesion
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * RNF-08 Persistencia de sesión (prueba instrumentada: usa el almacenamiento cifrado real del teléfono).
 * "Cerrar y volver a abrir la app" = SesionManager.simularCierreDeApp() + init().
 * Ojo: borra la sesión guardada de la app en el emulador donde corre.
 */
@RunWith(AndroidJUnit4::class)
class SesionManagerTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val usuario = UsuarioDto(
        id = "u-1",
        nombre = "Mariana López",
        email = "mariana@ejemplo.mx",
        ciudad = CiudadDto(id = 1, nombre = "Monterrey"),
        rol = "USUARIO",
        correoVerificado = true
    )

    private fun reabrirApp() {
        SesionManager.simularCierreDeApp()
        SesionManager.init(context)
    }

    @Before
    fun setUp() {
        SesionManager.init(context)
        SesionManager.cerrarSesion()
    }

    @After
    fun tearDown() = SesionManager.cerrarSesion()

    @Test
    fun conRecordarmeLaSesionSigueAlReabrirLaApp() {
        SesionManager.guardarSesion("token-de-prueba", usuario, recordar = true)

        reabrirApp()

        assertEquals("token-de-prueba", SesionManager.token)
        assertEquals("Mariana López", SesionManager.usuario?.nombre)
        assertEquals("Monterrey", SesionManager.usuario?.ciudad?.nombre)
        assertEquals(TipoSesion.USUARIO, SesionManager.tipoSesion)
    }

    @Test
    fun sinRecordarmeLaSesionNoSeGuarda() {
        SesionManager.guardarSesion("token-de-prueba", usuario, recordar = false)

        reabrirApp()

        assertNull(SesionManager.token)
        assertEquals(TipoSesion.INVITADO, SesionManager.tipoSesion)
    }

    @Test
    fun alCerrarSesionNoQuedaNadaGuardado() {
        SesionManager.guardarSesion("token-de-prueba", usuario, recordar = true)
        SesionManager.cerrarSesion()

        reabrirApp()

        assertNull(SesionManager.token)
        assertNull(SesionManager.usuario)
    }
}
