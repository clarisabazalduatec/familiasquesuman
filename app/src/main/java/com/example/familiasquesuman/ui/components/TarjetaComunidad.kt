package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.R
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.theme.GrisTexto

/**
 * Componente de tarjeta de Comunidad reutilizable con pestañas para avisos oficiales y publicaciones.
 */
@Composable
fun TarjetaComunidad(
    onVerComunidad: () -> Unit,
    modifier: Modifier = Modifier,
    titulo: String = "Comunidad",
    subtitulo: String = "Consulta avisos oficiales y\ncomparte experiencias de otras familias.",
    pestanaOficialTexto: String = "Oficial",
    pestanaComunidadTexto: String = "Comunidad",
    anuncioEtiqueta: String = "Anuncio oficial",
    anuncioTitulo: String = "Cambio de horario:",
    anuncioDetalle: String = "Reforestación Parque Central\niniciará a las 9:00 AM.",
    imagenResId: Int = R.drawable.parque_comunidad,
    textoComunidadVacio: String = "Conoce experiencias y publicaciones compartidas por otras familias.",
    textoBotonVerMas: String = "Ver comunidad",
    onChatbot: (() -> Unit)? = null
) {
    var pestanaSeleccionada by remember { mutableStateOf("Oficial") }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFBF5E4)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = AzulMarino,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.titleLarge,
                        color = AzulMarino
                    )
                    Text(
                        text = subtitulo,
                        style = MaterialTheme.typography.bodySmall,
                        color = GrisTexto
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Selector de pestañas
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { pestanaSeleccionada = "Oficial" },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        bottomStart = 20.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (pestanaSeleccionada == "Oficial") AzulMarino else Blanco,
                        contentColor = if (pestanaSeleccionada == "Oficial") Blanco else AzulMarino
                    )
                ) {
                    Text(
                        text = pestanaOficialTexto,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                OutlinedButton(
                    onClick = { pestanaSeleccionada = "Comunidad" },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 20.dp,
                        bottomEnd = 20.dp
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (pestanaSeleccionada == "Comunidad") AzulMarino else Blanco,
                        contentColor = if (pestanaSeleccionada == "Comunidad") Blanco else AzulMarino
                    )
                ) {
                    Text(
                        text = pestanaComunidadTexto,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Contenido de la pestaña
            if (pestanaSeleccionada == "Oficial") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = imagenResId),
                        contentDescription = anuncioTitulo,
                        modifier = Modifier
                            .width(125.dp)
                            .height(105.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Campaign,
                                contentDescription = null,
                                tint = AzulMarino,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = anuncioEtiqueta,
                                style = MaterialTheme.typography.labelSmall,
                                color = AzulMarino
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = anuncioTitulo,
                            style = MaterialTheme.typography.titleSmall,
                            color = AzulMarino
                        )
                        Text(
                            text = anuncioDetalle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrisTexto
                        )
                    }
                }
            } else {
                Text(
                    text = textoComunidadVacio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 28.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onVerComunidad) {
                    Text(
                        text = textoBotonVerMas,
                        style = MaterialTheme.typography.titleSmall,
                        color = AzulMarino
                    )
                    Text(
                        text = "  ›",
                        style = MaterialTheme.typography.titleLarge,
                        color = AzulMarino
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TarjetaComunidadPreview() {
    FamiliasQueSumanTheme {
        TarjetaComunidad(
            onVerComunidad = {}
        )
    }
}
