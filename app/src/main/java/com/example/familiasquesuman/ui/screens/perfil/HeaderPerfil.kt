package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.ui.components.FotoPerfil
import com.example.familiasquesuman.ui.theme.AzulMarino

@Composable
fun HeaderPerfil(
    nombreUsuario: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FotoPerfil(
            fotoUrl = SesionManager.usuario?.fotoUrl,
            nombre = nombreUsuario,
            tamano = 56.dp
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = nombreUsuario,
            style = MaterialTheme.typography.headlineSmall,
            color = AzulMarino,
            fontWeight = FontWeight.Bold
        )
    }
}