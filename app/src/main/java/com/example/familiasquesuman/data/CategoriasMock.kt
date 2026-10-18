package com.example.familiasquesuman.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList

/**
 * Categorías dinámicas en memoria para los formularios del administrador.
 * Se pueden agregar nuevas categorías en tiempo de ejecución.
 */
object CategoriasMock {
    val actividades: SnapshotStateList<String> =
        (listOf("Medio Ambiente", "Educación", "Apoyo Social", "Otros") +
                actividadesMockData.map { it.categoria })
            .filter { it.isNotBlank() }
            .distinct()
            .toMutableStateList()

    val donaciones: SnapshotStateList<String> =
        listOf("Juguetes", "Ropa", "Alimentos", "Higiene", "Electrónicos", "Salud", "Útiles", "Otros")
            .toMutableStateList()
}

/** Agrega la categoría si no existe (sin distinguir mayúsculas). Devuelve el nombre final, o null si estaba vacío. */
fun agregarCategoria(lista: MutableList<String>, nombre: String): String? {
    val limpio = nombre.trim()
    if (limpio.isEmpty()) return null
    val existente = lista.firstOrNull { it.equals(limpio, ignoreCase = true) }
    if (existente != null) return existente
    lista.add(limpio)
    return limpio
}