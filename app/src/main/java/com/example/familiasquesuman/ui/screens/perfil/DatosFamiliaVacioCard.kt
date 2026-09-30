package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.ui.theme.*

@Composable
fun DatosFamiliaVacioCard(
    onRegistrarClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Datos de la familia",
                    style = MaterialTheme.typography.titleLarge,
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onRegistrarClick) {
                    Text(
                        text = "Editar",
                        color = GrisTexto,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(AmbarClaro, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FamilyRestroom,
                        contentDescription = "Familia",
                        modifier = Modifier.size(40.dp),
                        tint = AzulMarino
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Aún no has registrado tu perfil familiar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onRegistrarClick,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Registrar mi familia",
                        color = Blanco,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}