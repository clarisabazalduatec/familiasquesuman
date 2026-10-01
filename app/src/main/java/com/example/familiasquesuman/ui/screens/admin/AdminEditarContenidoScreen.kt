package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.data.repository.donacionesMockData
import com.example.familiasquesuman.domain.*
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.screens.proyectos.proyectosMockData
import com.example.familiasquesuman.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditarContenidoScreen(
    navController: NavHostController,
    tipo: String,
    id: String,
) {
    var mostrarExitoDialog by remember { mutableStateOf(value = false) }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    // Campos del formulario
    var tituloText by remember { mutableStateOf("") }
    var descripcionCortaText by remember { mutableStateOf("") }
    var ubicacionText by remember { mutableStateOf("") }

    // Actividad
    var categoriaActividad by remember { mutableStateOf("Medio Ambiente") }
    var fechaText by remember { mutableStateOf("") }
    var horarioText by remember { mutableStateOf("") }
    var organizadorText by remember { mutableStateOf("") }
    var acercaDeText by remember { mutableStateOf("") }
    var lugaresTotalesText by remember { mutableStateOf("30") }

    // Proyecto
    var estadoProyectoIndex by remember { mutableIntStateOf(0) }
    var participantesText by remember { mutableStateOf("") }
    var descripcionLargaText by remember { mutableStateOf("") }
    var whatsappText by remember { mutableStateOf("") }
    var telefonoText by remember { mutableStateOf("") }

    // Donación
    var tipoDonacionIndex by remember { mutableIntStateOf(0) }
    var categoriaDonacionText by remember { mutableStateOf("") }
    var recaudadoText by remember { mutableStateOf("") }
    var metaText by remember { mutableStateOf("") }
    var condicionesText by remember { mutableStateOf("") }

    // Cargar datos existentes
    LaunchedEffect(tipo, id) {
        when (tipo.lowercase()) {
            "actividad" -> {
                val act = actividadesMockData.find { it.id == id.toIntOrNull() }
                if (act != null) {
                    tituloText = act.titulo
                    descripcionCortaText = act.descripcion
                    ubicacionText = act.ubicacion
                    categoriaActividad = act.categoria
                    fechaText = act.fecha
                    horarioText = act.horario
                    organizadorText = act.organizador
                    acercaDeText = act.acercaDe
                    lugaresTotalesText = act.lugaresTotales.toString()
                }
            }

            "proyecto" -> {
                val proy = proyectosMockData.find { it.id == id }
                if (proy != null) {
                    tituloText = proy.nombre
                    descripcionCortaText = proy.descripcionCorta
                    ubicacionText = proy.ubicacion
                    estadoProyectoIndex = if (proy.estado == EstadoProyecto.ACTIVO) 0 else 1
                    participantesText = proy.participantes
                    acercaDeText = proy.acercaDe
                    descripcionLargaText = proy.descripcionLarga
                    whatsappText = proy.telefonoWhatsapp ?: ""
                    telefonoText = proy.telefonoLlamada ?: ""
                }
            }

            "donacion" -> {
                val don = donacionesMockData.find { it.id == id }
                if (don != null) {
                    tituloText = don.titulo
                    descripcionCortaText = don.descripcionCorta
                    descripcionLargaText = don.descripcionLarga
                    categoriaDonacionText = don.fundacion
                    tipoDonacionIndex = if (don.tipo == TipoDonacion.CAMPANA) 0 else 1
                    recaudadoText = don.recaudado.toInt().toString()
                    metaText = don.meta.toInt().toString()
                    whatsappText = don.telefonoWhatsapp ?: ""
                    condicionesText = don.condiciones ?: ""
                    ubicacionText = don.ubicacion ?: ""
                }
            }
        }
    }

    fun guardarCambios() {
        if (tituloText.isBlank()) {
            errorMensaje = "El título no puede estar vacío."
            return
        }

        when (tipo.lowercase()) {
            "actividad" -> {
                val index = actividadesMockData.indexOfFirst { it.id == id.toIntOrNull() }
                if (index != -1) {
                    val vieja = actividadesMockData[index]
                    actividadesMockData[index] = vieja.copy(
                        titulo = tituloText.trim(),
                        descripcion = descripcionCortaText,
                        categoria = categoriaActividad,
                        colorCategoria = if (categoriaActividad == "Medio Ambiente") FondoVerde else if (categoriaActividad == "Educación") FondoAzulClaro else FondoNaranja,
                        textColorCategoria = if (categoriaActividad == "Medio Ambiente") TextoVerde else if (categoriaActividad == "Educación") TextoAzul else TextoNaranja,
                        fecha = fechaText,
                        horario = horarioText,
                        ubicacion = ubicacionText,
                        organizador = organizadorText,
                        acercaDe = acercaDeText,
                        lugaresTotales = lugaresTotalesText.toIntOrNull() ?: vieja.lugaresTotales,
                    )
                }
            }

            "proyecto" -> {
                val index = proyectosMockData.indexOfFirst { it.id == id }
                if (index != -1) {
                    val viejo = proyectosMockData[index]
                    proyectosMockData[index] = viejo.copy(
                        nombre = tituloText.trim(),
                        estado = if (estadoProyectoIndex == 0) EstadoProyecto.ACTIVO else EstadoProyecto.ANTERIOR,
                        ubicacion = ubicacionText,
                        participantes = participantesText,
                        descripcionCorta = descripcionCortaText,
                        acercaDe = acercaDeText,
                        descripcionLarga = descripcionLargaText,
                        telefonoWhatsapp = whatsappText.ifBlank { null },
                        telefonoLlamada = telefonoText.ifBlank { null },
                    )
                }
            }

            "donacion" -> {
                val index = donacionesMockData.indexOfFirst { it.id == id }
                if (index != -1) {
                    val vieja = donacionesMockData[index]
                    val nuevoRecaudado = recaudadoText.toFloatOrNull() ?: vieja.recaudado
                    val nuevoTextoProgreso = if (tipoDonacionIndex == 0) {
                        "$${nuevoRecaudado.toInt()} recaudados"
                    } else {
                        "${nuevoRecaudado.toInt()} entregadas"
                    }
                    donacionesMockData[index] = vieja.copy(
                        titulo = tituloText.trim(),
                        descripcionCorta = descripcionCortaText,
                        descripcionLarga = descripcionLargaText,
                        fundacion = categoriaDonacionText,
                        tipo = if (tipoDonacionIndex == 0) TipoDonacion.CAMPANA else TipoDonacion.ESPECIE,
                        categoria = categoriaDonacionText,
                        recaudado = nuevoRecaudado,
                        meta = metaText.toFloatOrNull() ?: vieja.meta,
                        textoProgreso = nuevoTextoProgreso,
                        telefonoWhatsapp = whatsappText.ifBlank { null },
                        condiciones = condicionesText.ifBlank { null },
                        ubicacion = ubicacionText.ifBlank { null },
                    )
                }
            }
        }

        mostrarExitoDialog = true
    }

    val tituloTipo = when (tipo.lowercase()) {
        "actividad" -> "Actividad"
        "proyecto" -> "Proyecto"
        else -> "Donación"
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.popBackStack() },
        titulo = "Editar $tituloTipo",
    ) { paddingVal ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVal)
                .background(CremaFondo)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Text(
                text = "Editar $tituloTipo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AzulMarino,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Modifica la información de la tarjeta que verán los usuarios.",
                fontSize = 13.sp,
                color = GrisTexto,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    OutlinedTextField(
                        value = tituloText,
                        onValueChange = { tituloText = it },
                        label = { Text("Título / Nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    )

                    OutlinedTextField(
                        value = descripcionCortaText,
                        onValueChange = { descripcionCortaText = it },
                        label = { Text("Descripción Corta") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    )

                    OutlinedTextField(
                        value = ubicacionText,
                        onValueChange = { ubicacionText = it },
                        label = { Text("Ubicación") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    )

                    when (tipo.lowercase()) {
                        "actividad" -> {
                            Text(
                                text = "Categoría de Actividad",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AzulMarino,
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                listOf("Medio Ambiente", "Educación", "Apoyo Social", "Otros").forEach { cat ->
                                    FilterChip(
                                        selected = categoriaActividad == cat,
                                        onClick = { categoriaActividad = cat },
                                        label = { Text(cat, fontSize = 11.sp) },
                                        shape = RoundedCornerShape(12.dp),
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                OutlinedTextField(
                                    value = fechaText,
                                    onValueChange = { fechaText = it },
                                    label = { Text("Fecha") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                OutlinedTextField(
                                    value = horarioText,
                                    onValueChange = { horarioText = it },
                                    label = { Text("Horario") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                OutlinedTextField(
                                    value = organizadorText,
                                    onValueChange = { organizadorText = it },
                                    label = { Text("Organizador") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                OutlinedTextField(
                                    value = lugaresTotalesText,
                                    onValueChange = { lugaresTotalesText = it },
                                    label = { Text("Lugares") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(0.8f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                            }

                            OutlinedTextField(
                                value = acercaDeText,
                                onValueChange = { acercaDeText = it },
                                label = { Text("Acerca de la actividad (Detallado)") },
                                minLines = 3,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                            )
                        }

                        "proyecto" -> {
                            Text(
                                text = "Estado del Proyecto",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AzulMarino,
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(
                                    selected = estadoProyectoIndex == 0,
                                    onClick = { estadoProyectoIndex = 0 },
                                    label = { Text("Proyecto Activo") },
                                )
                                FilterChip(
                                    selected = estadoProyectoIndex == 1,
                                    onClick = { estadoProyectoIndex = 1 },
                                    label = { Text("Proyecto Anterior") },
                                )
                            }

                            OutlinedTextField(
                                value = participantesText,
                                onValueChange = { participantesText = it },
                                label = { Text("Participantes / Beneficiarios") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                            )

                            OutlinedTextField(
                                value = acercaDeText,
                                onValueChange = { acercaDeText = it },
                                label = { Text("Acerca del Proyecto") },
                                maxLines = 2,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                            )

                            OutlinedTextField(
                                value = descripcionLargaText,
                                onValueChange = { descripcionLargaText = it },
                                label = { Text("Descripción Detallada") },
                                minLines = 3,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                OutlinedTextField(
                                    value = whatsappText,
                                    onValueChange = { whatsappText = it },
                                    label = { Text("WhatsApp de contacto") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                OutlinedTextField(
                                    value = telefonoText,
                                    onValueChange = { telefonoText = it },
                                    label = { Text("Teléfono de llamada") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                            }
                        }

                        "donacion" -> {
                            Text(
                                text = "Tipo de Donación",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AzulMarino,
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(
                                    selected = tipoDonacionIndex == 0,
                                    onClick = { tipoDonacionIndex = 0 },
                                    label = { Text("Campaña Monetaria") },
                                )
                                FilterChip(
                                    selected = tipoDonacionIndex == 1,
                                    onClick = { tipoDonacionIndex = 1 },
                                    label = { Text("Artículo / En Especie") },
                                )
                            }

                            OutlinedTextField(
                                value = categoriaDonacionText,
                                onValueChange = { categoriaDonacionText = it },
                                label = { Text("Categoría / Fundación") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                OutlinedTextField(
                                    value = recaudadoText,
                                    onValueChange = { recaudadoText = it },
                                    label = { Text(if (tipoDonacionIndex == 0) "Recaudado ($)" else "Entregados (Cant.)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                OutlinedTextField(
                                    value = metaText,
                                    onValueChange = { metaText = it },
                                    label = { Text(if (tipoDonacionIndex == 0) "Meta ($)" else "Meta (Cant.)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                            }

                            OutlinedTextField(
                                value = descripcionLargaText,
                                onValueChange = { descripcionLargaText = it },
                                label = { Text("Descripción Detallada") },
                                minLines = 3,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                            )

                            if (tipoDonacionIndex == 0) {
                                OutlinedTextField(
                                    value = whatsappText,
                                    onValueChange = { whatsappText = it },
                                    label = { Text("WhatsApp para Donar") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                )
                            } else {
                                OutlinedTextField(
                                    value = condicionesText,
                                    onValueChange = { condicionesText = it },
                                    label = { Text("Condiciones de Entrega") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                )
                            }
                        }
                    }

                    if (errorMensaje != null) {
                        Text(
                            text = errorMensaje!!,
                            color = ColorError,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { guardarCambios() },
                        colors = ButtonDefaults.buttonColors(containerColor = Ambar, contentColor = AzulMarino),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            tint = AzulMarino,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Guardar Cambios",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                        )
                    }
                }
            }
        }
    }

    if (mostrarExitoDialog) {
        AlertDialog(
            onDismissRequest = {
                mostrarExitoDialog = false
                navController.popBackStack()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = VerdeExito,
                    modifier = Modifier.size(48.dp),
                )
            },
            title = {
                Text(
                    text = "¡Cambios Guardados!",
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                )
            },
            text = {
                Text(
                    text = "La tarjeta ha sido actualizada correctamente en la aplicación.",
                    color = GrisTexto,
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarExitoDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold)
                }
            },
        )
    }
}
