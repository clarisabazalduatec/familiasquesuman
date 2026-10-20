package com.example.familiasquesuman.ui.screens.actividad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.repository.ActividadRepository
import com.example.familiasquesuman.data.repository.InscripcionRepository
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.remote.dto.toDomain
import com.example.familiasquesuman.domain.Actividad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

sealed interface ActividadUiState {
    object Cargando : ActividadUiState
    data class Exito(val actividades: List<Actividad>) : ActividadUiState
    data class Error(val mensaje: String) : ActividadUiState
}

/** Una actividad de la familia en el calendario. fecha = día local "AAAA-MM-DD". */
data class ActividadCalendario(
    val actividad: Actividad,
    val fecha: String,
    val enEspera: Boolean
)

sealed interface CalendarioUiState {
    object SinSesion : CalendarioUiState
    object Cargando : CalendarioUiState
    data class Error(val mensaje: String) : CalendarioUiState
    data class Listo(val actividades: List<ActividadCalendario>) : CalendarioUiState
}

sealed interface ActividadDetalleUiState {
    object Cargando : ActividadDetalleUiState
    data class Exito(val actividad: Actividad) : ActividadDetalleUiState
    data class Error(val mensaje: String) : ActividadDetalleUiState
}

class ActividadViewModel(
    private val repository: ActividadRepository = ActividadRepository(),
    private val inscripciones: InscripcionRepository = InscripcionRepository()
) : ViewModel() {

    private val _calendario = MutableStateFlow<CalendarioUiState>(CalendarioUiState.Cargando)
    val calendario: StateFlow<CalendarioUiState> = _calendario.asStateFlow()

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

    private var ciudadActual: Int? = null

    fun cargarActividades(ciudadId: Int? = ciudadActual) {
        ciudadActual = ciudadId
        viewModelScope.launch {
            _uiState.value = ActividadUiState.Cargando
            repository.obtenerActividades(ciudadId)
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

    /**
     * Calendario: las actividades en las que la familia está inscrita o en lista de espera
     * (GET /familias/mia/inscripciones). Sin sesión no hay familia que consultar.
     */
    fun cargarCalendario() {
        if (!SesionManager.estaAutenticado) {
            _calendario.value = CalendarioUiState.SinSesion
            return
        }
        // Si ya había datos se quedan visibles mientras recarga
        if (_calendario.value !is CalendarioUiState.Listo) _calendario.value = CalendarioUiState.Cargando
        viewModelScope.launch {
            inscripciones.misInscripciones()
                .onSuccess { lista ->
                    val dia = SimpleDateFormat("yyyy-MM-dd", Locale.US) // en la zona horaria del celular
                    val actividades = lista.mapNotNull { insc ->
                        val actividad = insc.actividad?.toDomain() ?: return@mapNotNull null
                        val inicio = actividad.iniciaEn ?: return@mapNotNull null
                        ActividadCalendario(actividad, dia.format(inicio), enEspera = insc.estado == "EN_ESPERA")
                    }.sortedBy { it.actividad.iniciaEn }
                    _calendario.value = CalendarioUiState.Listo(actividades)
                }
                .onFailure { e ->
                    _calendario.value = CalendarioUiState.Error(e.message ?: "No se pudo cargar tu calendario")
                }
        }
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