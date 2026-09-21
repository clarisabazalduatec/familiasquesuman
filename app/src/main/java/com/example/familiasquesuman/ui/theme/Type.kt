package com.example.familiasquesuman.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Fuente serif del sistema como punto de partida — se ve muy cercana a la de tu diseño.
// Si más adelante quieren la tipografía EXACTA de la marca (ej. "Playfair Display"),
// se agrega como font resource — te explico cómo abajo.
private val FuenteSerifMarca = FontFamily.Serif
private val FuenteSansMarca = FontFamily.SansSerif

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FuenteSerifMarca,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FuenteSerifMarca,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FuenteSerifMarca,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FuenteSansMarca,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FuenteSansMarca,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FuenteSansMarca,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FuenteSansMarca,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FuenteSansMarca,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FuenteSansMarca,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    ),
)