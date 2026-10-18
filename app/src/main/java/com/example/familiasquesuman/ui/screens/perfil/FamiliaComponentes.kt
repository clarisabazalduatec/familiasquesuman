package com.example.familiasquesuman.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.familiasquesuman.data.remote.dto.FamiliaDto
import com.example.familiasquesuman.data.remote.dto.InvitacionRecibidaDto
import com.example.familiasquesuman.data.remote.dto.MenorDto
import com.example.familiasquesuman.data.remote.dto.Parentescos
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.theme.GrisBorde
import com.example.familiasquesuman.ui.theme.GrisTexto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import com.example.familiasquesuman.ui.components.FotoPerfil
// =====================================================================
// TARJETAS
// =====================================================================

@Composable
private fun TarjetaPerfil(contenido: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) { contenido() }
    }
}

/** Sin sesión: invita a iniciar sesión. */
@Composable
fun PerfilInvitadoCard(onIniciarSesion: () -> Unit) {
    TarjetaPerfil {
        IconoCircular(Icons.Outlined.Lock)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Inicia sesión para ver tu perfil",
            style = MaterialTheme.typography.titleMedium,
            color = AzulMarino,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Así podrás registrar a tu familia e inscribirse juntos en actividades.",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        BotonAmbar(texto = "Iniciar sesión", onClick = onIniciarSesion)
    }
}

/** Con sesión pero sin familia. */
@Composable
fun SinFamiliaCard(onCrearFamilia: () -> Unit) {
    TarjetaPerfil {
        Text(
            text = "Datos de la familia",
            style = MaterialTheme.typography.titleLarge,
            color = AzulMarino,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(20.dp))
        IconoCircular(Icons.Outlined.FamilyRestroom)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Aún no perteneces a una familia.\nCrea la tuya o acepta una invitación.",
            style = MaterialTheme.typography.bodyMedium,
            color = GrisTexto,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        BotonAmbar(texto = "Crear mi familia", onClick = onCrearFamilia)
    }
}

/** Invitaciones que me hicieron (se usa en Perfil y en Notificaciones). */
@Composable
fun InvitacionesRecibidasCard(
    invitaciones: List<InvitacionRecibidaDto>,
    guardando: Boolean,
    onAceptar: (String) -> Unit,
    onRechazar: (String) -> Unit
) {
    TarjetaPerfil {
        SeccionTitulo("Invitaciones a familia")
        Spacer(Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            invitaciones.forEach { inv ->
                val familia = inv.familia.nombre ?: "una familia"
                val quien = inv.invitadoPor?.nombre ?: "Alguien"
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AmbarClaro.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "$quien te invitó a unirte a $familia",
                        color = AzulMarino,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onAceptar(inv.id) },
                            enabled = !guardando,
                            colors = ButtonDefaults.buttonColors(containerColor = Ambar),
                            shape = RoundedCornerShape(20.dp)
                        ) { Text("Aceptar", color = Blanco) }
                        OutlinedButton(
                            onClick = { onRechazar(inv.id) },
                            enabled = !guardando,
                            shape = RoundedCornerShape(20.dp)
                        ) { Text("Rechazar", color = AzulMarino) }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Solo puedes pertenecer a una familia.",
            style = MaterialTheme.typography.bodySmall,
            color = GrisTexto
        )
    }
}

