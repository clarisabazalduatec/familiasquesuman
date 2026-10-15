package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.familiasquesuman.domain.Actividad
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.admin.formulario.AdminActividadViewModel
import com.example.familiasquesuman.ui.screens.admin.formulario.TipoContenidoAdmin
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.ColorError
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.theme.GrisClaroFondo
import com.example.familiasquesuman.ui.theme.GrisTexto
import com.example.familiasquesuman.ui.theme.TextoVerde
import java.util.Date

@Composable
fun AdminModeracionActividadesScreen(
    navController: NavHostController,
    viewModel: AdminActividadViewModel = viewModel(),
) {
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }
    var actividadAEliminar by remember { mutableStateOf<Actividad?>(null) }

    val estado by viewModel.uiState.collectAsState()
    val actividades = estado.actividades

    LaunchedEffect(Unit) { viewModel.cargarLista() }

    val ahora = remember { Date() }
    fun Actividad.esPasada(): Boolean = terminaEn?.before(ahora) ?: false

    val activas = remember(actividades, ahora) { actividades.filter { it.activa && !it.esPasada() } }
    val pasadas = remember(actividades, ahora) { actividades.filter { it.esPasada() } }
    val desactivadas = remember(actividades, ahora) { actividades.filter { !it.activa && !it.esPasada() } }

    val listaAMostrar = when (pestanaSeleccionada) {
        0 -> activas
        1 -> pasadas
        2 -> desactivadas
        else -> emptyList()
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.popBackStack() },
        titulo = "Gestión de actividades",
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Actividades",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulMarino,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Revisa y edita las actividades de la comunidad.",
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
                    TabPill("Activas (${activas.size})", pestanaSeleccionada == 0) { pestanaSeleccionada = 0 }
                    TabPill("Pasadas (${pasadas.size})", pestanaSeleccionada == 1) { pestanaSeleccionada = 1 }
                    TabPill("Desactivadas (${desactivadas.size})", pestanaSeleccionada == 2) { pestanaSeleccionada = 2 }
                }

                if (estado.cargandoLista && actividades.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = Ambar)
                    }
                } else if (estado.errorLista != null && actividades.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = estado.errorLista ?: "", color = GrisTexto, fontSize = 14.sp)
                        TextButton(onClick = { viewModel.cargarLista() }) { Text("Reintentar", color = AzulMarino) }
                    }
                } else if (listaAMostrar.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "No hay actividades en esta categoría.", color = GrisTexto, fontSize = 14.sp)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(listaAMostrar, key = { it.id }) { actividad ->
                            TarjetaActividadAdmin(
                                actividad = actividad,
                                onEditarClick = {
                                    navController.navigate(
                                        Rutas.AdminEditarContenido.crearRuta(TipoContenidoAdmin.ACTIVIDAD, actividad.id),
                                    )
                                },
                                onAsistenciaClick = {
                                    navController.navigate(
                                        Rutas.AsistenciaActividades.crearRuta(actividad.id),
                                    )
                                },
                                onEliminarClick = {
                                    actividadAEliminar = actividad
                                },
                            )
                        }
                    }
                }
            }

            ExtendedFloatingActionButton(
                onClick = { navController.navigate(TipoContenidoAdmin.ACTIVIDAD.rutaCrear()) },
                containerColor = Ambar,
                contentColor = AzulMarino,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nueva Actividad", fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            )
        }
    }

    if (actividadAEliminar != null) {
        val act = actividadAEliminar!!
        AlertDialog(
            onDismissRequest = { actividadAEliminar = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = ColorError,
                    modifier = Modifier.size(40.dp),
                )
            },
            title = {
                Text(
                    text = "Eliminar Actividad",
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                    fontSize = 18.sp,
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar permanentemente la actividad '${act.titulo}'? Esta acción no se puede deshacer.",
                    color = GrisTexto,
                    fontSize = 13.sp,
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val toDelete = act
                        actividadAEliminar = null
                        viewModel.borrar(toDelete.id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorError, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Eliminar Definitivamente", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { actividadAEliminar = null }) {
                    Text("Cancelar", color = GrisTexto)
                }
            },
        )
    }
}

@Composable
fun TarjetaActividadAdmin(
    actividad: Actividad,
    onEditarClick: () -> Unit,
    onAsistenciaClick: () -> Unit,
    onEliminarClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = if (actividad.activa) "Activa" else "Desactivada",
                    color = if (actividad.activa) TextoVerde else ColorError,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                )
                val foto = actividad.imagenes.firstOrNull()
                if (foto != null) {
                    AsyncImage(
                        model = foto,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp)),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(GrisClaroFondo, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = GrisTexto)
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = actividad.titulo,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = actividad.fecha, fontSize = 11.sp, color = GrisTexto)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = actividad.ubicacion,
                        fontSize = 11.sp,
                        color = GrisTexto,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${actividad.lugaresDisponibles}/${actividad.lugaresTotales} lugares", fontSize = 11.sp, color = GrisTexto)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = onEditarClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulMarino,
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp),
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar", fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }

                Button(
                    onClick = onAsistenciaClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp),
                ) {
                    Icon(Icons.Default.HowToReg, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Asistencia", fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }

                OutlinedButton(
                    onClick = onEliminarClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorError),
                    border = BorderStroke(1.dp, ColorError.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp),
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminModeracionActividadesScreenPreview() {
    FamiliasQueSumanTheme {
        AdminModeracionActividadesScreen(
            navController = rememberNavController(),
        )
    }
}
