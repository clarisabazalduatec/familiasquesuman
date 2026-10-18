package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.familiasquesuman.data.remote.dto.MiInscripcionDto
import com.example.familiasquesuman.data.remote.dto.toDomain
import com.example.familiasquesuman.data.repository.InscripcionRepository
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino
import java.util.Date

@Composable
fun MisActividadesPerfil(
    navController: NavHostController,
    repository: InscripcionRepository = remember { InscripcionRepository() }
) {
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var inscripciones by remember { mutableStateOf<List<MiInscripcionDto>>(emptyList()) }
    var tabSeleccionada by remember { mutableIntStateOf(0) } // 0 = Próximas, 1 = Pasadas

    LaunchedEffect(Unit) {
        cargando = true
        repository.misInscripciones()
            .onSuccess {
                inscripciones = it
                cargando = false
            }
            .onFailure {
                error = it.message
                cargando = false
            }
    }

    val ahora = remember { Date() }

    val (proximas, pasadas) = remember(inscripciones) {
        inscripciones.partition { miInscripcion ->
            val domain = miInscripcion.actividad?.toDomain()
            val fechaReferencia = domain?.terminaEn ?: domain?.iniciaEn
            fechaReferencia == null || fechaReferencia.after(ahora)
        }
    }

    val listaActual = if (tabSeleccionada == 0) proximas else pasadas

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Mis actividades",
            color = AzulMarino,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Revisa tus inscripciones comunitarias y las actividades de tu familia.",
            color = AzulMarino.copy(alpha = 0.65f),
            fontSize = 14.sp,
            lineHeight = 19.sp
        )

        // Pestañas Próximas / Pasadas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(Color.White, RoundedCornerShape(22.dp))
                .padding(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (tabSeleccionada == 0) Ambar else Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { tabSeleccionada = 0 },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Próximas (${proximas.size})",
                    color = if (tabSeleccionada == 0) Color.White else AzulMarino.copy(alpha = 0.7f),
                    fontWeight = if (tabSeleccionada == 0) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (tabSeleccionada == 1) Ambar else Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { tabSeleccionada = 1 },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pasadas (${pasadas.size})",
                    color = if (tabSeleccionada == 1) Color.White else AzulMarino.copy(alpha = 0.7f),
                    fontWeight = if (tabSeleccionada == 1) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }

        if (cargando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Ambar)
            }
        } else if (error != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = error ?: "Ocurrió un error",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }
            }
        } else if (listaActual.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        tint = AzulMarino.copy(alpha = 0.35f),
                        modifier = Modifier.size(42.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (tabSeleccionada == 0) "No tienes actividades próximas registradas." else "No tienes actividades pasadas.",
                        color = AzulMarino.copy(alpha = 0.60f),
                        fontSize = 15.sp
                    )
                }
            }
        } else {
            listaActual.forEach { item ->
                TarjetaMiInscripcion(
                    inscripcion = item,
                    onClick = {
                        val actId = item.actividad?.id ?: item.actividadId ?: ""
                        navController.navigate(Rutas.ActividadDetalle.crearRuta(actId))
                    }
                )
            }
        }
    }
}

@Composable
private fun TarjetaMiInscripcion(
    inscripcion: MiInscripcionDto,
    onClick: () -> Unit
) {
    val actividad = remember(inscripcion.actividad) { inscripcion.actividad?.toDomain() }
    val participantesNombres = remember(inscripcion.participantes) {
        inscripcion.participantes.orEmpty().mapNotNull { it.nombre }.joinToString(", ").ifBlank { "Sin participantes" }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 90.dp, height = 90.dp)
                        .background(color = Color(0xFFECEDEF), shape = RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    val imagen = actividad?.imagenes?.firstOrNull()
                    if (imagen != null) {
                        AsyncImage(
                            model = imagen,
                            contentDescription = actividad?.titulo,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Event,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = actividad?.titulo ?: "Actividad",
                        color = AzulMarino,
                        fontSize = 15.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = Ambar.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = actividad?.categoria ?: "General",
                            color = AzulMarino,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Event,
                            contentDescription = null,
                            tint = AzulMarino.copy(alpha = 0.65f),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = actividad?.fecha ?: "",
                            color = AzulMarino.copy(alpha = 0.65f),
                            fontSize = 12.sp
                        )
                    }
                    if (!actividad?.horario.isNullOrBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = null,
                                tint = AzulMarino.copy(alpha = 0.65f),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = actividad?.horario ?: "",
                                color = AzulMarino.copy(alpha = 0.65f),
                                fontSize = 12.sp
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = null,
                            tint = AzulMarino.copy(alpha = 0.65f),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = actividad?.ubicacion ?: "",
                            color = AzulMarino.copy(alpha = 0.65f),
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            HorizontalDivider(color = AzulMarino.copy(alpha = 0.08f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Groups,
                    contentDescription = null,
                    tint = Ambar,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Inscritos: $participantesNombres",
                    color = AzulMarino,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
