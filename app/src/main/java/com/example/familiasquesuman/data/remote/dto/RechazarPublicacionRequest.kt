package com.example.familiasquesuman.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RechazarPublicacionRequest(
    @SerializedName("motivo_rechazo")
    val motivoRechazo: String
)