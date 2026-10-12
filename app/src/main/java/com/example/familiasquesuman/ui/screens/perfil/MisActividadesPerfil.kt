package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// =====================================================
// MODELO DE ACTIVIDAD
// =====================================================

data class ActividadInscrita(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val fechaHora: Long,
    val ubicacion: String,
    val inscritos: List<String>,
    val asistenciaConfirmada: Boolean = false
)

data class ResumenCancelacion(val actividad: String, val familiares: List<String>)


@Composable
fun MisActividadesPerfil() { var actividades by remember { mutableStateOf(listOf(
                ActividadInscrita(
                    id = 1,
                    nombre = "Reforestación en el Parque La Huasteca",
                    categoria = "Medio ambiente",
                    fechaHora = System.currentTimeMillis() + (48L * 60L * 60L * 1000L),
                    ubicacion = "Parque La Huasteca, Monterrey, N.L.",
                    inscritos = listOf("Mariana", "Sofía")
                ),
                ActividadInscrita(
                    id = 2,
                    nombre = "Colecta de alimentos en Banco de Alimentos",
                    categoria = "Donación",
                    fechaHora = System.currentTimeMillis() + (12L * 60L * 60L * 1000L),
                    ubicacion = "Banco de Alimentos, Monterrey, N.L.",
                    inscritos = listOf("Mariana")
                ),

                ActividadInscrita(
                    id = 3,
                    nombre = "Jornada de adopción de mascotas",
                    categoria = "Comunidad",
                    fechaHora = System.currentTimeMillis() + (72L * 60L * 60L * 1000L),
                    ubicacion = "Parque Central, Monterrey, N.L.",
                    inscritos = listOf("Mariana"),
                    asistenciaConfirmada = true
                )
            )
        )
    }

    var actividadSeleccionada by remember { mutableStateOf<ActividadInscrita?>(null) }
    var familiaresSeleccionados by remember { mutableStateOf<Set<String>>(emptySet()) }
    var mostrarSeleccionFamiliares by remember { mutableStateOf(false) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var resumenCancelacion by remember { mutableStateOf<ResumenCancelacion?>(null) }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(text = "Mis actividades",
            color = AzulMarino,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Text(text = "Revisa tus registros y cancela con al menos 24 horas de anticipación.",
            color = AzulMarino.copy(alpha = 0.65f),
            fontSize = 14.sp,
            lineHeight = 19.sp
        )

        if (actividades.isEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        tint = AzulMarino.copy(alpha = 0.35f),
                        modifier = Modifier.size(42.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Aún no tienes actividades registradas.",
                        color = AzulMarino.copy(alpha = 0.60f),
                        fontSize = 15.sp
                    )
                }
            }

        } else {
            actividades.forEach { actividad -> ActividadInscritaCard(
                    actividad = actividad,
                    onCancelar = {
                        actividadSeleccionada = actividad
                        familiaresSeleccionados = emptySet()
                        if (actividad.inscritos.size > 1) {
                            mostrarSeleccionFamiliares = true
                        } else {
                            familiaresSeleccionados = actividad.inscritos.toSet()
                            mostrarConfirmacion = true
                        }
                    }
                )
            }
        }
    }

    if (mostrarSeleccionFamiliares && actividadSeleccionada != null) {
        SeleccionarFamiliaresDialog(
            actividad = actividadSeleccionada!!,
            seleccionados = familiaresSeleccionados,
            onSeleccionar = { familiar -> familiaresSeleccionados =
                    if (familiar in familiaresSeleccionados) {
                        familiaresSeleccionados - familiar
                    } else {
                        familiaresSeleccionados + familiar
                    }
            },
            onContinuar = {
                mostrarSeleccionFamiliares = false
                mostrarConfirmacion = true
            },
            onCancelar = {
                mostrarSeleccionFamiliares = false
                familiaresSeleccionados = emptySet()
                actividadSeleccionada = null
            }
        )
    }

    if (mostrarConfirmacion && actividadSeleccionada != null) {
        ConfirmarCancelacionDialog(
            actividad = actividadSeleccionada!!,
            familiares = familiaresSeleccionados.toList(),
            onConfirmar = { val actividad = actividadSeleccionada ?: return@ConfirmarCancelacionDialog
                val familiaresCancelados = familiaresSeleccionados.toList()
                actividades = actividades.mapNotNull { actividadActual ->
                        if (actividadActual.id != actividad.id) {
                            actividadActual
                        } else {
                            val familiaresRestantes = actividadActual
                                    .inscritos
                                    .filterNot { familiar -> familiar in familiaresSeleccionados }
                            if (familiaresRestantes.isEmpty()) {
                                null
                            } else {
                                actividadActual.copy(inscritos = familiaresRestantes)
                            }
                        }
                    }

                resumenCancelacion = ResumenCancelacion(
                        actividad = actividad.nombre,
                        familiares = familiaresCancelados
                    )
                mostrarConfirmacion = false
                familiaresSeleccionados = emptySet()
                actividadSeleccionada = null
            },

            onRegresar = { mostrarConfirmacion = false

                if ((actividadSeleccionada?.inscritos?.size ?: 0) > 1) {
                    mostrarSeleccionFamiliares = true
                } else {
                    familiaresSeleccionados = emptySet()
                    actividadSeleccionada = null
                }
            }
        )
    }

    resumenCancelacion?.let { resumen -> CancelacionExitosaDialog(
            resumen = resumen,
            onCerrar = { resumenCancelacion = null }
        )
    }
}

