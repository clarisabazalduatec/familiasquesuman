package com.example.familiasquesuman.ui.screens.directorio

import com.example.familiasquesuman.data.repository.CentroRepository
import com.example.familiasquesuman.domain.CentroVisiteo
import com.example.familiasquesuman.domain.TipoCentro
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
class DirectorioViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repo: CentroRepository = mockk(relaxed = true)
    private lateinit var viewModel: DirectorioViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DirectorioViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargarListaExitosoActualizaState() = runTest {
        val listaMock = listOf(
            CentroVisiteo(
                id = "c1",
                tipo = TipoCentro.CASA_HOGAR,
                nombre = "Centro Comunitario Monterrey",
                activo = true,
                descripcionCorta = "Desc",
                informacionGeneral = "Info",
                necesidades = emptyList(),
                direccion = "Av. Principal 123",
                ciudadId = 1
            )
        )
        coEvery { repo.listar(1) } returns Result.success(listaMock)

        viewModel.cargarLista(1)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.lista.value
        assertFalse(state.cargando)
        assertNull(state.error)
        assertEquals(1, state.centros.size)
        assertEquals("Centro Comunitario Monterrey", state.centros[0].nombre)
    }

    @Test
    fun cargarListaFallaActualizaError() = runTest {
        coEvery { repo.listar(any()) } returns Result.failure(Exception("Error de directorio"))

        viewModel.cargarLista(null)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.lista.value
        assertFalse(state.cargando)
        assertEquals("Error de directorio", state.error)
    }
}
