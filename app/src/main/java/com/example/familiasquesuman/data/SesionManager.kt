package com.example.familiasquesuman.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.familiasquesuman.data.remote.dto.UsuarioDto
import com.example.familiasquesuman.ui.model.TipoSesion

/**
 * Única fuente de verdad de la sesión (reemplaza a SesionUsuario y SesionDemoState).
 * - token y usuario son estado de Compose: las pantallas se redibujan solas al iniciar/cerrar sesión.
 * - Con "Recordarme" se guarda en SharedPreferences; si no, vive solo en memoria.
 * - El rol viene del backend: sin token = INVITADO; con token = ADMIN o USUARIO.
 */
object SesionManager {

    private const val PREFS = "familias_sesion"
    private const val K_FOTO = "usuario_foto"
    private const val K_TOKEN = "token"
    private const val K_ID = "usuario_id"
    private const val K_NOMBRE = "usuario_nombre"
    private const val K_EMAIL = "usuario_email"
    private const val K_UBICACION = "usuario_ubicacion"
    private const val K_ROL = "usuario_rol"

    private var prefs: SharedPreferences? = null

    var token by mutableStateOf<String?>(null)
        private set

    var usuario by mutableStateOf<UsuarioDto?>(null)
        private set

    val estaAutenticado: Boolean
        get() = token != null

    val tipoSesion: TipoSesion
        get() = when {
            token == null -> TipoSesion.INVITADO
            usuario?.rol == "ADMIN" -> TipoSesion.ADMIN
            else -> TipoSesion.USUARIO
        }

    val esAdmin: Boolean
        get() = tipoSesion == TipoSesion.ADMIN

    /** Llamar una vez en MainActivity.onCreate, antes de setContent. */
    fun init(context: Context) {
        if (prefs != null) return
        val p = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs = p

        val tokenGuardado = p.getString(K_TOKEN, null)
        val idGuardado = p.getString(K_ID, null)
        if (tokenGuardado != null && idGuardado != null) {
            token = tokenGuardado
            usuario = UsuarioDto(
                id = idGuardado,
                nombre = p.getString(K_NOMBRE, "") ?: "",
                email = p.getString(K_EMAIL, "") ?: "",
                ubicacion = p.getString(K_UBICACION, null),
                rol = p.getString(K_ROL, "USUARIO"),
                fotoUrl = p.getString(K_FOTO, null)
            )
        }
    }

    fun guardarSesion(token: String, usuario: UsuarioDto, recordar: Boolean) {
        this.token = token
        this.usuario = usuario
        if (recordar) {
            prefs?.edit()
                ?.putString(K_TOKEN, token)
                ?.putUsuario(usuario)
                ?.apply()
        } else {
            limpiarGuardado()
        }
    }

    /** Actualiza los datos del usuario (por ejemplo, tras GET /usuarios/me) sin tocar el token. */
    fun actualizarUsuario(usuario: UsuarioDto) {
        this.usuario = usuario
        val p = prefs ?: return
        if (p.contains(K_TOKEN)) {
            p.edit().putUsuario(usuario).apply()
        }
    }

    fun cerrarSesion() {
        token = null
        usuario = null
        limpiarGuardado()
    }

    private fun limpiarGuardado() {
        prefs?.edit()?.clear()?.apply()
    }

    private fun SharedPreferences.Editor.putUsuario(u: UsuarioDto): SharedPreferences.Editor =
        putString(K_ID, u.id)
            .putString(K_NOMBRE, u.nombre)
            .putString(K_EMAIL, u.email)
            .putString(K_UBICACION, u.ubicacion)
            .putString(K_ROL, u.rol)
            .putString(K_FOTO, u.fotoUrl)
}