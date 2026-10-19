package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.theme.*
import androidx.compose.runtime.LaunchedEffect
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.ui.model.TipoSesion
import com.example.familiasquesuman.ui.navigation.Rutas
import androidx.compose.ui.platform.LocalContext
import com.example.familiasquesuman.ui.components.SeleccionadorImagenes
import com.example.familiasquesuman.data.repository.ImagenRepository
import com.example.familiasquesuman.data.repository.ComunidadRepository
import kotlinx.coroutines.launch
import com.example.familiasquesuman.data.remote.RetrofitInstance
import com.example.familiasquesuman.data.remote.dto.CategoriaDto
import kotlinx.coroutines.CancellationException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaPublicacionScreen(
    navController: NavHostController,
    esOficial: Boolean = false
) {
    val tipoSesion = SesionManager.tipoSesion
    val sesionIniciada = tipoSesion != TipoSesion.INVITADO
    val puedeCrearPublicacion = if (esOficial) {
        tipoSesion == TipoSesion.ADMIN
    } else {
        tipoSesion != TipoSesion.INVITADO
    }
        if (!sesionIniciada) { LaunchedEffect(Unit) {
            navController.navigate(Rutas.Login.ruta)
            {
                popUpTo(Rutas.NuevaPublicacion.ruta) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
        return
    }

    var titulo by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var ubicacion by rememberSaveable { mutableStateOf("Monterrey, Nuevo León") }
    var categoria by rememberSaveable { mutableStateOf("") }
    var categoriaId by rememberSaveable { mutableStateOf<Int?>(null) }
    var menuCategoriasAbierto by remember { mutableStateOf(false) }
    val categorias = remember { mutableStateListOf<CategoriaDto>() }
    var cargandoCategorias by remember { mutableStateOf(true) }
    var errorCategorias by remember { mutableStateOf<String?>(null) }
    val imagenes = remember { mutableStateListOf<String>() }
    var estaEnviando by remember { mutableStateOf(false) }
    var errorEnvio by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imagenRepository = remember { ImagenRepository() }
    val comunidadRepository = remember { ComunidadRepository() }
    suspend fun subirFotosComunidad(): List<String> {
        return imagenes.map { imagen ->
            imagenRepository.subirImagenComunidad(
                resolver = context.contentResolver,
                url = imagen
            )
        }
    }

    LaunchedEffect(Unit) {
        try {
            val respuesta = RetrofitInstance.api.getCategorias("PUBLICACION")
            categorias.clear()
            categorias.addAll(respuesta)
            errorCategorias = null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            errorCategorias = "No se pudieron cargar las categorías"
        } finally {
            cargandoCategorias = false
        }
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.COMUNIDAD,
    ) { paddingInterno ->
        val scrollState = androidx.compose.foundation.rememberScrollState()
        Column(modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .background(CremaFondo)
                .verticalScroll(scrollState)
                .padding(18.dp)
        ) {
            Surface(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Blanco,
                tonalElevation = 1.dp
            ) {
                Row(modifier = Modifier.padding(
                        horizontal = 6.dp,
                        vertical = 10.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Regresar",
                            tint = AzulMarino
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(text =
                                if (esOficial)
                                    "Nueva publicación oficial"
                                else
                                    "Nueva publicación",
                            style = MaterialTheme.typography.titleLarge,
                            color = AzulMarino
                        )
                        Text(text =
                                if (esOficial)
                                    "Crea un anuncio para las familias."
                                else
                                    "Comparte una experiencia con la comunidad.",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrisTexto
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Título *",
                style = MaterialTheme.typography.titleSmall,
                color = AzulMarino
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Escribe un título breve...") },
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(text = "Descripción *",
                style = MaterialTheme.typography.titleSmall,
                color = AzulMarino
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                placeholder = { Text("Cuéntanos tu experiencia, qué aprendiste o qué fue lo más especial.") },
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Spacer(modifier = Modifier.height(8.dp))
            SeleccionadorImagenes(listaImagenes = imagenes,
                maxImagenes = 5,
                titulo = "Fotografías de tu experiencia",
                subtitulo = "Selecciona hasta 5 fotografías"
            )

            Spacer(modifier = Modifier.height(18.dp))
            com.example.familiasquesuman.ui.components.SelectorCiudad(
                valorSeleccionado = ubicacion,
                onCiudadSeleccionada = { ubicacion = it },
                etiqueta = "Ubicación / Ciudad"
            )

            Spacer(modifier = Modifier.height(18.dp))
            Text(text = "Categoría *",
                style = MaterialTheme.typography.titleSmall,
                color = AzulMarino
            )
            Spacer(modifier = Modifier.height(6.dp))
            ExposedDropdownMenuBox(
                expanded = menuCategoriasAbierto,
                onExpandedChange = {
                    menuCategoriasAbierto =
                        !menuCategoriasAbierto
                }
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    placeholder = { Text("Selecciona una categoría") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Eco,
                            contentDescription = null,
                            tint = AzulMarino
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuCategoriasAbierto) },
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = menuCategoriasAbierto,
                    onDismissRequest = { menuCategoriasAbierto = false }
                ) {
                    categorias.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.nombre) },
                            onClick = { categoria = opcion.nombre
                                categoriaId = opcion.id
                                menuCategoriasAbierto = false
                            }
                        )
                    }
                }
            }
            if (cargandoCategorias) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            errorCategorias?.let { mensaje ->
                Text(text = mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(26.dp))
            Button(
                onClick = {
                    if (esOficial) {
                        errorEnvio = "La creación de avisos oficiales todavía no está conectada."
                    } else {
                        estaEnviando = true
                        errorEnvio = null
                        scope.launch {
                            try {
                                val urlsImagenes = subirFotosComunidad()
                                comunidadRepository.crearPublicacion(
                                    titulo = titulo.trim(),
                                    contenido = descripcion.trim(),
                                    categoriaId = categoriaId,
                                    imagenes = urlsImagenes,
                                    ubicacion = ubicacion
                                ).getOrThrow()
                                navController.navigate(Rutas.ResultadoPublicacion.crearRuta(true))
                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: Exception) {
                                errorEnvio = e.message
                                    ?: "No se pudo enviar la publicación."
                            } finally {
                                estaEnviando = false
                            }
                        }
                    }
                },
                enabled = titulo.isNotBlank() &&
                        descripcion.isNotBlank() &&
                        (esOficial || (categoriaId != null && !cargandoCategorias)) &&
                        !estaEnviando&&
                        puedeCrearPublicacion,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Ambar,
                    contentColor = AzulMarino
                )
            ) {
                if (estaEnviando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = AzulMarino
                    )
                } else {
                    Text(text =
                            if (esOficial)
                                "Publicar anuncio"
                            else
                                "Enviar publicación",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            errorEnvio?.let { mensaje ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}


