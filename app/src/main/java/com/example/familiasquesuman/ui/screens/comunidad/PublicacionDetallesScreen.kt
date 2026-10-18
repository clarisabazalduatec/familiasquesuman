package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.*
import com.example.familiasquesuman.ui.model.TipoSesion
import com.example.familiasquesuman.data.repository.ComunidadRepository
import kotlinx.coroutines.launch
import com.example.familiasquesuman.ui.components.FotoPerfil
import coil.compose.AsyncImage

@Composable
fun PublicacionDetalleScreen(navController: NavHostController, publicacionId: String) {
    val repository = remember { ComunidadRepository() }
    var publicacion by remember(publicacionId) { mutableStateOf<PublicacionComunidadUi?>(null) }
    var cargando by remember(publicacionId) { mutableStateOf(true) }
    var errorCarga by remember(publicacionId) { mutableStateOf<String?>(null) }
    val sesionIniciada = SesionManager.tipoSesion != TipoSesion.INVITADO

    LaunchedEffect(publicacionId) {
        cargando = true
        errorCarga = null
        publicacion = null
        repository.obtenerPublicacionPorId(publicacionId)
            .onSuccess { publicacion = it }
            .onFailure { errorCarga = "No se pudo cargar la publicación." }
        cargando = false
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.COMUNIDAD,
        backButton = true,
        onBackClick = { navController.popBackStack() }
    ) { paddingInterno ->
        when {
            cargando -> {
                Box(
                    modifier = Modifier
                        .padding(paddingInterno)
                        .fillMaxSize()
                        .background(CremaFondo),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AzulMarino)
                }
            }
            errorCarga != null -> {
                Box(
                    modifier = Modifier
                        .padding(paddingInterno)
                        .fillMaxSize()
                        .background(CremaFondo),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorCarga.orEmpty(),
                        color = AzulMarino
                    )
                }
            }
            publicacion == null -> {
                Box(
                    modifier = Modifier
                        .padding(paddingInterno)
                        .fillMaxSize()
                        .background(CremaFondo),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Publicación no encontrada",
                        color = AzulMarino
                    )
                }
            }
            else -> {
                DetallePublicacion(
                    publicacion = publicacion!!,
                    navController = navController,
                    paddingInterno = paddingInterno,
                    sesionIniciada = sesionIniciada
                )
            }
        }
    }
}

