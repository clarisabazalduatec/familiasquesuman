package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.domain.EstadoProyecto
import com.example.familiasquesuman.domain.Proyecto
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.admin.formulario.TipoContenidoAdmin
import com.example.familiasquesuman.ui.screens.admin.formulario.eliminarIniciativa
import com.example.familiasquesuman.ui.screens.proyectos.proyectosMockData
import com.example.familiasquesuman.ui.theme.*

@Composable
fun AdminModeracionProyectosScreen(navController: NavHostController) {
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }
    var proyectoAEliminar by remember { mutableStateOf<Proyecto?>(null) }

    val proyectosFiltrados = remember(pestanaSeleccionada, proyectosMockData.size) {
        if (pestanaSeleccionada == 0) {
            proyectosMockData.filter { it.estado == EstadoProyecto.ACTIVO && it.activo }
        } else {
            proyectosMockData.filter { it.estado == EstadoProyecto.ANTERIOR || !it.activo }
        }
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.popBackStack() },
        titulo = "Gestión de proyectos",
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
                        text = "Proyectos",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulMarino,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Revisa y edita los proyectos sociales de la plataforma.",
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
                    val countActivos = proyectosMockData.count { it.estado == EstadoProyecto.ACTIVO && it.activo }
                    val countAnteriores = proyectosMockData.count { it.estado == EstadoProyecto.ANTERIOR || !it.activo }

                    TabPill("Proyectos Activos ($countActivos)", pestanaSeleccionada == 0) { pestanaSeleccionada = 0 }
                    TabPill("Proyectos Anteriores ($countAnteriores)", pestanaSeleccionada == 1) { pestanaSeleccionada = 1 }
                }

                if (proyectosFiltrados.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No hay proyectos en esta categoría.",
                            color = GrisTexto,
                            fontSize = 14.sp,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(proyectosFiltrados) { proyecto ->
                            TarjetaProyectoAdmin(
                                proyecto = proyecto,
                                onEditarClick = {
                                    navController.navigate(
                                        Rutas.AdminEditarContenido.crearRuta(TipoContenidoAdmin.PROYECTO, proyecto.id),
                                    )
                                },
                                onEliminarClick = {
                                    proyectoAEliminar = proyecto
                                },
                            )
                        }
                    }
                }
            }

            ExtendedFloatingActionButton(
                onClick = { navController.navigate(TipoContenidoAdmin.PROYECTO.rutaCrear()) },
                containerColor = Ambar,
                contentColor = AzulMarino,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nuevo Proyecto", fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            )
        }
    }

    // Modal de Confirmación para Eliminar Proyecto
    if (proyectoAEliminar != null) {
        val proy = proyectoAEliminar!!
        AlertDialog(
            onDismissRequest = { proyectoAEliminar = null },
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
                    text = "Eliminar Proyecto",
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                    fontSize = 18.sp,
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar permanentemente el proyecto '${proy.nombre}'? Esta acción borrará la iniciativa por completo y no se podrá recuperar.",
                    color = GrisTexto,
                    fontSize = 13.sp,
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        eliminarIniciativa(TipoContenidoAdmin.PROYECTO, proy.id)
                        proyectoAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorError, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Eliminar Definitivamente", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { proyectoAEliminar = null }) {
                    Text("Cancelar", color = GrisTexto)
                }
            },
        )
    }
}

@Composable
fun TarjetaProyectoAdmin(
    proyecto: Proyecto,
    onEditarClick: () -> Unit,
    onEliminarClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = proyecto.nombre,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                    fontSize = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))

                val colorFondoBadge = when {
                    !proyecto.activo -> Color(0xFFFFEBEE)
                    proyecto.estado == EstadoProyecto.ACTIVO -> FondoVerde
                    else -> AmbarClaro
                }
                val colorTextoBadge = when {
                    !proyecto.activo -> ColorError
                    proyecto.estado == EstadoProyecto.ACTIVO -> TextoVerde
                    else -> Ambar
                }
                val textoBadge = when {
                    !proyecto.activo -> "Desactivado"
                    proyecto.estado == EstadoProyecto.ACTIVO -> "Activo"
                    else -> "Anterior"
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = colorFondoBadge,
                ) {
                    Text(
                        text = textoBadge,
                        color = colorTextoBadge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = proyecto.descripcionCorta,
                fontSize = 12.sp,
                color = GrisTexto,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false),
                    ) {
                        Icon(Icons.Default.Groups, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = proyecto.participantes,
                            fontSize = 11.sp,
                            color = GrisTexto,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false),
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = proyecto.ubicacion,
                            fontSize = 11.sp,
                            color = GrisTexto,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = onEliminarClick,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorError),
                        border = BorderStroke(1.dp, ColorError.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp),
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", modifier = Modifier.size(14.dp))
                    }

                    Button(
                        onClick = onEditarClick,
                        colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp),
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Editar", fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminModeracionProyectosScreenPreview(){
    FamiliasQueSumanTheme {
        AdminModeracionProyectosScreen(rememberNavController())
    }
}
