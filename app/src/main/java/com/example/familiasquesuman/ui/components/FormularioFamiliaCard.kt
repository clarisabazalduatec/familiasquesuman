package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.background
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
import com.example.familiasquesuman.ui.model.AdultoState
import com.example.familiasquesuman.ui.model.MenorState
import com.example.familiasquesuman.ui.screens.perfil.PerfilUiState
import com.example.familiasquesuman.ui.theme.*
import java.util.Calendar
import java.util.TimeZone

// Tarjeta del formulario para editar los datos de la familia.
@Composable
fun FormularioFamiliaCard(
    uiState: PerfilUiState,
    listaAdultos: List<AdultoState>,
    listaMenores: List<MenorState>,
    onAgregarAdulto: () -> Unit,
    onEliminarAdulto: (AdultoState) -> Unit,
    onActualizarAdulto: (AdultoState) -> Unit,
    onAgregarMenor: () -> Unit,
    onEliminarMenor: (MenorState) -> Unit,
    onActualizarMenor: (MenorState) -> Unit,
    onCancelar: () -> Unit,
    onGuardar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // 1. Encabezado superior con botón de regreso
            EncabezadoFormulario(onVolver = onCancelar)

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Sección Adultos
            EncabezadoSeccion(
                titulo = "Adultos",
                textoBoton = "+ Agregar",
                onAgregar = onAgregarAdulto
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (listaAdultos.isEmpty()) {
                EstadoVacioSeccion(mensaje = "Sin adultos registrados", icono = Icons.Outlined.Person)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    listaAdultos.forEach { adulto ->
                        TarjetaInputAdulto(
                            adulto = adulto,
                            onActualizar = onActualizarAdulto,
                            onEliminar = { onEliminarAdulto(adulto) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Sección Menores
            EncabezadoSeccion(
                titulo = "Menores",
                textoBoton = "+ Agregar",
                onAgregar = onAgregarMenor
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (listaMenores.isEmpty()) {
                EstadoVacioSeccion(mensaje = "Sin menores registrados", icono = Icons.Outlined.Face)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    listaMenores.forEach { menor ->
                        FilaInputMenor(
                            menor = menor,
                            onActualizar = onActualizarMenor,
                            onEliminar = { onEliminarMenor(menor) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 4. Botones de acción
            BotonesAccionFormulario(
                onCancelar = onCancelar,
                onGuardar = onGuardar
            )
        }
    }
}

// =========================================================================
// COMPONENTES MODULARES Y REUTILIZABLES
// =========================================================================

@Composable
private fun EncabezadoFormulario(onVolver: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onVolver, modifier = Modifier.size(24.dp)) {
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
}

@Composable
private fun EncabezadoSeccion(
    titulo: String,
    textoBoton: String,
    onAgregar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            color = AzulMarino,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onAgregar) {
            Text(textoBoton, color = AzulMarino, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EstadoVacioSeccion(mensaje: String, icono: ImageVector) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icono, contentDescription = null, tint = GrisTexto)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = mensaje, color = GrisTexto, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun TarjetaInputAdulto(
    adulto: AdultoState,
    onActualizar: (AdultoState) -> Unit,
    onEliminar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Datos de adulto", style = MaterialTheme.typography.labelMedium, color = GrisTexto)
            IconButton(onClick = onEliminar, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Eliminar adulto",
                    tint = Color(0xFFD9534F),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        CampoTextoBase(
            valor = adulto.nombre,
            onValueChange = { onActualizar(adulto.copy(nombre = it)) },
            placeholder = "Nombre completo",
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DesplegableRol(
                rolSeleccionado = adulto.rol,
                onRolSeleccionado = { nuevoRol -> onActualizar(adulto.copy(rol = nuevoRol)) },
                modifier = Modifier.weight(1f)
            )

            CampoTextoBase(
                valor = adulto.email,
                onValueChange = { onActualizar(adulto.copy(email = it)) },
                placeholder = "Email",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DesplegableRol(
    rolSeleccionado: String,
    onRolSeleccionado: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var desplegado by remember { mutableStateOf(false) }
    val opcionesRol = listOf("Mamá", "Papá", "Tutor/a", "Abuelo/a", "Otro")

    ExposedDropdownMenuBox(
        expanded = desplegado,
        onExpandedChange = { desplegado = !desplegado },
        modifier = modifier
    ) {
        CampoTextoBase(
            valor = rolSeleccionado,
            onValueChange = {},
            readOnly = true,
            placeholder = "Rol",
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = desplegado) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = desplegado,
            onDismissRequest = { desplegado = false },
            modifier = Modifier.background(Blanco)
        ) {
            opcionesRol.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion, color = AzulMarino) },
                    onClick = {
                        onRolSeleccionado(opcion)
                        desplegado = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FilaInputMenor(
    menor: MenorState,
    onActualizar: (MenorState) -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CampoTextoBase(
            valor = menor.nombre,
            onValueChange = { onActualizar(menor.copy(nombre = it)) },
            placeholder = "Nombre",
            modifier = Modifier.weight(1.2f)
        )

        CampoFechaNacimiento(
            fecha = menor.fechaNacimiento,
            onFechaSeleccionada = { nuevaFecha -> onActualizar(menor.copy(fechaNacimiento = nuevaFecha)) },
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onEliminar, modifier = Modifier.size(36.dp)) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Eliminar menor",
                tint = Color(0xFFD9534F)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CampoFechaNacimiento(
    fecha: String,
    onFechaSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarDatePicker by remember { mutableStateOf(false) }

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
                            onFechaSeleccionada("$dia/$mes/$anio")
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

    Box(modifier = modifier.clickable { mostrarDatePicker = true }) {
        CampoTextoBase(
            valor = fecha,
            onValueChange = {},
            readOnly = true,
            placeholder = "dd/mm/aaaa",
            trailingIcon = {
                IconButton(onClick = { mostrarDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = "Seleccionar fecha",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BotonesAccionFormulario(
    onCancelar: () -> Unit,
    onGuardar: () -> Unit
) {
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
            onClick = onGuardar,
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Ambar)
        ) {
            Text("Guardar", color = Blanco)
        }
    }
}

@Composable
private fun CampoTextoBase(
    valor: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        readOnly = readOnly,
        placeholder = { Text(placeholder, color = GrisTexto) },
        trailingIcon = trailingIcon,
        modifier = modifier,
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