package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.CiudadDto
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.GrisBorde

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorCiudad(
    valorSeleccionado: String,
    onCiudadSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier,
    etiqueta: String = "Ubicación / Ciudad"
) {
    var ciudades by remember { mutableStateOf<List<CiudadDto>>(emptyList()) }
    var expandido by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val lista = RetrofitInstance.api.listarCiudades()
            ciudades = lista
            if (valorSeleccionado.isBlank() && lista.isNotEmpty()) {
                val primera = lista.first()
                val textoPrimera = if (!primera.estado.isNullOrBlank()) "${primera.nombre}, ${primera.estado}" else primera.nombre
                onCiudadSeleccionada(textoPrimera)
            }
        } catch (_: Exception) {
        }
    }

    val opciones = ciudades.map { if (!it.estado.isNullOrBlank()) "${it.nombre}, ${it.estado}" else it.nombre }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = valorSeleccionado,
            onValueChange = {},
            readOnly = true,
            label = { Text(etiqueta) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Ambar,
                unfocusedBorderColor = GrisBorde,
                focusedContainerColor = Blanco,
                unfocusedContainerColor = Blanco,
                cursorColor = AzulMarino,
                focusedTextColor = AzulMarino,
                unfocusedTextColor = AzulMarino
            ),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onCiudadSeleccionada(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}
