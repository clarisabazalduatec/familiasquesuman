package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.familiasquesuman.data.remote.dto.MiInscripcionDto
import com.example.familiasquesuman.data.remote.dto.toDomain
import com.example.familiasquesuman.data.repository.InscripcionRepository
import com.example.familiasquesuman.ui.components.EncuestaActividadDialog
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino
import kotlinx.coroutines.launch
import java.util.Date
import com.example.familiasquesuman.ui.components.EstadoErrorServidor
import com.example.familiasquesuman.ui.components.EstadoVacio

class MisActividadesViewModel(
    private val repository: InscripcionRepository = InscripcionRepository(),
) : ViewModel() {
    var cargando by mutableStateOf(value = true)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var inscripciones by mutableStateOf<List<MiInscripcionDto>>(emptyList())
        private set

    // Guardar IDs de actividades cuyas encuestas ya fueron respondidas en esta sesión
    var encuestasRespondidas by mutableStateOf<Set<String>>(emptySet())
        private set

    fun marcarEncuestaRespondida(actividadId: String) {
        encuestasRespondidas = encuestasRespondidas + actividadId
    }

    fun cargar() {
        viewModelScope.launch {
            cargando = true
            error = null
            repository.misInscripciones(incluirCanceladas = true)
                .onSuccess {
                    inscripciones = it
                    cargando = false
                }
                .onFailure {
                    error = it.message
                    cargando = false
                }
        }
    }
}

private fun MiInscripcionDto.yaPaso(ahora: Date): Boolean {
    val domain = actividad?.toDomain()
    val fechaRef = domain?.terminaEn ?: domain?.iniciaEn
    return (fechaRef != null) && fechaRef.before(ahora)
}

private fun MiInscripcionDto.esCancelada(): Boolean {
    return estado?.equals("CANCELADA", ignoreCase = true) == true
}

