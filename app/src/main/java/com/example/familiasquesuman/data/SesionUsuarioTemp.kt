package com.example.familiasquesuman.data

import com.example.familiasquesuman.data.remote.dto.UsuarioDto

object SesionUsuario {
    var token: String? = null
        private set
    var usuario: UsuarioDto? = null
        private set

    fun guardarSesion(token: String, usuario: UsuarioDto) {
        this.token = token
        this.usuario = usuario
    }

    fun cerrarSesion() {
        token = null
        usuario = null
    }

    val estaAutenticado: Boolean
        get() = token != null
}