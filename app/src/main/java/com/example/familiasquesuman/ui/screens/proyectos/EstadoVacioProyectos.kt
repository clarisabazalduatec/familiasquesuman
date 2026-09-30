package com.example.familiasquesuman.ui.screens.proyectos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightbulbCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.familiasquesuman.ui.components.EstadoVacio

@Composable
fun EstadoVacioProyectos(
    modifier: Modifier = Modifier,
    mensaje: String = "No hay proyectos en esta categoría por el momento."
) {
    EstadoVacio(
        icono = Icons.Default.LightbulbCircle,
        titulo = "Sin proyectos disponibles",
        mensaje = mensaje,
        modifier = modifier
    )
}
