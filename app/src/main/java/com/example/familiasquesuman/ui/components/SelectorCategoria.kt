package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Chips de categorías (en varias líneas si no caben) + un chip "Nueva" para crear otra.
 * No sabe de qué es la categoría: sirve para actividades, donaciones, directorio, etc.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectorCategoria(
    categorias: List<String>,
    seleccionada: String,
    onSeleccionar: (String) -> Unit,
    onNuevaCategoria: (String) -> Unit,
    modifier: Modifier = Modifier,
    titulo: String = "Categoría",
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categorias.forEach { categoria ->
                FilterChip(
                    selected = categoria == seleccionada,
                    onClick = { onSeleccionar(categoria) },
                    label = { Text(categoria) },
                )
            }
            AssistChip(
                onClick = { mostrarDialogo = true },
                label = { Text("Nueva") },
                leadingIcon = { Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp)) },
            )
        }
    }

    if (mostrarDialogo) {
        DialogoNuevaCategoria(
            onConfirmar = {
                onNuevaCategoria(it)
                mostrarDialogo = false
            },
            onCancelar = { mostrarDialogo = false },
        )
    }
}

@Composable
private fun DialogoNuevaCategoria(onConfirmar: (String) -> Unit, onCancelar: () -> Unit) {
    var texto by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Nueva categoría") },
        text = {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Nombre") },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirmar(texto) }, enabled = texto.isNotBlank()) { Text("Agregar") }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text("Cancelar") } },
    )
}
