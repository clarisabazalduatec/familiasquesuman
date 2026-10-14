
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearAvisoOficialSheet(
    onCerrar: () -> Unit
) {
    if (!SesionManager.esAdmin) return

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    var incluirEvento by remember { mutableStateOf(false) }
    var incluirFecha by remember { mutableStateOf(false) }
    var incluirHorario by remember { mutableStateOf(false) }
    var incluirUbicacion by remember { mutableStateOf(false) }
    var incluirPublico by remember { mutableStateOf(false) }

    var fecha by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var publico by remember { mutableStateOf("") }

    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var documentoUri by remember { mutableStateOf<Uri?>(null) }
    var mostrarVistaPrevia by remember { mutableStateOf(false) }

    val seleccionarImagen = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        imagenUri = uri
    }

    val seleccionarDocumento = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        documentoUri = uri
    }

    val puedeContinuar = descripcion.isNotBlank() &&
            (!incluirEvento || (
                    (!incluirFecha || fecha.isNotBlank()) &&
                            (!incluirHorario || horario.isNotBlank()) &&
                            (!incluirUbicacion || ubicacion.isNotBlank()) &&
                            (!incluirPublico || publico.isNotBlank())
                    ))

    ModalBottomSheet(
        onDismissRequest = onCerrar,
        containerColor = CremaFondo,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Nuevo aviso oficial",
                style = MaterialTheme.typography.headlineSmall,
                color = AzulMarino
            )

            Text(
                text = "Comparte información importante con las familias.",
                style = MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción *") },
                placeholder = {
                    Text("Escribe aquí el contenido de tu aviso...")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                minLines = 5,
                shape = RoundedCornerShape(12.dp)
            )

            Text(
                text = "* Campo obligatorio",
                style = MaterialTheme.typography.labelSmall,
                color = GrisTexto
            )

            HorizontalDivider()

            Text(
                text = "Imagen (opcional)",
                style = MaterialTheme.typography.titleMedium,
                color = AzulMarino
            )

            if (imagenUri != null) {
                AsyncImage(
                    model = imagenUri,
                    contentDescription = "Imagen del anuncio",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
                    contentScale = ContentScale.Crop
                )

                TextButton(onClick = { imagenUri = null }) {
                    Text("Quitar imagen")
                }
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Detalles del evento",
                        style = MaterialTheme.typography.titleMedium,
                        color = AzulMarino
                    )

                    Text(
                        text = "Actívalo solo si tu aviso anuncia un evento.",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrisTexto
                    )
                }

                Switch(
                    checked = incluirEvento,
                    onCheckedChange = { incluirEvento = it }
                )
            }

            if (incluirEvento) {
                CampoOpcional(
                    titulo = "Fecha",
                    activo = incluirFecha,
                    onActivo = { incluirFecha = it },
                    valor = fecha,
                    onValor = { fecha = it },
                    placeholder = "Ej. Sábado, 15 de marzo"
                )

                CampoOpcional(
                    titulo = "Horario",
                    activo = incluirHorario,
                    onActivo = { incluirHorario = it },
                    valor = horario,
                    onValor = { horario = it },
                    placeholder = "Ej. 9:00 a.m. - 12:00 p.m."
                )

                CampoOpcional(
                    titulo = "Ubicación",
                    activo = incluirUbicacion,
                    onActivo = { incluirUbicacion = it },
                    valor = ubicacion,
                    onValor = { ubicacion = it },
                    placeholder = "Ej. Parque Los Encinos"
                )

                CampoOpcional(
                    titulo = "Dirigido a",
                    activo = incluirPublico,
                    onActivo = { incluirPublico = it },
                    valor = publico,
                    onValor = { publico = it },
                    placeholder = "Ej. Todas las familias"
                )
            }

            HorizontalDivider()

            Text(
                text = "Documento adjunto (opcional)",
                style = MaterialTheme.typography.titleMedium,
                color = AzulMarino
            )

            if (documentoUri == null) {
                OutlinedButton(
                    onClick = {
                        seleccionarDocumento.launch("application/pdf")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Outlined.AttachFile, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Adjuntar PDF")
                }
            } else {
                Text(
                    text = "PDF seleccionado",
                    color = AzulMarino
                )

                TextButton(onClick = { documentoUri = null }) {
                    Text("Quitar documento")
                }
            }

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = { mostrarVistaPrevia = true },
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
                Text(
                    text = "Revisar aviso",
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Vista previa solamente. El envío al backend se conectará después.",
                style = MaterialTheme.typography.bodySmall,
                color = GrisTexto
            )

            Spacer(Modifier.height(24.dp))
        }
    }

    if (mostrarVistaPrevia) {
        AlertDialog(
            onDismissRequest = { mostrarVistaPrevia = false },
            title = {
                Text(
                    text = titulo.ifBlank { "Aviso oficial" },
                    color = AzulMarino
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(descripcion.trim())

                    if (imagenUri != null) {
                        Text("Imagen adjunta")
                    }

                    if (incluirEvento) {
                        if (incluirFecha) Text("Fecha: $fecha")
                        if (incluirHorario) Text("Horario: $horario")
                        if (incluirUbicacion) Text("Ubicación: $ubicacion")
                        if (incluirPublico) Text("Dirigido a: $publico")
                    }

                    if (documentoUri != null) {
                        Text("Documento PDF adjunto")
                    }

                    HorizontalDivider()

                    Text(
                        "Este aviso todavía no se ha publicado.",
                        color = GrisTexto,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { mostrarVistaPrevia = false }
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
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titulo,
                modifier = Modifier.weight(1f),
                color = AzulMarino,
                style = MaterialTheme.typography.bodyMedium
            )

            Switch(
                checked = activo,
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
