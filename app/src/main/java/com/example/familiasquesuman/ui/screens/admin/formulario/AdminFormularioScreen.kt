package com.example.familiasquesuman.ui.screens.admin.formulario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.familiasquesuman.domain.EstadoProyecto
import com.example.familiasquesuman.data.CategoriasMock
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.data.agregarCategoria
import com.example.familiasquesuman.data.repository.donacionesMockData
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.components.SeleccionadorImagenes
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.ColorError
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.GrisTexto
import com.example.familiasquesuman.ui.theme.VerdeExito

/**
 * Pantalla ÚNICA para crear y editar.
 *  - id == null  -> crear (muestra el selector de tipo)
 *  - id != null  -> editar ese elemento (el tipo queda fijo y aparece "Eliminar")
 */
@Composable
fun AdminFormularioScreen(
    navController: NavHostController,
    tipoInicial: TipoContenidoAdmin = TipoContenidoAdmin.ACTIVIDAD,
    id: String? = null,
) {
    val esEdicion = id != null
    var tipo by remember(tipoInicial) { mutableStateOf(tipoInicial) }
    val context = LocalContext.current
    var mostrarExitoBackend by remember { mutableStateOf(false) }

    // Proyectos: se leen y guardan en el backend (los demás tipos siguen con mocks por ahora).
    val vmProyecto: AdminProyectoViewModel = viewModel()
    val estadoProyecto by vmProyecto.uiState.collectAsState()
    val esEdicionProyecto = esEdicion && tipoInicial == TipoContenidoAdmin.PROYECTO
    LaunchedEffect(id) { if (esEdicionProyecto && id != null) vmProyecto.cargar(id) }
    LaunchedEffect(estadoProyecto.guardado) { if (estadoProyecto.guardado != null) mostrarExitoBackend = true }

    // Directorio: igual que proyectos, contra el backend.
    val vmCentro: AdminCentroViewModel = viewModel()
    val estadoCentro by vmCentro.uiState.collectAsState()
    val esEdicionCentro = esEdicion && tipoInicial == TipoContenidoAdmin.DIRECTORIO
    LaunchedEffect(id) { if (esEdicionCentro && id != null) vmCentro.cargar(id) }
    LaunchedEffect(estadoCentro.guardado) { if (estadoCentro.guardado != null) mostrarExitoBackend = true }
    // Elemento original (solo al editar). Se busca una vez por id.
    val actividadOriginal = remember(id) {
        if (esEdicion && tipoInicial == TipoContenidoAdmin.ACTIVIDAD) actividadesMockData.find { it.id == id?.toIntOrNull() } else null
    }
    val proyectoOriginal = if (esEdicionProyecto) estadoProyecto.proyecto else null
    val donacionOriginal = remember(id) {
        if (esEdicion && tipoInicial == TipoContenidoAdmin.DONACION) donacionesMockData.find { it.id == id } else null
    }
    val centroOriginal = if (esEdicionCentro) estadoCentro.centro else null

    // Estado del formulario.
    var comun by remember(id, proyectoOriginal,centroOriginal) {
        mutableStateOf(
            actividadOriginal?.aFormulario()?.first
                ?: proyectoOriginal?.aFormulario()?.first
                ?: donacionOriginal?.aFormulario()?.first
                ?: centroOriginal?.aFormulario()?.first
                ?: ComunForm(),
        )
    }
    var actividad by remember(id) { mutableStateOf(actividadOriginal?.aFormulario()?.second ?: ActividadForm()) }
    var proyecto by remember(id, proyectoOriginal) { mutableStateOf(proyectoOriginal?.aFormulario()?.second ?: ProyectoForm()) }
    var donacion by remember(id) { mutableStateOf(donacionOriginal?.aFormulario()?.second ?: DonacionForm()) }
    var centro by remember(id,centroOriginal) { mutableStateOf(centroOriginal?.aFormulario()?.second ?: CentroForm()) }

    // Lista de imágenes.
    val listaImagenes = remember(tipo, id, proyectoOriginal,centroOriginal) {
        mutableStateListOf<String>().apply {
            actividadOriginal?.let { addAll(it.imagenesUrl) }
            proyectoOriginal?.let { p -> if (p.imagenesUrl.isNotEmpty()) addAll(p.imagenesUrl) else p.logoUrl?.let { add(it) } }
            donacionOriginal?.let { d -> if (d.imagenesUrl.isNotEmpty()) addAll(d.imagenesUrl) else add(d.imagenUrl) }
            centroOriginal?.logoUrl?.let { add(it) }
        }
    }

    var errorMensaje by remember { mutableStateOf<String?>(null) }
    var mostrarExito by remember { mutableStateOf(false) }
    var mostrarEliminar by remember { mutableStateOf(false) }

    // Editando algo del backend que todavía no llega: cargando o error.
    if ((esEdicionProyecto && proyectoOriginal == null) || (esEdicionCentro && centroOriginal == null)) {
        Box(modifier = Modifier.fillMaxSize().background(CremaFondo), contentAlignment = Alignment.Center) {
            val error = if (esEdicionProyecto) estadoProyecto.errorCarga else estadoCentro.errorCarga
            if (error != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(error, color = GrisTexto)
                    TextButton(onClick = {
                        id?.let { if (esEdicionProyecto) vmProyecto.cargar(it) else vmCentro.cargar(it) }
                    }) { Text("Reintentar", color = AzulMarino) }
                }
            } else {
                CircularProgressIndicator(color = Ambar)
            }
        }
        return
    }

    if (esEdicion && actividadOriginal == null && proyectoOriginal == null && donacionOriginal == null && centroOriginal == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No se encontró la iniciativa a editar.", color = GrisTexto)
        }
        return
    }

    fun guardar() {
        errorMensaje = validarFormulario(tipo, comun, actividad, proyecto, donacion, centro, listaImagenes)
        if (errorMensaje != null) return

        if (tipo == TipoContenidoAdmin.PROYECTO) {
            // Async: el diálogo de éxito se abre cuando el backend confirma (estadoProyecto.guardado).
            vmProyecto.guardar(id, comun, proyecto, listaImagenes.toList(), context.contentResolver)
            return
        }
        if (tipo == TipoContenidoAdmin.DIRECTORIO) {
            vmCentro.guardar(id, comun, centro, listaImagenes.toList(), context.contentResolver)
            return
        }

        when (tipo) {
            TipoContenidoAdmin.ACTIVIDAD -> guardarActividad(actividadOriginal, comun, actividad, listaImagenes.toList())
            TipoContenidoAdmin.PROYECTO -> Unit
            TipoContenidoAdmin.DONACION -> guardarDonacion(donacionOriginal, comun, donacion, listaImagenes.toList())
            TipoContenidoAdmin.DIRECTORIO -> Unit
        }
        mostrarExito = true
    }

    LaunchedEffect(estadoProyecto.error) {
        estadoProyecto.error?.let {
            errorMensaje = it
            vmProyecto.errorMostrado()
        }
    }
    LaunchedEffect(estadoCentro.error) {
        estadoCentro.error?.let {
            errorMensaje = it
            vmCentro.errorMostrado()
        }
    }
    val guardandoBackend = (tipo == TipoContenidoAdmin.PROYECTO && estadoProyecto.guardando) ||
            (tipo == TipoContenidoAdmin.DIRECTORIO && estadoCentro.guardando)

    val maxFotos = if (tipo == TipoContenidoAdmin.ACTIVIDAD) 5 else 1
    val tituloFotos = when (tipo) {
        TipoContenidoAdmin.ACTIVIDAD -> "Fotos de la Actividad"
        TipoContenidoAdmin.PROYECTO -> "Logo / Foto del Proyecto"
        TipoContenidoAdmin.DONACION -> "Foto de la Donación"
        TipoContenidoAdmin.DIRECTORIO -> "Logo del Centro"
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        chatbot = false,
        backButton = true,
        onBackClick = { navController.navigate(Rutas.Admin.ruta) },
        titulo = if (esEdicion) "Editar ${tipo.titulo}" else "Crear nuevo contenido",
    ) { paddingVal ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVal)
                .background(CremaFondo)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            Text(
                text = if (esEdicion) "Editar ${tipo.titulo}" else "Publicar Tarjeta",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AzulMarino,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (esEdicion) "Modifica la información o activa/desactiva la publicación."
                else "Selecciona el tipo de contenido que deseas agregar a la plataforma.",
                fontSize = 13.sp,
                color = GrisTexto,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!esEdicion) {
                SelectorTipoContenido(tipo = tipo, onSeleccion = { tipo = it; errorMensaje = null })
                Spacer(modifier = Modifier.height(20.dp))
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {

                    // En proyectos el activo/desactivado es el "Estado del Proyecto" (ver CamposProyecto).
                    if (tipo != TipoContenidoAdmin.PROYECTO) {
                        InterruptorActiva(activa = comun.activa, onCambio = { comun = comun.copy(activa = it) })
                    }

                    CamposComunes(
                        tipo = tipo,
                        form = comun,
                        onCambio = { comun = it },
                        ayudaUbicacion = if (tipo == TipoContenidoAdmin.DONACION && donacion.tipoDonacionAdmin != TipoDonacionAdmin.MONETARIA)
                            "Obligatoria en donaciones en especie y voluntariado." else null,
                    )

                    SeleccionadorImagenes(
                        listaImagenes = listaImagenes,
                        maxImagenes = maxFotos,
                        titulo = tituloFotos,
                    )

                    when (tipo) {
                        TipoContenidoAdmin.ACTIVIDAD -> CamposActividad(
                            form = actividad,
                            categorias = CategoriasMock.actividades,
                            onNuevaCategoria = { nombre ->
                                agregarCategoria(CategoriasMock.actividades, nombre)?.let { actividad = actividad.copy(categoria = it) }
                            },
                            onCambio = { actividad = it },
                        )

                        TipoContenidoAdmin.PROYECTO -> CamposProyecto(form = proyecto, onCambio = { proyecto = it })

                        TipoContenidoAdmin.DONACION -> CamposDonacion(
                            form = donacion,
                            esEdicion = esEdicion,
                            categorias = CategoriasMock.donaciones,
                            onNuevaCategoria = { nombre ->
                                agregarCategoria(CategoriasMock.donaciones, nombre)?.let { donacion = donacion.copy(categoria = it) }
                            },
                            onCambio = { donacion = it },
                        )

                        TipoContenidoAdmin.DIRECTORIO -> CamposCentro(form = centro, onCambio = { centro = it })
                    }

                    errorMensaje?.let {
                        Text(text = it, color = ColorError, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { guardar() },
                        enabled = !guardandoBackend,
                        colors = ButtonDefaults.buttonColors(containerColor = Ambar, contentColor = AzulMarino),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                    ) {
                        if (guardandoBackend) {
                            CircularProgressIndicator(color = AzulMarino, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Guardando...", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        } else {
                            Icon(
                                imageVector = if (esEdicion) Icons.Default.Save else Icons.Default.AddCircle,
                                contentDescription = null,
                                tint = AzulMarino,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (esEdicion) "Guardar Cambios" else "Publicar ${tipo.titulo}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                            )
                        }
                    }

                    if (esEdicion) {
                        OutlinedButton(
                            onClick = { mostrarEliminar = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorError),
                            border = BorderStroke(1.dp, ColorError.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Eliminar ${tipo.titulo} definitivamente", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }

    if (mostrarExito || mostrarExitoBackend) {
        val textoExito = when {
            tipo == TipoContenidoAdmin.PROYECTO && proyecto.estado == EstadoProyecto.ACTIVO ->
                "El proyecto se guardó y aparece en \"Proyectos activos\"."
            tipo == TipoContenidoAdmin.PROYECTO ->
                "El proyecto se guardó como desactivado y aparece en \"Proyectos anteriores\"."
            esEdicion && comun.activa -> "La tarjeta ha sido actualizada y está activa en la aplicación."
            esEdicion -> "La tarjeta ha sido desactivada y se ha ocultado para los usuarios."
            comun.activa -> "La tarjeta de ${tipo.titulo.lowercase()} ha sido agregada y ya está visible para los usuarios."
            else -> "La tarjeta se guardó como desactivada: los usuarios no la verán hasta que la actives."
        }
        AlertDialog(
            onDismissRequest = { mostrarExito = false; mostrarExitoBackend = false; navController.popBackStack() },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VerdeExito, modifier = Modifier.size(48.dp)) },
            title = {
                Text(
                    text = if (esEdicion) "¡Cambios Guardados!" else "¡Publicación Exitosa!",
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino,
                )
            },
            text = { Text(text = textoExito, color = GrisTexto) },
            confirmButton = {
                Button(
                    onClick = { mostrarExito = false; mostrarExitoBackend = false; navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulMarino, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                ) { Text("Aceptar", fontWeight = FontWeight.Bold) }
            },
        )
    }

    if (mostrarEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarEliminar = false },
            icon = { Icon(Icons.Default.Delete, contentDescription = null, tint = ColorError, modifier = Modifier.size(40.dp)) },
            title = { Text("Eliminar ${tipo.titulo}", fontWeight = FontWeight.Bold, color = AzulMarino, fontSize = 18.sp) },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar este ${tipo.titulo.lowercase()} definitivamente? " +
                            "Esta acción borrará la publicación por completo y no se podrá deshacer.",
                    color = GrisTexto,
                    fontSize = 13.sp,
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarEliminar = false
                        if (tipo == TipoContenidoAdmin.PROYECTO) {
                            id?.let { vmProyecto.borrar(it) { navController.popBackStack() } }
                        } else if (tipo == TipoContenidoAdmin.DIRECTORIO) {
                            id?.let { vmCentro.borrar(it) { navController.popBackStack() } }
                        } else {
                            id?.let { eliminarIniciativa(tipo, it) }
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorError, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                ) { Text("Eliminar Definitivamente", fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { mostrarEliminar = false }) { Text("Cancelar", color = GrisTexto) } },
        )
    }
}