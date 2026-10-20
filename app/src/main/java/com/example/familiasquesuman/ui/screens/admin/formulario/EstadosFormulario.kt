package com.example.familiasquesuman.ui.screens.admin.formulario

import com.example.familiasquesuman.domain.Actividad
import com.example.familiasquesuman.domain.CentroVisiteo
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.EstadoProyecto
import com.example.familiasquesuman.domain.Proyecto
import com.example.familiasquesuman.domain.TipoCentro
import com.example.familiasquesuman.domain.TipoDonacion
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// ---------------------------------------------------------------------------
// Estado del formulario: un data class por grupo de campos.
// ---------------------------------------------------------------------------

/** Campos que comparten los 3 tipos. */
data class ComunForm(
    val titulo: String = "",
    val descripcionCorta: String = "",
    val ubicacion: String = "",
    val activa: Boolean = true,
    val ciudadId: Int? = null,
)

data class ActividadForm(
    val categoriaId: Int? = null,
    val categoriaNombre: String = "",
    val fechaMillis: Long? = null,
    val horaInicio: Pair<Int, Int>? = null, // (hora 0..23, minuto 0..59)
    val horaFin: Pair<Int, Int>? = null,
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

/**
- Centro de visiteo. Campos de la tabla centros_visiteo; nombre, descripción corta, dirección y
- "activo" viajan en ComunForm (titulo, descripcionCorta, ubicacion, activa).
- `necesidades` = filas de centro_necesidades; el orden de la lista es la columna `orden`.
 */
data class CentroForm(
    val tipo: TipoCentro = TipoCentro.ASILO,
    val direccion: String = "",
    val informacionGeneral: String = "",
    val necesidades: List<String> = listOf(""),
    val comoAyudar: String = "",
    val recomendaciones: String = "",
    val telefono: String = "",
    val whatsapp: String = "",
    val verificado: Boolean = true,
)

// ---------------------------------------------------------------------------
// Helpers de Fecha y Hora
// ---------------------------------------------------------------------------

fun obtenerDateDesdeFechaYHora(fechaMillis: Long, hora: Pair<Int, Int>): Date {
    val calUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        timeInMillis = fechaMillis
    }
    val year = calUtc.get(Calendar.YEAR)
    val month = calUtc.get(Calendar.MONTH)
    val day = calUtc.get(Calendar.DAY_OF_MONTH)

    val calMty = Calendar.getInstance(TimeZone.getTimeZone("America/Monterrey")).apply {
        set(year, month, day, hora.first, hora.second, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calMty.time
}

fun formarIsoConZona(fechaMillis: Long, hora: Pair<Int, Int>): String {
    val date = obtenerDateDesdeFechaYHora(fechaMillis, hora)
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("America/Monterrey")
    }
    return sdf.format(date)
}

fun formatearFechaDisplay(fechaMillis: Long?): String {
    if (fechaMillis == null) return ""
    val calUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = fechaMillis }
    val localeEsMx = Locale.forLanguageTag("es-MX")
    val sdf = SimpleDateFormat("EEE, d 'de' MMM, yyyy", localeEsMx).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val raw = sdf.format(calUtc.time)
    return raw.replace(".", "").split(" ").joinToString(" ") { palabra ->
        if (palabra.equals("de", ignoreCase = true)) "de"
        else palabra.replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeEsMx) else it.toString() }
    }
}

fun formatearHoraDisplay(hora: Pair<Int, Int>?): String {
    if (hora == null) return ""
    val cal = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hora.first)
        set(Calendar.MINUTE, hora.second)
    }
    val sdf = SimpleDateFormat("hh:mm a", Locale.US)
    return sdf.format(cal.time)
}

// ---------------------------------------------------------------------------
// Modelo -> formulario (para precargar al editar)
// ---------------------------------------------------------------------------

