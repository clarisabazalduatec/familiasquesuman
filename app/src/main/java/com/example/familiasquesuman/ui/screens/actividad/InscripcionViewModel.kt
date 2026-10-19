package com.example.familiasquesuman.ui.screens.actividad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.remote.dto.EncuestaInicialRequest
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
import retrofit2.HttpException

data class InscripcionUiState(
    val cargando: Boolean = false,
    val familia: FamiliaDto? = null,
    val inscripcionActual: InscripcionDto? = null,
    val adultosSeleccionados: Set<String> = emptySet(),
    val menoresSeleccionados: Set<String> = emptySet(),
    val guardando: Boolean = false,
    val error: String? = null,
    val listo: Boolean = false,
    val mostrarDialogoListaEspera: Boolean = false,
    val agregadoListaEspera: Boolean = false,
    val mostrarEncuestaInicial: Boolean = false,
    val enviandoEncuesta: Boolean = false
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

    fun confirmar(actividadId: String, listaEspera: Boolean = false) {
        val state = _uiState.value
        val adultos = state.adultosSeleccionados.toList()
        val menores = state.menoresSeleccionados.toList()

        if (adultos.isEmpty() && menores.isEmpty()) {
            _uiState.update { it.copy(error = "Selecciona al menos a una persona para participar.") }
            return
        }

        if (state.guardando) return
        val esNuevaInscripcion = state.inscripcionActual == null
        _uiState.update { it.copy(guardando = true, error = null, mostrarDialogoListaEspera = false) }

        viewModelScope.launch {
            val resultado = if (esNuevaInscripcion) {
                inscripcionRepo.inscribir(actividadId, adultos, menores, listaEspera)
            } else {
                inscripcionRepo.modificarInscripcion(actividadId, adultos, menores, listaEspera)
            }

            resultado.onSuccess { inscripcion ->
                val esEspera = inscripcion.estado?.equals("EN_ESPERA", ignoreCase = true) == true
                if (esEspera) {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            inscripcionActual = inscripcion,
                            agregadoListaEspera = true
                        )
                    }
                } else if (esNuevaInscripcion) {
                    // Si es una inscripción nueva y exitosa, activamos el diálogo de la encuesta inicial
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            inscripcionActual = inscripcion,
                            mostrarEncuestaInicial = true
                        )
                    }
                } else {
                    // Modo edición
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            inscripcionActual = inscripcion,
                            listo = true
                        )
                    }
                }
            }.onFailure { e ->
                val httpEx = e as? HttpException
                val code = httpEx?.code() ?: 0
                val rawError = httpEx?.response()?.errorBody()?.string() ?: e.message.orEmpty()
                val parsedDetail = extraerMensajeError(e, rawError).lowercase()

                val isYaInscrito = (parsedDetail.contains("ya") || rawError.contains("ya", ignoreCase = true)) &&
                        (parsedDetail.contains("inscrit") || parsedDetail.contains("registr") || parsedDetail.contains("exist"))

                val isCupoAgotado = (!isYaInscrito && !listaEspera) && (
                        code == 409 || code == 400 || code == 422 ||
                                parsedDetail.contains("cupo") ||
                                parsedDetail.contains("llen") ||
                                parsedDetail.contains("lugares") ||
                                parsedDetail.contains("agotado") ||
                                parsedDetail.contains("espacios") ||
                                rawError.contains("cupo", ignoreCase = true) ||
                                rawError.contains("llena", ignoreCase = true) ||
                                rawError.contains("lleno", ignoreCase = true) ||
                                rawError.contains("lugares", ignoreCase = true) ||
                                rawError.contains("agotado", ignoreCase = true)
                        )

                if (isCupoAgotado) {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            mostrarDialogoListaEspera = true
                        )
                    }
                } else {
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
                                error = extraerMensajeError(e, rawError)
                            )
                        }
                    }
                }
            }
        }
    }

    fun enviarEncuestaInicial(actividadId: String, request: EncuestaInicialRequest) {
        if (_uiState.value.enviandoEncuesta) return
        _uiState.update { it.copy(enviandoEncuesta = true, error = null) }

        viewModelScope.launch {
            inscripcionRepo.enviarEncuestaInicial(actividadId, request)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            enviandoEncuesta = false,
                            mostrarEncuestaInicial = false,
                            listo = true
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            enviandoEncuesta = false,
                            mostrarEncuestaInicial = false,
                            listo = true
                        )
                    }
                }
        }
    }

    fun omitirEncuestaInicial() {
        _uiState.update {
            it.copy(
                mostrarEncuestaInicial = false,
                listo = true
            )
        }
    }

    fun aceptarListaEspera(actividadId: String) {
        confirmar(actividadId, listaEspera = true)
    }

    fun rechazarListaEspera() {
        _uiState.update { it.copy(mostrarDialogoListaEspera = false) }
    }

    fun consumirAgregadoListaEspera() {
        _uiState.update { it.copy(agregadoListaEspera = false) }
    }

    private fun extraerMensajeError(e: Throwable, rawError: String): String {
        return try {
            if (rawError.contains("\"detail\":")) {
                val detail = rawError.substringAfter("\"detail\":").substringBefore("}").trim().removeSurrounding("\"")
                if (detail.isNotBlank()) return detail
            }
            e.message ?: "Ocurrió un error al guardar la inscripción"
        } catch (_: Exception) {
            e.message ?: "Ocurrió un error al guardar la inscripción"
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