package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> FiltrosChips(
    opciones: List<T>,
    seleccionado: T?,
    etiquetaPara: (T) -> String,
    onSeleccionado: (T?) -> Unit,
    etiquetaTodos: String = "Todos",
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        FilterChip(
            selected = seleccionado == null,
            onClick = { onSeleccionado(null) },
            label = { Text(etiquetaTodos) }
        )
        opciones.forEach { opcion ->
            FilterChip(
                selected = seleccionado == opcion,
                onClick = { onSeleccionado(opcion) },
                label = { Text(etiquetaPara(opcion)) }
            )
        }
    }
}