@Composable
fun ActividadInscritaCard(actividad: ActividadInscrita, onCancelar: () -> Unit) {
    val ahora = System.currentTimeMillis()
    val diferencia = actividad.fechaHora - ahora
    val limite24Horas = 24L * 60L * 60L * 1000L
    val puedeCancelar = diferencia >= limite24Horas && !actividad.asistenciaConfirmada
    val formatoFecha = SimpleDateFormat("EEE, dd 'de' MMM 'de' yyyy", Locale("es", "MX"))
    val formatoHora = SimpleDateFormat("hh:mm a", Locale("es", "MX"))

    Card(modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.size(
                            width = 100.dp,
                            height = 115.dp
                        )
                        .background(color = Color(0xFFECEDEF), shape = RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Imagen", color = Color.Gray, fontSize = 12.sp)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = actividad.nombre,
                            color = AzulMarino,
                            fontSize = 15.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        EstadoActividad(actividad = actividad, puedeCancelar = puedeCancelar)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    CategoriaActividad(categoria = actividad.categoria)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "📅 ${formatoFecha.format(Date(actividad.fechaHora))}",
                        color = AzulMarino.copy(alpha = 0.65f),
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(text = "◷ ${formatoHora.format(Date(actividad.fechaHora))}",
                        color = AzulMarino.copy(alpha = 0.65f),
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(text = "📍 ${actividad.ubicacion}",
                        color = AzulMarino.copy(alpha = 0.65f),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            HorizontalDivider(color = AzulMarino.copy(alpha = 0.08f))
            Text(text = if (actividad.inscritos.size == 1) { "Inscrita: ${actividad.inscritos.first()}"
                    } else {
                        "Inscritos: ${actividad.inscritos.joinToString(", ")}"
                    },
                color = AzulMarino.copy(alpha = 0.65f),
                fontSize = 13.sp
            )
            when {
                actividad.asistenciaConfirmada -> {
                    Box(modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFF1F3F5),
                                shape = RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Asistencia confirmada",
                            color = AzulMarino.copy(alpha = 0.65f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                puedeCancelar -> { OutlinedButton(
                        onClick = onCancelar,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(width = 1.5.dp, color = Ambar),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Ambar)
                    ) {
                    Text(text = "Cancelar inscripción", fontWeight = FontWeight.SemiBold)
                    }
                }

                else -> {
                    Box(modifier = Modifier.fillMaxWidth().background(
                                color = Color(0xFFF1F1F1),
                                shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Faltan menos de 24 h · ya no se puede cancelar",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
private fun SeleccionarFamiliaresDialog(
    actividad: ActividadInscrita,
    seleccionados: Set<String>,
    onSeleccionar: (String) -> Unit,
    onContinuar: () -> Unit,
    onCancelar: () -> Unit
) {

    Dialog(onDismissRequest = onCancelar) {
        Surface(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White,
            tonalElevation = 6.dp) {
            Column(modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Box(modifier = Modifier.size(58.dp).background(
                            color = Ambar.copy(alpha = 0.15f),
                            shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        tint = Ambar,
                        modifier = Modifier.size(30.dp))
                }
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(text = "¿A quién deseas cancelar?",
                        color = AzulMarino,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold)
                    Text(text = "Selecciona a los integrantes de tu familia cuya inscripción deseas cancelar.",
                        color = AzulMarino.copy(alpha = 0.65f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp)
                }
                Surface(modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFF7E8)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "Actividad",
                            color = AzulMarino.copy(alpha = 0.50f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = actividad.nombre,
                            color = AzulMarino,
                            fontSize = 15.sp,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(text = "Familiares registrados",
                    color = AzulMarino,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold)
                actividad.inscritos.forEach { familiar -> val estaSeleccionado = familiar in seleccionados
                        Surface(modifier = Modifier.fillMaxWidth().clickable { onSeleccionar(familiar) },
                            shape = RoundedCornerShape(16.dp),
                            color =
                                if (estaSeleccionado) {
                                    Ambar.copy(alpha = 0.12f)
                                } else {
                                    Color(0xFFF7F7F7)
                                },
                            border = BorderStroke(
                                    width =
                                        if (estaSeleccionado) {
                                            1.5.dp
                                        } else {
                                            1.dp
                                        },

                                    color =
                                        if (estaSeleccionado) {
                                            Ambar
                                        } else {
                                            Color(0xFFE7E7E7)
                                        }
                                )
                        ) {

                            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 11.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(42.dp)
                                            .background(color =
                                                    if (estaSeleccionado) {
                                                        Ambar
                                                    } else {
                                                        AzulMarino.copy(alpha = 0.08f)
                                                    },
                                                shape = CircleShape
                                            ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = familiar.take(1).uppercase(),
                                        color =
                                            if (estaSeleccionado) {
                                                Color.White

                                            } else {
                                                AzulMarino
                                            },
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = familiar,
                                    color = AzulMarino,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier =Modifier.weight(1f)
                                )
                                Checkbox(checked = estaSeleccionado,
                                    onCheckedChange = { onSeleccionar(familiar) },
                                    colors = CheckboxDefaults.colors(checkedColor = Ambar)
                                )
                            }
                        }
                    }
                Button(onClick = onContinuar,
                    enabled = seleccionados.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar, disabledContainerColor = Color(0xFFE3E3E3))
                ) {
                    Text(text = "Continuar",
                        color =
                            if (seleccionados.isNotEmpty()
                            ) {
                                Color.White
                            } else {
                                Color.Gray
                            },
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                TextButton(onClick = onCancelar,
                    modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Volver", color = AzulMarino.copy(alpha = 0.65f))
                }
            }
        }
    }
}

@Composable
private fun ConfirmarCancelacionDialog(
    actividad: ActividadInscrita,
    familiares: List<String>,
    onConfirmar: () -> Unit,
    onRegresar: () -> Unit
) {
    Dialog(onDismissRequest = onRegresar) {
        Surface(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Box(modifier = Modifier.size(58.dp).background(
                            color = Color(0xFFFFF0D6),
                            shape =CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = null,
                        tint = Ambar,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Column { Text(text = "Confirmar cancelación",
                    color = AzulMarino, fontSize = 22.sp,
                    fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Revisa la información antes de continuar.",
                        color = AzulMarino.copy(alpha = 0.65f),
                        fontSize = 14.sp
                    )
                }
                Surface(modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFF8F8F8)
                ) {
                    Column(modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "Actividad",
                            color = AzulMarino.copy(alpha = 0.50f),
                            fontSize = 11.sp
                        )
                        Text(text = actividad.nombre,
                            color = AzulMarino,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Bold)
                        HorizontalDivider(color = AzulMarino.copy(alpha = 0.08f))
                        Text(text = if (familiares.size == 1) {
                                    "Se cancelará la inscripción de:"
                                } else {
                                    "Se cancelarán las inscripciones de:"
                                },
                            color = AzulMarino.copy(alpha = 0.65f),
                            fontSize = 13.sp
                        )
                        familiares.forEach { familiar ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier
                                                .size(30.dp)
                                                .background(
                                                    color = Ambar.copy(alpha = 0.15f),
                                                    shape = CircleShape
                                                ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = familiar.take(1).uppercase(),
                                            color = Ambar,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = familiar,
                                        color = AzulMarino,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                    }
                }
                Text(text = "Esta acción retirará la inscripción de los familiares seleccionados.",
                    color = AzulMarino.copy(alpha = 0.60f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Button(onClick = onConfirmar,
                    modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                52.dp
                            ),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar)) {
                    Text(text = "Sí, cancelar inscripción",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                OutlinedButton(onClick = onRegresar,
                    modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                            width = 1.dp,
                            color = AzulMarino.copy(alpha = 0.25f)
                        )
                ) {
                    Text(text = "Regresar", color = AzulMarino)
                }
            }
        }
    }
}

@Composable
private fun CancelacionExitosaDialog(resumen: ResumenCancelacion, onCerrar: () -> Unit)
{ Dialog(onDismissRequest = onCerrar) {
        Surface(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Box(modifier = Modifier
                            .size(72.dp)
                            .background(color = Color(0xFFE4F4E8), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF388E3C),
                        modifier = Modifier.size(42.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Cancelación realizada",
                        color = AzulMarino,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "La inscripción fue cancelada correctamente.",
                        color = AzulMarino.copy(alpha = 0.65f),
                        fontSize = 14.sp
                    )
                }
                Surface(modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFF8F8F8)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "Actividad",
                            color = AzulMarino.copy(alpha = 0.50f),
                            fontSize = 11.sp
                        )
                        Text(text = resumen.actividad,
                            color = AzulMarino,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            lineHeight = 19.sp
                        )
                        HorizontalDivider(color = AzulMarino.copy(alpha = 0.08f))
                        Text(text =
                                if (resumen.familiares.size == 1) {
                                    "Inscripción cancelada para:"
                                } else {
                                    "Inscripciones canceladas para:"
                                },
                            color = AzulMarino.copy(alpha = 0.60f),
                            fontSize = 13.sp
                        )
                        resumen.familiares
                            .forEach { familiar -> Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(26.dp)
                                                .background(color = Color(0xFFE4F4E8), shape = CircleShape),
                                        contentAlignment = Alignment.Center) {
                                        Text(text = "✓",
                                            color = Color(0xFF388E3C),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(9.dp))
                                    Text(text = familiar, color = AzulMarino, fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                    }
                }
                Button(onClick = onCerrar,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar)
                ) {
                    Text(text = "Listo",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoriaActividad(categoria: String) {
    val fondo =
        when (categoria) {
            "Medio ambiente" -> Color(0xFFE2F3E5)
            "Donación" -> Color(0xFFE4EEFF)
            else -> Color(0xFFFFE7E7
            )
        }
    val texto =
        when (categoria) {
            "Medio ambiente" -> Color(0xFF388E3C)
            "Donación" -> Color(0xFF1976D2)
            else -> Color(  0xFFC44B4B)
        }
    Box( modifier =   Modifier .background(color =  fondo, shape =  RoundedCornerShape( 20.dp ))
                .padding(  horizontal =  10.dp,  vertical =   4.dp  )) {
        Text(text = categoria,
            color =  texto,
            fontSize =  11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
@Composable
private fun EstadoActividad( actividad: ActividadInscrita, puedeCancelar: Boolean) {
    val texto: String
    val fondo: Color
    val colorTexto: Color

    if ( actividad.asistenciaConfirmada ) {
        texto = "Asistencia\nconfirmada"
        fondo =  Color(  0xFFE9EEF4  )
        colorTexto = AzulMarino.copy( alpha =  0.70f )
    } else if (puedeCancelar  ) {
        texto = "Próxima"
        fondo = Color( 0xFFE2F3E5 )
        colorTexto =  Color( 0xFF388E3C  )
    } else {
        texto = "En menos\nde 24 h"
        fondo = Color(  0xFFFFE4E4 )
        colorTexto = Color( 0xFFD64A4A )
    }
    Box(  modifier = Modifier .background(color =  fondo, shape =  RoundedCornerShape(  12.dp ))
                .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text( text = texto,
            color =  colorTexto,
            fontSize =  9.sp,
            lineHeight =  11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}