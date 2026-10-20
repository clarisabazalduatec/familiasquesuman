package com.example.familiasquesuman.ui.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.familiasquesuman.ui.theme.AzulMarino

/**
 * Botón "Cómo llegar": abre el link de Google Maps que guardó el admin (campo `ubicacion`).
 * Si el link viene vacío no se dibuja nada, así que se puede poner sin preguntar antes.
 */
@Composable
fun BotonComoLlegar(
    url: String?,
    modifier: Modifier = Modifier
) {
    if (url.isNullOrBlank()) return
    val context = LocalContext.current

    Button(
        onClick = {
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, url.trim().toUri()))
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(context, "No hay una app para abrir el mapa", Toast.LENGTH_SHORT).show()
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulMarino,
            contentColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Cómo llegar",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Cómo llegar",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