/** Mi familia: adultos, invitaciones pendientes y menores. */
@Composable
fun FamiliaCard(
    familia: FamiliaDto,
    miId: String?,
    guardando: Boolean,
    onEditarMiParentesco: () -> Unit,
    onInvitar: () -> Unit,
    onCancelarInvitacion: (String) -> Unit,
    onAgregarMenor: () -> Unit,
    onEditarMenor: (MenorDto) -> Unit,
    onBorrarMenor: (MenorDto) -> Unit,
    onSalir: () -> Unit
) {
    TarjetaPerfil {
        Text(
            text = familia.nombre ?: "Mi familia",
            style = MaterialTheme.typography.titleLarge,
            color = AzulMarino,
            fontWeight = FontWeight.Bold
        )

        // ---------- ADULTOS ----------
        Spacer(Modifier.height(16.dp))
        SeccionTitulo("Adultos")
        Spacer(Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            familia.adultos.forEach { adulto ->
                val soyYo = adulto.id == miId
                FilaMiembro(
                    icono = Icons.Outlined.Person,
                    foto = { FotoPerfil(fotoUrl = adulto.fotoUrl, nombre = adulto.nombre, tamano = 36.dp) },
                    titulo = if (soyYo) "${adulto.nombre} (tú)" else adulto.nombre,
                    subtitulo = Parentescos.etiqueta(adulto.parentesco)
                        ?: if (soyYo) "Toca el lápiz para indicar tu parentesco" else "Adulto"
                ) {
                    if (soyYo) {
                        IconButton(onClick = onEditarMiParentesco, enabled = !guardando) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Editar mi parentesco", tint = AzulMarino)
                        }
                    }
                }
            }
            familia.invitacionesPendientes.forEach { inv ->
                FilaMiembro(
                    icono = Icons.Outlined.MailOutline,
                    titulo = inv.invitado.nombre,
                    subtitulo = "Invitación pendiente · ${inv.invitado.email}",
                    atenuado = true
                ) {
                    IconButton(onClick = { onCancelarInvitacion(inv.id) }, enabled = !guardando) {
                        Icon(Icons.Outlined.Close, contentDescription = "Cancelar invitación", tint = GrisTexto)
                    }
                }
            }
        }
        BotonTexto(texto = "Invitar adulto", icono = Icons.Outlined.PersonAdd, enabled = !guardando, onClick = onInvitar)

        // ---------- MENORES ----------
        Spacer(Modifier.height(8.dp))
        SeccionTitulo("Menores")
        Spacer(Modifier.height(8.dp))
        if (familia.menores.isEmpty()) {
            Text(
                text = "Aún no hay menores registrados",
                color = GrisTexto,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                familia.menores.forEach { menor ->
                    FilaMiembro(
                        icono = Icons.Outlined.ChildCare,
                        titulo = menor.nombre,
                        subtitulo = "${menor.edad} ${if (menor.edad == 1) "año" else "años"} · ${isoAMostrar(menor.fechaNacimiento)}"
                    ) {
                        IconButton(onClick = { onEditarMenor(menor) }, enabled = !guardando) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Editar a ${menor.nombre}", tint = AzulMarino)
                        }
                        IconButton(onClick = { onBorrarMenor(menor) }, enabled = !guardando) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Eliminar a ${menor.nombre}", tint = GrisTexto)
                        }
                    }
                }
            }
        }
        BotonTexto(texto = "Agregar menor", icono = Icons.Outlined.ChildCare, enabled = !guardando, onClick = onAgregarMenor)

        // ---------- SALIR ----------
        HorizontalDivider(color = GrisBorde, modifier = Modifier.padding(vertical = 8.dp))
        TextButton(onClick = onSalir, enabled = !guardando) {
            Icon(
                Icons.AutoMirrored.Outlined.Logout,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text("Salir de la familia", color = MaterialTheme.colorScheme.error)
        }
    }
}

// =====================================================================
// DIÁLOGOS
// =====================================================================

@Composable
fun DialogoCrearFamilia(
    guardando: Boolean,
    error: String?,
    onConfirmar: (nombre: String, parentesco: String?) -> Unit,
    onCerrar: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var parentesco by remember { mutableStateOf<String?>(null) }

    DialogoBase(
        titulo = "Crear mi familia",
        textoConfirmar = "Crear",
        guardando = guardando,
        error = error,
        onConfirmar = { onConfirmar(nombre, parentesco) },
        onCerrar = onCerrar
    ) {
        CampoTexto(
            valor = nombre,
            onValueChange = { nombre = it },
            etiqueta = "Nombre de la familia (opcional)",
            capitalizar = true
        )
        Spacer(Modifier.height(12.dp))
        Text("¿Qué eres en la familia?", color = AzulMarino, fontWeight = FontWeight.Bold)
        SelectorParentesco(seleccionado = parentesco, onSeleccionar = { parentesco = it })
    }
}

@Composable
fun DialogoParentesco(
    actual: String?,
    guardando: Boolean,
    error: String?,
    onConfirmar: (String) -> Unit,
    onCerrar: () -> Unit
) {
    var parentesco by remember { mutableStateOf(actual) }
    DialogoBase(
        titulo = "Mi parentesco",
        textoConfirmar = "Guardar",
        guardando = guardando,
        error = error,
        confirmarHabilitado = parentesco != null,
        onConfirmar = { parentesco?.let(onConfirmar) },
        onCerrar = onCerrar
    ) {
        SelectorParentesco(seleccionado = parentesco, onSeleccionar = { parentesco = it })
    }
}

