package com.example.familiasquesuman.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object Ocioso : LoginUiState
    object Cargando : LoginUiState
    object Exito : LoginUiState
    data class Error(val mensaje: String) : LoginUiState
}

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Ocioso)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun iniciarSesion(email: String, password: String) {
        _uiState.value = LoginUiState.Cargando
        viewModelScope.launch {
            authRepository.login(email, password)
                .onSuccess { _uiState.value = LoginUiState.Exito }
                .onFailure { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "No se pudo iniciar sesión")
                }
        }
    }
}