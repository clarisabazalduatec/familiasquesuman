package com.example.familiasquesuman.ui.screens.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistroViewModelTest {

    @Test
    fun validacionDatosInvalidosMuestraErroresEnFormulario() {
        val viewModel = RegistroViewModel()
        viewModel.onNombre("A") // muy corto
        viewModel.onEmail("correo-invalido")
        viewModel.onPassword("123")
        viewModel.onConfirmar("456")

        viewModel.crearCuenta()

        val state = viewModel.uiState.value
        assertNotNull(state.errorNombre)
        assertNotNull(state.errorEmail)
        assertNotNull(state.errorPassword)
        assertNotNull(state.errorConfirmar)
        assertFalse(state.exito)
    }
}
