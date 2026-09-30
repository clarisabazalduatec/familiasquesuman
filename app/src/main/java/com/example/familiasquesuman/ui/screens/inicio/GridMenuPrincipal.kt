package com.example.familiasquesuman.ui.screens.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.ui.components.OpcionMenuPrincipal
import com.example.familiasquesuman.ui.components.TarjetaMenuPrincipal

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
        // fila 1
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
