package com.example.familiasquesuman.ui.screens.comunidad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.*
import androidx.compose.foundation.BorderStroke

@Composable
fun ResultadoPublicacionScreen(
    navController: NavHostController,
    fueExitoso: Boolean
) {

    PantallaPrincipalConMenu(
        navController = navController,
        pantallaActual = PantallaPrincipal.INICIO,

        acciones = {
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
                        "Notificaciones", tint = AzulMarino
                )
            }
        }
    ) { paddingInterno ->

        Column(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .background(CremaFondo)
                .padding(18.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {



            Spacer(modifier = Modifier.weight(.35f))
            Surface(
                modifier = Modifier.size(108.dp),
                shape = CircleShape,
                color = AmbarClaro
            ) {

                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector =
                            if (fueExitoso)
                                Icons.Outlined.Send
                            else
                                Icons.Outlined.ErrorOutline,
                        contentDescription = null,
                        modifier = Modifier.size(54.dp),
                        tint =
                            if (fueExitoso)
                                AzulMarino
                            else
                                ColorError
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))
            Text(text =
                    if (fueExitoso)
                        "¡Gracias por compartir!"
                    else
                        "No pudimos enviar tu publicación",
                style = MaterialTheme.typography.headlineSmall,
                color = AzulMarino
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text =
                    if (fueExitoso)
                        "Tu publicación ha sido enviada al equipo para su revisión. Te avisaremos cuando sea aprobada."
                    else
                        "Revisa la información e inténtalo nuevamente.",
                style = MaterialTheme.typography.bodyMedium,

                color = GrisTexto
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (fueExitoso) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Blanco),
                    shape = RoundedCornerShape(20.dp))
                {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) { Text(text = "¿Qué sigue?",
                            style =
                                MaterialTheme
                                    .typography
                                    .titleMedium,
                            color = AzulMarino
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        PasoRevision(
                            icono = Icons.Outlined.Schedule,
                            titulo =
                                "En revisión",
                            descripcion =
                                "Nuestro equipo revisará que tu publicación cumpla con las políticas de la comunidad."
                        )

                        PasoRevision(
                            icono =
                                Icons.Outlined.MailOutline,

                            titulo =
                                "Te avisaremos",

                            descripcion =
                                "Recibirás una notificación cuando sea aprobada o si requiere cambios."
                        )

                        PasoRevision(
                            icono =
                                Icons.Outlined.Groups,

                            titulo =
                                "Publicación",

                            descripcion =
                                "Una vez aprobada, aparecerá en la sección de Comunidad."
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // BOTÓN PRINCIPAL
                Button(
                    onClick = {
                        if (fueExitoso) {
                            navController.navigate(
                                Rutas.MisPublicaciones.ruta
                            ) {
                                popUpTo(Rutas.Comunidad.ruta) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        } else {
                            navController.popBackStack()
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    shape = RoundedCornerShape(16.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Ambar,
                        contentColor = Blanco
                    )
                ) {

                    Text(
                        text =
                            if (fueExitoso)
                                "Ver mis publicaciones"
                            else
                                "Reintentar",

                        style = MaterialTheme.typography.titleSmall
                    )
                }


                // BOTÓN SECUNDARIO
                OutlinedButton(
                    onClick = {

                        val regresoExitoso =
                            navController.popBackStack(
                                Rutas.Comunidad.ruta,
                                inclusive = false
                            )

                        if (!regresoExitoso) {

                            navController.navigate(
                                Rutas.Comunidad.ruta
                            ) {
                                launchSingleTop = true
                            }
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    shape = RoundedCornerShape(16.dp),

                    border = BorderStroke(
                        width = 1.5.dp,
                        color = Ambar
                    ),

                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Blanco,
                        contentColor = Ambar
                    )
                ) {

                    Text(
                        text = "Volver a Comunidad",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun PasoRevision(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    descripcion: String
) {

    Row(
        modifier = Modifier.padding(
            vertical = 8.dp
        )
    ) {

        Surface(
            modifier = Modifier.size(42.dp),
            shape = CircleShape,
            color = AmbarClaro
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = Ambar
                )
            }
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column {

            Text(
                text = titulo,
                style =
                    MaterialTheme.typography.titleSmall,
                color = AzulMarino
            )

            Text(
                text = descripcion,
                style =
                    MaterialTheme.typography.bodySmall,
                color = GrisTexto
            )
        }
    }
}