package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.familiasquesuman.ui.model.HijoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PerfilUiState(
    val enModoEdicion: Boolean = false,
    val esPerfilRegistrado: Boolean = false,
    val mama: String = "",
    val papa: String = "",
    val whatsapp: String = "",
    val email: String = "",
    val ciudad: String = ""
)

class PerfilViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    // Lista observable para los hijos
    val listaHijos = mutableStateListOf<HijoState>()

    fun abrirEdicion() {
        _uiState.update { it.copy(enModoEdicion = true) }
    }

    fun cancelarEdicion() {
        _uiState.update { it.copy(enModoEdicion = false) }
    }

    fun agregarHijo() {
        listaHijos.add(HijoState())
    }

    fun eliminarHijo(hijo: HijoState) {
        listaHijos.remove(hijo)
    }

    fun guardarPerfil(
        mama: String,
        papa: String,
        whatsapp: String,
        email: String,
        ciudad: String
    ) {
        _uiState.update {
            it.copy(
                enModoEdicion = false,
                esPerfilRegistrado = true,
                mama = mama,
                papa = papa,
                whatsapp = whatsapp,
                email = email,
                ciudad = ciudad
            )
        }
    }
}