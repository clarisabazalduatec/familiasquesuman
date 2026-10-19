package com.example.familiasquesuman.ui.screens.actividad.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.familiasquesuman.data.remote.dto.EncuestaInicialRequest
import com.example.familiasquesuman.ui.theme.*

data class OpcionPregunta(
    val texto: String,
    val icono: ImageVector
)

val opcionesMotivos = listOf(
    OpcionPregunta("Ayudar a mi comunidad", Icons.Outlined.Groups),
    OpcionPregunta("Aprender algo nuevo", Icons.Outlined.School),
    OpcionPregunta("Compartir tiempo en familia", Icons.Outlined.Favorite),
    OpcionPregunta("Otro (especifica)", Icons.Outlined.MoreHoriz)
)

val opcionesExpectativas = listOf(
    OpcionPregunta("Adquirir nuevas habilidades", Icons.Outlined.School),
    OpcionPregunta("Conocer a más personas", Icons.Outlined.Groups),
    OpcionPregunta("Contribuir a la comunidad", Icons.Outlined.Favorite),
    OpcionPregunta("Otro (especifica)", Icons.Outlined.MoreHoriz)
)

@Composable
fun EncuestaInicialDialog(
    isLoading: Boolean = false,
    onSubmit: (EncuestaInicialRequest) -> Unit
) {
    var motivosElegidos by remember { mutableStateOf(setOf<String>()) }
    var otroMotivoTexto by remember { mutableStateOf("") }

    var expectativasElegidas by remember { mutableStateOf(setOf<String>()) }
    var otraExpectativaTexto by remember { mutableStateOf("") }

    // Propiedades para impedir cerrar la encuesta tocando afuera o con el botón Atrás
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Blanco,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Text(
                    text = "Encuesta inicial",
                    style = MaterialTheme.typography.titleLarge,
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold
                )

                // Pregunta 1
                SeccionPregunta(
                    icono = Icons.Outlined.FavoriteBorder,
                    titulo = "¿Por qué te interesó esta actividad?",
                    subtitulo = "Selecciona una o varias opciones.",
                    opciones = opcionesMotivos,
                    seleccionados = motivosElegidos,
                    otroTexto = otroMotivoTexto,
                    onOtroTextoChange = { otroMotivoTexto = it },
                    onOptionToggle = { texto ->
                        motivosElegidos = if (motivosElegidos.contains(texto)) {
                            motivosElegidos - texto
                        } else {
                            motivosElegidos + texto
                        }
                    }
                )

                HorizontalDivider(Modifier, thickness = 1.dp, color = CremaFondo)

                // Pregunta 2
                SeccionPregunta(
                    icono = Icons.Outlined.StarBorder,
                    titulo = "¿Qué esperas lograr al participar?",
                    subtitulo = "Puedes seleccionar más de una opción.",
                    opciones = opcionesExpectativas,
                    seleccionados = expectativasElegidas,
                    otroTexto = otraExpectativaTexto,
                    onOtroTextoChange = { otraExpectativaTexto = it },
                    onOptionToggle = { texto ->
                        expectativasElegidas = if (expectativasElegidas.contains(texto)) {
                            expectativasElegidas - texto
                        } else {
                            expectativasElegidas + texto
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botón principal (ocupa todo el ancho, sin opción de omitir)
                Button(
                    onClick = {
                        val req = EncuestaInicialRequest(
                            motivos = motivosElegidos.toList().ifEmpty { listOf("General") },
                            otroMotivo = if (motivosElegidos.contains("Otro (especifica)")) otroMotivoTexto else null,
                            expectativas = expectativasElegidas.toList().ifEmpty { listOf("General") },
                            otraExpectativa = if (expectativasElegidas.contains("Otro (especifica)")) otraExpectativaTexto else null
                        )
                        onSubmit(req)
                    },
                    enabled = !isLoading && (motivosElegidos.isNotEmpty() && expectativasElegidas.isNotEmpty()),
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Blanco, modifier = Modifier.size(20.dp))
                    } else {
                        Text("Enviar respuestas", color = Blanco, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SeccionPregunta(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    opciones: List<OpcionPregunta>,
    seleccionados: Set<String>,
    otroTexto: String,
    onOtroTextoChange: (String) -> Unit,
    onOptionToggle: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(AmbarClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icono, contentDescription = null, tint = AzulMarino, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = titulo, color = AzulMarino, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = subtitulo, color = GrisTexto, fontSize = 12.sp)
            }
        }

        opciones.forEach { opcion ->
            val checked = seleccionados.contains(opcion.texto)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (checked) AmbarClaro.copy(alpha = 0.3f) else Blanco,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (checked) Ambar else Color.LightGray.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionToggle(opcion.texto) }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { onOptionToggle(opcion.texto) },
                        colors = CheckboxDefaults.colors(checkedColor = Ambar)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(CremaFondo, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = opcion.icono, contentDescription = null, tint = AzulMarino, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = opcion.texto, color = AzulMarino, fontSize = 13.sp)
                }
            }

            if (opcion.texto == "Otro (especifica)" && checked) {
                OutlinedTextField(
                    value = otroTexto,
                    onValueChange = onOtroTextoChange,
                    placeholder = { Text("Escribe aquí...", color = GrisTexto, fontSize = 13.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Ambar,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            }
        }
    }
}