package com.example.familiasquesuman.ui.screens.donar

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.familiasquesuman.data.SesionManager
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.TipoDonacion
import com.example.familiasquesuman.ui.components.EstadoErrorServidor
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.components.PieAvisoPrivacidad
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonacionScreen(
    navController: NavHostController,
    viewModel: DonacionViewModel = viewModel(),
) {
    val ciudadId = SesionManager.usuario?.ciudad?.id
    LaunchedEffect(ciudadId) {
        viewModel.cargarDonaciones(ciudadId = ciudadId)
    }

    val donaciones by viewModel.donaciones.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val tabSeleccionada by viewModel.tabSeleccionada.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val listaFiltros = listOf("Todos", "Juguetes", "Ropa", "Alimentos", "Higiene", "Electrónicos", "Salud", "Útiles")

    val donacionesAMostrar = remember(donaciones, tabSeleccionada, categoriaSeleccionada) {
        donaciones.filter { donacion ->
            val estaActiva = donacion.activa

            when (tabSeleccionada) {
                0 -> {
                    // Pestaña "Donar a campaña" (Campañas monetarias y Voluntariados)
                    val esCampanaOTiempo = donacion.tipo == TipoDonacion.CAMPANA || donacion.tipo == TipoDonacion.TIEMPO
                    estaActiva && esCampanaOTiempo
                }
                1 -> {
                    // Pestaña "Tengo algo para donar" (ÚNICAMENTE donaciones en especie)
                    val esEspecie = donacion.tipo == TipoDonacion.ESPECIE
                    val coincideFiltro = categoriaSeleccionada == "Todos" ||
                            donacion.categoria.isNullOrBlank() ||
                            donacion.categoria.equals(categoriaSeleccionada, ignoreCase = true)

                    estaActiva && esEspecie && coincideFiltro
                }
                else -> estaActiva
            }
        }
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.DONAR,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
        ) {
            item {
                EncabezadoDonacion()
            }
            item {
                SelectorDeTabs(tabSeleccionada) { nuevaTab ->
                    viewModel.cambiarTab(nuevaTab)
                }
            }

            if (tabSeleccionada == 1 && error == null) {
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(listaFiltros) { filtro ->
                            val seleccionado = filtro == categoriaSeleccionada
                            FilterChip(
                                selected = seleccionado,
                                onClick = { viewModel.seleccionarCategoria(filtro) },
                                label = { Text(filtro) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White,
                                ),
                            )
                        }
                    }
                }
            }

            // --- Lógica de Carga, Error y Estado Vacío ---
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            } else if (error != null) {
                item {
                    EstadoErrorServidor(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        onReintentar = {
                            viewModel.cargarDonaciones()
                        }
                    )
                }
            } else if (donacionesAMostrar.isEmpty()) {
                item {
                    EstadoVacioDonaciones(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 350.dp)
                            .padding(vertical = 24.dp)
                    )
                }
            } else {
                items(donacionesAMostrar) { donacion ->
                    TarjetaDonacion(
                        donacion = donacion,
                        onVerMasClick = {
                            navController.navigate(Rutas.DonacionDetalle.crearRuta(donacion.id))
                        },
                    )
                }

            }
            // Aviso de Privacidad
            item {
                PieAvisoPrivacidad(
                    navController = navController
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
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Cada donación es un puente hacia un futuro mejor para las familias de nuestra comunidad. Descubre historias de esperanza y únete al cambio.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun SelectorDeTabs(tabSeleccionada: Int, onTabSelected: (Int) -> Unit) {
    TabRow(
        selectedTabIndex = tabSeleccionada,
        modifier = Modifier.padding(bottom = 16.dp),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        Tab(
            selected = tabSeleccionada == 0,
            onClick = { onTabSelected(0) },
            text = { Text("Donar a campaña", style = MaterialTheme.typography.titleSmall) },
        )
        Tab(
            selected = tabSeleccionada == 1,
            onClick = { onTabSelected(1) },
            text = { Text("Tengo algo para donar", style = MaterialTheme.typography.titleSmall) },
        )
    }
}

@Composable
fun TarjetaDonacion(
    donacion: Donacion,
    onVerMasClick: () -> Unit,
    context: Context = LocalContext.current,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    .background(Color.LightGray),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(color = AmbarClaro, shape = RoundedCornerShape(4.dp)) {
                Text(
                    text = donacion.fundacion,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Ambar,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = donacion.titulo,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = donacion.descripcionCorta,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (donacion.tipo == TipoDonacion.CAMPANA) {
                if (donacion.opcionesDisponibles != null && donacion.opcionesDisponibles > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = AzulMarino,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${donacion.opcionesDisponibles} opciones de donación disponibles",
                                style = MaterialTheme.typography.labelLarge,
                                color = AzulMarino,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = {
                        val numero = donacion.telefonoWhatsapp ?: "528112345678"
                        val mensaje = "Hola, quiero ayudar en: ${donacion.titulo}"
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            "https://wa.me/$numero?text=${Uri.encode(mensaje)}".toUri(),
                        )
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarino),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "WhatsApp", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Quiero ayudar", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedButton(
                    onClick = onVerMasClick,
                    modifier = Modifier.height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulMarino),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Ver más")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DonacionScreenPreview() {
    FamiliasQueSumanTheme {
        DonacionScreen(navController = rememberNavController())
    }
}