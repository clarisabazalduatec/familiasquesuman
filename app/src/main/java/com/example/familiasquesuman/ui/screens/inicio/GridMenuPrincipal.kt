package com.example.familiasquesuman.ui.screens.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.height(220.dp)
    ) {
        items(opciones) { opcion ->
            TarjetaMenuPrincipal(opcion = opcion, onClick = { onOpcionClick(opcion) })
        }
    }
}

@Composable
private fun TarjetaMenuPrincipal(
    opcion: OpcionMenuPrincipal,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (opcion.destacada) {
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
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