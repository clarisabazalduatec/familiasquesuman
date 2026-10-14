package com.example.familiasquesuman.ui.screens.actividad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.familiasquesuman.data.ActividadMock
import com.example.familiasquesuman.data.toDomain
import com.example.familiasquesuman.ui.screens.actividad.components.LeyendaFiltros
import com.example.familiasquesuman.ui.screens.actividad.components.TarjetaActividadDia
import com.example.familiasquesuman.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


data class ActividadInscritaCalendario(val actividadId: Int, val fecha: String)

@Composable
fun CalendarioActividades(
    todasLasActividades: List<ActividadMock>,
    inscripciones: List<ActividadInscritaCalendario>,
    onActividadClick: (String) -> Unit,
    onVolver: () -> Unit = {}
) {
    val locale = Locale("es", "MX")
    val formatoBase = remember { SimpleDateFormat("yyyy-MM-dd", locale) }
    var mesActual by remember { mutableStateOf(Calendar.getInstance().apply {
                val fechaInicial = inscripciones.firstOrNull()?.fecha
                if (fechaInicial != null) {
                    formatoBase.parse(fechaInicial)?.let { time = it }
                }
                set(Calendar.DAY_OF_MONTH, 1)
            }
        )
    }
    var fechaSeleccionada by remember { mutableStateOf<String?>(null) }
    val actividadesPorFecha = remember(
        todasLasActividades,
        inscripciones
    ) { inscripciones
            .mapNotNull { inscripcion ->
                val actividad = todasLasActividades.firstOrNull { it.id == inscripcion.actividadId }
                if (actividad != null) {
                    inscripcion.fecha to actividad
                } else {
                    null
                }
            }
            .groupBy(keySelector = { it.first },
                valueTransform = { it.second }
            )
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Calendario de Actividades",
                color = AzulOscuro,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Consulta las actividades en las que tú y tu familia están inscritos.",
                color = TextoGrisActividad,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(onClick = {
                    mesActual = (mesActual.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
                    fechaSeleccionada = null
                },
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Box(modifier = Modifier.size(42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Mes anterior",
                        tint = AzulOscuro
                    )
                }
            }
            val nombreMes = SimpleDateFormat("MMMM", locale)
                    .format(mesActual.time)
                    .replaceFirstChar { it.uppercase() }
            Text(text = "$nombreMes ${mesActual.get(Calendar.YEAR)}",
                color = AzulOscuro,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Surface(onClick = {
                    mesActual = (mesActual.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
                    fechaSeleccionada = null
                },
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Box(modifier = Modifier.size(42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Mes siguiente",
                        tint = AzulOscuro
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp)
        )
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {

            listOf("DOM", "LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB"
            ).forEach { dia ->
                Text(text = dia,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = TextoGrisActividad,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        val primerDiaMes = (mesActual.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
        val espaciosIniciales = primerDiaMes.get(Calendar.DAY_OF_WEEK) - 1
        val diasDelMes = mesActual.getActualMaximum(Calendar.DAY_OF_MONTH)
        val totalCeldas = espaciosIniciales + diasDelMes
        val semanas = (totalCeldas + 6) / 7
        Column(modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            repeat(semanas) { semana ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    repeat(7) { columna ->
                        val posicion = semana * 7 + columna
                        val numeroDia = posicion - espaciosIniciales + 1
                        if (numeroDia in 1..diasDelMes) {
                            val fechaCalendario = (mesActual.clone() as Calendar).apply {
                                    set(Calendar.DAY_OF_MONTH, numeroDia)
                                }
                            val fecha = formatoBase.format(fechaCalendario.time)
                            val actividadesDia = actividadesPorFecha[fecha].orEmpty()
                            DiaCalendario(modifier = Modifier.weight(1f),
                                fecha = fecha,
                                seleccionado = fechaSeleccionada == fecha,
                                actividades = actividadesDia,
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
        HorizontalDivider(color = GrisBordeClaro,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        if (fechaSeleccionada == null) {
            Surface(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(18.dp),
                color = AmbarClaro
            ) {
                Text(text = "Selecciona un día con puntos para ver las actividades en las que estás inscrita.",
                    modifier = Modifier.padding(18.dp),
                    color = AzulOscuro,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }

        } else {
            val fecha = fechaSeleccionada!!
            val formatoFecha = SimpleDateFormat("EEEE, d 'de' MMMM", locale)
            val textoFecha = formatoBase
                    .parse(fecha)
                    ?.let { formatoFecha.format(it).replaceFirstChar { letra -> letra.uppercase() }
                    } ?: fecha
            Text(text = textoFecha,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = AzulOscuro,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            val actividadesSeleccionadas = actividadesPorFecha[fecha].orEmpty()
            if (actividadesSeleccionadas.isEmpty()) {
                Surface(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Text(text = "No tienes actividades inscritas para este día.",
                        modifier = Modifier.padding(18.dp),
                        color = TextoGrisActividad,
                        fontSize = 13.sp
                    )
                }

            } else {
                actividadesSeleccionadas.forEach { actividad ->
                    TarjetaActividadDia(
                        actividad = actividad.toDomain(),
                        onActividadClick = onActividadClick
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp)
        )
    }
}


@Composable
private fun DiaCalendario(
    modifier: Modifier,
    fecha: String,
    seleccionado: Boolean,
    actividades: List<ActividadMock>,
    onClick: () -> Unit
) {

    Column(modifier = modifier
            .height(64.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(
                    if (seleccionado) {
                        Ambar
                    } else {
                        Color.Transparent
                    }
                ),

            contentAlignment = Alignment.Center
        ) {

            Text(text = fecha
                        .substringAfterLast("-")
                        .toInt()
                        .toString(),
                color =
                    if (seleccionado) {
                        Color.White
                    } else {
                        AzulOscuro
                    },

                fontSize = 16.sp,
                fontWeight =
                    if (seleccionado) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Medium
                    }
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        if (actividades.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                actividades
                    .take(3)
                    .forEach { actividad ->
                        Box(modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(
                                    colorPuntoCategoria(
                                        actividad.categoria
                                    )
                                )
                        )
                    }
            }

        } else { Spacer( modifier = Modifier.height(6.dp)) }
    }
}


private fun colorPuntoCategoria(categoria: String): Color {
    return when (categoria) {
        "Medio Ambiente" -> FiltroVerde
        "Educación" -> FiltroAzul
        "Apoyo Social" -> FiltroNaranja
        "Otros" -> FiltroMorado
        else -> FiltroMorado
    }
}