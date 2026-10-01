package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.R
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaPublicacionScreen(
    navController: NavHostController,
    esOficial: Boolean = false
) {

    var titulo by rememberSaveable {
        mutableStateOf("")
    }

    var descripcion by rememberSaveable {
        mutableStateOf("")
    }

    var ubicacion by rememberSaveable {
        mutableStateOf("")
    }

    var categoria by rememberSaveable {
        mutableStateOf("")
    }

    var menuCategoriasAbierto by remember {
        mutableStateOf(false)
    }

    var tieneFotos by rememberSaveable {
        mutableStateOf(false)
    }

    var estaEnviando by remember {
        mutableStateOf(false)
    }

    val categorias = listOf(
        "Medio Ambiente",
        "Comedor Solidario",
        "Educación",
        "Voluntariado",
        "Salud"
    )

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,

        chatbot = false
    ) { paddingInterno ->

        val scrollState =
            androidx.compose.foundation.rememberScrollState()

        Column(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .background(CremaFondo)
                .verticalScroll(scrollState)
                .padding(18.dp)
        ) {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Blanco,
                tonalElevation = 1.dp
            ) {

                Row(
                    modifier = Modifier.padding(
                        horizontal = 6.dp,
                        vertical = 10.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Regresar",
                            tint = AzulMarino
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    Column {

                        Text(
                            text =
                                if (esOficial)
                                    "Nueva publicación oficial"
                                else
                                    "Nueva publicación",
                            style = MaterialTheme.typography.titleLarge,
                            color = AzulMarino
                        )

                        Text(
                            text =
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

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Título *",
                style = MaterialTheme.typography.titleSmall,
                color = AzulMarino
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Escribe un título breve...")
                },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Descripción *",
                style = MaterialTheme.typography.titleSmall,
                color = AzulMarino
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                placeholder = {
                    Text(
                        "Cuéntanos tu experiencia, qué aprendiste o qué fue lo más especial."
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Fotos (opcional)",
                style = MaterialTheme.typography.titleSmall,
                color = AzulMarino
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                if (tieneFotos) {

                    FotoNuevaPublicacion()
                    FotoNuevaPublicacion()
                }

                OutlinedCard(
                    modifier = Modifier
                        .size(
                            width = 95.dp,
                            height = 110.dp
                        )
                        .clickable {
                            tieneFotos = true
                        },
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment =
                            Alignment.CenterHorizontally,
                        verticalArrangement =
                            Arrangement.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Outlined.AddPhotoAlternate,
                            contentDescription = null,
                            tint = AzulMarino
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "Agregar\nfotos",
                            style =
                                MaterialTheme.typography.bodySmall,
                            color = GrisTexto
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Categoría *",
                style =
                    MaterialTheme.typography.titleSmall,
                color = AzulMarino
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

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

                    placeholder = {
                        Text(
                            "Selecciona una categoría"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector = Icons.Outlined.Eco,
                            contentDescription = null,
                            tint = AzulMarino
                        )
                    },

                    trailingIcon = {

                        ExposedDropdownMenuDefaults
                            .TrailingIcon(
                                expanded =
                                    menuCategoriasAbierto
                            )
                    },

                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = menuCategoriasAbierto,

                    onDismissRequest = {
                        menuCategoriasAbierto = false
                    }
                ) {

                    categorias.forEach { opcion ->

                        DropdownMenuItem(
                            text = {
                                Text(opcion)
                            },

                            onClick = {

                                categoria = opcion
                                menuCategoriasAbierto = false
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Ubicación (opcional)",
                style =
                    MaterialTheme.typography.titleSmall,
                color = AzulMarino
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            OutlinedTextField(
                value = ubicacion,

                onValueChange = {
                    ubicacion = it
                },

                modifier = Modifier.fillMaxWidth(),

                placeholder = {
                    Text(
                        "Ej. Parque Central, Monterrey"
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Outlined.LocationOn,
                        contentDescription = null
                    )
                },

                shape = RoundedCornerShape(12.dp)
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Button(
                onClick = {
                    if (esOficial) {
                        navController.navigate(Rutas.Comunidad.ruta)
                        {
                            popUpTo(Rutas.Comunidad.ruta)
                        }

                    } else {
                        estaEnviando = true
                    }
                },

                enabled =
                    titulo.isNotBlank() &&
                            descripcion.isNotBlank() &&
                            categoria.isNotBlank() &&
                            !estaEnviando,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),

                shape = RoundedCornerShape(16.dp),

                colors =
                    ButtonDefaults.buttonColors(
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

                    Text(
                        text =
                            if (esOficial)
                                "Publicar anuncio"
                            else
                                "Enviar publicación",

                        style =
                            MaterialTheme.typography.titleSmall
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(30.dp)
            )
        }
    }

    LaunchedEffect(estaEnviando) {
        if (
            estaEnviando &&
            !esOficial
        ) {
            delay(800)
            navController.navigate(
                Rutas.ResultadoPublicacion
                    .crearRuta(true)
            )
        }
    }
}

@Composable
private fun FotoNuevaPublicacion() {

    Image(
        painter = painterResource(
            R.drawable.parque_comunidad
        ),
        contentDescription = null,
        modifier = Modifier
            .size(
                width = 95.dp,
                height = 110.dp
            )
            .clip(
                RoundedCornerShape(12.dp)
            ),
        contentScale = ContentScale.Crop
    )
}