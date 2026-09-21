package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.domain.Publicacion

@Composable
fun TarjetaPublicacion(publicacion: Publicacion, modifier: Modifier = Modifier) {
    Card(shape = RoundedCornerShape(12.dp), modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = publicacion.autor, style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = publicacion.tiempoRelativo,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (publicacion.esOficial) {
                    AssistChip(onClick = {}, label = { Text("Oficial") }, enabled = false)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = publicacion.contenido, style = MaterialTheme.typography.bodyMedium)

            publicacion.imagenUrl?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                // TODO: reemplazar este Box gris por la imagen real cuando conectemos Coil
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                IconoConTexto(icono = Icons.Default.FavoriteBorder, texto = "${publicacion.numeroLikes}")
                IconoConTexto(icono = Icons.Default.ChatBubbleOutline, texto = "${publicacion.numeroComentarios}")
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Default.Share, contentDescription = "Compartir")
            }
        }
    }
}

@Composable
private fun IconoConTexto(icono: androidx.compose.ui.graphics.vector.ImageVector, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icono, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = texto, style = MaterialTheme.typography.labelMedium)
    }
}