@Composable
fun DialogoInvitarAdulto(
    guardando: Boolean,
    error: String?,
    onConfirmar: (String) -> Unit,
    onCerrar: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    DialogoBase(
        titulo = "Invitar adulto",
        textoConfirmar = "Invitar",
        guardando = guardando,
        error = error,
        onConfirmar = { onConfirmar(email) },
        onCerrar = onCerrar
    ) {
        Text(
            text = "La persona debe tener una cuenta en la app y no pertenecer a otra familia. " +
                    "Verá tu invitación en sus notificaciones y en su perfil.",
            style = MaterialTheme.typography.bodySmall,
            color = GrisTexto
        )
        Spacer(Modifier.height(12.dp))
        CampoTexto(
            valor = email,
            onValueChange = { email = it },
            etiqueta = "Correo electrónico",
            teclado = KeyboardType.Email
        )
    }
}

/** Agregar (menor = null) o editar un menor. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoMenor(
    menor: MenorDto?,
    guardando: Boolean,
    error: String?,
    onConfirmar: (nombre: String, fechaIso: String) -> Unit,
    onCerrar: () -> Unit
) {
    var nombre by remember { mutableStateOf(menor?.nombre ?: "") }
    var fechaIso by remember { mutableStateOf(menor?.fechaNacimiento) }
    var mostrarCalendario by remember { mutableStateOf(false) }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    DialogoBase(
        titulo = if (menor == null) "Agregar menor" else "Editar menor",
        textoConfirmar = "Guardar",
        guardando = guardando,
        error = errorLocal ?: error,
        onConfirmar = {
            val fecha = fechaIso
            if (fecha == null) {
                errorLocal = "Elige la fecha de nacimiento"
            } else {
                errorLocal = null
                onConfirmar(nombre, fecha)
            }
        },
        onCerrar = onCerrar
    ) {
        CampoTexto(
            valor = nombre,
            onValueChange = { nombre = it },
            etiqueta = "Nombre",
            capitalizar = true
        )
        Spacer(Modifier.height(12.dp))

        // Campo de solo lectura que abre el calendario al tocarlo.
        Box {
            CampoTexto(
                valor = fechaIso?.let { isoAMostrar(it) } ?: "",
                onValueChange = {},
                etiqueta = "Fecha de nacimiento",
                soloLectura = true,
                icono = Icons.Outlined.CalendarMonth
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { mostrarCalendario = true }
            )
        }
    }

    if (mostrarCalendario) {
        val hoy = System.currentTimeMillis()
        val anioActual = Calendar.getInstance().get(Calendar.YEAR)
        val estado = rememberDatePickerState(
            initialSelectedDateMillis = fechaIso?.let { isoAMillis(it) },
            yearRange = (anioActual - 30)..anioActual,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis <= hoy
            }
        )
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    estado.selectedDateMillis?.let {
                        fechaIso = millisAIso(it)
                        errorLocal = null
                    }
                    mostrarCalendario = false
                }) { Text("Aceptar", color = Ambar) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar", color = GrisTexto) }
            }
        ) {
            DatePicker(state = estado)
        }
    }
}

/** Confirmación simple (borrar menor, salir de la familia). */
@Composable
fun DialogoConfirmar(
    titulo: String,
    mensaje: String,
    textoConfirmar: String,
    guardando: Boolean,
    error: String?,
    onConfirmar: () -> Unit,
    onCerrar: () -> Unit
) {
    DialogoBase(
        titulo = titulo,
        textoConfirmar = textoConfirmar,
        guardando = guardando,
        error = error,
        peligro = true,
        onConfirmar = onConfirmar,
        onCerrar = onCerrar
    ) {
        Text(text = mensaje, color = AzulMarino, style = MaterialTheme.typography.bodyMedium)
    }
}

// =====================================================================
// PIEZAS PEQUEÑAS
// =====================================================================

