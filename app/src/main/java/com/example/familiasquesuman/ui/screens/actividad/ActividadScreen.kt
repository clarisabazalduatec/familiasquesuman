package com.example.familiasquesuman.ui.screens.actividad

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.ui.components.FiltrosChips
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.actividad.components.BannerSincronizarCalendario
import com.example.familiasquesuman.ui.screens.actividad.components.HeaderDiaActividades
import com.example.familiasquesuman.ui.screens.actividad.components.TarjetaActividadDia
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.theme.GrisBordeClaro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadScreen(navController: NavHostController) {
    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }
    val categorias = remember { listOf("Medio Ambiente", "Educación", "Apoyo Social", "Otros") }

    val actividadesFiltradas = remember(categoriaSeleccionada, actividadesMockData) {
        actividadesMockData.filter { it.activa }.filter { actividad ->
            categoriaSeleccionada == null || actividad.categoria == categoriaSeleccionada
        }
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.ACTIVIDADES,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CremaFondo)
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = "Actividades en Familia",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Actividades en familia para ayudar durante el año.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            item {
                FiltrosChips(
                    opciones = categorias,
                    seleccionado = categoriaSeleccionada,
                    etiquetaPara = { it },
                    onSeleccionado = { categoriaSeleccionada = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
            item {
                HorizontalDivider(
                    color = GrisBordeClaro,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
            }
            item { HeaderDiaActividades() }
            items(actividadesFiltradas) { actividad ->
                TarjetaActividadDia(
                    actividad = actividad,
                    onActividadClick = { id ->
                        navController.navigate(Rutas.ActividadDetalle.crearRuta(id))
                    },
                )
            }
            item { BannerSincronizarCalendario() }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActividadScreenPreview() {
    FamiliasQueSumanTheme {
        ActividadScreen(navController = rememberNavController())
    }
}
