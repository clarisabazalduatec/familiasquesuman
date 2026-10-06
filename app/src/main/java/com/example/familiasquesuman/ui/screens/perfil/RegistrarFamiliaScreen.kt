package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.FormularioFamiliaCard
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.theme.CremaFondo

@Composable
fun RegistrarFamiliaScreen(
    navController: NavHostController,
    viewModel: PerfilViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.PERFIL,
        backButton = true,
        onBackClick = { navController.popBackStack() }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .background(CremaFondo)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            FormularioFamiliaCard(
                uiState = uiState,
                listaHijos = viewModel.listaHijos,
                onAgregarHijo = { viewModel.agregarHijo() },
                onEliminarHijo = { hijo -> viewModel.eliminarHijo(hijo) },
                onCancelar = { navController.popBackStack() },
                onGuardar = { mama, papa, whatsapp, email, ciudad ->
                    viewModel.guardarPerfil(mama, papa, whatsapp, email, ciudad)
                    navController.popBackStack()
                }
            )
        }
    }
}
