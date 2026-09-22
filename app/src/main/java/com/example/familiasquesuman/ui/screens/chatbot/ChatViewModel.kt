package com.example.familiasquesuman.ui.screens.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                contenido = "¡Hola! Soy tu asistente. Estoy aquí para ayudarte a encontrar proyectos, organizar tu voluntariado o resolver dudas.",
                esDelUsuario = false
            )
        )
    )
    val mensajes: StateFlow<List<MensajeChat>> = _mensajes

    private val _estaEnviando = MutableStateFlow(false)
    val estaEnviando: StateFlow<Boolean> = _estaEnviando

    fun enviarMensaje(texto: String) {
        _mensajes.value = _mensajes.value + MensajeChat(contenido = texto, esDelUsuario = true)
        _estaEnviando.value = true

        viewModelScope.launch {
            chatRepository.enviarMensaje(texto)
                .onSuccess { respuesta ->
                    _mensajes.value = _mensajes.value + respuesta
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
}