package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.familiasquesuman.ui.model.AdultoState
import com.example.familiasquesuman.ui.model.MenorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PerfilViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    val listaAdultos = mutableStateListOf<AdultoState>()
    val listaMenores = mutableStateListOf<MenorState>()

    fun abrirEdicion() {
        _uiState.update { it.copy(enModoEdicion = true) }
    }

    fun cancelarEdicion() {
        _uiState.update { it.copy(enModoEdicion = false) }
    }

    // --- ADULTOS ---
    fun agregarAdulto(adulto: AdultoState = AdultoState()) {
        listaAdultos.add(adulto)
    }

    fun eliminarAdulto(adulto: AdultoState) {
        listaAdultos.remove(adulto)
    }

    fun actualizarAdulto(adultoActualizado: AdultoState) {
        val index = listaAdultos.indexOfFirst { it.id == adultoActualizado.id }
        if (index != -1) listaAdultos[index] = adultoActualizado
    }

    // --- MENORES ---
    fun agregarMenor(menor: MenorState = MenorState()) {
        listaMenores.add(menor)
    }

    fun eliminarMenor(menor: MenorState) {
        listaMenores.remove(menor)
    }

    fun actualizarMenor(menorActualizado: MenorState) {
        val index = listaMenores.indexOfFirst { it.id == menorActualizado.id }
        if (index != -1) listaMenores[index] = menorActualizado
    }

    // --- GUARDAR ---
    fun guardarPerfil() {
        _uiState.update {
            it.copy(
                enModoEdicion = false,
                esPerfilRegistrado = true
            )
        }
    }
}