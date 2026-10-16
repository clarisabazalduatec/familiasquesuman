package com.example.familiasquesuman.ui.screens.notificaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.remote.dto.NotificacionDto
import com.example.familiasquesuman.data.repository.NotificacionesRepository
import com.example.familiasquesuman.notificaciones.DestinosNotificacion
import com.example.familiasquesuman.notificaciones.EstadoNotificaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotificacionesUiState(
    val cargando: Boolean = true,
    val error: String? = null,
    val notificaciones: List<NotificacionDto> = emptyList()
) {
    val hayNoLeidas: Boolean get() = notificaciones.any { !it.leida }
}

class NotificacionesViewModel(
    private val repo: NotificacionesRepository = NotificacionesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificacionesUiState())
    val uiState: StateFlow<NotificacionesUiState> = _uiState.asStateFlow()

    fun cargar() {
        _uiState.update { it.copy(error = null) } // recarga silenciosa: se queda lo que ya se ve
        viewModelScope.launch {
            repo.listar()
                .onSuccess { lista -> _uiState.update { it.copy(cargando = false, notificaciones = lista) } }
                .onFailure { e -> _uiState.update { it.copy(cargando = false, error = e.message) } }
            repo.refrescarContador()
        }
    }

    /** Al tocar una: se marca como leída y regresa la ruta a la que hay que ir (o null). */
    fun abrir(n: NotificacionDto): String? {
        if (!n.leida) {
            // Se marca en pantalla al momento; el backend se avisa en segundo plano
            _uiState.update { s ->
                s.copy(notificaciones = s.notificaciones.map { if (it.id == n.id) it.copy(leida = true) else it })
            }
            EstadoNotificaciones.noLeidas = (EstadoNotificaciones.noLeidas - 1).coerceAtLeast(0)
            viewModelScope.launch { repo.marcarLeida(n.id) }
        }
        return DestinosNotificacion.ruta(n.tipo, n.destinoTipo, n.destinoId)
    }

    fun marcarTodas() {
        _uiState.update { s -> s.copy(notificaciones = s.notificaciones.map { it.copy(leida = true) }) }
        EstadoNotificaciones.noLeidas = 0
        viewModelScope.launch { repo.marcarTodas() }
    }
}