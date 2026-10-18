package com.example.familiasquesuman.ui.screens.donar

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.TipoDonacion
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.GrisBorde
import com.example.familiasquesuman.ui.navigation.Rutas

fun abrirEnlaceWeb(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}

fun abrirMaps(context: Context, direccion: String) {
    val intent = Intent(Intent.ACTION_VIEW, "geo:0,0?q=${Uri.encode(direccion)}".toUri())
    context.startActivity(intent)
}

@Composable
fun EncabezadoBadges(tipoTexto: String, activa: Boolean) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(color = AmbarClaro, shape = RoundedCornerShape(16.dp)) {
            Text(
                text = tipoTexto,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                color = Ambar,
                fontWeight = FontWeight.Bold,
            )
        }
        val colorFondo = if (activa) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
        val colorTexto = if (activa) Color(0xFF4CAF50) else Color(0xFFE53935)
        val textoEstado = if (activa) "Activa" else "Inactiva"

        Surface(color = colorFondo, shape = RoundedCornerShape(16.dp)) {
            Text(
                text = textoEstado,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                color = colorTexto,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonacionDetalleScreen(
    navController: NavHostController,
    donacionId: String,
    viewModel: DonacionViewModel = viewModel(),
) {
    LaunchedEffect(donacionId) {
        viewModel.cargarDetalleDonacion(donacionId)
    }

    val donacion = viewModel.donacionSeleccionada.collectAsState().value
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val iraDonacion = {
        if (navController.currentDestination?.route != Rutas.Donar.ruta) {
            navController.navigate(Rutas.Donar.ruta) {
                popUpTo(Rutas.Donar.ruta) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    if (isLoading && donacion == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AzulMarino)
        }
        return
    }

    if (donacion == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Donación no encontrada", color = MaterialTheme.colorScheme.onBackground)
        }
        return
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.DONAR,
        backButton = true,
        onBackClick = iraDonacion

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            if (donacion.imagenUrl.isNotBlank()) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = donacion.imagenUrl,
                        contentDescription = donacion.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (donacion.tipo) {
                    TipoDonacion.ESPECIE -> DetalleEspecieView(donacion = donacion, context = context)
                    TipoDonacion.TIEMPO -> DetalleTiempoView(donacion = donacion, context = context)
                    else -> DetalleCampanaView(donacion = donacion, context = context)
                }
            }
        }
    }
}

