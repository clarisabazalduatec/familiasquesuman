package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme

@Composable
fun ResultadoPublicacionScreen(navController: NavHostController, fueExitoso: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (fueExitoso) Icons.Default.AccessTime else Icons.Default.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = if (fueExitoso) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (fueExitoso) "¡Gracias por compartir!" else "No pudimos publicar tu experiencia",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (fueExitoso) {
                "Tu publicación está en revisión por nuestro equipo antes de aparecer en el feed de la comunidad."
            } else {
                "Hubo un problema al subir tu publicación. Revisa tu conexión e inténtalo de nuevo."
            },
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (fueExitoso) {
            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = if (fueExitoso) Icons.Default.CalendarToday else Icons.Default.Autorenew,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Ver mis publicaciones")
                    }

            TextButton(onClick = { navController.navigate(Rutas.Comunidad.ruta) { popUpTo(Rutas.Comunidad.ruta) { inclusive = true } } }) {
                Text("Volver a la Comunidad")
            }
        } else {
            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                Text("Reintentar")
            }
            TextButton(onClick = { navController.navigate(Rutas.Comunidad.ruta) { popUpTo(Rutas.Comunidad.ruta) { inclusive = true } } }) {
                Text("Cancelar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResultadoPublicacionScreenPreview() {
    FamiliasQueSumanTheme {
        ResultadoPublicacionScreen(navController = rememberNavController(), fueExitoso = true)
    }
}