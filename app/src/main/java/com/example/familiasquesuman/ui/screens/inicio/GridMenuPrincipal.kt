package com.example.familiasquesuman.ui.screens.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class OpcionMenuPrincipal(
    val titulo: String,
    val subtitulo: String,
    val icono: ImageVector,
    val destacada: Boolean = false
)

@Composable
fun GridMenuPrincipal(
    opciones: List<OpcionMenuPrincipal>,
    onOpcionClick: (OpcionMenuPrincipal) -> Unit,
    modifier: Modifier = Modifier
) {
    require(opciones.size == 4) { "GridMenuPrincipal espera exactamente 4 opciones" }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TarjetaMenuPrincipal(opciones[0], Modifier.weight(1f)) { onOpcionClick(opciones[0]) }
            TarjetaMenuPrincipal(opciones[1], Modifier.weight(1f)) { onOpcionClick(opciones[1]) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            TarjetaMenuPrincipal(opciones[2], Modifier.weight(1f)) { onOpcionClick(opciones[2]) }
            TarjetaMenuPrincipal(opciones[3], Modifier.weight(1f)) { onOpcionClick(opciones[3]) }
        }
    }
}

@Composable
private fun TarjetaMenuPrincipal(
    opcion: OpcionMenuPrincipal,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (opcion.destacada) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Icon(imageVector = opcion.icono, contentDescription = null)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = opcion.titulo, style = MaterialTheme.typography.titleSmall)
            Text(
                text = opcion.subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}