package com.example.familiasquesuman.ui.screens.proyectos

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familiasquesuman.data.repository.ProyectoRepository
import com.example.familiasquesuman.domain.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

data class ProyectosUiState(
    val cargando: Boolean = true,
    val proyectos: List<Proyecto> = emptyList(),
    val error: String? = null
)

data class ProyectoDetalleUiState(
    val cargando: Boolean = true,
    val proyecto: Proyecto? = null,
    val error: String? = null
)

class ProyectoViewModel(
    private val repo: ProyectoRepository = ProyectoRepository()
) : ViewModel() {

    private val _lista = MutableStateFlow(ProyectosUiState())
    val lista: StateFlow<ProyectosUiState> = _lista.asStateFlow()

    private val _detalle = MutableStateFlow(ProyectoDetalleUiState())
    val detalle: StateFlow<ProyectoDetalleUiState> = _detalle.asStateFlow()

    /** Se llama cada vez que se abre la pantalla: si ya hay datos, se quedan visibles mientras recarga. */
    fun cargarLista() {
        _lista.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            repo.listar()
                .onSuccess { proyectos -> _lista.value = ProyectosUiState(cargando = false, proyectos = proyectos) }
                .onFailure { e -> _lista.update { it.copy(cargando = false, error = e.message) } }
        }
    }

    fun cargarDetalle(id: String) {
        _detalle.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            repo.detalle(id)
                .onSuccess { p -> _detalle.value = ProyectoDetalleUiState(cargando = false, proyecto = p) }
                .onFailure { e -> _detalle.update { it.copy(cargando = false, error = e.message) } }
        }
    }
}