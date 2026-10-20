package com.example.familiasquesuman.ui.screens.admin.formulario

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.remote.dto.CentroGuardarDto
import com.example.familiasquesuman.data.repository.CentroRepository
import com.example.familiasquesuman.domain.CentroVisiteo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminCentroUiState(
    // Pantalla de gestión del directorio
    val cargandoLista: Boolean = true,
    val centros: List<CentroVisiteo> = emptyList(),
    val errorLista: String? = null,

    // Formulario
    val cargando: Boolean = false,         // cargando el centro a editar
    val centro: CentroVisiteo? = null,     // el centro a editar (null al crear)
    val errorCarga: String? = null,
    val guardando: Boolean = false,        // guardando o borrando
    val error: String? = null,             // error al guardar/borrar
    val guardado: CentroVisiteo? = null    // != null => se guardó bien (mostrar el diálogo de éxito)
)

/** Lista, crear, editar y borrar centros del directorio desde el panel de admin. */
class AdminCentroViewModel(
    private val repo: CentroRepository = CentroRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminCentroUiState())
    val uiState: StateFlow<AdminCentroUiState> = _uiState.asStateFlow()

    private var cargadoId: String? = null

    /** Todos los centros (también los desactivados). Se llama cada vez que se abre la pantalla. */
    fun cargarLista() {
        _uiState.update { it.copy(cargandoLista = true, errorLista = null) }
        viewModelScope.launch {
            repo.listarAdmin()
                .onSuccess { c -> _uiState.update { it.copy(cargandoLista = false, centros = c) } }
                .onFailure { e -> _uiState.update { it.copy(cargandoLista = false, errorLista = e.message) } }
        }
    }

    /** Carga el centro a editar (una sola vez por id). */
    fun cargar(id: String) {
        if (cargadoId == id && _uiState.value.centro != null) return
        cargadoId = id
        _uiState.update { it.copy(cargando = true, errorCarga = null) }
        viewModelScope.launch {
            repo.detalleAdmin(id)
                .onSuccess { c -> _uiState.update { it.copy(cargando = false, centro = c) } }
                .onFailure { e -> _uiState.update { it.copy(cargando = false, errorCarga = e.message) } }
        }
    }

    /** id == null crea; con id reemplaza. imagenes = la lista del SeleccionadorImagenes (la 1a es el logo). */
    fun guardar(
        id: String?,
        comun: ComunForm,
        form: CentroForm,
        imagenes: List<String>,
        resolver: ContentResolver
    ) {
        if (_uiState.value.guardando) return
        val datos = CentroGuardarDto(
            tipo = form.tipo.name,
            nombre = comun.titulo.trim(),
            descripcionCorta = comun.descripcionCorta.trim(),
            informacionGeneral = form.informacionGeneral.trim(),
            direccion = form.direccion.trim(),
            ubicacion = comun.ubicacion.trim().ifBlank { null },
            ciudadId = comun.ciudadId,
            logoUrl = imagenes.firstOrNull(),
            comoAyudar = form.comoAyudar.trim().ifBlank { null },
            recomendaciones = form.recomendaciones.trim().ifBlank { null },
            telefono = form.telefono.trim().ifBlank { null },
            whatsapp = form.whatsapp.trim().ifBlank { null },
            verificado = form.verificado,
            activo = comun.activa,
            necesidades = form.necesidades.map { it.trim() }.filter { it.isNotEmpty() }
        )
        _uiState.update { it.copy(guardando = true, error = null) }
        viewModelScope.launch {
            repo.guardar(id, datos, resolver)
                .onSuccess { c -> _uiState.update { it.copy(guardando = false, guardado = c) } }
                .onFailure { e -> _uiState.update { it.copy(guardando = false, error = e.message) } }
        }
    }

    fun borrar(id: String, alTerminar: () -> Unit = {}) {
        if (_uiState.value.guardando) return
        _uiState.update { it.copy(guardando = true, error = null) }
        viewModelScope.launch {
            repo.borrar(id)
                .onSuccess {
                    _uiState.update { s -> s.copy(guardando = false, centros = s.centros.filterNot { it.id == id }) }
                    alTerminar()
                }
                .onFailure { e -> _uiState.update { it.copy(guardando = false, error = e.message) } }
        }
    }

    fun errorMostrado() = _uiState.update { it.copy(error = null) }
}