package com.example.familiasquesuman.ui.navigation

sealed class Rutas(val ruta: String) {
    object Inicio : Rutas("inicio")
    object Actividades : Rutas("actividades")
    object Donar : Rutas("donar")
    object Proyectos : Rutas("proyectos")
    object Directorio : Rutas("directorio")
    object Chatbot : Rutas("chatbot")
    object  Login : Rutas("login")
}