package com.example.familiasquesuman.ui.screens.actividad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.repository.ActividadRepository
import com.example.familiasquesuman.domain.Actividad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ActividadUiState {
    object Cargando : ActividadUiState
    data class Exito(val actividades: List<Actividad>) : ActividadUiState
    data class Error(val mensaje: String) : ActividadUiState
}

sealed interface ActividadDetalleUiState {
    object Cargando : ActividadDetalleUiState
    data class Exito(val actividad: Actividad) : ActividadDetalleUiState
    data class Error(val mensaje: String) : ActividadDetalleUiState
}

class ActividadViewModel(
    private val repository: ActividadRepository = ActividadRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ActividadUiState>(ActividadUiState.Cargando)
    val uiState: StateFlow<ActividadUiState> = _uiState.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada.asStateFlow()

    private val _categorias = MutableStateFlow<List<String>>(
        listOf("Medio Ambiente", "Educación", "Apoyo Social", "Otros")
    )
    val categorias: StateFlow<List<String>> = _categorias.asStateFlow()

    private val _detalleUiState = MutableStateFlow<ActividadDetalleUiState>(ActividadDetalleUiState.Cargando)
    val detalleUiState: StateFlow<ActividadDetalleUiState> = _detalleUiState.asStateFlow()

    init {
        cargarActividades()
    }

    fun cargarActividades() {
        viewModelScope.launch {
            _uiState.value = ActividadUiState.Cargando
            repository.obtenerActividades()
                .onSuccess { lista ->
                    _uiState.value = ActividadUiState.Exito(lista)
                }
                .onFailure { e ->
                    _uiState.value = ActividadUiState.Error(
                        e.message ?: "No se pudieron cargar las actividades"
                    )
                }

            repository.obtenerCategorias().onSuccess { cats ->
                if (cats.isNotEmpty()) {
                    _categorias.value = cats
                }
            }
        }
    }

    fun seleccionarCategoria(categoria: String?) {
        _categoriaSeleccionada.value = if (_categoriaSeleccionada.value == categoria) null else categoria
    }

    fun reintentar() {
        cargarActividades()
    }

    fun cargarDetalle(id: String) {
        viewModelScope.launch {
            _detalleUiState.value = ActividadDetalleUiState.Cargando

            val enLista = (_uiState.value as? ActividadUiState.Exito)?.actividades?.find { it.id == id }
            if (enLista != null) {
                _detalleUiState.value = ActividadDetalleUiState.Exito(enLista)
            }

            repository.obtenerActividadPorId(id)
                .onSuccess { actividad ->
                    _detalleUiState.value = ActividadDetalleUiState.Exito(actividad)
                }
                .onFailure { e ->
                    if (enLista == null) {
                        _detalleUiState.value = ActividadDetalleUiState.Error(
                            e.message ?: "Error al obtener detalle de la actividad"
                        )
                    }
                }
        }
    }
}