fun Actividad.aFormulario(): Pair<ComunForm, ActividadForm> {
    val calInicio = iniciaEn?.let {
        Calendar.getInstance(TimeZone.getTimeZone("America/Monterrey")).apply { time = it }
    }
    val calFin = terminaEn?.let {
        Calendar.getInstance(TimeZone.getTimeZone("America/Monterrey")).apply { time = it }
    }

    val fechaMillis = calInicio?.run {
        val calUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH))
        }
        calUtc.timeInMillis
    }

    val horaInicioPair = calInicio?.let { it.get(Calendar.HOUR_OF_DAY) to it.get(Calendar.MINUTE) }
    val horaFinPair = calFin?.let { it.get(Calendar.HOUR_OF_DAY) to it.get(Calendar.MINUTE) }

    return ComunForm(
        titulo = titulo,
        descripcionCorta = descripcion,
        ubicacion = ubicacionUrl.orEmpty(),
        activa = activa,
        ciudadId = ciudadId
    ) to ActividadForm(
        categoriaId = categoriaId,
        categoriaNombre = categoria,
        fechaMillis = fechaMillis,
        horaInicio = horaInicioPair,
        horaFin = horaFinPair,
        organizador = organizador,
        lugaresTotales = lugaresTotales.toString(),
        acercaDe = acercaDe
    )
}

fun Proyecto.aFormulario(): Pair<ComunForm, ProyectoForm> =
    ComunForm(nombre, descripcionCorta, ubicacionUrl.orEmpty(), activo, ciudadId) to ProyectoForm(
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
        ubicacion = ubicacionUrl.orEmpty(),
        activa = activa,
        ciudadId = ciudadId
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
    ComunForm(nombre, descripcionCorta, mapaUrl.orEmpty(), activo, ciudadId) to CentroForm(
        tipo = tipo,
        direccion = direccion,
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
    esEdicion: Boolean = false,
): String? {
    if (comun.titulo.isBlank()) return "El título no puede estar vacío."
    if (comun.descripcionCorta.isBlank()) return "Agrega una descripción corta."
    if (comun.ciudadId == null) return "Elige la ciudad."
    if (comun.ubicacion.isNotBlank() && !comun.ubicacion.trim().startsWith("http"))
        return "La ubicación debe ser un link de Google Maps (https://...)."

    return when (tipo) {
        TipoContenidoAdmin.ACTIVIDAD -> {
            val dateInicio = if (actividad.fechaMillis != null && actividad.horaInicio != null) {
                obtenerDateDesdeFechaYHora(actividad.fechaMillis, actividad.horaInicio)
            } else null
            val dateFin = if (actividad.fechaMillis != null && actividad.horaFin != null) {
                obtenerDateDesdeFechaYHora(actividad.fechaMillis, actividad.horaFin)
            } else null

            when {
                comun.ubicacion.isBlank() -> "Pega el link de Google Maps de la ubicación."
                actividad.categoriaId == null && actividad.categoriaNombre.isBlank() -> "Elige o crea una categoría."
                actividad.fechaMillis == null -> "Indica la fecha."
                actividad.horaInicio == null -> "Indica la hora de inicio."
                actividad.horaFin == null -> "Indica la hora de fin."
                dateInicio != null && dateFin != null && !dateFin.after(dateInicio) -> "La hora de fin debe ser posterior a la hora de inicio."
                !esEdicion && dateInicio != null && dateInicio.before(Date()) -> "La fecha y hora de inicio deben ser en el futuro."
                actividad.organizador.isBlank() -> "Indica el organizador."
                (actividad.lugaresTotales.toIntOrNull() ?: 0) <= 0 -> "Los lugares deben ser un número mayor a 0."
                else -> null
            }
        }

        TipoContenidoAdmin.PROYECTO -> when {
            comun.ubicacion.isBlank() -> "Pega el link de Google Maps de la ubicación."
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
            donacion.tipoDonacionAdmin == TipoDonacionAdmin.ESPECIE && comun.ubicacion.isBlank() -> "Pega el link de Google Maps de la ubicación."
            donacion.tipoDonacionAdmin == TipoDonacionAdmin.TIEMPO && donacion.fechaLimite.isBlank() -> "Indica la fecha del evento o límite."
            donacion.tipoDonacionAdmin == TipoDonacionAdmin.TIEMPO && comun.ubicacion.isBlank() -> "Pega el link de Google Maps de la ubicación."
            else -> null
        }

        TipoContenidoAdmin.DIRECTORIO -> when {
            comun.ubicacion.isBlank() -> "Pega el link de Google Maps de la ubicación."
            centro.direccion.isBlank() -> "Indica la dirección del centro."
            centro.informacionGeneral.isBlank() -> "Agrega la información general del centro."
            centro.comoAyudar.isBlank() -> "Explica cómo se puede ayudar al centro."
            !telefonoValido(centro.telefono) || !telefonoValido(centro.whatsapp) -> MSG_TELEFONO
            else -> null
        }
    }
}
