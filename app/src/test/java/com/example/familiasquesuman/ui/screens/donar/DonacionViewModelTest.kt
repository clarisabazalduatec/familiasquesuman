package com.example.familiasquesuman.ui.screens.donar

import com.example.familiasquesuman.data.repository.DonacionRepository
import com.example.familiasquesuman.data.repository.ImagenRepository
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.TipoDonacion
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
class DonacionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val donacionRepo: DonacionRepository = mockk(relaxed = true)
    private val imagenRepo: ImagenRepository = mockk(relaxed = true)
    private lateinit var viewModel: DonacionViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DonacionViewModel(donacionRepo, imagenRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargarDonacionesExitosoActualizaState() = runTest {
        val listaMock = listOf(
            Donacion(
                id = "don1",
                titulo = "Campaña Víveres",
                descripcionCorta = "Ayuda alimentaria",
                descripcionLarga = "Descripción larga de prueba",
                fundacion = "Fundación Test",
                imagenUrl = "",
                tipo = TipoDonacion.ESPECIE,
                categoria = "Alimentos",
                recaudado = 50f,
                meta = 100f
            )
        )
        coEvery { donacionRepo.obtenerDonaciones(any(), any(), any()) } returns listaMock

        viewModel.cargarDonaciones()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
        assertEquals(1, viewModel.donaciones.value.size)
        assertEquals("Campaña Víveres", viewModel.donaciones.value[0].titulo)
        assertEquals(50f, viewModel.donaciones.value[0].recaudado)
        assertEquals(100f, viewModel.donaciones.value[0].meta)
    }

    @Test
    fun cargarDonacionesFallaManejaError() = runTest {
        coEvery { donacionRepo.obtenerDonaciones(any(), any(), any()) } throws RuntimeException("Error servidor donaciones")

        viewModel.cargarDonaciones()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertNotNull(viewModel.error.value)
        assertTrue(viewModel.error.value!!.contains("Error servidor donaciones"))
    }
}
