package com.example.familiasquesuman.ui.screens.comunidad

import androidx.annotation.DrawableRes
import com.example.familiasquesuman.R


enum class TipoPublicacion { OFICIAL, COMUNIDAD }
enum class EstadoPublicacion { EN_REVISION, PUBLICADA, RECHAZADA }


data class ComentarioUi(
    val autor: String,
    val tiempo: String,
    val comentario: String,
    val likes: Int = 0
)


data class PublicacionComunidadUi(
    val id: String,
    val tipo: TipoPublicacion,
    val autor: String,
    val tiempoRelativo: String,
    val titulo: String? = null,
    val contenido: String,
    val categoria: String? = null,

    @DrawableRes
    val imagenRes: Int? = null,
    val numeroLikes: Int = 0,
    val numeroComentarios: Int = 0,
    val fechaEvento: String? = null,
    val horarioEvento: String? = null,
    val ubicacion: String? = null,
    val documentoNombre: String? = null,
    val documentoDetalle: String? = null,
    val estado: EstadoPublicacion? = null,
    val comentarios: List<ComentarioUi> = emptyList()
)


val publicacionesOficialesMock = listOf(

    PublicacionComunidadUi(
        id = "oficial_reforestacion",
        tipo = TipoPublicacion.OFICIAL,
        autor = "Equipo de Familias que Suman",
        tiempoRelativo = "14 de marzo de 2025",
        titulo = "Jornada de Reforestación Comunitaria",
        contenido =
            "Este sábado plantaremos 20 árboles nuevos en el parque del barrio. " +
                    "Te invitamos a participar con toda tu familia. " +
                    "Compartiremos herramientas prácticas para el cuidado del medio ambiente.",
        categoria = "Oficial",
        imagenRes = R.drawable.parque_comunidad,
        fechaEvento = "Sábado, 15 de marzo de 2025",
        horarioEvento = "9:00 a.m. – 12:00 p.m.",
        ubicacion = "Parque Los Encinos",
        documentoNombre = "Guía de la capacitación",
        documentoDetalle = "PDF · 2.4 MB"
    ),

    PublicacionComunidadUi(
        id = "oficial_alimentos",
        tipo = TipoPublicacion.OFICIAL,
        autor = "Equipo de Familias que Suman",
        tiempoRelativo = "Hace 3 horas",
        titulo = "Nueva entrega de alimentos",
        contenido =
            "Informamos las fechas y requisitos para la próxima entrega de alimentos " +
                    "a las familias registradas.",
        categoria = "Aviso",
        imagenRes = R.drawable.parque_comunidad,
        fechaEvento = "Martes, 18 de marzo de 2025",
        horarioEvento = "10:00 a.m. – 2:00 p.m.",
        ubicacion = "Centro Comunitario San Pedro"
    )
)


val publicacionesComunidadMock = listOf(
    PublicacionComunidadUi(
        id = "comunidad_carlos",
        tipo = TipoPublicacion.COMUNIDAD,
        autor = "Carlos M.",
        tiempoRelativo = "Hace 5 horas",
        contenido =
            "Mil gracias a todos los que donaron alimentos hoy. " +
                    "Juntos alimentamos a 50 familias. Fue una jornada increíble, " +
                    "llena de solidaridad y comunidad. Los niños aprendieron sobre el " +
                    "valor de compartir y ver cómo, juntos, podemos hacer una gran diferencia. 🌳💚",
        categoria = "Comedor Solidario",
        imagenRes = R.drawable.parque_comunidad,
        numeroLikes = 24,
        numeroComentarios = 5,
        comentarios = listOf(
            ComentarioUi(
                autor = "Ana R.",
                tiempo = "Hace 4 horas",
                comentario = "¡Qué hermosa iniciativa! 💚",
                likes = 3
            ),
            ComentarioUi(
                autor = "Javier T.",
                tiempo = "Hace 3 horas",
                comentario = "Gracias por organizarlo. ¡Fue un gran día!",
                likes = 2
            )
        )
    ),

    PublicacionComunidadUi(
        id = "comunidad_maria",
        tipo = TipoPublicacion.COMUNIDAD,
        autor = "María L.",
        tiempoRelativo = "Hace 1 día",
        contenido =
            "Participamos en una actividad de reforestación con nuestros hijos. " +
                    "Fue increíble verlos aprender mientras ayudábamos a cuidar nuestro entorno.",
        categoria = "Cuidado del Medio Ambiente",
        imagenRes = R.drawable.parque_comunidad,
        numeroLikes = 18,
        numeroComentarios = 3
    )
)


val misPublicacionesMock = listOf(
    PublicacionComunidadUi(
        id = "m_publicacion_revision",
        tipo = TipoPublicacion.COMUNIDAD,
        autor = "Mariana",
        tiempoRelativo = "Enviado el 12 de septiembre de 2026",
        titulo = "Jornada de limpieza en el Parque Central",
        contenido =
            "Este sábado nos reunimos con más de 30 familias para limpiar el Parque Central.",
        categoria = "Medio Ambiente",
        imagenRes = R.drawable.parque_comunidad,
        estado = EstadoPublicacion.EN_REVISION
    ),

    PublicacionComunidadUi(
        id = "m_publicacion_publicada",
        tipo = TipoPublicacion.COMUNIDAD,
        autor = "Mariana",
        tiempoRelativo = "Publicado el 5 de septiembre de 2026",
        titulo = "Lectura para Niños",
        contenido =
            "Compartimos una tarde increíble leyendo cuentos con niñas y niños de la comunidad.",
        categoria = "Educación",
        imagenRes = R.drawable.parque_comunidad,
        estado = EstadoPublicacion.PUBLICADA,
        numeroLikes = 21,
        numeroComentarios = 4
    ),

    PublicacionComunidadUi(
        id = "m_publicacion_rechazada",
        tipo = TipoPublicacion.COMUNIDAD,
        autor = "Mariana",
        tiempoRelativo = "Enviado el 1 de septiembre de 2026",
        titulo = "Comedor Solidario",
        contenido =
            "Participamos en una jornada de apoyo al comedor comunitario.",
        categoria = "Comedor Solidario",
        imagenRes = R.drawable.parque_comunidad,
        estado = EstadoPublicacion.RECHAZADA
    )
)


fun buscarPublicacion(id: String): PublicacionComunidadUi? {
    return (
            publicacionesOficialesMock +
                    publicacionesComunidadMock +
                    misPublicacionesMock
            )
        .firstOrNull { it.id == id }
}