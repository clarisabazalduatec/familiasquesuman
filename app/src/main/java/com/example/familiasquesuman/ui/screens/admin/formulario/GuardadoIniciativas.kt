package com.example.familiasquesuman.ui.screens.admin.formulario

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import com.example.familiasquesuman.data.ActividadMock
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.data.agregarActividadMock
import com.example.familiasquesuman.data.remote.dto.DonacionGuardarDto
import com.example.familiasquesuman.data.remote.dto.DonacionOpcionInDto
import com.example.familiasquesuman.domain.CentroVisiteo
import com.example.familiasquesuman.ui.screens.directorio.agregarCentroMock
import com.example.familiasquesuman.ui.screens.directorio.centrosDeEjemplo
import com.example.familiasquesuman.ui.theme.FondoAzulClaro
import com.example.familiasquesuman.ui.theme.TextoAzul

/** Convierte los campos del formulario de donación al DTO que acepta el Backend (FastAPI). */
fun DonacionForm.toGuardarDto(comun: ComunForm, imagenes: List<String>): DonacionGuardarDto {
    val modalidad = when (tipoDonacionAdmin) {
        TipoDonacionAdmin.ESPECIE -> "ESPECIE"
        TipoDonacionAdmin.TIEMPO -> "TIEMPO"
        TipoDonacionAdmin.MONETARIA -> "CAMPANA"
    }

    val esTiempo = tipoDonacionAdmin == TipoDonacionAdmin.TIEMPO
    val esEspecie = tipoDonacionAdmin == TipoDonacionAdmin.ESPECIE
    val esMonetaria = tipoDonacionAdmin == TipoDonacionAdmin.MONETARIA

    // Usa DonacionOpcionInDto y asigna 0.0 en lugar de null para evitar errores de tipo
    val listaOpciones: List<DonacionOpcionInDto> = when {
        esMonetaria && montosSugeridos.isNotBlank() -> {
            montosSugeridos.split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .map { valor ->
                    val montoNum = valor.replace("$", "").replace("MXN", "").trim().toDoubleOrNull() ?: 0.0
                    DonacionOpcionInDto(titulo = "Aporte sugerido", monto = montoNum)
                }
        }
        esEspecie && articulosNecesarios.isNotBlank() -> {
            articulosNecesarios.split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .map { articulo ->
                    DonacionOpcionInDto(titulo = articulo, monto = 0.0) // 👈 0.0 en vez de null
                }
        }
        else -> emptyList()
    }

    return DonacionGuardarDto(
        titulo = comun.titulo.trim(),
        descripcionCorta = comun.descripcionCorta.trim(),
        modalidad = modalidad,
        categoria = if (esEspecie) categoria.trim().ifBlank { null } else null,
        ubicacion = comun.ubicacion.trim().ifBlank { null },
        meta = if (tieneMeta) meta.toDoubleOrNull() else null,
        recaudado = recaudado.toDoubleOrNull() ?: 0.0,
        descripcionLarga = descripcionLarga.trim().ifBlank { null },
        whatsapp = whatsapp.trim().ifBlank { null },
        condicionesRecepcion = if (esEspecie) condiciones.trim().ifBlank { null } else null,
        fechaLimite = if (esTiempo) fechaLimite.trim().ifBlank { null } else null,
        horario = if (esTiempo) horario.trim().ifBlank { null } else null,
        activa = comun.activa,
        imagenes = imagenes,
        organizacionNombre = fundacion.trim().ifBlank { null },
        opciones = listaOpciones
    )
}

fun guardarActividad(base: ActividadMock?, comun: ComunForm, form: ActividadForm, imagenes: List<String>) {
    val total = form.lugaresTotales.toIntOrNull() ?: 30
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
                colorCategoria = FondoAzulClaro,
                textColorCategoria = TextoAzul,
                fecha = form.fecha.trim(),
                horario = form.horario.trim(),
                ubicacion = comun.ubicacion.trim(),
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

fun guardarCentro(base: CentroVisiteo?, comun: ComunForm, form: CentroForm, imagenes: List<String>) {
    val necesidades = form.necesidades.map { it.trim() }.filter { it.isNotEmpty() }

    if (base == null) {
        agregarCentroMock(
            CentroVisiteo(
                id = System.currentTimeMillis().toString(),
                nombre = comun.titulo.trim(),
                tipo = form.tipo,
                descripcionCorta = comun.descripcionCorta.trim(),
                informacionGeneral = form.informacionGeneral.trim(),
                necesidades = necesidades,
                direccion = comun.ubicacion.trim(),
                logoUrl = imagenes.firstOrNull(),
                comoAyudar = form.comoAyudar.trim(),
                recomendaciones = form.recomendaciones.trim(),
                telefono = form.telefono.trim().ifBlank { null },
                whatsapp = form.whatsapp.trim().ifBlank { null },
                verificado = form.verificado,
                activo = comun.activa,
            ),
        )
    } else {
        val index = centrosDeEjemplo.indexOfFirst { it.id == base.id }
        if (index == -1) return
        centrosDeEjemplo[index] = centrosDeEjemplo[index].copy(
            nombre = comun.titulo.trim(),
            tipo = form.tipo,
            descripcionCorta = comun.descripcionCorta.trim(),
            informacionGeneral = form.informacionGeneral.trim(),
            necesidades = necesidades,
            direccion = comun.ubicacion.trim(),
            logoUrl = imagenes.firstOrNull(),
            comoAyudar = form.comoAyudar.trim(),
            recomendaciones = form.recomendaciones.trim(),
            telefono = form.telefono.trim().ifBlank { null },
            whatsapp = form.whatsapp.trim().ifBlank { null },
            verificado = form.verificado,
            activo = comun.activa,
        )
    }
}

fun eliminarIniciativa(tipo: TipoContenidoAdmin, id: String) {
    when (tipo) {
        TipoContenidoAdmin.ACTIVIDAD -> actividadesMockData.removeAll { it.id == id.toIntOrNull() }
        TipoContenidoAdmin.PROYECTO -> Unit
        TipoContenidoAdmin.DONACION -> Unit
        TipoContenidoAdmin.DIRECTORIO -> centrosDeEjemplo.removeAll { it.id == id }
    }
}