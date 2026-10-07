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
import com.example.familiasquesuman.ui.model.AdultoState
import com.example.familiasquesuman.ui.model.MenorState
import com.example.familiasquesuman.ui.theme.*

@Composable
fun DatosFamiliaRegistradaCard(
    uiState: PerfilUiState,
    listaAdultos: List<AdultoState>,
    listaMenores: List<MenorState>,
    onEditarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Encabezado principal
            EncabezadoTarjeta(onEditarClick = onEditarClick)

            Spacer(modifier = Modifier.height(16.dp))

            // Sección Adultos
            SeccionTitulo(titulo = "Adultos registrados")
            Spacer(modifier = Modifier.height(8.dp))

            if (listaAdultos.isEmpty()) {
                TextoVacio(mensaje = "Sin adultos registrados")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listaAdultos.forEach { adulto ->
                        TarjetaMiembroFamilia(
                            titulo = "${adulto.nombre.ifBlank { "Sin nombre" }} (${adulto.rol.ifBlank { "Adulto" }})",
                            subtitulo = adulto.email.takeIf { it.isNotBlank() }?.let { "Email: $it" },
                            icono = Icons.Outlined.Person
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección Menores
            SeccionTitulo(titulo = "Menores registrados")
            Spacer(modifier = Modifier.height(8.dp))

            if (listaMenores.isEmpty()) {
                TextoVacio(mensaje = "Sin menores registrados")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listaMenores.forEach { menor ->
                        TarjetaMiembroFamilia(
                            titulo = menor.nombre.ifBlank { "Menor sin nombre" },
                            subtitulo = menor.fechaNacimiento.takeIf { it.isNotBlank() }?.let { "Nacimiento: $it" },
                            icono = Icons.Outlined.Face
                        )
                    }
                }
            }
        }
    }
}

// Componentes reutilizables
@Composable
private fun EncabezadoTarjeta(onEditarClick: () -> Unit) {
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
}

@Composable
private fun SeccionTitulo(titulo: String) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.titleMedium,
        color = AzulMarino,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun TextoVacio(mensaje: String) {
    Text(
        text = mensaje,
        color = GrisTexto,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun TarjetaMiembroFamilia(
    titulo: String,
    subtitulo: String?,
    icono: ImageVector
) {
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
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Blanco,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold,
                color = AzulMarino
            )
            subtitulo?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto
                )
            }
        }
    }
}