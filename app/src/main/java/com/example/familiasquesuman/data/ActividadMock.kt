package com.example.familiasquesuman.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.familiasquesuman.ui.theme.FondoAzulClaro
import com.example.familiasquesuman.ui.theme.FondoNaranja
import com.example.familiasquesuman.ui.theme.FondoVerde
import com.example.familiasquesuman.ui.theme.TextoAzul
import com.example.familiasquesuman.ui.theme.TextoNaranja
import com.example.familiasquesuman.ui.theme.TextoVerde

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
    val distancia: String,
    val organizador: String,
    val organizadorVerificado: Boolean,
    val acercaDe: String,
    val lugaresDisponibles: Int,
    val lugaresTotales: Int,
    val participantesAdicionales: Int
)

val actividadesMockData = mutableStateListOf(
    ActividadMock(
        id = 1,
        titulo = "Reforestación Parque Central",
        descripcion = "Únete a la jornada de plantación de árboles nativos.",
        categoria = "Medio Ambiente",
        iconoCategoria = Icons.Default.Star, 
        colorCategoria = FondoVerde,
        textColorCategoria = TextoVerde,
        fecha = "Sáb, 24 de Oct",
        horario = "09:00 AM - 13:00 PM",
        ubicacion = "Parque Central, Puerta Norte",
        distancia = "A 2.5 km de ti",
        organizador = "Fundación Verde Vivo",
        organizadorVerificado = true,
        acercaDe = "Únete a nosotros para restaurar el pulmón de nuestra ciudad. Durante esta jornada, las familias plantarán árboles autóctonos y aprenderán sobre la importancia de la biodiversidad local. Es una excelente oportunidad para enseñar a los más pequeños sobre el cuidado del medio ambiente mediante la acción directa.",
        lugaresDisponibles = 15,
        lugaresTotales = 50,
        participantesAdicionales = 12
    ),
    ActividadMock(
        id = 2,
        titulo = "Lectura para Niños",
        descripcion = "Apoya como voluntario en el círculo de lectura comunitaria.",
        categoria = "Educación",
        iconoCategoria = Icons.Default.Info, 
        colorCategoria = FondoAzulClaro,
        textColorCategoria = TextoAzul,
        fecha = "Dom, 25 de Oct",
        horario = "10:00 AM - 12:00 PM",
        ubicacion = "Biblioteca Norte",
        distancia = "A 5.0 km de ti",
        organizador = "Círculo de Lectores",
        organizadorVerificado = true,
        acercaDe = "Apoya como voluntario leyendo cuentos a niños de la comunidad. Ayudaremos a fomentar el hábito de la lectura y la imaginación en los más pequeños.",
        lugaresDisponibles = 5,
        lugaresTotales = 20,
        participantesAdicionales = 7
    ),
    ActividadMock(
        id = 3,
        titulo = "Comedor Solidario",
        descripcion = "Colabora en la preparación y entrega de alimentos a quienes más lo necesitan.",
        categoria = "Apoyo Social",
        iconoCategoria = Icons.Default.Favorite, 
        colorCategoria = FondoNaranja,
        textColorCategoria = TextoNaranja,
        fecha = "Mié, 28 de Oct",
        horario = "16:00 PM - 19:00 PM",
        ubicacion = "Centro Comunitario",
        distancia = "A 1.2 km de ti",
        organizador = "Manos Amigas",
        organizadorVerificado = false,
        acercaDe = "Colabora en la preparación y entrega de alimentos a quienes más lo necesitan en nuestra comunidad. Toda ayuda es bienvenida para servir cenas calientes.",
        lugaresDisponibles = 18,
        lugaresTotales = 30,
        participantesAdicionales = 0
    )
)

fun agregarActividadMock(actividad: ActividadMock) {
    actividadesMockData.add(0, actividad)
}
