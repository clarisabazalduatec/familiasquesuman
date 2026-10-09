package com.example.familiasquesuman.ui.screens.proyectos

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.familiasquesuman.domain.*

val proyectosMockData = mutableStateListOf(
    Proyecto(
        id = "1",
        nombre = "Trazo... Escribiendo una nueva historia",
        estado = EstadoProyecto.ACTIVO,
        ubicacion = "Monterrey",
        participantes = "25 Mujeres",
        descripcionCorta = "Somos un grupo de mujeres voluntarias que realizamos visitas quincenales al Centro de Reinserción Social de Escobedo para acompañar a mujeres privadas de la libertad mediante pláticas d...",
        acercaDe = "Voluntariado que visita a mujeres en el penal de Escobedo.",
        descripcionLarga = "Trazo es un voluntariado que nace de la Asociación Renace, desde hace más de 2 años lleva pláticas de desarrollo humano y cursos de acuarela y caligrafía a mujeres privadas de la libertad quienes al final del semestre reciben un reconocimiento el cual les ayuda en su expediente judicial.\n\nSomos un grupo de mujeres voluntarias que realizamos visitas quincenales al Centro de Reinserción Social de Escobedo para acompañar a mujeres privadas de la libertad mediante pláticas de desarrollo humano, talleres de acuarela y actividades que fortalecen su autoestima, creatividad y crecimiento personal.\n\nEn cada encuentro buscamos ofrecer un espacio de escucha, aprendizaje y esperanza, recordándoles que siempre es posible comenzar de nuevo.",
        opcionesApoyo = listOf(
            OpcionApoyo(
                tipo = TipoApoyo.TIEMPO_TALENTO,
                titulo = "Buscamos personas que quieran compartir su tiempo y talento.",
                descripcion = "Voluntarias que imparten pláticas de desarrollo humano. • Talleres de acuarela y actividades creativas. • Otros talleres que promuevan el aprendizaje y el bienestar emocional."
            ),
            OpcionApoyo(
                tipo = TipoApoyo.APORTACION_ECONOMICA,
                titulo = "Aportación económica",
                descripcion = "Donativos para comprar materiales: • Pinturas, pinceles, papel y otros insumos. • Aportaciones económicas para los alimentos que se entregan a las mujeres privadas de la libertad durante cada visita."
            )
        ),
        telefonoWhatsapp = "5218110771068",
        telefonoLlamada = "8110771068"
    ),
    Proyecto(
        id = "2",
        nombre = "Voluntariado DIF Te Acompaña",
        estado = EstadoProyecto.ACTIVO,
        ubicacion = "San Pedro Garza García",
        participantes = "200 adultos mayores",
        descripcionCorta = "Acompañar a adultos mayores o jóvenes vulnerables, en soledad, con discapacidad o con una red de apoyo reducida. Realizar visitas en familia mínimo una vez cada 15 días. Se les puede...",
        acercaDe = "Acompañamiento a adultos mayores en situación de vulnerabilidad.",
        descripcionLarga = "Acompañar a adultos mayores o jóvenes vulnerables, en soledad, con discapacidad o con una red de apoyo reducida.",
        opcionesApoyo = emptyList(),
    ),
)

fun agregarProyectoMock(proyecto: Proyecto) {
    proyectosMockData.add(0, proyecto)
}

fun eliminarProyectoMock(id: String) {
    proyectosMockData.removeAll { it.id == id }
}

class ProyectoViewModel : ViewModel() {

    fun obtenerProyectosPorEstado(estado: EstadoProyecto): List<Proyecto> =
        if (estado == EstadoProyecto.ACTIVO) {
            proyectosMockData.filter { it.estado == EstadoProyecto.ACTIVO && it.activo }
        } else {
            proyectosMockData.filter { it.estado == EstadoProyecto.ANTERIOR || !it.activo }
        }

    fun obtenerProyectoPorId(id: String): Proyecto? =
        proyectosMockData.find { it.id == id }
}
