package com.example.familiasquesuman.ui.screens.actividad

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.data.ActividadMock
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.ui.components.BarraNavegacionInferior
import com.example.familiasquesuman.ui.components.MenuLateral
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarinoOscuro
import com.example.familiasquesuman.ui.theme.AzulOscuro
import com.example.familiasquesuman.ui.theme.GrisClaroFondo
import com.example.familiasquesuman.ui.theme.GrisTexto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuLateral(
                onInicioClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Rutas.Inicio.ruta)
                },
                onIniciarSesionClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Rutas.Login.ruta)
                },
                onCrearCuentaClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Rutas.Login.ruta)
                },
                onComunidadClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Rutas.Comunidad.ruta)
                }
            )
        }
    ) {
        Scaffold(
            containerColor = GrisClaroFondo,
            topBar = { TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                },
                title = { Text("Familias que Suman+") },
                actions = { /* igual que antes */ }
            )},
            bottomBar = {
                BarraNavegacionInferior(
                    pantallaActual = PantallaPrincipal.ACTIVIDADES,
                    onPantallaSeleccionada = { pantalla ->
                        val ruta = when (pantalla) {
                            PantallaPrincipal.ACTIVIDADES -> Rutas.Actividades.ruta
                            PantallaPrincipal.PROYECTOS -> Rutas.Proyectos.ruta
                            PantallaPrincipal.INICIO -> Rutas.Inicio.ruta
                            PantallaPrincipal.DONAR -> Rutas.Donar.ruta
                            PantallaPrincipal.DIRECTORIO -> Rutas.Directorio.ruta
                        }
                        navController.navigate(ruta) {
                            launchSingleTop = true
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Rutas.Chatbot.ruta) },
                    containerColor = AzulMarinoOscuro,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.ChatBubble, contentDescription = "Asistente Virtual")
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item { LeyendaFiltros() }
                item { Separador() }
                item { HeaderDiaActividades() }
                items(actividadesMockData) { actividad ->
                    TarjetaActividadDia(
                        actividad = actividad,
                        onActividadClick = { id ->
                            navController.navigate(Rutas.ActividadDetalle.crearRuta(id))
                        }
                    )
                }
                item { BannerSincronizarCalendario() }
            }
        }
    }
}

@Composable
fun LeyendaFiltros() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ItemLeyenda("Medio Ambiente", Color.Green)
        ItemLeyenda("Educación", Color.Blue)
        ItemLeyenda("Apoyo Social", Color.Yellow)
        ItemLeyenda("Otros", Color.Magenta)
    }
}

@Composable
fun ItemLeyenda(texto: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(4.dp))
        Text(texto, fontSize = 10.sp, color = GrisTexto)
    }
}

@Composable
fun Separador() {
    HorizontalDivider(color = Color(0xFFE8E8E8), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
fun HeaderDiaActividades() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "dia tal, fecha tal",
            fontSize = 18.sp, 
            fontWeight = FontWeight.Bold, 
            color = AzulMarinoOscuro
        )
        Surface(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE8E8E8)),
            color = Color.White
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Ver agenda semanal", fontSize = 12.sp, color = GrisTexto, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun TarjetaActividadDia(actividad: ActividadMock, onActividadClick: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onActividadClick(actividad.id) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(140.dp) // Altura ajustada al nuevo diseño horizontal
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Imagen Izquierda
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .background(Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.Default.Image, 
                    contentDescription = null, 
                    tint = Color.Gray, 
                    modifier = Modifier.align(Alignment.Center).size(32.dp)
                )
            }
            
            // Contenido Derecha
            Column(
                modifier = Modifier
                    .weight(0.65f)
                    .padding(12.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header (Tag y Bookmark)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = actividad.colorCategoria
                    ) {
                        Text(
                            text = actividad.categoria, 
                            fontSize = 10.sp, 
                            color = actividad.textColorCategoria,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Icon(Icons.Default.BookmarkBorder, contentDescription = "Guardar", tint = AzulMarinoOscuro, modifier = Modifier.size(20.dp))
                }
                
                // Info (Hora, Título, Lugar)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(12.dp), tint = GrisTexto)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(actividad.horario, fontSize = 11.sp, color = GrisTexto)
                    }
                    Text(
                        text = actividad.titulo, 
                        fontSize = 16.sp, 
                        fontWeight = FontWeight.Bold, 
                        color = AzulMarinoOscuro,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(12.dp), tint = GrisTexto)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(actividad.ubicacion, fontSize = 11.sp, color = GrisTexto)
                    }
                }
                
                // Footer (Lugares y Avatares)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "${actividad.lugaresDisponibles} lugares", 
                        fontSize = 10.sp, 
                        color = GrisTexto
                    )
                    
                    // Simulación de avatares superpuestos
                    Row {
                        Box(modifier = Modifier.size(20.dp).background(Color.Gray, CircleShape).clip(CircleShape).border(1.dp, Color.White, CircleShape))
                        Box(modifier = Modifier.size(20.dp).offset(x = (-8).dp).background(Color.DarkGray, CircleShape).clip(CircleShape).border(1.dp, Color.White, CircleShape))
                        Box(modifier = Modifier.size(20.dp).offset(x = (-16).dp).background(Color.LightGray, CircleShape).clip(CircleShape).border(1.dp, Color.White, CircleShape))
                        if(actividad.participantesAdicionales > 0) {
                             Box(
                                modifier = Modifier.size(20.dp).offset(x = (-24).dp).background(Ambar, CircleShape).clip(CircleShape).border(1.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                             ) {
                                Text("+${actividad.participantesAdicionales}", fontSize = 8.sp, color = AzulMarinoOscuro, fontWeight = FontWeight.Bold)
                             }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BannerSincronizarCalendario() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Ambar.copy(alpha = 0.3f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, RoundedCornerShape(12.dp)), 
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color(0xFFF9A825), modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("¿Quieres agregar la actividad a tu calendario?", fontWeight = FontWeight.Bold, color = AzulMarinoOscuro, fontSize = 12.sp, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Sincroniza las fechas que te interesan con tu calendario personal.", color = GrisTexto, fontSize = 10.sp, lineHeight = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFC107)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(14.dp), tint = AzulMarinoOscuro)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sincronizar", color = AzulMarinoOscuro, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}