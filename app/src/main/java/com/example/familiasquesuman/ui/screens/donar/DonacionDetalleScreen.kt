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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
import androidx.core.net.toUri

fun abrirEnlaceWeb(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}

fun abrirMaps(context: Context, direccion: String) {
    val intent = Intent(Intent.ACTION_VIEW, "geo:0,0?q=${Uri.encode(direccion)}".toUri())
    context.startActivity(intent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonacionDetalleScreen(
    navController: NavHostController,
    donacionId: String,
    viewModel: DonacionViewModel = viewModel(),
) {
    val donacion = viewModel.obtenerDonacionPorId(donacionId)
    val context = LocalContext.current

    if (donacion == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Donación no encontrada", color = MaterialTheme.colorScheme.onBackground)
        }
        return
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.DONAR
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            // IMAGEN Y BOTÓN VOLVER
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = donacion.imagenUrl,
                    contentDescription = donacion.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = AzulMarino
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // CONDICIONAL DE VISTA
                if (donacion.tipo == TipoDonacion.ESPECIE) {
                    DetalleEspecieView(donacion = donacion, context = context)
                } else {
                    DetalleCampanaView(donacion = donacion, context = context)
                }
            }
        }
    }
}

@Composable
fun DetalleEspecieView(donacion: Donacion, context: Context) {
    TarjetaSeccion {
        // Título de la Organización o Fundación
        Text(
            text = donacion.titulo,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AzulMarino
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Categoría / Subtítulo
        Text(
            text = donacion.categoria ?: donacion.fundacion,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Descripción concisa (usando descripcionCorta)
        Text(
            text = donacion.descripcionCorta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ubicación / Dirección
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = donacion.direccionCompleta ?: donacion.ubicacion ?: "Dirección no especificada",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Categorías de artículos aceptados
        val categorias = donacion.categoriasEspecie ?: listOf("Tablets", "Cargadores", "Electrónicos")
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categorias.forEach { cat ->
                ChipEtiqueta(
                    texto = cat,
                    colorFondo = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    colorTexto = AzulMarino
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = GrisBorde, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // Destinatarios (Tonalidad AzulMarino)
        SeccionEtiquetas(
            titulo = "Destinatarios",
            lista = donacion.destinatarios ?: listOf("Estudiantes", "Niños"),
            colorFondo = AzulMarino.copy(alpha = 0.1f),
            colorTexto = AzulMarino
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Condiciones (Tonalidad Ambar)
        SeccionEtiquetas(
            titulo = "Condiciones",
            lista = donacion.condicionesArticulos ?: listOf("Nuevo", "Usado en buen estado"),
            colorFondo = AmbarClaro,
            colorTexto = Ambar
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Métodos de entrega (Tonalidad Verde)
        SeccionEtiquetas(
            titulo = "Métodos de entrega",
            lista = donacion.metodosEntrega ?: listOf("Entrega en el centro", "Recolección"),
            colorFondo = Color(0xFFE8F5E9),
            colorTexto = Color(0xFF2E7D32)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Tarjeta gris/superficie para Condiciones de recepción
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Condiciones de recepción",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = donacion.condicionesRecepcion ?: "Comunicarse para coordinar la entrega.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botones de Acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    val numero = donacion.telefonoWhatsapp ?: "8110809078"
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

            Button(
                onClick = {
                    val dir = donacion.direccionCompleta ?: donacion.ubicacion ?: donacion.titulo
                    abrirMaps(context, dir)
                },
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

@Composable
fun DetalleCampanaView(donacion: Donacion, context: Context) {
    TarjetaSeccion {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(color = AmbarClaro, shape = RoundedCornerShape(16.dp)) {
                Text(
                    text = "Campaña",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Ambar,
                    fontWeight = FontWeight.Bold,
                )
            }
            Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(16.dp)) {
                Text(
                    text = "Activa",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = donacion.titulo,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = AzulMarino,
        )

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
            text = "Descripción",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AzulMarino,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = donacion.descripcionLarga,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    if (!donacion.opcionesDetalle.isNullOrEmpty()) {
        TarjetaSeccion {
            Text(
                text = "Opciones de donación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AzulMarino
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Elige cómo quieres ayudar:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            donacion.opcionesDetalle.forEachIndexed { index, opcion ->
                val bgColor = if (index % 2 == 0) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)

                Surface(
                    color = bgColor,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = AzulMarino,
                            shape = CircleShape,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "${index + 1}", color = MaterialTheme.colorScheme.surface, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = opcion.first,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AzulMarino
                            )
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
    } else {
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
                    text = "Meta total: ${donacion.meta.toInt()} MXN",
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
                Text(text = donacion.textoProgreso, style = MaterialTheme.typography.titleSmall, color = AzulMarino)
            }
            Spacer(modifier = Modifier.height(8.dp))

            val progreso = if (donacion.meta > 0) (donacion.recaudado / donacion.meta).coerceIn(0f, 1f) else 0f
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

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = donacion.fundacion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = donacion.telefonoWhatsapp ?: "No disponible", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    val numero = donacion.telefonoWhatsapp ?: "528112345678"
                    val mensaje = "Hola, me interesa ayudar en la campaña: ${donacion.titulo}"
                    abrirEnlaceWeb(context, "https://wa.me/$numero?text=${Uri.encode(mensaje)}")
                },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF66BB6A)),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(Icons.Default.Phone, contentDescription = "WhatsApp", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Quiero ayudar", fontWeight = FontWeight.Bold, color = Color.White)
            }

            OutlinedButton(
                onClick = {
                    val numero = donacion.telefonoWhatsapp ?: "528112345678"
                    context.startActivity(Intent(Intent.ACTION_DIAL, "tel:$numero".toUri()))
                },
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulMarino),
                border = BorderStroke(1.dp, GrisBorde),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Llamar")
            }
        }
    }
}

// ========================================================
// 3. COMPONENTES AUXILIARES
// ========================================================
@Composable
fun SeccionEtiquetas(titulo: String, lista: List<String>, colorFondo: Color, colorTexto: Color) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1F2937)
    )
    Spacer(modifier = Modifier.height(6.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        lista.forEach { item ->
            ChipEtiqueta(texto = item, colorFondo = colorFondo, colorTexto = colorTexto)
        }
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
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = colorTexto
        )
    }
}

@Composable
fun TarjetaSeccion(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, GrisBorde)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}