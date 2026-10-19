package com.example.familiasquesuman.domain

import com.google.gson.annotations.SerializedName

data class EncuestaActividadRequest(
    @SerializedName("experiencia_general")
    val experienciaGeneral: Int,

    @SerializedName("utilidad_valor")
    val utilidadValor: Int,

    @SerializedName("facilidad_participacion")
    val facilidadParticipacion: Int,

    @SerializedName("comentarios")
    val comentarios: String? = null
)