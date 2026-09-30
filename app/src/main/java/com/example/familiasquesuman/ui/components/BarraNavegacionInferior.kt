package com.example.familiasquesuman.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familiasquesuman.ui.theme.Ambar
import com.example.familiasquesuman.ui.theme.AzulMarino
import com.example.familiasquesuman.ui.theme.CremaFondo


enum class PantallaPrincipal(
    val etiqueta: String
) {
    INICIO("Inicio"),
    ACTIVIDADES("Actividades"),
    PROYECTOS("Proyectos"),
    DONAR("Donar"),
    DIRECTORIO("Directorio"),
    CHATBOT("Asistente"),
    PERFIL("Perfil")
}

@Composable
fun BarraNavegacionInferior(
    pantallaActual: PantallaPrincipal,
    onPantallaSeleccionada: (PantallaPrincipal) -> Unit
) {

    // Azul ligeramente suavizado como el mockup.
    val azulNavbar = AzulMarino.copy(alpha = 0.72f)

    val colores = NavigationBarItemDefaults.colors(

        // SELECCIONADO
        selectedIconColor = Ambar,
        selectedTextColor = Ambar,

        // Quitamos la burbuja detrás del seleccionado
        indicatorColor = Color.Transparent,

        // NO SELECCIONADO
        unselectedIconColor = azulNavbar,
        unselectedTextColor = azulNavbar
    )


    NavigationBar(
        modifier = Modifier.height(90.dp),
        containerColor = CremaFondo,
        tonalElevation = 0.dp
    ) {

        //inicio
        NavigationBarItem(
            selected =
                pantallaActual ==
                        PantallaPrincipal.INICIO,
            onClick = {
                onPantallaSeleccionada(
                    PantallaPrincipal.INICIO
                )
            },
            icon = {
                Icon(imageVector =Icons.Outlined.Home,
                    contentDescription = "Inicio",
                    modifier =Modifier.size(24.dp)
                )
            },

            label = {
                Text(
                    text = "Inicio",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight =
                        if (
                            pantallaActual ==
                            PantallaPrincipal.INICIO
                        ) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        }
                )
            },

            colors = colores
        )

        //actividades
        NavigationBarItem(
            selected =
                pantallaActual ==
                        PantallaPrincipal.ACTIVIDADES,
            onClick = {
                onPantallaSeleccionada(
                    PantallaPrincipal.ACTIVIDADES
                )
            },

            icon = {
                Icon(
                    imageVector =
                        Icons.Outlined.Groups,
                    contentDescription =
                        "Actividades",
                    modifier =
                        Modifier.size(24.dp)
                )
            },

            label = {
                Text(
                    text = "Actividades",
                    style =
                        MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight =
                        if (
                            pantallaActual ==
                            PantallaPrincipal.ACTIVIDADES
                        ) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        }
                )
            },

            colors = colores
        )


        //proyectos
        NavigationBarItem(
            selected =
                pantallaActual ==
                        PantallaPrincipal.PROYECTOS,
            onClick = {
                onPantallaSeleccionada(
                    PantallaPrincipal.PROYECTOS
                )
            },

            icon = {
                Icon(
                    imageVector =Icons.Outlined.Lightbulb,
                    contentDescription =
                        "Proyectos",
                    modifier =Modifier.size(24.dp)
                )
            },

            label = {
                Text(
                    text = "Proyectos",
                    style =MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight =
                        if (
                            pantallaActual ==
                            PantallaPrincipal.PROYECTOS
                        ) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        }
                )
            },
            colors = colores
        )


        //donar
        NavigationBarItem(
            selected =
                pantallaActual ==
                        PantallaPrincipal.DONAR,
            onClick = {
                onPantallaSeleccionada(
                    PantallaPrincipal.DONAR
                )
            },

            icon = {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription =
                        "Donar",
                    modifier = Modifier.size(24.dp)
                )
            },

            label = {
                Text(
                    text = "Donar",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight =
                        if (
                            pantallaActual ==
                            PantallaPrincipal.DONAR
                        ) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        }
                )
            },
            colors = colores
        )


        //directorio
        NavigationBarItem(
            selected =
                pantallaActual ==
                        PantallaPrincipal.DIRECTORIO,
            onClick = {
                onPantallaSeleccionada(
                    PantallaPrincipal.DIRECTORIO
                )
            },

            icon = {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription =
                        "Directorio",
                    modifier = Modifier.size(24.dp)
                )
            },

            label = {
                Text(
                    text = "Directorio",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight =
                        if (
                            pantallaActual ==
                            PantallaPrincipal.DIRECTORIO
                        ) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        }
                )
            },
            colors = colores
        )
    }
}