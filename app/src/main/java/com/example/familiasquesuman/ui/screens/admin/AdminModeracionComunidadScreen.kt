package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.theme.*

@Composable
fun AdminModeracionComunidadScreen(navController: NavHostController) {
    var pestañaSeleccionada by remember { mutableStateOf(0) }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.popBackStack() },
        titulo = "Gestión de comunidad"
    ) { paddingVal ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVal)
                .background(CremaFondo)
        ) {
            // Pestañas (Pendientes, Aprobados, Rechazados)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabPill("Pendientes (5)", pestañaSeleccionada == 0) { pestañaSeleccionada = 0 }
                TabPill("Aprobados", pestañaSeleccionada == 1) { pestañaSeleccionada = 1 }
                TabPill("Rechazados", pestañaSeleccionada == 2) { pestañaSeleccionada = 2 }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TarjetaModeracionComunidad(
                        nombre = "María González",
                        tiempo = "Hace 2 horas",
                        estado = "Pendiente",
                        contenido = "¡Qué gran experiencia en la actividad de reforestación! 🌱 Gracias a todos los que hicieron esto posible. 🌳",
                        likes = 24,
                        comentarios = 3,
                        onRechazar = {},
                        onAprobar = {}
                    )
                }
                item {
                    TarjetaModeracionComunidad(
                        nombre = "Luis Hernández",
                        tiempo = "Hace 5 horas",
                        estado = "Pendiente",
                        contenido = "Gracias por la donación de alimentos, hoy muchas familias pudieron disfrutar una comida caliente. 💙",
                        likes = 32,
                        comentarios = 5,
                        onRechazar = {},
                        onAprobar = {}
                    )
                }
            }
        }
    }
}

@Composable
fun TabPill(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (seleccionado) AzulMarino else Color.White,
        border = if (!seleccionado) BorderStroke(1.dp, GrisBorde) else null
    ) {
        Text(
            text = texto,
            color = if (seleccionado) Color.White else AzulMarino,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun TarjetaModeracionComunidad(
    nombre: String,
    tiempo: String,
    estado: String,
    contenido: String,
    likes: Int,
    comentarios: Int,
    onRechazar: () -> Unit,
    onAprobar: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(GrisClaroFondo, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = GrisTexto)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = nombre, fontWeight = FontWeight.Bold, color = AzulMarino, fontSize = 14.sp)
                        Text(text = tiempo, color = GrisTexto, fontSize = 11.sp)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AmbarClaro
                ) {
                    Text(
                        text = estado,
                        color = Ambar,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = contenido,
                color = AzulMarino,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Imágenes de la publicación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .background(GrisClaroFondo, RoundedCornerShape(12.dp))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp)
                        .background(GrisClaroFondo, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+2", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = GrisBordeClaro)

            Spacer(modifier = Modifier.height(12.dp))

            // Footer (Likes, Comentarios y Botones de acción)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$likes", color = GrisTexto, fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$comentarios", color = GrisTexto, fontSize = 12.sp)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onRechazar,
                        border = BorderStroke(1.dp, ColorError),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorError),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Rechazar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = personalidad@{ onAprobar() },
                        colors = ButtonDefaults.buttonColors(containerColor = AzulMarino),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Aprobar", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun AdminModeracionComunidadScreenPreview() {
    FamiliasQueSumanTheme {
        AdminModeracionComunidadScreen(
            navController = rememberNavController()
        )
    }
}