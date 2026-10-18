package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.data.remote.dto.AsistenciaItemDto
import com.example.familiasquesuman.data.remote.dto.InscripcionAdminDto
import com.example.familiasquesuman.data.repository.ActividadRepository
import com.example.familiasquesuman.data.repository.InscripcionRepository
import com.example.familiasquesuman.ui.components.BarraBusqueda
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.theme.*
import kotlinx.coroutines.launch

enum class EstadoAsistencia {
    PENDIENTE,
    ASISTIO,
    FALTO
}

data class ParticipanteAsistenciaUi(
    val participanteId: Int,
    val inscripcionId: String,
    val nombre: String,
    val tipo: String,
    val familiaNombre: String,
    val inscritoPorEmail: String?,
    val estado: EstadoAsistencia = EstadoAsistencia.PENDIENTE
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAsistenciaActividadesScreen(
    navController: NavHostController,
    actividadId: String = "1",
    actividadRepo: ActividadRepository = remember { ActividadRepository() },
    inscripcionRepo: InscripcionRepository = remember { InscripcionRepository() }
) {
    val scope = rememberCoroutineScope()
    var tabSeleccionada by remember { mutableIntStateOf(0) } // 0 = Pase de Lista, 1 = Asistentes, 2 = Faltantes
    var textoBusqueda by remember { mutableStateOf("") }
    var mostrarExitoDialog by remember { mutableStateOf(false) }
    var inscripcionAEliminar by remember { mutableStateOf<InscripcionAdminDto?>(null) }

    var cargando by remember { mutableStateOf(true) }
    var guardando by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var actividad by remember { mutableStateOf<com.example.familiasquesuman.domain.Actividad?>(null) }

    val participantes = remember { mutableStateListOf<ParticipanteAsistenciaUi>() }
    var inscripcionesAdmin by remember { mutableStateOf<List<InscripcionAdminDto>>(emptyList()) }

    fun cargarDatos() {
        scope.launch {
            cargando = true
            errorMsg = null
            
            try {
                actividadRepo.obtenerActividadPorId(actividadId).onSuccess {
                    actividad = it
                }
            } catch (_: Exception) {}

            val resInsc = inscripcionRepo.listarInscripcionesAdmin(actividadId)
            resInsc.onSuccess { list ->
                inscripcionesAdmin = list
                participantes.clear()
                list.forEach { insc ->
                    val familia = insc.familia?.nombre ?: "Familia sin nombre"
                    val email = insc.inscritoPor?.email
                    insc.participantes.forEach { part ->
                        val est = when (part.asistencia?.uppercase()) {
                            "ASISTIO" -> EstadoAsistencia.ASISTIO
                            "FALTO" -> EstadoAsistencia.FALTO
                            else -> EstadoAsistencia.PENDIENTE
                        }
                        participantes.add(
                            ParticipanteAsistenciaUi(
                                participanteId = part.id,
                                inscripcionId = insc.id,
                                nombre = part.nombre ?: "",
                                tipo = if (part.tipo == "ADULTO") "Adulto" else "Menor",
                                familiaNombre = familia,
                                inscritoPorEmail = email,
                                estado = est
                            )
                        )
                    }
                }
                cargando = false
            }.onFailure { e ->
                errorMsg = e.message
                cargando = false
            }
        }
    }

    LaunchedEffect(actividadId) {
        cargarDatos()
    }

    val countPendientes = participantes.count { it.estado == EstadoAsistencia.PENDIENTE }
    val countAsistieron = participantes.count { it.estado == EstadoAsistencia.ASISTIO }
    val countFaltantes = participantes.count { it.estado == EstadoAsistencia.FALTO }

    val personasFiltradas = remember(tabSeleccionada, textoBusqueda, participantes.map { it.estado }) {
        participantes.filter { p ->
            val coincideTab = when (tabSeleccionada) {
                0 -> p.estado == EstadoAsistencia.PENDIENTE
                1 -> p.estado == EstadoAsistencia.ASISTIO
                else -> p.estado == EstadoAsistencia.FALTO
            }
            val coincideBusqueda = textoBusqueda.isBlank() ||
                    p.nombre.contains(textoBusqueda, ignoreCase = true) ||
                    p.familiaNombre.contains(textoBusqueda, ignoreCase = true)
            coincideTab && coincideBusqueda
        }
    }



    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.popBackStack() },
        titulo = "Control de asistencia"
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
                // Header de la Actividad
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Control de Asistencia",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulMarino
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Actividad: ${actividad?.titulo ?: "Cargando..."}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (actividad != null) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${actividad!!.fecha} • ${actividad!!.horario} • ${actividad!!.ubicacion}",
                                    fontSize = 12.sp,
                                    color = GrisTexto
                                )
                            }
                        }
                    }
                }

                // Selector de Pestañas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TabPill("Pase de Lista ($countPendientes)", tabSeleccionada == 0) { tabSeleccionada = 0 }
                    TabPill("Asistentes ($countAsistieron)", tabSeleccionada == 1) { tabSeleccionada = 1 }
                    TabPill("Faltantes ($countFaltantes)", tabSeleccionada == 2) { tabSeleccionada = 2 }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Buscador
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    BarraBusqueda(
                        texto = textoBusqueda,
                        onTextoChange = { textoBusqueda = it },
                        placeholder = "Buscar persona o familia..."
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Lista
                if (cargando) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Ambar)
                    }
                } else if (errorMsg != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = errorMsg ?: "", color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { cargarDatos() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                } else if (personasFiltradas.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (tabSeleccionada) {
                                0 -> "No hay personas pendientes en el pase de lista."
                                1 -> "Aún no se ha registrado ningún asistente."
                                else -> "No hay ausencias en la lista de faltantes."
                            },
                            color = GrisTexto,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = personasFiltradas,
                            key = { "${it.inscripcionId}_${it.participanteId}" }
                        ) { persona ->
                            TarjetaPaseListaPersona(
                                participante = persona,
                                tabActual = tabSeleccionada,
                                onMarcarAsistencia = { nuevoEstado ->
                                    val index = participantes.indexOfFirst { it.participanteId == persona.participanteId }
                                    if (index != -1) {
                                        participantes[index] = participantes[index].copy(estado = nuevoEstado)
                                    }
                                },
                                onDeleteInscripcion = {
                                    val insc = inscripcionesAdmin.find { it.id == persona.inscripcionId }
                                    if (insc != null) {
                                        inscripcionAEliminar = insc
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Botón flotante para guardar cambios
            if (!cargando && participantes.isNotEmpty()) {
                Button(
                    onClick = {
                        scope.launch {
                            guardando = true
                            val items = participantes.map {
                                AsistenciaItemDto(
                                    participanteId = it.participanteId,
                                    asistencia = it.estado.name
                                )
                            }
                            inscripcionRepo.guardarAsistenciaAdmin(actividadId, items)
                                .onSuccess { updatedList ->
                                    inscripcionesAdmin = updatedList
                                    guardando = false
                                    mostrarExitoDialog = true
                                }
                                .onFailure { e ->
                                    guardando = false
                                    errorMsg = e.message
                                }
                        }
                    },
                    enabled = !guardando,
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar, contentColor = AzulMarino),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(4.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (guardando) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = AzulMarino)
                    } else {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AzulMarino)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Guardar Registro de Asistencia",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }

    if (mostrarExitoDialog) {
        AlertDialog(
            onDismissRequest = { mostrarExitoDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = VerdeExito,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "¡Pase de Lista Guardado!",
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino
                )
            },
            text = {
                Text(
                    text = "Se han registrado los resultados para '${actividad?.titulo ?: ""}':\n\n• Asistieron: $countAsistieron participantes\n• No asistieron / Faltantes: $countFaltantes\n• Por confirmar: $countPendientes",
                    color = GrisTexto,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarExitoDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    inscripcionAEliminar?.let { insc ->
        AlertDialog(
            onDismissRequest = { inscripcionAEliminar = null },
            title = {
                Text("Eliminar inscripción", fontWeight = FontWeight.Bold, color = AzulMarino)
            },
            text = {
                Text("¿Estás seguro de que deseas eliminar la inscripción de '${insc.familia?.nombre ?: "la familia"}'? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        val toDelete = insc
                        inscripcionAEliminar = null
                        scope.launch {
                            inscripcionRepo.borrarInscripcionAdmin(actividadId, toDelete.id)
                                .onSuccess {
                                    cargarDatos()
                                }
                                .onFailure { e ->
                                    errorMsg = e.message
                                }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { inscripcionAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun TarjetaPaseListaPersona(
    participante: ParticipanteAsistenciaUi,
    tabActual: Int,
    onMarcarAsistencia: (EstadoAsistencia) -> Unit,
    onDeleteInscripcion: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(GrisClaroFondo),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = AzulMarino,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = participante.nombre,
                        fontWeight = FontWeight.Bold,
                        color = AzulMarino,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = GrisTexto,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${participante.familiaNombre} • ${participante.tipo}",
                            fontSize = 12.sp,
                            color = GrisTexto,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (!participante.inscritoPorEmail.isNullOrBlank()) {
                        Text(
                            text = "Inscrito por: ${participante.inscritoPorEmail}",
                            fontSize = 11.sp,
                            color = GrisTexto.copy(alpha = 0.8f)
                        )
                    }
                }

                IconButton(
                    onClick = onDeleteInscripcion,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar inscripción",
                        tint = Color.Gray.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Badge en pestañas 1 (Asistieron) y 2 (Faltantes)
                if (tabActual != 0) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (participante.estado == EstadoAsistencia.ASISTIO) FondoVerde else Color(0xFFFFEBEE)
                    ) {
                        Text(
                            text = if (participante.estado == EstadoAsistencia.ASISTIO) "Asistió" else "No asistió",
                            color = if (participante.estado == EstadoAsistencia.ASISTIO) TextoVerde else ColorError,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Acciones por Pestaña
            if (tabActual == 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onMarcarAsistencia(EstadoAsistencia.FALTO) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorError),
                        border = BorderStroke(1.dp, ColorError),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "No asistió",
                            tint = ColorError,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "No asistió",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = ColorError
                        )
                    }

                    Button(
                        onClick = { onMarcarAsistencia(EstadoAsistencia.ASISTIO) },
                        colors = ButtonDefaults.buttonColors(containerColor = FondoVerde, contentColor = TextoVerde),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Asistió",
                            tint = TextoVerde,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Asistió",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextoVerde
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onMarcarAsistencia(EstadoAsistencia.PENDIENTE) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "Cambiar",
                            tint = GrisTexto,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Deshacer / Regresar al Pase de Lista",
                            fontSize = 12.sp,
                            color = GrisTexto
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminAsistenciaActividadesScreenPreview() {
    FamiliasQueSumanTheme {
        AdminAsistenciaActividadesScreen(navController = rememberNavController())
    }
}
