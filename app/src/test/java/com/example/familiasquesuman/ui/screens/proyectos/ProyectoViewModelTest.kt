package com.example.familiasquesuman.ui.screens.proyectos

import com.example.familiasquesuman.data.repository.ProyectoRepository
import com.example.familiasquesuman.domain.EstadoProyecto
import com.example.familiasquesuman.domain.Proyecto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProyectoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repo: ProyectoRepository = mockk(relaxed = true)
    private lateinit var viewModel: ProyectoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProyectoViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargarListaProyectosExitosoActualizaState() = runTest {
        val listaMock = listOf(
            Proyecto(
                id = "p1",
                nombre = "Proyecto Educativo",
                estado = EstadoProyecto.ACTIVO,
                ubicacion = "Monterrey",
                participantes = "50 niños",
                descripcionCorta = "Desc",
                acercaDe = "Acerca",
                descripcionLarga = "Larga",
                opcionesApoyo = emptyList(),
                ciudadId = 1
            )
        )
        coEvery { repo.listar(1) } returns Result.success(listaMock)

        viewModel.cargarLista(1)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.lista.value
        assertFalse(state.cargando)
        assertNull(state.error)
        assertEquals(1, state.proyectos.size)
        assertEquals("Proyecto Educativo", state.proyectos[0].nombre)
        assertEquals(1, state.proyectos[0].ciudadId)
    }

    @Test
    fun cargarListaProyectosFallaActualizaError() = runTest {
        coEvery { repo.listar(any()) } returns Result.failure(Exception("Error de proyectos"))

        viewModel.cargarLista(null)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.lista.value
        assertFalse(state.cargando)
        assertEquals("Error de proyectos", state.error)
    }
}
