package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.ui.theme.*

@Composable
fun MisInsigniasCard() {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Insignias",
                    tint = AzulMarino
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mis insignias",
                    style = MaterialTheme.typography.titleLarge,
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "¡Participa y gana estrellas en tus proyectos, donaciones y actividades!",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InsigniaItem(titulo = "Proyectos", valor = "0")
                InsigniaItem(titulo = "Donaciones", valor = "0")
                InsigniaItem(titulo = "Actividades", valor = "0")
            }
        }
    }
}

@Composable
private fun InsigniaItem(titulo: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(AmbarClaro, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Ambar,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = titulo, style = MaterialTheme.typography.labelMedium, color = AzulMarino)
        Text(text = valor, style = MaterialTheme.typography.titleMedium, color = AzulMarino, fontWeight = FontWeight.Bold)
    }
}