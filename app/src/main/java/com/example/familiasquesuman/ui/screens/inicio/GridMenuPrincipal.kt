package com.example.familiasquesuman.ui.screens.inicio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.R
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.GrisTexto

private val CremaIcono = Color(0xFFFBF5E4)
data class OpcionMenuPrincipal(
    val titulo: String,
    val subtitulo: String,
    val icono: androidx.compose.ui.graphics.vector.ImageVector,
    val destacada: Boolean = false
)

@Composable
fun GridMenuPrincipal(
    opciones: List<OpcionMenuPrincipal>,
    onOpcionClick: (OpcionMenuPrincipal) -> Unit,
    modifier: Modifier = Modifier
) {
    require(opciones.size == 4) {
        "GridMenuPrincipal espera exactamente 4 opciones"
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // fila1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaMenuPrincipal(
                opcion = opciones[0],
                modifier = Modifier.weight(1f)
            ) {
                onOpcionClick(opciones[0])
            }
            TarjetaMenuPrincipal(
                opcion = opciones[1],
                modifier = Modifier.weight(1f)
            ) {
                onOpcionClick(opciones[1])
            }
        }

        // fila 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaMenuPrincipal(
                opcion = opciones[2],
                modifier = Modifier.weight(1f)
            ) {
                onOpcionClick(opciones[2])
            }

            TarjetaMenuPrincipal(
                opcion = opciones[3],
                modifier = Modifier.weight(1f)
            ) {
                onOpcionClick(opciones[3])
            }
        }
    }
}


@Composable
public fun TarjetaMenuPrincipal(
    opcion: OpcionMenuPrincipal,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val iconoDrawable = when (opcion.titulo) {
        "Actividades en Familia" ->
            R.drawable.actividad_onb
        "Quiero Donar" ->
            R.drawable.donar_onb
        "Proyectos" ->
            R.drawable.proyectos_onb
        "Directorio de Visiteo" ->
            R.drawable.directorio_onb
        else -> null
    }

//4 tarjetas de iconos
    Card(
        onClick = onClick,
        modifier = modifier
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

            //circulo e icono
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(CremaIcono),
                contentAlignment = Alignment.Center
            ) {
                if (iconoDrawable != null) {
                    Image(
                        painter = painterResource(
                            id = iconoDrawable
                        ),
                        contentDescription = opcion.titulo,
                        modifier = Modifier
                            .size(54.dp)
                            .graphicsLayer(
                                scaleX = 1.25f,
                                scaleY = 1.25f
                            ),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Icon(
                        imageVector = opcion.icono,
                        contentDescription = opcion.titulo,
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
                    text = opcion.titulo,
                    style = MaterialTheme.typography.titleSmall,
                    color = AzulMarino,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp)
                )
                Icon(
                    imageVector =
                        Icons.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AzulMarino,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = opcion.subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}