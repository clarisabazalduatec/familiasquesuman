package com.example.familiasquesuman.ui.screens.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistroUiState(
    val nombre: String = "",
    val email: String = "",
    val ciudadId: Int? = null,
    val password: String = "",
    val confirmarPassword: String = "",
    val errorNombre: String? = null,
    val errorEmail: String? = null,
    val errorPassword: String? = null,
    val errorConfirmar: String? = null,
    val errorGeneral: String? = null, // error del backend (correo ya registrado, sin conexión...)
    val cargando: Boolean = false,
    val exito: Boolean = false
)

class RegistroViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    companion object {
        const val MIN_PASSWORD = 8
    }

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState

    // Al escribir en un campo se borra su error.
    fun onNombre(v: String) = _uiState.update { it.copy(nombre = v, errorNombre = null, errorGeneral = null) }
    fun onEmail(v: String) = _uiState.update { it.copy(email = v, errorEmail = null, errorGeneral = null) }
    fun onCiudad(id: Int) = _uiState.update { it.copy(ciudadId = id) }
    fun onPassword(v: String) = _uiState.update { it.copy(password = v, errorPassword = null, errorGeneral = null) }
    fun onConfirmar(v: String) = _uiState.update { it.copy(confirmarPassword = v, errorConfirmar = null) }

    fun crearCuenta() {
        val s = _uiState.value
        if (s.cargando) return

        val errorNombre = if (s.nombre.trim().length < 2) "Escribe tu nombre" else null
        val errorEmail = when {
            s.email.isBlank() -> "Escribe tu correo"
            !Patterns.EMAIL_ADDRESS.matcher(s.email.trim()).matches() -> "Ese correo no parece válido"
            else -> null
        }
        val errorPassword = if (s.password.length < MIN_PASSWORD) "Usa al menos $MIN_PASSWORD caracteres" else null
        val errorConfirmar = if (s.confirmarPassword != s.password) "Las contraseñas no coinciden" else null

        if (listOf(errorNombre, errorEmail, errorPassword, errorConfirmar).any { it != null }) {
            _uiState.update {
                it.copy(
                    errorNombre = errorNombre,
                    errorEmail = errorEmail,
                    errorPassword = errorPassword,
                    errorConfirmar = errorConfirmar
                )
            }
            return
        }

        _uiState.update { it.copy(cargando = true, errorGeneral = null) }
        viewModelScope.launch {
            authRepository.registrar(nombre = s.nombre, email = s.email, password = s.password, ciudadId = s.ciudadId)
                .onSuccess { _uiState.update { it.copy(cargando = false, exito = true) } }
                .onFailure { e ->
                    _uiState.update { it.copy(cargando = false, errorGeneral = e.message ?: "No se pudo crear la cuenta") }
                }
        }
    }
}
