package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.familiasquesuman.data.repository.ComunidadRepository
import com.example.familiasquesuman.data.remote.dto.PublicacionDto
import com.example.familiasquesuman.ui.screens.comunidad.PublicacionComunidadUi
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.familiasquesuman.data.remote.dto.toDomain
import androidx.compose.foundation.clickable

@Composable
fun AdminModeracionComunidadScreen(navController: NavHostController) {
    var pestañaSeleccionada by remember { mutableStateOf(0) }
    val repository = remember { ComunidadRepository() }
    val scope = rememberCoroutineScope()

    var pendientes by remember { mutableStateOf<List<PublicacionComunidadUi>>(emptyList()) }
    var aprobadas by remember { mutableStateOf<List<PublicacionDto>>(emptyList()) }
    var rechazadas by remember { mutableStateOf<List<PublicacionDto>>(emptyList()) }

    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var actualizacion by remember { mutableIntStateOf(0) }
    var idParaAprobar by remember { mutableStateOf<String?>(null) }
    var idParaRechazar by remember { mutableStateOf<String?>(null) }
    var motivoRechazo by remember { mutableStateOf("") }
    var idProcesando by remember { mutableStateOf<String?>(null) }
    var errorAccion by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(pestañaSeleccionada, actualizacion) {
        cargando = true
        error = null

        when (pestañaSeleccionada) {
            0 -> repository.obtenerPublicacionesPendientes()
                .onSuccess { pendientes = it }
                .onFailure { error = "No se pudieron cargar las publicaciones pendientes." }

            1 -> repository.obtenerPublicacionesAprobadas()
                .onSuccess { aprobadas = it }
                .onFailure { error = "No se pudieron cargar las publicaciones aprobadas." }

            2 -> repository.obtenerPublicacionesRechazadas()
                .onSuccess { rechazadas = it }
                .onFailure { error = "No se pudieron cargar las publicaciones rechazadas." }
        }
        cargando = false
        }

        idParaAprobar?.let { id ->
            AlertDialog(
                onDismissRequest = {
                    if (idProcesando == null) idParaAprobar = null
                },
                title = {
                    Text("Aprobar publicación")
                },
                text = {
                    Text("¿Estás seguro de que quieres aprobar esta publicación? Será visible en Comunidad.")
                },
                confirmButton = {
                    Button(
                        enabled = idProcesando == null,
                        onClick = {
                            idProcesando = id
                            errorAccion = null

                            scope.launch {
                                repository.aprobarPublicacion(id)
                                    .onSuccess {
                                        idParaAprobar = null
                                        pendientes = pendientes.filterNot { it.id == id }
                                        actualizacion++
                                    }
                                    .onFailure {
                                        errorAccion = "No se pudo aprobar la publicación."
                                        idParaAprobar = null
                                    }

                                idProcesando = null
                            }
                        }
                    ) {
                        Text("Aprobar")
                    }
                },
                dismissButton = {
                    TextButton(
                        enabled = idProcesando == null,
                        onClick = { idParaAprobar = null }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
        idParaRechazar?.let { id ->
            AlertDialog(
                onDismissRequest = {
                    if (idProcesando == null) idParaRechazar = null
                },
                title = {
                    Text("Rechazar publicación")
                },
                text = {
                    Column {
                        Text("Escribe el motivo del rechazo.")

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = motivoRechazo,
                            onValueChange = {
                                if (it.length <= 2000) motivoRechazo = it
                            },
                            label = { Text("Motivo del rechazo") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            maxLines = 5
                        )
                    }
                },
                confirmButton = {
                    Button(
                        enabled = motivoRechazo.isNotBlank() && idProcesando == null,
                        onClick = {
                            val motivo = motivoRechazo.trim()
                            idProcesando = id
                            errorAccion = null

                            scope.launch {
                                repository.rechazarPublicacion(id, motivo)
                                    .onSuccess {
                                        idParaRechazar = null
                                        motivoRechazo = ""
                                        pendientes = pendientes.filterNot { it.id == id }
                                        actualizacion++
                                    }
                                    .onFailure {
                                        errorAccion = "No se pudo rechazar la publicación."
                                        idParaRechazar = null
                                    }

                                idProcesando = null
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorError
                        )
                    ) {
                        Text("Rechazar")
                    }
                },
                dismissButton = {
                    TextButton(
                        enabled = idProcesando == null,
                        onClick = {
                            idParaRechazar = null
                            motivoRechazo = ""
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )

        }


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
            Column(modifier = Modifier.padding(16.dp, vertical = 5.dp)) {
                Text(
                    text = "Comunidad",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Revisa y modera las publicaciones de la comunidad.",
                    fontSize = 13.sp,
                    color = GrisTexto,
                )
            }
            // Pestañas (Pendientes, Aprobados, Rechazados)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabPill(
                    "Pendientes (${pendientes.size})",
                    pestañaSeleccionada == 0
                ) {
                    pestañaSeleccionada = 0
                }
                TabPill("Aprobados", pestañaSeleccionada == 1) {
                    errorAccion = null
                    pestañaSeleccionada = 1
                }
                TabPill("Rechazados", pestañaSeleccionada == 2) {
                    errorAccion = null
                    pestañaSeleccionada = 2
                }            }
            errorAccion?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = ColorError,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (cargando) {
                    item {
                        CircularProgressIndicator(
                            color = AzulMarino
                        )
                    }
                } else if (error != null) {
                    item {
                        Text(
                            text = error.orEmpty(),
                            color = ColorError
                        )
                    }
                } else {
                    when (pestañaSeleccionada) {

                        // PUBLICACIONES PENDIENTES
                        0 -> {
                            if (pendientes.isEmpty()) {
                                item {
                                    Text(
                                        text = "No hay publicaciones pendientes de revisión.",
                                        color = GrisTexto
                                    )
                                }
                            }

                            items(
                                count = pendientes.size,
                                key = { pendientes[it].id }
                            ) { index ->
                                val publicacion = pendientes[index]

                                TarjetaModeracionComunidad(
                                    nombre = publicacion.autor,
                                    tiempo = publicacion.tiempoRelativo,
                                    estado = "Pendiente",
                                    contenido = publicacion.contenido,
                                    imagenUrl = publicacion.imagenUrl,
                                    titulo = publicacion.titulo,
                                    habilitado = idProcesando == null,
                                    onRechazar = {
                                        motivoRechazo = ""
                                        idParaRechazar = publicacion.id
                                    },
                                    onAprobar = {
                                        idParaAprobar = publicacion.id
                                    }
                                )
                            }
                        }

                        // HISTORIAL DE APROBADAS
                        1 -> {
                            if (aprobadas.isEmpty()) {
                                item {
                                    Text(
                                        text = "Todavía no hay publicaciones aprobadas.",
                                        color = GrisTexto
                                    )
                                }
                            }

                            items(
                                count = aprobadas.size,
                                key = { aprobadas[it].id }
                            ) { index ->
                                val publicacion = aprobadas[index].toDomain()

                                TarjetaModeracionComunidad(
                                    nombre = publicacion.autor,
                                    tiempo = publicacion.tiempoRelativo,
                                    estado = "Aprobada",
                                    contenido = publicacion.contenido,
                                    imagenUrl = publicacion.imagenUrl,
                                    titulo = publicacion.titulo,
                                    mostrarAcciones = false,
                                    onRechazar = {},
                                    onAprobar = {},
                                    onAbrir = {
                                        navController.navigate(
                                            com.example.familiasquesuman.ui.navigation.Rutas.DetallePublicacion.crearRuta(publicacion.id)
                                        )
                                    },

                                )
                            }
                        }

                        // HISTORIAL DE RECHAZADAS
                        2 -> {
                            if (rechazadas.isEmpty()) {
                                item {
                                    Text(
                                        text = "Todavía no hay publicaciones rechazadas.",
                                        color = GrisTexto
                                    )
                                }
                            }

                            items(
                                count = rechazadas.size,
                                key = { rechazadas[it].id }
                            ) { index ->
                                val dto = rechazadas[index]
                                val publicacion = dto.toDomain()

                                TarjetaModeracionComunidad(
                                    nombre = publicacion.autor,
                                    tiempo = publicacion.tiempoRelativo,
                                    estado = "Rechazada",
                                    contenido = publicacion.contenido,
                                    imagenUrl = publicacion.imagenUrl,
                                    titulo = publicacion.titulo,
                                    motivo = dto.motivoRechazo,
                                    mostrarAcciones = false,
                                    onRechazar = {},
                                    onAprobar = {}
                                )
                            }
                        }
                    }
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
        onRechazar: () -> Unit,
        onAprobar: () -> Unit,
        imagenUrl: String? = null,
        onAbrir: (() -> Unit)? = null,
        titulo: String? = null,
        motivo: String? = null,
        mostrarAcciones: Boolean = true,
        habilitado: Boolean = true
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (onAbrir != null) {
                        Modifier.clickable { onAbrir() }
                    } else {
                        Modifier
                    }
                )
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
                            Text(
                                text = nombre,
                                fontWeight = FontWeight.Bold,
                                color = AzulMarino,
                                fontSize = 14.sp
                            )
                            Text(text = tiempo, color = GrisTexto, fontSize = 11.sp)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = when (estado) {
                            "Aprobada" -> Color(0xFFE5F3E8)
                            "Rechazada" -> Color(0xFFFFE8E8)
                            else -> AmbarClaro
                        }
                    ) {
                        Text(
                            text = estado,
                            color = when (estado) {
                                "Aprobada" -> Color(0xFF257942)
                                "Rechazada" -> ColorError
                                else -> Ambar
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                titulo?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = it,
                        color = AzulMarino,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                }

                Text(
                    text = contenido,
                    color = AzulMarino,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                motivo?.takeIf { it.isNotBlank() }?.let {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Motivo del rechazo: $it",
                        color = ColorError,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Imágenes de la publicación
                imagenUrl?.takeIf { it.isNotBlank() }?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = "Imagen de la publicación",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentScale = ContentScale.Crop
                    )
                }


                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = GrisBordeClaro)

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    if (mostrarAcciones) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = onRechazar,
                                    enabled = habilitado,
                                    border = BorderStroke(1.dp, ColorError),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorError),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 12.dp,
                                        vertical = 4.dp
                                    ),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text(
                                        "Rechazar",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Button(
                                    onClick = onAprobar,
                                    enabled = habilitado,
                                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarino),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 16.dp,
                                        vertical = 4.dp
                                    ),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text(
                                        "Aprobar",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
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