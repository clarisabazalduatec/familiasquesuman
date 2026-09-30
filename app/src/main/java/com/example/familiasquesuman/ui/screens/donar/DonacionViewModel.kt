package com.example.familiasquesuman.ui.screens.donar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.repository.DonacionRepository
import com.example.familiasquesuman.data.repository.DonacionRepositoryFake
import com.example.familiasquesuman.data.repository.donacionesMockData
import com.example.familiasquesuman.data.repository.registrarAporteDonacion
import com.example.familiasquesuman.domain.Donacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DonacionViewModel(
    private val repository: DonacionRepository = DonacionRepositoryFake()
) : ViewModel() {

    private val _donaciones = MutableStateFlow<List<Donacion>>(donacionesMockData)
    val donaciones: StateFlow<List<Donacion>> = _donaciones.asStateFlow()

    // 0 = Campañas Urgentes, 1 = Tengo algo para donar
    private val _tabSeleccionada = MutableStateFlow(0)
    val tabSeleccionada: StateFlow<Int> = _tabSeleccionada.asStateFlow()

    init {
        cargarDonaciones()
    }

    private fun cargarDonaciones() {
        viewModelScope.launch {
            _donaciones.value = repository.obtenerDonaciones()
        }
    }

    fun cambiarTab(index: Int) {
        _tabSeleccionada.value = index
    }

    fun obtenerDonacionPorId(id: String): Donacion? {
        return donacionesMockData.find { it.id == id }
    }

    fun registrarAporte(id: String, cantidad: Float) {
        registrarAporteDonacion(id, cantidad)
    }

    private val _categoriaSeleccionada = MutableStateFlow<String?>("Todos")
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada.asStateFlow()

    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }
}
