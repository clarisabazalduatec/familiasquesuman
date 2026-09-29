package com.example.familiasquesuman.ui.screens.actividad.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familiasquesuman.data.ActividadMock
import com.example.familiasquesuman.ui.theme.*

/**
 * Chip de categoría unificado para tarjetas y pantallas de detalles.
 */
@Composable
fun CategoriaChip(
    categoria: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    mostrarIcono: Boolean = true
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            if (mostrarIcono && icono != null) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = textColor
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = categoria,
                fontSize = 11.sp,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Muestra una lista de avatares superpuestos con badge opcional de participantes adicionales.
 */
@Composable
fun AvataresParticipantes(
    participantesAdicionales: Int,
    modifier: Modifier = Modifier,
    tamanoAvatar: Dp = 20.dp,
    desplazamientoOffset: Dp = 8.dp
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(tamanoAvatar)
                .background(Color.Gray, CircleShape)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(tamanoAvatar)
                .offset(x = -desplazamientoOffset)
                .background(Color.DarkGray, CircleShape)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(tamanoAvatar)
                .offset(x = -(desplazamientoOffset * 2))
                .background(Color.LightGray, CircleShape)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        )
        if (participantesAdicionales > 0) {
            Box(
                modifier = Modifier
                    .size(tamanoAvatar)
                    .offset(x = -(desplazamientoOffset * 3))
                    .background(Ambar, CircleShape)
                    .clip(CircleShape)
                    .border(1.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$participantesAdicionales",
                    fontSize = 8.sp,
                    color = AzulOscuro,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Tarjeta de actividad en la lista principal de Actividades.
 */
@Composable
fun TarjetaActividadDia(
    actividad: ActividadMock,
    onActividadClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onActividadClick(actividad.id) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(140.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Imagen Izquierda
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .background(GrisClaroFondo)
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(32.dp)
                )
            }

            // Contenido Derecha
            Column(
                modifier = Modifier
                    .weight(0.65f)
                    .padding(12.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header (Tag y Bookmark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoriaChip(
                        categoria = actividad.categoria,
                        icono = actividad.iconoCategoria,
                        backgroundColor = actividad.colorCategoria,
                        textColor = actividad.textColorCategoria,
                        mostrarIcono = true
                    )
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Guardar",
                        tint = AzulOscuro,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Info (Hora, Título, Lugar)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = TextoGrisActividad
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = actividad.horario,
                            fontSize = 11.sp,
                            color = TextoGrisActividad
                        )
                    }
                    Text(
                        text = actividad.titulo,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuro,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = TextoGrisActividad
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = actividad.ubicacion,
                            fontSize = 11.sp,
                            color = TextoGrisActividad,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Footer (Lugares y Avatares)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "${actividad.lugaresDisponibles} lugares disponibles",
                        fontSize = 10.sp,
                        color = TextoGrisActividad
                    )

                    AvataresParticipantes(
                        participantesAdicionales = actividad.participantesAdicionales
                    )
                }
            }
        }
    }
}

/**
 * Banner informativo para sincronizar calendario.
 */
@Composable
fun BannerSincronizarCalendario(
    modifier: Modifier = Modifier,
    onSincronizarClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = AmbarClaro,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = AmarilloOscuro,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "¿Quieres agregar la actividad a tu calendario?",
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sincroniza las fechas que te interesan con tu calendario personal.",
                    color = TextoGrisActividad,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Ambar,
                onClick = onSincronizarClick
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = AzulOscuro
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sincronizar",
                        color = AzulOscuro,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Leyenda de filtros de categorías.
 */
@Composable
fun LeyendaFiltros(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ItemLeyenda("Medio Ambiente", FiltroVerde)
        ItemLeyenda("Educación", FiltroAzul)
        ItemLeyenda("Apoyo Social", FiltroNaranja)
        ItemLeyenda("Otros", FiltroMorado)
    }
}

@Composable
fun ItemLeyenda(texto: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = texto,
            fontSize = 10.sp,
            color = TextoGrisActividad,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Encabezado con fecha y acción para la agenda semanal.
 */
@Composable
fun HeaderDiaActividades(
    modifier: Modifier = Modifier,
    fechaTexto: String = "Sábado, 24 de Octubre",
    onVerAgendaClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = fechaTexto,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = AzulOscuro
        )
        Surface(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GrisBordeClaro),
            color = Color.White,
            onClick = onVerAgendaClick
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = TextoGrisActividad,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Ver agenda semanal",
                    fontSize = 12.sp,
                    color = TextoGrisActividad,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Tarjeta genérica de información secundaria (p.ej. Ubicación o Fecha y Hora en Detalle).
 */
@Composable
fun TarjetaInfoActividad(
    titulo: String,
    lineaPrincipal: String,
    lineaSecundaria: String,
    icono: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = BeigeClaro,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = AzulOscuro,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = titulo,
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lineaPrincipal,
                fontSize = 12.sp,
                color = TextoGrisActividad
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = lineaSecundaria,
                fontSize = 10.sp,
                color = AzulEnlace,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Tarjeta de información del Organizador de la actividad.
 */
@Composable
fun TarjetaOrganizador(
    nombreOrganizador: String,
    esVerificado: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(FondoVerde, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    tint = TextoVerde
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = nombreOrganizador,
                        fontWeight = FontWeight.Bold,
                        color = AzulOscuro,
                        fontSize = 14.sp
                    )
                    if (esVerificado) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verificado",
                            tint = AzulEnlace,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    text = if (esVerificado) "Organizador verificado" else "Organizador comunitario",
                    color = TextoGrisActividad,
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = { /* Info del organizador */ }) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info organizador",
                    tint = AzulOscuro
                )
            }
        }
    }
}

/**
 * Tarjeta con la barra de plazas disponibles/totales y estado.
 */
@Composable
fun TarjetaPlazasDisponibles(
    disponibles: Int,
    totales: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Plazas disponibles",
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro,
                    fontSize = 14.sp
                )
                Text(
                    text = "$disponibles / $totales",
                    fontWeight = FontWeight.Bold,
                    color = AzulOscuro,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val progreso = if (totales > 0) disponibles.toFloat() / totales.toFloat() else 0f
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = AmarilloOscuro,
                trackColor = GrisBordeClaro
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.GroupAdd,
                    contentDescription = null,
                    tint = AzulOscuro,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (disponibles <= 5) "¡Súmate, quedan pocas plazas!" else "¡Únete con tu familia!",
                    color = TextoGrisActividad,
                    fontSize = 12.sp
                )
            }
        }
    }
}
