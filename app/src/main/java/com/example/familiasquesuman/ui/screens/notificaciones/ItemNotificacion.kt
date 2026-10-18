package com.example.familiasquesuman.ui.screens.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.data.remote.dto.NotificacionDto
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco

private fun iconoPara(tipo: String): ImageVector = when (tipo) {
    "PUBLICACION_APROBADA" -> Icons.Default.CheckCircle
    "PUBLICACION_RECHAZADA" -> Icons.Default.Cancel
    "NUEVO_COMENTARIO" -> Icons.AutoMirrored.Filled.Chat
    "NUEVOS_LIKES" -> Icons.Default.Favorite
    "INVITACION_FAMILIA" -> Icons.Default.GroupAdd
    "AVISO_OFICIAL" -> Icons.Default.Campaign
    "RECORDATORIO_ACTIVIDAD" -> Icons.Default.Event
    "PUBLICACION_POR_REVISAR" -> Icons.Default.RateReview
    "ACTIVIDAD_LLENA" -> Icons.Default.EventBusy
    "LUGAR_CONFIRMADO" -> Icons.Default.CheckCircle
    else -> Icons.Default.Notifications
}

@Composable
fun NotificacionItem(
    notificacion: NotificacionDto,
    tiempo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            // Las no leídas resaltan un poco
            containerColor = if (notificacion.leida) Blanco else AmbarClaro.copy(alpha = 0.45f),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(AmbarClaro),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = iconoPara(notificacion.tipo),
                    contentDescription = null,
                    tint = AzulMarino,
                    modifier = Modifier.size(20.dp),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = notificacion.titulo,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (notificacion.leida) FontWeight.SemiBold else FontWeight.Bold,
                        color = AzulMarino,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tiempo,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notificacion.cuerpo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            if (!notificacion.leida) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Ambar),
                )
            }
        }
    }
}