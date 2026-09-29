package com.example.familiasquesuman.ui.screens.proyectos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.domain.EstadoProyecto
import com.example.familiasquesuman.ui.components.BarraBusqueda
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme

@Composable
fun ProyectosScreen(
    navController: NavHostController,
    viewModel: ProyectoViewModel = viewModel()
) {
    var pestañaActiva by remember { mutableStateOf(EstadoProyecto.ACTIVO) }
    var textoBusqueda by remember { mutableStateOf("") }

    val proyectosFiltrados = remember(pestañaActiva, textoBusqueda) {
        viewModel.obtenerProyectosPorEstado(pestañaActiva).filter {
            textoBusqueda.isBlank() || it.nombre.contains(textoBusqueda, ignoreCase = true)
        }
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.PROYECTOS,
        chatbot = false
    ) { paddingInterno ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(paddingInterno).fillMaxSize()
        ) {
            item {
                Text(text = "Proyectos", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Proyectos con causas y objetivos específicos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                TabRow(selectedTabIndex = if (pestañaActiva == EstadoProyecto.ACTIVO) 0 else 1) {
                    Tab(
                        selected = pestañaActiva == EstadoProyecto.ACTIVO,
                        onClick = { pestañaActiva = EstadoProyecto.ACTIVO },
                        text = { Text("Proyectos activos") }
                    )
                    Tab(
                        selected = pestañaActiva == EstadoProyecto.ANTERIOR,
                        onClick = { pestañaActiva = EstadoProyecto.ANTERIOR },
                        text = { Text("Proyectos anteriores") }
                    )
                }
            }

            item { BarraBusqueda(texto = textoBusqueda, onTextoChange = { textoBusqueda = it }, placeholder = "Buscar proyectos...") }

            if (proyectosFiltrados.isEmpty()) {
                item { EstadoVacioProyectos() }
            } else {
                items(proyectosFiltrados) { proyecto ->
                    TarjetaProyecto(
                        proyecto = proyecto,
                        onVerDetallesClick = {
                            navController.navigate(Rutas.ProyectoDetalle.crearRuta(proyecto.id))
                        }
                    )
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Proyectos curados por Familias que Suman",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProyectosScreenPreview() {
    FamiliasQueSumanTheme {
        ProyectosScreen(navController = rememberNavController())
    }
}