@Composable
private fun DetallePublicacion(
    publicacion: PublicacionComunidadUi,
    navController: NavHostController,
    paddingInterno: PaddingValues,
    sesionIniciada: Boolean
) {
    val repository = remember { ComunidadRepository() }
    val scope = rememberCoroutineScope()
    var enviandoComentario by remember(publicacion.id) { mutableStateOf(false) }
    var errorEnvioComentario by remember(publicacion.id) { mutableStateOf<String?>(null) }
    var tieneLike by remember(publicacion.id) { mutableStateOf(publicacion.meGusta) }
    var likes by remember(publicacion.id) { mutableIntStateOf(publicacion.numeroLikes) }
    var guardandoLike by remember(publicacion.id) { mutableStateOf(false) }
    var errorLike by remember(publicacion.id) { mutableStateOf<String?>(null) }
    var comentario by rememberSaveable(publicacion.id) { mutableStateOf("") }
    val comentarios = remember(publicacion.id) { mutableStateListOf<ComentarioUi>() }
    var cargandoComentarios by remember(publicacion.id) { mutableStateOf(true) }
    var errorComentarios by remember(publicacion.id) { mutableStateOf<String?>(null) }

    LaunchedEffect(publicacion.id) {
        cargandoComentarios = true
        errorComentarios = null
        repository.obtenerComentarios(publicacion.id)
            .onSuccess { resultado ->
                comentarios.clear()
                comentarios.addAll(resultado)
            }
            .onFailure {
                comentarios.clear()
            }
        cargandoComentarios = false
    }

    val totalComentarios = if (cargandoComentarios || errorComentarios != null) {
        publicacion.numeroComentarios
    } else {
        comentarios.size
    }

    LazyColumn(
        modifier = Modifier
            .padding(paddingInterno)
            .fillMaxSize()
            .background(CremaFondo),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        item {
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
            ) {
                // Header (Foto, Autor, Tiempo y Categoría/Aviso)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FotoPerfil(
                        fotoUrl = publicacion.autorFotoUrl,
                        nombre = publicacion.autor,
                        tamano = 44.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = publicacion.autor,
                            style = MaterialTheme.typography.titleSmall,
                            color = AzulMarino
                        )
                        Text(
                            text = publicacion.tiempoRelativo,
                            style = MaterialTheme.typography.bodySmall,
                            color = GrisTexto
                        )
                    }

                    val etiqueta = if (publicacion.tipo == TipoPublicacion.OFICIAL) {
                        "Aviso Oficial"
                    } else {
                        publicacion.categoria
                    }

                    etiqueta?.let { texto ->
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (publicacion.tipo == TipoPublicacion.OFICIAL) {
                                AzulMarino.copy(alpha = .12f)
                            } else {
                                Ambar.copy(alpha = .25f)
                            }
                        ) {
                            Text(
                                text = texto,
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 5.dp
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = AzulMarino
                            )
                        }
                    }

                    publicacion.estado?.let { estado ->
                        Spacer(modifier = Modifier.width(6.dp))
                        EstadoChip(estado = estado)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Título
                publicacion.titulo?.takeIf { it.isNotBlank() }?.let { titulo ->
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.headlineSmall,
                        color = AzulMarino
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Contenido
                Text(
                    text = publicacion.contenido,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AzulMarino
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Imagen
            publicacion.imagenUrl?.takeIf { it.isNotBlank() }?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Datos adicionales de evento o documento (si aplica)
            if (publicacion.fechaEvento != null || publicacion.horarioEvento != null || publicacion.ubicacion != null || publicacion.publicoObjetivo != null || publicacion.documentoNombre != null) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Blanco),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            publicacion.fechaEvento?.let {
                                DatoDetalle(icono = Icons.Outlined.CalendarMonth, texto = it)
                            }
                            publicacion.horarioEvento?.let {
                                DatoDetalle(icono = Icons.Outlined.Schedule, texto = it)
                            }
                            publicacion.ubicacion?.let {
                                DatoDetalle(icono = Icons.Outlined.LocationOn, texto = it)
                            }
                            publicacion.publicoObjetivo?.takeIf { it.isNotBlank() }?.let { publico ->
                                DatoDetalle(icono = Icons.Outlined.Groups, texto = publico)
                            }
                        }
                    }

                    publicacion.documentoNombre?.let { nombre ->
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Documento adjunto",
                            style = MaterialTheme.typography.titleSmall,
                            color = AzulMarino
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Blanco),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.PictureAsPdf,
                                    contentDescription = null,
                                    tint = ColorError
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = nombre,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = AzulMarino
                                    )
                                    Text(
                                        text = publicacion.documentoDetalle.orEmpty(),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GrisTexto
                                    )
                                }
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = Icons.Outlined.Download,
                                        contentDescription = "Descargar",
                                        tint = AzulMarino
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Barra de interacciones social
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (!sesionIniciada || SesionManager.token.isNullOrBlank()) {
                            navController.navigate(Rutas.Login.ruta)
                        } else if (!guardandoLike) {
                            guardandoLike = true
                            errorLike = null
                            scope.launch {
                                try {
                                    val resultado = if (tieneLike) {
                                        repository.quitarLike(publicacion.id)
                                    } else {
                                        repository.darLike(publicacion.id)
                                    }
                                    resultado.onSuccess {
                                        tieneLike = !tieneLike
                                        likes += if (tieneLike) 1 else -1
                                    }.onFailure {
                                        errorLike = "No se pudo actualizar el me gusta."
                                    }
                                } finally {
                                    guardandoLike = false
                                }
                            }
                        }
                    },
                    enabled = !guardandoLike
                ) {
                    Icon(
                        imageVector = if (tieneLike) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (tieneLike) "Quitar me gusta" else "Dar me gusta",
                        tint = if (tieneLike) ColorError else AzulMarino
                    )
                }

                Text(
                    text = likes.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AzulMarino
                )
                Spacer(modifier = Modifier.width(18.dp))
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Comentarios",
                    tint = AzulMarino
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = totalComentarios.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AzulMarino
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Compartir",
                        tint = AzulMarino
                    )
                }
            }

            errorLike?.let { mensaje ->
                Text(
                    text = mensaje,
                    modifier = Modifier.padding(horizontal = 18.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 18.dp), color = GrisBorde)
            Text(
                text = "Comentarios ($totalComentarios)",
                modifier = Modifier.padding(
                    start = 18.dp,
                    end = 18.dp,
                    top = 14.dp,
                    bottom = 4.dp
                ),
                style = MaterialTheme.typography.titleSmall,
                color = AzulMarino
            )
        }

        item {
            if (cargandoComentarios) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    color = AzulMarino
                )
            } else if (comentarios.isEmpty()) {
                Text(
                    text = "Aún no hay comentarios. ¡Sé el primero en comentar!",
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )
            }

            errorComentarios?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(18.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        items(items = comentarios) { item ->
            Row(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
            ) {
                FotoPerfil(
                    fotoUrl = item.autorFotoUrl,
                    nombre = item.autor,
                    tamano = 38.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.autor,
                            style = MaterialTheme.typography.titleSmall,
                            color = AzulMarino
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.tiempo,
                            style = MaterialTheme.typography.labelSmall,
                            color = GrisTexto
                        )
                    }
                    Text(
                        text = item.comentario,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AzulMarino
                    )
                }
            }
        }

        item {
            if (sesionIniciada) {
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    placeholder = {
                        Text(
                            text = "Escribe un comentario...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                val texto = comentario.trim()
                                if (texto.isNotEmpty() && !enviandoComentario) {
                                    enviandoComentario = true
                                    errorEnvioComentario = null
                                    scope.launch {
                                        val resultado = repository.publicarComentario(
                                            publicacionId = publicacion.id,
                                            contenido = texto
                                        )
                                        resultado.onSuccess { comentarioGuardado ->
                                            comentarios.add(comentarioGuardado)
                                            comentario = ""
                                        }.onFailure {
                                            errorEnvioComentario =
                                                "No se pudo publicar el comentario. Inténtalo nuevamente."
                                        }
                                        enviandoComentario = false
                                    }
                                }
                            },
                            enabled = comentario.isNotBlank() && !enviandoComentario
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Send,
                                contentDescription = "Enviar",
                                tint = if (comentario.isNotBlank() && !enviandoComentario) {
                                    AzulMarino
                                } else {
                                    GrisTexto
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                )
                errorEnvioComentario?.let { mensaje ->
                    Text(
                        text = mensaje,
                        modifier = Modifier.padding(horizontal = 18.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Blanco),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = null,
                            tint = AzulMarino,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "¿Quieres participar en la conversación?",
                            style = MaterialTheme.typography.titleSmall,
                            color = AzulMarino
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Inicia sesión para dejar un comentario.",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrisTexto
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { navController.navigate(Rutas.Login.ruta) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = AzulMarino
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Login,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Iniciar sesión",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DatoDetalle(icono: androidx.compose.ui.graphics.vector.ImageVector, texto: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = AzulMarino,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = AzulMarino
        )
    }
}
