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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
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
    viewModel: ActividadViewModel = viewModel(),
    inscripcionViewModel: InscripcionViewModel = viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, actividadId) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.cargarDetalle(actividadId)
                if (SesionManager.tipoSesion != TipoSesion.INVITADO) {
                    inscripcionViewModel.cargar(actividadId, forzar = true)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val detalleState by viewModel.detalleUiState.collectAsState()
    val inscripcionState by inscripcionViewModel.uiState.collectAsState()
    var mostrarDialogoCancelar by remember { mutableStateOf(false) }

    LaunchedEffect(inscripcionState.listo) {
        if (inscripcionState.listo) {
            viewModel.cargarDetalle(actividadId)
            inscripcionViewModel.cargar(actividadId, forzar = true)
            inscripcionViewModel.consumirListo()
        }
    }

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
                        Button(onClick = {
                            viewModel.cargarDetalle(actividadId)
                            if (SesionManager.tipoSesion != TipoSesion.INVITADO) {
                                inscripcionViewModel.cargar(actividadId, forzar = true)
                            }
                        }) {
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

                            val imageUrl = actividad.imagenes.firstOrNull()
                            if (imageUrl != null) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = actividad.titulo,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
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

                            Spacer(modifier = Modifier.height(140.dp))
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
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    val esInvitado = SesionManager.tipoSesion == TipoSesion.INVITADO
                                    val estaInscrito = !esInvitado && inscripcionState.inscripcionActual != null
                                    val sinLugares = actividad.lugaresDisponibles <= 0

                                    if (esInvitado) {
                                        Button(
                                            onClick = {
                                                navController.navigate(Rutas.Login.ruta) {
                                                    launchSingleTop = true
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Ambar),
                                            shape = RoundedCornerShape(24.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(56.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.VolunteerActivism,
                                                contentDescription = null,
                                                tint = AzulOscuro
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Inscribirse",
                                                color = AzulOscuro,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    } else if (estaInscrito) {
                                        Button(
                                            onClick = {
                                                navController.navigate(
                                                    Rutas.ActividadSeleccionParticipantes.crearRuta(
                                                        actividadId
                                                    )
                                                )
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Ambar),
                                            shape = RoundedCornerShape(24.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(52.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = null,
                                                tint = AzulOscuro
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Modificar inscripción",
                                                color = AzulOscuro,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        OutlinedButton(
                                            onClick = { mostrarDialogoCancelar = true },
                                            shape = RoundedCornerShape(24.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(48.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = Color.Red
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Cancelar inscripción",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    } else if (sinLugares) {
                                        Button(
                                            onClick = {},
                                            enabled = false,
                                            colors = ButtonDefaults.buttonColors(
                                                disabledContainerColor = Color.LightGray,
                                                disabledContentColor = Color.DarkGray
                                            ),
                                            shape = RoundedCornerShape(24.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(56.dp)
                                        ) {
                                            Text(
                                                text = "Sin lugares disponibles",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                navController.navigate(
                                                    Rutas.ActividadSeleccionParticipantes.crearRuta(
                                                        actividadId
                                                    )
                                                )
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Ambar),
                                            shape = RoundedCornerShape(24.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(56.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.VolunteerActivism,
                                                contentDescription = null,
                                                tint = AzulOscuro
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Inscribirse",
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
    }

    if (mostrarDialogoCancelar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCancelar = false },
            title = {
                Text(
                    text = "Cancelar inscripción",
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas cancelar la inscripción de tu familia a esta actividad?",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoCancelar = false
                        inscripcionViewModel.cancelar(actividadId)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
                ) {
                    Text("Sí, cancelar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { mostrarDialogoCancelar = false }) {
                    Text("No")
                }
            }
        )
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
