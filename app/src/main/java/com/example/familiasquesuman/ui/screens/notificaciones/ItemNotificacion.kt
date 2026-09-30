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
import com.example.familiasquesuman.domain.Notificacion
import com.example.familiasquesuman.domain.TipoNotificacion
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino

private fun iconoPara(tipo: TipoNotificacion): ImageVector = when (tipo) {
    TipoNotificacion.PARTICIPACION -> Icons.Default.Person
    TipoNotificacion.INSIGNIA -> Icons.Default.Star
    TipoNotificacion.COMENTARIO -> Icons.AutoMirrored.Filled.Chat
    TipoNotificacion.DONACION -> Icons.Default.VolunteerActivism
    TipoNotificacion.TALLER -> Icons.Default.Event
}

@Composable
fun NotificacionItem(notificacion: Notificacion, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
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
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = notificacion.tiempo,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notificacion.descripcion,
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
