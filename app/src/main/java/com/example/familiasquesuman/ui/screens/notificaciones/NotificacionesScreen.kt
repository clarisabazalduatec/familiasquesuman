package com.example.familiasquesuman.ui.screens.notificaciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.domain.Notificacion
import com.example.familiasquesuman.domain.TipoNotificacion
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme

private val notificacionesHoy = listOf(
    Notificacion("1", TipoNotificacion.PARTICIPACION, "Mateo en Reforestación...", "Tu hermano participará en la jornada \"Reforestación en Parque Central\".", "10:30 AM", leida = false),
    Notificacion("2", TipoNotificacion.INSIGNIA, "¡Nueva insignia obtenida!", "Has RECICLADO 10 kilos. Sigue haciendo la diferencia.", "9:15 AM", leida = false),
)

private val notificacionesAyer = listOf(
    Notificacion("3", TipoNotificacion.COMENTARIO, "La Familia Crece comentó tu p...", "\"Qué gran iniciativa! Nos encanta ver el impacto de proyectos.\"", "Ayer", leida = true),
    Notificacion("4", TipoNotificacion.DONACION, "Donación Confirmada", "Gracias por tu donación. Tu apoyo hará una diferencia en vidas.", "Ayer", leida = true),
)

private val notificacionesEstaSemana = listOf(
    Notificacion("5", TipoNotificacion.TALLER, "Nuevo Taller Disponible", "Inscríbete en el taller sobre \"Huertos en Casa\" este 12 de Oct a las 4:00 PM.", "Mar", leida = true),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                title = { Text("Notificaciones") }
            )
        }
    ) { paddingInterno ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
        ) {
            item { EncabezadoSeccion("HOY") }
            items(notificacionesHoy) { NotificacionItem(it) }

            item { Spacer(modifier = Modifier.height(8.dp)); EncabezadoSeccion("AYER") }
            items(notificacionesAyer) { NotificacionItem(it) }

            item { Spacer(modifier = Modifier.height(8.dp)); EncabezadoSeccion("ESTA SEMANA") }
            items(notificacionesEstaSemana) { NotificacionItem(it) }
        }
    }
}

@Composable
private fun EncabezadoSeccion(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Preview(showBackground = true)
@Composable
private fun NotificacionesScreenPreview() {
    FamiliasQueSumanTheme {
        NotificacionesScreen(navController = rememberNavController())
    }
}