package com.example.familiasquesuman.ui.screens.admin.formulario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.familiasquesuman.data.CategoriasMock
import com.example.familiasquesuman.data.actividadesMockData
import com.example.familiasquesuman.data.agregarCategoria
import com.example.familiasquesuman.domain.EstadoProyecto
import com.example.familiasquesuman.domain.TipoDonacion
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.components.SeleccionadorImagenes
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.screens.directorio.centrosDeEjemplo
import com.example.familiasquesuman.ui.screens.donar.DonacionViewModel
import com.example.familiasquesuman.ui.theme.*

/**
- Pantalla ÚNICA para crear y editar.

-  - id == null  -> crear (muestra el selector de tipo)

-  - id != null  -> editar ese elemento (el tipo queda fijo y aparece "Eliminar")

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
    var mostrarExitoProyecto by remember { mutableStateOf(false) }

    // ViewModels
    val vmProyecto: AdminProyectoViewModel = viewModel()
    val estadoProyecto by vmProyecto.uiState.collectAsState()
    val esEdicionProyecto = esEdicion && tipo == TipoContenidoAdmin.PROYECTO

    val vmDonacion: DonacionViewModel = viewModel()
    val esEdicionDonacion = esEdicion && tipo == TipoContenidoAdmin.DONACION

    // ESTADO AGREGADO: Observamos si la donación está cargando/guardando
    val isLoadingDonacion by vmDonacion.isLoading.collectAsState()

    // Carga de backend al entrar en modo edición
    LaunchedEffect(id, tipo) {
        if (esEdicionProyecto) vmProyecto.cargar(id)
        if (esEdicionDonacion) vmDonacion.cargarDetalleDonacion(id)
    }

    val donacionDetalleCompleto by vmDonacion.donacionSeleccionada.collectAsState()

    val actividadOriginal = remember(id) {
        if (esEdicion && tipo == TipoContenidoAdmin.ACTIVIDAD) actividadesMockData.find { it.id == id.toIntOrNull() } else null
    }
    val proyectoOriginal = if (esEdicionProyecto) estadoProyecto.proyecto else null
    val donacionOriginal = if (esEdicionDonacion) donacionDetalleCompleto else null
    val centroOriginal = remember(id) {
        if (esEdicion && tipo == TipoContenidoAdmin.DIRECTORIO) centrosDeEjemplo.find { it.id == id } else null
    }

    // Estados del formulario
    var comun by remember { mutableStateOf(ComunForm()) }
    var actividad by remember { mutableStateOf(ActividadForm()) }
    var proyecto by remember { mutableStateOf(ProyectoForm()) }
    var donacion by remember { mutableStateOf(DonacionForm()) }
    var centro by remember { mutableStateOf(CentroForm()) }
    val listaImagenes = remember { mutableStateListOf<String>() }

    // Sincronización segura al recibir los datos de la donación
    LaunchedEffect(donacionOriginal) {
        donacionOriginal?.let { d ->
            try {
                val (c, f) = d.aFormulario()
                comun = c
                donacion = f
                listaImagenes.clear()
                if (d.imagenesUrl.isNotEmpty()) {
                    listaImagenes.addAll(d.imagenesUrl)
                } else if (d.imagenUrl.isNotEmpty()) {
                    listaImagenes.add(d.imagenUrl)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    var errorMensaje by remember { mutableStateOf<String?>(null) }
    var mostrarExito by remember { mutableStateOf(false) }
    var mostrarEliminar by remember { mutableStateOf(false) }

    // Pantalla de carga mientras se obtienen los datos completos del servidor
    val cargando = (esEdicionProyecto && proyectoOriginal == null) || (esEdicionDonacion && donacionOriginal == null)
    if (cargando) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CremaFondo),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Ambar)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Cargando donación...", color = GrisTexto, fontSize = 14.sp)
            }
        }
        return
    }

    fun guardar() {
        errorMensaje = validarFormulario(tipo, comun, actividad, proyecto, donacion, centro, listaImagenes)
        if (errorMensaje != null) return

        if (tipo == TipoContenidoAdmin.PROYECTO) {
            vmProyecto.guardar(id, comun, proyecto, listaImagenes.toList(), context.contentResolver)
            return
        }

        if (tipo == TipoContenidoAdmin.DONACION) {
            val dto = donacion.toGuardarDto(comun, listaImagenes.toList())
            vmDonacion.guardarDonacion(
                id = id, // Se envía el ID si es edición
                contentResolver = context.contentResolver,
                datos = dto,
                onSuccess = { mostrarExito = true },
                onError = { error -> errorMensaje = error }
            )
            return
        }

        when (tipo) {
            TipoContenidoAdmin.ACTIVIDAD -> guardarActividad(actividadOriginal, comun, actividad, listaImagenes.toList())
            TipoContenidoAdmin.DIRECTORIO -> guardarCentro(centroOriginal, comun, centro, listaImagenes.toList())
            else -> Unit
        }
        mostrarExito = true
    }

    LaunchedEffect(estadoProyecto.error) {
        estadoProyecto.error?.let {
            errorMensaje = it
            vmProyecto.errorMostrado()
        }
    }

    val guardandoProyecto = tipo == TipoContenidoAdmin.PROYECTO && estadoProyecto.guardando

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

                    if (tipo != TipoContenidoAdmin.PROYECTO) {
                        InterruptorActiva(activa = comun.activa, onCambio = { comun = comun.copy(activa = it) })
                    }

                    CamposComunes(
                        tipo = tipo,
                        form = comun,
                        onCambio = { comun = it },
                        ayudaUbicacion = if (tipo == TipoContenidoAdmin.DONACION && donacion.tipo != TipoDonacion.CAMPANA)
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

                    // LÓGICA DE BLOQUEO ACTUALIZADA
                    val estaGuardando = guardandoProyecto || (tipo == TipoContenidoAdmin.DONACION && isLoadingDonacion)

                    Button(
                        onClick = { guardar() },
                        enabled = !estaGuardando, // Bloquea si está guardando donación o proyecto
                        colors = ButtonDefaults.buttonColors(containerColor = Ambar, contentColor = AzulMarino),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                    ) {
                        if (estaGuardando) {
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

    if (mostrarExito || mostrarExitoProyecto) {
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
            onDismissRequest = { mostrarExito = false; mostrarExitoProyecto = false; navController.popBackStack() },
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
                    onClick = { mostrarExito = false; mostrarExitoProyecto = false; navController.popBackStack() },
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
                        when (tipo) {
                            TipoContenidoAdmin.PROYECTO -> {
                                id?.let { vmProyecto.borrar(it) { navController.popBackStack() } }
                            }
                            TipoContenidoAdmin.DONACION -> {
                                id?.let {
                                    vmDonacion.eliminarDonacion(
                                        id = it,
                                        onSuccess = { navController.popBackStack() },
                                        onError = { err -> errorMensaje = err }
                                    )
                                }
                            }
                            else -> {
                                id?.let { eliminarIniciativa(tipo, it) }
                                navController.popBackStack()
                            }
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