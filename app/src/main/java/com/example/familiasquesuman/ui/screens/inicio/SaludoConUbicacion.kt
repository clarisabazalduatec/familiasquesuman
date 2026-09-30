package com.example.familiasquesuman.ui.screens.inicio

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.GrisBorde
import com.example.familiasquesuman.ui.theme.GrisTexto
import com.example.familiasquesuman.R
import androidx.compose.foundation.Image


@Composable
fun SaludoConUbicacion(
    nombreUsuario: String,
    ciudad: String,
    modifier: Modifier = Modifier
) {

    // tiene como determinado MTY
    val ciudadInicial = when {
        ciudad.contains("Monterrey", ignoreCase = true) ->
            "Monterrey, N.L."
        ciudad.contains("Hermosillo", ignoreCase = true) ->
            "Hermosillo, Son."
        else -> ciudad
    }

    var ciudadSeleccionada by rememberSaveable { mutableStateOf(ciudadInicial) }
    var menuExpandido by rememberSaveable { mutableStateOf(false) }
    val ciudades = listOf("Monterrey, N.L.", "Hermosillo, Son.")

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically)
    {
        //foto de perfil
        Image(
            painter = painterResource(
                id = R.drawable.mariana
            ),
            contentDescription = "Foto de Mariana",
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = "¡Hola, $nombreUsuario!",
                style = MaterialTheme.typography.titleLarge,
                color = AzulMarino
            )

            // ubicacion
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { menuExpandido = true },
                    verticalAlignment = Alignment.CenterVertically)
                {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = GrisTexto,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = ciudadSeleccionada,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrisTexto
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Cambiar ciudad",
                        tint = AzulMarino,
                        modifier = Modifier.size(20.dp)
                    )
                }

                //menu de ciudades
                DropdownMenu(expanded = menuExpandido, onDismissRequest =
                    { menuExpandido = false },
                    offset = DpOffset(
                        x = 0.dp, y = 6.dp),
                    containerColor = Blanco,
                    tonalElevation = 0.dp,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .width(215.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(width = 1.dp, color = GrisBorde,
                            shape = RoundedCornerShape(14.dp))
                ) {
                    ciudades.forEach { ciudadOpcion ->
                        val seleccionada = ciudadSeleccionada == ciudadOpcion
                        val interactionSource = remember { MutableInteractionSource() }
                        Row( modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    ciudadSeleccionada = ciudadOpcion
                                    menuExpandido = false
                                }
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically) {

                            Text(
                                text = ciudadOpcion,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight =
                                    if (seleccionada) { FontWeight.SemiBold }
                                    else { FontWeight.Normal },
                                color =
                                    if (seleccionada) {
                                        AzulMarino }
                                    else {
                                        GrisTexto },
                                modifier = Modifier.weight(1f))
                            if (seleccionada) {
                                Icon(imageVector = Icons.Default.Check,
                                    contentDescription = "Seleccionada",
                                    tint = AzulMarino,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}