package com.example.familiasquesuman.ui.screens.actividad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.remote.dto.FamiliaDto
import com.example.familiasquesuman.data.remote.dto.InscripcionDto
import com.example.familiasquesuman.data.repository.FamiliaRepository
import com.example.familiasquesuman.data.repository.InscripcionRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class InscripcionUiState(
    val cargando: Boolean = false,
    val familia: FamiliaDto? = null,
    val inscripcionActual: InscripcionDto? = null,
    val adultosSeleccionados: Set<String> = emptySet(),
    val menoresSeleccionados: Set<String> = emptySet(),
    val guardando: Boolean = false,
    val error: String? = null,
    val listo: Boolean = false
)

class InscripcionViewModel(
    private val familiaRepo: FamiliaRepository = FamiliaRepository(),
    private val inscripcionRepo: InscripcionRepository = InscripcionRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(InscripcionUiState())
    val uiState: StateFlow<InscripcionUiState> = _uiState.asStateFlow()

    private var actividadCargadaId: String? = null

    fun cargar(actividadId: String, forzar: Boolean = false) {
        if (!forzar && actividadCargadaId == actividadId && !_uiState.value.cargando && _uiState.value.familia != null) {
            return
        }
        actividadCargadaId = actividadId
        _uiState.update { it.copy(cargando = true, error = null, listo = false) }

        viewModelScope.launch {
            val familiaDeferred = async { familiaRepo.miFamilia() }
            val inscripcionDeferred = async { inscripcionRepo.miInscripcion(actividadId) }

            val resFamilia = familiaDeferred.await()
            val resInscripcion = inscripcionDeferred.await()

            val familia = resFamilia.getOrNull()
            val inscripcion = resInscripcion.getOrNull()

            val adultos: Set<String>
            val menores: Set<String>

            if (inscripcion != null) {
                // Modo edición: preseleccionar a sus participantes
                adultos = inscripcion.participantes.orEmpty().mapNotNull { it.usuarioId }.toSet()
                menores = inscripcion.participantes.orEmpty().mapNotNull { it.menorId }.toSet()
            } else {
                // Preseleccionar solo al usuario actual
                val miId = SesionManager.usuario?.id
                adultos = if (miId != null) setOf(miId) else emptySet()
                menores = emptySet()
            }

            val errorMsg = resFamilia.exceptionOrNull()?.message
                ?: (if (resInscripcion.isFailure) resInscripcion.exceptionOrNull()?.message else null)

            _uiState.update {
                it.copy(
                    cargando = false,
                    familia = familia,
                    inscripcionActual = inscripcion,
                    adultosSeleccionados = adultos,
                    menoresSeleccionados = menores,
                    error = errorMsg
                )
            }
        }
    }

    fun alternarAdulto(id: String) {
        _uiState.update { current ->
            val nuevos = if (id in current.adultosSeleccionados) {
                current.adultosSeleccionados - id
            } else {
                current.adultosSeleccionados + id
            }
            current.copy(adultosSeleccionados = nuevos)
        }
    }

    fun alternarMenor(id: String) {
        _uiState.update { current ->
            val nuevos = if (id in current.menoresSeleccionados) {
                current.menoresSeleccionados - id
            } else {
                current.menoresSeleccionados + id
            }
            current.copy(menoresSeleccionados = nuevos)
        }
    }

    fun confirmar(actividadId: String) {
        val state = _uiState.value
        val adultos = state.adultosSeleccionados.toList()
        val menores = state.menoresSeleccionados.toList()

        if (adultos.isEmpty() && menores.isEmpty()) {
            _uiState.update { it.copy(error = "Selecciona al menos a una persona para participar.") }
            return
        }

        if (state.guardando) return
        _uiState.update { it.copy(guardando = true, error = null) }

        viewModelScope.launch {
            val resultado = if (state.inscripcionActual == null) {
                inscripcionRepo.inscribir(actividadId, adultos, menores)
            } else {
                inscripcionRepo.modificarInscripcion(actividadId, adultos, menores)
            }

            resultado.onSuccess { inscripcion ->
                _uiState.update {
                    it.copy(
                        guardando = false,
                        inscripcionActual = inscripcion,
                        listo = true
                    )
                }
            }.onFailure { e ->
                // Por si el servidor ya la había creado (o dice "ya estás inscrito"):
                val inscExistente = inscripcionRepo.miInscripcion(actividadId).getOrNull()
                if (inscExistente != null) {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            inscripcionActual = inscExistente,
                            listo = true
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            error = e.message ?: "Ocurrió un error al guardar la inscripción"
                        )
                    }
                }
            }
        }
    }

    fun cancelar(actividadId: String) {
        val state = _uiState.value
        if (state.guardando) return
        _uiState.update { it.copy(guardando = true, error = null) }

        viewModelScope.launch {
            inscripcionRepo.cancelarInscripcion(actividadId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            inscripcionActual = null,
                            adultosSeleccionados = emptySet(),
                            menoresSeleccionados = emptySet(),
                            listo = true
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            error = e.message ?: "No se pudo cancelar la inscripción"
                        )
                    }
                }
        }
    }

    fun limpiarError() {
        _uiState.update { it.copy(error = null) }
    }

    fun consumirListo() {
        _uiState.update { it.copy(listo = false) }
    }
}
