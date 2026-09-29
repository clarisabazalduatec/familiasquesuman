package com.example.familiasquesuman.ui.screens.actividad

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.actividad.components.CategoriaChip
import com.example.familiasquesuman.ui.screens.actividad.components.TarjetaInfoActividad
import com.example.familiasquesuman.ui.screens.actividad.components.TarjetaOrganizador
import com.example.familiasquesuman.ui.screens.actividad.components.TarjetaPlazasDisponibles
import com.example.familiasquesuman.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadDetalleScreen(navController: NavHostController, actividadId: Int) {
    // Buscar la actividad, si no existe mostramos la primera por defecto para evitar crashes
    val actividad = actividadesMockData.find { it.id == actividadId } ?: actividadesMockData.first()

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.ACTIVIDADES,
        acciones = {
            IconButton(onClick = { navController.navigate(Rutas.Notificaciones.ruta) }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
            }
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoClaro)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Imagen Hero con superposición
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    // Placeholder para la imagen (Gris claro)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(GrisClaroFondo)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(64.dp)
                        )
                    }

                    // Gradiente oscuro en la parte inferior de la imagen
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    ),
                                    startY = 300f
                                )
                            )
                    )

                    // Botones flotantes (Compartir y Favorito)
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { /* Compartir */ },
                            modifier = Modifier
                                .background(Color.White, CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir",
                                tint = AzulOscuro,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = { /* Favorito */ },
                            modifier = Modifier
                                .background(Color.White, CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = AzulOscuro,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Título y Tag de Categoría sobre la imagen
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(
                                horizontal = 16.dp,
                                vertical = 32.dp
                            )
                    ) {
                        CategoriaChip(
                            categoria = actividad.categoria,
                            icono = actividad.iconoCategoria,
                            backgroundColor = actividad.colorCategoria,
                            textColor = actividad.textColorCategoria,
                            mostrarIcono = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = actividad.titulo,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 32.sp
                        )
                    }

                    // Efecto de esquinas redondeadas en la parte inferior para conectar con el contenido
                    Surface(
                        color = FondoClaro,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .align(Alignment.BottomCenter)
                    ) {}
                }

                // Contenido de la actividad
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FondoClaro)
                        .padding(horizontal = 16.dp)
                ) {
                    // Tarjetas de Ubicación y Fecha
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TarjetaInfoActividad(
                            titulo = "Ubicación",
                            lineaPrincipal = actividad.ubicacion,
                            lineaSecundaria = actividad.distancia,
                            icono = Icons.Default.Place,
                            modifier = Modifier.weight(1f)
                        )
                        TarjetaInfoActividad(
                            titulo = "Fecha y Hora",
                            lineaPrincipal = actividad.fecha,
                            lineaSecundaria = actividad.horario,
                            icono = Icons.Default.DateRange,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Organizador
                    TarjetaOrganizador(
                        nombreOrganizador = actividad.organizador,
                        esVerificado = actividad.organizadorVerificado
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Acerca del proyecto
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = actividad.iconoCategoria,
                            contentDescription = null,
                            tint = actividad.textColorCategoria,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Acerca del proyecto",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulOscuro
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = actividad.acercaDe,
                        fontSize = 14.sp,
                        color = TextoGrisActividad,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Plazas disponibles
                    TarjetaPlazasDisponibles(
                        disponibles = actividad.lugaresDisponibles,
                        totales = actividad.lugaresTotales
                    )

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            val isScrolling by remember {
                derivedStateOf { scrollState.isScrollInProgress }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                AnimatedVisibility(
                    visible = !isScrolling,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    Surface(
                        color = FondoClaro,
                        modifier = Modifier.fillMaxWidth(),
                        shadowElevation = 16.dp
                    ) {
                        Button(
                            onClick = {
                                navController.navigate(
                                    Rutas.ActividadParticipar.crearRuta(
                                        actividadId
                                    )
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Ambar),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(56.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolunteerActivism,
                                contentDescription = null,
                                tint = AzulOscuro
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Quiero participar",
                                color = AzulOscuro,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
