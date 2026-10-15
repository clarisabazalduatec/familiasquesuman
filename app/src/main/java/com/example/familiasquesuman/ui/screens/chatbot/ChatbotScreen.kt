package com.example.familiasquesuman.ui.screens.chatbot
import com.example.familiasquesuman.domain.EnlaceChat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.ui.components.BarraNavegacionInferior
import com.example.familiasquesuman.ui.components.MenuLateral
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.AzulMarinoOscuro
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    navController: NavHostController,
    chatViewModel: ChatViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val mensajes by chatViewModel.mensajes.collectAsState()
    val estaEnviando by chatViewModel.estaEnviando.collectAsState()
    var textoActual by remember { mutableStateOf("") }

    PantallaPrincipalConMenu(
        backButton = true,
        onBackClick = { navController.popBackStack() },
        navbar = false,
        navController = navController,
        pantallaActual = PantallaPrincipal.CHATBOT
    ) { paddingInterno -> Column(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
        ){
                LazyColumn(
                    reverseLayout = true,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    // reverseLayout: el primer item es el de hasta abajo
                    if (estaEnviando) {
                        item(key = "pensando") { BurbujaPensando() }
                    }
                    items(mensajes.reversed()) { mensaje ->
                        BurbujaMensaje(
                            mensaje = mensaje,
                            onEnlaceClick = { enlace -> navController.navigate(rutaDeEnlace(enlace)) },
                        )
                    }
                }

                HorizontalDivider()
                BarraEscribirMensaje(
                    texto = textoActual,
                    onTextoChange = { textoActual = it },
                    habilitado = !estaEnviando,
                    onEnviarClick = {
                        chatViewModel.enviarMensaje(textoActual)
                        textoActual = ""
                    }
                )
            }
        }
    }

/** A qué pantalla lleva cada botón que pone el asistente bajo su respuesta. */
private fun rutaDeEnlace(enlace: EnlaceChat): String = when (enlace.tipo) {
    "actividad" -> Rutas.ActividadDetalle.crearRuta(enlace.id)
    "proyecto" -> Rutas.ProyectoDetalle.crearRuta(enlace.id)
    "centro" -> Rutas.DirectorioDetalle.crearRuta(enlace.id)
    // Donaciones todavía usa datos de prueba en la app: se abre la sección.
    // Cuando se conecte al backend, cambiar por "detalle_donacion/${enlace.id}".
    "donacion" -> Rutas.Donar.ruta
    else -> Rutas.Inicio.ruta
}

@Preview(showBackground = true)
@Composable
private fun ChatbotScreenPreview() {
    FamiliasQueSumanTheme {
        ChatbotScreen(navController = rememberNavController())
    }
}