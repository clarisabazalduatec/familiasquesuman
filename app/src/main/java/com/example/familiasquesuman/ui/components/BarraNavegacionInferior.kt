package com.example.familiasquesuman.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightbulbCircle
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

enum class PantallaPrincipal(val etiqueta: String) {
    INICIO("Inicio"),
    ACTIVIDADES("Actividades"),
    PROYECTOS("Proyectos"),
    DONAR("Donar"),
    DIRECTORIO("Directorio"),
    CHATBOT("Asistente")
}

@Composable
fun BarraNavegacionInferior(
    pantallaActual: PantallaPrincipal,
    onPantallaSeleccionada: (PantallaPrincipal) -> Unit
) {

    NavigationBar {
        NavigationBarItem(
            selected = pantallaActual == PantallaPrincipal.INICIO,
            onClick = { onPantallaSeleccionada(PantallaPrincipal.INICIO) },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = pantallaActual == PantallaPrincipal.ACTIVIDADES,
            onClick = { onPantallaSeleccionada(PantallaPrincipal.ACTIVIDADES) },
            icon = { Icon(Icons.Default.Groups, contentDescription = null) },
            label = { Text("Actividad") }
        )
        NavigationBarItem(
            selected = pantallaActual == PantallaPrincipal.PROYECTOS,
            onClick = { onPantallaSeleccionada(PantallaPrincipal.PROYECTOS) },
            icon = { Icon(Icons.Default.LightbulbCircle, contentDescription = null) },
            label = { Text("Proyectos") }
        )
        NavigationBarItem(
            selected = pantallaActual == PantallaPrincipal.DONAR,
            onClick = { onPantallaSeleccionada(PantallaPrincipal.DONAR) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            label = { Text("Donar") }
        )
        NavigationBarItem(
            selected = pantallaActual == PantallaPrincipal.DIRECTORIO,
            onClick = { onPantallaSeleccionada(PantallaPrincipal.DIRECTORIO) },
            icon = { Icon(Icons.Default.Place, contentDescription = null) },
            label = { Text("Directorio") }
        )
        NavigationBarItem(
            selected = pantallaActual == PantallaPrincipal.CHATBOT,
            onClick = { onPantallaSeleccionada(PantallaPrincipal.CHATBOT) },
            icon = { Icon(Icons.Default.ChatBubble, contentDescription = null) },
            label = { Text("Chatbot") }
        )
    }
}