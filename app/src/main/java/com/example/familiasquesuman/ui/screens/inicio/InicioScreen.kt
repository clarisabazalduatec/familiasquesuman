package com.example.familiasquesuman.ui.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LightbulbCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.ui.components.OpcionMenuPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.components.TarjetaActividadDestacada
import com.example.familiasquesuman.ui.components.TarjetaComunidad
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme

// Sección Tu Impacto
@Composable
fun SeccionTuImpacto(
    numeroActividades: Int,
    horasDonadas: Int,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Tu Impacto",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Has participado en 3 proyectos este mes.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                EstadisticaImpacto(valor = "$numeroActividades", etiqueta = "Actividades")
                EstadisticaImpacto(valor = "$$horasDonadas", etiqueta = "Horas donadas")
            }
        }
    }
}

@Composable
private fun EstadisticaImpacto(
    valor: String,
    etiqueta: String
) {
    Column {
        Text(
            text = valor,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

// Screen principal
@Composable
fun InicioScreen(
    navController: NavHostController
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
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = AzulMarino
                )
            }
        }
    ) { paddingInterno ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingInterno)
                .fillMaxSize()
                .background(CremaFondo),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 2.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SaludoConUbicacion(
                    nombreUsuario = "Mariana",
                    ciudad = "Monterrey, NL"
                )
            }
            item {
                GridMenuPrincipal(
                    opciones = listOf(
                        OpcionMenuPrincipal(
                            titulo = "Actividades en Familia",
                            subtitulo = "Actividades en familia para ayudar durante el año.",
                            icono = Icons.Default.Groups
                        ),
                        OpcionMenuPrincipal(
                            titulo = "Quiero Donar",
                            subtitulo = "Apoyo en especie y tiempo.",
                            icono = Icons.Default.Favorite
                        ),
                        OpcionMenuPrincipal(
                            titulo = "Proyectos",
                            subtitulo = "Proyectos con causas y objetivos específicos.",
                            icono = Icons.Default.LightbulbCircle
                        ),
                        OpcionMenuPrincipal(
                            titulo = "Directorio de Visiteo",
                            subtitulo = "Centros y espacios para visitar y apoyar en familia.",
                            icono = Icons.Default.Place
                        )
                    ),
                    onOpcionClick = { opcion ->
                        val ruta = when (opcion.titulo) {
                            "Actividades en Familia" -> Rutas.Actividades.ruta
                            "Quiero Donar" -> Rutas.Donar.ruta
                            "Proyectos" -> Rutas.Proyectos.ruta
                            "Directorio de Visiteo" -> Rutas.Directorio.ruta
                            else -> Rutas.Inicio.ruta
                        }
                        navController.navigate(ruta)
                    }
                )
            }

            // Tarjeta de comunidad
            item {
                TarjetaComunidad(
                    onVerComunidad = {
                        navController.navigate(Rutas.Comunidad.ruta)
                    },
                    onChatbot = {
                        navController.navigate(Rutas.Chatbot.ruta)
                    }
                )
            }

            //chatbot
            item {
                TarjetaActividadDestacada(
                    titulo = "Chatbot",
                    descripcion = "Habla con nuestro chatbot",
                    icono = Icons.Default.ChatBubble,
                    onClick = {
                        navController.navigate(Rutas.Chatbot.ruta)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InicioScreenPreview() {
    FamiliasQueSumanTheme {
        InicioScreen(
            navController = rememberNavController()
        )
    }
}
