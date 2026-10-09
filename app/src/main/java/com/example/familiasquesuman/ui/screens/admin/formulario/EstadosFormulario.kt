package com.example.familiasquesuman.ui.screens.admin.formulario

import com.example.familiasquesuman.data.ActividadMock
import com.example.familiasquesuman.domain.CentroVisiteo
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.EstadoProyecto
import com.example.familiasquesuman.domain.Proyecto
import com.example.familiasquesuman.domain.TipoApoyo
import com.example.familiasquesuman.domain.TipoCentro
import com.example.familiasquesuman.domain.TipoDonacion

// ---------------------------------------------------------------------------
// Estado del formulario: un data class por grupo de campos, en vez de ~25 variables sueltas.
// Todo lo que el usuario escribe es String; se convierte a número/enum solo al guardar.
// ---------------------------------------------------------------------------

/** Campos que comparten los 3 tipos. */
data class ComunForm(
    val titulo: String = "",
    val descripcionCorta: String = "",
    val ubicacion: String = "",
    val activa: Boolean = true,
)

data class ActividadForm(
    val categoria: String = "",
    val fecha: String = "",
    val horario: String = "",
    val organizador: String = "Familias que Suman",
    val lugaresTotales: String = "30",
    val acercaDe: String = "",
)

data class ProyectoForm(
    val estado: EstadoProyecto = EstadoProyecto.ACTIVO,
    val participantes: String = "",
    val acercaDe: String = "",
    val descripcionLarga: String = "",
    val whatsapp: String = "",
    val telefono: String = "",
    val aceptaVoluntarios: Boolean = true,
    val aceptaAportacion: Boolean = false,
)

data class DonacionForm(
    val tipo: TipoDonacion = TipoDonacion.CAMPANA,
    val categoria: String = "",
    val fundacion: String = "",
    val recaudado: String = "0",
    val meta: String = "",
    val descripcionLarga: String = "",
    val whatsapp: String = "",
    val condiciones: String = "",
)

/**
 * Centro de visiteo. Campos de la tabla centros_visiteo; nombre, descripción corta, dirección y
 * "activo" viajan en ComunForm (titulo, descripcionCorta, ubicacion, activa).
 * `necesidades` = filas de centro_necesidades; el orden de la lista es la columna `orden`.
 */
data class CentroForm(
    val tipo: TipoCentro = TipoCentro.ASILO,
    val informacionGeneral: String = "",
    val necesidades: List<String> = listOf(""),
    val comoAyudar: String = "",
    val recomendaciones: String = "",
    val telefono: String = "",
    val whatsapp: String = "",
    val verificado: Boolean = true,
)

// ---------------------------------------------------------------------------
// Modelo -> formulario (para precargar al editar)
// ---------------------------------------------------------------------------

fun ActividadMock.aFormulario(): Pair<ComunForm, ActividadForm> =
    ComunForm(titulo, descripcion, ubicacion, activa) to ActividadForm(
        categoria = categoria,
        fecha = fecha,
        horario = horario,
        organizador = organizador,
        lugaresTotales = lugaresTotales.toString(),
        acercaDe = acercaDe,
    )

fun Proyecto.aFormulario(): Pair<ComunForm, ProyectoForm> =
    ComunForm(nombre, descripcionCorta, ubicacion, activo) to ProyectoForm(
        estado = estado,
        participantes = participantes,
        acercaDe = acercaDe,
        descripcionLarga = descripcionLarga,
        whatsapp = telefonoWhatsapp.orEmpty(),
        telefono = telefonoLlamada.orEmpty(),
        aceptaVoluntarios = opcionesApoyo.any { it.tipo == TipoApoyo.TIEMPO_TALENTO },
        aceptaAportacion = opcionesApoyo.any { it.tipo == TipoApoyo.APORTACION_ECONOMICA },
    )

