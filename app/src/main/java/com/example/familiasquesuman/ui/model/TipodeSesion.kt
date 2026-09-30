package com.example.familiasquesuman.ui.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Estado temporal de sesión para el prototipo de frontend.
 *
 * INVITADO = no ha iniciado sesión
 * USUARIO  = inició sesión como usuario normal
 * ADMIN    = inició sesión como administrador
 */
enum class TipoSesion {
    INVITADO,
    USUARIO,
    ADMIN
}

/**
 * Estado compartido temporal.
 *
 * Más adelante esto se sustituirá por el estado real
 * del AuthViewModel / repositorio de autenticación.
 */
object SesionDemoState {

    // Empezamos como ADMIN para poder probar todas las vistas.
    var tipoSesion by mutableStateOf(TipoSesion.ADMIN)
}