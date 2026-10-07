package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.FormularioFamiliaCard
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
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
                            listaAdultos = viewModel.listaAdultos,
                            listaMenores = viewModel.listaMenores,
                            onAgregarAdulto = { viewModel.agregarAdulto() },
                            onEliminarAdulto = { adulto -> viewModel.eliminarAdulto(adulto) },
                            onActualizarAdulto = { adulto -> viewModel.actualizarAdulto(adulto) },
                            onAgregarMenor = { viewModel.agregarMenor() },
                            onEliminarMenor = { menor -> viewModel.eliminarMenor(menor) },
                            onActualizarMenor = { menor -> viewModel.actualizarMenor(menor) },
                            onCancelar = { viewModel.cancelarEdicion() },
                            onGuardar = { viewModel.guardarPerfil() }
                        )
                    }
                    uiState.esPerfilRegistrado -> {
                        DatosFamiliaRegistradaCard(
                            uiState = uiState,
                            listaAdultos = viewModel.listaAdultos,
                            listaMenores = viewModel.listaMenores,
                            onEditarClick = { viewModel.abrirEdicion() }
                        )
                    }
                    else -> {
                        DatosFamiliaVacioCard(
                            onRegistrarClick = { viewModel.abrirEdicion() }
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