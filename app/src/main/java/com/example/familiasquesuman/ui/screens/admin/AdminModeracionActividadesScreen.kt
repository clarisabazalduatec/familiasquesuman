package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.data.ActividadMock
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.admin.formulario.TipoContenidoAdmin
import com.example.familiasquesuman.ui.theme.*

@Composable
fun AdminModeracionActividadesScreen(navController: NavHostController) {
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TabPill("Activas (${actividadesMockData.count { it.activa }})", pestanaSeleccionada == 0) { pestanaSeleccionada = 0 }
                    TabPill("Pasadas", pestanaSeleccionada == 1) { pestanaSeleccionada = 1 }
                    TabPill("Desactivadas (${actividadesMockData.count { !it.activa }})", pestanaSeleccionada == 2) { pestanaSeleccionada = 2 }
                }

                val listaAMostrar = remember(pestanaSeleccionada, actividadesMockData.size) {
                    if (pestanaSeleccionada == 2) {
                        actividadesMockData.filter { !it.activa }
                    } else {
                        actividadesMockData.filter { it.activa }
                    }
                }

                if (listaAMostrar.isEmpty()) {
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
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(listaAMostrar) { actividad ->
                            TarjetaActividadAdmin(
                                actividad = actividad,
                                onEditarClick = {
                                    navController.navigate(
                                        Rutas.AdminEditarContenido.crearRuta(TipoContenidoAdmin.ACTIVIDAD, actividad.id.toString()),
                                    )
                                },
                                onAsistenciaClick = {
                                    navController.navigate(
                                        Rutas.AsistenciaActividades.crearRuta(actividad.id),
                                    )
                                },
                            )
                        }
                    }
                }
            }

            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Rutas.AdminCrearContenido.ruta) },
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
}

@Composable
fun TarjetaActividadAdmin(
    actividad: ActividadMock,
    onEditarClick: () -> Unit,
    onAsistenciaClick: () -> Unit,
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
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(GrisClaroFondo, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = GrisTexto)
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
