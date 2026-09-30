package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.*

@Composable
fun AdminScreen(navController: NavHostController) {
    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.navigate(Rutas.Inicio.ruta) },
        acciones = {
            IconButton(onClick = { navController.navigate(Rutas.Notificaciones.ruta) }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = AzulMarino)
            }
        }
    ) { paddingInterno ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .background(CremaFondo),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hola, Administrador",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aquí puedes gestionar el contenido y el impacto de la comunidad.",
                    fontSize = 14.sp,
                    color = GrisTexto
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                AdminCardOption(
                    titulo = "Moderación de comunidad",
                    descripcion = "Revisa y aprueba publicaciones de la comunidad.",
                    icono = Icons.Default.Groups,
                    fondoIcono = FondoAzulClaro,
                    colorIcono = TextoAzul,
                    onClick = { navController.navigate(Rutas.AdminModeracionComunidad.ruta) }
                )
            }

            item {
                AdminCardOption(
                    titulo = "Moderación de actividades",
                    descripcion = "Gestiona las actividades activas y su contenido.",
                    icono = Icons.Default.DateRange,
                    fondoIcono = FondoVerde,
                    colorIcono = TextoVerde,
                    onClick = { navController.navigate(Rutas.AdminModeracionActividades.ruta) }
                )
            }

            item {
                AdminCardOption(
                    titulo = "Actualización de donaciones",
                    descripcion = "Revisa el progreso de las campañas de donación.",
                    icono = Icons.Default.Favorite,
                    fondoIcono = FondoNaranja,
                    colorIcono = TextoNaranja,
                    onClick = { navController.navigate(Rutas.AdminActualizacionDonaciones.ruta) }
                )
            }
        }
    }
}

@Composable
fun AdminCardOption(
    titulo: String,
    descripcion: String,
    icono: ImageVector,
    fondoIcono: Color,
    colorIcono: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(fondoIcono, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorIcono,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = descripcion,
                    fontSize = 12.sp,
                    color = GrisTexto,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = GrisTexto
            )
        }
    }
}
