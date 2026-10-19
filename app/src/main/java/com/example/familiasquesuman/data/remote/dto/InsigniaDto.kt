package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName

data class InsigniaDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("actividad_id")
    val actividadId: String,

    @SerializedName("titulo_actividad")
    val tituloActividad: String,

    @SerializedName("categoria")
    val categoria: String? = "General",

    @SerializedName("fecha_otorgada")
    val fechaOtorgada: String? = null
)