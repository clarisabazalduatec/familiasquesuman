package com.example.familiasquesuman.ui.screens.actividad

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.model.SesionDemoState
import com.example.familiasquesuman.ui.model.TipoSesion
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.actividad.components.CategoriaChip
import com.example.familiasquesuman.ui.screens.actividad.components.TarjetaInfoActividad
import com.example.familiasquesuman.ui.screens.actividad.components.TarjetaOrganizador
import com.example.familiasquesuman.ui.screens.actividad.components.TarjetaPlazasDisponibles
import com.example.familiasquesuman.ui.screens.actividad.components.obtenerColorCategoria
import com.example.familiasquesuman.ui.screens.actividad.components.obtenerIconoCategoria
import com.example.familiasquesuman.ui.screens.actividad.components.obtenerTextoColorCategoria
import com.example.familiasquesuman.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadDetalleScreen(
    navController: NavHostController,
    actividadId: String,
    viewModel: ActividadViewModel = viewModel()
) {
    LaunchedEffect(actividadId) {
        viewModel.cargarDetalle(actividadId)
    }

    val detalleState by viewModel.detalleUiState.collectAsState()

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.ACTIVIDADES,
        backButton = true
    ) { paddingValues ->
        when (val state = detalleState) {
            is ActividadDetalleUiState.Cargando -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FondoClaro)
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is ActividadDetalleUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FondoClaro)
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.mensaje,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.cargarDetalle(actividadId) }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            is ActividadDetalleUiState.Exito -> {
                val actividad = state.actividad
                val iconoCat = obtenerIconoCategoria(actividad.categoria)
                val colorFondoCat = obtenerColorCategoria(actividad.categoria)
                val colorTextoCat = obtenerTextoColorCategoria(actividad.categoria)
                val scrollState = rememberScrollState()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FondoClaro)
                        .padding(paddingValues)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        // Imagen Hero con superposición
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(GrisClaroFondo)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(64.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.8f)
                                            ),
                                            startY = 300f
                                        )
                                    )
                            )

                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = { /* Compartir */ },
                                    modifier = Modifier
                                        .background(Color.White, CircleShape)
                                        .size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Compartir",
                                        tint = AzulOscuro,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { /* Favorito */ },
                                    modifier = Modifier
                                        .background(Color.White, CircleShape)
                                        .size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorito",
                                        tint = AzulOscuro,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 32.dp
                                    )
                            ) {
                                CategoriaChip(
                                    categoria = actividad.categoria,
                                    icono = iconoCat,
                                    backgroundColor = colorFondoCat,
                                    textColor = colorTextoCat,
                                    mostrarIcono = true
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = actividad.titulo,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    lineHeight = 32.sp
                                )
                            }

                            Surface(
                                color = FondoClaro,
                                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp)
                                    .align(Alignment.BottomCenter)
                            ) {}
                        }

                        // Contenido de la actividad
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(FondoClaro)
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                TarjetaInfoActividad(
                                    titulo = "Ubicación",
                                    lineaPrincipal = actividad.ubicacion,
                                    lineaSecundaria = "",
                                    icono = Icons.Default.Place,
                                    modifier = Modifier.weight(1f)
                                )
                                TarjetaInfoActividad(
                                    titulo = "Fecha y Hora",
                                    lineaPrincipal = actividad.fecha,
                                    lineaSecundaria = actividad.horario,
                                    icono = Icons.Default.DateRange,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            TarjetaOrganizador(
                                nombreOrganizador = actividad.organizador,
                                esVerificado = actividad.organizadorVerificado
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = iconoCat,
                                    contentDescription = null,
                                    tint = colorTextoCat,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Acerca del proyecto",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulOscuro
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = actividad.acercaDe,
                                fontSize = 14.sp,
                                color = TextoGrisActividad,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            TarjetaPlazasDisponibles(
                                disponibles = actividad.lugaresDisponibles,
                                totales = actividad.lugaresTotales
                            )

                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }

                    val isScrolling by remember {
                        derivedStateOf { scrollState.isScrollInProgress }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    ) {
                        AnimatedVisibility(
                            visible = !isScrolling,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it })
                        ) {
                            Surface(
                                color = FondoClaro,
                                modifier = Modifier.fillMaxWidth(),
                                shadowElevation = 16.dp
                            ) {
                                Button(
                                    onClick = {
                                        if (SesionDemoState.tipoSesion == TipoSesion.INVITADO) {
                                            navController.navigate(Rutas.Login.ruta) {
                                                launchSingleTop = true
                                            }
                                        } else {
                                            navController.navigate(
                                                Rutas.ActividadSeleccionParticipantes.crearRuta(
                                                    actividadId
                                                )
                                            )
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Ambar),
                                    shape = RoundedCornerShape(24.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .height(56.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolunteerActivism,
                                        contentDescription = null,
                                        tint = AzulOscuro
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Quiero participar",
                                        color = AzulOscuro,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActividadDetalleScreenPreview() {
    FamiliasQueSumanTheme {
        ActividadDetalleScreen(
            navController = rememberNavController(),
            actividadId = "1"
        )
    }
}
