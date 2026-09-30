package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.*

@Composable
fun AdminActualizacionDonacionesScreen(navController: NavHostController) {
    var pestanaSeleccionada by remember { mutableStateOf(0) }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.popBackStack() },
        titulo = "Actualización de donaciones",
        acciones = {
            IconButton(onClick = { navController.navigate(Rutas.AdminCrearContenido.ruta) }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar donación", tint = AzulMarino)
            }
        }
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
                    TabPill("Activas (4)", pestanaSeleccionada == 0) { pestanaSeleccionada = 0 }
                    TabPill("Finalizadas", pestanaSeleccionada == 1) { pestanaSeleccionada = 1 }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        TarjetaDonacionAdmin(
                            titulo = "Despensas básicas para familias",
                            progreso = 0.65f,
                            porcentajeTexto = "65%",
                            estadisticas = "1,300 / 2,000 despensas",
                            fechaFin = "Finaliza: 31 oct. 2025"
                        )
                    }
                    item {
                        TarjetaDonacionAdmin(
                            titulo = "Útiles escolares",
                            progreso = 0.42f,
                            porcentajeTexto = "42%",
                            estadisticas = "420 / 1,000 paquetes",
                            fechaFin = "Finaliza: 15 nov. 2025"
                        )
                    }
                }
            }

            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Rutas.AdminCrearContenido.ruta) },
                containerColor = Ambar,
                contentColor = AzulMarino,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nueva Donación", fontWeight = FontWeight.Bold) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun TarjetaDonacionAdmin(
    titulo: String,
    progreso: Float,
    porcentajeTexto: String,
    estadisticas: String,
    fechaFin: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(GrisClaroFondo, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Image, contentDescription = null, tint = GrisTexto)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = titulo,
                        fontWeight = FontWeight.Bold,
                        color = AzulMarino,
                        fontSize = 14.sp,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = FondoVerde
                    ) {
                        Text(
                            text = "Activa",
                            color = TextoVerde,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = estadisticas, fontSize = 11.sp, color = GrisTexto)
                    Text(text = porcentajeTexto, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AzulMarino)
                }

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = AzulMarino,
                    trackColor = GrisBordeClaro
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = fechaFin, fontSize = 11.sp, color = GrisTexto)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = GrisTexto)
        }
    }
}
