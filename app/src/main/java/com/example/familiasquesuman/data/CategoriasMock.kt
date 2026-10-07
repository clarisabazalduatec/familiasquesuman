package com.example.familiasquesuman.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.familiasquesuman.data.repository.donacionesMockData

/**
 * Categorías en memoria, mientras no exista el backend.
 * Arrancan con lo que ya hay en los datos de ejemplo; el admin puede agregar más desde el formulario.
 * Cuando haya backend, esto se reemplaza por GET/POST /categorias.
 */
object CategoriasMock {
    val actividades: SnapshotStateList<String> =
        (listOf("Medio Ambiente", "Educación", "Apoyo Social", "Otros") +
                actividadesMockData.mapNotNull { it.categoria })
            .filter { it.isNotBlank() }
            .distinct()
            .toMutableStateList()

    val donaciones: SnapshotStateList<String> =
        donacionesMockData
            .mapNotNull { it.categoria }
            .filter { it.isNotBlank() }
            .distinct()
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
