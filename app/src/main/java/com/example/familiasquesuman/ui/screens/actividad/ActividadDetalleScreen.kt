package com.example.familiasquesuman.ui.screens.actividad

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.data.ActividadMock
import com.example.familiasquesuman.data.actividadesMockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadDetalleScreen(navController: NavHostController, actividadId: Int) {
    // Buscar la actividad, si no existe mostramos la primera por defecto para evitar crashes
    val actividad = actividadesMockData.find { it.id == actividadId } ?: actividadesMockData.first()

    Scaffold(
        containerColor = LightBackground,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = DarkBlue)
                    }
                },
                title = {
                    Text(
                        text = "Familias que Suman+",
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        BadgedBox(badge = { Badge { Text("1") } }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = DarkBlue)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBackground
                )
            )
        },
        bottomBar = {
            Surface(
                color = LightBackground,
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 16.dp
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF9A825)), // Amarillo oscuro
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp)
                ) {
                    Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = DarkBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Quiero participar", color = DarkBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                        .background(Color.LightGray)
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
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
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
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir", tint = DarkBlue, modifier = Modifier.size(20.dp))
                    }
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorito", tint = DarkBlue, modifier = Modifier.size(20.dp))
                    }
                }

                // Título y Tag de Categoría sobre la imagen
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 32.dp) // padding para evitar chocar con la superficie blanca
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = actividad.colorCategoria
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(actividad.iconoCategoria, contentDescription = null, modifier = Modifier.size(14.dp), tint = actividad.textColorCategoria)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = actividad.categoria,
                                fontSize = 12.sp,
                                color = actividad.textColorCategoria,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
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
                    color = LightBackground,
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
                    .background(LightBackground)
                    .padding(horizontal = 16.dp)
            ) {
                // Tarjetas de Ubicación y Fecha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tarjeta de Ubicación
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF6F4ED), // Beige muy claro
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = DarkBlue, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ubicación", fontWeight = FontWeight.Bold, color = DarkBlue, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(actividad.ubicacion, fontSize = 12.sp, color = TextGray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(actividad.distancia, fontSize = 10.sp, color = Color(0xFF1976D2), fontWeight = FontWeight.Medium) // Azul
                        }
                    }
                    
                    // Tarjeta de Fecha y Hora
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF6F4ED), // Beige muy claro
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DateRange, contentDescription = null, tint = DarkBlue, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Fecha y Hora", fontWeight = FontWeight.Bold, color = DarkBlue, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(actividad.fecha, fontSize = 12.sp, color = TextGray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(actividad.horario, fontSize = 10.sp, color = Color(0xFF1976D2), fontWeight = FontWeight.Medium) // Azul
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Organizador
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Logo organizador
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFE5F0E6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Eco, contentDescription = null, tint = Color(0xFF2E7D32))
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(actividad.organizador, fontWeight = FontWeight.Bold, color = DarkBlue, fontSize = 14.sp)
                                if (actividad.organizadorVerificado) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Verified, contentDescription = "Verificado", tint = Color(0xFF1976D2), modifier = Modifier.size(14.dp))
                                }
                            }
                            Text("Organizador verificado", color = TextGray, fontSize = 12.sp)
                        }
                        
                        Icon(Icons.Default.Info, contentDescription = "Info organizador", tint = DarkBlue)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Acerca del proyecto
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(actividad.iconoCategoria, contentDescription = null, tint = actividad.textColorCategoria, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Acerca del proyecto", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = actividad.acercaDe,
                    fontSize = 14.sp,
                    color = TextGray,
                    lineHeight = 22.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Plazas disponibles
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Plazas disponibles", fontWeight = FontWeight.Bold, color = DarkBlue, fontSize = 14.sp)
                            Text("${actividad.lugaresDisponibles} / ${actividad.lugaresTotales}", fontWeight = FontWeight.Bold, color = DarkBlue, fontSize = 14.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Barra de progreso
                        val progreso = actividad.lugaresDisponibles.toFloat() / actividad.lugaresTotales.toFloat()
                        LinearProgressIndicator(
                            progress = { progreso },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(0xFFF9A825), // Amarillo oscuro
                            trackColor = Color(0xFFE8E8E8)
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.GroupAdd, contentDescription = null, tint = DarkBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("¡Súmate, quedan pocas plazas!", color = TextGray, fontSize = 12.sp)
                        }
                    }
                }
                
                // Espacio extra al final para que el botón no tape contenido
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
