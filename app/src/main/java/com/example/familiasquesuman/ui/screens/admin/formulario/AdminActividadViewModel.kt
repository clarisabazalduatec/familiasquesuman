package com.example.familiasquesuman.ui.screens.admin.formulario

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.remote.dto.ActividadGuardarDto
import com.example.familiasquesuman.data.remote.dto.CategoriaDto
import com.example.familiasquesuman.data.repository.ActividadRepository
import com.example.familiasquesuman.domain.Actividad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminActividadUiState(
    val cargandoLista: Boolean = false,
    val actividades: List<Actividad> = emptyList(),
    val errorLista: String? = null,

    val cargando: Boolean = false,         // cargando la actividad a editar
    val actividad: Actividad? = null,      // la actividad a editar (null al crear)
    val errorCarga: String? = null,
    val guardando: Boolean = false,        // guardando o borrando
    val error: String? = null,             // error al guardar/borrar
    val guardado: Actividad? = null,       // != null => se guardó bien
    val borrado: Boolean = false,          // true => se borró bien
    val categorias: List<CategoriaDto> = emptyList()
)

class AdminActividadViewModel(
    private val repo: ActividadRepository = ActividadRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminActividadUiState())
    val uiState: StateFlow<AdminActividadUiState> = _uiState.asStateFlow()

    private var cargadoId: String? = null

    init {
        cargarCategorias()
    }

    fun cargarLista() {
        _uiState.update { it.copy(cargandoLista = true, errorLista = null) }
        viewModelScope.launch {
            repo.listarAdmin()
                .onSuccess { a -> _uiState.update { it.copy(cargandoLista = false, actividades = a) } }
                .onFailure { e -> _uiState.update { it.copy(cargandoLista = false, errorLista = e.message) } }
        }
    }

    fun cargar(id: String) {
        if (cargadoId == id && _uiState.value.actividad != null) return
        cargadoId = id
        _uiState.update { it.copy(cargando = true, errorCarga = null) }
        viewModelScope.launch {
            repo.detalleAdmin(id)
                .onSuccess { a -> _uiState.update { it.copy(cargando = false, actividad = a) } }
                .onFailure { e -> _uiState.update { it.copy(cargando = false, errorCarga = e.message) } }
        }
    }

    fun guardar(
        id: String?,
        comun: ComunForm,
        form: ActividadForm,
        imagenes: List<String>,
        resolver: ContentResolver
    ) {
        if (_uiState.value.guardando) return
        val fecha = form.fechaMillis ?: return
        val horaIni = form.horaInicio ?: return
        val horaFin = form.horaFin ?: return

        val iniciaEnIso = formarIsoConZona(fecha, horaIni)
        val terminaEnIso = formarIsoConZona(fecha, horaFin)

        val datos = ActividadGuardarDto(
            titulo = comun.titulo.trim(),
            descripcionCorta = comun.descripcionCorta.trim(),
            ubicacion = comun.ubicacion.trim().ifBlank { null },
            ciudadId = comun.ciudadId,
            categoriaId = form.categoriaId ?: return,
            organizacionNombre = form.organizador.trim().ifBlank { null },
            iniciaEn = iniciaEnIso,
            terminaEn = terminaEnIso,
            cupoTotal = form.lugaresTotales.toIntOrNull() ?: 30,
            acercaDe = form.acercaDe.trim().ifBlank { null },
            activa = comun.activa,
            imagenes = imagenes
        )

        _uiState.update { it.copy(guardando = true, error = null) }
        viewModelScope.launch {
            repo.guardar(id, datos, resolver)
                .onSuccess { a -> _uiState.update { it.copy(guardando = false, guardado = a) } }
                .onFailure { e -> _uiState.update { it.copy(guardando = false, error = e.message) } }
        }
    }

    fun borrar(id: String, alTerminar: () -> Unit = {}) {
        if (_uiState.value.guardando) return
        _uiState.update { it.copy(guardando = true, error = null) }
        viewModelScope.launch {
            repo.borrar(id)
                .onSuccess {
                    _uiState.update { s -> s.copy(guardando = false, borrado = true, actividades = s.actividades.filterNot { it.id == id }) }
                    alTerminar()
                }
                .onFailure { e -> _uiState.update { it.copy(guardando = false, error = e.message) } }
        }
    }

    fun cargarCategorias() {
        viewModelScope.launch {
            repo.obtenerCategoriasDto()
                .onSuccess { cats -> _uiState.update { it.copy(categorias = cats) } }
        }
    }

    fun crearCategoria(nombre: String, alCrear: (CategoriaDto) -> Unit = {}) {
        viewModelScope.launch {
            repo.crearCategoria(nombre)
                .onSuccess { cat ->
                    _uiState.update { s -> s.copy(categorias = s.categorias + cat) }
                    alCrear(cat)
                }
                .onFailure { e -> _uiState.update { it.copy(error = e.message) } }
        }
    }

    fun errorMostrado() = _uiState.update { it.copy(error = null) }
}
