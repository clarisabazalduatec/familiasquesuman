package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.domain.EstadoProyecto
import com.example.familiasquesuman.domain.Proyecto
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.proyectos.proyectosMockData
import com.example.familiasquesuman.ui.theme.*

@Composable
fun AdminModeracionProyectosScreen(navController: NavHostController) {
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

    val estadoActual = if (pestanaSeleccionada == 0) EstadoProyecto.ACTIVO else EstadoProyecto.ANTERIOR
    val proyectosFiltrados = remember(pestanaSeleccionada, proyectosMockData.size) {
        proyectosMockData.filter { it.estado == estadoActual }
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.popBackStack() },
        titulo = "Gestión de proyectos"
    ) { paddingVal ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVal)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CremaFondo)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TabPill("Proyectos Activos", pestanaSeleccionada == 0) { pestanaSeleccionada = 0 }
                    TabPill("Proyectos Anteriores", pestanaSeleccionada == 1) { pestanaSeleccionada = 1 }
                }

                if (proyectosFiltrados.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay proyectos en esta categoría.",
                            color = GrisTexto,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(proyectosFiltrados) { proyecto ->
                            TarjetaProyectoAdmin(
                                proyecto = proyecto,
                                onEditarClick = {
                                    navController.navigate(
                                        Rutas.AdminEditarContenido.crearRuta("proyecto", proyecto.id)
                                    )
                                }
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
                text = { Text("Nuevo Proyecto", fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun TarjetaProyectoAdmin(
    proyecto: Proyecto,
    onEditarClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = proyecto.nombre,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (proyecto.estado == EstadoProyecto.ACTIVO) FondoVerde else AmbarClaro
                ) {
                    Text(
                        text = if (proyecto.estado == EstadoProyecto.ACTIVO) "Activo" else "Anterior",
                        color = if (proyecto.estado == EstadoProyecto.ACTIVO) TextoVerde else Ambar,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = proyecto.descripcionCorta,
                fontSize = 12.sp,
                color = GrisTexto,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Groups, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = proyecto.participantes, fontSize = 11.sp, color = GrisTexto)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = proyecto.ubicacion, fontSize = 11.sp, color = GrisTexto)
                    }
                }

                Button(
                    onClick = onEditarClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Editar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
