package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.CremaFondo

@Composable
fun AdminScreen(
    navController: NavHostController
) {

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        acciones = {
            IconButton(
                onClick = {
                    navController.navigate(
                        Rutas.Notificaciones.ruta
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = AzulMarino
                )
            }
        }

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
                Text("Admin")
            }
        }
    }
}