@Composable
fun DetalleEspecieView(donacion: Donacion, context: Context) {
    TarjetaSeccion {
        EncabezadoBadges(tipoTexto = "En Especie", activa = donacion.activa)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = donacion.titulo,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AzulMarino
        )

        if (!donacion.fundacion.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = donacion.fundacion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Oculta el chip si la categoría es un número/ID como "1"
        if (!donacion.categoria.isNullOrBlank() && donacion.categoria.toIntOrNull() == null) {
            ChipEtiqueta(texto = donacion.categoria, colorFondo = AmbarClaro, colorTexto = Ambar)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = donacion.descripcionCorta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        val ubicacionMostrar = donacion.direccionCompleta ?: donacion.ubicacion
        if (!ubicacionMostrar.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = ubicacionMostrar,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (donacion.descripcionLarga.isNotBlank()) {
        TarjetaSeccion {
            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AzulMarino,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = donacion.descripcionLarga,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    SeccionOpcionesSugeridas(donacion = donacion)

    val condicionesMostrar = donacion.condiciones.orEmpty().ifBlank { donacion.condicionesRecepcion.orEmpty() }
    if (condicionesMostrar.isNotBlank()) {
        TarjetaSeccion {
            Text(
                text = "Condiciones de recepción",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = AzulMarino
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = condicionesMostrar,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }

    TarjetaSeccion {
        Text(
            text = "Contacto",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AzulMarino,
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!donacion.telefonoWhatsapp.isNullOrBlank()) {
                Button(
                    onClick = {
                        val numero = donacion.telefonoWhatsapp
                        context.startActivity(Intent(Intent.ACTION_DIAL, "tel:$numero".toUri()))
                    },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarino)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Llamar", fontWeight = FontWeight.Bold)
                }
            }

            val dir = donacion.direccionCompleta ?: donacion.ubicacion
            if (!dir.isNullOrBlank()) {
                Button(
                    onClick = { abrirMaps(context, dir) },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cómo llegar", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun DetalleCampanaView(donacion: Donacion, context: Context) {
    TarjetaSeccion {
        EncabezadoBadges(tipoTexto = "Campaña", activa = donacion.activa)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = donacion.titulo,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AzulMarino,
        )

        if (!donacion.fundacion.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = donacion.fundacion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Oculta el chip si la categoría es un número/ID como "1"
        if (!donacion.categoria.isNullOrBlank() && donacion.categoria.toIntOrNull() == null) {
            Spacer(modifier = Modifier.height(8.dp))
            ChipEtiqueta(texto = donacion.categoria, colorFondo = AmbarClaro, colorTexto = Ambar)
        }

        if (!donacion.ubicacion.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = donacion.ubicacion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = donacion.descripcionCorta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    if (donacion.descripcionLarga.isNotBlank()) {
        TarjetaSeccion {
            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AzulMarino,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = donacion.descripcionLarga,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    SeccionOpcionesSugeridas(donacion = donacion)

    if (donacion.meta > 0f) {
        TarjetaSeccion {
            Text(
                text = "Meta de la campaña",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AzulMarino
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.DateRange, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Meta total: $${donacion.meta.toInt()} MXN",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "$${donacion.recaudado.toInt()} MXN recaudados de $${donacion.meta.toInt()} MXN",
                    style = MaterialTheme.typography.titleSmall,
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            val progreso = (donacion.recaudado / donacion.meta).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = AzulMarino,
                trackColor = GrisBorde,
            )
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AmbarClaro),
        border = BorderStroke(1.dp, Ambar)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "¿Cómo ayudar?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AzulMarino,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = donacion.comoAyudar ?: "Ayúdanos a difundir esta campaña con familiares y amigos para lograr llegar a la meta.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    TarjetaSeccion {
        Text(
            text = "Contacto",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AzulMarino,
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (!donacion.fundacion.isNullOrBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = donacion.fundacion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (!donacion.telefonoWhatsapp.isNullOrBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = donacion.telefonoWhatsapp, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        val numero = donacion.telefonoWhatsapp
                        val mensaje = "Hola, me interesa ayudar en la campaña: ${donacion.titulo}"
                        abrirEnlaceWeb(context, "https://wa.me/$numero?text=${Uri.encode(mensaje)}")
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A)),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(Icons.Default.Phone, contentDescription = "WhatsApp", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Contactar por WhatsApp", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DetalleTiempoView(donacion: Donacion, context: Context) {
    TarjetaSeccion {
        EncabezadoBadges(tipoTexto = "Voluntariado", activa = donacion.activa)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = donacion.titulo,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AzulMarino,
        )

        if (!donacion.fundacion.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = donacion.fundacion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Oculta el chip si la categoría es un número/ID como "1"
        if (!donacion.categoria.isNullOrBlank() && donacion.categoria.toIntOrNull() == null) {
            Spacer(modifier = Modifier.height(8.dp))
            ChipEtiqueta(texto = donacion.categoria, colorFondo = AmbarClaro, colorTexto = Ambar)
        }

        if (!donacion.ubicacion.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = donacion.ubicacion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = donacion.descripcionCorta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    TarjetaSeccion {
        Text(
            text = "Detalles del Voluntariado",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AzulMarino,
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (!donacion.fechaLimite.isNullOrEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.DateRange, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Fecha límite: ${donacion.fechaLimite}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (!donacion.horario.isNullOrEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = AzulMarino, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Horario: ${donacion.horario}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (donacion.descripcionLarga.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = donacion.descripcionLarga,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    TarjetaSeccion {
        Text(
            text = "Contacto",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AzulMarino,
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (!donacion.fundacion.isNullOrBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = donacion.fundacion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (!donacion.telefonoWhatsapp.isNullOrBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = donacion.telefonoWhatsapp, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        val numero = donacion.telefonoWhatsapp
                        val mensaje = "Hola, me interesa participar como voluntario en: ${donacion.titulo}"
                        abrirEnlaceWeb(context, "https://wa.me/$numero?text=${Uri.encode(mensaje)}")
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A)),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(Icons.Default.Phone, contentDescription = "WhatsApp", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Quiero ser voluntario", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SeccionOpcionesSugeridas(donacion: Donacion) {
    if (!donacion.opcionesDetalle.isNullOrEmpty()) {
        TarjetaSeccion {
            Text(
                text = "Opciones sugeridas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AzulMarino
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Apoyos o montos sugeridos:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))

            donacion.opcionesDetalle.forEachIndexed { index, opcion ->
                val bgColor = if (index % 2 == 0) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)

                Surface(
                    color = bgColor,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = AzulMarino,
                            shape = CircleShape,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "${index + 1}", color = MaterialTheme.colorScheme.surface, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            if (opcion.first.isNotBlank()) {
                                Text(
                                    text = opcion.first,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AzulMarino
                                )
                            }
                            Text(
                                text = opcion.second,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AzulMarino
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaSeccion(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun ChipEtiqueta(texto: String, colorFondo: Color, colorTexto: Color) {
    Surface(
        color = colorFondo,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = colorTexto,
            fontWeight = FontWeight.Medium
        )
    }
}