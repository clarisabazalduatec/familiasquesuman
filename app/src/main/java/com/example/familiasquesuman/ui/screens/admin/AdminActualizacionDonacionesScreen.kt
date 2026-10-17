package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.TipoDonacion
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.admin.formulario.TipoContenidoAdmin
import com.example.familiasquesuman.ui.screens.donar.DonacionViewModel
import com.example.familiasquesuman.ui.theme.*

@Composable
fun AdminActualizacionDonacionesScreen(
    navController: NavHostController,
    viewModel: DonacionViewModel = viewModel()
) {
    val donaciones by viewModel.donaciones.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var pestanaSeleccionada by remember { mutableIntStateOf(0) }
    var donacionParaAporte by remember { mutableStateOf<Donacion?>(null) }
    var montoAporteText by remember { mutableStateOf("100") }
    var mostrarExitoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.esModoAdmin = true
        viewModel.cargarDonaciones()
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.popBackStack() },
        titulo = "Gestión de donaciones",
    ) { paddingVal ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVal),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CremaFondo),
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Text(
                        text = "Donaciones",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulMarino,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Revisa y edita campañas, donaciones en especie y voluntariados",
                        fontSize = 13.sp,
                        color = GrisTexto,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TabPill("Activas (${donaciones.count { it.activa }})", pestanaSeleccionada == 0) { pestanaSeleccionada = 0 }
                    TabPill("Desactivadas (${donaciones.count { !it.activa }})", pestanaSeleccionada == 1) { pestanaSeleccionada = 1 }
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AzulMarino)
                    }
                } else {
                    val donacionesFiltradas = if (pestanaSeleccionada == 1) {
                        donaciones.filter { !it.activa }
                    } else {
                        donaciones.filter { it.activa }
                    }

                    if (donacionesFiltradas.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(text = "No hay donaciones registradas en el servidor.", color = GrisTexto, fontSize = 14.sp)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(donacionesFiltradas) { donacion ->
                                TarjetaDonacionAdmin(
                                    donacion = donacion,
                                    onEditarClick = {
                                        navController.navigate(
                                            Rutas.AdminEditarContenido.crearRuta(TipoContenidoAdmin.DONACION, donacion.id),
                                        )
                                    },
                                    onSumarAporteClick = {
                                        montoAporteText = when (donacion.tipo) {
                                            TipoDonacion.CAMPANA -> "250"
                                            else -> "1"
                                        }
                                        donacionParaAporte = donacion
                                    },
                                )
                            }
                        }
                    }
                }
            }

            ExtendedFloatingActionButton(
                onClick = { navController.navigate(TipoContenidoAdmin.DONACION.rutaCrear()) },
                containerColor = Ambar,
                contentColor = AzulMarino,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nueva Donación", fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            )
        }
    }

    if (donacionParaAporte != null) {
        val don = donacionParaAporte!!
        val esCampana = don.tipo == TipoDonacion.CAMPANA
        val esTiempo = don.tipo == TipoDonacion.TIEMPO

        AlertDialog(
            onDismissRequest = { donacionParaAporte = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                    tint = Ambar,
                    modifier = Modifier.size(40.dp),
                )
            },
            title = {
                Text(
                    text = if (esTiempo) "Registrar Voluntario" else "Registrar Donación Recibida",
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                    fontSize = 18.sp,
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = if (esTiempo) "Ingresa el número de voluntarios a sumar a: ${don.titulo}" else "Ingresa la cantidad recibida para sumarla al avance de: ${don.titulo}",
                        color = GrisTexto,
                        fontSize = 13.sp,
                    )

                    if (esCampana) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            listOf("100", "250", "500", "1000").forEach { monto ->
                                FilterChip(
                                    selected = montoAporteText == monto,
                                    onClick = { montoAporteText = monto },
                                    label = { Text("+$monto", fontSize = 11.sp) },
                                    shape = RoundedCornerShape(10.dp),
                                )
                            }
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            listOf("1", "2", "5", "10").forEach { cant ->
                                FilterChip(
                                    selected = montoAporteText == cant,
                                    onClick = { montoAporteText = cant },
                                    label = { Text("+$cant", fontSize = 12.sp) },
                                    shape = RoundedCornerShape(10.dp),
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = montoAporteText,
                        onValueChange = { montoAporteText = it },
                        label = {
                            Text(
                                when (don.tipo) {
                                    TipoDonacion.CAMPANA -> "Monto a sumar ($)"
                                    TipoDonacion.TIEMPO -> "Cantidad de voluntarios"
                                    else -> "Cantidad de artículos recibidos"
                                }
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val monto = montoAporteText.toDoubleOrNull() ?: 0.0
                        if (monto > 0) {
                            viewModel.sumarAporte(
                                donacionLista = don,
                                cantidadSumar = monto,
                                onSuccess = {
                                    donacionParaAporte = null
                                    mostrarExitoDialog = true
                                },
                                onError = { _ ->
                                    donacionParaAporte = null
                                }
                            )
                        } else {
                            donacionParaAporte = null
                        }
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar, contentColor = AzulMarino),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = AzulMarino, strokeWidth = 2.dp)
                    } else {
                        Text("Guardar", fontWeight = FontWeight.Bold)
                    }
                }
            },
        )
    }

    if (mostrarExitoDialog) {
        AlertDialog(
            onDismissRequest = { mostrarExitoDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = VerdeExito,
                    modifier = Modifier.size(48.dp),
                )
            },
            title = {
                Text(
                    text = "¡Registro Exitoso!",
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                )
            },
            text = {
                Text(
                    text = "La donación recibida se ha procesado correctamente.",
                    color = GrisTexto,
                )
            },
            confirmButton = {
                Button(
                    onClick = { mostrarExitoDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold)
                }
            },
        )
    }
}

@Composable
fun TarjetaDonacionAdmin(
    donacion: Donacion,
    onEditarClick: () -> Unit,
    onSumarAporteClick: () -> Unit,
) {
    val tieneMeta = donacion.meta > 0f

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(GrisClaroFondo, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = GrisTexto)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = donacion.titulo,
                            fontWeight = FontWeight.Bold,
                            color = AzulMarino,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (donacion.activa) FondoVerde else Color(0xFFFFEBEE),
                        ) {
                            Text(
                                text = if (donacion.activa) "Activa" else "Desactivada",
                                color = if (donacion.activa) TextoVerde else ColorError,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = donacion.fundacion, fontSize = 11.sp, color = GrisTexto)
                        // Muestra el avance recaudado solo si la donación tiene meta establecida
                        if (tieneMeta) {
                            Text(
                                text = donacion.textoProgreso,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulMarino
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // La barra de progreso solo se muestra si tiene meta (> 0)
                    if (tieneMeta && donacion.tipo != TipoDonacion.TIEMPO) {
                        val progreso = (donacion.recaudado / donacion.meta).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progreso },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = AzulMarino,
                            trackColor = GrisBordeClaro,
                        )
                    } else if (donacion.tipo == TipoDonacion.TIEMPO) {
                        Text(
                            text = donacion.horario ?: donacion.fechaLimite ?: "Voluntariado",
                            fontSize = 10.sp,
                            color = GrisTexto,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Solo muestra el botón si NO es de TIEMPO y si tiene META (> 0)
                if (donacion.tipo != TipoDonacion.TIEMPO && tieneMeta) {
                    OutlinedButton(
                        onClick = onSumarAporteClick,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = AzulMarino
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sumar Aporte",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulMarino,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Button(
                    onClick = onEditarClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp),
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar", fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }
            }
        }
    }
}