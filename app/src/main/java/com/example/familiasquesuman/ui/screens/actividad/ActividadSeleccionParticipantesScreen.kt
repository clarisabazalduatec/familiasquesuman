package com.example.familiasquesuman.ui.screens.actividad

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.remote.dto.Parentescos
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.AmarilloOscuro
import com.example.familiasquesuman.ui.theme.AzulOscuro
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.theme.FondoClaro
import com.example.familiasquesuman.ui.theme.TextoGrisActividad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadSeleccionParticipantesScreen(
    navController: NavHostController,
    actividadId: String,
    actividadViewModel: ActividadViewModel = viewModel(),
    inscripcionViewModel: InscripcionViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(actividadId) {
        actividadViewModel.cargarDetalle(actividadId)
        inscripcionViewModel.cargar(actividadId)
    }

    val detalleState by actividadViewModel.detalleUiState.collectAsState()
    val inscripcionState by inscripcionViewModel.uiState.collectAsState()

    // Manejar navegación al confirmar con éxito
    LaunchedEffect(inscripcionState.listo) {
        if (inscripcionState.listo) {
            inscripcionViewModel.consumirListo()
            navController.navigate(Rutas.ActividadConfirmacion.crearRuta(actividadId))
        }
    }

    // Manejar errores
    LaunchedEffect(inscripcionState.error) {
        inscripcionState.error?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            actividadViewModel.cargarDetalle(actividadId)
            inscripcionViewModel.limpiarError()
        }
    }

    val miUsuarioId = SesionManager.usuario?.id

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.ACTIVIDADES,
        navbar = false,
        backButton = true
    ) { paddingValues ->
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent,
            modifier = Modifier.padding(paddingValues)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (inscripcionState.cargando) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                } else if (inscripcionState.familia == null) {
                    // Sin familia: muestra un mensaje y deja solo el botón de ir al perfil
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = AzulOscuro,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Aún no perteneces a una familia",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulOscuro,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Para poder inscribirte a las actividades, primero debes crear una familia o unirte a una desde tu perfil.",
                            fontSize = 15.sp,
                            color = TextoGrisActividad,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = {
                                navController.navigate(Rutas.Perfil.ruta) {
                                    launchSingleTop = true
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, AmarilloOscuro),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAddAlt1,
                                contentDescription = null,
                                tint = AzulOscuro
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Ir a mi perfil a registrar mi familia",
                                color = AzulOscuro,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    val familia = inscripcionState.familia!!
                    val actividad = (detalleState as? ActividadDetalleUiState.Exito)?.actividad
                    val cupoTotal = actividad?.lugaresTotales ?: 999
                    val totalSeleccionados = inscripcionState.adultosSeleccionados.size + inscripcionState.menoresSeleccionados.size
                    val puedeGuardar = totalSeleccionados in 1..cupoTotal && !inscripcionState.guardando

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Selecciona quiénes participarán",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulOscuro,
                            textAlign = TextAlign.Center,
                            lineHeight = 34.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Puedes elegir a los miembros de tu familia que asistirán.",
                            fontSize = 15.sp,
                            color = AzulOscuro,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Adultos
                            items(familia.adultos, key = { "adulto_${it.id}" }) { adulto ->
                                val esTu = adulto.id == miUsuarioId
                                val estaSeleccionado = adulto.id in inscripcionState.adultosSeleccionados
                                val etiquetaNombre = if (esTu) "Tú" else adulto.nombre
                                val etiquetaRol = if (esTu) "Adulto" else (Parentescos.etiqueta(adulto.parentesco) ?: "Adulto")

                                TarjetaParticipanteSeleccionable(
                                    nombre = etiquetaNombre,
                                    subtitulo = etiquetaRol,
                                    seleccionado = estaSeleccionado,
                                    onClick = { inscripcionViewModel.alternarAdulto(adulto.id) }
                                )
                            }

                            // Menores
                            items(familia.menores, key = { "menor_${it.id}" }) { menor ->
                                val estaSeleccionado = menor.id in inscripcionState.menoresSeleccionados

                                TarjetaParticipanteSeleccionable(
                                    nombre = menor.nombre,
                                    subtitulo = "${menor.edad} años",
                                    seleccionado = estaSeleccionado,
                                    onClick = { inscripcionViewModel.alternarMenor(menor.id) }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = {
                                        navController.navigate(Rutas.Perfil.ruta) {
                                            launchSingleTop = true
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, AmarilloOscuro),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PersonAddAlt1,
                                        contentDescription = null,
                                        tint = AzulOscuro
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Editar integrantes en mi perfil",
                                        color = AzulOscuro,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }
                    }

                    // Botón inferior flotante
                    Surface(
                        color = FondoClaro,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        shadowElevation = 16.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Seleccionaste $totalSeleccionados · cupo máximo $cupoTotal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (totalSeleccionados > cupoTotal) Color.Red else AzulOscuro
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { inscripcionViewModel.confirmar(actividadId) },
                                enabled = puedeGuardar,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmarilloOscuro,
                                    disabledContainerColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                            ) {
                                if (inscripcionState.guardando) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = AzulOscuro
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Groups,
                                        contentDescription = null,
                                        tint = AzulOscuro
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Confirmar participación",
                                        color = AzulOscuro,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    if (inscripcionState.agregadoListaEspera) {
                        AlertDialog(
                            onDismissRequest = {
                                inscripcionViewModel.consumirAgregadoListaEspera()
                                navController.popBackStack()
                            },
                            title = {
                                Text(
                                    text = "Lista de espera",
                                    fontWeight = FontWeight.Bold,
                                    color = AzulOscuro
                                )
                            },
                            text = {
                                val pos = inscripcionState.inscripcionActual?.posicionEspera
                                val mensaje = if (pos != null) {
                                    "Fuiste añadido a la lista de espera en la posición $pos."
                                } else {
                                    "Fuiste añadido a la lista de espera."
                                }
                                Text(
                                    text = mensaje,
                                    fontSize = 15.sp
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        inscripcionViewModel.consumirAgregadoListaEspera()
                                        navController.popBackStack()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloOscuro, contentColor = AzulOscuro)
                                ) {
                                    Text("OK", fontWeight = FontWeight.Bold)
                                }
                            }
                        )
                    }

                    if (inscripcionState.mostrarDialogoListaEspera) {
                        AlertDialog(
                            onDismissRequest = { inscripcionViewModel.rechazarListaEspera() },
                            title = {
                                Text(
                                    text = "Actividad llena",
                                    fontWeight = FontWeight.Bold,
                                    color = AzulOscuro
                                )
                            },
                            text = {
                                Text(
                                    text = "¿Quieres unirte a la lista de espera?",
                                    fontSize = 15.sp
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = { inscripcionViewModel.aceptarListaEspera(actividadId) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloOscuro, contentColor = AzulOscuro)
                                ) {
                                    Text("Sí, unirme", fontWeight = FontWeight.Bold)
                                }
                            },
                            dismissButton = {
                                OutlinedButton(onClick = { inscripcionViewModel.rechazarListaEspera() }) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaParticipanteSeleccionable(
    nombre: String,
    subtitulo: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nombre,
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro,
                    fontSize = 16.sp
                )
                Text(
                    text = subtitulo,
                    color = TextoGrisActividad,
                    fontSize = 14.sp
                )
            }

            if (seleccionado) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Seleccionado",
                    tint = AmarilloOscuro,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Circle,
                    contentDescription = "No seleccionado",
                    tint = AzulOscuro,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActividadSeleccionParticipantesScreenPreview() {
    FamiliasQueSumanTheme {
        ActividadSeleccionParticipantesScreen(
            navController = rememberNavController(),
            actividadId = "1"
        )
    }
}
