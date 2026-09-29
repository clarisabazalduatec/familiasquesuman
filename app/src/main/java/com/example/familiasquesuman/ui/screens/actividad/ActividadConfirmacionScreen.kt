package com.example.familiasquesuman.ui.screens.actividad

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadConfirmacionScreen(navController: NavHostController, actividadId: Int) {
    val actividad = actividadesMockData.find { it.id == actividadId } ?: actividadesMockData.first()

    val regresarAlDetalle = {
        val rutaDetalle = Rutas.ActividadDetalle.crearRuta(actividadId)
        val popped = navController.popBackStack(route = rutaDetalle, inclusive = false)
        if (!popped) {
            navController.navigate(rutaDetalle) {
                popUpTo(Rutas.Actividades.ruta)
            }
        }
    }

    // Intercepta el botón/gesto físico "Atrás" del sistema
    BackHandler {
        regresarAlDetalle()
    }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Placeholder para la ilustración de confirmación
            Box(
                modifier = Modifier
                    .size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    tint = AzulOscuro.copy(alpha = 0.5f),
                    modifier = Modifier.size(100.dp)
                )

                // Círculo de check encima
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 16.dp)
                        .size(48.dp)
                        .background(AmarilloOscuro, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¡Participación confirmada!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = AzulOscuro,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu registro quedó listo. Aquí tienes un resumen de la actividad y de las personas que asistirán.",
                fontSize = 14.sp,
                color = AzulOscuro,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta de Resumen
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Actividad
                    Row {
                        Icon(Icons.Default.Event, contentDescription = null, tint = AzulOscuro)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Actividad", color = TextoGrisActividad, fontSize = 12.sp)
                            Text(actividad.titulo, color = AzulOscuro, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrisBordeClaro)

                    // Fecha
                    Row {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = AzulOscuro)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Fecha", color = TextoGrisActividad, fontSize = 12.sp)
                            Text(actividad.fecha, color = AzulOscuro, fontSize = 16.sp)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrisBordeClaro)

                    // Hora
                    Row {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = AzulOscuro)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Hora", color = TextoGrisActividad, fontSize = 12.sp)
                            Text(actividad.horario, color = AzulOscuro, fontSize = 16.sp)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrisBordeClaro)

                    // Lugar
                    Row {
                        Icon(Icons.Default.Place, contentDescription = null, tint = AzulOscuro)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Lugar", color = TextoGrisActividad, fontSize = 12.sp)
                            Text(actividad.ubicacion, color = AzulOscuro, fontSize = 16.sp)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrisBordeClaro)

                    // Participantes
                    Row {
                        Icon(Icons.Default.Groups, contentDescription = null, tint = AzulOscuro)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Participantes", color = TextoGrisActividad, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            // Avatar 1
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(24.dp).background(Color.LightGray, CircleShape), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Tú", color = AzulOscuro, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen de asistentes
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AmarilloPrincipal.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Groups, contentDescription = null, tint = AmarilloOscuro)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("1 asistente confirmado", color = AzulOscuro, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botones Finales
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = AmarilloOscuro),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = AzulOscuro)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar al calendario", color = AzulOscuro, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { regresarAlDetalle() },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulOscuro),
                    border = BorderStroke(1.dp, AmarilloOscuro),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Ver detalles de la actividad", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActividadConfirmacionScreenPreview() {
    FamiliasQueSumanTheme {
        ActividadConfirmacionScreen(
            navController = rememberNavController(),
            actividadId = 1
        )
    }
}
