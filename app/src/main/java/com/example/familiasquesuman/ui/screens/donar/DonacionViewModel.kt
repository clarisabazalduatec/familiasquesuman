package com.example.familiasquesuman.ui.screens.donar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.repository.DonacionRepository
import com.example.familiasquesuman.data.repository.DonacionRepositoryFake
import com.example.familiasquesuman.domain.Donacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DonacionViewModel(
    private val repository: DonacionRepository = DonacionRepositoryFake()
) : ViewModel() {

    // Se guarda toda la lista de donaciones
    private val _donaciones = MutableStateFlow<List<Donacion>>(emptyList())
    val donaciones: StateFlow<List<Donacion>> = _donaciones.asStateFlow()

    // 0 = Campañas Urgentes, 1 = Tengo algo para donar
    private val _tabSeleccionada = MutableStateFlow(0)
    val tabSeleccionada: StateFlow<Int> = _tabSeleccionada.asStateFlow()

    init {
        cargarDonaciones()
    }

    private fun cargarDonaciones() {
        viewModelScope.launch {
            // Pide los datos sin importarle de dónde vienen
            _donaciones.value = repository.obtenerDonaciones()
        }
    }

    fun cambiarTab(index: Int) {
        _tabSeleccionada.value = index
    }

    // Busca una donación específica por su ID
    fun obtenerDonacionPorId(id: String): Donacion? {
        return _donaciones.value.find { it.id == id }
    }

    private val _categoriaSeleccionada = MutableStateFlow<String?>("Todos")
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada.asStateFlow()

    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }
}