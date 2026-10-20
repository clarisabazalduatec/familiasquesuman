package com.example.familiasquesuman.ui.screens.notificaciones

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.familiasquesuman.data.remote.dto.NotificacionDto
import com.example.familiasquesuman.ui.components.EstadoVacio
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.perfil.InvitacionesRecibidasCard
import com.example.familiasquesuman.ui.screens.perfil.PerfilViewModel
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.GrisTexto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import com.example.familiasquesuman.ui.components.EstadoErrorServidor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacionesScreen(
    navController: NavHostController,
    viewModel: NotificacionesViewModel = viewModel(),
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()
    // Invitaciones a familia: se aceptan o rechazan aquí mismo (con su propia tarjeta)
    val perfil by perfilViewModel.uiState.collectAsState()
    val invitaciones = perfil.invitacionesRecibidas
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.cargar()
        perfilViewModel.cargar()
    }
    LaunchedEffect(perfil.mensaje) {
        perfil.mensaje?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            perfilViewModel.mensajeMostrado()
            viewModel.cargar() // aceptar/rechazar cambia lo que hay que mostrar
        }
    }

    val grupos = agruparPorFecha(estado.notificaciones)

    Scaffold(
        containerColor = CremaFondo,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CremaFondo,
                    navigationIconContentColor = AzulMarino,
                    titleContentColor = AzulMarino,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                title = {
                    Text(
                        text = "Notificaciones",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AzulMarino,
                    )
                },
                actions = {
                    if (estado.hayNoLeidas && estado.error == null && !estado.cargando) {
                        TextButton(onClick = { viewModel.marcarTodas() }) {
                            Text("Marcar leídas", color = Ambar)
                        }
                    }
                }
            )
        },
    ) { paddingInterno ->
        when {
            estado.cargando && estado.notificaciones.isEmpty() -> Box(
                Modifier.padding(paddingInterno).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = Ambar) }

            (estado.error != null || perfil.errorCarga != null) &&
                    estado.notificaciones.isEmpty() &&
                    invitaciones.isEmpty() -> Box(
                modifier = Modifier
                    .padding(paddingInterno)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EstadoErrorServidor(
                    onReintentar = { viewModel.cargar()
                        perfilViewModel.cargar()
                    }
                )
            }

            estado.notificaciones.isEmpty() && invitaciones.isEmpty() -> Box(
                Modifier.padding(paddingInterno).fillMaxSize()
            ) {
                EstadoVacio(
                    icono = Icons.Default.NotificationsOff,
                    titulo = "Sin notificaciones",
                    mensaje = "Aquí aparecerán tus actualizaciones y avisos importantes.",
                )
            }

            else -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(paddingInterno).fillMaxSize(),
            ) {
                if (invitaciones.isNotEmpty()) {
                    item {
                        InvitacionesRecibidasCard(
                            invitaciones = invitaciones,
                            guardando = perfil.guardando,
                            onAceptar = { perfilViewModel.aceptarInvitacion(it) },
                            onRechazar = { perfilViewModel.rechazarInvitacion(it) }
                        )
                    }
                }
                grupos.forEach { (titulo, lista) ->
                    item(key = "grupo_$titulo") { EncabezadoSeccion(titulo) }
                    items(lista, key = { it.id }) { n ->
                        NotificacionItem(
                            notificacion = n,
                            tiempo = tiempoCorto(n.creadoEn),
                            onClick = {
                                val ruta = viewModel.abrir(n)
                                // Las invitaciones ya se ven en esta misma pantalla
                                if (ruta != null && ruta != Rutas.Notificaciones.ruta) {
                                    navController.navigate(ruta) { launchSingleTop = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EncabezadoSeccion(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 6.dp, bottom = 2.dp),
    )
}

// ---------- fechas ----------
// El backend manda UTC ("2026-10-16T21:43:00.123Z"). Se usa SimpleDateFormat y no java.time
// porque la app soporta Android 7 (minSdk 24) y java.time necesita Android 8.

private val formatoServidor = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

private fun fechaDe(iso: String): Date? = try {
    formatoServidor.parse(iso.take(19))
} catch (_: Exception) {
    null
}

/** Días completos entre la fecha y hoy (0 = hoy, 1 = ayer...), en la hora del celular. */
private fun diasDesde(fecha: Date): Int {
    fun inicioDelDia(t: Long) = Calendar.getInstance().apply {
        timeInMillis = t
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    val diferencia = inicioDelDia(System.currentTimeMillis()) - inicioDelDia(fecha.time)
    return (diferencia / (24L * 60 * 60 * 1000)).toInt()
}

private fun agruparPorFecha(lista: List<NotificacionDto>): List<Pair<String, List<NotificacionDto>>> =
    lista.groupBy { n ->
        val dias = fechaDe(n.creadoEn)?.let { diasDesde(it) } ?: 99
        when {
            dias <= 0 -> "HOY"
            dias == 1 -> "AYER"
            dias < 7 -> "ESTA SEMANA"
            else -> "ANTERIORES"
        }
    }.toList() // groupBy respeta el orden: la lista ya viene de la más nueva a la más vieja

private fun tiempoCorto(iso: String): String {
    val fecha = fechaDe(iso) ?: return ""
    val local = Locale("es", "MX")
    return when (val dias = diasDesde(fecha)) {
        0 -> SimpleDateFormat("HH:mm", local).format(fecha)
        1 -> "Ayer"
        in 2..6 -> SimpleDateFormat("EEE", local).format(fecha).replaceFirstChar { it.uppercase() }.trimEnd('.')
        else -> SimpleDateFormat("dd/MM", local).format(fecha)
    }
}