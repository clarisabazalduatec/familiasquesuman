package com.example.familiasquesuman.ui.screens.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.ui.components.BarraNavegacionInferior
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme

// Tarjeta de Actividad Destacada

@Composable
fun TarjetaActividadDestacada(
    categoria: String,
    titulo: String,
    descripcion: String,
    fecha: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AssistChip(
                onClick = {},
                label = { Text(categoria) },
                enabled = false
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = titulo, style = MaterialTheme.typography.titleMedium)
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = fecha, style = MaterialTheme.typography.labelSmall)
        }
    }
}

// Sección de Tu Impacto

@Composable
fun SeccionTuImpacto(
    numeroActividades: Int,
    horasDonadas: Int,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tu Impacto",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Has participado en 3 proyectos este mes.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                EstadisticaImpacto(valor = "$numeroActividades", etiqueta = "Actividades")
                EstadisticaImpacto(valor = "$$horasDonadas", etiqueta = "Horas donadas")
            }
        }
    }
}

@Composable
private fun EstadisticaImpacto(valor: String, etiqueta: String) {
    Column {
        Text(text = valor, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onPrimary)
        Text(text = etiqueta, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary)
    }
}

// Screen de inicio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, contentDescription = "Menú") } },
                title = { Text("Familias que Suman+") },
                actions = { IconButton(onClick = {}) { Icon(Icons.Default.Notifications, contentDescription = "Notificaciones") } }
            )
        },
        bottomBar = {
            BarraNavegacionInferior(
                pantallaActual = PantallaPrincipal.INICIO,
                onPantallaSeleccionada = { /* navegación real, la agregamos después */ }
            )
        }
    ) { paddingInterno ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
        ) {
            item {
                SaludoConUbicacion(nombreUsuario = "Mariana", ciudad = "Monterrey, NL")
            }
            item {
                GridMenuPrincipal(
                    opciones = listOf(
                        OpcionMenuPrincipal("Actividades en Familia", "Actividades en familia para ayudar durante el año.", Icons.Default.Groups),
                        OpcionMenuPrincipal("Quiero Donar", "Apoyo en especie y tiempo.", Icons.Default.Favorite),
                        OpcionMenuPrincipal("Proyectos", "Proyectos con causas y objetivos específicos.", Icons.Default.LightbulbCircle),
                        OpcionMenuPrincipal("Directorio de Visiteo", "Centros y espacios para visitar y apoyar en familia.", Icons.Default.Place, destacada = true),
                    ),
                    onOpcionClick = { /* navegación real, la agregamos después */ }
                )
            }
            item {
                TarjetaActividadDestacada(
                    categoria = "Educación",
                    titulo = "Lectura para Niños",
                    descripcion = "Apoya como voluntario en el círculo de lectura comunitaria.",
                    fecha = "Sábado, 10:00 AM",
                    onClick = {}
                )
            }
            item {
                SeccionTuImpacto(numeroActividades = 12, horasDonadas = 500)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InicioScreenPreview() {
    FamiliasQueSumanTheme {
        InicioScreen()
    }
}