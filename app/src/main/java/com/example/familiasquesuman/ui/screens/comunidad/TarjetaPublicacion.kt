package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.ui.theme.*

@Composable
fun TarjetaPublicacion(
    publicacion: PublicacionComunidadUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (
        publicacion.tipo ==
        TipoPublicacion.OFICIAL
    ) {

        TarjetaPublicacionOficial(
            publicacion = publicacion,
            onClick = onClick,
            modifier = modifier
        )

    } else {

        TarjetaPublicacionComunidad(
            publicacion = publicacion,
            onClick = onClick,
            modifier = modifier
        )
    }
}

@Composable
private fun TarjetaPublicacionOficial(
    publicacion: PublicacionComunidadUi,
    onClick: () -> Unit,
    modifier: Modifier
) {

    Card(
        onClick = onClick,
        modifier =
            modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(22.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = Blanco
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column {

            publicacion.imagenRes?.let { imagen ->

                Image(
                    painter =
                        painterResource(imagen),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(165.dp),
                    contentScale =
                        ContentScale.Crop
                )
            }

            Column(
                modifier =
                    Modifier.padding(16.dp)
            ) {

                Text(
                    text =
                        publicacion.titulo.orEmpty(),
                    style =
                        MaterialTheme.typography
                            .titleLarge,
                    color = AzulMarino
                )

                Spacer(
                    Modifier.height(5.dp)
                )

                Text(
                    text =
                        publicacion.contenido,

                    style =
                        MaterialTheme.typography
                            .bodyMedium,

                    color = GrisTexto,

                    maxLines = 3,

                    overflow =
                        TextOverflow.Ellipsis
                )

                Spacer(
                    Modifier.height(12.dp)
                )

                publicacion
                    .fechaEvento
                    ?.let {

                        FilaDato(
                            Icons.Outlined
                                .CalendarMonth,
                            it
                        )
                    }

                publicacion
                    .horarioEvento
                    ?.let {

                        FilaDato(
                            Icons.Outlined.Schedule,
                            it
                        )
                    }

                publicacion
                    .ubicacion
                    ?.let {

                        FilaDato(
                            Icons.Outlined.LocationOn,
                            it
                        )
                    }

                Spacer(
                    Modifier.height(8.dp)
                )

                HorizontalDivider(
                    color =
                        GrisBorde.copy(
                            alpha = .7f
                        )
                )

                TextButton(
                    onClick = onClick,
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Ver anuncio",
                        style =
                            MaterialTheme.typography
                                .titleSmall,
                        color = AzulMarino
                    )

                    Spacer(
                        Modifier.weight(1f)
                    )

                    Icon(
                        imageVector =
                            Icons.Outlined
                                .ChevronRight,
                        contentDescription = null,
                        tint = AzulMarino
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaPublicacionComunidad(
    publicacion: PublicacionComunidadUi,
    onClick: () -> Unit,
    modifier: Modifier
) {

    var tieneLike by rememberSaveable(
        publicacion.id
    ) {
        mutableStateOf(false)
    }

    var likes by rememberSaveable(
        publicacion.id
    ) {
        mutableIntStateOf(
            publicacion.numeroLikes
        )
    }

    Card(
        onClick = onClick,

        modifier =
            modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(22.dp),

        colors =
            CardDefaults.cardColors(
                containerColor = Blanco
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column {

            Row(
                modifier =
                    Modifier.padding(
                        start = 14.dp,
                        end = 14.dp,
                        top = 14.dp,
                        bottom = 10.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Surface(
                    modifier =
                        Modifier.size(42.dp),

                    shape = CircleShape,

                    color = AmbarClaro
                ) {

                    Box(
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Outlined.Person,
                            contentDescription = null,
                            tint = AzulMarino
                        )
                    }
                }

                Spacer(
                    Modifier.width(9.dp)
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            publicacion.autor,

                        style =
                            MaterialTheme.typography
                                .titleSmall,

                        color = AzulMarino
                    )

                    Text(
                        text =
                            publicacion
                                .tiempoRelativo,

                        style =
                            MaterialTheme.typography
                                .bodySmall,

                        color = GrisTexto
                    )
                }

                publicacion
                    .categoria
                    ?.let {

                        Surface(
                            shape =
                                RoundedCornerShape(
                                    50
                                ),

                            color =
                                Ambar.copy(
                                    alpha = .28f
                                )
                        ) {

                            Text(
                                text = it,

                                modifier =
                                    Modifier.padding(
                                        horizontal =
                                            10.dp,
                                        vertical =
                                            5.dp
                                    ),

                                style =
                                    MaterialTheme.typography
                                        .labelSmall,

                                color = AzulMarino
                            )
                        }
                    }
            }

            publicacion
                .imagenRes
                ?.let {

                    Image(
                        painter =
                            painterResource(it),

                        contentDescription = null,

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(215.dp),

                        contentScale =
                            ContentScale.Crop
                    )
                }

            Column(
                modifier =
                    Modifier.padding(14.dp)
            ) {

                Text(
                    text =
                        publicacion.contenido,

                    style =
                        MaterialTheme.typography
                            .bodyMedium,

                    color = AzulMarino,

                    maxLines = 3,

                    overflow =
                        TextOverflow.Ellipsis
                )

                Spacer(
                    Modifier.height(9.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = {

                            tieneLike =
                                !tieneLike

                            likes +=
                                if (tieneLike)
                                    1
                                else
                                    -1
                        },
                        modifier =
                            Modifier.size(36.dp)
                    ) {

                        Icon(
                            imageVector =
                                if (tieneLike)
                                    Icons.Filled.Favorite
                                else
                                    Icons.Outlined
                                        .FavoriteBorder,

                            contentDescription =
                                "Me gusta",

                            tint =
                                if (tieneLike)
                                    ColorError
                                else
                                    AzulMarino
                        )
                    }

                    Text(
                        text = likes.toString(),
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color = AzulMarino
                    )

                    Spacer(
                        Modifier.width(14.dp)
                    )

                    IconButton(
                        onClick = onClick,
                        modifier =
                            Modifier.size(36.dp)
                    ) {

                        Icon(
                            imageVector =
                                Icons.Outlined
                                    .ChatBubbleOutline,
                            contentDescription =
                                "Comentarios",
                            tint = AzulMarino
                        )
                    }

                    Text(
                        text =
                            publicacion
                                .numeroComentarios
                                .toString(),

                        style =
                            MaterialTheme.typography
                                .bodySmall,

                        color = AzulMarino
                    )

                    Spacer(
                        Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {}
                    ) {

                        Icon(
                            imageVector =
                                Icons.Outlined.Share,
                            contentDescription =
                                "Compartir",
                            tint = AzulMarino
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaDato(
    icono:
    androidx.compose.ui.graphics.vector.ImageVector,
    texto: String
) {

    Row(
        modifier =
            Modifier.padding(
                vertical = 3.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icono,
            contentDescription = null,
            modifier =
                Modifier.size(18.dp),
            tint = AzulMarino
        )

        Spacer(
            Modifier.width(8.dp)
        )

        Text(
            text = texto,
            style =
                MaterialTheme.typography
                    .bodySmall,
            color = AzulMarino
        )
    }
}