@Composable
private fun DialogoBase(
    titulo: String,
    textoConfirmar: String,
    guardando: Boolean,
    error: String?,
    onConfirmar: () -> Unit,
    onCerrar: () -> Unit,
    peligro: Boolean = false,
    confirmarHabilitado: Boolean = true,
    contenido: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!guardando) onCerrar() },
        containerColor = Blanco,
        title = { Text(titulo, color = AzulMarino, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                contenido()
                if (error != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                enabled = !guardando && confirmarHabilitado,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (peligro) MaterialTheme.colorScheme.error else Ambar
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                if (guardando) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Blanco, strokeWidth = 2.dp)
                } else {
                    Text(textoConfirmar, color = Blanco)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onCerrar, enabled = !guardando) { Text("Cancelar", color = GrisTexto) }
        }
    )
}

@Composable
private fun SelectorParentesco(seleccionado: String?, onSeleccionar: (String) -> Unit) {
    Column {
        Parentescos.opciones.forEach { (codigo, etiqueta) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = seleccionado == codigo,
                        onClick = { onSeleccionar(codigo) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 2.dp)
            ) {
                RadioButton(
                    selected = seleccionado == codigo,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(selectedColor = Ambar)
                )
                Spacer(Modifier.width(8.dp))
                Text(etiqueta, color = AzulMarino)
            }
        }
    }
}

@Composable
private fun CampoTexto(
    valor: String,
    onValueChange: (String) -> Unit,
    etiqueta: String,
    teclado: KeyboardType = KeyboardType.Text,
    capitalizar: Boolean = false,
    soloLectura: Boolean = false,
    icono: ImageVector? = null
) {
    val iconoFinal: (@Composable () -> Unit)? =
        icono?.let { { Icon(it, contentDescription = null, tint = GrisTexto) } }

    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        singleLine = true,
        readOnly = soloLectura,
        trailingIcon = iconoFinal,
        keyboardOptions = KeyboardOptions(
            keyboardType = teclado,
            capitalization = if (capitalizar) KeyboardCapitalization.Words else KeyboardCapitalization.None
        ),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Ambar,
            unfocusedBorderColor = GrisBorde,
            focusedLabelColor = Ambar,
            cursorColor = AzulMarino,
            focusedTextColor = AzulMarino,
            unfocusedTextColor = AzulMarino
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun FilaMiembro(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    atenuado: Boolean = false,
    foto: (@Composable () -> Unit)? = null, // si se pasa, va en lugar del ícono (adultos con foto de perfil)
    acciones: @Composable RowScope.() -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                AmbarClaro.copy(alpha = if (atenuado) 0.12f else 0.3f),
                RoundedCornerShape(12.dp)
            )
            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp)
    ) {
        if (foto != null) {
            foto()
        } else {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(if (atenuado) GrisTexto else Ambar, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, contentDescription = null, tint = Blanco, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, fontWeight = FontWeight.Bold, color = AzulMarino)
            Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = GrisTexto)
        }
        acciones()
    }
}

@Composable
private fun SeccionTitulo(titulo: String) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.titleMedium,
        color = AzulMarino,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun IconoCircular(icono: ImageVector) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(AmbarClaro, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icono, contentDescription = null, modifier = Modifier.size(40.dp), tint = AzulMarino)
        }
    }
}

@Composable
private fun BotonAmbar(texto: String, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Ambar),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(text = texto, color = Blanco, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun BotonTexto(texto: String, icono: ImageVector, enabled: Boolean, onClick: () -> Unit) {
    TextButton(onClick = onClick, enabled = enabled) {
        Icon(icono, contentDescription = null, tint = Ambar, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(texto, color = Ambar, fontWeight = FontWeight.Bold)
    }
}

// =====================================================================
// FECHAS ("AAAA-MM-DD" del backend <-> milisegundos del calendario)
// Se usa SimpleDateFormat en UTC porque java.time requiere API 26 y la app soporta API 24.
// =====================================================================

private fun formato(patron: String) =
    SimpleDateFormat(patron, Locale.US).apply { timeZone = TimeZone.getTimeZone("UTC") }

private fun millisAIso(millis: Long): String = formato("yyyy-MM-dd").format(Date(millis))

private fun isoAMillis(iso: String): Long? = try {
    formato("yyyy-MM-dd").parse(iso)?.time
} catch (_: Exception) {
    null
}

fun isoAMostrar(iso: String): String = try {
    formato("dd/MM/yyyy").format(formato("yyyy-MM-dd").parse(iso)!!)
} catch (_: Exception) {
    iso
}
