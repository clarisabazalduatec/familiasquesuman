
package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.familiasquesuman.R
import com.example.familiasquesuman.data.SesionManager
import com.example.familiasquesuman.data.repository.ComunidadRepository
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.model.TipoSesion
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.*

private enum class SeccionComunidad { OFICIAL, COMUNIDAD }

@Composable
fun ComunidadScreen(
    navController: NavHostController,
    esAdministrador: Boolean = false
) {
    var seccionSeleccionada by rememberSaveable {
        mutableStateOf(SeccionComunidad.OFICIAL)
    }

    val sesionIniciada = SesionManager.tipoSesion != TipoSesion.INVITADO

    var mostrarFormularioOficial by rememberSaveable {
        mutableStateOf(false)
    }

    val comunidadRepository = remember { ComunidadRepository() }

    var publicacionesComunidad by remember {
        mutableStateOf<List<PublicacionComunidadUi>>(emptyList())
    }

    var publicacionesOficiales by remember {
        mutableStateOf<List<PublicacionComunidadUi>>(emptyList())
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var errorCarga by remember {
        mutableStateOf<String?>(null)
    }

    // Actualizar las publicaciones al regresar a Comunidad
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(
            Lifecycle.State.RESUMED
        ) {
            cargando = true
            errorCarga = null

            try {
                val resultadoComunidad =
                    comunidadRepository.obtenerPublicaciones()

                resultadoComunidad.onSuccess {
                    publicacionesComunidad = it
                }.onFailure { error ->
                    android.util.Log.e(
                        "COMUNIDAD",
                        "Error al cargar publicaciones",
                        error
                    )
                    errorCarga = "Error Comunidad: ${error.message}"
                }

                val resultadoOficiales =
                    comunidadRepository.obtenerAvisosOficiales()

                resultadoOficiales.onSuccess {
                    publicacionesOficiales = it
                }.onFailure { error ->
                    android.util.Log.e(
                        "COMUNIDAD",
                        "Error al cargar avisos oficiales",
                        error
                    )
                    errorCarga = "Error Avisos: ${error.message}"
                }
            } finally {
                cargando = false
            }
        }
    }

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,
        navbar = false,
        backButton = true,
        onBackClick = { navController.popBackStack() }
    ) { paddingInterno ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CremaFondo)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingInterno)
                    .fillMaxSize()
                    .background(CremaFondo),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 10.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                item {
                    Spacer(Modifier.height(14.dp))

                    SelectorComunidad(
                        seleccionada = seccionSeleccionada,
                        onSeleccionar = {
                            seccionSeleccionada = it
                        }
                    )
                }

                if (seccionSeleccionada == SeccionComunidad.OFICIAL) {

                    items(
                        publicacionesOficiales,
                        key = { it.id }
                    ) { publicacion ->

                        TarjetaPublicacion(
                            publicacion = publicacion,
                            onClick = {
                                navController.navigate(
                                    Rutas.DetallePublicacion.crearRuta(
                                        publicacion.id
                                    )
                                )
                            },
                            onRequiereLogin = {
                                navController.navigate(Rutas.Login.ruta)
                            }
                        )
                    }

                } else {

                    item {
                        AvisoRevision()
                    }

                    item {
                        CajaCrearPublicacion(
                            texto = "¿Qué quieres compartir hoy?",
                            textoSecundario = "Tu publicación se enviará para revisión.",
                            onClick = {
                                if (sesionIniciada) {
                                    navController.navigate(
                                        Rutas.NuevaPublicacion.ruta
                                    )
                                } else {
                                    navController.navigate(
                                        Rutas.Login.ruta
                                    )
                                }
                            }
                        )
                    }

                    item {
                        TextButton(
                            onClick = {
                                navController.navigate(
                                    Rutas.MisPublicaciones.ruta
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Article,
                                contentDescription = null,
                                tint = AzulMarino
                            )

                            Spacer(Modifier.width(8.dp))

                            Text(
                                text = "Ver mis publicaciones",
                                style = MaterialTheme.typography.titleSmall,
                                color = AzulMarino
                            )

                            Spacer(Modifier.weight(1f))

                            Icon(
                                imageVector = Icons.Outlined.ChevronRight,
                                contentDescription = null,
                                tint = AzulMarino
                            )
                        }
                    }

                    items(
                        publicacionesComunidad,
                        key = { it.id }
                    ) { publicacion ->

                        TarjetaPublicacion(
                            publicacion = publicacion,
                            onClick = {
                                navController.navigate(
                                    Rutas.DetallePublicacion.crearRuta(
                                        publicacion.id
                                    )
                                )
                            },
                            onRequiereLogin = {
                                navController.navigate(Rutas.Login.ruta)
                            }
                        )
                    }
                }

                if (cargando) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = AzulMarino,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                if (errorCarga != null) {
                    item {
                        Text(
                            text = errorCarga.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            if (
                SesionManager.esAdmin &&
                seccionSeleccionada == SeccionComunidad.OFICIAL
            ) {
                FloatingActionButton(
                    onClick = {
                        mostrarFormularioOficial = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(paddingInterno)
                        .padding(
                            end = 20.dp,
                            bottom = 20.dp
                        ),
                    containerColor = Ambar,
                    contentColor = AzulMarino,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Crear aviso oficial",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }

    if (mostrarFormularioOficial && SesionManager.esAdmin) {
        CrearAvisoOficialSheet(
            onCerrar = {
                mostrarFormularioOficial = false
            }
        )
    }
}

@Composable
private fun SelectorComunidad(
    seleccionada: SeccionComunidad,
    onSeleccionar: (SeccionComunidad) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Blanco,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
        ) {
            SegmentoSelector(
                texto = "Avisos Oficiales",
                seleccionado = seleccionada == SeccionComunidad.OFICIAL,
                modifier = Modifier.weight(1f),
                onClick = {
                    onSeleccionar(SeccionComunidad.OFICIAL)
                }
            )

            SegmentoSelector(
                texto = "Comunidad",
                seleccionado = seleccionada == SeccionComunidad.COMUNIDAD,
                modifier = Modifier.weight(1f),
                onClick = {
                    onSeleccionar(SeccionComunidad.COMUNIDAD)
                }
            )
        }
    }
}

@Composable
private fun SegmentoSelector(
    texto: String,
    seleccionado: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(46.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = if (seleccionado) {
            AzulMarino
        } else {
            Blanco
        }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = texto,
                style = MaterialTheme.typography.titleSmall,
                color = if (seleccionado) {
                    Blanco
                } else {
                    AzulMarino
                },
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun AvisoRevision() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = AzulMarino.copy(alpha = 0.07f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = AzulMarino
            )

            Spacer(Modifier.width(9.dp))

            Text(
                text = "Las publicaciones se revisan antes de publicarse.",
                style = MaterialTheme.typography.bodySmall,
                color = AzulMarino
            )
        }
    }
}

@Composable
private fun CajaCrearPublicacion(
    texto: String,
    textoSecundario: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.mariana),
                    contentDescription = "Perfil",
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = texto,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )

                Surface(
                    modifier = Modifier.size(42.dp),
                    shape = CircleShape,
                    color = Ambar
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Nueva publicación",
                            tint = AzulMarino
                        )
                    }
                }
            }

            Spacer(Modifier.height(5.dp))

            Text(
                text = textoSecundario,
                modifier = Modifier.padding(start = 54.dp),
                style = MaterialTheme.typography.labelSmall,
                color = GrisTexto
            )
        }
    }
}
