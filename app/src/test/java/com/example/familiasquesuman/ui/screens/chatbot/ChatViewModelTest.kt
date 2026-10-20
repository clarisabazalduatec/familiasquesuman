package com.example.familiasquesuman.ui.screens.chatbot

import com.example.familiasquesuman.data.repository.ChatRepository
import com.example.familiasquesuman.domain.MensajeChat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val chatRepository: ChatRepository = mockk()
    private lateinit var viewModel: ChatViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ChatViewModel(chatRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun estadoInicialContieneSaludo() = runTest {
        val mensajes = viewModel.mensajes.value
        assertEquals(1, mensajes.size)
        assertFalse(mensajes[0].esDelUsuario)
        assertTrue(mensajes[0].contenido.contains("Asistente de Familias que Suman"))
        assertFalse(viewModel.estaEnviando.value)
    }

    @Test
    fun enviarMensajeExitosoActualizaConversacion() = runTest {
        val respuestaEsperada = MensajeChat(contenido = "Aquí tienes información sobre las actividades.", esDelUsuario = false)
        coEvery { chatRepository.enviarMensaje(any(), any()) } returns Result.success(respuestaEsperada)

        viewModel.enviarMensaje("¿Qué actividades hay?")
        
        // Antes de que termine la corrutina, estaEnviando debería ser true
        assertTrue(viewModel.estaEnviando.value)

        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.estaEnviando.value)
        val mensajes = viewModel.mensajes.value
        assertEquals(3, mensajes.size) // Saludo + Pregunta usuario + Respuesta IA
        assertEquals("¿Qué actividades hay?", mensajes[1].contenido)
        assertTrue(mensajes[1].esDelUsuario)
        assertEquals("Aquí tienes información sobre las actividades.", mensajes[2].contenido)
        assertFalse(mensajes[2].esDelUsuario)
    }

    @Test
    fun enviarMensajeFallaManejaErrorCorrectamente() = runTest {
        coEvery { chatRepository.enviarMensaje(any(), any()) } returns Result.failure(Exception("Error de red"))

        viewModel.enviarMensaje("Hola")
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.estaEnviando.value)
        val mensajes = viewModel.mensajes.value
        assertEquals(3, mensajes.size)
        assertEquals("No pude conectarme. Intenta de nuevo.", mensajes[2].contenido)
        assertFalse(mensajes[2].esDelUsuario)
    }
}
