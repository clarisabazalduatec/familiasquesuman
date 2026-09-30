package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
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

private val CremaIcono = Color(0xFFFBF5E4)

data class OpcionMenuPrincipal(
    val titulo: String,
    val subtitulo: String,
    val icono: ImageVector,
    val destacada: Boolean = false
)

/**
 * Componente de tarjeta para opciones en cuadrícula (grid 2x2).
 * Soporta íconos vectoriales o recursos de imagen drawable.
 */
@Composable
fun TarjetaMenuPrincipal(
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    iconoDrawable: Int? = null,
    colorFondoIcono: Color = CremaIcono,
    mostrarFlecha: Boolean = true
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(182.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(colorFondoIcono),
                contentAlignment = Alignment.Center
            ) {
                if (iconoDrawable != null) {
                    Image(
                        painter = painterResource(id = iconoDrawable),
                        contentDescription = titulo,
                        modifier = Modifier
                            .size(54.dp)
                            .graphicsLayer(
                                scaleX = 1.25f,
                                scaleY = 1.25f
                            ),
                        contentScale = ContentScale.Fit
                    )
                } else if (icono != null) {
                    Icon(
                        imageVector = icono,
                        contentDescription = titulo,
                        tint = AzulMarino,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(7.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleSmall,
                    color = AzulMarino,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = if (mostrarFlecha) 22.dp else 0.dp)
                )
                if (mostrarFlecha) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        tint = AzulMarino,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Sobrecarga que acepta un objeto [OpcionMenuPrincipal].
 */
@Composable
fun TarjetaMenuPrincipal(
    opcion: OpcionMenuPrincipal,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val iconoDrawable = when (opcion.titulo) {
        "Actividades en Familia" -> R.drawable.actividad_onb
        "Quiero Donar" -> R.drawable.donar_onb
        "Proyectos" -> R.drawable.proyectos_onb
        "Directorio de Visiteo" -> R.drawable.directorio_onb
        else -> null
    }

    TarjetaMenuPrincipal(
        titulo = opcion.titulo,
        subtitulo = opcion.subtitulo,
        icono = opcion.icono,
        iconoDrawable = iconoDrawable,
        onClick = onClick,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun TarjetaMenuPrincipalPreview() {
    FamiliasQueSumanTheme {
        TarjetaMenuPrincipal(
            titulo = "Actividades en Familia",
            subtitulo = "Actividades en familia para ayudar durante el año.",
            iconoDrawable = R.drawable.actividad_onb,
            onClick = {}
        )
    }
}
