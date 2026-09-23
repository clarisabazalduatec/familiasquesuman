package com.example.familiasquesuman.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// Modelo de datos para las actividades
data class ActividadMock(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val iconoCategoria: ImageVector,
    val colorCategoria: Color,
    val textColorCategoria: Color,
    val fecha: String,
    val horario: String,
    val ubicacion: String,
    val lugaresDisponibles: Int,
    val participantesAdicionales: Int
)

val actividadesMockData = listOf(
    ActividadMock(
        id = 1,
        titulo = "Reforestación Parque Central",
        descripcion = "Únete a la jornada de plantación de árboles nativos.",
        categoria = "Medio Ambiente",
        iconoCategoria = Icons.Default.Star, 
        colorCategoria = Color(0xFFE5F0E6),
        textColorCategoria = Color(0xFF2E7D32),
        fecha = "14 de mayo",
        horario = "8:00 AM - 12:00 PM",
        ubicacion = "Parque Central",
        lugaresDisponibles = 24,
        participantesAdicionales = 12
    ),
    ActividadMock(
        id = 2,
        titulo = "Lectura para Niños",
        descripcion = "Apoya como voluntario en el círculo de lectura comunitaria.",
        categoria = "Educación",
        iconoCategoria = Icons.Default.Info, 
        colorCategoria = Color(0xFFE5EEFF),
        textColorCategoria = Color(0xFF1565C0),
        fecha = "14 de mayo",
        horario = "10:00 AM - 12:00 PM",
        ubicacion = "Biblioteca Norte",
        lugaresDisponibles = 15,
        participantesAdicionales = 7
    ),
    ActividadMock(
        id = 3,
        titulo = "Comedor Solidario",
        descripcion = "Colabora en la preparación y entrega de alimentos a quienes más lo necesitan.",
        categoria = "Apoyo Social",
        iconoCategoria = Icons.Default.Favorite, 
        colorCategoria = Color(0xFFFDF0D5),
        textColorCategoria = Color(0xFFE65100),
        fecha = "14 de mayo",
        horario = "4:00 PM - 7:00 PM",
        ubicacion = "Centro Comunitario",
        lugaresDisponibles = 18,
        participantesAdicionales = 0
    )
)