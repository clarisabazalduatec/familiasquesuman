package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.theme.GrisTexto

/**
 * Componente reutilizable de tarjeta para mostrar actividades u opciones destacadas.
 * Los campos [categoria], [fecha] e [icono] son opcionales.
 */
@Composable
fun TarjetaActividadDestacada(
    titulo: String,
    descripcion: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    categoria: String? = null,
    fecha: String? = null,
    icono: ImageVector? = null
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (!categoria.isNullOrBlank()) {
                AssistChip(
                    onClick = {},
                    label = { Text(categoria) },
                    enabled = false
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icono != null) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = AzulMarino,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = AzulMarino
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = GrisTexto
            )

            if (!fecha.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = fecha,
                    style = MaterialTheme.typography.labelSmall,
                    color = AzulMarino
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TarjetaActividadDestacadaPreview() {
    FamiliasQueSumanTheme {
        TarjetaActividadDestacada(
            categoria = "Voluntariado",
            titulo = "Reforestación Parque Central",
            descripcion = "Ayúdanos a plantar árboles y restaurar el parque principal de la ciudad.",
            fecha = "Sábado 15 de Octubre - 9:00 AM",
            onClick = {}
        )
    }
}
