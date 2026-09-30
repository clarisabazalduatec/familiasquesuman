package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.ui.model.HijoState
import com.example.familiasquesuman.ui.screens.perfil.PerfilUiState
import com.example.familiasquesuman.ui.theme.*
import java.util.Calendar
import java.util.TimeZone

@Composable
fun FormularioFamiliaCard(
    uiState: PerfilUiState,
    listaHijos: List<HijoState>,
    onAgregarHijo: () -> Unit,
    onEliminarHijo: (HijoState) -> Unit,
    onCancelar: () -> Unit,
    onGuardar: (mama: String, papa: String, whatsapp: String, email: String, ciudad: String) -> Unit
) {
    var mama by remember { mutableStateOf(uiState.mama) }
    var papa by remember { mutableStateOf(uiState.papa) }
    var whatsapp by remember { mutableStateOf(uiState.whatsapp) }
    var email by remember { mutableStateOf(uiState.email) }
    var ciudad by remember { mutableStateOf(uiState.ciudad) }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onCancelar, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = AzulMarino
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Datos de la familia",
                    style = MaterialTheme.typography.titleLarge,
                    color = AzulMarino,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            CampoInput(valor = mama, onValueChange = { mama = it }, icono = Icons.Outlined.Person, etiqueta = "Mamá", placeholder = "Nombre de la mamá")
            Spacer(modifier = Modifier.height(14.dp))
            CampoInput(valor = papa, onValueChange = { papa = it }, icono = Icons.Outlined.Person, etiqueta = "Papá", placeholder = "Nombre del papá")
            Spacer(modifier = Modifier.height(14.dp))
            CampoInput(valor = whatsapp, onValueChange = { whatsapp = it }, icono = Icons.Outlined.Phone, etiqueta = "WhatsApp", placeholder = "81 1234 5678")
            Spacer(modifier = Modifier.height(14.dp))
            CampoInput(valor = email, onValueChange = { email = it }, icono = Icons.Outlined.Email, etiqueta = "Email", placeholder = "correo@ejemplo.com")
            Spacer(modifier = Modifier.height(14.dp))
            // Ciudad en formato de texto abierto
            CampoInput(valor = ciudad, onValueChange = { ciudad = it }, icono = Icons.Outlined.LocationOn, etiqueta = "Ciudad", placeholder = "Monterrey")

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.PersonOutline, contentDescription = null, tint = GrisTexto)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Hijos",
                        style = MaterialTheme.typography.titleMedium,
                        color = AzulMarino,
                        fontWeight = FontWeight.Bold
                    )
                }
                TextButton(onClick = onAgregarHijo) {
                    Text("+ Agregar", color = AzulMarino, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (listaHijos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Face, contentDescription = null, tint = GrisTexto)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sin hijos registrados",
                            color = GrisTexto,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    listaHijos.forEach { hijo ->
                        RenglonHijoInput(
                            hijo = hijo,
                            onEliminar = { onEliminarHijo(hijo) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulMarino)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = { onGuardar(mama, papa, whatsapp, email, ciudad) },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Ambar)
                ) {
                    Text("Guardar", color = Blanco)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RenglonHijoInput(
    hijo: HijoState,
    onEliminar: () -> Unit
) {
    var nombre by remember { mutableStateOf(hijo.nombre) }
    var fecha by remember { mutableStateOf(hijo.fechaNacimiento) }
    var mostrarDatePicker by remember { mutableStateOf(false) }

    // Diálogo con DatePicker M3 (soporta clic directo al año para scroll rápido)
    if (mostrarDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                            calendar.timeInMillis = millis
                            val dia = calendar.get(Calendar.DAY_OF_MONTH)
                            val mes = calendar.get(Calendar.MONTH) + 1
                            val anio = calendar.get(Calendar.YEAR)
                            fecha = "$dia/$mes/$anio"
                            hijo.fechaNacimiento = fecha
                        }
                        mostrarDatePicker = false
                    }
                ) {
                    Text("Aceptar", color = AzulMarino, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) {
                    Text("Cancelar", color = GrisTexto)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                hijo.nombre = it
            },
            modifier = Modifier.weight(1.2f),
            placeholder = { Text("Nombre", color = GrisTexto) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFEEEEEE),
                focusedBorderColor = Ambar,
                unfocusedContainerColor = Blanco,
                focusedContainerColor = Blanco
            ),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { mostrarDatePicker = true }
        ) {
            OutlinedTextField(
                value = fecha,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("dd/mm/aaaa", color = GrisTexto) },
                trailingIcon = {
                    IconButton(onClick = { mostrarDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = "Calendario",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFEEEEEE),
                    focusedBorderColor = Ambar,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        IconButton(onClick = onEliminar, modifier = Modifier.size(36.dp)) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Eliminar",
                tint = Color(0xFFD9534F)
            )
        }
    }
}

@Composable
private fun CampoInput(
    valor: String,
    onValueChange: (String) -> Unit,
    icono: ImageVector,
    etiqueta: String,
    placeholder: String
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(etiqueta, color = AzulMarino) },
        placeholder = { Text(placeholder, color = GrisTexto) },
        leadingIcon = { Icon(icono, contentDescription = null, tint = GrisTexto) },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFEEEEEE),
            focusedBorderColor = Ambar,
            unfocusedContainerColor = Blanco,
            focusedContainerColor = Blanco
        ),
        singleLine = true
    )
}