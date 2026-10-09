package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.familiasquesuman.domain.CentroVisiteo
import com.example.familiasquesuman.domain.TipoCentro
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.admin.formulario.TipoContenidoAdmin
import com.example.familiasquesuman.ui.screens.directorio.centrosDeEjemplo
import com.example.familiasquesuman.ui.screens.directorio.eliminarCentroMock
import com.example.familiasquesuman.ui.theme.*

@Composable
fun AdminModeracionDirectorioScreen(navController: NavHostController) {
    var pestanaSeleccionada by remember { mutableIntStateOf(0) } // 0 = Todos, 1 = Asilos, 2 = Casas Hogar, 3 = Comedores
    var centroAEliminar by remember { mutableStateOf<CentroVisiteo?>(null) }

    val tipoFiltro = when (pestanaSeleccionada) {
        1 -> TipoCentro.ASILO
        2 -> TipoCentro.CASA_HOGAR
        3 -> TipoCentro.COMEDOR
        else -> null
    }

    val centrosFiltrados = remember(pestanaSeleccionada, centrosDeEjemplo.size) {
        if (tipoFiltro == null) {
            centrosDeEjemplo
        } else {
            centrosDeEjemplo.filter { it.tipo == tipoFiltro }
        }
    }

    val countAsilos = centrosDeEjemplo.count { it.tipo == TipoCentro.ASILO }
    val countCasasHogar = centrosDeEjemplo.count { it.tipo == TipoCentro.CASA_HOGAR }
    val countComedores = centrosDeEjemplo.count { it.tipo == TipoCentro.COMEDOR }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.popBackStack() },
        titulo = "Gestión de directorio",
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
                // Header
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Directorio de Visiteo",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulMarino,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Gestiona, edita y agrega nuevos centros de visiteo verificados.",
                        fontSize = 13.sp,
                        color = GrisTexto,
                    )
                }

                // Filter Tab Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TabPill("Todos (${centrosDeEjemplo.size})", pestanaSeleccionada == 0) { pestanaSeleccionada = 0 }
                    TabPill("Asilos ($countAsilos)", pestanaSeleccionada == 1) { pestanaSeleccionada = 1 }
                    TabPill("Casas Hogar ($countCasasHogar)", pestanaSeleccionada == 2) { pestanaSeleccionada = 2 }
                    TabPill("Comedores ($countComedores)", pestanaSeleccionada == 3) { pestanaSeleccionada = 3 }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (centrosFiltrados.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No hay centros registrados en esta categoría.",
                            color = GrisTexto,
                            fontSize = 14.sp,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(centrosFiltrados, key = { it.id }) { centro ->
                            TarjetaDirectorioAdmin(
                                centro = centro,
                                onEditarClick = {
                                    navController.navigate(
                                        Rutas.AdminEditarContenido.crearRuta(TipoContenidoAdmin.DIRECTORIO.claveRuta, centro.id.toString()),
                                    )
                                },
                                onEliminarClick = { centroAEliminar = centro },
                            )
                        }
                    }
                }
            }

            ExtendedFloatingActionButton(
                onClick = { navController.navigate(TipoContenidoAdmin.DIRECTORIO.rutaCrear()) },
                containerColor = Ambar,
                contentColor = AzulMarino,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nuevo Centro", fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            )
        }
    }


    // Modal Confirmar Eliminar Centro
    if (centroAEliminar != null) {
        val c = centroAEliminar!!
        AlertDialog(
            onDismissRequest = { centroAEliminar = null },
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
                    text = "Eliminar Centro",
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                    fontSize = 18.sp,
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar permanentemente '${c.nombre}' del directorio? Esta acción no se puede deshacer.",
                    color = GrisTexto,
                    fontSize = 13.sp,
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        eliminarCentroMock(c.id)
                        centroAEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorError, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Eliminar Definitivamente", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { centroAEliminar = null }) {
                    Text("Cancelar", color = GrisTexto)
                }
            },
        )
    }
}

@Composable
fun TarjetaDirectorioAdmin(
    centro: CentroVisiteo,
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
                verticalAlignment = Alignment.Top,
            ) {
                // Logo o Ícono del Centro
                if (centro.logoUrl != null) {
                    AsyncImage(
                        model = centro.logoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp)),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GrisClaroFondo),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Domain,
                            contentDescription = null,
                            tint = AzulMarino,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AmbarClaro,
                        ) {
                            Text(
                                text = centro.tipo.etiqueta,
                                color = Ambar,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            )
                        }

                        if (!centro.activo) {
                            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFFFEBEE)) {
                                Text(
                                    text = "Desactivado",
                                    color = ColorError,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                )
                            }
                        } else if (centro.verificado) Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "Verificado",
                                tint = AzulMarino,
                                modifier = Modifier.size(14.dp),
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Verificado",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulMarino,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = centro.nombre,
                        fontWeight = FontWeight.Bold,
                        color = AzulMarino,
                        fontSize = 15.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = GrisTexto,
                            modifier = Modifier.size(13.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = centro.direccion,
                            fontSize = 11.sp,
                            color = GrisTexto,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = centro.descripcionCorta,
                fontSize = 12.sp,
                color = GrisTexto,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Botones Acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
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

                Spacer(modifier = Modifier.width(8.dp))

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


@Preview(showBackground = true)
@Composable
private fun AdminModeracionDirectorioScreenPreview() {
    FamiliasQueSumanTheme {
        AdminModeracionDirectorioScreen(rememberNavController())
    }
}
