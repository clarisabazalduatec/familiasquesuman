package com.example.familiasquesuman.ui.screens.donar

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.DonacionGuardarDto
import com.example.familiasquesuman.data.repository.DonacionRepository
import com.example.familiasquesuman.data.repository.DonacionRepositoryImpl
import com.example.familiasquesuman.data.repository.ImagenRepository
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.TipoDonacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DonacionViewModel(
    private val repository: DonacionRepository = DonacionRepositoryImpl(RetrofitInstance.api),
    private val imagenRepository: ImagenRepository = ImagenRepository()
) : ViewModel() {

    private val _donaciones = MutableStateFlow<List<Donacion>>(emptyList())
    val donaciones: StateFlow<List<Donacion>> = _donaciones.asStateFlow()

    private val _donacionSeleccionada = MutableStateFlow<Donacion?>(null)
    val donacionSeleccionada: StateFlow<Donacion?> = _donacionSeleccionada.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _tabSeleccionada = MutableStateFlow(0)
    val tabSeleccionada: StateFlow<Int> = _tabSeleccionada.asStateFlow()

    private val _categoriaSeleccionada = MutableStateFlow<String?>("Todos")
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada.asStateFlow()

    var esModoAdmin = false

    private var ciudadActual: Int? = null

    fun cargarDonaciones(
        modalidad: String? = null,
        incluirDesactivadas: Boolean = esModoAdmin,
        ciudadId: Int? = ciudadActual
    ) {
        ciudadActual = ciudadId
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _donaciones.value = repository.obtenerDonaciones(modalidad, incluirDesactivadas, ciudadId)
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error al obtener las donaciones"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cargarDetalleDonacion(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _donacionSeleccionada.value = null
            try {
                val detalle = repository.obtenerDonacionPorId(id)
                _donacionSeleccionada.value = detalle
            } catch (e: Exception) {
                android.util.Log.e("DonacionVM", "Error al cargar donación ID: $id", e)
                _error.value = e.localizedMessage ?: "Error al cargar la donación"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cambiarTab(index: Int) {
        _tabSeleccionada.value = index
        cargarDonaciones()
    }

    fun seleccionarCategoria(categoria: String) {
        _categoriaSeleccionada.value = categoria
    }

    fun guardarDonacion(
        id: String? = null,
        contentResolver: ContentResolver,
        datos: DonacionGuardarDto,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val imagenesSubidas = datos.imagenes.map { url ->
                    imagenRepository.subirSiHaceFalta(contentResolver, url)
                }
                val datosConUrlsReales = datos.copy(imagenes = imagenesSubidas)

                if (id.isNullOrEmpty()) {
                    repository.crearDonacion(datosConUrlsReales)
                } else {
                    repository.actualizarDonacion(id, datosConUrlsReales)
                }

                cargarDonaciones()
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error al guardar la donación")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun eliminarDonacion(
        id: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.eliminarDonacion(id)
                cargarDonaciones()
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Error al eliminar la donación")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sumarAporte(
        donacionLista: Donacion,
        cantidadSumar: Double,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val donacionCompleta = repository.obtenerDonacionPorId(donacionLista.id)
                val nuevoRecaudado = donacionCompleta.recaudado + cantidadSumar.toFloat()

                val imagenesBase = if (donacionCompleta.imagenesUrl.isNotEmpty()) {
                    donacionCompleta.imagenesUrl
                } else if (donacionCompleta.imagenUrl.isNotEmpty()) {
                    listOf(donacionCompleta.imagenUrl)
                } else {
                    emptyList()
                }

                val dto = DonacionGuardarDto(
                    titulo = donacionCompleta.titulo,
                    descripcionCorta = donacionCompleta.descripcionCorta,
                    modalidad = when (donacionCompleta.tipo) {
                        TipoDonacion.TIEMPO -> "TIEMPO"
                        TipoDonacion.ESPECIE -> "ESPECIE"
                        else -> "CAMPANA"
                    },
                    meta = donacionCompleta.meta.toDouble(),
                    recaudado = nuevoRecaudado.toDouble(),
                    ubicacion = donacionCompleta.ubicacionUrl,
                    ciudadId = donacionCompleta.ciudadId ?: 1,
                    descripcionLarga = donacionCompleta.descripcionLarga,
                    whatsapp = donacionCompleta.telefonoWhatsapp,
                    activa = donacionCompleta.activa,
                    organizacionNombre = donacionCompleta.fundacion.ifBlank { null },
                    imagenes = imagenesBase,
                    categoria = donacionCompleta.categoria
                )

                repository.actualizarDonacion(donacionCompleta.id, dto)

                // Re-cargar la lista preservando el modo actual (Admin o Público)
                cargarDonaciones(incluirDesactivadas = esModoAdmin)
                onSuccess()
            } catch (e: Exception) {
                val errorMsg = e.localizedMessage ?: "Error al actualizar lo recaudado"
                _error.value = errorMsg
                onError(errorMsg)
            } finally {
                _isLoading.value = false
            }
        }
    }
}