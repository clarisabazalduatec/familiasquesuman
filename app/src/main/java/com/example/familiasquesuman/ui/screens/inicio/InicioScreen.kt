package com.example.familiasquesuman.ui.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LightbulbCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.familiasquesuman.ui.components.PantallaPrincipal
import com.example.familiasquesuman.ui.components.PantallaPrincipalConMenu
import com.example.familiasquesuman.ui.navigation.Rutas
import com.example.familiasquesuman.ui.theme.AmbarClaro
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.Blanco
import com.example.familiasquesuman.ui.theme.CremaFondo
import com.example.familiasquesuman.ui.theme.FamiliasQueSumanTheme
import com.example.familiasquesuman.ui.theme.GrisTexto
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import com.example.familiasquesuman.R



@Composable
fun TarjetaActividadDestacada(
    categoria: String,
    titulo: String,
    descripcion: String,
    fecha: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = modifier.fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            AssistChip(
                onClick = {},
                label = {
                    Text(categoria)
                },
                enabled = false
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = AzulMarino
            )
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = GrisTexto
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = fecha,
                style = MaterialTheme.typography.labelSmall,
                color = AzulMarino
            )
        }
    }
}


//tu impacto
@Composable
fun SeccionTuImpacto(
    numeroActividades: Int,
    horasDonadas: Int,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary),
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

//screen principal
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
                            subtitulo =
                                "Actividades en familia para ayudar durante el año.",
                            icono = Icons.Default.Groups
                        ),
                        OpcionMenuPrincipal(
                            titulo = "Quiero Donar",
                            subtitulo =
                                "Apoyo en especie y tiempo.",
                            icono = Icons.Default.Favorite
                        ),
                        OpcionMenuPrincipal(
                            titulo = "Proyectos",
                            subtitulo =
                                "Proyectos con causas y objetivos específicos.",
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
                            "Actividades en Familia" ->
                                Rutas.Actividades.ruta
                            "Quiero Donar" ->
                                Rutas.Donar.ruta
                            "Proyectos" ->
                                Rutas.Proyectos.ruta
                            "Directorio de Visiteo" ->
                                Rutas.Directorio.ruta
                            else ->
                                Rutas.Inicio.ruta
                        }
                        navController.navigate(ruta)
                    }
                )
            }

            //comunidad
            item {
                TarjetaComunidadInicio(
                    onVerComunidad = {
                        navController.navigate(
                            Rutas.Comunidad.ruta)
                    },
                    onChatbot = {
                        navController.navigate(
                            Rutas.Chatbot.ruta
                        )
                    }
                )
            }

            item {
                TarjetaActividadDestacada(
                    categoria = "Educación",
                    titulo = "Lectura para Niños",
                    descripcion =
                        "Apoya como voluntario en el círculo de lectura comunitaria.",
                    fecha = "Sábado, 10:00 AM",
                    onClick = {}
                )
            }

            item {
                SeccionTuImpacto(
                    numeroActividades = 12,
                    horasDonadas = 500
                )
            }
        }
    }
}


//comunidad
@Composable
private fun TarjetaComunidadInicio(
    onVerComunidad: () -> Unit,
    onChatbot: () -> Unit
) {

    var pestanaSeleccionada by remember { mutableStateOf("Oficial") }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(
                            Color(0xFFFBF5E4)
                        ),

                    contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = AzulMarino,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Comunidad",
                        style = MaterialTheme.typography.titleLarge,
                        color = AzulMarino
                    )
                    Text(
                        text =
                            "Consulta avisos oficiales y\n" +
                                    "comparte experiencias de otras familias.",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrisTexto
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))


            //comunidad (oficial)
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        pestanaSeleccionada = "Oficial"
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        bottomStart = 20.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (pestanaSeleccionada == "Oficial") {
                                AzulMarino
                            } else {
                                Blanco
                            },
                        contentColor =
                            if (pestanaSeleccionada == "Oficial") {
                                Blanco
                            } else {
                                AzulMarino
                            }
                    )
                ) {
                    Text(
                        text = "Oficial",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                OutlinedButton(
                    onClick = {
                        pestanaSeleccionada = "Comunidad"
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 20.dp,
                        bottomEnd = 20.dp
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor =
                            if (pestanaSeleccionada == "Comunidad") {
                                AzulMarino
                            } else {
                                Blanco
                            },
                        contentColor =
                            if (pestanaSeleccionada == "Comunidad") {
                                Blanco
                            } else {
                                AzulMarino
                            }
                    )
                ) {
                    Text(
                        text = "Comunidad",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }


            Spacer(modifier = Modifier.height(14.dp))


            //contenido de la pestaña
            if (pestanaSeleccionada == "Oficial") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // foto temporal
                    Image(
                        painter = painterResource(
                            id = R.drawable.parque_comunidad
                        ),
                        contentDescription = "Reforestación Parque Central",
                        modifier = Modifier
                            .width(125.dp)
                            .height(105.dp)
                            .clip(
                                RoundedCornerShape(14.dp)
                            ),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Outlined.Campaign,
                                contentDescription = null,
                                tint = AzulMarino,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Anuncio oficial",
                                style =
                                    MaterialTheme.typography.labelSmall,
                                color = AzulMarino
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Cambio de horario:",
                            style =
                                MaterialTheme.typography.titleSmall,
                            color = AzulMarino
                        )
                        Text(
                            text =
                                "Reforestación Parque Central\n" +
                                        "iniciará a las 9:00 AM.",
                            style =
                                MaterialTheme.typography.bodyMedium,
                            color = GrisTexto
                        )
                    }
                }

            } else {
                Text(
                    text =
                        "Conoce experiencias y publicaciones " +
                                "compartidas por otras familias.",
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color = GrisTexto,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 28.dp
                        )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = onVerComunidad
                ) {
                    Text(
                        text = "Ver comunidad",
                        style =
                            MaterialTheme.typography.titleSmall,
                        color = AzulMarino
                    )
                    Text(
                        text = "  ›",
                        style =
                            MaterialTheme.typography.titleLarge,
                        color = AzulMarino
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}


@Preview(
    showBackground = true
)
@Composable
private fun InicioScreenPreview() {

    FamiliasQueSumanTheme {

        InicioScreen(
            navController =
                rememberNavController()
        )
    }
}