package com.example.familiasquesuman.ui.components

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

/**
 * Botón "Cómo llegar": abre el link de Google Maps guardado (campo `url`) o
 * busca la dirección en Google Maps usando la dirección dada (campo `direccion`).
 * Si ambos vienen vacíos no se dibuja nada.
 */
@Composable
fun BotonComoLlegar(
    modifier: Modifier = Modifier,
    url: String? = null,
    direccion: String? = null
) {
    val context = LocalContext.current
    val targetUrl = remember(url, direccion) {
        when {
            !url.isNullOrBlank() -> url.trim()
            !direccion.isNullOrBlank() -> "https://www.google.com/maps/search/?api=1&query=${Uri.encode(direccion.trim())}"
            else -> null
        }
    }

    if (targetUrl == null) return

    Button(
        onClick = {
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, targetUrl.toUri()))
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(context, "No hay una app para abrir el mapa", Toast.LENGTH_SHORT).show()
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2563EB),
            contentColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Icon(
            imageVector = Icons.Default.NearMe,
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
