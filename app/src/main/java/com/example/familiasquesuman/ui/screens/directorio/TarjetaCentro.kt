package com.example.familiasquesuman.ui.screens.directorio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.familiasquesuman.domain.CentroVisiteo

@Composable
fun TarjetaCentro(
    centro: CentroVisiteo,
    onComoAyudarClick: () -> Unit,
    onVerDetallesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(shape = RoundedCornerShape(12.dp), modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.Top) {


                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    AssistChip(onClick = {}, label = { Text(centro.tipo.etiqueta) }, enabled = false)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = centro.nombre, style = MaterialTheme.typography.titleMedium)
                }

                Icon(
                    Icons.Default.Shield,
                    contentDescription = "Verificado",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = centro.descripcionCorta,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Información general", style = MaterialTheme.typography.labelLarge)
            Text(
                text = centro.informacionGeneral,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Necesidades (${centro.necesidades.size})", style = MaterialTheme.typography.labelLarge)
            centro.necesidades.forEach { necesidad ->
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(text = "•  ", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = necesidad,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = centro.direccion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onComoAyudarClick, modifier = Modifier.weight(1f)) {
                    Text("Cómo ayudar")
                }
                Button(onClick = onVerDetallesClick, modifier = Modifier.weight(1f)) {
                    Text("Ver detalles")
                }
            }
        }
    }
}

@Composable
private fun LogoCentro(logoUrl: String?) {
    if (logoUrl != null) {
        AsyncImage(
            model = logoUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
    } else {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}