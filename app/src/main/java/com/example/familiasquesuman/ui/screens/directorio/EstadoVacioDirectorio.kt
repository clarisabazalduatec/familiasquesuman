package com.example.familiasquesuman.ui.screens.directorio

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.familiasquesuman.ui.components.EstadoVacio

@Composable
fun EstadoVacioDirectorio(
    modifier: Modifier = Modifier,
    mensaje: String = "No hay centros en esta categoría por el momento.",
) {
    EstadoVacio(
        icono = Icons.Default.LocationOn,
        titulo = "Sin centros de visiteo",
        mensaje = mensaje,
        modifier = modifier,
    )
}
