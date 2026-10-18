package com.example.familiasquesuman.ui.screens.donar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.familiasquesuman.ui.components.EstadoVacio

@Composable
fun EstadoVacioDonaciones(
    modifier: Modifier = Modifier,
    mensaje: String = "No hay donaciones en esta categoría por el momento."
) {
    EstadoVacio(
        icono = Icons.Default.PendingActions,
        titulo = "Sin donaciones disponibles",
        mensaje = mensaje,
        modifier = modifier
    )
}