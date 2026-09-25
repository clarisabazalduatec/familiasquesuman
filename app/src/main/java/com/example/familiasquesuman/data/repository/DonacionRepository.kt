package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.TipoDonacion

// La pantalla solo conocerá esto
interface DonacionRepository {
    suspend fun obtenerDonaciones(): List<Donacion>
}

// Almacén de datos falsos por el momento
class DonacionRepositoryFake : DonacionRepository {
    override suspend fun obtenerDonaciones(): List<Donacion> {
        return listOf(
            Donacion(
                id = "1",
                titulo = "Comedor Solidario Barrio Sur",
                descripcionCorta = "Ayúdanos a asegurar 500 raciones semanales para familias en situación de vulnerabilidad.",
                descripcionLarga = "Aquí irá todo el detalle de la fundación, horarios y cómo se usarán los fondos...",
                fundacion = "Alimentación",
                imagenUrl = "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=600&auto=format&fit=crop", // Imagen de muestra
                tipo = TipoDonacion.CAMPANA,
                recaudado = 3500f,
                meta = 6000f,
                textoProgreso = "$3,500 recaudados",
                telefonoWhatsapp = "528112345678" // Número sin el +
            ),
            Donacion(
                id = "2",
                titulo = "Tablets para Aprender",
                descripcionCorta = "Faltan 20 dispositivos para que la clase completa de la Escuela No. 4 pueda aprender y crecer.",
                descripcionLarga = "Las tablets serán entregadas a niños de primaria. Buscamos equipos que funcionen correctamente y tengan cargador.",
                fundacion = "Educación",
                imagenUrl = "https://images.unsplash.com/photo-1503676260728-1c00da094a0b?q=80&w=600&auto=format&fit=crop", // Imagen de muestra
                tipo = TipoDonacion.ARTICULO,
                categoria = "Electrónicos",
                recaudado = 15f,
                meta = 35f,
                textoProgreso = "15 entregadas",
                condiciones = "Nuevo o buen estado",
                ubicacion = "19.432608,-99.133209" // Coordenadas para Google Maps
            )
        )
    }
}