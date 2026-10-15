package com.example.familiasquesuman.ui.screens.proyectos

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.familiasquesuman.domain.EstadoProyecto
import com.example.familiasquesuman.domain.OpcionApoyo
import com.example.familiasquesuman.domain.Proyecto
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.theme.FondoAzulClaro
import com.example.familiasquesuman.ui.theme.FondoVerde
import com.example.familiasquesuman.ui.theme.TextoAzul
import com.example.familiasquesuman.ui.theme.TextoVerde

private fun abrirEnlace(context: Context, url: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "No hay una app para abrir este enlace", Toast.LENGTH_SHORT).show()
    }
}

/** Abre WhatsApp con un mensaje ya escrito. */
private fun abrirWhatsApp(context: Context, numero: String, mensaje: String) =
    abrirEnlace(context, "https://wa.me/$numero?text=${Uri.encode(mensaje)}")

/** Colores de las opciones de apoyo, por orden: amarillo, azul, verde (y se repite). */
private val coloresOpciones: List<Pair<Color, Color>> = listOf(
    Ambar to AmbarClaro,
    TextoAzul to FondoAzulClaro,
    TextoVerde to FondoVerde,
)

@Composable
fun ProyectoDetalleScreen(
    navController: NavHostController,
    proyectoId: String,
    viewModel: ProyectoViewModel = viewModel()
) {
    val estado by viewModel.detalle.collectAsState()
    LaunchedEffect(proyectoId) { viewModel.cargarDetalle(proyectoId) }

    val irAProyectos = {
        if (navController.currentDestination?.route != Rutas.Proyectos.ruta) {
            navController.navigate(Rutas.Proyectos.ruta) {
                popUpTo(Rutas.Proyectos.ruta) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.PROYECTOS,
        backButton = true,
        onBackClick = irAProyectos
    ) { paddingInterno ->
        val proyecto = estado.proyecto
        when {
            proyecto != null -> ContenidoDetalle(
                proyecto = proyecto,
                modifier = Modifier.padding(paddingInterno)
            )

            estado.cargando -> Box(
                modifier = Modifier.padding(paddingInterno).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            else -> Column(
                modifier = Modifier.padding(paddingInterno).fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = estado.error ?: "Proyecto no encontrado",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = { viewModel.cargarDetalle(proyectoId) }) { Text("Reintentar") }
                TextButton(onClick = irAProyectos) { Text("Volver a Proyectos") }
            }
        }
    }
}

@Composable
private fun ContenidoDetalle(
    proyecto: Proyecto,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {


        // ---------- Tarjeta encabezado ----------
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (proyecto.logoUrl != null) {
                        AsyncImage(
                            model = proyecto.logoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        ChipEstadoActivo(
                            texto = if (proyecto.estado == EstadoProyecto.ACTIVO) "Activo" else "Anterior"
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = proyecto.nombre,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (proyecto.ubicacion.isNotBlank()) {
                        DatoConIcono(icono = Icons.Default.LocationOn, texto = proyecto.ubicacion)
                    }
                    DatoConIcono(icono = Icons.Default.Groups, texto = proyecto.participantes)
                }

                // Línea corta bajo el título (descripcion_corta)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = proyecto.descripcionCorta,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ---------- Secciones de texto (solo si tienen contenido) ----------
        if (proyecto.acercaDe.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            SeccionTexto(titulo = "Acerca del proyecto", texto = proyecto.acercaDe)
        }

        if (proyecto.descripcionLarga.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            SeccionTexto(titulo = "Descripción", texto = proyecto.descripcionLarga)
        }

        proyecto.comoAyudar?.let { texto ->
            Spacer(modifier = Modifier.height(16.dp))
            SeccionTexto(titulo = "¿Cómo ayudar?", texto = texto, resaltada = true)
        }

        // ---------- Opciones de apoyo ----------
        if (proyecto.opcionesApoyo.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Elige tu forma de apoyar y que más se adapte a ti",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            proyecto.opcionesApoyo.forEachIndexed { indice, opcion ->
                val whatsapp = proyecto.telefonoWhatsapp
                TarjetaOpcionApoyo(
                    opcion = opcion,
                    colores = coloresOpciones[indice % coloresOpciones.size],
                    onClick = whatsapp?.let {
                        {
                            abrirWhatsApp(
                                context, it,
                                "Hola, vi el proyecto \"${proyecto.nombre}\" en Familias que Suman " +
                                        "y me interesa apoyar con: ${opcion.titulo}"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // ---------- Síguenos en ----------
        proyecto.instagramUrl?.let { url ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Síguenos en:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedButton(onClick = { abrirEnlace(context, url) }, shape = RoundedCornerShape(24.dp)) {
                    Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Instagram")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Contacto ----------
        proyecto.telefonoWhatsapp?.let { numero ->
            BotonPrimario(
                onClick = {
                    abrirWhatsApp(
                        context, numero,
                        "Hola, vi el proyecto \"${proyecto.nombre}\" en Familias que Suman " +
                                "y me gustaría más información."
                    )
                },
                containerColor = AzulMarino,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Escribir por WhatsApp", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        proyecto.telefonoLlamada?.let { telefono ->
            BotonPrimario(
                onClick = { abrirEnlace(context, "tel:$telefono") },
                containerColor = Ambar,
                contentColor = AzulMarino
            ) {
                Icon(imageVector = Icons.Default.Phone, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Llamar: $telefono", fontWeight = FontWeight.Bold)
            }
        }

        // Curado por
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Proyecto curado por Familias que Suman",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DatoConIcono(icono: androidx.compose.ui.graphics.vector.ImageVector, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SeccionTexto(titulo: String, texto: String, resaltada: Boolean = false) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (resaltada) MaterialTheme.colorScheme.surfaceVariant
            else MaterialTheme.colorScheme.surface
        ),
        border = if (resaltada) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (resaltada) 0.dp else 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyMedium,
                color = if (resaltada) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** onClick = null cuando el proyecto no tiene WhatsApp: la tarjeta se ve pero no hace nada. */
@Composable
private fun TarjetaOpcionApoyo(
    opcion: OpcionApoyo,
    colores: Pair<Color, Color>,
    onClick: (() -> Unit)?
) {
    val (acento, fondo) = colores
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, acento.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(fondo),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.VolunteerActivism,
                    contentDescription = null,
                    tint = acento
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(acento)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = opcion.titulo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = opcion.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (onClick != null) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Apoyar por WhatsApp",
                    tint = acento
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProyectoDetalleScreenPreview() {
    FamiliasQueSumanTheme {
        ProyectoDetalleScreen(navController = rememberNavController(), proyectoId = "1")
    }
}
