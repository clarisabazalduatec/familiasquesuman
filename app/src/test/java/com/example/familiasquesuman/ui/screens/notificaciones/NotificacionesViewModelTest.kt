package com.example.familiasquesuman.ui.screens.notificaciones

import com.example.familiasquesuman.data.remote.dto.NotificacionDto
import com.example.familiasquesuman.data.repository.NotificacionesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotificacionesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repo: NotificacionesRepository = mockk(relaxed = true)
    private lateinit var viewModel: NotificacionesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = NotificacionesViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargarNotificacionesExitosoActualizaState() = runTest {
        val listaMock = listOf(
            NotificacionDto(
                id = 1L,
                tipo = "AVISO",
                titulo = "Aviso 1",
                cuerpo = "Mensaje 1",
                destinoTipo = null,
                destinoId = null,
                leida = false,
                creadoEn = "2026-10-19T00:00:00Z"
            )
        )
        coEvery { repo.listar() } returns Result.success(listaMock)

        viewModel.cargar()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.cargando)
        assertNull(state.error)
        assertEquals(1, state.notificaciones.size)
        assertEquals("Aviso 1", state.notificaciones[0].titulo)
        assertTrue(state.hayNoLeidas)
    }

    @Test
    fun cargarNotificacionesFallaActualizaError() = runTest {
        coEvery { repo.listar() } returns Result.failure(Exception("Error de conexión"))

        viewModel.cargar()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.cargando)
        assertEquals("No se pudo conectar con el servidor.", state.error)
    }

    @Test
    fun abrirNotificacionMarcaComoLeida() = runTest {
        val noti = NotificacionDto(
            id = 2L,
            tipo = "RECORDATORIO",
            titulo = "Aviso 2",
            cuerpo = "Msg",
            destinoTipo = "ACTIVIDAD",
            destinoId = "act1",
            leida = false,
            creadoEn = "2026-10-19T00:00:00Z"
        )
        
        coEvery { repo.listar() } returns Result.success(listOf(noti))
        viewModel.cargar()
        testDispatcher.scheduler.advanceUntilIdle()

        val ruta = viewModel.abrir(noti)
        testDispatcher.scheduler.advanceUntilIdle()

        val notiActualizada = viewModel.uiState.value.notificaciones.find { it.id == 2L }
        assertNotNull(notiActualizada)
        assertTrue(notiActualizada!!.leida)
        assertNotNull(ruta)
        coVerify { repo.marcarLeida(2L) }
    }
}
