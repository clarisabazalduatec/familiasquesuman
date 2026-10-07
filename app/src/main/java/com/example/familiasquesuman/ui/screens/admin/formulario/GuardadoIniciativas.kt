package com.example.familiasquesuman.ui.screens.admin.formulario

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import com.example.familiasquesuman.data.ActividadMock
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.data.agregarActividadMock
import com.example.familiasquesuman.data.repository.agregarDonacionMock
import com.example.familiasquesuman.data.repository.donacionesMockData
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.OpcionApoyo
import com.example.familiasquesuman.domain.Proyecto
import com.example.familiasquesuman.domain.TipoApoyo
import com.example.familiasquesuman.domain.TipoDonacion
import com.example.familiasquesuman.ui.screens.proyectos.agregarProyectoMock
import com.example.familiasquesuman.ui.screens.proyectos.proyectosMockData
import com.example.familiasquesuman.ui.theme.FondoAzulClaro
import com.example.familiasquesuman.ui.theme.TextoAzul

// ---------------------------------------------------------------------------
// TEMPORAL: todo lo que toca las listas mock vive SOLO en este archivo.
// Cuando exista el backend, se reemplaza por un repositorio + ViewModel (Fase 5)
// y la pantalla y los formularios no cambian.
// ---------------------------------------------------------------------------

/** base == null -> crear; base != null -> editar. */
fun guardarActividad(base: ActividadMock?, comun: ComunForm, form: ActividadForm, imagenes: List<String>) {
    val total = form.lugaresTotales.toInt()
    val descripcion = comun.descripcionCorta.trim()
    val acercaDe = form.acercaDe.trim().ifBlank { descripcion }

    if (base == null) {
        agregarActividadMock(
            ActividadMock(
                id = (actividadesMockData.maxOfOrNull { it.id } ?: 0) + 1,
                titulo = comun.titulo.trim(),
                descripcion = descripcion,
                categoria = form.categoria,
                iconoCategoria = Icons.Default.Star,
                // TODO: borrar estas dos líneas cuando quites los colores de ActividadMock
                colorCategoria = FondoAzulClaro,
                textColorCategoria = TextoAzul,
                fecha = form.fecha.trim(),
                horario = form.horario.trim(),
                ubicacion = comun.ubicacion.trim(),
                // La distancia real depende de la ubicación del usuario: se calculará con el backend.
                distancia = "",
                organizador = form.organizador.trim(),
                organizadorVerificado = true,
                acercaDe = acercaDe,
                lugaresDisponibles = total,
                lugaresTotales = total,
                participantesAdicionales = 0,
                imagenesUrl = imagenes,
                activa = comun.activa,
            ),
        )
    } else {
        val index = actividadesMockData.indexOfFirst { it.id == base.id }
        if (index == -1) return
        val vieja = actividadesMockData[index]
        // Si cambia el total, los lugares disponibles se ajustan por la misma diferencia.
        val disponibles = (vieja.lugaresDisponibles + (total - vieja.lugaresTotales)).coerceIn(0, total)
        actividadesMockData[index] = vieja.copy(
            titulo = comun.titulo.trim(),
            descripcion = descripcion,
            categoria = form.categoria,
            fecha = form.fecha.trim(),
            horario = form.horario.trim(),
            ubicacion = comun.ubicacion.trim(),
            organizador = form.organizador.trim(),
            acercaDe = acercaDe,
            lugaresTotales = total,
            lugaresDisponibles = disponibles,
            imagenesUrl = imagenes,
            activa = comun.activa,
        )
    }
}

fun guardarProyecto(base: Proyecto?, comun: ComunForm, form: ProyectoForm, imagenes: List<String>) {
    val descripcion = comun.descripcionCorta.trim()
    val acercaDe = form.acercaDe.trim().ifBlank { descripcion }
    val descripcionLarga = form.descripcionLarga.trim().ifBlank { acercaDe }

    if (base == null) {
        agregarProyectoMock(
            Proyecto(
                id = System.currentTimeMillis().toString(),
                nombre = comun.titulo.trim(),
                logoUrl = imagenes.firstOrNull(),
                estado = form.estado,
                ubicacion = comun.ubicacion.trim(),
                participantes = form.participantes.trim(),
                descripcionCorta = descripcion,
                acercaDe = acercaDe,
                descripcionLarga = descripcionLarga,
                opcionesApoyo = construirOpcionesApoyo(form, emptyList()),
                telefonoWhatsapp = form.whatsapp.trim().ifBlank { null },
                telefonoLlamada = form.telefono.trim().ifBlank { null },
                imagenesUrl = imagenes,
                activo = comun.activa,
            ),
        )
    } else {
        val index = proyectosMockData.indexOfFirst { it.id == base.id }
        if (index == -1) return
        val viejo = proyectosMockData[index]
        proyectosMockData[index] = viejo.copy(
            nombre = comun.titulo.trim(),
            logoUrl = imagenes.firstOrNull(), // si el admin quitó la imagen, se respeta
            estado = form.estado,
            ubicacion = comun.ubicacion.trim(),
            participantes = form.participantes.trim(),
            descripcionCorta = descripcion,
            acercaDe = acercaDe,
            descripcionLarga = descripcionLarga,
            opcionesApoyo = construirOpcionesApoyo(form, viejo.opcionesApoyo),
            telefonoWhatsapp = form.whatsapp.trim().ifBlank { null },
            telefonoLlamada = form.telefono.trim().ifBlank { null },
            imagenesUrl = imagenes,
            activo = comun.activa,
        )
    }
}

