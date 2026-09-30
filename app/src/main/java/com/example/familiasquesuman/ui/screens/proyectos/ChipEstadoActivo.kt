package com.example.familiasquesuman.ui.screens.proyectos

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familiasquesuman.ui.theme.VerdeExito
import com.example.familiasquesuman.ui.theme.VerdeExitoClaro

@Composable
fun ChipEstadoActivo(
    modifier: Modifier = Modifier,
    texto: String = "Activo"
) {
    AssistChip(
        onClick = {},
        enabled = false,
        label = {
            Text(
                text = texto,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = AssistChipDefaults.assistChipColors(
            disabledContainerColor = VerdeExitoClaro,
            disabledLabelColor = VerdeExito
        ),
        border = null,
        modifier = modifier
    )
}
