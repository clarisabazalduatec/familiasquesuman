package com.example.familiasquesuman.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.familiasquesuman.data.remote.dto.CiudadDto
import com.example.familiasquesuman.data.remote.dto.UsuarioDto
import com.example.familiasquesuman.ui.model.TipoSesion

/**
 * Única fuente de verdad de la sesión.
 * - token y usuario son estado de Compose: las pantallas se redibujan solas al iniciar/cerrar sesión.
 * - Con "Recordarme" se guarda cifrado con EncryptedSharedPreferences (AES-256 GCM); si no, vive solo en memoria.
 * - El rol viene del backend: sin token = INVITADO; con token = ADMIN o USUARIO.
 */
object SesionManager {

    private const val PREFS = "familias_sesion_secure"
    private const val K_FOTO = "usuario_foto"
    private const val K_TOKEN = "token"
    private const val K_ID = "usuario_id"
    private const val K_NOMBRE = "usuario_nombre"
    private const val K_EMAIL = "usuario_email"
    private const val K_CIUDAD_ID = "usuario_ciudad_id"
    private const val K_CIUDAD = "usuario_ciudad"
    private const val K_ROL = "usuario_rol"
    private const val K_VERIFICADO = "usuario_correo_verificado"

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

    /** false solo si el backend dijo que falta verificar el correo (inscribirse, publicar y comentar lo piden). */
    val correoVerificado: Boolean
        get() = usuario?.correoVerificado != false

    /** Llamar una vez en MainActivity.onCreate, antes de setContent. */
    fun init(context: Context) {
        if (prefs != null) return
        val appContext = context.applicationContext
        val p = try {
            val masterKey = MasterKey.Builder(appContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                appContext,
                PREFS,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (_: Exception) {
            // Fallback en caso de error de Keystore en emulador/dispositivo
            appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        }
        prefs = p

        val tokenGuardado = p.getString(K_TOKEN, null)
        val idGuardado = p.getString(K_ID, null)
        if (tokenGuardado != null && idGuardado != null) {
            token = tokenGuardado
            val ciudadId = p.getInt(K_CIUDAD_ID, -1)
            val ciudadNombre = p.getString(K_CIUDAD, "") ?: ""
            val ciudadDto = if (ciudadId >= 0) CiudadDto(id = ciudadId, nombre = ciudadNombre) else null
            usuario = UsuarioDto(
                id = idGuardado,
                nombre = p.getString(K_NOMBRE, "") ?: "",
                email = p.getString(K_EMAIL, "") ?: "",
                ciudad = ciudadDto,
                rol = p.getString(K_ROL, "USUARIO"),
                fotoUrl = p.getString(K_FOTO, null),
                correoVerificado = p.getBoolean(K_VERIFICADO, true)
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

    /** Actualiza los datos del usuario sin tocar el token. */
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
            .putInt(K_CIUDAD_ID, u.ciudad?.id ?: -1)
            .putString(K_CIUDAD, u.ciudad?.nombre)
            .putString(K_ROL, u.rol)
            .putString(K_FOTO, u.fotoUrl)
            .putBoolean(K_VERIFICADO, u.correoVerificado != false)
}