@Composable
fun MisActividadesPerfil(
    navController: NavHostController,
    viewModel: MisActividadesViewModel = viewModel(),
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.cargar()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var tabSeleccionada by remember { mutableIntStateOf(0) } // 0 = Próximas, 1 = Historial
    var mostrarPopUpConcluida by remember { mutableStateOf(false) }

    // Estado para controlar qué encuesta se está respondiendo
    var actividadParaEncuestaId by remember { mutableStateOf<String?>(null) }
    val perfilUiState by perfilViewModel.uiState.collectAsState()

    val ahora = remember { Date() }

    val (proximas, historial) = remember(viewModel.inscripciones, ahora) {
        val proxi = viewModel.inscripciones
            .filter { !it.esCancelada() && !it.yaPaso(ahora) }
            .sortedBy { it.actividad?.toDomain()?.iniciaEn?.time ?: Long.MAX_VALUE }

        val histo = viewModel.inscripciones
            .filter { it.esCancelada() || it.yaPaso(ahora) }
            .sortedByDescending { it.actividad?.toDomain()?.iniciaEn?.time ?: 0L }

        Pair(proxi, histo)
    }

    val actividadesConAsistencia = remember(viewModel.inscripciones, ahora) {
        viewModel.inscripciones.count {
            it.yaPaso(ahora) && !it.esCancelada() && it.participantes.any { p -> p.asistencia == "ASISTIO" }
        }
    }

    val personasAsistenciasTotal = remember(viewModel.inscripciones) {
        viewModel.inscripciones
            .filter { !it.esCancelada() }
            .sumOf { item -> item.participantes.count { p -> p.asistencia == "ASISTIO" } }
    }

    val listaActual = if (tabSeleccionada == 0) proximas else historial

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

        // Pestañas Próximas / Historial
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
                    text = if (viewModel.cargando || viewModel.error != null)
                        "Próximas"
                    else
                        "Próximas (${proximas.size})",
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
                    text = if (viewModel.cargando || viewModel.error != null)
                        "Historial"
                    else
                        "Historial (${historial.size})",
                    color = if (tabSeleccionada == 1) Color.White else AzulMarino.copy(alpha = 0.7f),
                    fontWeight = if (tabSeleccionada == 1) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }

        if (viewModel.cargando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Ambar)
            }
        } else if (viewModel.error != null) {
            EstadoErrorServidor(
                onReintentar = {
                    viewModel.cargar()
                }
            )
        } else {
            if (tabSeleccionada == 1) {
                ResumenHistorialCard(
                    actividadesConAsistencia = actividadesConAsistencia,
                    personasAsistenciasTotal = personasAsistenciasTotal
                )
            }

            if (listaActual.isEmpty()) {
                EstadoVacio(
                    icono = if (tabSeleccionada == 0)
                        Icons.Outlined.Event
                    else
                        Icons.Outlined.CheckCircle,
                    titulo = if (tabSeleccionada == 0)
                        "Aún no tienes actividades próximas"
                    else
                        "Tu historial está vacío",
                    mensaje = if (tabSeleccionada == 0)
                        "Cuando te inscribas en una actividad, aparecerá aquí."
                    else
                        "Las actividades que hayas concluido o cancelado aparecerán aquí."
                )
            } else {
                listaActual.forEach { item ->
                    val actId = item.actividad?.id ?: item.actividadId ?: ""

                    // Aquí se evalúa si ya respondió (desde Backend o en la Sesión actual)
                    val yaRespondio = item.encuestaRespondida || viewModel.encuestasRespondidas.contains(actId)

                    TarjetaMiInscripcion(
                        inscripcion = item,
                        ahora = ahora,
                        encuestaRespondida = yaRespondio,
                        onClick = {
                            if (!item.esCancelada() && !item.yaPaso(ahora)) {
                                navController.navigate(Rutas.ActividadDetalle.crearRuta(actId))
                            } else {
                                mostrarPopUpConcluida = true
                            }
                        },
                        onResponderEncuestaClick = {
                            if (!yaRespondio) {
                                actividadParaEncuestaId = actId
                            }
                        }
                    )
                }
            }
        }
    }

    // Diálogo para responder la encuesta
    actividadParaEncuestaId?.let { actId ->
        val inscripcionSel = viewModel.inscripciones.find {
            (it.actividad?.id ?: it.actividadId) == actId
        }
        val titulo = inscripcionSel?.actividad?.titulo ?: "Actividad comunitaria"

        EncuestaActividadDialog(
            actividadId = actId,
            tituloActividad = titulo,
            isLoading = perfilUiState.guardando,
            onDismissRequest = { actividadParaEncuestaId = null },
            onSubmit = { request ->
                perfilViewModel.enviarEncuesta(actId, request)
                viewModel.marcarEncuestaRespondida(actId)
                actividadParaEncuestaId = null
            }
        )
    }

    if (mostrarPopUpConcluida) {
        AlertDialog(
            onDismissRequest = { mostrarPopUpConcluida = false },
            containerColor = Color.White,
            title = {
                Text(
                    text = "Actividad concluida",
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Esta actividad ya concluyó",
                    color = AzulMarino,
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { mostrarPopUpConcluida = false }) {
                    Text("Aceptar", color = Ambar, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun ResumenHistorialCard(
    actividadesConAsistencia: Int,
    personasAsistenciasTotal: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = actividadesConAsistencia.toString(),
                    color = AzulMarino,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Actividades participadas",
                    color = AzulMarino.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
            VerticalDivider(
                modifier = Modifier.height(36.dp),
                color = AzulMarino.copy(alpha = 0.12f)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = personasAsistenciasTotal.toString(),
                    color = Ambar,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Personas-asistencia",
                    color = AzulMarino.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun TarjetaMiInscripcion(
    inscripcion: MiInscripcionDto,
    ahora: Date,
    encuestaRespondida: Boolean = false,
    onClick: () -> Unit,
    onResponderEncuestaClick: () -> Unit
) {
    val actividad = remember(inscripcion.actividad) { inscripcion.actividad?.toDomain() }
    val esCancelada = remember(inscripcion.estado) { inscripcion.esCancelada() }
    val yaPaso = remember(inscripcion.actividad, ahora) { inscripcion.yaPaso(ahora) }

    val participantesNombres = remember(inscripcion.participantes) {
        inscripcion.participantes.mapNotNull { it.nombre }.joinToString(", ").ifBlank { "Sin participantes" }
    }

    val alphaCard = if (esCancelada) 0.65f else 1.0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { alpha = alphaCard }
            .clickable(onClick = onClick),
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
                            contentDescription = actividad.titulo,
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                        if (esCancelada) {
                            Surface(
                                color = Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Cancelada",
                                    color = Color(0xFFC62828),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        } else if (inscripcion.estado?.equals("EN_ESPERA", ignoreCase = true) == true) {
                            Surface(
                                color = Ambar.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                val pos = inscripcion.posicionEspera
                                val textoPos = if (pos != null) "En lista de espera · posición $pos" else "En lista de espera"
                                Text(
                                    text = textoPos,
                                    color = AzulMarino,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
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
                                text = actividad.horario,
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

            if (yaPaso) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Groups,
                            contentDescription = null,
                            tint = Ambar,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Asistencia:",
                            color = AzulMarino,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    val participantes = inscripcion.participantes
                    if (participantes.isEmpty()) {
                        Text(
                            text = "Sin participantes",
                            color = AzulMarino.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 22.dp)
                        )
                    } else {
                        participantes.forEach { p ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 22.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = p.nombre ?: "Participante",
                                    color = AzulMarino,
                                    fontSize = 13.sp
                                )
                                val (textoEstado, colorEstado) = when (p.asistencia) {
                                    "ASISTIO" -> "Asistió" to Color(0xFF2E7D32)
                                    "FALTO" -> "No asistió" to Color(0xFFC62828)
                                    else -> "Sin registrar" to AzulMarino.copy(alpha = 0.6f)
                                }
                                Text(
                                    text = textoEstado,
                                    color = colorEstado,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // BOTÓN DINÁMICO PARA ENCUESTA
                    if (!esCancelada) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = onResponderEncuestaClick,
                            enabled = !encuestaRespondida,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Ambar,
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFFE2E8F0),
                                disabledContentColor = AzulMarino.copy(alpha = 0.4f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (encuestaRespondida) Icons.Outlined.CheckCircle else Icons.Outlined.RateReview,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (encuestaRespondida) "Encuesta respondida" else "Responder encuesta",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
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
}