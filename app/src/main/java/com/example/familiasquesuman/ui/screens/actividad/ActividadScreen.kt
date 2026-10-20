package com.example.familiasquesuman.ui.screens.actividad

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.ui.components.EstadoErrorServidor
import com.example.familiasquesuman.ui.components.EstadoVacio
import com.example.familiasquesuman.ui.components.FiltrosChips
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.components.PieAvisoPrivacidad
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.actividad.components.HeaderDiaActividades
import com.example.familiasquesuman.ui.screens.actividad.components.TarjetaActividadDia
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.theme.GrisBordeClaro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadScreen(
    navController: NavHostController,
    viewModel: ActividadViewModel = viewModel(),
) {
    var mostrarCalendario by remember { mutableStateOf(value = false) }

    val ciudadId = SesionManager.usuario?.ciudad?.id
    LaunchedEffect(ciudadId) {
        viewModel.cargarActividades(ciudadId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val categorias by viewModel.categorias.collectAsState()

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.ACTIVIDADES,
        backButton = mostrarCalendario,
        onBackClick = { mostrarCalendario = false },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CremaFondo)
                .padding(paddingValues)
        ) {
            if (mostrarCalendario) {
                // Inscripciones reales de la familia (se recarga al abrirlo y si cambia la sesión)
                val calendario by viewModel.calendario.collectAsState()
                LaunchedEffect(SesionManager.token) { viewModel.cargarCalendario() }
                CalendarioActividades(
                    estado = calendario,
                    onActividadClick = { id ->
                        navController.navigate(Rutas.ActividadDetalle.crearRuta(id))
                    },
                    onReintentar = { viewModel.cargarCalendario() },
                    onIniciarSesion = { navController.navigate(Rutas.Login.ruta) },
                )
            } else {
                val actividadesFiltradas = remember(categoriaSeleccionada, uiState) {
                    val exitoState = uiState as? ActividadUiState.Exito
                    if (exitoState != null) {
                        val lista = if (categoriaSeleccionada == null) {
                            exitoState.actividades
                        } else {
                            exitoState.actividades.filter {
                                it.categoria.equals(categoriaSeleccionada, ignoreCase = true)
                            }
                        }
                        lista.sortedWith(
                            compareBy<com.example.familiasquesuman.domain.Actividad> { it.iniciaEn ?: java.util.Date(Long.MAX_VALUE) }
                        )
                    } else {
                        emptyList()
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp),
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Text(
                                text = "Actividades en Familia",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Actividades en familia para ayudar durante el año.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    item {
                        FiltrosChips(
                            opciones = categorias,
                            seleccionado = categoriaSeleccionada,
                            etiquetaPara = { it },
                            onSeleccionado = { viewModel.seleccionarCategoria(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }
                    item {
                        HorizontalDivider(
                            color = GrisBordeClaro,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 4.dp),
                        )
                    }
                    item {
                        // Antes decía una fecha fija ("Sábado, 24 de Octubre")
                        HeaderDiaActividades(fechaTexto = "Próximas actividades") {
                            mostrarCalendario = true
                        }
                    }

                    when (val state = uiState) {
                        is ActividadUiState.Cargando -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                        is ActividadUiState.Error -> {
                            item {
                                EstadoErrorServidor(
                                    mensaje = state.mensaje.ifBlank { "No se pudo conectar con el servidor." },
                                    onReintentar = { viewModel.reintentar() }
                                )
                            }
                        }
                        is ActividadUiState.Exito -> {
                            if (actividadesFiltradas.isEmpty()) {
                                item {
                                    EstadoVacio(
                                        mensaje = "No hay actividades disponibles por el momento.",
                                        modifier = Modifier.padding(vertical = 32.dp),
                                    )
                                }
                            } else {
                                items(actividadesFiltradas) { actividad ->
                                    TarjetaActividadDia(
                                        actividad = actividad,
                                        onActividadClick = { id ->
                                            navController.navigate(Rutas.ActividadDetalle.crearRuta(id))
                                        },
                                    )
                                }
                            }
                        }
                    }

                    // Aviso de Privacidad
                    item {
                        PieAvisoPrivacidad(
                            navController = navController,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }


                }

            }

        }

    }
}

@Preview(showBackground = true)
@Composable
private fun ActividadScreenPreview() {
    FamiliasQueSumanTheme {
        ActividadScreen(navController = rememberNavController())
    }
}