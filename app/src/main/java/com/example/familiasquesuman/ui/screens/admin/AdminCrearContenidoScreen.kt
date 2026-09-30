package com.example.familiasquesuman.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.data.ActividadMock
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.data.agregarActividadMock
import com.example.familiasquesuman.data.repository.agregarDonacionMock
import com.example.familiasquesuman.domain.*
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.screens.proyectos.agregarProyectoMock
import com.example.familiasquesuman.ui.theme.*

enum class TipoContenidoAdmin(val titulo: String) {
    ACTIVIDAD("Actividad"),
    PROYECTO("Proyecto"),
    DONACION("Donación"),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCrearContenidoScreen(navController: NavHostController) {
    var tipoSeleccionado by remember { mutableStateOf(TipoContenidoAdmin.ACTIVIDAD) }
    var mostrarExitoDialog by remember { mutableStateOf(value = false) }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    // Campos comunes
    var tituloText by remember { mutableStateOf("") }
    var descripcionCortaText by remember { mutableStateOf("") }
    var ubicacionText by remember { mutableStateOf("") }

    // Campos de Actividad
    var categoriaActividad by remember { mutableStateOf("Medio Ambiente") }
    var fechaText by remember { mutableStateOf("") }
    var horarioText by remember { mutableStateOf("") }
    var organizadorText by remember { mutableStateOf("Familias que Suman") }
    var acercaDeText by remember { mutableStateOf("") }
    var lugaresTotalesText by remember { mutableStateOf("30") }

    // Campos de Proyecto
    var estadoProyectoIndex by remember { mutableIntStateOf(0) } // 0 = Activo, 1 = Anterior
    var participantesText by remember { mutableStateOf("") }
    var descripcionLargaText by remember { mutableStateOf("") }
    var whatsappText by remember { mutableStateOf("") }
    var telefonoText by remember { mutableStateOf("") }

    // Campos de Donación
    var tipoDonacionIndex by remember { mutableIntStateOf(0) } // 0 = Campaña, 1 = Artículo
    var categoriaDonacionText by remember { mutableStateOf("Alimentación") }
    var metaText by remember { mutableStateOf("5000") }
    var condicionesText by remember { mutableStateOf("Buen estado") }

    fun resetForm() {
        tituloText = ""
        descripcionCortaText = ""
        ubicacionText = ""
        acercaDeText = ""
        descripcionLargaText = ""
        fechaText = ""
        horarioText = ""
        whatsappText = ""
        telefonoText = ""
        participantesText = ""
    }

    fun guardarContenido() {
        if (tituloText.isBlank()) {
            errorMensaje = "Por favor ingresa un título o nombre."
            return
        }

        when (tipoSeleccionado) {
            TipoContenidoAdmin.ACTIVIDAD -> {
                val idNuevo = (actividadesMockData.maxOfOrNull { it.id } ?: 0) + 1
                val nuevaActividad = ActividadMock(
                    id = idNuevo,
                    titulo = tituloText.trim(),
                    descripcion = descripcionCortaText.ifBlank { "Actividad comunitaria para familias." },
                    categoria = categoriaActividad,
                    iconoCategoria = Icons.Default.Star,
                    colorCategoria = if (categoriaActividad == "Medio Ambiente") FondoVerde else if (categoriaActividad == "Educación") FondoAzulClaro else FondoNaranja,
                    textColorCategoria = if (categoriaActividad == "Medio Ambiente") TextoVerde else if (categoriaActividad == "Educación") TextoAzul else TextoNaranja,
                    fecha = fechaText.ifBlank { "Próximamente" },
                    horario = horarioText.ifBlank { "09:00 AM - 12:00 PM" },
                    ubicacion = ubicacionText.ifBlank { "Centro Comunitario" },
                    distancia = "A 1.0 km de ti",
                    organizador = organizadorText.ifBlank { "Familias que Suman" },
                    organizadorVerificado = true,
                    acercaDe = acercaDeText.ifBlank { descripcionCortaText.ifBlank { "Acompáñanos en esta actividad familiar." } },
                    lugaresDisponibles = lugaresTotalesText.toIntOrNull() ?: 30,
                    lugaresTotales = lugaresTotalesText.toIntOrNull() ?: 30,
                    participantesAdicionales = 0,
                )
                agregarActividadMock(nuevaActividad)
            }

            TipoContenidoAdmin.PROYECTO -> {
                val nuevoProyecto = Proyecto(
                    id = System.currentTimeMillis().toString(),
                    nombre = tituloText.trim(),
                    logoUrl = null,
                    estado = if (estadoProyectoIndex == 0) EstadoProyecto.ACTIVO else EstadoProyecto.ANTERIOR,
                    ubicacion = ubicacionText.ifBlank { "Monterrey, N.L." },
                    participantes = participantesText.ifBlank { "20 Voluntarios" },
                    descripcionCorta = descripcionCortaText.ifBlank { "Proyecto social en beneficio de la comunidad." },
                    acercaDe = acercaDeText.ifBlank { descripcionCortaText.ifBlank { "Iniciativa respaldada por Familias que Suman." } },
                    descripcionLarga = descripcionLargaText.ifBlank { acercaDeText.ifBlank { descripcionCortaText } },
                    opcionesApoyo = listOf(
                        OpcionApoyo(
                            tipo = TipoApoyo.TIEMPO_TALENTO,
                            titulo = "Aportación de tiempo y voluntariado",
                            descripcion = "Súmate como voluntario activo en este proyecto.",
                        ),
                        OpcionApoyo(
                            tipo = TipoApoyo.APORTACION_ECONOMICA,
                            titulo = "Donación económica",
                            descripcion = "Apoya para adquisición de materiales y suministros.",
                        ),
                    ),
                    telefonoWhatsapp = whatsappText.ifBlank { "528112345678" },
                    telefonoLlamada = telefonoText.ifBlank { "8112345678" },
                )
                agregarProyectoMock(nuevoProyecto)
            }

            TipoContenidoAdmin.DONACION -> {
                val esCampana = tipoDonacionIndex == 0
                val nuevaDonacion = Donacion(
                    id = System.currentTimeMillis().toString(),
                    titulo = tituloText.trim(),
                    descripcionCorta = descripcionCortaText.ifBlank { "Apoya esta causa donando insumos o recursos." },
                    descripcionLarga = descripcionLargaText.ifBlank { descripcionCortaText },
                    fundacion = categoriaDonacionText.ifBlank { "Comunidad" },
                    imagenUrl = "https://images.unsplash.com/photo-1593113598332-cd288d649433?q=80&w=600&auto=format&fit=crop",
                    tipo = if (esCampana) TipoDonacion.CAMPANA else TipoDonacion.ARTICULO,
                    categoria = categoriaDonacionText,
                    recaudado = 0f,
                    meta = metaText.toFloatOrNull() ?: 1000f,
                    textoProgreso = if (esCampana) "$0 recaudados" else "0 entregados",
                    telefonoWhatsapp = whatsappText.ifBlank { "528112345678" },
                    condiciones = condicionesText.ifBlank { "Buen estado" },
                    ubicacion = ubicacionText.ifBlank { "Centro de Acopio Principal" },
                )
                agregarDonacionMock(nuevaDonacion)
            }
        }

        mostrarExitoDialog = true
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.popBackStack() },
        titulo = "Crear nuevo contenido",
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
                text = "Publicar Tarjeta",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AzulMarino,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Selecciona el tipo de contenido que deseas agregar a la plataforma.",
                fontSize = 13.sp,
                color = GrisTexto,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de Tipo de Contenido
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TipoContenidoAdmin.entries.forEach { tipo ->
                    val seleccionado = tipoSeleccionado == tipo
                    Surface(
                        onClick = {
                            tipoSeleccionado = tipo
                            errorMensaje = null
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = if (seleccionado) AzulMarino else Color.White,
                        border = if (!seleccionado) BorderStroke(1.dp, GrisBorde) else null,
                        modifier = Modifier.weight(1f),
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = when (tipo) {
                                    TipoContenidoAdmin.ACTIVIDAD -> Icons.Default.DateRange
                                    TipoContenidoAdmin.PROYECTO -> Icons.Default.Lightbulb
                                    TipoContenidoAdmin.DONACION -> Icons.Default.Favorite
                                },
                                contentDescription = null,
                                tint = if (seleccionado) Color.White else AzulMarino,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tipo.titulo,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (seleccionado) Color.White else AzulMarino,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Formulario Dinámico Card Container
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
                    Text(
                        text = "Datos de la ${tipoSeleccionado.titulo}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulMarino,
                    )

                    OutlinedTextField(
                        value = tituloText,
                        onValueChange = { tituloText = it },
                        label = { Text("Título / Nombre de la ${tipoSeleccionado.titulo}") },
                        placeholder = { Text("Ej. ${if (tipoSeleccionado == TipoContenidoAdmin.ACTIVIDAD) "Limpieza de Parque" else if (tipoSeleccionado == TipoContenidoAdmin.PROYECTO) "Voluntariado Juvenil" else "Colecta de Alimentos"}") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    )

                    OutlinedTextField(
                        value = descripcionCortaText,
                        onValueChange = { descripcionCortaText = it },
                        label = { Text("Descripción Corta") },
                        placeholder = { Text("Breve resumen visible en la tarjeta") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    )

                    OutlinedTextField(
                        value = ubicacionText,
                        onValueChange = { ubicacionText = it },
                        label = { Text("Ubicación / Ciudad") },
                        placeholder = { Text("Ej. Parque Central, Monterrey") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    )

                    // Campos específicos por Tipo
                    when (tipoSeleccionado) {
                        TipoContenidoAdmin.ACTIVIDAD -> {
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
                                    placeholder = { Text("Sáb, 15 de Nov") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                OutlinedTextField(
                                    value = horarioText,
                                    onValueChange = { horarioText = it },
                                    label = { Text("Horario") },
                                    placeholder = { Text("09:00 - 12:00") },
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

                        TipoContenidoAdmin.PROYECTO -> {
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
                                placeholder = { Text("Ej. 25 Mujeres, 100 Niños") },
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
                                label = { Text("Descripción Detallada / Misión") },
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
                                    placeholder = { Text("528112345678") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                                OutlinedTextField(
                                    value = telefonoText,
                                    onValueChange = { telefonoText = it },
                                    label = { Text("Teléfono de llamada") },
                                    placeholder = { Text("8112345678") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                            }
                        }

                        TipoContenidoAdmin.DONACION -> {
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

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                OutlinedTextField(
                                    value = categoriaDonacionText,
                                    onValueChange = { categoriaDonacionText = it },
                                    label = { Text("Categoría / Fundación") },
                                    placeholder = { Text("Ej. Alimentación, Salud") },
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
                                    modifier = Modifier.weight(0.8f),
                                    shape = RoundedCornerShape(12.dp),
                                )
                            }

                            OutlinedTextField(
                                value = descripcionLargaText,
                                onValueChange = { descripcionLargaText = it },
                                label = { Text("Descripción Detallada de la Donación") },
                                minLines = 3,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                            )

                            if (tipoDonacionIndex == 0) {
                                OutlinedTextField(
                                    value = whatsappText,
                                    onValueChange = { whatsappText = it },
                                    label = { Text("WhatsApp para Donar") },
                                    placeholder = { Text("528112345678") },
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
                                    placeholder = { Text("Ej. Nuevo o en buen estado") },
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
                        onClick = { guardarContenido() },
                        colors = ButtonDefaults.buttonColors(containerColor = Ambar, contentColor = AzulMarino),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = AzulMarino,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Publicar ${tipoSeleccionado.titulo}",
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
                    text = "¡Publicación Exitosa!",
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                )
            },
            text = {
                Text(
                    text = "La tarjeta de ${tipoSeleccionado.titulo.lowercase()} ha sido agregada correctamente y ya está visible para todos los usuarios de la comunidad.",
                    color = GrisTexto,
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarExitoDialog = false
                        resetForm()
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
