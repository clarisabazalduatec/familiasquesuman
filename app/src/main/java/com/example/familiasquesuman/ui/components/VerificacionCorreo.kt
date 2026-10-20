package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MarkEmailUnread
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.GrisTexto

/**
 * Para las acciones que piden correo verificado (inscribirse, publicar, comentar).
 * Uso:
 *   val conCorreoVerificado = rememberGuardiaCorreoVerificado(navController)
 *   Button(onClick = { conCorreoVerificado { navegarAInscribirse() } })
 * Si el correo ya está verificado ejecuta la acción; si no, muestra un aviso con botón "Verificar ahora".
 */
@Composable
fun rememberGuardiaCorreoVerificado(navController: NavHostController): (() -> Unit) -> Unit {
    var mostrarAviso by remember { mutableStateOf(false) }

    if (mostrarAviso) {
        AlertDialog(
            onDismissRequest = { mostrarAviso = false },
            icon = { Icon(Icons.Outlined.MarkEmailUnread, contentDescription = null, tint = AzulMarino) },
            title = { Text("Verifica tu correo", color = AzulMarino) },
            text = {
                Text(
                    "Para inscribirte a actividades, publicar o comentar necesitamos confirmar que " +
                            "${SesionManager.usuario?.email ?: "tu correo"} es tuyo. Te mandaremos un código de 6 dígitos.",
                    color = GrisTexto
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarAviso = false
                        navController.navigate(Rutas.VerificarCorreo.ruta)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Blanco)
                ) { Text("Verificar ahora") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarAviso = false }) { Text("Ahora no", color = AzulMarino) }
            }
        )
    }

    return { accion ->
        if (SesionManager.correoVerificado) accion() else mostrarAviso = true
    }
}

/** Tarjeta para el Perfil mientras el correo no esté verificado. */
@Composable
fun TarjetaVerificarCorreo(onVerificar: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = AmbarClaro,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.MarkEmailUnread, contentDescription = null, tint = AzulMarino,
                modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Verifica tu correo", fontWeight = FontWeight.Bold, color = AzulMarino)
                Text(
                    "Lo necesitas para inscribirte, publicar y comentar.",
                    style = MaterialTheme.typography.bodySmall, color = GrisTexto
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onVerificar,
                contentPadding = PaddingValues(horizontal = 14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Blanco)
            ) { Text("Verificar") }
        }
    }
}
