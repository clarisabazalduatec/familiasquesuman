package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familiasquesuman.data.remote.dto.InsigniaDto
import com.example.familiasquesuman.ui.theme.*

@Composable
fun MisInsigniasCard(
    insignias: List<InsigniaDto> = emptyList()
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Insignias",
                    tint = Ambar,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mis insignias",
                    style = MaterialTheme.typography.titleLarge,
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "¡Cada estrella es una medalla por tus actividades comunitarias concluidas!",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (insignias.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CremaFondo, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Aún no tienes insignias. ¡Participa y concluye actividades para ganar tu primera medalla!",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrisTexto,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp)
                ) {
                    items(insignias) { insignia ->
                        InsigniaItem(
                            titulo = insignia.tituloActividad,
                            categoria = insignia.categoria?.takeIf { !it.startsWith("<") } ?: "General"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InsigniaItem(titulo: String, categoria: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(76.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = AmbarClaro,
            border = BorderStroke(1.5.dp, Ambar),
            modifier = Modifier
                .size(58.dp)
                .shadow(elevation = 2.dp, shape = CircleShape)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Ambar,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = titulo,
            style = MaterialTheme.typography.labelMedium,
            color = AzulMarino,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Text(
            text = categoria,
            style = MaterialTheme.typography.bodySmall,
            color = GrisTexto,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}