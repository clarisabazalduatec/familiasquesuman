package com.example.familiasquesuman.ui.screens.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.repository.FamiliaRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Perfil = datos de mi familia real (backend). Cada acción se guarda al momento
 * y después se recarga la familia para que la pantalla quede igual que el servidor.
 * También lo usa NotificacionesScreen para las invitaciones recibidas.
 */
class PerfilViewModel(
    private val repo: FamiliaRepository = FamiliaRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    private var cargadoPara: String? = null
    // ---------- carga ----------

    fun cargar() {
        val usuarioActual = SesionManager.usuario?.id
        if (!SesionManager.estaAutenticado || usuarioActual == null) {
            cargadoPara = null
            _uiState.value = PerfilUiState(cargando = false)
            return
        }
        if (usuarioActual != cargadoPara) {
            // Cambió la cuenta: no mostrar nada de la anterior, ni un instante.
            cargadoPara = usuarioActual
            _uiState.value = PerfilUiState(cargando = true)
        } else {
            _uiState.update { it.copy(errorCarga = null) } // recarga silenciosa: se queda lo que ya se ve
        }
        viewModelScope.launch {
            val perfil = async { repo.miPerfil() }
            val familia = async { repo.miFamilia() }
            val invitaciones = async { repo.misInvitaciones() }

            val p = perfil.await()
            val f = familia.await()
            val i = invitaciones.await()

            val error = listOf(p, f, i).firstNotNullOfOrNull { it.exceptionOrNull()?.message }
            _uiState.update {
                it.copy(
                    cargando = false,
                    errorCarga = error,
                    miId = p.getOrNull()?.id ?: it.miId,
                    miParentesco = p.getOrNull()?.parentesco ?: it.miParentesco,
                    familia = if (f.isSuccess) f.getOrNull() else it.familia,
                    invitacionesRecibidas = i.getOrNull() ?: it.invitacionesRecibidas
                )
            }
        }
    }

    /** Recarga sin mostrar el indicador de carga de pantalla completa. */
    private suspend fun recargarEnSilencio() {
        val f = repo.miFamilia()
        val i = repo.misInvitaciones()
        _uiState.update {
            it.copy(
                familia = if (f.isSuccess) f.getOrNull() else it.familia,
                invitacionesRecibidas = i.getOrNull() ?: it.invitacionesRecibidas
            )
        }
    }

    // ---------- diálogos ----------

    fun abrirDialogo(dialogo: DialogoPerfil) =
        _uiState.update { it.copy(dialogo = dialogo, errorDialogo = null) }

    fun cerrarDialogo() =
        _uiState.update { it.copy(dialogo = null, errorDialogo = null) }

    fun mensajeMostrado() = _uiState.update { it.copy(mensaje = null) }

    /**
     * Acción que viene de un diálogo: si sale bien, cierra el diálogo, recarga y avisa;
     * si falla, deja el diálogo abierto con el error.
     */
    private fun accionDeDialogo(avisoExito: String?, accion: suspend () -> Result<*>) {
        if (_uiState.value.guardando) return
        _uiState.update { it.copy(guardando = true, errorDialogo = null) }
        viewModelScope.launch {
            accion()
                .onSuccess {
                    recargarEnSilencio()
                    _uiState.update { it.copy(guardando = false, dialogo = null, mensaje = avisoExito) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(guardando = false, errorDialogo = e.message) }
                }
        }
    }

    /** Igual, pero para botones sueltos (sin diálogo): el error sale como aviso. */
    private fun accionDirecta(avisoExito: String?, accion: suspend () -> Result<*>) {
        if (_uiState.value.guardando) return
        _uiState.update { it.copy(guardando = true) }
        viewModelScope.launch {
            accion()
                .onSuccess {
                    recargarEnSilencio()
                    _uiState.update { it.copy(guardando = false, mensaje = avisoExito) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(guardando = false, mensaje = e.message) }
                }
        }
    }

    // ---------- familia ----------

    fun crearFamilia(nombre: String, parentesco: String?) =
        accionDeDialogo("¡Tu familia quedó registrada!") {
            repo.crearFamilia(nombre, parentesco).onSuccess {
                _uiState.update { s -> s.copy(miParentesco = parentesco ?: s.miParentesco) }
            }
        }

    fun salirDeFamilia() =
        accionDeDialogo("Saliste de la familia") {
            repo.salirDeFamilia().onSuccess {
                _uiState.update { s -> s.copy(familia = null, miParentesco = null) }
            }
        }

    fun cambiarMiParentesco(parentesco: String) =
        accionDeDialogo(null) {
            repo.cambiarMiParentesco(parentesco).onSuccess { perfil ->
                _uiState.update { s -> s.copy(miParentesco = perfil.parentesco) }
            }
        }

    // ---------- menores ----------

    /** id null = agregar; fecha en formato "AAAA-MM-DD". */
    fun guardarMenor(id: String?, nombre: String, fechaNacimiento: String) {
        if (nombre.isBlank()) {
            _uiState.update { it.copy(errorDialogo = "Escribe el nombre") }
            return
        }
        accionDeDialogo(if (id == null) "Menor agregado" else "Cambios guardados") {
            repo.guardarMenor(id, nombre, fechaNacimiento)
        }
    }

    fun borrarMenor(id: String) =
        accionDeDialogo("Menor eliminado") { repo.borrarMenor(id) }

    // ---------- invitaciones enviadas ----------

    fun invitarAdulto(email: String) {
        if (email.isBlank()) {
            _uiState.update { it.copy(errorDialogo = "Escribe un correo") }
            return
        }
        accionDeDialogo("Invitación enviada") { repo.invitarAdulto(email) }
    }

    fun cancelarInvitacion(id: String) =
        accionDirecta("Invitación cancelada") { repo.cancelarInvitacion(id) }

    // ---------- invitaciones recibidas ----------

    fun aceptarInvitacion(id: String) =
        accionDirecta("¡Te uniste a la familia!") {
            repo.aceptarInvitacion(id).onSuccess {
                repo.miPerfil().onSuccess { p -> _uiState.update { s -> s.copy(miParentesco = p.parentesco) } }
            }
        }

    fun rechazarInvitacion(id: String) =
        accionDirecta("Invitación rechazada") { repo.rechazarInvitacion(id) }
}