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
// Estado del formulario: un data class por grupo de campos.
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

data class OpcionApoyoForm(
    val titulo: String = "",
    val descripcion: String = "",
)

/** ======= PROYECTO =======*/
data class ProyectoForm(
    val estado: EstadoProyecto = EstadoProyecto.ACTIVO,
    val participantes: String = "",
    val acercaDe: String = "",
    val descripcionLarga: String = "",
    val comoAyudar: String = "",
    val instagram: String = "",
    val whatsapp: String = "",
    val telefono: String = "",
    val opciones: List<OpcionApoyoForm> = emptyList(),
)

enum class TipoDonacionAdmin(val titulo: String) {
    MONETARIA("Campaña Monetaria"),
    ESPECIE("En Especie"),
    TIEMPO("Tiempo")
}

data class DonacionForm(
    val tipo: TipoDonacion = TipoDonacion.CAMPANA,
    val tipoDonacionAdmin: TipoDonacionAdmin = TipoDonacionAdmin.MONETARIA,
    val categoria: String = "",
    val fundacion: String = "",
    val recaudado: String = "0",
    val meta: String = "",
    val descripcionLarga: String = "",
    val whatsapp: String = "",
    val condiciones: String = "",
    // Campos dinámicos
    val tieneMeta: Boolean = false,
    val montosSugeridos: String = "", // Para Monetaria (ej. "100, 200, 500")
    val articulosNecesarios: String = "", // Para Especie
    val fechaLimite: String = "", // Para Tiempo
    val horario: String = "", // Para Tiempo
)

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
        comoAyudar = comoAyudar.orEmpty(),
        instagram = instagramUrl.orEmpty(),
        whatsapp = telefonoWhatsapp.orEmpty(),
        telefono = telefonoLlamada.orEmpty(),
        opciones = opcionesApoyo.map { OpcionApoyoForm(titulo = it.titulo, descripcion = it.descripcion) },
    )

fun Donacion.aFormulario(): Pair<ComunForm, DonacionForm> {
    val esMonetaria = tipo == TipoDonacion.CAMPANA
    val esEspecie = tipo == TipoDonacion.ESPECIE

    // Forma súper segura de Kotlin sin usar operadores '?' dudosos
    val montosTexto = if (esMonetaria && !opcionesDetalle.isNullOrEmpty()) {
        opcionesDetalle!!.map { pair ->
            val valor = pair.second.ifBlank { pair.first }
            valor.replace("$", "").replace("MXN", "").replace(".0", "").trim()
        }.filter { it.isNotBlank() }.joinToString(", ")
    } else ""

    val articulosTexto = if (esEspecie && !opcionesDetalle.isNullOrEmpty()) {
        opcionesDetalle!!.map { pair ->
            pair.first.ifBlank { pair.second }.trim()
        }.filter { it.isNotBlank() }.joinToString(", ")
    } else ""

    return ComunForm(
        titulo = titulo,
        descripcionCorta = descripcionCorta,
        ubicacion = ubicacion.orEmpty(),
        activa = activa
    ) to DonacionForm(
        tipo = tipo,
        tipoDonacionAdmin = when (tipo) {
            TipoDonacion.ESPECIE -> TipoDonacionAdmin.ESPECIE
            TipoDonacion.TIEMPO -> TipoDonacionAdmin.TIEMPO
            else -> TipoDonacionAdmin.MONETARIA
        },
        categoria = categoria.orEmpty(),
        fundacion = fundacion,
        recaudado = recaudado.toInt().toString(),
        meta = meta.toInt().toString(),
        tieneMeta = meta > 0f,
        montosSugeridos = montosTexto,
        articulosNecesarios = articulosTexto.ifBlank { condiciones.orEmpty() },
        descripcionLarga = descripcionLarga,
        whatsapp = telefonoWhatsapp.orEmpty(),
        condiciones = condiciones.orEmpty(),
        fechaLimite = fechaLimite.orEmpty(),
        horario = horario.orEmpty()
    )
}

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
// Validación de Formulario
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
            proyecto.opciones.any { it.titulo.isBlank() && it.descripcion.isNotBlank() } ->
                "Cada forma de apoyo necesita un título."
            proyecto.instagram.isNotBlank() && !proyecto.instagram.trim().startsWith("http") ->
                "El Instagram debe ser un enlace completo (https://instagram.com/...)."
            else -> null
        }

        TipoContenidoAdmin.DONACION -> when {
            donacion.fundacion.isBlank() -> "Indica la fundación u organización."
            donacion.tipoDonacionAdmin == TipoDonacionAdmin.ESPECIE && donacion.categoria.isBlank() -> "Elige o crea una categoría."
            imagenes.isEmpty() -> "Agrega una imagen para la donación."
            !telefonoValido(donacion.whatsapp) -> MSG_TELEFONO

            donacion.tipoDonacionAdmin == TipoDonacionAdmin.MONETARIA && donacion.tieneMeta && (donacion.meta.toFloatOrNull() ?: 0f) <= 0f -> "La meta económica debe ser mayor a 0."
            donacion.tipoDonacionAdmin == TipoDonacionAdmin.ESPECIE && donacion.articulosNecesarios.isBlank() -> "Indica los artículos requeridos."
            donacion.tipoDonacionAdmin == TipoDonacionAdmin.ESPECIE && donacion.tieneMeta && (donacion.meta.toFloatOrNull() ?: 0f) <= 0f -> "La meta de artículos debe ser mayor a 0."
            donacion.tipoDonacionAdmin == TipoDonacionAdmin.ESPECIE && comun.ubicacion.isBlank() -> "Indica el centro de acopio o ubicación."
            donacion.tipoDonacionAdmin == TipoDonacionAdmin.TIEMPO && donacion.fechaLimite.isBlank() -> "Indica la fecha del evento o límite."
            donacion.tipoDonacionAdmin == TipoDonacionAdmin.TIEMPO && comun.ubicacion.isBlank() -> "Indica la ubicación donde se requiere el apoyo."
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