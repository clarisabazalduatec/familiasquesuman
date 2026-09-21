package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaPublicacionScreen(navController: NavHostController) {
    var contenido by remember { mutableStateOf("") }
    var tieneEvidenciaAdjunta by remember { mutableStateOf(false) } // simula "con datos" / "sin datos"
    var estaEnviando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Cancelar")
                    }
                },
                title = { Text("Comunidad en Acción") }
            )
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Comparte y celebra el impacto de nuestra comunidad.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contenido,
                onValueChange = { contenido = it },
                label = { Text("¿Qué sumaste hoy?") },
                placeholder = { Text("Cuéntanos cómo fue tu experiencia...") },
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Zona de evidencia — "Con datos" (foto ya adjunta) vs "Sin datos" (vacío, invita a agregar)
            if (tieneEvidenciaAdjunta) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.TopEnd
                ) {
                    // TODO: mostrar la imagen real seleccionada cuando conectemos selector de imágenes
                    IconButton(onClick = { tieneEvidenciaAdjunta = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Quitar evidencia")
                    }
                }
            } else {
                OutlinedCard(
                    onClick = { tieneEvidenciaAdjunta = true }, // simulado por ahora
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = null)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Agregar evidencia (foto)", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    estaEnviando = true
                },
                enabled = contenido.isNotBlank() && !estaEnviando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (estaEnviando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Publicar")
                }
            }
        }
    }

    // Simulación temporal de red mientras no existe el endpoint real — se reemplaza por el repository/ViewModel real después
    LaunchedEffect(estaEnviando) {
        if (estaEnviando) {
            delay(1200)
            val fueExitoso = contenido.length > 5 // regla falsa solo para poder probar ambos caminos
            navController.navigate(Rutas.ResultadoPublicacion.crearRuta(fueExitoso)) {
                popUpTo(Rutas.Comunidad.ruta)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NuevaPublicacionScreenPreview() {
    FamiliasQueSumanTheme {
        NuevaPublicacionScreen(navController = rememberNavController())
    }
}