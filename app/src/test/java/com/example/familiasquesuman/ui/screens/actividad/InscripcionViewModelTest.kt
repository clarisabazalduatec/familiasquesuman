package com.example.familiasquesuman.ui.screens.actividad

import com.example.familiasquesuman.data.remote.dto.FamiliaDto
import com.example.familiasquesuman.data.remote.dto.InscripcionDto
import com.example.familiasquesuman.data.repository.FamiliaRepository
import com.example.familiasquesuman.data.repository.InscripcionRepository
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
class InscripcionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val familiaRepo: FamiliaRepository = mockk(relaxed = true)
    private val inscripcionRepo: InscripcionRepository = mockk(relaxed = true)
    private lateinit var viewModel: InscripcionViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = InscripcionViewModel(familiaRepo, inscripcionRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cargarFamiliaEInscripcionExitoso() = runTest {
        val familiaMock = FamiliaDto(
            id = "fam1",
            nombre = "Familia Test",
            creadoEn = "2026-10-19T00:00:00Z",
            adultos = emptyList(),
            menores = emptyList(),
            invitacionesPendientes = emptyList()
        )
        val inscripcionMock = InscripcionDto(id = "insc1", actividadId = "act1", estado = "CONFIRMADA", participantes = emptyList())

        coEvery { familiaRepo.miFamilia() } returns Result.success(familiaMock)
        coEvery { inscripcionRepo.miInscripcion("act1") } returns Result.success(inscripcionMock)

        viewModel.cargar("act1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.cargando)
        assertEquals(familiaMock, state.familia)
        assertEquals(inscripcionMock, state.inscripcionActual)
        assertNull(state.error)
    }

    @Test
    fun cancelarInscripcionExitosoLimpiaInscripcionActual() = runTest {
        coEvery { inscripcionRepo.cancelarInscripcion("act1") } returns Result.success(Unit)

        viewModel.cancelar("act1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.guardando)
        assertNull(state.inscripcionActual)
        assertTrue(state.listo)
        coVerify { inscripcionRepo.cancelarInscripcion("act1") }
    }
}
