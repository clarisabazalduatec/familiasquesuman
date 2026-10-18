package com.example.familiasquesuman.ui.screens.chatbot

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/** Burbuja con tres puntos animados mientras el asistente prepara la respuesta. */
@Composable
fun BurbujaPensando(modifier: Modifier = Modifier) {
    val transicion = rememberInfiniteTransition(label = "pensando")
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.semantics { contentDescription = "El asistente está escribiendo" },
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(3) { i ->
                    // Cada punto sube y se aclara con un pequeño desfase: efecto de "escribiendo..."
                    val progreso by transicion.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 450),
                            repeatMode = RepeatMode.Reverse,
                            initialStartOffset = StartOffset(i * 150),
                        ),
                        label = "punto$i",
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .graphicsLayer {
                                translationY = -4.dp.toPx() * progreso
                                alpha = 0.35f + 0.65f * progreso
                            }
                            .background(MaterialTheme.colorScheme.onSurfaceVariant, CircleShape),
                    )
                }
            }
        }
    }
}