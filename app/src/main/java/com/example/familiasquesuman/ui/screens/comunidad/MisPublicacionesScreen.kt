package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.*
import coil.compose.AsyncImage
import com.example.familiasquesuman.data.repository.ComunidadRepository
import com.example.familiasquesuman.data.remote.dto.PublicacionDto
import com.example.familiasquesuman.data.remote.dto.toDomain
import kotlinx.coroutines.CancellationException
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalLifecycleOwner

private enum class FiltroEstado { TODAS, REVISION, PUBLICADAS, RECHAZADAS }

@Composable
fun MisPublicacionesScreen(navController: NavHostController) {
    var filtro by rememberSaveable { mutableStateOf(FiltroEstado.TODAS) }

    val repository = remember { ComunidadRepository() }
    val lifecycleOwner = LocalLifecycleOwner.current

    var todasLasPublicaciones by remember {
        mutableStateOf<List<PublicacionDto>>(emptyList())
    }

    var cargando by remember { mutableStateOf(true) }
    var errorCarga by remember { mutableStateOf<String?>(null) }
    var actualizacion by remember { mutableIntStateOf(0) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) {
                actualizacion++
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(actualizacion) {
        cargando = true
        errorCarga = null

        try {
            repository.obtenerMisPublicaciones()
                .onSuccess { todasLasPublicaciones = it }
                .onFailure {
                    errorCarga = "No se pudieron cargar tus publicaciones."
                }
        } catch (e: CancellationException) {
            throw e
        } finally {
            cargando = false
        }
    }

    val publicacionesFiltradas = when (filtro) {
        FiltroEstado.TODAS -> todasLasPublicaciones
        FiltroEstado.REVISION -> todasLasPublicaciones.filter {
            it.estado == "PENDIENTE"
        }
        FiltroEstado.PUBLICADAS -> todasLasPublicaciones.filter {
            it.estado == "APROBADA"
        }
        FiltroEstado.RECHAZADAS -> todasLasPublicaciones.filter {
            it.estado == "RECHAZADA"
        }
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.COMUNIDAD,
    ) { paddingInterno -> LazyColumn(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .background(CremaFondo),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 8.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Surface(modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Blanco,
                    tonalElevation = 1.dp
                ) {
                    Row(modifier = Modifier.padding(
                            horizontal = 6.dp,
                            vertical = 10.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.AutoMirrored
                                        .Outlined
                                        .ArrowBack,
                                contentDescription = "Regresar",
                                tint = AzulMarino
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(text = "Mis publicaciones",
                                style = MaterialTheme
                                        .typography
                                        .titleLarge,
                                color = AzulMarino
                            )

                            Text(text = "Consulta el estado de lo que has compartido.",
                                style = MaterialTheme
                                        .typography
                                        .bodySmall,
                                color = GrisTexto
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FiltroPublicacionesChip(
                            texto = "Todas",
                            seleccionado = filtro == FiltroEstado.TODAS,
                            onClick = { filtro = FiltroEstado.TODAS }
                        )
                    }

                    item {
                        FiltroPublicacionesChip(
                            texto = "En revisión",
                            seleccionado = filtro == FiltroEstado.REVISION,
                            onClick = { filtro = FiltroEstado.REVISION }
                        )
                    }

                    item {
                        FiltroPublicacionesChip(
                            texto = "Publicadas",
                            seleccionado = filtro == FiltroEstado.PUBLICADAS,
                            onClick = { filtro = FiltroEstado.PUBLICADAS }
                        )
                    }

                    item {
                        FiltroPublicacionesChip(
                            texto = "Rechazadas",
                            seleccionado = filtro == FiltroEstado.RECHAZADAS,
                            onClick = { filtro = FiltroEstado.RECHAZADAS }
                        )
                    }
                }

            } // Cierra el item del encabezado

// Indicador de carga y mensajes
        if (cargando) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AzulMarino)
                }
            }
        } else if (errorCarga != null) {
            item {
                Text(
                    text = errorCarga.orEmpty(),
                    color = ColorError
                )
            }
        } else if (publicacionesFiltradas.isEmpty()) {
            item {
                Text(
                    text = "No tienes publicaciones en esta sección.",
                    color = GrisTexto
                )
            }
        }

// Publicaciones reales
        items(
            items = if (cargando || errorCarga != null) {
                emptyList()
            } else {
                publicacionesFiltradas
            },
            key = { it.id }
        )
        { dto ->
            val publicacion = dto.toDomain()
            Card(
                onClick = {
                    navController.navigate(
                        Rutas.DetallePublicacion.crearRuta(publicacion.id)
                    )
                },
                enabled = dto.estado == "APROBADA",
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Blanco
                ),
                shape = RoundedCornerShape(18.dp)
            )
                   {
                    Row(modifier = Modifier.padding(12.dp),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        publicacion.imagenUrl?.takeIf { it.isNotBlank() }?.let { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(82.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = publicacion.titulo.orEmpty(),
                                style =
                                    MaterialTheme
                                        .typography
                                        .titleSmall,
                                color = AzulMarino
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            EstadoChip(estado = publicacion.estado)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = publicacion.tiempoRelativo,
                                style =
                                    MaterialTheme
                                        .typography
                                        .labelSmall,
                                color = GrisTexto
                            )
                            if (dto.estado == "RECHAZADA") {
                                dto.motivoRechazo
                                    ?.takeIf { it.isNotBlank() }
                                    ?.let { motivo ->
                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "Motivo: $motivo",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = ColorError
                                        )
                                    }
                            }
                        }
                        if (dto.estado == "APROBADA") {
                            Icon(
                                imageVector = Icons.Outlined.ChevronRight,
                                contentDescription = null,
                                tint = AzulMarino
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FiltroPublicacionesChip(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    FilterChip(selected = seleccionado,
        onClick = onClick,
        label = {
            Text(text = texto,
                style = MaterialTheme.typography.labelMedium
            )
        },
        colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = AzulMarino,
                selectedLabelColor = Blanco
            )
    )
}

@Composable
fun EstadoChip(
    estado: EstadoPublicacion?
) {
    if (estado == null) return
    val texto = when (estado) {
        EstadoPublicacion.EN_REVISION -> "En revisión"
        EstadoPublicacion.PUBLICADA -> "Publicada"
        EstadoPublicacion.RECHAZADA -> "Rechazada"
    }

    val color = when (estado) {
        EstadoPublicacion.EN_REVISION -> Ambar
        EstadoPublicacion.PUBLICADA -> ColorExito
        EstadoPublicacion.RECHAZADA -> ColorError
    }

    Surface(shape = RoundedCornerShape(50),
        color = color.copy(
            alpha = .14f
        )
    ) {

        Text(text = texto,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}