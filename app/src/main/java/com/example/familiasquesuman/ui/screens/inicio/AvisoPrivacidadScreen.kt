
package com.example.familiasquesuman.ui.screens.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.GrisTexto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvisoPrivacidadScreen(
    navController: NavHostController
) {
    Scaffold(
        containerColor = CremaFondo,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Aviso de Privacidad",
                        color = AzulMarino,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Regresar",
                            tint = AzulMarino
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = CremaFondo
                )
            )
        }
    ) { paddingInterno ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 16.dp,
                bottom = 36.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Aviso de Privacidad Integral",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AzulMarino
                )
            }

            item {
                Text(
                    text = "Última actualización: julio de 2026",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTexto
                )
            }

            item {
                ParrafoAviso(
                    "En cumplimiento con la legislación mexicana aplicable " +
                            "en materia de protección de datos personales, " +
                            "[Nombre legal de la Asociación Civil], en adelante " +
                            "\"Familias que Suman\", con domicilio en [Monterrey], " +
                            "pone a disposición este aviso para informar cómo " +
                            "recaba, utiliza, protege y trata los datos personales."
                )
            }

            item {
                SeccionAviso(
                    "1. Responsable del tratamiento",
                    "Familias que Suman es responsable del tratamiento " +
                            "de los datos personales que recaba y se compromete " +
                            "a proteger su privacidad mediante medidas " +
                            "administrativas, técnicas y físicas razonables.\n\n" +
                            "Contacto: hola@familiasquesuman.com"
                )
            }

            item {
                SeccionAviso(
                    "2. Datos personales que podemos recabar",
                    "Dependiendo de tu relación con Familias que Suman, " +
                            "podremos solicitar nombre completo, teléfono, correo " +
                            "electrónico, ciudad o municipio, información de " +
                            "participación como voluntario o donante, así como " +
                            "nombre y edad de menores cuando sea necesario.\n\n" +
                            "También podrán recabarse fotografías y videos " +
                            "de actividades cuando exista la autorización " +
                            "correspondiente, y la información que proporciones " +
                            "en formularios o comunicaciones.\n\n" +
                            "Los datos sensibles no se solicitan habitualmente. " +
                            "Si fueran necesarios, se solicitará el " +
                            "consentimiento correspondiente."
                )
            }

            item {
                SeccionAviso(
                    "3. Finalidades del tratamiento",
                    "Los datos se utilizan para registrar participantes, " +
                            "coordinar actividades y donaciones, gestionar " +
                            "inscripciones, contactar a voluntarios e instituciones, " +
                            "informar cambios y atender solicitudes.\n\n" +
                            "Como finalidades secundarias, se podrá enviar " +
                            "información de campañas, eventos y resultados, " +
                            "así como elaborar estadísticas internas.\n\n" +
                            "Si no deseas el uso de tus datos para estas " +
                            "finalidades secundarias, puedes escribir a " +
                            "hola@familiasquesuman.com."
                )
            }

            item {
                SeccionAviso(
                    "4. Fotografías y videos",
                    "Durante las actividades podrán tomarse fotografías " +
                            "y videos con fines informativos, institucionales " +
                            "o de difusión. Se solicitarán las autorizaciones " +
                            "correspondientes cuando sea necesario.\n\n" +
                            "Las fotografías de beneficiarios también estarán " +
                            "sujetas a las políticas de las instituciones participantes."
                )
            }

            item {
                SeccionAviso(
                    "5. Transferencia de datos personales",
                    "Familias que Suman podrá compartir únicamente " +
                            "la información necesaria con instituciones " +
                            "participantes para coordinar actividades.\n\n" +
                            "Los datos no serán vendidos ni comercializados, " +
                            "y las transferencias se limitarán a las " +
                            "permitidas por la legislación aplicable " +
                            "o autorizadas por la persona titular."
                )
            }

            item {
                SeccionAviso(
                    "6. Derechos ARCO",
                    "Puedes ejercer tus derechos de Acceso, " +
                            "Rectificación, Cancelación y Oposición, " +
                            "así como revocar tu consentimiento cuando " +
                            "legalmente proceda.\n\n" +
                            "Envía tu solicitud a hola@familiasquesuman.com, " +
                            "incluyendo nombre completo, medio de respuesta, " +
                            "derecho que deseas ejercer y, cuando corresponda, " +
                            "documentos para acreditar tu identidad."
                )
            }

            item {
                SeccionAviso(
                    "7. Cookies y tecnologías similares",
                    "El sitio web podrá utilizar cookies u otras " +
                            "tecnologías para mejorar la navegación, " +
                            "obtener estadísticas y optimizar " +
                            "el funcionamiento de la plataforma.\n\n" +
                            "Estas tecnologías podrán administrarse desde " +
                            "la configuración del navegador, según corresponda."
                )
            }

            item {
                SeccionAviso(
                    "8. Conservación y seguridad",
                    "Los datos personales se conservarán durante " +
                            "el tiempo necesario para cumplir las finalidades " +
                            "descritas. Familias que Suman adoptará medidas " +
                            "razonables para protegerlos contra accesos, " +
                            "usos o divulgaciones no autorizados."
                )
            }

            item {
                SeccionAviso(
                    "9. Menores de edad",
                    "Cuando sea necesario gestionar la participación " +
                            "de menores, sus datos deberán ser proporcionados " +
                            "por quien ejerza la patria potestad o tutela " +
                            "y se utilizarán para las finalidades " +
                            "relacionadas con la actividad correspondiente."
                )
            }

            item {
                SeccionAviso(
                    "10. Cambios al aviso",
                    "Familias que Suman podrá modificar este documento " +
                            "por cambios legales, operativos o internos.\n\n" +
                            "Las modificaciones serán publicadas en " +
                            "www.familiasquesuman.com."
                )
            }

            item {
                SeccionAviso(
                    "11. Contacto",
                    "Si tienes dudas sobre el tratamiento de tus " +
                            "datos personales, escríbenos a:\n\n" +
                            "hola@familiasquesuman.com"
                )
            }

            item {
                HorizontalDivider(
                    color = AzulMarino.copy(alpha = 0.15f)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Gracias por confiar en Familias que Suman.",
                    color = AzulMarino,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun SeccionAviso(
    titulo: String,
    contenido: String
) {
    Column {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AzulMarino
        )

        Spacer(modifier = Modifier.height(8.dp))

        ParrafoAviso(contenido)
    }
}

@Composable
private fun ParrafoAviso(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodyMedium,
        color = AzulMarino
    )
}
