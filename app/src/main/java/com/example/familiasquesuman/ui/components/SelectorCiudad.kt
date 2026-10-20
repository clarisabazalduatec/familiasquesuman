package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.CiudadDto
import com.example.familiasquesuman.ui.theme.*

private fun etiquetaCiudad(c: CiudadDto) =
    if (!c.estado.isNullOrBlank()) "${c.nombre}, ${c.estado}" else c.nombre

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorCiudad(
    ciudadIdSeleccionada: Int?,
    onCiudadSeleccionada: (CiudadDto) -> Unit,
    modifier: Modifier = Modifier,
    etiqueta: String = "Ciudad"
) {
    var ciudades by remember { mutableStateOf<List<CiudadDto>>(emptyList()) }
    var expandido by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val lista = RetrofitInstance.api.listarCiudades()
            ciudades = lista
            if (ciudadIdSeleccionada == null && lista.isNotEmpty()) {
                onCiudadSeleccionada(lista.firstOrNull { it.nombre == "Monterrey" } ?: lista.first())
            }
        } catch (_: Exception) {}
    }

    val actual = ciudades.firstOrNull { it.id == ciudadIdSeleccionada }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = actual?.let { etiquetaCiudad(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(etiqueta) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Ambar, unfocusedBorderColor = GrisBorde,
                focusedContainerColor = Blanco, unfocusedContainerColor = Blanco,
                cursorColor = AzulMarino, focusedTextColor = AzulMarino, unfocusedTextColor = AzulMarino
            ),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            ciudades.forEach { c ->
                DropdownMenuItem(
                    text = { Text(etiquetaCiudad(c)) },
                    onClick = { onCiudadSeleccionada(c); expandido = false }
                )
            }
        }
    }
}
