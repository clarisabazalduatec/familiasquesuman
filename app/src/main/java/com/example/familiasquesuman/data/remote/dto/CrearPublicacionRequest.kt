package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CrearPublicacionRequest(
    val titulo: String,
    val contenido: String,
    val ubicacion: String? = null,
    @SerializedName("ciudad_id")
    val ciudadId: Int? = null,
    @SerializedName("categoria_id")
    val categoriaId: Int?,
    val imagenes: List<String>
)
