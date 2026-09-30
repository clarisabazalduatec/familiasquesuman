package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.ui.screens.perfil.model.HijoState
import com.example.familiasquesuman.ui.theme.*

@Composable
fun DatosFamiliaRegistradaCard(
    uiState: PerfilUiState,
    listaHijos: List<HijoState>,
    onEditarClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Encabezado con botón Editar
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
                TextButton(onClick = onEditarClick) {
                    Text(
                        text = "Editar",
                        color = Ambar,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información guardada
            DatoFamiliarItem(icono = Icons.Outlined.Person, etiqueta = "Mamá", valor = uiState.mama.ifBlank { "No registrado" })
            DatoFamiliarItem(icono = Icons.Outlined.Person, etiqueta = "Papá", valor = uiState.papa.ifBlank { "No registrado" })
            DatoFamiliarItem(icono = Icons.Outlined.Phone, etiqueta = "WhatsApp", valor = uiState.whatsapp.ifBlank { "No registrado" })
            DatoFamiliarItem(icono = Icons.Outlined.Email, etiqueta = "Email", valor = uiState.email.ifBlank { "No registrado" })
            DatoFamiliarItem(icono = Icons.Outlined.LocationOn, etiqueta = "Ciudad", valor = uiState.ciudad.ifBlank { "No seleccionada" })

            Spacer(modifier = Modifier.height(16.dp))

            // Sección Hijos
            Text(
                text = "Hijos registrados",
                style = MaterialTheme.typography.titleMedium,
                color = AzulMarino,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (listaHijos.isEmpty()) {
                Text(text = "Sin hijos registrados", color = GrisTexto, style = MaterialTheme.typography.bodyMedium)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listaHijos.forEach { hijo ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(AmbarClaro.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Ambar, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Outlined.Face, contentDescription = null, tint = Blanco, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = hijo.nombre.ifBlank { "Hijo sin nombre" },
                                    fontWeight = FontWeight.Bold,
                                    color = AzulMarino
                                )
                                if (hijo.fechaNacimiento.isNotBlank()) {
                                    Text(
                                        text = "Nacimiento: ${hijo.fechaNacimiento}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GrisTexto
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DatoFamiliarItem(icono: ImageVector, etiqueta: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = GrisTexto, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "$etiqueta: ", color = GrisTexto, fontWeight = FontWeight.Medium)
        Text(text = valor, color = AzulMarino, fontWeight = FontWeight.SemiBold)
    }
}