package com.example.familiasquesuman.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.TipoDonacion

interface DonacionRepository {
    suspend fun obtenerDonaciones(): List<Donacion>
}

val donacionesMockData = mutableStateListOf(
    Donacion(
        id = "1",
        titulo = "Comedor Solidario Barrio Sur",
        descripcionCorta = "Ayúdanos a asegurar 500 raciones semanales para familias en situación de vulnerabilidad.",
        descripcionLarga = "El Comedor Solidario Barrio Sur atiende a más de 120 familias diariamente. Con tu apoyo, aseguraremos los insumos básicos (arroz, frijol, verduras y proteínas) para brindar comidas calientes durante los próximos 3 meses.\n\nHorario de atención: Lunes a Viernes de 8:00 AM a 4:00 PM.",
        fundacion = "Campaña",
        imagenUrl = "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=600&auto=format&fit=crop",
        tipo = TipoDonacion.CAMPANA,
        recaudado = 3500f,
        meta = 6000f,
        textoProgreso = "$3,500 recaudados",
        telefonoWhatsapp = "528112345678",
        opcionesDisponibles = 3,
        opcionesDetalle = listOf(
            Pair("Despensa básica", "$350"),
            Pair("Apoyo semanal para 1 familia", "$700"),
            Pair("Insumos para el comedor (1 día)", "$2,500")
        ),
        comoAyudar = "Cada aporte va directo a la compra de víveres al mayoreo para estirar cada peso al máximo."
    ),
    Donacion(
        id = "2",
        titulo = "Uniformes Escolares Comunitarios",
        descripcionCorta = "Dona para equipar con uniformes a 100 estudiantes de primaria en zonas rurales.",
        descripcionLarga = "Buscamos proveer un kit completo (pantalón/falda, camisa, suéter y zapatos) a 100 niños y niñas para que la falta de uniforme no sea un impedimento en su educación.\n\nTrabajamos directamente con maquiladoras locales para conseguir el mejor precio y reactivar la economía del municipio.",
        fundacion = "Campaña",
        imagenUrl = "https://images.unsplash.com/photo-1503676260728-1c00da094a0b?q=80&w=600&auto=format&fit=crop",
        tipo = TipoDonacion.CAMPANA,
        recaudado = 15000f,
        meta = 50000f,
        textoProgreso = "$15,000 recaudados",
        telefonoWhatsapp = "528112345678",
        opcionesDisponibles = null,
        opcionesDetalle = null,
        comoAyudar = "Súmate a esta causa y viste a un niño de oportunidades para este nuevo ciclo escolar."
    ),
    Donacion(
        id = "3",
        titulo = "Tablets para Aprender",
        descripcionCorta = "Equipamiento del aula digital de la Escuela No. 4 para que la clase completa pueda aprender y crecer.",
        descripcionLarga = "",
        fundacion = "Escuela San Bernabé",
        imagenUrl = "https://images.unsplash.com/photo-1561154464-82e9adf32764?q=80&w=600&auto=format&fit=crop",
        tipo = TipoDonacion.ESPECIE,
        categoria = "Electrónicos",
        direccionCompleta = "Av. Alfonso Reyes 100, Zona Poniente, Monterrey, N.L.",
        categoriasEspecie = listOf("Tablets", "Cargadores", "Electrónicos"),
        destinatarios = listOf("Estudiantes", "Niños"),
        condicionesArticulos = listOf("Nuevo", "Usado en buen estado"),
        metodosEntrega = listOf("Entrega en el centro", "Recolección"),
        condicionesRecepcion = "Pantalla intacta, Wi-Fi funcional y de preferencia cargador incluido.",
        telefonoWhatsapp = "8110809078"
    ),
    Donacion(
        id = "4",
        titulo = "Abrigos y Cobijas de Invierno",
        descripcionCorta = "Colecta de prendas abrigadoras y cobijas limpias para apoyar a familias y adultos mayores del sector.",
        descripcionLarga = "",
        fundacion = "Comedor San José",
        imagenUrl = "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?q=80&w=600&auto=format&fit=crop",
        tipo = TipoDonacion.ESPECIE,
        categoria = "Ropa",
        direccionCompleta = "Av. Aztlán 2304, Col. San Bernabé, Monterrey, N.L.",
        categoriasEspecie = listOf("Ropa de Invierno", "Cobijas", "Calzado"),
        destinatarios = listOf("Familias", "Adultos Mayores", "Niños"),
        condicionesArticulos = listOf("Nuevo", "Usado en buen estado"),
        metodosEntrega = listOf("Entrega en el centro"),
        condicionesRecepcion = "Entregar prendas limpias, desinfectadas y en bolsas cerradas.",
        telefonoWhatsapp = "8181234567"
    )
)

fun agregarDonacionMock(donacion: Donacion) {
    donacionesMockData.add(0, donacion)
}

fun registrarAporteDonacion(id: String, cantidad: Float) {
    val index = donacionesMockData.indexOfFirst { it.id == id }
    if (index != -1) {
        val vieja = donacionesMockData[index]
        val nuevoRecaudado = vieja.recaudado + cantidad
        val nuevoTextoProgreso = if (vieja.tipo == TipoDonacion.CAMPANA) {
            "$${nuevoRecaudado.toInt()} recaudados"
        } else {
            "${nuevoRecaudado.toInt()} entregadas"
        }
        donacionesMockData[index] = vieja.copy(
            recaudado = nuevoRecaudado,
            textoProgreso = nuevoTextoProgreso
        )
    }
}

class DonacionRepositoryFake : DonacionRepository {
    override suspend fun obtenerDonaciones(): List<Donacion> {
        return donacionesMockData
    }
}
