package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.theme.*

@Composable
fun AdminModeracionActividadesScreen(navController: NavHostController) {
    var pestanaSeleccionada by remember { mutableStateOf(0) }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.popBackStack() },
        titulo = "Moderación de actividades",
        acciones = {
            IconButton(onClick = { /* Filtrar */ }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtrar", tint = AzulMarino)
            }
        }
    ) { paddingVal ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVal)
                .background(CremaFondo)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabPill("Activas (6)", pestanaSeleccionada == 0) { pestanaSeleccionada = 0 }
                TabPill("Pasadas", pestanaSeleccionada == 1) { pestanaSeleccionada = 1 }
                TabPill("Canceladas", pestanaSeleccionada == 2) { pestanaSeleccionada = 2 }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TarjetaActividadAdmin(
                        titulo = "Jornada de limpieza en el parque",
                        fecha = "Sáb, 27 de sep. 2025",
                        lugar = "Parque Central, Hermosillo",
                        inscritos = "12/20 inscritos"
                    )
                }
                item {
                    TarjetaActividadAdmin(
                        titulo = "Taller de cocina saludable para familias",
                        fecha = "Lun, 29 de sep. 2025",
                        lugar = "Centro Comunitario Norte",
                        inscritos = "8/15 inscritos"
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaActividadAdmin(
    titulo: String,
    fecha: String,
    lugar: String,
    inscritos: String
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

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = fecha, fontSize = 11.sp, color = GrisTexto)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = lugar, fontSize = 11.sp, color = GrisTexto)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = inscritos, fontSize = 11.sp, color = GrisTexto)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = GrisTexto)
        }
    }
}
