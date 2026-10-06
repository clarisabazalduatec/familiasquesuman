package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.FormularioFamiliaCard
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.inicio.SaludoConUbicacion
import com.example.familiasquesuman.ui.theme.*

@Composable
fun PerfilScreen(
    navController: NavHostController,
    viewModel: PerfilViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.PERFIL
    ) { paddingInterno ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .background(CremaFondo),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 2.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SaludoConUbicacion(
                    nombreUsuario = "Mariana",
                    ciudad = "Monterrey, NL"
                )
            }

            item {
                when {
                    uiState.enModoEdicion -> {
                        FormularioFamiliaCard(
                            uiState = uiState,
                            listaHijos = viewModel.listaHijos,
                            onAgregarHijo = { viewModel.agregarHijo() },
                            onEliminarHijo = { hijo -> viewModel.eliminarHijo(hijo) },
                            onCancelar = { viewModel.cancelarEdicion() },
                            onGuardar = { mama, papa, whatsapp, email, ciudad ->
                                viewModel.guardarPerfil(mama, papa, whatsapp, email, ciudad)
                            }
                        )
                    }
                    uiState.esPerfilRegistrado -> {
                        DatosFamiliaRegistradaCard(
                            uiState = uiState,
                            listaHijos = viewModel.listaHijos,
                            onEditarClick = { viewModel.abrirEdicion() }
                        )
                    }
                    else -> {
                        DatosFamiliaVacioCard(
                            onRegistrarClick = {
                                navController.navigate(Rutas.RegistrarFamilia.ruta)
                            }
                        )
                    }
                }
            }

            item {
                MisInsigniasCard()
            }
        }
    }
}