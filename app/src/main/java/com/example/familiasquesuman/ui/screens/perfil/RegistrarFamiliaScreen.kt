package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.theme.CremaFondo

@Composable
fun RegistrarFamiliaScreen(
    navController: NavHostController
) {
    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.PERFIL,
        backButton = true
    ) { paddingInterno ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .background(CremaFondo)
        ) {
            // Pantalla vacía por ahora
        }
    }
}
