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
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.ui.components.BarraBusqueda
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.theme.*

enum class EstadoAsistencia {
    PENDIENTE,
    ASISTIO,
    NO_ASISTIO
}

data class ParticipanteAsistencia(
    val id: String,
    val nombre: String,
    val tipoUsuario: String,
    val telefono: String? = null,
    val estado: EstadoAsistencia = EstadoAsistencia.PENDIENTE
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAsistenciaActividadesScreen(
    navController: NavHostController,
    actividadId: Int = 1
) {
    var tabSeleccionada by remember { mutableIntStateOf(0) } // 0 = Pase de Lista, 1 = Asistentes, 2 = Faltantes
    var textoBusqueda by remember { mutableStateOf("") }
    var mostrarExitoDialog by remember { mutableStateOf(value = false) }

    val actividadActual = remember(actividadId) {
        actividadesMockData.find { it.id == actividadId } ?: actividadesMockData.first()
    }

    // Lista mutable de personas/familias registradas
    val participantes = remember(actividadId) {
        mutableStateListOf(
            ParticipanteAsistencia("1", "Familia González Ramírez", "Familia (4 integrantes)", "8112345678", EstadoAsistencia.PENDIENTE),
            ParticipanteAsistencia("2", "Familia Hernández López", "Familia (3 integrantes)", "8119876543", EstadoAsistencia.PENDIENTE),
            ParticipanteAsistencia("3", "Mariana Pérez Treviño", "Voluntaria Individual", "8115554433", EstadoAsistencia.PENDIENTE),
            ParticipanteAsistencia("4", "Carlos Rodríguez Garza", "Voluntario Individual", "8116667788", EstadoAsistencia.PENDIENTE),
            ParticipanteAsistencia("5", "Familia Martínez Cavazos", "Familia (5 integrantes)", "8113332211", EstadoAsistencia.PENDIENTE),
            ParticipanteAsistencia("6", "Familia Morales Cantú", "Familia (2 integrantes)", "8118889900", EstadoAsistencia.PENDIENTE),
            ParticipanteAsistencia("7", "Sofia Villarreal Fernández", "Voluntaria Individual", "8114445566", EstadoAsistencia.PENDIENTE),
            ParticipanteAsistencia("8", "Familia Garza Benítez", "Familia (3 integrantes)", "8117778899", EstadoAsistencia.PENDIENTE)
        )
    }

    val countPendientes = participantes.count { it.estado == EstadoAsistencia.PENDIENTE }
    val countAsistieron = participantes.count { it.estado == EstadoAsistencia.ASISTIO }
    val countNoAsistieron = participantes.count { it.estado == EstadoAsistencia.NO_ASISTIO }

    val personasFiltradas = remember(tabSeleccionada, textoBusqueda, participantes.map { it.estado }) {
        participantes.filter { p ->
            val coincideTab = when (tabSeleccionada) {
                0 -> p.estado == EstadoAsistencia.PENDIENTE
                1 -> p.estado == EstadoAsistencia.ASISTIO
                else -> p.estado == EstadoAsistencia.NO_ASISTIO
            }
            val coincideBusqueda = textoBusqueda.isBlank() || p.nombre.contains(textoBusqueda, ignoreCase = true)
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
                // Header de la Actividad Seleccionada

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
                                .padding( vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ){
                        Text(
                            text = "Actividad: ${actividadActual.titulo}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${actividadActual.fecha} • ${actividadActual.horario} • ${actividadActual.ubicacion}",
                            fontSize = 12.sp,
                            color = GrisTexto
                        )
                    }


               // Spacer(modifier = Modifier.height(12.dp))

                // Selector de Pestañas (Pase de Lista / Asistentes / Faltantes)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TabPill("Pase de Lista ($countPendientes)", tabSeleccionada == 0) { tabSeleccionada = 0 }
                    TabPill("Asistentes ($countAsistieron)", tabSeleccionada == 1) { tabSeleccionada = 1 }
                    TabPill("Faltantes ($countNoAsistieron)", tabSeleccionada == 2) { tabSeleccionada = 2 }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Buscador de Persona / Familia
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    BarraBusqueda(
                        texto = textoBusqueda,
                        onTextoChange = { textoBusqueda = it },
                        placeholder = "Buscar en la lista..."
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Lista de Personas por Pestaña
                if (personasFiltradas.isEmpty()) {
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
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = personasFiltradas,
                            key = { it.id }
                        ) { persona ->
                            TarjetaPaseListaPersona(
                                participante = persona,
                                tabActual = tabSeleccionada,
                                onMarcarAsistencia = { nuevoEstado ->
                                    val index = participantes.indexOfFirst { it.id == persona.id }
                                    if (index != -1) {
                                        participantes[index] = participantes[index].copy(estado = nuevoEstado)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Botón flotante para guardar cambios
            if (participantes.any { it.estado != EstadoAsistencia.PENDIENTE }) {
                Button(
                    onClick = { mostrarExitoDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar, contentColor = AzulMarino),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(4.dp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
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
                    text = "Se han registrado los resultados para '${actividadActual.titulo}':\n\n• Asistieron: $countAsistieron personas/familias\n• No asistieron / Faltantes: $countNoAsistieron\n• Por confirmar: $countPendientes",
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
}

@Composable
fun TarjetaPaseListaPersona(
    participante: ParticipanteAsistencia,
    tabActual: Int,
    onMarcarAsistencia: (EstadoAsistencia) -> Unit
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
                            text = participante.tipoUsuario,
                            fontSize = 12.sp,
                            color = GrisTexto
                        )
                    }
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
                // Tab 0 (Pase de Lista): Dos botones claros [ No asistió ]  [ Asistió ]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onMarcarAsistencia(EstadoAsistencia.NO_ASISTIO) },
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
                // Tabs 1 (Asistieron) y 2 (Faltantes): Botón para Deshacer / Reclasificar persona
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
