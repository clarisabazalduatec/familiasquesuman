package com.example.familiasquesuman.ui.screens.actividad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familiasquesuman.ui.screens.actividad.components.LeyendaFiltros
import com.example.familiasquesuman.ui.screens.actividad.components.TarjetaActividadDia
import com.example.familiasquesuman.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Calendario con las actividades en las que la familia está inscrita (o en lista de espera).
 * Los datos vienen del backend (ActividadViewModel.cargarCalendario).
 */
@Composable
fun CalendarioActividades(
    estado: CalendarioUiState,
    onActividadClick: (String) -> Unit,
    onReintentar: () -> Unit,
    onIniciarSesion: () -> Unit,
) {
    // verticalScroll: el mes + la lista del día no caben en pantallas chicas
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Calendario de Actividades",
                color = AzulOscuro,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Consulta las actividades en las que tú y tu familia están inscritos.",
                color = TextoGrisActividad,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(18.dp))

        when (estado) {
            CalendarioUiState.SinSesion -> AvisoCalendario(
                texto = "Inicia sesión para ver las actividades en las que está inscrita tu familia.",
                boton = "Iniciar sesión",
                onBoton = onIniciarSesion
            )

            CalendarioUiState.Cargando -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = Ambar) }

            is CalendarioUiState.Error -> AvisoCalendario(
                texto = estado.mensaje,
                boton = "Reintentar",
                onBoton = onReintentar
            )

            is CalendarioUiState.Listo -> MesConActividades(
                actividades = estado.actividades,
                onActividadClick = onActividadClick
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun MesConActividades(
    actividades: List<ActividadCalendario>,
    onActividadClick: (String) -> Unit,
) {
    val locale = remember { Locale("es", "MX") }
    val formatoBase = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US) }
    val hoy = remember { formatoBase.format(Date()) }

    // Mes inicial: el de la próxima actividad; si no hay próximas, el mes actual
    val fechaInicial = actividades.firstOrNull { it.fecha >= hoy }?.fecha
    var mesActual by remember(fechaInicial) {
        mutableStateOf(Calendar.getInstance().apply {
            fechaInicial?.let { f -> formatoBase.parse(f)?.let { time = it } }
            set(Calendar.DAY_OF_MONTH, 1)
        })
    }
    var fechaSeleccionada by remember(fechaInicial) { mutableStateOf(fechaInicial) }
    val actividadesPorFecha = remember(actividades) { actividades.groupBy { it.fecha } }

    if (actividades.isEmpty()) {
        AvisoCalendario(
            texto = "Tu familia todavía no está inscrita en ninguna actividad. Elige una en la lista para inscribirte."
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

    // ---------- Encabezado del mes ----------
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BotonMes(Icons.Default.ChevronLeft, "Mes anterior") {
            mesActual = (mesActual.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
            fechaSeleccionada = null
        }
        val nombreMes = SimpleDateFormat("MMMM", locale)
            .format(mesActual.time)
            .replaceFirstChar { it.uppercase() }
        Text(
            text = "$nombreMes ${mesActual.get(Calendar.YEAR)}",
            color = AzulOscuro,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        BotonMes(Icons.Default.ChevronRight, "Mes siguiente") {
            mesActual = (mesActual.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
            fechaSeleccionada = null
        }
    }

    Spacer(modifier = Modifier.height(18.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        listOf("DOM", "LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB").forEach { dia ->
            Text(
                text = dia,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = TextoGrisActividad,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    // ---------- Cuadrícula del mes ----------
    Spacer(modifier = Modifier.height(8.dp))
    val primerDiaMes = (mesActual.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
    val espaciosIniciales = primerDiaMes.get(Calendar.DAY_OF_WEEK) - 1
    val diasDelMes = mesActual.getActualMaximum(Calendar.DAY_OF_MONTH)
    val semanas = (espaciosIniciales + diasDelMes + 6) / 7
    Column(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(semanas) { semana ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(7) { columna ->
                    val numeroDia = semana * 7 + columna - espaciosIniciales + 1
                    if (numeroDia in 1..diasDelMes) {
                        val fecha = formatoBase.format(
                            (mesActual.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, numeroDia) }.time
                        )
                        DiaCalendario(
                            modifier = Modifier.weight(1f),
                            numeroDia = numeroDia,
                            esHoy = fecha == hoy,
                            seleccionado = fechaSeleccionada == fecha,
                            actividades = actividadesPorFecha[fecha].orEmpty(),
                            onClick = { fechaSeleccionada = fecha }
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f).height(64.dp))
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
    LeyendaFiltros()
    HorizontalDivider(
        color = GrisBordeClaro,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )

    // ---------- Actividades del día elegido ----------
    val fecha = fechaSeleccionada
    if (fecha == null) {
        if (actividades.isNotEmpty()) {
            AvisoCalendario(texto = "Selecciona un día con puntos para ver las actividades de tu familia.")
        }
    } else {
        val textoFecha = formatoBase.parse(fecha)
            ?.let { SimpleDateFormat("EEEE, d 'de' MMMM", locale).format(it).replaceFirstChar { l -> l.uppercase() } }
            ?: fecha
        Text(
            text = textoFecha,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = AzulOscuro,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )
        val delDia = actividadesPorFecha[fecha].orEmpty()
        if (delDia.isEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Text(
                    text = "Tu familia no tiene actividades este día.",
                    modifier = Modifier.padding(18.dp),
                    color = TextoGrisActividad,
                    fontSize = 13.sp
                )
            }
        } else {
            delDia.forEach { item ->
                if (item.enEspera) {
                    Text(
                        text = "En lista de espera",
                        modifier = Modifier.padding(start = 16.dp, top = 6.dp),
                        color = Ambar,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                TarjetaActividadDia(
                    actividad = item.actividad,
                    onActividadClick = onActividadClick
                )
            }
        }
    }
}

@Composable
private fun BotonMes(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    descripcion: String,
    onClick: () -> Unit
) {
    Surface(onClick = onClick, shape = CircleShape, color = Color.White, shadowElevation = 2.dp) {
        Box(modifier = Modifier.size(42.dp), contentAlignment = Alignment.Center) {
            Icon(imageVector = icono, contentDescription = descripcion, tint = AzulOscuro)
        }
    }
}

/** Tarjeta ámbar con un mensaje y, opcionalmente, un botón. */
@Composable
private fun AvisoCalendario(texto: String, boton: String? = null, onBoton: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        color = AmbarClaro
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = texto, color = AzulOscuro, fontSize = 13.sp, lineHeight = 18.sp)
            if (boton != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onBoton,
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) { Text(boton, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun DiaCalendario(
    modifier: Modifier,
    numeroDia: Int,
    esHoy: Boolean,
    seleccionado: Boolean,
    actividades: List<ActividadCalendario>,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(64.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(
                    when {
                        seleccionado -> Ambar
                        esHoy -> AmbarClaro
                        else -> Color.Transparent
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = numeroDia.toString(),
                color = if (seleccionado) Color.White else AzulOscuro,
                fontSize = 16.sp,
                fontWeight = if (seleccionado || esHoy) FontWeight.Bold else FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        if (actividades.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                actividades.take(3).forEach { item ->
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(colorPuntoCategoria(item.actividad.categoria))
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

private fun colorPuntoCategoria(categoria: String): Color {
    return when (categoria) {
        "Medio Ambiente" -> FiltroVerde
        "Educación" -> FiltroAzul
        "Apoyo Social" -> FiltroNaranja
        else -> FiltroMorado
    }
}