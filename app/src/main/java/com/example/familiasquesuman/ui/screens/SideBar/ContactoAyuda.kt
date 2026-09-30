package com.example.familiasquesuman.ui.screens.informacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Sms
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.R
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.GrisTexto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoAyudaScreen(
    navController: NavHostController
) {

    var nombre by remember {
        mutableStateOf("")
    }

    var correo by remember {
        mutableStateOf("")
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    val snackbarHostState =
        remember {
            SnackbarHostState()
        }

    val scope =
        rememberCoroutineScope()

    val scrollState =
        rememberScrollState()

    Scaffold(
        containerColor = CremaFondo,

        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },

        topBar = {

            CenterAlignedTopAppBar(
                colors =
                    TopAppBarDefaults
                        .centerAlignedTopAppBarColors(
                            containerColor = CremaFondo,
                            navigationIconContentColor = AzulMarino,
                            actionIconContentColor = AzulMarino
                        ),

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored
                                    .Outlined
                                    .ArrowBack,
                            contentDescription =
                                "Regresar",
                            tint = AzulMarino
                        )
                    }
                },

                title = {

                    androidx.compose.foundation.Image(
                        painter =
                            painterResource(
                                id = R.drawable.logo2_onb
                            ),
                        contentDescription =
                            "Familias que Suman",
                        modifier =
                            Modifier
                                .width(220.dp)
                                .height(54.dp),
                        contentScale =
                            ContentScale.Fit
                    )
                },

                actions = {

                    IconButton(
                        onClick = {
                            navController.navigate(
                                Rutas.Notificaciones.ruta
                            )
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Notifications,
                            contentDescription =
                                "Notificaciones",
                            tint = AzulMarino
                        )
                    }
                }
            )
        }
    ) { paddingInterno ->

        Column(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .background(CremaFondo)
                .verticalScroll(scrollState)
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 18.dp,
                    bottom = 28.dp
                )
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text = "Contacto y ayuda",
                        style =
                            MaterialTheme.typography
                                .headlineMedium,
                        color = AzulMarino
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "¿Tienes dudas o sugerencias? Escríbenos.",
                        style =
                            MaterialTheme.typography
                                .titleSmall,
                        color = AzulMarino
                    )
                }

                Icon(
                    imageVector =
                        Icons.Outlined.Send,
                    contentDescription = null,
                    tint = Ambar,
                    modifier = Modifier
                        .size(58.dp)
                        .rotate(-20f)
                )
            }

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text =
                    "Nos encantaría leerte. Puedes escribirnos si tienes dudas sobre la plataforma, quieres compartir una sugerencia o necesitas más información.",
                style =
                    MaterialTheme.typography.bodyMedium,
                color = GrisTexto
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            // NOMBRE
            EtiquetaFormulario(
                texto = "Nombre"
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                },
                modifier =
                    Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text =
                            "Escribe tu nombre",
                        style =
                            MaterialTheme.typography
                                .bodyMedium
                    )
                },
                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Outlined.Person,
                        contentDescription = null,
                        tint = AzulMarino
                    )
                },
                singleLine = true,
                shape =
                    RoundedCornerShape(14.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor =
                            AzulMarino,
                        unfocusedBorderColor =
                            AzulMarino.copy(
                                alpha = .20f
                            ),
                        focusedContainerColor =
                            Color.Transparent,
                        unfocusedContainerColor =
                            Color.Transparent
                    )
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // CORREO
            EtiquetaFormulario(
                texto = "Correo electrónico"
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                },
                modifier =
                    Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text =
                            "tu.correo@ejemplo.com"
                    )
                },
                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Outlined.Email,
                        contentDescription = null,
                        tint = AzulMarino
                    )
                },
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Email
                    ),
                shape =
                    RoundedCornerShape(14.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor =
                            AzulMarino,
                        unfocusedBorderColor =
                            AzulMarino.copy(
                                alpha = .20f
                            )
                    )
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // MENSAJE
            EtiquetaFormulario(
                texto = "Mensaje"
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            OutlinedTextField(
                value = mensaje,
                onValueChange = {
                    mensaje = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp),
                placeholder = {
                    Text(
                        text =
                            "Cuéntanos en qué podemos ayudarte..."
                    )
                },
                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Outlined.Sms,
                        contentDescription = null,
                        tint = AzulMarino
                    )
                },
                shape =
                    RoundedCornerShape(14.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor =
                            AzulMarino,
                        unfocusedBorderColor =
                            AzulMarino.copy(
                                alpha = .20f
                            )
                    )
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Button(
                onClick = {

                    scope.launch {

                        snackbarHostState
                            .showSnackbar(
                                message =
                                    "Tu mensaje fue enviado."
                            )
                    }

                    nombre = ""
                    correo = ""
                    mensaje = ""
                },

                enabled =
                    nombre.isNotBlank() &&
                            correo.isNotBlank() &&
                            mensaje.isNotBlank(),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            AzulMarino,
                        contentColor =
                            Blanco
                    )
            ) {

                Text(
                    text =
                        "Enviar mensaje",
                    style =
                        MaterialTheme.typography
                            .titleSmall
                )

                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )

                Icon(
                    imageVector =
                        Icons.Outlined
                            .ChevronRight,
                    contentDescription = null
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.Top
            ) {

                Icon(
                    imageVector =
                        Icons.Outlined.Info,
                    contentDescription = null,
                    tint = GrisTexto,
                    modifier =
                        Modifier.size(20.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )

                Text(
                    text =
                        "Este formulario es para dudas o sugerencias sobre Familias que Suman+.",
                    modifier =
                        Modifier.weight(1f),
                    style =
                        MaterialTheme.typography
                            .bodySmall,
                    color = GrisTexto
                )
            }
        }
    }
}

@Composable
private fun EtiquetaFormulario(
    texto: String
) {

    Text(
        text = texto,
        style =
            MaterialTheme.typography
                .titleSmall,
        color = AzulMarino
    )
}