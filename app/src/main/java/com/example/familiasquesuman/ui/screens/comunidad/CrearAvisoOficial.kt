
package com.example.familiasquesuman.ui.screens.comunidad

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.ui.theme.*
import androidx.compose.ui.platform.LocalContext
import com.example.familiasquesuman.data.repository.ImagenRepository
import com.example.familiasquesuman.data.repository.ComunidadRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearAvisoOficialSheet(onCerrar: () -> Unit, onPublicado: () -> Unit) {
    if (!SesionManager.esAdmin) return
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var incluirEvento by remember { mutableStateOf(false) }
    var incluirPublico by remember { mutableStateOf(false) }
    var fechaMillis by remember { mutableStateOf<Long?>(null) }
    var horaInicio by remember { mutableStateOf<Int?>(null) }
    var minutoInicio by remember { mutableStateOf<Int?>(null) }
    var horaFin by remember { mutableStateOf<Int?>(null) }
    var minutoFin by remember { mutableStateOf<Int?>(null) }
    var mostrarCalendario by remember { mutableStateOf(false) }
    var mostrarHoraInicio by remember { mutableStateOf(false) }
    var mostrarHoraFin by remember { mutableStateOf(false) }
    var publico by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var mostrarVistaPrevia by remember { mutableStateOf(false) }
    var estaPublicando by remember { mutableStateOf(false) }
    var errorPublicacion by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imagenRepository = remember { ImagenRepository() }
    val comunidadRepository = remember { ComunidadRepository() }
    val seleccionarImagen = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> imagenUri = uri }
    val inicioMinutos = if (horaInicio != null && minutoInicio != null) {
        horaInicio!! * 60 + minutoInicio!!
    } else null
    val finMinutos = if (horaFin != null && minutoFin != null) {
        horaFin!! * 60 + minutoFin!!
    } else null
    val horarioValido = inicioMinutos != null && finMinutos != null && finMinutos > inicioMinutos
    val puedeContinuar = descripcion.isNotBlank() &&
            (!incluirEvento || (fechaMillis != null && horarioValido && (!incluirPublico || publico.isNotBlank())))
    ModalBottomSheet(onDismissRequest = onCerrar,
        containerColor = CremaFondo,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    ) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = "Nuevo aviso oficial",
                    style = MaterialTheme.typography.headlineSmall,
                    color = AzulMarino
                )
                Text(text = "Comparte información importante con las familias.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )
                OutlinedTextField(value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                OutlinedTextField(value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción *") },
                    placeholder = { Text("Escribe aquí el contenido de tu aviso...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp),
                    minLines = 5,
                    shape = RoundedCornerShape(12.dp)
                )
                Text(text = "* Campo obligatorio",
                    style = MaterialTheme.typography.labelSmall,
                    color = GrisTexto
                )
                HorizontalDivider()
                Text(text = "Imagen (opcional)",
                    style = MaterialTheme.typography.titleMedium,
                    color = AzulMarino
                )
                if (imagenUri != null) {
                    AsyncImage(model = imagenUri,
                        contentDescription = "Imagen del anuncio",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp),
                        contentScale = ContentScale.Crop
                    )
                    TextButton(onClick = { imagenUri = null }) { Text("Quitar imagen") }
                } else {
                    OutlinedButton(
                        onClick = { seleccionarImagen.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Outlined.AddPhotoAlternate, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Agregar imagen")
                    }
                }
                HorizontalDivider()
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Detalles del evento",
                            style = MaterialTheme.typography.titleMedium,
                            color = AzulMarino
                        )
                    }
                    Switch(checked = incluirEvento, onCheckedChange = { incluirEvento = it }) }
                if (incluirEvento) {
                    Text(text = "Fecha del evento *",
                        color = AzulMarino,
                        style = MaterialTheme.typography.titleSmall
                    )
                    OutlinedButton(onClick = { mostrarCalendario = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(fechaMillis?.let { formatearFechaEvento(it) } ?: "Seleccionar fecha")
                    }
                    Text(text = "Horario del evento *",
                        color = AzulMarino,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(onClick = { mostrarHoraInicio = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                if (horaInicio != null && minutoInicio != null)
                                    String.format(Locale.US, "%02d:%02d", horaInicio, minutoInicio)
                                else "Inicio"
                            )
                        }
                        OutlinedButton(onClick = { mostrarHoraFin = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                if (horaFin != null && minutoFin != null)
                                    String.format(Locale.US, "%02d:%02d", horaFin, minutoFin)
                                else "Fin"
                            )
                        }
                    }
                    if (inicioMinutos != null && finMinutos != null && !horarioValido) {
                        Text(text = "La hora de finalización debe ser posterior a la de inicio.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    CampoOpcional(titulo = "Dirigido a",
                        activo = incluirPublico,
                        onActivo = { incluirPublico = it },
                        valor = publico,
                        onValor = { publico = it },
                        placeholder = "Ej. Todas las familias"
                    )
                }
            }
            HorizontalDivider()
            Spacer(Modifier.height(10.dp))
            Button(onClick = { mostrarVistaPrevia = true },
                enabled = puedeContinuar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Ambar,
                    contentColor = AzulMarino
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Revisar aviso",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    if (mostrarCalendario) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = fechaMillis)
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        fechaMillis = datePickerState.selectedDateMillis
                        mostrarCalendario = false
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarCalendario = false }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    if (mostrarHoraInicio) {
        val timeState = rememberTimePickerState(
            initialHour = horaInicio ?: 9,
            initialMinute = minutoInicio ?: 0,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { mostrarHoraInicio = false },
            title = { Text("Hora de inicio") },
            text = {
                TimePicker(state = timeState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        horaInicio = timeState.hour
                        minutoInicio = timeState.minute
                        mostrarHoraInicio = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarHoraInicio = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
    if (mostrarHoraFin) {
        val timeState = rememberTimePickerState(
            initialHour = horaFin ?: 12,
            initialMinute = minutoFin ?: 0,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { mostrarHoraFin = false },
            title = { Text("Hora de finalización") },
            text = {
                TimePicker(state = timeState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        horaFin = timeState.hour
                        minutoFin = timeState.minute
                        mostrarHoraFin = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarHoraFin = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
    if (mostrarVistaPrevia) {
        AlertDialog(
            onDismissRequest = {
                if (!estaPublicando) mostrarVistaPrevia = false
            },
            title = { Text(text = titulo.ifBlank { "Aviso oficial" }, color = AzulMarino) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(descripcion.trim())
                    if (imagenUri != null) {
                        Text("Imagen adjunta")
                    }
                    if (incluirEvento) {
                        fechaMillis?.let { Text("Fecha: ${formatearFechaEvento(it)}") }
                        if (horaInicio != null && minutoInicio != null &&
                            horaFin != null && minutoFin != null
                        ) {
                            val inicio = String.format(Locale.US, "%02d:%02d", horaInicio, minutoInicio)
                            val fin = String.format(Locale.US, "%02d:%02d", horaFin, minutoFin)
                            Text("Horario: $inicio - $fin")
                        }
                        if (incluirPublico) {
                            Text("Dirigido a: $publico")
                        }
                    }
                    HorizontalDivider()
                    Text("Este aviso todavía no se ha publicado.",
                        color = GrisTexto,
                        style = MaterialTheme.typography.bodySmall
                    )
                    errorPublicacion?.let { mensaje ->
                        Text(text = mensaje,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = { Button(onClick = {
                        estaPublicando = true
                        errorPublicacion = null
                        scope.launch {
                            try {
                                val urlsImagenes = imagenUri?.let { uri ->
                                    listOf(
                                        imagenRepository.subirSiHaceFalta(
                                            resolver = context.contentResolver,
                                            url = uri.toString()
                                        )
                                    )
                                } ?: emptyList()
                                comunidadRepository.crearAvisoOficial(
                                    titulo = titulo.trim().ifBlank { null },
                                    contenido = descripcion.trim(),
                                    imagenes = urlsImagenes,
                                    eventoIniciaEn = if (incluirEvento) {
                                        fechaHoraIso(
                                            fechaMillis!!,
                                            horaInicio!!,
                                            minutoInicio!!
                                        )
                                    } else null,
                                    eventoTerminaEn = if (incluirEvento) {
                                        fechaHoraIso(
                                            fechaMillis!!,
                                            horaFin!!,
                                            minutoFin!!
                                        )
                                    } else null,
                                    publicoObjetivo = if (incluirEvento && incluirPublico) {
                                        publico.trim().ifBlank { null }
                                    } else null
                                ).getOrThrow()
                                mostrarVistaPrevia = false
                                onPublicado()
                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: Exception) {
                                errorPublicacion = e.message
                                    ?: "No se pudo publicar el aviso."
                            } finally {
                                estaPublicando = false
                            }
                        }
                    },
                    enabled = !estaPublicando &&
                            puedeContinuar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Ambar,
                        contentColor = AzulMarino
                    )
                ) {
                    if (estaPublicando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = AzulMarino
                        )
                    } else {
                        Text("Publicar aviso")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarVistaPrevia = false },
                    enabled = !estaPublicando
                ) {
                    Text("Volver a editar")
                }
            }
        )
    }
}
    @Composable
    private fun CampoOpcional(
        titulo: String,
        activo: Boolean,
        onActivo: (Boolean) -> Unit,
        valor: String,
        onValor: (String) -> Unit,
        placeholder: String
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = titulo,
                    modifier = Modifier.weight(1f),
                    color = AzulMarino,
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(checked = activo,
                    onCheckedChange = onActivo
                )
            }
            if (activo) {
                OutlinedTextField(
                    value = valor,
                    onValueChange = onValor,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(placeholder) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

        }
    }
}
    private fun formatearFechaEvento(millis: Long): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale("es", "MX")).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date(millis))
    }
    private fun fechaHoraIso(
        fechaMillis: Long,
        hora: Int,
        minuto: Int
    ): String {
        val fechaUtc = Calendar.getInstance(
            TimeZone.getTimeZone("UTC")
        ).apply {
            timeInMillis = fechaMillis
        }
        val fechaLocal = Calendar.getInstance().apply {
            set(Calendar.YEAR, fechaUtc.get(Calendar.YEAR))
            set(Calendar.MONTH, fechaUtc.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, fechaUtc.get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, hora)
            set(Calendar.MINUTE, minuto)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            Locale.US
        ).format(fechaLocal.time)
    }

