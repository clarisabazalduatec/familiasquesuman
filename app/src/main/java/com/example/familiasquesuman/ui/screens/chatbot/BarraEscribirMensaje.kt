package com.example.familiasquesuman.ui.screens.chatbot

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BarraEscribirMensaje(
    texto: String,
    onTextoChange: (String) -> Unit,
    onEnviarClick: () -> Unit,
    habilitado: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = texto,
            onValueChange = onTextoChange,
            placeholder = { Text("Escribe un mensaje...") },
            modifier = Modifier.weight(1f),
            maxLines = 4,
            enabled = habilitado
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onEnviarClick,
            enabled = habilitado && texto.isNotBlank()
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Enviar")
        }
    }
}