fun guardarDonacion(base: Donacion?, comun: ComunForm, form: DonacionForm, imagenes: List<String>) {
    val esCampana = form.tipo == TipoDonacion.CAMPANA
    val recaudado = form.recaudado.toFloatOrNull() ?: 0f
    val meta = form.meta.toFloat()
    val descripcion = comun.descripcionCorta.trim()
    val descripcionLarga = form.descripcionLarga.trim().ifBlank { descripcion }
    // WhatsApp solo aplica a campañas; condiciones solo a donaciones en especie.
    val whatsapp = if (esCampana) form.whatsapp.trim().ifBlank { null } else null
    val condiciones = if (esCampana) null else form.condiciones.trim().ifBlank { null }

    if (base == null) {
        agregarDonacionMock(
            Donacion(
                id = System.currentTimeMillis().toString(),
                titulo = comun.titulo.trim(),
                descripcionCorta = descripcion,
                descripcionLarga = descripcionLarga,
                fundacion = form.fundacion.trim(),
                imagenUrl = imagenes.first(),
                tipo = form.tipo,
                categoria = form.categoria,
                recaudado = 0f,
                meta = meta,
                textoProgreso = textoProgresoDonacion(form.tipo, 0f),
                telefonoWhatsapp = whatsapp,
                condiciones = condiciones,
                ubicacion = comun.ubicacion.trim().ifBlank { null },
                imagenesUrl = imagenes,
                activa = comun.activa,
            ),
        )
    } else {
        val index = donacionesMockData.indexOfFirst { it.id == base.id }
        if (index == -1) return
        donacionesMockData[index] = donacionesMockData[index].copy(
            titulo = comun.titulo.trim(),
            descripcionCorta = descripcion,
            descripcionLarga = descripcionLarga,
            fundacion = form.fundacion.trim(),
            imagenUrl = imagenes.first(),
            tipo = form.tipo,
            categoria = form.categoria,
            recaudado = recaudado,
            meta = meta,
            textoProgreso = textoProgresoDonacion(form.tipo, recaudado),
            telefonoWhatsapp = whatsapp,
            condiciones = condiciones,
            ubicacion = comun.ubicacion.trim().ifBlank { null },
            imagenesUrl = imagenes,
            activa = comun.activa,
        )
    }
}

fun eliminarIniciativa(tipo: TipoContenidoAdmin, id: String) {
    when (tipo) {
        TipoContenidoAdmin.ACTIVIDAD -> actividadesMockData.removeAll { it.id == id.toIntOrNull() }
        TipoContenidoAdmin.PROYECTO -> proyectosMockData.removeAll { it.id == id }
        TipoContenidoAdmin.DONACION -> donacionesMockData.removeAll { it.id == id }
    }
}

// Antes Crear decía "entregados" y Editar "entregadas"; ahora hay un solo lugar.
private fun textoProgresoDonacion(tipo: TipoDonacion, cantidad: Float): String =
    if (tipo == TipoDonacion.CAMPANA) "\$${cantidad.toInt()} recaudados" else "${cantidad.toInt()} entregadas"

/** Solo se crean las opciones que el admin marcó; conserva las ya escritas al editar. */
private fun construirOpcionesApoyo(form: ProyectoForm, existentes: List<OpcionApoyo>): List<OpcionApoyo> = buildList {
    if (form.aceptaVoluntarios) {
        add(
            existentes.firstOrNull { it.tipo == TipoApoyo.TIEMPO_TALENTO }
                ?: OpcionApoyo(
                    tipo = TipoApoyo.TIEMPO_TALENTO,
                    titulo = "Aportación de tiempo y voluntariado",
                    descripcion = "Súmate como voluntario activo en este proyecto.",
                ),
        )
    }
    if (form.aceptaAportacion) {
        add(
            existentes.firstOrNull { it.tipo == TipoApoyo.APORTACION_ECONOMICA }
                ?: OpcionApoyo(
                    tipo = TipoApoyo.APORTACION_ECONOMICA,
                    titulo = "Aportación económica",
                    descripcion = "Apoya con donativos para materiales y suministros.",
                ),
        )
    }
    // Cualquier otro tipo de apoyo que ya existiera se conserva tal cual.
    addAll(existentes.filter { it.tipo != TipoApoyo.TIEMPO_TALENTO && it.tipo != TipoApoyo.APORTACION_ECONOMICA })
}
