package com.example.familiasquesuman.ui.screens.proyectos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectosScreen(
    navController: NavHostController,
    viewModel: ProyectoViewModel = viewModel(),
) {
    var pestanaActiva by remember { mutableStateOf(EstadoProyecto.ACTIVO) }
    var textoBusqueda by remember { mutableStateOf("") }

    val proyectosFiltrados = remember(pestanaActiva, textoBusqueda) {
        viewModel.obtenerProyectosPorEstado(pestanaActiva).filter {
            textoBusqueda.isBlank() ||
                    it.nombre.contains(textoBusqueda, ignoreCase = true) ||
                    it.descripcionCorta.contains(textoBusqueda, ignoreCase = true)
        }
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.PROYECTOS,
        chatbot = false,
        acciones = {
            IconButton(onClick = { navController.navigate(Rutas.Notificaciones.ruta) }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
            }
        },
    ) { paddingInterno ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize(),
        ) {
            item {
                Column(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "Proyectos",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Proyectos con causas y objetivos específicos.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            item {
                PrimaryTabRow(
                    selectedTabIndex = if (pestanaActiva == EstadoProyecto.ACTIVO) 0 else 1,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    Tab(
                        selected = pestanaActiva == EstadoProyecto.ACTIVO,
                        onClick = { pestanaActiva = EstadoProyecto.ACTIVO },
                        text = {
                            Text(
                                text = "Proyectos activos",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (pestanaActiva == EstadoProyecto.ACTIVO) FontWeight.Bold else FontWeight.Normal,
                            )
                        },
                    )
                    Tab(
                        selected = pestanaActiva == EstadoProyecto.ANTERIOR,
                        onClick = { pestanaActiva = EstadoProyecto.ANTERIOR },
                        text = {
                            Text(
                                text = "Proyectos anteriores",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (pestanaActiva == EstadoProyecto.ANTERIOR) FontWeight.Bold else FontWeight.Normal,
                            )
                        },
                    )
                }
            }

            item {
                BarraBusqueda(
                    texto = textoBusqueda,
                    onTextoChange = { textoBusqueda = it },
                    placeholder = "Buscar proyectos...",
                )
            }

            if (proyectosFiltrados.isEmpty()) {
                item { EstadoVacioProyectos() }
            } else {
                items(proyectosFiltrados) { proyecto ->
                    TarjetaProyecto(
                        proyecto = proyecto,
                        onVerDetallesClick = {
                            navController.navigate(Rutas.ProyectoDetalle.crearRuta(proyecto.id))
                        },
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Proyectos curados por Familias que Suman",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
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
