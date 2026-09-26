package com.example.familiasquesuman.data.repository

import com.example.familiasquesuman.domain.Donacion
import com.example.familiasquesuman.domain.TipoDonacion

interface DonacionRepository {
    suspend fun obtenerDonaciones(): List<Donacion>
}

class DonacionRepositoryFake : DonacionRepository {
    override suspend fun obtenerDonaciones(): List<Donacion> {
        return listOf(
            Donacion(
                id = "1",
                titulo = "Comedor Solidario Barrio Sur",
                descripcionCorta = "Ayúdanos a asegurar 500 raciones semanales para familias en situación de vulnerabilidad.",
                // --- TEXTO DUMMY REALISTA ---
                descripcionLarga = "El Comedor Solidario Barrio Sur atiende a más de 120 familias diariamente. Con tu apoyo, podremos asegurar los insumos necesarios (arroz, frijol, verduras y proteínas) para seguir brindando comidas calientes durante los próximos 3 meses.\n\nHorarios de atención de la fundación: Lunes a Viernes de 8:00 AM a 4:00 PM.\n\n¡Cada aporte monetario va directo a la compra de víveres al mayoreo, lo que nos permite estirar cada peso al máximo y seguir transformando la realidad de nuestra comunidad!",
                fundacion = "Alimentación",
                imagenUrl = "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?q=80&w=600&auto=format&fit=crop",
                tipo = TipoDonacion.CAMPANA,
                recaudado = 3500f,
                meta = 6000f,
                textoProgreso = "$3,500 recaudados",
                telefonoWhatsapp = "528112345678"
            ),
            Donacion(
                id = "2",
                titulo = "Tablets para Aprender",
                descripcionCorta = "Faltan 20 dispositivos para que la clase completa de la Escuela No. 4 pueda aprender y crecer.",
                // --- TEXTO DUMMY REALISTA ---
                descripcionLarga = "Nuestra meta es equipar el aula digital de la Escuela No. 4, ubicada en la zona poniente de la ciudad. Actualmente, los alumnos comparten 5 equipos antiguos, lo que dificulta su aprendizaje en herramientas tecnológicas básicas.\n\nLas tablets pueden ser usadas, pero te pedimos que estén en buenas condiciones, con la pantalla intacta, que funcionen al conectarse al Wi-Fi y que de preferencia incluyan su cargador.\n\nPuedes llevarlas directamente a nuestro centro de acopio o enviarnos un mensaje para coordinar la recolección.",
                fundacion = "Educación",
                imagenUrl = "https://images.unsplash.com/photo-1503676260728-1c00da094a0b?q=80&w=600&auto=format&fit=crop",
                tipo = TipoDonacion.ARTICULO,
                categoria = "Electrónicos",
                recaudado = 15f,
                meta = 35f,
                textoProgreso = "15 entregadas",
                condiciones = "Nuevo o buen estado. Con cargador incluido.",
                ubicacion = "19.432608,-99.133209"
            )
        )
    }
}