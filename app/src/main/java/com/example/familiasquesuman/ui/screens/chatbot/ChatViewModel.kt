package com.example.familiasquesuman.ui.screens.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.remote.dto.ChatTurnoDto
import com.example.familiasquesuman.data.repository.ChatRepository
import com.example.familiasquesuman.domain.MensajeChat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository = ChatRepository()
) : ViewModel() {

    private val _mensajes = MutableStateFlow(
        listOf(
            MensajeChat(
                contenido = "Asistente de Familias que Suman. Consulta actividades, proyectos, donaciones y centros del directorio, o resuelve dudas sobre la app.",
                esDelUsuario = false
            )
        )
    )
    val mensajes: StateFlow<List<MensajeChat>> = _mensajes

    private val _estaEnviando = MutableStateFlow(false)
    val estaEnviando: StateFlow<Boolean> = _estaEnviando

    /**
     * Lo que Sumi "recuerda": solo preguntas con su respuesta exitosa de esta pantalla
     * (sin el saludo ni los errores de conexión). Al salir del chat se pierde: no se guarda en ningún lado.
     */
    private val historial = mutableListOf<ChatTurnoDto>()

    fun enviarMensaje(texto: String) {
        if (_estaEnviando.value) return
        _mensajes.value = _mensajes.value + MensajeChat(contenido = texto, esDelUsuario = true)
        _estaEnviando.value = true

        val contexto = historial.takeLast(MAX_HISTORIAL)
        viewModelScope.launch {
            chatRepository.enviarMensaje(texto, contexto)
                .onSuccess { respuesta ->
                    _mensajes.value = _mensajes.value + respuesta
                    historial += ChatTurnoDto(rol = "usuario", contenido = texto)
                    historial += ChatTurnoDto(rol = "asistente", contenido = respuesta.contenido)
                }
                .onFailure {
                    _mensajes.value = _mensajes.value + MensajeChat(
                        contenido = "No pude conectarme. Intenta de nuevo.",
                        esDelUsuario = false
                    )
                }
            _estaEnviando.value = false
        }
    }

    private companion object {
        const val MAX_HISTORIAL = 10 // igual que en el backend
    }
}