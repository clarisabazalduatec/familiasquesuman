package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuLateral(
    onIniciarSesionClick: () -> Unit,
    onCrearCuentaClick: () -> Unit,
    onComunidadClick: () -> Unit
) {
    ModalDrawerSheet {
        Text(
            text = "Familias que Suman+",
            modifier = androidx.compose.ui.Modifier.padding(16.dp)
        )
        HorizontalDivider()
        NavigationDrawerItem(
            label = { Text("Iniciar sesión") },
            selected = false,
            onClick = onIniciarSesionClick,
            modifier = androidx.compose.ui.Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Crear cuenta") },
            selected = false,
            onClick = onCrearCuentaClick,
            modifier = androidx.compose.ui.Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Comunidad") },
            selected = false,
            onClick = onComunidadClick,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}