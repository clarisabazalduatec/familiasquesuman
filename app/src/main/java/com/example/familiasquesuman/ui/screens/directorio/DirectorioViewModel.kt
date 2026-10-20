package com.example.familiasquesuman.ui.screens.directorio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.repository.CentroRepository
import com.example.familiasquesuman.domain.CentroVisiteo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DirectorioUiState(
    val cargando: Boolean = true,
    val centros: List<CentroVisiteo> = emptyList(),
    val error: String? = null
)

data class CentroDetalleUiState(
    val cargando: Boolean = true,
    val centro: CentroVisiteo? = null,
    val error: String? = null
)

/** Directorio público: lista y detalle de centros activos. */
class DirectorioViewModel(
    private val repo: CentroRepository = CentroRepository()
) : ViewModel() {

    private val _lista = MutableStateFlow(DirectorioUiState())
    val lista: StateFlow<DirectorioUiState> = _lista.asStateFlow()

    private val _detalle = MutableStateFlow(CentroDetalleUiState())
    val detalle: StateFlow<CentroDetalleUiState> = _detalle.asStateFlow()

    private var ciudadActual: Int? = null

    /** Se llama cada vez que se abre la pantalla: si ya hay datos, se quedan visibles mientras recarga. */
    fun cargarLista(ciudadId: Int? = ciudadActual) {
        ciudadActual = ciudadId
        _lista.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            repo.listar(ciudadId)
                .onSuccess { centros -> _lista.value = DirectorioUiState(cargando = false, centros = centros) }
                .onFailure { e -> _lista.update { it.copy(cargando = false, error = e.message) } }
        }
    }

    fun cargarDetalle(id: String) {
        _detalle.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            repo.detalle(id)
                .onSuccess { c -> _detalle.value = CentroDetalleUiState(cargando = false, centro = c) }
                .onFailure { e -> _detalle.update { it.copy(cargando = false, error = e.message) } }
        }
    }
}