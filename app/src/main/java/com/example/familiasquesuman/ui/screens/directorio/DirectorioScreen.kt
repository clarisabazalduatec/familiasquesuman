package com.example.familiasquesuman.ui.screens.directorio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.domain.CentroVisiteo
import com.example.familiasquesuman.domain.TipoCentro
import com.example.familiasquesuman.ui.components.BarraBusqueda
import com.example.familiasquesuman.ui.components.EstadoVacio
import com.example.familiasquesuman.ui.components.FiltrosChips
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme

val centrosDeEjemplo = mutableStateListOf(
    CentroVisiteo(
        id = "1",
        tipo = TipoCentro.ASILO,
        nombre = "Morada del Anciano Desvalido Cadereyta",
        descripcionCorta = "Asilo de ancianos donde se atienden 24 horas a 46 adultos mayores.",
        informacionGeneral = "Atención a adultos mayores en abandono, soledad y falta de apoyo familiar. Se les proporciona una vida digna. Se les ofrece refugio, alimentación, atención integral y compañía.",
        necesidades = listOf(
            "Alimentos como: azúcar, leche, aceite, té, gelatina, mole en lata, saladitas, servilletas, ensure, jugos y frutas",
            "Limpieza: trapeadores, cubetas, botes de basura, guantes, cloro, fabuloso, pino, jabón líquido, shampoo, desengrasantes, bolsas de basura",
        ),
        direccion = "Blvd Jose Maria Gonzalez #1000, Cadereyta",
        activo = true
    ),
)

fun agregarCentroMock(centro: CentroVisiteo) {
    centrosDeEjemplo.add(0, centro)
}

fun eliminarCentroMock(id: String) {
    centrosDeEjemplo.removeAll { it.id == id }
}

fun obtenerCentroPorId(id: String): CentroVisiteo? {
    return centrosDeEjemplo.find { it.id == id } ?: centrosDeEjemplo.firstOrNull()
}

@Composable
fun DirectorioScreen(navController: NavHostController) {
    var textoBusqueda by remember { mutableStateOf("") }
    var tipoSeleccionado by remember { mutableStateOf<TipoCentro?>(null) }

    val centrosFiltrados = remember(textoBusqueda, tipoSeleccionado, centrosDeEjemplo.size) {
        centrosDeEjemplo.filter { centro ->
            val coincideTipo = (tipoSeleccionado == null) || (centro.tipo == tipoSeleccionado)
            val coincideBusqueda = textoBusqueda.isBlank() ||
                    centro.nombre.contains(textoBusqueda, ignoreCase = true) ||
                    centro.descripcionCorta.contains(textoBusqueda, ignoreCase = true)
            coincideTipo && coincideBusqueda
        }
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.DIRECTORIO,
    ) { paddingInterno ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize(),
        ) {
            item {
                Column(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "Directorio de Visiteo",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Centros y espacios verificados para visitar y ayudar en familia.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            item {
                BarraBusqueda(
                    texto = textoBusqueda,
                    onTextoChange = { textoBusqueda = it },
                    placeholder = "Buscar centro...",
                )
            }
            item {
                FiltrosChips(
                    opciones = TipoCentro.entries,
                    seleccionado = tipoSeleccionado,
                    etiquetaPara = { it.etiqueta },
                    onSeleccionado = { tipoSeleccionado = it },
                )
            }
            if (centrosFiltrados.isEmpty()) {
                item { EstadoVacioDirectorio() }
            } else {
                items(centrosFiltrados) { centro ->
                    TarjetaCentro(
                        centro = centro,
                        onComoAyudarClick = {
                            navController.navigate(Rutas.DirectorioDetalle.crearRuta(centro.id))
                        }
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Centros verificados por Familias que Suman",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun EstadoVacioDirectorio(
    modifier: Modifier = Modifier,
    mensaje: String = "No hay centros en esta categoría por el momento.",
) {
    EstadoVacio(
        icono = Icons.Default.LocationOn,
        titulo = "Sin centros de visiteo",
        mensaje = mensaje,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun DirectorioScreenPreview() {
    FamiliasQueSumanTheme {
        DirectorioScreen(navController = rememberNavController())
    }
}
