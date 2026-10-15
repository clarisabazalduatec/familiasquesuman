package com.example.familiasquesuman.ui.screens.admin.formulario

import com.example.familiasquesuman.data.repository.agregarDonacionMock
import com.example.familiasquesuman.data.repository.donacionesMockData
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.TipoDonacion

// ---------------------------------------------------------------------------
// TEMPORAL: todo lo que toca las listas mock vive SOLO en este archivo.
// Cuando exista el backend, se reemplaza por un repositorio + ViewModel (Fase 5)
// y la pantalla y los formularios no cambian.
// ---------------------------------------------------------------------------

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
        TipoContenidoAdmin.ACTIVIDAD -> Unit // se borra en el backend (AdminActividadViewModel)
        TipoContenidoAdmin.PROYECTO -> Unit  // se borra en el backend (AdminProyectoViewModel)
        TipoContenidoAdmin.DONACION -> donacionesMockData.removeAll { it.id == id }
        TipoContenidoAdmin.DIRECTORIO -> Unit // se borra en el backend (AdminCentroViewModel)
    }
}

private fun textoProgresoDonacion(tipo: TipoDonacion, cantidad: Float): String =
    if (tipo == TipoDonacion.CAMPANA) "\$${cantidad.toInt()} recaudados" else "${cantidad.toInt()} entregadas"
