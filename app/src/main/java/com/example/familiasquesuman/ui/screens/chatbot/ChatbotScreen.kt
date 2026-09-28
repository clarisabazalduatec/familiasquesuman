package com.example.familiasquesuman.ui.screens.chatbot

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
        navController = navController,
        pantallaActual = PantallaPrincipal.CHATBOT,
        acciones = {
            IconButton(
                onClick = {
                    navController.navigate(Rutas.Notificaciones.ruta)
                }
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
            }
        }
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
                    items(mensajes.reversed()) { mensaje ->
                        BurbujaMensaje(mensaje = mensaje)
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


@Preview(showBackground = true)
@Composable
private fun ChatbotScreenPreview() {
    FamiliasQueSumanTheme {
        ChatbotScreen(navController = rememberNavController())
    }
}