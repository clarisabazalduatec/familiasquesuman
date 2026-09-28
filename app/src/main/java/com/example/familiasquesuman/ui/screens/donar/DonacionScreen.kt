package com.example.familiasquesuman.ui.screens.donar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.TipoDonacion
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.GrisBorde
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.components.BarraNavegacionInferior
import com.example.familiasquesuman.ui.components.MenuLateral
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonacionScreen(
    navController: NavHostController,
    viewModel: DonacionViewModel = viewModel()
) {
    val donaciones by viewModel.donaciones.collectAsState()
    val tabSeleccionada by viewModel.tabSeleccionada.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val listaFiltros = listOf("Todos", "Juguetes", "Ropa", "Alimentos", "Higiene", "Electrónicos", "Salud", "Útiles")

    val donacionesAMostrar = donaciones.filter { donacion ->
        if (tabSeleccionada == 0) {
            donacion.tipo == TipoDonacion.CAMPANA // Si es campaña, mostramos normal
        } else {
            // Si es artículo, revisamos que coincida con el filtro elegido
            val esArticulo = donacion.tipo == TipoDonacion.ARTICULO
            val coincideFiltro = categoriaSeleccionada == "Todos" || donacion.categoria == categoriaSeleccionada
            esArticulo && coincideFiltro
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        acciones = {
            IconButton(onClick = { navController.navigate(Rutas.Notificaciones.ruta) }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
            }
        }
    )  { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // <- Respeta el espacio de las barras
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    EncabezadoDonacion()
                }
                item {
                    SelectorDeTabs(tabSeleccionada) { nuevaTab ->
                        viewModel.cambiarTab(nuevaTab)
                    }
                }

                if (tabSeleccionada == 1) {
                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre cada botoncito
                        ) {
                            items(listaFiltros) { filtro ->
                                val seleccionado = filtro == categoriaSeleccionada
                                FilterChip(
                                    selected = seleccionado,
                                    onClick = { viewModel.seleccionarCategoria(filtro) },
                                    label = { Text(filtro) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary, // Azul marino sí está seleccionado
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                items(donacionesAMostrar) { donacion ->
                    TarjetaDonacion(
                        donacion = donacion,
                        onDonarClick = {
                            navController.navigate("detalle_donacion/${donacion.id}")
                        }
                    )
                }
            }
        }
    }


@Composable
fun EncabezadoDonacion() {
    Column(modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)) {
        Text(
            text = "Tu apoyo\ntransforma vidas",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Cada donación es un puente hacia un futuro mejor para las familias de nuestra comunidad. Descubre historias de esperanza y únete al cambio.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SelectorDeTabs(tabSeleccionada: Int, onTabSelected: (Int) -> Unit) {
    TabRow(
        selectedTabIndex = tabSeleccionada,
        modifier = Modifier.padding(bottom = 16.dp),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Tab(
            selected = tabSeleccionada == 0,
            onClick = { onTabSelected(0) },
            text = { Text("Donar a campaña", style = MaterialTheme.typography.titleSmall) }
        )
        Tab(
            selected = tabSeleccionada == 1,
            onClick = { onTabSelected(1) },
            text = { Text("Tengo algo para donar", style = MaterialTheme.typography.titleSmall) }
        )
    }
}

@Composable
fun TarjetaDonacion(donacion: Donacion, onDonarClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = donacion.imagenUrl,
                contentDescription = donacion.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = AmbarClaro,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = donacion.fundacion,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Ambar
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = donacion.titulo,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = donacion.descripcionCorta,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = donacion.textoProgreso,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Meta: ${donacion.meta.toInt()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val progreso = if (donacion.meta > 0) donacion.recaudado / donacion.meta else 0f
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = AzulMarino,
                trackColor = GrisBorde
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDonarClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = AzulMarino
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Donar ahora", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DonacionScreenPreview() {
    FamiliasQueSumanTheme {
        // rememberNavController() crea un controlador falso solo para que la vista previa no falle
        DonacionScreen(navController = rememberNavController())
    }
}