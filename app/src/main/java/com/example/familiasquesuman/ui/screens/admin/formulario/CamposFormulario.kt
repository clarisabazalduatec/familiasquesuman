package com.example.familiasquesuman.ui.screens.admin.formulario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familiasquesuman.domain.EstadoProyecto
import com.example.familiasquesuman.domain.TipoDonacion
import com.example.familiasquesuman.ui.components.SelectorCategoria
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.ColorError
import com.example.familiasquesuman.ui.theme.FondoVerde
import com.example.familiasquesuman.ui.theme.GrisBorde
import com.example.familiasquesuman.ui.theme.GrisTexto
import com.example.familiasquesuman.ui.theme.TextoVerde

// ---------------------------------------------------------------------------
// Piezas pequeñas reutilizables dentro del formulario
// ---------------------------------------------------------------------------

/** Un solo campo de texto con el estilo del proyecto (antes se repetía ~40 veces). */
@Composable
fun CampoTexto(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    placeholder: String? = null,
    ayuda: String? = null,
    teclado: KeyboardType = KeyboardType.Text,
    minLineas: Int = 1,
    maxLineas: Int = minLineas,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        placeholder = if (placeholder != null) { { Text(placeholder) } } else null,
        supportingText = if (ayuda != null) { { Text(ayuda) } } else null,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        singleLine = maxLineas == 1,
        minLines = minLineas,
        maxLines = maxLineas,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
    )
}

@Composable
private fun EtiquetaSeccion(texto: String) {
    Text(text = texto, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = AzulMarino)
}

@Composable
private fun OpcionConCheck(texto: String, marcada: Boolean, onCambio: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = marcada, onCheckedChange = onCambio)
        Text(text = texto, fontSize = 13.sp, color = AzulMarino)
    }
}

/** Selector Actividad / Proyecto / Donación (solo se muestra al crear). */
@Composable
fun SelectorTipoContenido(tipo: TipoContenidoAdmin, onSeleccion: (TipoContenidoAdmin) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TipoContenidoAdmin.entries.forEach { opcion ->
            val seleccionado = tipo == opcion
            Surface(
                onClick = { onSeleccion(opcion) },
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
                        imageVector = when (opcion) {
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
                        text = opcion.titulo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (seleccionado) Color.White else AzulMarino,
                    )
                }
            }
        }
    }
}

/** Interruptor activa / desactivada (antes solo existía al editar). */
@Composable
fun InterruptorActiva(activa: Boolean, onCambio: (Boolean) -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (activa) FondoVerde else Color(0xFFFFEBEE),
        border = BorderStroke(1.dp, if (activa) TextoVerde.copy(alpha = 0.5f) else ColorError.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (activa) "Iniciativa Activa" else "Iniciativa Desactivada",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (activa) TextoVerde else ColorError,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (activa) "Esta publicación está visible para los usuarios" else "Iniciativa oculta en la aplicación",
                    fontSize = 11.sp,
                    color = GrisTexto,
                )
            }
            Switch(
                checked = activa,
                onCheckedChange = onCambio,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = TextoVerde,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = ColorError,
                ),
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Secciones por tipo. Cada una recibe SU data class y devuelve una copia modificada.
// Se llaman dentro de una Column, así que sus campos quedan como hijos directos de ella.
// ---------------------------------------------------------------------------

@Composable
fun CamposComunes(
    tipo: TipoContenidoAdmin,
    form: ComunForm,
    onCambio: (ComunForm) -> Unit,
    ayudaUbicacion: String? = null,
) {
    val ejemplo = when (tipo) {
        TipoContenidoAdmin.ACTIVIDAD -> "Limpieza de Parque"
        TipoContenidoAdmin.PROYECTO -> "Voluntariado Juvenil"
        TipoContenidoAdmin.DONACION -> "Colecta de Alimentos"
    }
    CampoTexto(
        valor = form.titulo,
        onCambio = { onCambio(form.copy(titulo = it)) },
        etiqueta = "Título / Nombre de la ${tipo.titulo}",
        placeholder = "Ej. $ejemplo",
    )
    CampoTexto(
        valor = form.descripcionCorta,
        onCambio = { onCambio(form.copy(descripcionCorta = it)) },
        etiqueta = "Descripción corta",
        placeholder = "Breve resumen visible en la tarjeta",
        maxLineas = 2,
    )
    CampoTexto(
        valor = form.ubicacion,
        onCambio = { onCambio(form.copy(ubicacion = it)) },
        etiqueta = "Ubicación / Ciudad",
        placeholder = "Ej. Parque Central, Monterrey",
        ayuda = ayudaUbicacion,
    )
}

@Composable
fun CamposActividad(
    form: ActividadForm,
    categorias: List<String>,
    onNuevaCategoria: (String) -> Unit,
    onCambio: (ActividadForm) -> Unit,
) {
    SelectorCategoria(
        categorias = categorias,
        seleccionada = form.categoria,
        onSeleccionar = { onCambio(form.copy(categoria = it)) },
        onNuevaCategoria = onNuevaCategoria,
        titulo = "Categoría de Actividad",
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoTexto(form.fecha, { onCambio(form.copy(fecha = it)) }, "Fecha", Modifier.weight(1f), placeholder = "Sáb, 15 de Nov")
        CampoTexto(form.horario, { onCambio(form.copy(horario = it)) }, "Horario", Modifier.weight(1f), placeholder = "09:00 - 12:00")
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoTexto(form.organizador, { onCambio(form.copy(organizador = it)) }, "Organizador", Modifier.weight(1f))
        CampoTexto(
            form.lugaresTotales,
            { onCambio(form.copy(lugaresTotales = it.filter(Char::isDigit))) },
            "Lugares",
            Modifier.weight(0.8f),
            teclado = KeyboardType.Number,
        )
    }
    CampoTexto(
        valor = form.acercaDe,
        onCambio = { onCambio(form.copy(acercaDe = it)) },
        etiqueta = "Acerca de la actividad (detallado)",
        minLineas = 3,
        maxLineas = Int.MAX_VALUE,
    )
}

