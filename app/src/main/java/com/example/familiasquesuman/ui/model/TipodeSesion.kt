package com.example.familiasquesuman.ui.model

/**
 * INVITADO = no ha iniciado sesión
 * USUARIO  = inició sesión como usuario normal
 * ADMIN    = inició sesión como administrador
 *
 * El valor actual lo da SesionManager.tipoSesion (sale del rol que manda el backend).
 */
enum class TipoSesion {
    INVITADO,
    USUARIO,
    ADMIN
}