fun Donacion.aFormulario(): Pair<ComunForm, DonacionForm> =
    ComunForm(titulo, descripcionCorta, ubicacion.orEmpty(), activa) to DonacionForm(
        tipo = tipo,
        categoria = categoria.orEmpty(),
        fundacion = fundacion,
        recaudado = recaudado.toInt().toString(),
        meta = meta.toInt().toString(),
        descripcionLarga = descripcionLarga,
        whatsapp = telefonoWhatsapp.orEmpty(),
        condiciones = condiciones.orEmpty(),
    )

fun CentroVisiteo.aFormulario(): Pair<ComunForm, CentroForm> =
    ComunForm(nombre, descripcionCorta, direccion, activo) to CentroForm(
        tipo = tipo,
        informacionGeneral = informacionGeneral.orEmpty(),
        necesidades = necesidades.ifEmpty { listOf("") },
        comoAyudar = comoAyudar.orEmpty(),
        recomendaciones = recomendaciones.orEmpty(),
        telefono = telefono.orEmpty(),
        whatsapp = whatsapp.orEmpty(),
        verificado = verificado,
    )

// ---------------------------------------------------------------------------
// Validación: devuelve el mensaje de error, o null si todo está bien.
// Reemplaza los valores inventados que antes se guardaban en silencio.
// ---------------------------------------------------------------------------

private const val MSG_TELEFONO = "Los teléfonos deben tener solo dígitos, con lada (ej. 528112345678)."

fun telefonoValido(texto: String): Boolean =
    texto.isBlank() || (texto.all { it.isDigit() } && texto.length in 10..13)

fun validarFormulario(
    tipo: TipoContenidoAdmin,
    comun: ComunForm,
    actividad: ActividadForm,
    proyecto: ProyectoForm,
    donacion: DonacionForm,
    centro: CentroForm,
    imagenes: List<String>,
): String? {
    if (comun.titulo.isBlank()) return "El título no puede estar vacío."
    if (comun.descripcionCorta.isBlank()) return "Agrega una descripción corta."

    return when (tipo) {
        TipoContenidoAdmin.ACTIVIDAD -> when {
            comun.ubicacion.isBlank() -> "Indica la ubicación."
            actividad.categoria.isBlank() -> "Elige o crea una categoría."
            actividad.fecha.isBlank() -> "Indica la fecha."
            actividad.horario.isBlank() -> "Indica el horario."
            actividad.organizador.isBlank() -> "Indica el organizador."
            (actividad.lugaresTotales.toIntOrNull() ?: 0) <= 0 -> "Los lugares deben ser un número mayor a 0."
            else -> null
        }

        TipoContenidoAdmin.PROYECTO -> when {
            comun.ubicacion.isBlank() -> "Indica la ubicación."
            proyecto.participantes.isBlank() -> "Indica los participantes o beneficiarios."
            !telefonoValido(proyecto.whatsapp) || !telefonoValido(proyecto.telefono) -> MSG_TELEFONO
            else -> null
        }

        TipoContenidoAdmin.DONACION -> when {
            donacion.fundacion.isBlank() -> "Indica la fundación u organización."
            donacion.categoria.isBlank() -> "Elige o crea una categoría."
            imagenes.isEmpty() -> "Agrega una imagen para la donación."
            (donacion.meta.toFloatOrNull() ?: 0f) <= 0f -> "La meta debe ser mayor a 0."
            (donacion.recaudado.toFloatOrNull() ?: -1f) < 0f -> "El monto recaudado no es válido."
            donacion.tipo == TipoDonacion.ESPECIE && comun.ubicacion.isBlank() -> "Indica el centro de acopio."
            donacion.tipo == TipoDonacion.CAMPANA && donacion.whatsapp.isBlank() -> "Indica el WhatsApp para donar."
            !telefonoValido(donacion.whatsapp) -> MSG_TELEFONO
            else -> null
        }

        TipoContenidoAdmin.DIRECTORIO -> when {
            comun.ubicacion.isBlank() -> "Indica la dirección del centro."
            centro.informacionGeneral.isBlank() -> "Agrega la información general del centro."
            centro.comoAyudar.isBlank() -> "Explica cómo se puede ayudar al centro."
            !telefonoValido(centro.telefono) || !telefonoValido(centro.whatsapp) -> MSG_TELEFONO
            else -> null
        }
    }
}