@Composable
fun CamposProyecto(form: ProyectoForm, onCambio: (ProyectoForm) -> Unit) {
    EtiquetaSeccion("Estado del Proyecto")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = form.estado == EstadoProyecto.ACTIVO,
            onClick = { onCambio(form.copy(estado = EstadoProyecto.ACTIVO)) },
            label = { Text("Proyecto Activo") },
        )
        FilterChip(
            selected = form.estado == EstadoProyecto.ANTERIOR,
            onClick = { onCambio(form.copy(estado = EstadoProyecto.ANTERIOR)) },
            label = { Text("Proyecto Anterior") },
        )
    }

    CampoTexto(
        valor = form.participantes,
        onCambio = { onCambio(form.copy(participantes = it)) },
        etiqueta = "Participantes / Beneficiarios",
        placeholder = "Ej. 25 Mujeres, 100 Niños",
    )
    CampoTexto(
        valor = form.acercaDe,
        onCambio = { onCambio(form.copy(acercaDe = it)) },
        etiqueta = "Acerca del Proyecto",
        maxLineas = 2,
    )
    CampoTexto(
        valor = form.descripcionLarga,
        onCambio = { onCambio(form.copy(descripcionLarga = it)) },
        etiqueta = "Descripción detallada / Misión",
        minLineas = 3,
        maxLineas = Int.MAX_VALUE,
    )

    EtiquetaSeccion("Formas de apoyo que se muestran en el proyecto")
    OpcionConCheck("Tiempo y voluntariado", form.aceptaVoluntarios) { onCambio(form.copy(aceptaVoluntarios = it)) }
    OpcionConCheck("Aportación económica", form.aceptaAportacion) { onCambio(form.copy(aceptaAportacion = it)) }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        CampoTexto(
            form.whatsapp, { onCambio(form.copy(whatsapp = it)) }, "WhatsApp de contacto", Modifier.weight(1f),
            placeholder = "528112345678", teclado = KeyboardType.Phone,
        )
        CampoTexto(
            form.telefono, { onCambio(form.copy(telefono = it)) }, "Teléfono de llamada", Modifier.weight(1f),
            placeholder = "8112345678", teclado = KeyboardType.Phone,
        )
    }
}

@Composable
fun CamposDonacion(
    form: DonacionForm,
    esEdicion: Boolean,
    categorias: List<String>,
    onNuevaCategoria: (String) -> Unit,
    onCambio: (DonacionForm) -> Unit,
) {
    val esCampana = form.tipo == TipoDonacion.CAMPANA

    EtiquetaSeccion("Tipo de Donación")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = esCampana,
            onClick = { onCambio(form.copy(tipo = TipoDonacion.CAMPANA)) },
            label = { Text("Campaña Monetaria") },
        )
        FilterChip(
            selected = !esCampana,
            onClick = { onCambio(form.copy(tipo = TipoDonacion.ESPECIE)) },
            label = { Text("Artículo / En Especie") },
        )
    }

    SelectorCategoria(
        categorias = categorias,
        seleccionada = form.categoria,
        onSeleccionar = { onCambio(form.copy(categoria = it)) },
        onNuevaCategoria = onNuevaCategoria,
        titulo = "Categoría de la Donación",
    )

    // Antes un solo campo llenaba "categoría" y "fundación" a la vez; ahora son dos datos distintos.
    CampoTexto(
        valor = form.fundacion,
        onCambio = { onCambio(form.copy(fundacion = it)) },
        etiqueta = "Fundación / Organización",
        placeholder = "Ej. Comedor San José",
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        if (esEdicion) {
            CampoTexto(
                form.recaudado,
                { onCambio(form.copy(recaudado = it.filter(Char::isDigit))) },
                if (esCampana) "Recaudado ($)" else "Entregados (Cant.)",
                Modifier.weight(1f),
                teclado = KeyboardType.Number,
            )
        }
        CampoTexto(
            form.meta,
            { onCambio(form.copy(meta = it.filter(Char::isDigit))) },
            if (esCampana) "Meta ($)" else "Meta (Cant.)",
            Modifier.weight(1f),
            teclado = KeyboardType.Number,
        )
    }

    CampoTexto(
        valor = form.descripcionLarga,
        onCambio = { onCambio(form.copy(descripcionLarga = it)) },
        etiqueta = "Descripción detallada",
        minLineas = 3,
        maxLineas = Int.MAX_VALUE,
    )

    if (esCampana) {
        CampoTexto(
            form.whatsapp, { onCambio(form.copy(whatsapp = it)) }, "WhatsApp para donar",
            placeholder = "528112345678", teclado = KeyboardType.Phone,
        )
    } else {
        CampoTexto(
            form.condiciones, { onCambio(form.copy(condiciones = it)) }, "Condiciones de entrega (opcional)",
            placeholder = "Ej. Nuevo o en buen estado",
        )
    }
}
