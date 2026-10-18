package com.example.familiasquesuman.ui.screens.admin.formulario

import com.example.familiasquesuman.data.remote.dto.DonacionGuardarDto
import com.example.familiasquesuman.data.remote.dto.DonacionOpcionInDto

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
                    DonacionOpcionInDto(titulo = articulo, monto = 0.0)
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

fun eliminarIniciativa(tipo: TipoContenidoAdmin, id: String) {
    when (tipo) {
        TipoContenidoAdmin.ACTIVIDAD -> Unit // se borra en el backend (AdminActividadViewModel)
        TipoContenidoAdmin.PROYECTO -> Unit  // se borra en el backend (AdminProyectoViewModel)
        TipoContenidoAdmin.DONACION -> Unit // DonacionViewModel
        TipoContenidoAdmin.DIRECTORIO -> Unit // se borra en el backend (AdminCentroViewModel)
    }
}
