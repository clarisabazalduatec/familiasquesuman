package com.example.familiasquesuman.ui.screens.login

import com.example.familiasquesuman.data.repository.AuthRepository
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
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val authRepo: AuthRepository = mockk(relaxed = true)
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(authRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun iniciarSesionConCamposVaciosMuestraError() = runTest {
        viewModel.iniciarSesion("", "", false)
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals("Escribe tu correo y tu contraseña", (state as LoginUiState.Error).mensaje)
    }

    @Test
    fun iniciarSesionExitosoEstableceEstadoExito() = runTest {
        coEvery { authRepo.login("test@mail.com", "12345678", false) } returns Result.success(Unit)

        viewModel.iniciarSesion("test@mail.com", "12345678", false)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(LoginUiState.Exito, viewModel.uiState.value)
    }

    @Test
    fun iniciarSesionFallaManejaError() = runTest {
        coEvery { authRepo.login(any(), any(), any()) } returns Result.failure(Exception("Credenciales inválidas"))

        viewModel.iniciarSesion("test@mail.com", "wrong", false)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals("Credenciales inválidas", (state as LoginUiState.Error).mensaje)
    }
}
