package com.example.familiasquesuman.ui.screens.proyectos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.repository.ProyectoRepository
import com.example.familiasquesuman.domain.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProyectosUiState(
    val cargando: Boolean = true,
    val proyectos: List<Proyecto> = emptyList(),
    val error: String? = null
)

data class ProyectoDetalleUiState(
    val cargando: Boolean = true,
    val proyecto: Proyecto? = null,
    val error: String? = null
)

class ProyectoViewModel(
    private val repo: ProyectoRepository = ProyectoRepository()
) : ViewModel() {

    private val _lista = MutableStateFlow(ProyectosUiState())
    val lista: StateFlow<ProyectosUiState> = _lista.asStateFlow()

    private val _detalle = MutableStateFlow(ProyectoDetalleUiState())
    val detalle: StateFlow<ProyectoDetalleUiState> = _detalle.asStateFlow()

    private var ciudadActual: Int? = null

    /** Se llama cada vez que se abre la pantalla: si ya hay datos, se quedan visibles mientras recarga. */
    fun cargarLista(ciudadId: Int? = ciudadActual) {
        ciudadActual = ciudadId
        _lista.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            repo.listar(ciudadId)
                .onSuccess { proyectos -> _lista.value = ProyectosUiState(cargando = false, proyectos = proyectos) }
                .onFailure { e -> _lista.update { it.copy(cargando = false, error = e.message) } }
        }
    }

    fun cargarDetalle(id: String) {
        _detalle.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            repo.detalle(id)
                .onSuccess { p -> _detalle.value = ProyectoDetalleUiState(cargando = false, proyecto = p) }
                .onFailure { e -> _detalle.update { it.copy(cargando = false, error = e.message) } }
        }
    }
}