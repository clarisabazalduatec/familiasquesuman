package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.*
import com.example.familiasquesuman.ui.model.SesionDemoState
import com.example.familiasquesuman.ui.model.TipoSesion

@Composable
fun PublicacionDetalleScreen(
    navController: NavHostController,
    publicacionId: String
) {

    val publicacion = buscarPublicacion(publicacionId)
    val sesionIniciada = SesionDemoState.tipoSesion != TipoSesion.INVITADO

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,

    ) { paddingInterno ->
        if (publicacion == null) {
            Box(modifier = Modifier
                    .padding(paddingInterno)
                    .fillMaxSize()
                    .background(CremaFondo),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Publicación no encontrada",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AzulMarino
                )
            }
            return@PantallaPrincipalConMenu
        }

        if (publicacion.tipo == TipoPublicacion.OFICIAL) {
            DetalleOficial(publicacion = publicacion,
                navController = navController,
                paddingInterno = paddingInterno
            )

        } else {
            DetalleComunidad(
                publicacion = publicacion,
                navController = navController,
                paddingInterno = paddingInterno,
                sesionIniciada = sesionIniciada
            )
        }
    }
}

@Composable
private fun DetalleOficial(
    publicacion: PublicacionComunidadUi,
    navController: NavHostController,
    paddingInterno: PaddingValues
) {

    LazyColumn(modifier = Modifier
            .padding(paddingInterno)
            .fillMaxSize()
            .background(CremaFondo),
        contentPadding = PaddingValues(bottom = 28.dp)) {
        item {
            Column(modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp)
            ) {

                Surface(modifier = Modifier.fillMaxWidth(),
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
                            Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Regresar",
                                tint = AzulMarino,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = publicacion.titulo.orEmpty(),
                                style = MaterialTheme.typography.titleLarge,
                                color = AzulMarino
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(text = "Por ${publicacion.autor}",
                                style = MaterialTheme.typography.labelMedium,
                                color = AzulMarino
                            )
                            Text(text = publicacion.tiempoRelativo,
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTexto
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            publicacion.imagenRes?.let { imagen -> Image(painter = painterResource(imagen),
                    contentDescription = publicacion.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(245.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(18.dp)) {
                Text(text = publicacion.contenido,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AzulMarino
                )
                Spacer(modifier = Modifier.height(18.dp))
                Card(modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Blanco
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp))
                    { publicacion.fechaEvento?.let {
                            DatoDetalle(icono = Icons.Outlined.CalendarMonth, texto = it)
                        }
                        publicacion.horarioEvento?.let {
                            DatoDetalle(icono = Icons.Outlined.Schedule, texto = it)
                        }
                        publicacion.ubicacion?.let {
                            DatoDetalle(icono = Icons.Outlined.LocationOn, texto = it)
                        }
                        DatoDetalle(icono = Icons.Outlined.Groups, texto = "Abierto a todas las familias")
                    }
                }
                publicacion.documentoNombre?.let { nombre ->
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(text = "Documento adjunto",
                        style = MaterialTheme.typography.titleSmall,
                        color = AzulMarino
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Blanco
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Outlined.PictureAsPdf,
                                contentDescription = null,
                                tint = ColorError
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = nombre,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = AzulMarino
                                )
                                Text(text = publicacion.documentoDetalle.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GrisTexto
                                )
                            }
                            IconButton(onClick = {}) {
                                Icon(imageVector = Icons.Outlined.Download,
                                    contentDescription = "Descargar",
                                    tint = AzulMarino
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(22.dp))
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulMarino,
                        contentColor = Blanco
                    )
                ) {
                    Text(text = "¡Te esperamos!",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun DetalleComunidad(
    publicacion: PublicacionComunidadUi,
    navController: NavHostController,
    paddingInterno: PaddingValues,
    sesionIniciada: Boolean

) {
    var tieneLike by rememberSaveable(publicacion.id) { mutableStateOf(false) }
    var likes by rememberSaveable(publicacion.id) { mutableIntStateOf(publicacion.numeroLikes) }
    var comentario by rememberSaveable { mutableStateOf("") }
    val comentarios = remember(publicacion.id) {
        mutableStateListOf<ComentarioUi>().apply {
            addAll(publicacion.comentarios)
        }
    }
    val comentariosNuevos = comentarios.size - publicacion.comentarios.size
    val totalComentarios = publicacion.numeroComentarios + comentariosNuevos
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
                    vertical = 8.dp
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Blanco,
                    tonalElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 6.dp,
                            vertical = 10.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Regresar",
                                tint = AzulMarino
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            modifier = Modifier.size(42.dp),
                            shape = CircleShape,
                            color = AmbarClaro
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = AzulMarino
                                )
                            }
                        }
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
                        EstadoChip(estado = publicacion.estado)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                publicacion.categoria?.let {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Ambar.copy(alpha = .25f)
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 5.dp
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = AzulMarino
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                publicacion.titulo?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineSmall,
                        color = AzulMarino
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Text(
                    text = publicacion.contenido,
                    style = MaterialTheme.typography.bodyLarge,
                    color = AzulMarino
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
            publicacion.imagenRes?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(285.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 14.dp,
                        vertical = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {
                    tieneLike = !tieneLike
                    likes += if (tieneLike) {
                        1
                    } else {
                        -1
                    }
                }
                ) {
                    Icon(
                        imageVector =
                            if (tieneLike)
                                Icons.Filled.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,
                        contentDescription = "Me gusta",
                        tint =
                            if (tieneLike)
                                ColorError
                            else
                                AzulMarino
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
        items(items = comentarios) { item ->
            Row(
                modifier = Modifier.padding(
                    horizontal = 18.dp,
                    vertical = 8.dp
                )
            ) {
                Surface(
                    modifier = Modifier.size(38.dp),
                    shape = CircleShape,
                    color = AmbarClaro
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = AzulMarino
                        )
                    }
                }
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
                                if (comentario.isNotBlank()) {
                                    comentarios.add(
                                        ComentarioUi(
                                            autor = "Mariana",
                                            tiempo = "Ahora",
                                            comentario = comentario.trim(),
                                            likes = 0
                                        )
                                    )
                                    comentario = ""
                                }
                            },
                            enabled = comentario.isNotBlank()
                        ) {

                            Icon(
                                imageVector = Icons.Outlined.Send,
                                contentDescription = "Enviar",
                                tint =
                                    if (comentario.isNotBlank())
                                        AzulMarino
                                    else
                                        GrisTexto
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(
                            horizontal = 18.dp,
                            vertical = 12.dp
                        )
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                )
            } else {
                Card(
                    modifier = Modifier
                        .padding(
                            horizontal = 18.dp,
                            vertical = 12.dp
                        )
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
                            style = MaterialTheme
                                .typography
                                .titleSmall,
                            color = AzulMarino
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Inicia sesión para dejar un comentario.",
                            style = MaterialTheme
                                .typography
                                .bodySmall,
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
                            Icon(imageVector = Icons.Outlined.Login, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Iniciar sesión",
                                style = MaterialTheme
                                    .typography
                                    .titleSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DatoDetalle(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    texto: String
) {
    Row(modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icono, contentDescription = null,
            tint = AzulMarino,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = AzulMarino
        )
    }
}