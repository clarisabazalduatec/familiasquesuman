package com.example.familiasquesuman.ui.screens.chatbot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.domain.EnlaceChat
import com.example.familiasquesuman.domain.MensajeChat
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino

@Composable
fun BurbujaMensaje(
    mensaje: MensajeChat,
    modifier: Modifier = Modifier,
    onEnlaceClick: (EnlaceChat) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (mensaje.esDelUsuario) Alignment.End else Alignment.Start,
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (mensaje.esDelUsuario) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            // Los mensajes del asistente traen **negritas** y viñetas de markdown: se dibujan bonitas.
            val texto = remember(mensaje.contenido, mensaje.esDelUsuario) {
                if (mensaje.esDelUsuario) AnnotatedString(mensaje.contenido) else formatearMarkdown(mensaje.contenido)
            }
            Text(
                text = texto,
                color = if (mensaje.esDelUsuario) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }

        // Botones para abrir lo que mencionó el asistente
        if (mensaje.enlaces.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(top = 6.dp).widthIn(max = 280.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                mensaje.enlaces.forEach { enlace ->
                    AssistChip(
                        onClick = { onEnlaceClick(enlace) },
                        label = {
                            Text(
                                text = enlace.titulo,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.SemiBold,
                            )
                        },
                        leadingIcon = { Icon(iconoDeEnlace(enlace.tipo), contentDescription = null, modifier = Modifier.size(18.dp)) },
                        trailingIcon = { Icon(Icons.Default.ChevronRight, contentDescription = "Abrir", modifier = Modifier.size(18.dp)) },
                        shape = RoundedCornerShape(12.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = AmbarClaro,
                            labelColor = AzulMarino,
                            leadingIconContentColor = AzulMarino,
                            trailingIconContentColor = AzulMarino,
                        ),
                        border = null,
                    )
                }
            }
        }
    }
}

private fun iconoDeEnlace(tipo: String): ImageVector = when (tipo) {
    "actividad" -> Icons.Default.Event
    "proyecto" -> Icons.Default.Favorite
    "donacion" -> Icons.Default.VolunteerActivism
    "centro" -> Icons.Default.Home
    else -> Icons.Default.ChevronRight
}

/**
 * Markdown mínimo que usa el asistente: **negritas** y viñetas ("* " o "- " al inicio de línea).
 * Todo lo demás se muestra tal cual.
 */
fun formatearMarkdown(texto: String): AnnotatedString = buildAnnotatedString {
    val lineas = texto.trim().lines()
    lineas.forEachIndexed { i, lineaOriginal ->
        var linea = lineaOriginal
        val vineta = Regex("^\\s*[*-]\\s+").find(linea)
        if (vineta != null) {
            append("•  ")
            linea = linea.substring(vineta.range.last + 1)
        }
        // Partes alternas: fuera / dentro de ** **
        linea.split("**").forEachIndexed { j, parte ->
            if (j % 2 == 1) withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(parte) } else append(parte)
        }
        if (i < lineas.lastIndex) append("\n")
    }
}