package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CrearAvisoOficialRequest(
    val titulo: String?,
    val contenido: String,
    val ubicacion: String? = null,
    val imagenes: List<String> = emptyList(),

    @SerializedName("evento_inicia_en")
    val eventoIniciaEn: String? = null,

    @SerializedName("evento_termina_en")
    val eventoTerminaEn: String? = null,

    @SerializedName("publico_objetivo")
    val publicoObjetivo: String? = null,


    @SerializedName("documento_url")
    val documentoUrl: String? = null,

    @SerializedName("documento_nombre")
    val documentoNombre: String? = null

)
