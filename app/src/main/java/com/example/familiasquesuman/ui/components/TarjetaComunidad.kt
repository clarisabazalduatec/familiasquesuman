package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.R
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.GrisBorde
import com.example.familiasquesuman.ui.theme.GrisTexto
import coil.compose.AsyncImage
import com.example.familiasquesuman.ui.screens.comunidad.PublicacionComunidadUi
import androidx.compose.material.icons.filled.Favorite
@Composable
fun TarjetaComunidad(
    onVerComunidad: () -> Unit,
    onChatbot: () -> Unit = {},
    ultimoAviso: PublicacionComunidadUi? = null,
    ultimaPublicacion: PublicacionComunidadUi? = null,
    onAbrirPublicacion: (String) -> Unit = {}
) {


    var pestanaSeleccionada by rememberSaveable {
        mutableStateOf("Oficial")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(54.dp),
                    shape = CircleShape,
                    color = AmbarClaro
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = AzulMarino,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Comunidad",
                        style = MaterialTheme.typography.titleLarge,
                        color = AzulMarino
                    )

                    Text(
                        text = "Consulta avisos oficiales y comparte experiencias de otras familias.",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrisTexto
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            //tabs
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent,
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = GrisBorde
                )
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // AVISOS OFICIALES
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 22.dp,
                                    bottomStart = 22.dp
                                )
                            )
                            .background(
                                if (pestanaSeleccionada == "Oficial") {
                                    AzulMarino
                                } else {
                                    Blanco
                                }
                            )
                            .clickable {
                                pestanaSeleccionada = "Oficial"
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Avisos Oficiales",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color =
                                if (pestanaSeleccionada == "Oficial") {
                                    Blanco
                                } else {
                                    AzulMarino
                                }
                        )
                    }

                    // COMUNIDAD
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(
                                RoundedCornerShape(
                                    topEnd = 22.dp,
                                    bottomEnd = 22.dp
                                )
                            )
                            .background(
                                if (pestanaSeleccionada == "Comunidad") {
                                    AzulMarino
                                } else {
                                    Blanco
                                }
                            )
                            .clickable {
                                pestanaSeleccionada = "Comunidad"
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Comunidad",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color =
                                if (pestanaSeleccionada == "Comunidad") {
                                    Blanco
                                } else {
                                    AzulMarino
                                }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )


            if (pestanaSeleccionada == "Oficial") {
                if (ultimoAviso != null) {
                    ContenidoAvisoOficial(
                        publicacion = ultimoAviso,
                        onClick = {
                            onAbrirPublicacion(ultimoAviso.id)
                        }
                    )
                } else {
                    Text(
                        text = "No hay avisos oficiales disponibles.",
                        color = GrisTexto
                    )
                }
            } else {
                if (ultimaPublicacion != null) {
                    ContenidoComunidad(
                        publicacion = ultimaPublicacion,
                        onClick = {
                            onAbrirPublicacion(ultimaPublicacion.id)
                        }
                    )
                } else {
                    Text(
                        text = "No hay publicaciones disponibles.",
                        color = GrisTexto
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )


            TextButton(
                onClick = onVerComunidad,
                modifier = Modifier.fillMaxWidth()
            ) {

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "Ver comunidad",
                    style = MaterialTheme.typography.titleSmall,
                    color = AzulMarino
                )

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = AzulMarino,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

//avisios oficiales
@Composable
private fun ContenidoAvisoOficial(
    publicacion: PublicacionComunidadUi,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = publicacion.imagenUrl,
            contentDescription = "Aviso oficial",
            modifier = Modifier
                .width(120.dp)
                .height(92.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Outlined.Campaign,
                    contentDescription = null,
                    tint = AzulMarino,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(
                    modifier = Modifier.width(5.dp)
                )

                Text(
                    text = "Aviso oficial",
                    style = MaterialTheme.typography.labelSmall,
                    color = AzulMarino
                )
            }

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = publicacion.titulo.orEmpty(),                style = MaterialTheme.typography.titleSmall,
                color = AzulMarino
            )

            Text(
                text = publicacion.contenido,                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


//comunidad
@Composable
private fun ContenidoComunidad(
    publicacion: PublicacionComunidadUi,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = AmbarClaro
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = AzulMarino,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(9.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = publicacion.autor,
                    style = MaterialTheme.typography.titleSmall,
                    color = AzulMarino
                )

                Text(
                    text = publicacion.tiempoRelativo,
                    style = MaterialTheme.typography.labelSmall,
                    color = GrisTexto
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically)
        {
            AsyncImage(
                model = publicacion.imagenUrl,
                contentDescription = "Publicación de comunidad",
                modifier = Modifier
                    .width(120.dp)
                    .height(92.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f))
            {

                Text(
                    text = publicacion.contenido,
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically)
                {
                    Icon(
                        imageVector = if (publicacion.meGusta) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = null,
                        tint = if (publicacion.meGusta) {
                            Color.Red
                        } else {
                            AzulMarino
                        },
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${publicacion.numeroLikes}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AzulMarino
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector =
                            Icons.Outlined.ChatBubbleOutline,
                        contentDescription = null,
                        tint = AzulMarino,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${publicacion.numeroComentarios}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AzulMarino
                    )
                }
            }
        }
    }
}