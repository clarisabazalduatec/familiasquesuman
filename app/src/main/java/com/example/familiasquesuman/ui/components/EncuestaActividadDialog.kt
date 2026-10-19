package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.familiasquesuman.domain.EncuestaActividadRequest
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino

@Composable
fun EncuestaActividadDialog(
    actividadId: String,
    tituloActividad: String = "Actividad comunitaria",
    isLoading: Boolean = false,
    onDismissRequest: () -> Unit,
    onSubmit: (EncuestaActividadRequest) -> Unit
) {
    // Control del flujo de 2 pasos
    var pasoActual by remember { mutableIntStateOf(1) } // 1: Bienvenida, 2: Formulario

    // Estado del formulario
    var experienciaGeneral by remember { mutableIntStateOf(0) }
    var utilidadValor by remember { mutableIntStateOf(0) }
    var facilidadParticipacion by remember { mutableIntStateOf(0) }
    var comentarios by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFF8F9FA)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Barra superior de navegación / título de diálogo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChevronLeft,
                        contentDescription = "Volver",
                        tint = AzulMarino,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                if (pasoActual == 2) pasoActual = 1 else onDismissRequest()
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Encuesta de satisfacción",
                        color = AzulMarino,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (pasoActual == 1) {
                    // ==========================================
                    // PASO 1: VISTA DE BIENVENIDA
                    // ==========================================
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(20.dp))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Ilustración central (Portapapeles)
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .background(Color(0xFFFFF8E7), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AssignmentTurnedIn,
                                contentDescription = null,
                                tint = Ambar,
                                modifier = Modifier.size(56.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "¡Gracias por participar!",
                            color = AzulMarino,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Tu opinión nos ayuda a mejorar y seguir creando experiencias que generen un mayor impacto.",
                            color = AzulMarino.copy(alpha = 0.65f),
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Banner de tiempo estimado
                        Surface(
                            color = Color(0xFFFFF9E8),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = AzulMarino,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Esta encuesta es corta y toma menos de 2 minutos.",
                                    color = AzulMarino.copy(alpha = 0.8f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Botón Comenzar
                        Button(
                            onClick = { pasoActual = 2 },
                            colors = ButtonDefaults.buttonColors(containerColor = Ambar),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Comenzar encuesta",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    // ==========================================
                    // PASO 2: FORMULARIO DE PREGUNTAS
                    // ==========================================
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Tarjeta de Identificación del Proyecto/Actividad
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .background(Color(0xFFFFF8E7), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Handshake,
                                        contentDescription = null,
                                        tint = Ambar,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Actividad:",
                                        color = AzulMarino.copy(alpha = 0.5f),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = tituloActividad,
                                        color = AzulMarino,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Tu experiencia nos importa",
                                        color = AzulMarino.copy(alpha = 0.5f),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        // Pregunta 1: Experiencia General
                        TarjetaPreguntaRating(
                            icono = Icons.Outlined.SentimentSatisfied,
                            pregunta = "¿Cómo calificarías tu experiencia general en el proyecto?",
                            rating = experienciaGeneral,
                            onRatingChanged = { experienciaGeneral = it }
                        )

                        // Pregunta 2: Utilidad / Valor
                        TarjetaPreguntaRating(
                            icono = Icons.Outlined.People,
                            pregunta = "¿Qué tan útil y valiosa fue la actividad para ti?",
                            rating = utilidadValor,
                            onRatingChanged = { utilidadValor = it }
                        )

                        // Pregunta 3: Facilidad de participación
                        TarjetaPreguntaRating(
                            icono = Icons.Outlined.Lightbulb,
                            pregunta = "¿Qué tan fácil fue participar y completar las actividades?",
                            rating = facilidadParticipacion,
                            onRatingChanged = { facilidadParticipacion = it }
                        )

                        // Pregunta 4: Comentarios
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color(0xFFF0F4F8), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.ChatBubbleOutline,
                                            contentDescription = null,
                                            tint = AzulMarino,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "¿Tienes algún comentario o sugerencia para mejorar?",
                                        color = AzulMarino,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = comentarios,
                                    onValueChange = { comentarios = it },
                                    placeholder = {
                                        Text(
                                            "Escribe aquí...",
                                            color = Color.Gray,
                                            fontSize = 13.sp
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3,
                                    maxLines = 4,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color(0xFFE2E8F0),
                                        focusedBorderColor = Ambar
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Botones Cancelar / Enviar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismissRequest,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp),
                                shape = RoundedCornerShape(20.dp),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFCBD5E1))
                                )
                            ) {
                                Text("Cancelar", color = AzulMarino, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    val request = EncuestaActividadRequest(
                                        experienciaGeneral = if (experienciaGeneral == 0) 5 else experienciaGeneral,
                                        utilidadValor = if (utilidadValor == 0) 5 else utilidadValor,
                                        facilidadParticipacion = if (facilidadParticipacion == 0) 5 else facilidadParticipacion,
                                        comentarios = comentarios.ifBlank { null }
                                    )
                                    onSubmit(request)
                                },
                                enabled = !isLoading,
                                colors = ButtonDefaults.buttonColors(containerColor = Ambar),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Enviar", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaPreguntaRating(
    icono: ImageVector,
    pregunta: String,
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFF0F4F8), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = AzulMarino,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = pregunta,
                    color = AzulMarino,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 18.sp,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Selector de 5 estrellas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Estrella $i",
                        tint = if (i <= rating) Color(0xFFFFB800) else Color(0xFFD1D5DB),
                        modifier = Modifier
                            .size(30.dp)
                            .clickable { onRatingChanged(i) }
                    )
                }
            }
        }
    }
}