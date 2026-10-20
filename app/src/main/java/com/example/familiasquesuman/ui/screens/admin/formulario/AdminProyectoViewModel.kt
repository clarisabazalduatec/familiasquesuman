package com.example.familiasquesuman.ui.screens.admin.formulario

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.remote.dto.OpcionApoyoGuardarDto
import com.example.familiasquesuman.data.remote.dto.ProyectoGuardarDto
import com.example.familiasquesuman.data.repository.ProyectoRepository
import com.example.familiasquesuman.domain.Proyecto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminProyectoUiState(
    val cargando: Boolean = false,      // cargando el proyecto a editar
    val proyecto: Proyecto? = null,     // el proyecto a editar (null al crear)
    val errorCarga: String? = null,
    val guardando: Boolean = false,     // guardando o borrando
    val error: String? = null,          // error al guardar/borrar
    val guardado: Proyecto? = null,     // != null => se guardó bien (mostrar el diálogo de éxito)
    val borrado: Boolean = false        // true => se borró bien (regresar)
)

/** Crear, editar y borrar proyectos desde el panel de admin (contra el backend). */
class AdminProyectoViewModel(
    private val repo: ProyectoRepository = ProyectoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminProyectoUiState())
    val uiState: StateFlow<AdminProyectoUiState> = _uiState.asStateFlow()

    private var cargadoId: String? = null

    /** Carga el proyecto a editar (una sola vez por id). */
    fun cargar(id: String) {
        if (cargadoId == id && _uiState.value.proyecto != null) return
        cargadoId = id
        _uiState.update { it.copy(cargando = true, errorCarga = null) }
        viewModelScope.launch {
            repo.detalle(id)
                .onSuccess { p -> _uiState.update { it.copy(cargando = false, proyecto = p) } }
                .onFailure { e -> _uiState.update { it.copy(cargando = false, errorCarga = e.message) } }
        }
    }

    /** id == null crea; con id reemplaza. imagenes = la lista del SeleccionadorImagenes (la 1a es el logo). */
    fun guardar(
        id: String?,
        comun: ComunForm,
        form: ProyectoForm,
        imagenes: List<String>,
        resolver: ContentResolver
    ) {
        if (_uiState.value.guardando) return
        val datos = ProyectoGuardarDto(
            titulo = comun.titulo.trim(),
            descripcionCorta = comun.descripcionCorta.trim(),
            beneficiarios = form.participantes.trim(),
            estado = form.estado.name,
            ubicacion = comun.ubicacion.trim().ifBlank { null },
            ciudadId = comun.ciudadId,
            acercaDe = form.acercaDe.trim().ifBlank { null },
            descripcionLarga = form.descripcionLarga.trim().ifBlank { null },
            comoAyudar = form.comoAyudar.trim().ifBlank { null },
            instagramUrl = form.instagram.trim().ifBlank { null },
            whatsapp = form.whatsapp.trim().ifBlank { null },
            telefono = form.telefono.trim().ifBlank { null },
            logoUrl = imagenes.firstOrNull(),
            opcionesApoyo = form.opciones
                .filter { it.titulo.isNotBlank() }
                .map { OpcionApoyoGuardarDto(titulo = it.titulo.trim(), descripcion = it.descripcion.trim()) }
        )
        _uiState.update { it.copy(guardando = true, error = null) }
        viewModelScope.launch {
            repo.guardar(id, datos, resolver)
                .onSuccess { p -> _uiState.update { it.copy(guardando = false, guardado = p) } }
                .onFailure { e -> _uiState.update { it.copy(guardando = false, error = e.message) } }
        }
    }

    fun borrar(id: String, alTerminar: () -> Unit = {}) {
        if (_uiState.value.guardando) return
        _uiState.update { it.copy(guardando = true, error = null) }
        viewModelScope.launch {
            repo.borrar(id)
                .onSuccess {
                    _uiState.update { it.copy(guardando = false, borrado = true) }
                    alTerminar()
                }
                .onFailure { e -> _uiState.update { it.copy(guardando = false, error = e.message) } }
        }
    }

    fun errorMostrado() = _uiState.update { it.copy(error = null) }
}