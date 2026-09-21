package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.domain.Publicacion
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme

private val publicacionesDeEjemplo = listOf(
    Publicacion(
        id = "1",
        autor = "Ana Martínez",
        tiempoRelativo = "Hace 1 hora",
        contenido = "¡Recordatorio! Capacitación del próximo sábado. Por favor confirmen su asistencia al taller sobre primeros auxilios. ¡Es muy importante!",
        esOficial = true,
        numeroLikes = 16,
        numeroComentarios = 4
    ),
    Publicacion(
        id = "2",
        autor = "Familia Pérez",
        tiempoRelativo = "Hace 2 horas",
        contenido = "¡Qué mañana tan increíble! Logramos plantar 20 árboles nuevos con la ayuda de los vecinos. Los niños aprendieron sobre el cuidado de la naturaleza.",
        esOficial = false,
        imagenUrl = "placeholder",
        numeroLikes = 24,
        numeroComentarios = 5
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComunidadScreen(navController: NavHostController) {
    var mostrarSoloOficial by remember { mutableStateOf(false) }

    val publicacionesFiltradas = remember(mostrarSoloOficial) {
        if (mostrarSoloOficial) publicacionesDeEjemplo.filter { it.esOficial }
        else publicacionesDeEjemplo
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                title = { Text("Familias que Suman+") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Rutas.NuevaPublicacion.ruta) }) {
                Icon(Icons.Default.Add, contentDescription = "Nueva publicación")
            }
        }
    ) { paddingInterno ->
        Column(modifier = Modifier.padding(paddingInterno).fillMaxSize().padding(16.dp)) {

            Text(text = "Comunidad en Acción", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Comparte y celebra el impacto de nuestra comunidad.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = !mostrarSoloOficial,
                    onClick = { mostrarSoloOficial = false },
                    label = { Text("Comunidad") }
                )
                FilterChip(
                    selected = mostrarSoloOficial,
                    onClick = { mostrarSoloOficial = true },
                    label = { Text("Oficial") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(publicacionesFiltradas) { publicacion ->
                    TarjetaPublicacion(publicacion = publicacion)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ComunidadScreenPreview() {
    FamiliasQueSumanTheme {
        ComunidadScreen(navController = rememberNavController())
    }
}