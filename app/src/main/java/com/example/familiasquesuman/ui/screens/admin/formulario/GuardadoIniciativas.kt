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

/**
 * Centro de visiteo. Es la ÚNICA función (junto con CentroVisiteo.aFormulario) que conoce los
 * campos del modelo CentroVisiteo; si el modelo cambia, se ajusta aquí.
 */


fun eliminarIniciativa(tipo: TipoContenidoAdmin, id: String) {
    when (tipo) {
        TipoContenidoAdmin.ACTIVIDAD -> actividadesMockData.removeAll { it.id == id.toIntOrNull() }
        TipoContenidoAdmin.PROYECTO -> Unit
        TipoContenidoAdmin.DONACION -> donacionesMockData.removeAll { it.id == id }
        TipoContenidoAdmin.DIRECTORIO -> Unit // se borra en el backend (AdminCentroViewModel)
    }
}

// Antes Crear decía "entregados" y Editar "entregadas"; ahora hay un solo lugar.
private fun textoProgresoDonacion(tipo: TipoDonacion, cantidad: Float): String =
    if (tipo == TipoDonacion.CAMPANA) "\$${cantidad.toInt()} recaudados" else "${cantidad.toInt()} entregadas"

